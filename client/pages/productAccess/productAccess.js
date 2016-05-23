/**
 * 产品准入管理
 */
define([
'http',
'config',
'util',
'extension'
], function (http, config, util, $$) {
	return {
    name: 'productAccess',
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
    				config.api.productApproveList, 
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
							return row.expAror+"%"+"~"+row.expArorSec+"%";
						}
						return row.expAror+"%";
					}
				},
				{
					align: 'center',
					field: 'raisedTotalNumber',
					formatter: function (val, row, index) {
						var typeOid = row.typeOid;  
						if(typeOid=="PRODUCTTYPE_01") {
							return row.raisedTotalNumber;
						} else {
							return "不限";
						}
					}
				},
				{
					field: 'netUnitShare',
					align: 'center'
				},
				{
					align: 'center',
					field: 'channelNum'
				},
				{
					field: 'investment',
					align: 'center'
				},
				{
					field: 'applicant',
					align: 'center'
				},
				{
					field: 'applyTime',
					align: 'center',
					formatter: function (val) {
						return util.table.formatter.timestampToDate(val, 'YYYY-MM-DD HH:mm:ss')
					}
				},
				{
					field: 'auditor',
					align: 'center'
				},
				{
					field: 'auditTime',
					align: 'center',
					formatter: function (val) {
						return util.table.formatter.timestampToDate(val, 'YYYY-MM-DD HH:mm:ss')
					}
				},
				{
					field: 'reviewer',
					align: 'center'
				},
				{
					field: 'reviewTime',
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
		           	    		text: '选择渠道',
		           	    		type: 'button',
		           	    		class: 'item-channel',
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
									$$.detailAutoFix($('#productDetailModal'), data); // 自动填充详情
									$('#productDetailModal').modal('show');
								} else {
									alert(查询失败);
								}
							})
						},
						'click .item-channel': function(e, value, row) {
							http.post(
								config.api.channels, {
		                		  data: {
		                			  oid:row.oid
		                		  },
		                		  contentType: 'form'
		                		},
		                		function (obj) {

		                		  $('#projectModal').modal('show');
		                		}
		                	);
						},
						'click .item-approve': function(e, value, row) {
							$("#oid").val(row.oid)
							$("#admitComment").val("")
							$$.confirm({
								container: $('#doAdmitConfirm'),
								trigger: this,
								accept: function() {
									http.post(config.api.productAdmitApprove, {
										data: {
											oid: row.oid
										},
										contentType: 'form',
									}, function(result) {
										$('#productAccessTable').bootstrapTable('refresh')
									})
								}
							})
						},
						'click .item-reject': function(e, value, row) {
							$("#oid").val(row.oid)
							$("#admitComment").val("")
							$$.confirm({
								container: $('#doAdmitConfirm'),
								trigger: this,
								accept: function() {
									var auditComment = $("#admitComment").val()
									if(null==auditComment || ""==auditComment) {
										
									} else {
										http.post(config.api.productAdmitReject, {
											data: {
												oid: row.oid,
												auditComment: auditComment
											},
											contentType: 'form',
										}, function(result) {
											$('#productAccessTable').bootstrapTable('refresh')
										})
									}
								}
							})
						}
					}
				},
			]
		}
    	// 数据表格初始化
    	$('#productAccessTable').bootstrapTable(tableConfig)
    	// 搜索表单初始化
    	$$.searchInit($('#searchForm'), $('#productAccessTable'))
    	
    	// 表格querystring扩展函数，会在表格每次数据加载时触发，用于自定义querystring
    	function getQueryParams (val) {
    		var form = document.searchForm
    		pageOptions.size = val.limit
    		pageOptions.number = parseInt(val.offset / val.limit) + 1
    		pageOptions.name = form.name.value.trim()
    		pageOptions.type = form.type.value.trim()
    		return val
  		}
		
    	     
    }
  }
})
