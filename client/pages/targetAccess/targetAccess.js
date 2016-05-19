/**
 * 标的会前审查
 */
define([
	'http',
	'config',
	'util',
	'extension'
], function(http, config, util, $$) {
	return {
		name: 'targetAccess',
		init: function() {
			// 分页配置
			var pageOptions = {
					number: 1,
					size: 10
				}
				// 数据表格配置
			var tableConfig = {
					ajax: function(origin) {
						http.post(config.api.targetCheckListQuery, {
							data: {
								page: pageOptions.number,
								rows: pageOptions.size
							},
							contentType: 'form'
						}, function(rlt) {
							origin.success(rlt)
						})
					},
					pageNumber: pageOptions.number,
					pageSize: pageOptions.size,
					pagination: true,
					sidePagination: 'server',
					pageList: [10, 20, 30, 50, 100],
					queryParams: getQueryParams,
					columns: [{
						field: 'sn'
					}, {
						field: 'name'
					}, {
						field: 'accrualType'
					}, {
						field: 'expSetDate'
					}, {
						field: 'type',
						formatter: function(val) {
							return util.enum.transform('TARGETTYPE', val);
						}
					}, {
						field: 'raiseScope',
						formatter: function(val) {
							if (val)
								var temp = val / 10000;
							return temp.toFixed(0) + "万";
						}
					}, {
						field: 'life',
						formatter: function(val, row) {
							switch (row.lifeUnit) {
								case "day":
									return val + '天';
								case "month":
									return val + '月';
								case "year":
									return val + '年';
								default:
									val;
							}
						}
					}, {
						field: 'expIncome',
						formatter: function(val) {
							if (val)
								return val.toFixed(2) + "%";
						}
					}, {
						field: 'expAror',
						formatter: function(val, row) {
							if (val)
								if (val == row.expArorSec) {
									return val.toFixed(2) + "%";
								} else {
									var maxAro = parseFloat(row.expArorSec);
									return val.toFixed(2) + '-' + maxAro.toFixed(2) + "%";
								}
						}
					}, {
						field: 'state',
						formatter: function(val) {
							switch (val) {
								case 'waitPretrial':
									return '待预审'
								case 'pretrial':
									return '预审中'
								case 'waitMeeting':
									return '待过会'
								case 'metting':
									return '过会中'
								case 'collecting':
									return '募集中'
								case 'establish':
									return '成立'
								case 'unEstablish':
									return '成立失败'
								case 'reject':
									return '驳回'
								case 'overdue':
									return '逾期'
								case 'invalid':
									return '作废'
								default:
									return '作废'
							}
						}
					}, {
						align: 'center',
						formatter: function(val, row) {
							var buttons = [{
								text: '查看详情',
								type: 'button',
								class: 'item-detail',
								isRender: true
							}, {
								text: '底层项目',
								type: 'button',
								class: 'item-project',
								isRender: true
							}, {
								text: '预审',
								type: 'button',
								class: 'item-check',
								isRender: true
							}];
							return util.table.formatter.generateButton(buttons);
						},
						events: {
							'click .item-check': function(e, value, row) {
								$("#coid").val(row.oid)
								$('#accessModal').modal('show');
							},
							'click .item-invalid': function(e, value, row) {
								$("#confirmTitle").html("确定作废标的？")
								$$.confirm({
									container: $('#doConfirm'),
									trigger: this,
									accept: function() {
										http.post(config.api.targetInvalid, {
											data: {
												oid: row.oid
											},
											contentType: 'form',
										}, function(result) {
											$('#targetApplyTable').bootstrapTable('refresh')
										})
									}
								})
							},
							'click .item-detail': function(e, value, row) {
								http.post(config.api.targetDetQuery, {
									data: {
										oid: row.oid
									},
									contentType: 'form'
								}, function(result) {
									if (result.errorCode == 0) {
										var data = result.investment;
										$$.detailAutoFix($('#detTargetForm'), data); // 自动填充详情
										$$.formAutoFix($('#detTargetForm'), data); // 自动填充表单
										$('#targetDetailModal').modal('show');
									} else {
										alert(查询失败);
									}
								})
							}
						}
					}]
				}
				// 初始化表格
			$('#targetAccessTable').bootstrapTable(tableConfig)

			function getQueryParams(val) {
				var form = document.targetSearchForm
				pageOptions.size = val.limit
				pageOptions.number = parseInt(val.offset / val.limit) + 1
				return val
			}

			$('#checkpass').on('click', function() {
				$("#confirmTitle").html("确定预审通过？")
				$$.confirm({
					container: $('#doConfirm'),
					trigger: this,
					accept: function() {
						check(config.api.targetCheckPass);
					}
				})
			})
			$('#checkreject').on('click', function() {
				$("#confirmTitle").html("确定预审驳回？")
				$$.confirm({
					container: $('#doConfirm'),
					trigger: this,
					accept: function() {
						check(config.api.targetCheckReject);
					}
				})
			})
		}
	}

	function check(url) {
		$('#accessFrom').ajaxSubmit({
			url: url,
			success: function(result) {
				$('#accessFrom').clearForm();
				$('#accessModal').modal('hide');
				$('#targetAccessTable').bootstrapTable('refresh');
			}
		})
	}
})