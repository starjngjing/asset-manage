/**
 * 扩展内容，提供jquery插件类似的扩展功能
 * amd模块，使用requirejs载入
 */

define({
  /**
   * confirm方法，使用 $$.confirm 将一段html结构变成confirm功能弹窗
   * 参数 options
   *      -- options.container：弹窗div，jquery对象
   *      -- options.trigger：点击trigger呼出弹窗，domNode对象
   *      -- options.position：弹窗出现位置，string，目前仅支持'bottomLeft'
   *      -- options.accept：点击确定后触发的回调函数
   *      -- options.cancel：点击取消后触发的回调函数
   */
  confirm: function (options) {

    var confirm = options.container
    var trigger = options.trigger
    var position = options.position || 'bottomLeft'
    var acceptCallback = options.accept
    var cancelCallback = options.cancel

    confirm
      .css({
        top: document.body.scrollTop + trigger.getBoundingClientRect().top,
        right: document.documentElement.clientWidth - trigger.getBoundingClientRect().right
      })
      .addClass(position)
      .show()

    $(document)
      .off('click.confirm')
      .on('click.confirm', function (e) {
        if (!trigger.contains(e.target) && !confirm[0].contains(e.target)) {
          confirm.hide()
        }
      })

    confirm.find('.accept').on('click', function () {
      if (acceptCallback) acceptCallback()
      confirm.hide()
    })

    confirm.find('.cancel').on('click', function () {
      if (cancelCallback) cancelCallback()
      confirm.hide()
    })
  },
  /**
   * 搜索表单初始化，使用 $$.searchInit 将grid中的搜索表单键盘事件绑定表格刷新
   * 参数 form：搜索表单 jquery对象
   * 参数 table：搜索表单对应的表格 jquery对象
   */
  searchInit: function (form, table) {
    form
      .find('input[type=text]:not(.datepicker)')
      .on('focus', function () {
        $(document).on('keyup.gridSearch', function (e) {
          if (e.keyCode === 13) {
            table.bootstrapTable('refresh')
          }
        })
      })
      .on('blur', function () {
        $(document).off('keyup.gridSearch')
      })
    form
      .find('input[type=text].datepicker')
      .datepicker({
        format: 'yyyy-mm-dd',
        autoclose: true
      })
      .on('hide', function () {
        table.bootstrapTable('refresh')
      })
    form
      .find('select')
      .on('change.gridSearch', function () {
        table.bootstrapTable('refresh')
      })
  },
  /**
   * treetable初始化
   * 参数 source：数据源 数组
   * 参数 table：表格 jquery对象
   * 参数 columns：单元格配置，同bootstrap配置中的culumns 数组
   */
  treetableInit: function (source, table, columns) {
    console.log(source)
    var tbody = $('<tbody></tbody>')
    source.forEach(function (item) {
      tbody.append(trGenerator(item, columns))
      if (item.children) {
        item.children.forEach(function (sub) {
          tbody.append(trGenerator(sub, columns, item.id))
          if (sub.children) {
            sub.children.forEach(function (triple) {
              tbody.append(trGenerator(triple, columns, sub.id))
            })
          }
        })
      }
    })

    table
      .append(tbody)
      .treetable({
        expandable: true
      })
      .treetable('expandAll')

    function trGenerator (item, columns, parentId) {
      var tr = $('<tr data-tt-id="' + item.id + '"></tr>')
      if (parentId) {
        tr.attr('data-tt-parent-id', parentId)
      }
      columns.forEach(function (column) {
        var td = $('<td></td>')
        td.css({
          'width': column.width ? parseInt(column.width) + 'px' : 'auto',
          'text-align': column.align || 'left'
        })
        var value = item[column.field] || ''
        var formatter = column.formatter ? column.formatter(value, item) : value
        td.html(formatter).appendTo(tr)
        var events = column.events
        if (events) {
          for (var key in events) {
            var arr = key.split(' ')
            td.find(arr[1]).on(arr[0], function (e) {
              events[key](e, value, item)
            })
          }
        }
      })
      return tr
    }
  },
  /**
   * iCheck 与 select2 区域内初始化
   * 参数 parent：要进行初始化的区域 jquery对象
   */
  csInit: function (parent) {
    parent.find('.icheck').iCheck({
      checkboxClass: 'icheckbox_minimal-blue',
      radioClass: 'iradio_minimal-blue'
    })
    parent.find('.select2').select2()
  }

})