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
              width: 200,
              formatter: function (val) {
            	  return '<div class="func-area">' +
                  '<a href="javascript:void(0)" class="item-establish">成立</a>' +
                  '<a href="javascript:void(0)" class="item-unestablish">不成立</a>' +
                  '<a href="javascript:void(0)" class="item-detail">详情</a>' +
                  '</div>'
              },
              events: {
                  'click .item-establish': function (e, value, row) {
                    $('#updateModal').modal('show')
                  },
                  'click .item-detail': function (e, value, row) {
                    http.post(config.api.applyGetUserInfo, {
                      data: {
                        aoid: row.oid
                      },
                      contentType: 'form'
                    }, function (result) {
                      $('#detailModal')
                      .find('.detail-property')
                      .each(function (index, item) {
                        switch (index) {
                          case 0:
                            item.innerText = result.name || '--'
                            break
                          case 1:
                            item.innerText = result.sex || '--'
                            break
                          case 2:
                            item.innerText = result.company || '--'
                            break
                          case 3:
                            item.innerText = result.position || '--'
                            break
                          case 4:
                            item.innerText = result.phone || '--'
                            break
                        }
                      })
                      $('#detailModal').modal('show')
                    })
                  }
                
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
          pageOptions.type = form.type.value;
          pageOptions.raiseScope = form.raiseScope.value;
          pageOptions.lifed = form.lifed.value;
          pageOptions.expAror = form.expAror.value;
          pageOptions.rows = val.limit
          pageOptions.page = parseInt(val.offset / val.limit) + 1
          return val
        }
      
    }
  }
})