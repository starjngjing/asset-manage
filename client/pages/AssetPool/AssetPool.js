/**
 * 资产池管理
 */
define([
  'http',
  'config',
  'util',
  'extension'
], function (http, config, util, $$) {
  	return {
    	name: 'AssetPool',
    	init: function () {
	    	// 分页配置
	        var pageOptions = {
	        	poolName:"",
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
	          	columns: [
					{// 编号
						field: 'oid',
					  
					},
		            {// 名称
		            	field: 'name',
		              	align: 'center'
		            },
		            {// 资产规模
		              	field: 'scale',
		            },
		            {// 现金比例
		            	field: 'cashRate',
		            },
		            {// 货币基金（现金类管理工具）比例
		            	field: 'cashtoolRate',
		            },
		            {// 信托（计划）比例
		              	field: 'targetRate',
		            },
		            {// 可用现金
		            	field: 'cashPosition',
		            },
		            {// 冻结资金
		            	field: 'freezeCash',
		            },
		            {// 在途资金
		              	field: 'transitCash',
		            },
		            {// 状态
		            	field: 'state',
		            },
	          	]
	        }
	
	        // 初始化数据表格
	        $('#dataTable').bootstrapTable(tableConfig);
	        // 搜索表单初始化
	        $$.searchInit($('#searchForm'), $('#dataTable'));
	        
	        function getQueryParams (val) {
	          	var form = document.searchForm
	          	$.extend(pageOptions, util.form.serializeJson(form)); //合并对象，修改第一个对象
	          
	          	pageOptions.rows = val.limit
	          	pageOptions.page = parseInt(val.offset / val.limit) + 1
	          	return val
	        }
    	}
  	}
})
