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
        		page: 1,
          rows: 10
        }
        // 数据表格配置
        var tableConfig = {
          ajax: function (origin) {
            http.post(config.api.listinvestment, {
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
          //  http.post(config.api.listinvestment, {
           //   contentType: 'form'
           // }, function (result) {
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
            //})
          },
          columns: [
            {// 名称
            	field: 'name',
//              width: 60,
              align: 'center'
              
            },
            {// 类型
//            	width: 60,
              field: 'type',
              formatter: function (val) {
            	  //config.INVESTMENT_TYPE
                return val;
              }
            },
            {// 收益率
            	field: 'expAror',
            	formatter: function (val) {
            		return val+"%";
            	}
            },
            {
            	// 标的规模
            	field: 'raiseScope',
            	formatter: function (val) {
            		return val;
            	}
            },
            { // 标的限期（日）
              field: 'lifed',
              
            },
            { // 状态
            	field: 'state',
            	formatter: function (val) {
            		return val;
            	}
            },
            { // 已购份额
            	field: 'state',
            	formatter: function (val) {
            		return '已购份额';
            	}
            },
            {
//              field: 'operator',
              formatter: function (val) {
            	  return '<div class="func-area">' +
                  '<a href="javascript:void(0)" class="item-update">成立</a>' +
                  '<a href="javascript:void(0)" class="item-update">不成立</a>' +
                  '<a href="javascript:void(0)" class="item-update">详情</a>' +
                  '</div>'
               }
            }
          ]
        }

        // 初始化数据表格
        $('#dataTable').bootstrapTable(tableConfig)
        // 搜索表单初始化
        $$.searchInit($('#searchForm'), $('#dataTable'))

        function getQueryParams (val) {
          var form = document.searchForm
          pageOptions.name = form.name.value;
          pageOptions.life = form.life.value;
          pageOptions.rows = val.limit
          pageOptions.page = parseInt(val.offset / val.limit) + 1
          return val
        }
      
    }
  }
})