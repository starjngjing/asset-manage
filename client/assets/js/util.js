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
    }
  }
})
