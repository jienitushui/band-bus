import storage from '@system.storage';

function get(param = {}) {
  storage.get({
    key: param.key,
    default: param.default !== undefined ? param.default : '',
    success: param.success,
    fail: param.fail,
    complete: param.complete
  });
}

function set(param = {}) {
  storage.set({
    key: param.key,
    value: param.value,
    success: param.success,
    fail: param.fail,
    complete: param.complete
  });
}

function clear(param = {}) {
  storage.clear({
    success: param.success,
    fail: param.fail,
    complete: param.complete
  });
}

function del(param = {}) {
  storage.delete({
    key: param.key,
    success: param.success,
    fail: param.fail,
    complete: param.complete
  });
}

export default { get, set, clear, delete: del };
