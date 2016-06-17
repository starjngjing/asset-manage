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
					formatter: function(val) {
						return util.enum.transform('investorType', val);
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
							var currHoldPageOptions = {
								page: 1,
								rows: 10
							};
							var currHoldTableConfig = {
								ajax: function(origin) {
									http.post(config.api.invest.manager.holdList, {
											data: function() {
												var d = {};
												d.op = 'curr',
													d.accountOid = row.oid,
													$.extend(d, currHoldPageOptions);
												return d;
											}(),
											contentType: 'form'
										},
										function(rlt) {
											origin.success(rlt)
										})
								},
								pageNumber: currHoldPageOptions.page,
								pageSize: currHoldPageOptions.rows,
								pagination: true,
								sidePagination: 'server',
								pageList: [10, 20, 30, 50, 100],
								queryParams: function(val) {
									currHoldPageOptions.rows = val.limit;
									currHoldPageOptions.page = parseInt(val.offset / val.limit) + 1;
									return val;
								},
								columns: [{ // 渠道名称
									field: 'channelName',
									align: 'center'
								}, { // 本金份额
									field: 'balance',
									align: 'center',
									formatter: function(val) {
										return val;
									}
								}, { // 基础收益
									field: 'compound',
									align: 'center',
									formatter: function(val) {
										return val;
									}
								}, { //奖励收益
									field: 'reward',
									align: 'center',
									formatter: function(val) {
										return val;
									}
								}, { //持仓天数
									field: 'holdDays',
									align: 'center'
								}, { //适用奖励收益率
									visible: false,
									field: 'rewardRatio',
									align: 'center',
									formatter: function(val) {
										if (val)
											return val.toFixed(2) + '%';
										return val;
									}
								}]
							};
							$('#currHoldTable').bootstrapTable('destroy');
							$('#currHoldTable').bootstrapTable(currHoldTableConfig);

							// 初始化历史仓明细
							var hisHoldPageOptions = {
								page: 1,
								rows: 10
							};
							var hisHoldTableConfig = {
								ajax: function(origin) {
									http.post(config.api.invest.manager.holdList, {
										data: function() {
											var d = {};
											d.op = 'his',
												d.accountOid = row.oid,
												$.extend(d, hisHoldPageOptions);
											return d;
										}(),
										contentType: 'form'
									}, function(rlt) {
										origin.success(rlt)
									})
								},
								pageNumber: hisHoldPageOptions.page,
								pageSize: hisHoldPageOptions.rows,
								pagination: true,
								sidePagination: 'server',
								pageList: [10, 20, 30, 50, 100],
								queryParams: function(val) {
									hisHoldPageOptions.rows = val.limit;
									hisHoldPageOptions.page = parseInt(val.offset / val.limit) + 1;
									return val;
								},
								columns: [{ // 渠道名称
									field: 'channelName',
									align: 'center'
								}, { // 本金份额
									field: 'balance',
									align: 'center',
									formatter: function(val) {
										return val;
									}
								}, { // 基础收益
									field: 'compound',
									align: 'center',
									formatter: function(val) {
										return val;
									}
								}, { //奖励收益
									field: 'reward',
									align: 'center',
									formatter: function(val) {
										return val;
									}
								}, { //持仓天数
									field: 'holdDays',
									align: 'center'
								}, { //适用奖励收益率
									visible: true,
									field: 'rewardRatio',
									align: 'center',
									formatter: function(val) {
										if (val)
											return val.toFixed(2) + '%';
										return val;
									}
								}]
							};
							$('#hisHoldTable').bootstrapTable('destroy');
							$('#hisHoldTable').bootstrapTable(hisHoldTableConfig);

							// 初始化订单明细
							var orderPageOptions = {
								page: 1,
								row: 10
							};
							var orderTableConfig = {
								ajax: function(origin) {
									http.post(config.api.invest.manager.orderList, {
										data: function() {
											var d = {};
											d.accountOid = row.oid,
												$.extend(d, orderPageOptions);
											return d;
										}(),
										contentType: 'form'
									}, function(rlt) {
										origin.success(rlt)
									})
								},
								pageNumber: orderPageOptions.page,
								pageSize: orderPageOptions.rows,
								pagination: true,
								sidePagination: 'server',
								pageList: [10, 20, 30, 50, 100],
								queryParams: function(val) {
									orderPageOptions.rows = val.limit;
									orderPageOptions.page = parseInt(val.offset / val.limit) + 1;
									return val;
								},
								columns: [{ // 订单号
									field: 'orderCode',
									align: 'center'
								}, { // 交易类型
									field: 'orderType',
									align: 'center',
									formatter: function(val) {
										return util.enum.transform('investorOrderType', val);
									}
								}, { // 订单金额
									field: 'orderAmount',
									align: 'center',
									formatter: function(val) {
										return val;
									}

								}, { //订单状态
									field: 'orderStatus',
									align: 'center',
									formatter: function(val) {
										var className = ''
										switch (val) {
											case 'SUBMIT':
												className = 'text-yellow'
												break
											case 'DISABLE':
												className = 'text-red'
												break
											default:
												className = 'text-green'
												break
										}
										return '<span class="' + className + '">' + util.enum.transform('investorOrderStatus', val) + '</span>'
									}
								}, { //下单时间
									field: 'createTime',
									align: 'center'
								}, { //确认时间
									visible: true,
									field: 'completeTime',
									align: 'center',
									formatter: function(val) {
										return val;
									}
								}]
							};
							$('#orderTable').bootstrapTable('destroy');
							$('#orderTable').bootstrapTable(orderTableConfig);

							$('#investorDetailModal').modal('show');
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