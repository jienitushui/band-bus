package uno.keyin.bus.wear

import android.content.Context
import android.os.Handler
import android.os.Looper
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy
import java.util.concurrent.atomic.AtomicReference

/**
 * 通过反射调用小米 XMS Wearable SDK，避免在未放入 aar 时编译失败。
 * 将官方 SDK 的 aar 放入 app/libs 后，运行时即可连接小米穿戴服务并与手表快应用 interconnect。
 */
object XmsWearSdkBridge {

    private val main = Handler(Looper.getMainLooper())

    fun isSdkOnClasspath(): Boolean = try {
        Class.forName("com.xiaomi.xms.wearable.Wearable")
        true
    } catch (_: Throwable) {
        false
    }

    private fun runUi(block: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) block()
        else main.post(block)
    }

    private fun wearableClass(): Class<*> = Class.forName("com.xiaomi.xms.wearable.Wearable")

    fun getNodeApi(context: Context): Any? {
        val c = wearableClass()
        val m = c.getMethod("getNodeApi", Context::class.java)
        return m.invoke(null, context.applicationContext)
    }

    fun getMessageApi(context: Context): Any? {
        val c = wearableClass()
        val m = c.getMethod("getMessageApi", Context::class.java)
        return m.invoke(null, context.applicationContext)
    }

    fun getAuthApi(context: Context): Any? {
        val c = wearableClass()
        val m = c.getMethod("getAuthApi", Context::class.java)
        return m.invoke(null, context.applicationContext)
    }

    fun getServiceApi(context: Context): Any? {
        val c = wearableClass()
        val m = c.getMethod("getServiceApi", Context::class.java)
        return m.invoke(null, context.applicationContext)
    }

    private val serviceConnListenerHolder = AtomicReference<Any?>(null)

    /**
     * 监听本应用与「小米穿戴」App 服务的连接（文档 6. 管理服务连接状态）。
     * 服务未连上时，[fetchConnectedNodeIds] 常返回空列表；连上后再刷新节点。
     */
    fun registerWearServiceConnectionListener(
        context: Context,
        onConnected: () -> Unit,
        onDisconnected: () -> Unit,
    ) {
        if (!isSdkOnClasspath()) return
        try {
            unregisterWearServiceConnectionListener(context)
            val iface = Class.forName("com.xiaomi.xms.wearable.service.OnServiceConnectionListener")
            val handler = InvocationHandler { _, method, _ ->
                when (method.name) {
                    "onServiceConnected" -> runUi { onConnected() }
                    "onServiceDisconnected" -> runUi { onDisconnected() }
                }
                null
            }
            val proxy = Proxy.newProxyInstance(iface.classLoader, arrayOf(iface), handler)
            serviceConnListenerHolder.set(proxy)
            val api = getServiceApi(context) ?: return
            val reg = api.javaClass.getMethod("registerServiceConnectionListener", iface)
            reg.invoke(api, proxy)
        } catch (_: Throwable) {
        }
    }

    fun unregisterWearServiceConnectionListener(context: Context) {
        val proxy = serviceConnListenerHolder.getAndSet(null) ?: return
        if (!isSdkOnClasspath()) return
        try {
            val iface = Class.forName("com.xiaomi.xms.wearable.service.OnServiceConnectionListener")
            val api = getServiceApi(context) ?: return
            val un = api.javaClass.getMethod("unregisterServiceConnectionListener", iface)
            un.invoke(api, proxy)
        } catch (_: Throwable) {
        }
    }

    /** 获取已连接节点 id 列表（通常 0 或 1 个） */
    fun fetchConnectedNodeIds(context: Context, onResult: (List<String>) -> Unit, onError: (String) -> Unit) {
        if (!isSdkOnClasspath()) {
            runUi { onError("未检测到 SDK：请将 xms wearable 的 aar 放入 android/app/libs/ 后重新编译") }
            return
        }
        try {
            val nodeApi = getNodeApi(context) ?: run {
                runUi { onError("getNodeApi 返回 null") }
                return
            }
            val task = invokeConnectedNodesTask(nodeApi) ?: run {
                runUi { onError("无法取得 connectedNodes / getConnectedNodes") }
                return
            }
            addTaskSuccess(task) { value ->
                val ids = extractNodeIds(value)
                runUi { onResult(ids) }
            }
            addTaskFailure(task) { e ->
                runUi { onError(e?.message ?: "getConnectedNodes 失败") }
            }
        } catch (e: Throwable) {
            runUi { onError(e.message ?: e.toString()) }
        }
    }

    private fun invokeConnectedNodesTask(nodeApi: Any): Any? {
        val recv = nodeApi.javaClass
        val tryNames = listOf("getConnectedNodes", "connectedNodes")
        for (name in tryNames) {
            val methods = recv.methods.filter { it.name == name && it.parameterCount == 0 }
            for (m in methods) {
                try {
                    return m.invoke(nodeApi)
                } catch (_: Throwable) {
                }
            }
        }
        val field = recv.fields.find { it.name == "connectedNodes" }
        if (field != null) {
            try {
                return field.get(nodeApi)
            } catch (_: Throwable) {
            }
        }
        return null
    }

    @Suppress("UNCHECKED_CAST")
    private fun extractNodeIds(value: Any?): List<String> {
        if (value !is List<*>) return emptyList()
        val out = ArrayList<String>()
        for (item in value) {
            if (item == null) continue
            try {
                val idM = item.javaClass.methods.find { it.name == "getId" && it.parameterCount == 0 }
                val id = idM?.invoke(item) as? String
                if (!id.isNullOrBlank()) {
                    out.add(id)
                    continue
                }
            } catch (_: Throwable) {
            }
            try {
                val f = item.javaClass.getField("id")
                val id = f.get(item) as? String
                if (!id.isNullOrBlank()) out.add(id)
            } catch (_: Throwable) {
            }
        }
        return out
    }

    fun sendTextToNode(context: Context, nodeId: String, text: String, onOk: () -> Unit, onErr: (String) -> Unit) {
        if (!isSdkOnClasspath()) {
            runUi { onErr("未检测到 SDK aar") }
            return
        }
        try {
            val api = getMessageApi(context) ?: run {
                runUi { onErr("getMessageApi null") }
                return
            }
            val m = api.javaClass.getMethod("sendMessage", String::class.java, ByteArray::class.java)
            val task = m.invoke(api, nodeId, text.toByteArray(Charsets.UTF_8)) ?: run {
                runUi { onErr("sendMessage 返回 null") }
                return
            }
            addTaskSuccess(task) { runUi { onOk() } }
            addTaskFailure(task) { e -> runUi { onErr(e?.message ?: "sendMessage 失败") } }
        } catch (e: Throwable) {
            runUi { onErr(e.message ?: e.toString()) }
        }
    }

    private val listenerHolder = AtomicReference<Any?>(null)

    fun registerMessageListener(
        context: Context,
        nodeId: String,
        onBytes: (String, ByteArray) -> Unit,
        onOk: () -> Unit,
        onErr: (String) -> Unit,
    ) {
        if (!isSdkOnClasspath()) {
            runUi { onErr("未检测到 SDK aar") }
            return
        }
        try {
            val iface = Class.forName("com.xiaomi.xms.wearable.message.OnMessageReceivedListener")
            val handler = InvocationHandler { _, method, args ->
                if (method.name == "onMessageReceived" && args != null && args.size >= 2) {
                    val id = args[0] as? String ?: ""
                    val msg = args[1] as? ByteArray ?: ByteArray(0)
                    runUi { onBytes(id, msg) }
                }
                null
            }
            val proxy = Proxy.newProxyInstance(iface.classLoader, arrayOf(iface), handler)
            listenerHolder.set(proxy)

            val api = getMessageApi(context) ?: run {
                runUi { onErr("getMessageApi null") }
                return
            }
            val m = api.javaClass.getMethod("addListener", String::class.java, iface)
            val task = m.invoke(api, nodeId, proxy) ?: run {
                runUi { onErr("addListener 返回 null") }
                return
            }
            addTaskSuccess(task) { runUi { onOk() } }
            addTaskFailure(task) { e -> runUi { onErr(e?.message ?: "addListener 失败") } }
        } catch (e: Throwable) {
            runUi { onErr(e.message ?: e.toString()) }
        }
    }

    fun unregisterMessageListener(context: Context, nodeId: String, onDone: () -> Unit, @Suppress("UNUSED_PARAMETER") onErr: (String) -> Unit) {
        val proxy = listenerHolder.getAndSet(null) ?: run {
            runUi { onDone() }
            return
        }
        if (!isSdkOnClasspath()) {
            runUi { onDone() }
            return
        }
        try {
            val api = getMessageApi(context) ?: run {
                runUi { onDone() }
                return
            }
            val mRemove = api.javaClass.methods.find { it.name == "removeListener" && it.parameterCount == 1 }
                ?: api.javaClass.methods.find { it.name == "removeListener" && it.parameterCount == 2 }
            if (mRemove == null) {
                runUi { onDone() }
                return
            }
            val task = when (mRemove.parameterCount) {
                1 -> mRemove.invoke(api, nodeId)
                2 -> mRemove.invoke(api, nodeId, proxy)
                else -> mRemove.invoke(api, nodeId)
            }
            if (task != null) {
                addTaskSuccess(task) { runUi { onDone() } }
                addTaskFailure(task) { runUi { onDone() } }
            } else runUi { onDone() }
        } catch (_: Throwable) {
            runUi { onDone() }
        }
    }

    fun requestDeviceManagerPermission(
        context: Context,
        nodeId: String,
        onOk: () -> Unit,
        onErr: (String) -> Unit,
    ) {
        if (!isSdkOnClasspath()) {
            runUi { onErr("未检测到 SDK aar") }
            return
        }
        try {
            val permClass = Class.forName("com.xiaomi.xms.wearable.auth.Permission")
            val dm = permClass.getField("DEVICE_MANAGER").get(null)
            val notify = permClass.getField("NOTIFY").get(null)
            val arr = java.lang.reflect.Array.newInstance(permClass, 2)
            java.lang.reflect.Array.set(arr, 0, dm)
            java.lang.reflect.Array.set(arr, 1, notify)

            val auth = getAuthApi(context) ?: run {
                runUi { onErr("getAuthApi null") }
                return
            }
            val m = auth.javaClass.methods.firstOrNull { meth ->
                meth.name == "requestPermission" &&
                    meth.parameterCount == 2 &&
                    meth.parameterTypes[0] == String::class.java &&
                    meth.parameterTypes[1].isArray
            } ?: run {
                runUi { onErr("找不到 requestPermission(String, Permission[])") }
                return
            }

            val task = m.invoke(auth, nodeId, arr) ?: run {
                runUi { onErr("requestPermission 返回 null") }
                return
            }
            addTaskSuccess(task) { runUi { onOk() } }
            addTaskFailure(task) { e -> runUi { onErr(e?.message ?: "申请权限失败") } }
        } catch (e: Throwable) {
            runUi { onErr(e.message ?: e.toString()) }
        }
    }

    private fun addTaskSuccess(task: Any, onSuccess: (Any?) -> Unit) {
        val iface = Class.forName("com.xiaomi.xms.wearable.tasks.OnSuccessListener")
        val proxy = Proxy.newProxyInstance(iface.classLoader, arrayOf(iface)) { _, method, args ->
            if (method.name == "onSuccess") onSuccess(args?.firstOrNull())
            null
        }
        val m = task.javaClass.getMethod("addOnSuccessListener", iface)
        m.invoke(task, proxy)
    }

    private fun addTaskFailure(task: Any, onFailure: (Throwable?) -> Unit) {
        val iface = Class.forName("com.xiaomi.xms.wearable.tasks.OnFailureListener")
        val proxy = Proxy.newProxyInstance(iface.classLoader, arrayOf(iface)) { _, method, args ->
            if (method.name == "onFailure") {
                val ex = args?.firstOrNull() as? Throwable
                onFailure(ex)
            }
            null
        }
        val m = task.javaClass.getMethod("addOnFailureListener", iface)
        m.invoke(task, proxy)
    }
}
