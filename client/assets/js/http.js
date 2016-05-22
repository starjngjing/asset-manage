/**
 * ajax请求全局封装，集中进行errorCode处理
 * amd模块，使用requirejs载入
 */

define({
  /**
   * get
   *
   * 参数url：请求服务器url
   * 参数data：ajax请求所需参数，javascript对象
   *          -- data.contentType：请求数据类型，默认为'application/json'，可设置为'form'兼容旧式form请求
   *          -- data.dataType：返回数据类型，默认为'json'
   *          -- data.data：请求数据体，json格式，默认为空
   * 参数success：请求成功后触发的回调函数
   * 参数failure：请求失败后触发的回调函数
   */
  get: function (url, data, success, failure) {
    baseAjax('get', url, data, success, failure)
  },
  /**
   * post，参数同上
   */
  post: function (url, data, success, failure) {
    baseAjax('post', url, data, success, failure)
  }
})

function baseAjax (type, url, options, success, failure) {
  if (typeof options === 'function') {
    failure = success
    success = options
    options = undefined
  }

  options = options || {}

  $.ajax({
    type: type,
    url: url,
    contentType: (function () {
      switch (options.contentType) {
        case 'form':
          return 'application/x-www-form-urlencoded'
        default:
          return 'application/json'
      }
    })(),
    xhrFields: {
      withCredentials: true
    },
    dataType: options.dataType || 'json',
    data: options.data || '',
    async: options.async || true
  }).then(function (res) {
    if (!res.errorCode) {
      if (success) {
        success(res)
      }
    } else {
      if (failure) {
        failure(res)
      }
      errorHandle(res)
    }
  }, function (err) {
    console.log(err)
  })
}

function errorHandle (err) {
  toastr.error(err.errorMessage, '错误信息', {
    timeOut: 3600000
  })
  switch (err.errorCode) {
    case 10000:
      //location.href = 'login.html'
      break
    default:
      break
  }
  console.log(err)
}
