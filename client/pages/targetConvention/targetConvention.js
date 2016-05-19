/**
 * 标的过会管理
 */
define([
  'config'
], function (config) {
  return {
    name: 'targetConvention',
    init: function () {
      // js逻辑写在这里

      // 新建会议按钮点击事件
      $('#targetConventionAdd').on('click', function () {
        $('#addTargetConventionModal').modal('show')
      })

      // 参会人select2初始化
      var addForm = document.addTargetConventionForm

      $(addForm.participant).select2({
        multiple: "multiple",
        ajax: {
          //type: 'post',
          url: 'https://api.github.com/search/repositories',
          dataType: 'json',
          delay: 250,  // 输入->查询 停顿毫秒数
          data: function (params) {
            console.log(params)
            return {
              q: params.term  // 输入字符串
            }
          },
          processResults: function (data) {
            return {
              results: data.rows.map(function (item) {
                var showObj = {}
                showObj.id = item.id
                showObj.text = item.name
              })
            }
          },
          cache: true
        },
        escapeMarkup: function (markup) {
          return markup
        },
        minimumInputLength: 1
      })
    }
  }
})