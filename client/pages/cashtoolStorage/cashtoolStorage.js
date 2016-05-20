/**
 * 现金类管理工具备选库
 */
define([
'http',
'config',
'extension',
'util'
], function (http, config, $$, util) {
  return {
    name: 'cashtoolStorage',
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
            http.post(config.api.listCashTool, {
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
          onLoadSuccess: function () {},
          columns: [
            {// 代码
            	field: 'ticker',
//              width: 60,
              align: 'center'
              
            },
            {// 基金名称
            	field: 'secShortName',
//              width: 60,
            	align: 'center'
            		
            },
            {// 类型
//            	width: 60,
              field: 'etfLof',
              formatter: function (val) {
            	return util.enum.transform('CASHTOOLTYPE', val);
              }
            },
            {// 最新流通份额
            	field: 'circulationShares',
            	formatter: function (val) {
            		return val;
            	}
            },
            {// 7日年化收益率
            	field: 'weeklyYield',
            	formatter: function (val) {
            		if(val)
            			return val.toFixed(2) + "%";
            		return val;
            	}
            },
            { // 状态
            	field: 'state',
            	formatter: function (val) {
            		return val;
            	}
            },
            { // 持有份额
            	field: 'holdAmount',
            	formatter: function (val) {
            		return val;
            	}
            },
            {
//              field: 'operator',
              width: 260,
              align: 'center',
              formatter: function (val) {
            	  var buttons = [
            	    {
            	      text: '移除出库',
            	      type: 'button',
            	      class: 'item-remove',
            	      isRender: true
            	    },
              	    {
              	    	text: '收益采集',
              	    	type: 'button',
              	    	class: 'item-interest',
              	    }
            	  ];
            	  return util.table.formatter.generateButton(buttons);
              },
              events: {
                  'click .item-remove': function (e, value, row) { // 移除出库
                	  $("#confirmTitle").html("确定移除现金管理工具？")
						$$.confirm({
							container: $('#doConfirm'),
							trigger: this,
							accept: function() {
								http.post(config.api.removeCashTool, {
									data: {
										oid: row.oid
									},
									contentType: 'form'
								}, function(result) {
									if (result.errorCode == 0) {
										$('#dataTable').bootstrapTable('refresh');
									} else {
										alert('移除现金管理工具失败');
									}
								})
							}
						})
                	  
                  },
                  'click .item-interest': function (e, value, row) { // 收益采集
                	  http.post(config.api.cashtoolDetQuery, {
                		  data: {
                			  oid:row.oid
                		  },
                		  contentType: 'form'
                	  },
                	  function (obj) {
                		  var data  = obj.data;
                		  if(!data){
                			  toastr.error('现金管理工具详情数据不存在', '错误信息', {
                				  timeOut: 10000
                			  });
                		  }
                		  $$.detailAutoFix($('#cashToolDetail'), data);	// 自动填充详情
                		  $$.formAutoFix($('#interestForm'), data); // 自动填充表单
                	  });
                	  $('#interestModal').modal('show');
                  },
                  'click .item-detail': function (e, value, row) { // 详情
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
        
        // 收益采集 按钮点击事件
        $("#interestSubmit").click(function(){
        	$("#interestForm").ajaxSubmit({
        		type:"post",  //提交方式  
                //dataType:"json", //数据类型'xml', 'script', or 'json'  
        		url: config.api.cashToolRevenueSave,
        		success:function(data) {
        			$('#interestForm').clearForm();
        			$('#interestModal').modal('hide');
        			$('#dataTable').bootstrapTable('refresh');
        		}
        	});
        	
        });
        
        // 修改按钮点击事件
        $("#unEstablishSubmit").click(function(){
        	$("#unEstablishForm").ajaxSubmit({
        		type:"post",  //提交方式  
        		//dataType:"json", //数据类型'xml', 'script', or 'json'  
        		url: config.api.unEstablish,
        		success:function(data) {
        			$('#unEstablishForm').clearForm();
        			$('#unEstablishModal').modal('hide');
        			$('#dataTable').bootstrapTable('refresh');
        		}
        	});
        	
        });

        function getQueryParams (val) {
          var form = document.searchForm
          pageOptions.ticker = form.ticker.value;
          pageOptions.secShortName = form.secShortName.value;
          pageOptions.etfLof = form.etfLof.value;
          pageOptions.circulationShares = form.circulationShares.value;
          pageOptions.weeklyYield = form.weeklyYield.value;
          pageOptions.rows = val.limit
          pageOptions.page = parseInt(val.offset / val.limit) + 1
          return val
        }
      
    }
  }
})