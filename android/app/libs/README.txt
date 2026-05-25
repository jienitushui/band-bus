小米穿戴 XMS Wearable SDK（与 interconnect_dev_test_demo 一致）
============================================================

请将小米穿戴 XMS SDK 的 **.aar** 放到本目录：

  android/app/libs/*.aar

推荐来源：仓库内 **`interconnect_dev_test_demo/libs/xms-wearable-lib_1.4_release.aar`**（与 demo 一致），复制到本目录即可；也可使用《小米穿戴第三方 APP 能力开放接口文档》附件中的同名/更新版本 aar。

放入后重新 Sync / 编译，应用即可通过反射加载 com.xiaomi.xms.wearable.Wearable 与手表快应用 interconnect。

也可额外拷贝到：
  interconnect_dev_test_demo/XMS Wearable Demo/xms-wearable-sdk/app/libs/
（android/app/build.gradle.kts 会同时扫描该路径下的 aar/jar）
