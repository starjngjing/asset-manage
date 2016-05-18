/**
 * 投资标的备选库
 */
define([
'http',
'config',
'extension',
'util'
], function (http, config, $$, util) {
  return {
    name: 'targetStorage',
    init: function () {
      // js逻辑写在这里

        // 分页配置
        var pageOptions = {
          number: 1,
          size: 10
        }
        // 数据表格配置
        var tableConfig = {
          ajax: function (origin) {
            http.post(config.api.listinvestment, {
              data: {
                page: pageOptions.number,
                rows: pageOptions.size
              },
              contentType: 'form'
            }, function (rlt) {
              origin.success(rlt)
            })
          },
          pageNumber: pageOptions.number,
          pageSize: pageOptions.size,
          pagination: true,
          sidePagination: 'server',
          pageList: [10, 20, 30, 50, 100],
          queryParams: getQueryParams,
          onLoadSuccess: function () {
            http.post(config.api.listinvestment, {
              contentType: 'form'
            }, function (result) {
//              $('#clubData').html('会员机构：' + result.clubData + '家')
//              $('#platAssetData').html('平台资产：' + result.platAssetData + '项')
//              $('#assetSizeData').html('资产规模：' + result.assetSizeData + '亿')
//              $('#updateTime').html('变更时间：' + util.table.formatter.timestampToDate(result.updateTime, 'YYYY-MM-DD'))
//              document.clubDataForm.data.value = result.clubData
//              document.platAssetDataForm.data.value = result.platAssetData
//              document.assetSizeDataForm.data.value = result.assetSizeData
//              $('#clubDataForm').validator()
//              $('#platAssetDataForm').validator()
//              $('#assetSizeDataForm').validator()
            })
          },
          columns: [
            {
              width: 30,
              align: 'center',
              formatter: function (val, row, index) {
                return index + 1
              }
            },
            {
              field: 'dataKind',
              formatter: function (val) {
                switch (val) {
                  case 'assetsize': return '资产规模'
                  case 'platasset': return '平台资产'
                  case 'club': return '会员机构'
                }
              }
            },
            {
              field: 'data'
            },
            {
              field: 'updateTime',
              formatter: function (val) {
                return util.table.formatter.timestampToDate(val, 'YYYY-MM-DD')
              }
            },
            {
              field: 'operator'
            }
          ]
        }

        // 初始化数据表格
        $('#dataTable').bootstrapTable(tableConfig)
        // 搜索表单初始化
        $$.searchInit($('#searchForm'), $('#dataTable'))

        function getQueryParams (val) {
          var form = document.searchForm
          pageOptions.size = val.limit
          pageOptions.number = parseInt(val.offset / val.limit) + 1
          return val
        }
      
    }
  }
})