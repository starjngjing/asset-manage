/**
 * 实用工具
 * amd模块，使用requirejs载入
 */

define([
  'config'
],function (config) {
  return {
    /**
     * table操作实用工具
     */
    table: {
      /**
       * 常用 formatter
       */
      formatter: {
        /**
         * 图片url转成缩略图
         */
        urlToThumb: function (val, position) {
          if (val) {
            position = position || 'left'
            return '<a href="http://www.guohuaigroup.com' + val + '" class="thumb-area" target="_blank">' +
                     '<span class="thumb fa fa-picture-o text-light-blue"></span>' +
                     '<img src="http://www.guohuaigroup.com' + val + '" class="thumb-' + position + '" />' +
                   '</a>'
          } else {
            return ''
          }
        },
        /**
         * timestamp转成时间格式字符串
         */
        timestampToDate: function (val, format) {
          return moment(val).format(format)
        },
        /**
         * 数字转成进度条
         */
        numberToProgress: function (val) {
          val = parseInt(val * 100)
          if (val <= 30) {
            return '<div class="progress progress-xs progress-striped active">' +
                     '<div class="progress-bar progress-bar-primary" style="width: ' + val + '%"></div>' +
                   '</div>'
          } else if (val > 30 && val <= 60) {
            return '<div class="progress progress-xs progress-striped active">' +
                     '<div class="progress-bar progress-bar-danger" style="width: ' + val + '%"></div>' +
                   '</div>'
          } else if (val > 60 && val <= 99){
            return '<div class="progress progress-xs progress-striped active">' +
                     '<div class="progress-bar progress-bar-yellow" style="width: ' + val + '%"></div>' +
                   '</div>'
          } else {
            return '<div class="progress progress-xs progress-striped active">' +
                     '<div class="progress-bar progress-bar-success" style="width: ' + val + '%"></div>' +
                   '</div>'
          }
        },
        /**
         * 数字转成百分比显示
         */
        numberToPercentage: function (val) {
          val = parseInt(val * 100)
          if (val <= 30) {
            return '<span class="badge bg-light-blue">' + val + '%</span>'
          } else if (val > 30 && val <= 60) {
            return '<span class="badge bg-red">' + val + '%</span>'
          } else if (val > 60 && val <= 99){
            return '<span class="badge bg-yellow">' + val + '%</span>'
          } else {
            return '<span class="badge bg-green">' + val + '%</span>'
          }
        },
        /**
         * 生成功能按钮
         */
        generateButton: function (arr) {
          var format = '<div class="func-area">'
          arr.forEach(function (item) {
            switch (item.type) {
              case 'button':
                if (item.isRender === undefined || item.isRender) {
                  format += '<button class="btn btn-default btn-xs ' + item.class + '">' + item.text + '</button>'
                }
                break
              case 'buttonGroup':
                if (item.isRender === undefined || item.isRender) {
                  format += '<div class="btn-group">' +
                  '<button class="btn btn-default btn-xs">更多</button>' +
                  '<button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">' +
                  '<span class="caret"></span>' +
                  '<span class="sr-only">Toggle Dropdown</span>' +
                  '</button>' +
                  '<ul class="dropdown-menu dropdown-menu-right" role="menu">'
                  item.sub.forEach(function (sub) {
                    if (sub.isRender === undefined || sub.isRender) {
                      format += '<li><a href="javascript:void(0)" class="' + sub.class + '">' + sub.text + '</a></li>'
                    }
                  })
                  format += '</ul></div>'
                }
                break
            }
          })
          format += '</div>'
          return format
        }
      }
    },
    /**
     * 枚举值操作实用工具
     */
    enum: {
      /**
       * 转换（类似vue/angular中的filter）
       */
      transform: function (enumName, val) {
        var rlt = ''
        config[enumName].forEach(function (item) {
          if (item.id === val) {
            rlt = item.text
          }
        })
        return rlt
      }
    },
    /**
     * 表单操作实用工具
     */
    form: {
      /**
       * 表单验证工具
       */
      validator: {
        init : function (form) {
          form.validator({
            custom: {
              validfloat: this.validfloat,
              validint: this.validint
            },
            errors: {
              validfloat: '数据格式不正确',
              validint: '数据格式不正确'
            }
          })
        },
        /**
         * 浮点数校验，例如 data-validfloat="10.2"，10.2 表示小数点前面10位后面2位，默认前后各10位
         * @param $el 验证表单元素 jquery对象
         * @returns 验证结果 {boolean}
         * FIXME 目前默认值不起作用，框架导致，待修改
         */
        validfloat: function ($el) {
          var value = $el.val().trim()
          var range = $el.attr('data-validfloat') || '10.10'
          var rangeArr = range.split('.')
          var intPart = rangeArr[0]
          var decPart = rangeArr[1]
          var regStr = '^[+-]?\\d{0,' + intPart + '}(\\.\\d{0,' + decPart + '})?$'
          var floatReg = new RegExp(regStr)
          if (!floatReg.test(value)) {
            return false
          } else {
            return true
          }
        },
        /**
         * 整数校验，例如 data-validint="1-1000"，1-1000 表示许可范围从1到1000
         * @param $el 验证表单元素 jquery对象
         * @returns 验证结果 {boolean}
         * FIXME 目前默认值不起作用，框架导致，待修改
         */
        validint: function ($el) {
          var value = $el.val().trim()
          var range = $el.attr('data-validint') || '0-1000000000'
          var rangeArr = range.split('-')
          var start = Number(rangeArr[0])
          var end = Number(rangeArr[1])
          var regStr = '^[+-]?\\d*$'
          var intReg = new RegExp(regStr)
          if (!intReg.test(value)) {
            return false
          } else {
            if (parseInt(value) > end || parseInt(value) < start) {
              return false
            } else {
              return true
            }
          }
        }
      },
      /**
       * 将表单序列化成json对象
       * @param form 表单
       * @returns 列化之后的json对象
       */
      serializeJson: function(form){
        var serializeObj = {}
        var array = $(form).serializeArray()
        var str = $(form).serialize()
        $(array).each(function () {
          if (serializeObj[this.name]) {
            if ($.isArray(serializeObj[this.name])) {
              serializeObj[this.name].push(this.value)
            } else {
              serializeObj[this.name] = [serializeObj[this.name], this.value]
            }
          }else{
            serializeObj[this.name] = this.value
          }
        })
        return serializeObj
      }
    },
    /**
     * 导航栏操作实用工具
     */
    nav: {
      /**
       * 导航栏手动触发页面跳转
       * @param pageName 页面名称
       * @param querystring 参数
       */
      dispatch: function (pageName, querystring) {
        $('#sidebarMenu').find('a').each(function (index, item) {
          if ($(item).attr('data-gh-route') === pageName) {
            $(item).click()
            location.hash = '#' + pageName + '?' + querystring
          }
        })
      }
    },
    /**
     * 将对象转换成带参数的形式 &a=1&b=2
     */
    buildQueryUrl: function (url, param) {
    	var x = url
		  var ba = true
      if (x.indexOf('?') != -1) {
        if (x.indexOf('?') == url.length - 1) {
          ba = false
        } else {
          ba = true
        }
      } else {
        x = x + '?'
        ba = false
      }
      var builder = ''
      for (var i in param) {
        var p = '&' + i + '='
        if (param[i]) {
          var v = param[i]
          if (Object.prototype.toString.call(v) === '[object Array]') {
            for (var j = 0; j < v.length; j++) {
              builder = builder + p + encodeURIComponent(v[j])
            }
          } else if (typeof(v) == "object" && Object.prototype.toString.call(v).toLowerCase() == "[object object]" && !v.length) {
            builder = builder + p + encodeURIComponent(JSON.stringify(v))
          } else {
            builder = builder + p + encodeURIComponent(v)
          }
        }
      }
      if (!ba) {
        builder = builder.substring(1)
      }
		  return x + builder
    }
  }
})
