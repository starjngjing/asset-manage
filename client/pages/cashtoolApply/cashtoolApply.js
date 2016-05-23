/**
 * 现金类管理工具申请
 */
define([
	'http',
	'config',
	'util',
	'extension'
], function(http, config, util, $$) {
	return {
		name: 'cashtoolApply',
		init: function() {
			// js逻辑写在这里
			// 分页配置
			var pageOptions = {
					number: 1,
					size: 10,
					ticker: '',
					secShortName: '',
					etfLof: '',
					state: ''
				}
				// 数据表格配置
			var tableConfig = {
					ajax: function(origin) {
						http.post(config.api.cashtoolListQuery, {
							data: {
								page: pageOptions.number,
								rows: pageOptions.size,
								ticker: pageOptions.ticker,
								secShortName: pageOptions.secShortName,
								etfLof: pageOptions.etfLof,
								state: pageOptions.state
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
						field: 'ticker'
					}, {
						field: 'secShortName'
					}, {
						field: 'etfLof',
						formatter: function(val) {
							return util.enum.transform('CASHTOOLTYPE', val);
						}
					}, {
						field: 'dividendType'
					}, {
						field: 'perfBenchmark'
					}, {
						field: 'riskLevel'
					}, {
						field: 'state',
						formatter: function(val) {
							return util.enum.transform('cashtoolStates', val);
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
								text: '编辑',
								type: 'button',
								class: 'item-edit',
								isRender: row.state == 'waitPretrial' || row.state == 'reject'
							}, {
								text: '提交审核',
								type: 'button',
								class: 'item-check',
								isRender: row.state == 'waitPretrial' || row.state == 'reject'
							}, ];
							return util.table.formatter.generateButton(buttons);
						},
						events: {
							'click .item-check': function(e, value, row) {
								$("#confirmTitle").html("确定提交预审？")
								$$.confirm({
									container: $('#doConfirm'),
									trigger: this,
									accept: function() {

									}
								})
							},
							'click .item-detail': function(e, value, row) {
								http.post(config.api.cashtoolDetQuery, {
									data: {
										oid: row.oid
									},
									contentType: 'form'
								}, function(result) {
									if (result.errorCode == 0) {
										var data = result.data;
										$$.detailAutoFix($('#detCashToolForm'), data); // 自动填充详情
										$$.formAutoFix($('#detCashToolForm'), data); // 自动填充表单
										$('#cashToolDetailModal').modal('show');
									} else {
										alert('查询失败');
									}
								})
							}
						}
					}]
				}
				// 初始化表格
			$('#cashToolApplyTable').bootstrapTable(tableConfig)
				// 搜索表单初始化
			$$.searchInit($('#cashToolSearchForm'), $('#cashToolApplyTable'))
				// 新建标的按钮点击事件
			$('#cashToolAdd').on('click', function() {
					$('#addCashToolModal').modal('show')
				})
				// 新建底层资产按钮点击事件
			$('#assetAdd').on('click', function() {
				$('#addAssetModal').modal('show')
			})

			$('#saveCashTool').on('click', function() {
				saveCashTool();
			})

			function getQueryParams(val) {
				var form = document.cashToolSearchForm
				pageOptions.size = val.limit
				pageOptions.number = parseInt(val.offset / val.limit) + 1
				pageOptions.ticker = form.ticker.value.trim();
				pageOptions.secShortName = form.secShortName.value.trim();
				pageOptions.etfLof = form.etfLof.value.trim();
				pageOptions.state = form.state.value.trim();
				return val
			}
		}
	}

	function saveCashTool() {
		$('#addCashToolForm').ajaxSubmit({
			url: config.api.cashtoolAdd,
			//			contentType : 'application/json',
			success: function(result) {
				$('#addCahsToolForm').clearForm();
				$('#addCashToolModal').modal('hide');
				$('#cashToolApplyTable').bootstrapTable('refresh');
			}
		})
	}

})