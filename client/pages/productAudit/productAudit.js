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
		// 用于存储表格checkbox选中的项
		var checkItems = []
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
					field: 'code',
					align: 'center'
				},
				{
					field: 'name',
					align: 'center'
				},
     			{
					field: 'typeName',
					align: 'center'
				},
				{
					field: 'administrator',
					align: 'center'
				},
				{
					align: 'center',
					field: 'status',
					formatter: function (val) {
						switch (val) {
							case 'CREATE': return '新建'
							case 'UPDATE': return '修改'
							case 'AUDITING': return '审核中'
							case 'AUDITFAIL': return '审核不通过'
							case 'AUDITFAIL': return '审核通过'
							case 'REVIEWFAIL': return '复核不通过'
							case 'REVIEWPASS': return '复核通过'
							case 'ADMITFAIL': return '准入不通过'
							case 'ADMITPASS': return '准入通过'
							case 'NOTSTARTRAISE': return '未开始募集'
							case 'RAISING': return '募集中(募集期)'
							case 'NOESTALISHING': return '成立未开始'
							case 'ESTALISHING': return '已成立'
							default : return '-'
						}
					}
				},
				{
					align: 'center',
					field: 'durationPeriod',
					formatter: function (val, row, index) {
						var typeOid = row.typeOid;  
						if(typeOid=="PRODUCTTYPE_01") {
							return row.durationPeriod+"天";
						} else {
							return "不固定";
						}
					}
				},
				{
					field: 'expAror',
					align: 'center',
					formatter: function (val, row, index) {
						if(row.expArorSec!=null && row.expAror!=row.expArorSec) {
							return row.expArorSec+"%"+"~"+row.expArorSec+"%";
						}
						return row.expAror+"%";
					}
				},
				{
					field: 'raisedTotalNumber',
					align: 'center'
				},
				{
					field: 'netUnitShare',
					align: 'center'
				},
				{
					align: 'center',
					formatter: function (val, row, index) {
						return index + 1
					}
				},
				{
					field: 'investment',
					align: 'center'
				},
				{
					field: 'createTime',
					align: 'center',
					formatter: function (val) {
						return util.table.formatter.timestampToDate(val, 'YYYY-MM-DD HH:mm:ss')
					}
				},
				{
					align: 'center',
					formatter: function(val, row) {
						var buttons = [
		           	    	{
		           	      		text: '详情',
		           	      		type: 'button',
		           	      		class: 'item-detail',
		           	      		isRender: true
		           	    	},
		           	    	{
		           	    		text: '批准',
		           	    		type: 'button',
		           	    		class: 'item-approve',
		           	    		isRender: true
		           	    	},
		           	    	{
		          	      		text: '驳回',
		           	      		type: 'button',
		           	      		class: 'item-reject',
		           	      		isRender: true
		           	    	}
		           	    ];
		           	  	return util.table.formatter.generateButton(buttons);
		           	},
		           	events: {
						'click .item-detail': function(e, value, row) {
							http.post(config.api.productDetail, {
								data: {
									oid: row.oid
								},
								contentType: 'form'
							}, function(result) {
								if (result.errorCode == 0) {
									var data = result;
									$$.detailAutoFix($('#productDetailForm'), data); // 自动填充详情
									$('#productDetailModal').modal('show');
								} else {
									alert(查询失败);
								}
							})
						},
						'click .item-approve': function(e, value, row) {
//							$("#confirmTitle").html("确定作废产品名称为:")
//							$("#confirmTitle1").html(row.fullName+"的产品吗？")
//							$$.confirm({
//								container: $('#doConfirm'),
//								trigger: this,
//								accept: function() {
//									http.post(config.api.productInvalid, {
//										data: {
//											oid: row.oid
//										},
//										contentType: 'form',
//									}, function(result) {
//										$('#productDesignTable').bootstrapTable('refresh')
//									})
//								}
//							})
						},
						'click .item-reject': function(e, value, row) {
//							$("#confirmTitle").html("确定作废产品名称为:")
//							$("#confirmTitle1").html(row.fullName+"的产品吗？")
//							$$.confirm({
//								container: $('#doConfirm'),
//								trigger: this,
//								accept: function() {
//									http.post(config.api.productInvalid, {
//										data: {
//											oid: row.oid
//										},
//										contentType: 'form',
//									}, function(result) {
//										$('#productDesignTable').bootstrapTable('refresh')
//									})
//								}
//							})
						}
					}
				},
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
    	

		// 提交审核按钮点击事件
		$('#productAudit').on('click', function () {
			if(checkItems.length>0) {
				var productNames = checkItems.map(function (item) {
					return item.name
				})
				var pnames = "";
				var le = productNames.length
				for (var i=0;i<le;i++) {
					if(i>0){
						pnames+="，"
					}
					pnames+=productNames[i]
				}
				$("#auditProductNames").html(pnames)
				$('#productAuditModal').modal('show')
			}
		})

		// 提交审核弹窗 -> 提交按钮点击事件
		$('#doProductAudit').on('click', function () {
			// 获取id数组
			var oids = checkItems.map(function (item) {
				return item.oid
			})
			// 提交数组
			http.post(
				config.api.productAuditApply, 
				{
					data: {
						oids: JSON.stringify(oids)
					},
					contentType: 'form',
				}, 
				function(result) {
					$('#productAuditModal').modal('hide')
					$('#productDesignTable').bootstrapTable('refresh')
				}
			)
		})
    	     
    }
  }
})
