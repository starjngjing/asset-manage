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
        ajax: {
          url: 'https://api.github.com/search/repositories',
          dataType: 'json',
          delay: 250,
          data: function (params) {
            return {
              q: params.term,
              page: params.page
            }
          },
          processResults: function (data, params) {
            params.page = params.page || 1
            return {
              results: data.items,
              pagination: {
                more: (params.page * 30) < data.total_count
              }
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