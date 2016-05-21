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
        		op:"historyList",
        		page: 1,
          rows: 10
        }
        // 数据表格配置
        var tableConfig = {
          ajax: function (origin) {
            http.post(config.api.listinvestmentPoolList, {
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
          //  http.post(config.api.listinvestmentPoolList, {
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
            	return util.enum.transform('TARGETTYPE', val);
              }
            },
            {// 收益率
            	field: 'expAror',
            	formatter: function (val) {
            		if(val)
						return val.toFixed(2) + "%";
            		return val;
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
            	      text: '成立',
            	      type: 'button',
            	      class: 'item-establish',
            	      isRender: true
            	    },
            	    {
              	      text: '不成立',
              	      type: 'button',
              	      class: 'item-unEstablish',
              	      isRender: true
              	    },
              	    {
              	    	text: '本息兑付',
              	    	type: 'button',
              	    	class: 'item-targetIncome',
//              	    	isRender: row.state == 'establish',
              	    	isRender: true,
              	    },
              	    {
              	    	text: '财务数据',
              	    	type: 'button',
              	    	class: 'item-financialData',
              	    	//isRender: row.state == 'establish',
              	    	isRender: true,
              	    }
            	  ];
            	  return util.table.formatter.generateButton(buttons);
              },
              events: {
                  'click .item-establish': function (e, value, row) {
                	  // 初始化   付息日 
                      for ( var i = 1; i <= 30; i++) {
	                      	var option = $("<option>").val(i).text(i);
	                      	$(establishForm.accrualDate).append(option);
	              		}
                      $(establishForm.accrualDate).val(10); // 默认10个工作日
                	  
                	http.post(config.api.targetDetQuery, {
                        data: {
                        	oid:row.oid
                        },
                        contentType: 'form'
                      },
                	  function (obj) {
                    	  var data  = obj.investment;
                    	  if(!data){
                    		  toastr.error('标的详情数据不存在', '错误信息', {
                    			    timeOut: 10000
                    			  });
                    	  }
                	  $$.detailAutoFix($('#establishForm'), data);	// 自动填充详情
                	  $$.formAutoFix($('#establishForm'), data); // 自动填充表单
                	});
                	$('#establishModal').modal('show');
                  },
                  'click .item-unEstablish': function (e, value, row) {
                	  http.post(config.api.targetDetQuery, {
                		  data: {
                			  oid:row.oid
                		  },
                		  contentType: 'form'
                	  },
                	  function (obj) {
                		  var data  = obj.investment;
                		  if(!data){
                			  toastr.error('标的详情数据不存在', '错误信息', {
                				  timeOut: 10000
                			  });
                		  }
                		  $$.detailAutoFix($('#unEstablishForm'), data);	// 自动填充详情
                		  $$.formAutoFix($('#unEstablishForm'), data); // 自动填充表单
                	  });
                	  $('#unEstablishModal').modal('show');
                  },
                  'click .item-targetIncome': function (e, value, row) {
                	  http.post(config.api.targetDetQuery, {
                		  data: {
                			  oid:row.oid
                		  },
                		  contentType: 'form'
                	  },
                	  function (obj) {
                		  var data  = obj.investment;
                		  if(!data){
                			  toastr.error('标的详情数据不存在', '错误信息', {
                				  timeOut: 10000
                			  });
                		  }
                		  $$.detailAutoFix($('#targetIncomeForm'), data);	// 自动填充详情
                		  $$.formAutoFix($('#targetIncomeForm'), data); // 自动填充表单
                	  });
                	  $('#targetIncomeModal').modal('show');
                  },
                  'click .item-financialData': function(e, value, row) {
                	  
                  }
                
              }
            }
          ]
        }

        // 初始化数据表格
        $('#dataTable').bootstrapTable(tableConfig)
        // 搜索表单初始化
        $$.searchInit($('#searchForm'), $('#dataTable'))
        
        
        // 成立 按钮点击事件
        $("#establishSubmit").click(function(){
        	$("#establishForm").ajaxSubmit({
        		type:"post",  //提交方式  
                //dataType:"json", //数据类型'xml', 'script', or 'json'  
        		url: config.api.establish,
        		success:function(data) {
        			$('#establishForm').clearForm();
        			$('#establishModal').modal('hide');
        			$('#dataTable').bootstrapTable('refresh');
        		}
        	});
        	
        });
        
        // 不成立 按钮点击事件
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
        
        // 本息兑付 按钮点击事件
        $("#targetIncomeSubmit").click(function(){
        	$("#targetIncomeForm").ajaxSubmit({
        		type:"post",  //提交方式  
        		//dataType:"json", //数据类型'xml', 'script', or 'json'  
        		url: config.api.targetIncomeSave,
        		success:function(data) {
        			$('#targetIncomeForm').clearForm();
        			$('#targetIncomeModal').modal('hide');
        			$('#dataTable').bootstrapTable('refresh');
        		}
        	});
        	
        });

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