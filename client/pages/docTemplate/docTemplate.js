/**
 * 凭证模板
 */
define([
  'http',
  'config',
  'util',
  'extension'
], function (http, config, util, $$) {
  return {
    name: 'docTemplate',
    init: function () {
      // 分页配置
      var pageOptions = {
        poolName: "",
        page: 1,
        rows: 10
      }
      // 数据表格配置
      var tableConfig = {
        ajax: function (origin) {
          http.post(config.api.duration.assetPool.getAll, {
            data: pageOptions,
            contentType: 'form'
          }, function (rlt) {
            origin.success(rlt)
          })
        },
        pageNumber: pageOptions.page,
        pageSize: pageOptions.rows,
        pagination: true,
        sidePagination: 'server',
        pageList: [10, 20, 30, 50, 100],
        queryParams: getQueryParams,
        onLoadSuccess: function () {
        },
        columns: []
      }

      // 初始化数据表格
      $('#assetPoolTable').bootstrapTable(tableConfig);

      $('#updateTemplateModal').modal('show')

      function getQueryParams(val) {
        var form = document.searchForm
        $.extend(pageOptions, util.form.serializeJson(form)); //合并对象，修改第一个对象

        pageOptions.rows = val.limit
        pageOptions.page = parseInt(val.offset / val.limit) + 1
        return val
      }
    }
  }
})
