/**
 * 审核列表
 */
define([
'http',
'config',
'util',
'extension'
], function (http, config, util, $$) {
	return {
    name: 'productAudit',
    init: function () {
    	// js逻辑写在这里
    	// 数据表格分页、搜索条件配置
    	var pageOptions = {
    		number: 1,
        	size: 10,
        	name: '',
        	type: ''
        }
    	// 数据表格配置
    	var tableConfig = {
    		ajax: function (origin) {
    			http.post(
    				config.api.productAuditList, 
    				{
    					data: {
    						page: pageOptions.number,
    						rows: pageOptions.size,
    						name: pageOptions.name,
    						type: pageOptions.type
    					},
    					contentType: 'form'
    				},
    				function (rlt) {
    					origin.success(rlt)
    				}
    			)
    		},
    		pageNumber: pageOptions.number,
    		pageSize: pageOptions.size,
    		pagination: true,
    		sidePagination: 'server',
    		pageList: [10, 20, 30, 50, 100],
    		queryParams: getQueryParams,
    		onLoadSuccess: function () {},
    		columns: [
    			{
       				field: 'code'
      			},
      			{
        			field: 'name'
      			},
     			{
        			field: 'typeName'
      			},
      			{
        			field: 'administrator'
      			},
      			{
        			field: 'expAror',
        			formatter: function (val, row, index) {
        				if(row.expArorSec!=null && row.expAror!=row.expArorSec) {
        					return row.expArorSec+"%"+"~"+row.expArorSec+"%";
        				}
          				return row.expAror+"%";
        			}
      			},      
      			{
        			field: 'raisedTotalNumber',
        			formatter: function (val) {
        				if(val!=null && val>0) {
        					return val;
        				}
          				return "不限";
        			}
      			},
      			{
        			field: 'netUnitShare'
      			},
      			{
        			width: 30,
        			align: 'center',
        			formatter: function (val, row, index) {
          				return index + 1
        			}
      			},
      			{
        			field: 'applicant'
      			},
      			{
        			field: 'applyTime',
        			formatter: function (val) {
          				return util.table.formatter.timestampToDate(val, 'YYYY-MM-DD HH:mm:ss')
        			}
      			},    
      			{
        			width: 170,
        			align: 'center',
        			formatter: function () {
          				return '<div class="func-area">' + 
                   			'<a href="javascript:void(0)" class="item-detail">详情</a>' + 
                   			'<a href="javascript:void(0)" class="item-approve">批准</a>' + 
                   			'<a href="javascript:void(0)" class="item-reject">驳回</a>' +
                 			'</div>'
        			},
        			events: {
          				'click .item-approve': function (e, value, row) {
            				var form = document.updateForm
            				form.oid.value = row.oid
            				form.openstate.value = row.openState
            				form.remark.value = row.remark
            				$('#updateModal').modal('show')
          				},
          				
          				'click .item-detail': function (e, value, row) {
            				http.post(
            					ams.product.detail, 
            					{
            						data: {
                						oid: row.oid
              						},
              						contentType: 'form'
            					}, 
            					function (result) {
              						$('#detailModal').find('.detail-property').each(function (index, item) {
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
            					}
            				)
          				}
        			}
      			}
    			]
  			}
    	// 数据表格初始化
    	$('#productAuditTable').bootstrapTable(tableConfig)
    	// 搜索表单初始化
    	$$.searchInit($('#searchForm'), $('#productAuditTable'))
    	// 表格querystring扩展函数，会在表格每次数据加载时触发，用于自定义querystring
    	function getQueryParams (val) {
    		var form = document.searchForm
    		pageOptions.size = val.limit
    		pageOptions.number = parseInt(val.offset / val.limit) + 1
    		pageOptions.name = form.name.value.trim()
    		pageOptions.type = form.type.value.trim()
    		return val
  		}
    	
    	$('#saveProductSubmit').on('click', function () {
    		var typeOid = $("#typeSelect  option:selected").val();  
    		if(typeOid=="PRODUCTTYPE_01") {
    			$('#addProductForm').ajaxSubmit({
      			url: config.api.savePeriodic,
      			success: function (addResult) {
        			$('#addProductModal').modal('hide')
        			$('#productDesignTable').bootstrapTable('refresh')
      			}
    		})
    		} else {
    			$('#addProductForm').ajaxSubmit({
      			url: config.api.saveCurrent,
      			success: function (addResult) {
        			$('#addProductModal').modal('hide')
        			$('#productDesignTable').bootstrapTable('refresh')
      			}
    		})
    		}
    		
  		})
    	
    	
    	     
    }
  }
})
