/**
 * 复核列表
 */
define([
'http',
'config',
'util',
'extension'
], function (http, config, util, $$) {
	return {
    name: 'productReview',
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
		var selectProductOid 
    	// 数据表格配置
    	var tableConfig = {
    		ajax: function (origin) {
    			http.post(
    				config.api.productCheckList, 
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
							case 'AUDITPASS': return '审核通过'
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
					field: 'assetPoolName',
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
									
									switch (data.typeOid) {
										case 'PRODUCTTYPE_01':
											$('#detailProductType01Area').show()
											$('#detailProductType02Area').hide()
											break
										case 'PRODUCTTYPE_02':
											$('#detailProductType02Area').show()
											$('#detailProductType01Area').hide()
											break
									}
									
									var productDetailFiles = []
									if(data.files!=null && data.files.length>0) {
										for(var i=0;i<data.files.length;i++){
											productDetailFiles.push(data.files[i])
										}
									}
									$('#productDetailFileTable').bootstrapTable('load', productDetailFiles)
									
									$$.detailAutoFix($('#productDetailModal'), data); // 自动填充详情
									$('#productDetailModal').modal('show');
								} else {
									alert(查询失败);
								}
							})
						},
						'click .item-approve': function(e, value, row) {
							selectProductOid = row.oid;
							$("#oid").val(row.oid)
							$("#reviewComment").val("")
							$$.confirm({
								container: $('#doReviewConfirm'),
								trigger: this,
								accept: function() {
									http.post(config.api.productReviewApprove, {
										data: {
											oid: row.oid
										},
										contentType: 'form',
									}, function(result) {
										$('#productReviewTable').bootstrapTable('refresh')
									})
								}
							})
						},
						'click .item-reject': function(e, value, row) {
							selectProductOid = row.oid;
							$("#oid").val(row.oid)
							$("#reviewComment").val("")
							$$.confirm({
								container: $('#doReviewConfirm'),
								trigger: this,
								accept: function() {
									var auditComment = $("#reviewComment").val()
									if(null==auditComment || ""==auditComment) {
										
									} else {
										http.post(config.api.productReviewReject, {
											data: {
												oid: row.oid,
												auditComment: auditComment
											},
											contentType: 'form',
										}, function(result) {
											$('#productReviewTable').bootstrapTable('refresh')
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
    	$('#productReviewTable').bootstrapTable(tableConfig)
    	// 搜索表单初始化
    	$$.searchInit($('#searchForm'), $('#productReviewTable'))
    	
    	// 详情附件表格配置
    	var productDetailFileTableConfig = {
			columns: [
				{
					field: 'name',
				},
				{
					field: 'operator',
				},
				{
					field: 'createTime',
				},
				{
					width: 100,
					align: 'center',
					formatter: function() {
						var buttons = [{
							text: '下载',
							type: 'button',
							class: 'item-download'
						}]
						return util.table.formatter.generateButton(buttons)
					},
					events: {
						'click .item-download': function(e, value, row) {
							location.href = 'http://api.guohuaigroup.com' + row.furl
						}
					}
				}
			]
		}
    	// 详情附件表格初始化
		$('#productDetailFileTable').bootstrapTable(productDetailFileTableConfig)
    	
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

