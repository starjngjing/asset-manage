/**
 * 审核列表
 */
define([
	'http',
	'config',
	'util',
	'extension'
], function(http, config, util, $$) {
	return {
		name: 'investor',
		init: function() {

			// 产品列表
			/*http.post(config.api.duration.assetPool.getNameList, function(json) {
					var productOptions = ''
					var select = document.searchForm.productOid
					json.rows.forEach(function(item) {
						productOptions += '<option value="' + item.oid + '">' + item.name + '</option>'
					})
					$(select).html(productOptions)
				})*/
			// 分页配置
			var pageOptions = {
				page: 1,
				rows: 10
			};
			var tableConfig = {
				ajax: function(origin) {
					http.post(config.api.invest.manager.accountList, {
						data: pageOptions,
						contentType: 'form'
					}, function(rlt) {
						origin.success(rlt)
					})
				},
				pageNumber: pageOptions.page,
				pageSize: pageOptions.rows,
				pagination: true,
				sidePagination: 'server',
				pageList: [10, 20, 30, 50, 100],
				queryParams: getQueryParams,
				onLoadSuccess: function() {},
				columns: [{ // 标识
					field: 'sn',
					formatter: function(val, row, index) {
						return val;
					}
				}, { // 真实姓名
					field: 'realName',
				}, {
					// 手机号码
					field: 'phoneNum'
				}, { // 类型
					field: 'type',
					formatter: function(val, row, index) {
						return val;
					}
				}, { // 产品名称
					field: 'productName',
					formatter: function(val, row, index) {
						return val;
					}
				}, { // 本金金额
					field: 'balance',
					formatter: function(val, row, index) {
						return val;
					}
				}, { // 基础收益
					field: 'income',
					formatter: function(val, row) {
						if (val)
							return (val * 100.0).toFixed(2) + "%";
						return val;
					}
				}, { // 奖励收益
					field: 'reward',
					formatter: function(val, row) {
						if (val)
							return (val * 100.0).toFixed(2) + "%";
						return val;
					}
				}, {
					// 操作区
					width: 120,
					align: 'center',
					formatter: function(val, row) {
						var buttons = [{
							text: '详情',
							type: 'button',
							class: 'item-detail',
							isRender: true
						}];
						return util.table.formatter.generateButton(buttons);
					},
					events: {
						'click .item-detail': function(e, value, row) {
							// 初始化当前持仓明细
							var currHoldTableConfig = {};
							$('#currHoldTable').bootstrapTable('destroy');
							$('#currHoldTable').bootstrapTable(currHoldTableConfig);

							// 初始化历史仓明细
							var currHoldTableConfig = {};
							$('#currHoldTable').bootstrapTable('destroy');
							$('#currHoldTable').bootstrapTable(currHoldTableConfig);
							
							// 初始化订单明细
							var currHoldTableConfig = {};
							$('#currHoldTable').bootstrapTable('destroy');
							$('#currHoldTable').bootstrapTable(currHoldTableConfig);
							$('#investorDetailModal').modal('show')
						}
					}

				}]
			};

			// 初始化数据表格
			$('#dataTable').bootstrapTable(tableConfig);
			// 搜索表单初始化
			$$.searchInit($('#searchForm'), $('#dataTable'));

			function getQueryParams(val) {
				var form = document.searchForm;
				$.extend(pageOptions, util.form.serializeJson(form)); //合并对象，修改第一个对象

				pageOptions.rows = val.limit;
				pageOptions.page = parseInt(val.offset / val.limit) + 1

				return val;
			}
		}
	}
});