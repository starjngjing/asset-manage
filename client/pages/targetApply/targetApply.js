/**
 * 标的申请管理
 */
define([

], function () {
  return {
    name: 'targetApply',
    init: function () {
      // js逻辑写在这里

      // 新建标的按钮点击事件
      $('#targetAdd').on('click', function () {
        $('#addTargetModal').modal('show')
      })
    }
  }
})