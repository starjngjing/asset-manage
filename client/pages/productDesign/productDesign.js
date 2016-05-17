/**
 * 产品申请管理
 */
define([
  'util'
], function (util) {
  return {
    name: 'productDesign',
    init: function () {
      // js逻辑写在这里

      // 新建产品按钮点击事件
      $('#productAdd').on('click', function () {
        $('#addProductModal').modal('show')
      })
    }
  }
})
