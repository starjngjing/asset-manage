/**
 * 投资标的备选库
 */
define([
	'http',
	'config',
	'extension',
	'util'
], function(http, config, $$, util) {
	return {
		name: 'targetStorage',
		init: function() {
			// js逻辑写在这里
			var targetInfo;
			// 分页配置
			var pageOptions = {
					op: "storageList",
					page: 1,
					rows: 10
				}
				// 数据表格配置
			var tableConfig = {
				ajax: function(origin) {
					http.post(config.api.listinvestmentPoolList, {
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
				columns: [{ // 编号
					field: 'sn',
					//              width: 60,

				}, { // 名称
					field: 'name',
					//              width: 60,
					align: 'center'

				}, { // 类型
					//            	width: 60,
					field: 'type',
					formatter: function(val) {
						return util.enum.transform('TARGETTYPE', val);
					}
				}, { // 预期年化收益率
					field: 'expAror',
					formatter: function(val) {
						if (val)
							return val.toFixed(2) + "%";
						return val;
					}
				}, {
					// 标的规模
					field: 'raiseScope',
					formatter: function(val) {
						var temp = val / 10000;
						return temp.toFixed(0) + "万";
					}
				}, { // 标的限期
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

				}, { // 状态
					field: 'lifeState',
					formatter: function(val) {
						return util.enum.transform('targetLifeStates', val);
					}

				}, { // 已持有份额
					visible: true,
					field: 'holdAmount',
					formatter: function(val) {
						return val;
					}
				}, { // 申请中份额
					visible: true,
					field: 'applyAmount',
					formatter: function(val) {
						return val;
					}
				}, { // 风险等级
					align: 'center',
					field: 'riskRate',
					formatter: function(val) {
						return util.table.convertRisk(val);
					}
				}, {
					//              field: 'operator',
					width: 310,
					align: 'center',
					formatter: function(val, row) {
						var buttons = [{
							text: '查看详情',
							type: 'button',
							class: 'item-detail',
							isRender: true
						}, {
							text: '估值',
							type: 'button',
							class: 'item-assess',
							isRender: false
						}, {
							text: '成立',
							type: 'button',
							class: 'item-establish',
							isRender: (row.state === 'collecting' || row.state === 'meetingPass') && row.lifeState === 'PREPARE',
						}, {
							text: '不成立',
							type: 'button',
							class: 'item-unEstablish',
							isRender: (row.state === 'collecting' || row.state === 'meetingPass') && row.lifeState === 'PREPARE',
						}, {
							text: '本息兑付',
							type: 'button',
							class: 'item-targetIncome',
							isRender: row.state !== 'invalid' && row.lifeState === 'STAND_UP', // 只有已经成立后的标的才能进行本息兑付
							//              	    	isRender: true,
						}, {
							text: '逾期',
							type: 'button',
							class: 'item-overdue',
							isRender: row.state !== 'invalid' && row.lifeState === 'STAND_UP', // 只有已经成立后的标的才能进行逾期
						}, {
							text: '移除出库',
							type: 'button',
							class: 'item-remove',
							isRender: row.state !== 'invalid' && row.state !== 'metting'
						}, {
							text: '财务数据',
							type: 'button',
							class: 'item-financialData',
							//isRender: row.state == 'establish',
							isRender: false,
						}];
						return util.table.formatter.generateButton(buttons);
					},
					events: {
						'click .item-detail': function(e, value, row) { // 标的详情
							http.post(config.api.targetDetQuery, {
								data: {
									oid: row.oid
								},
								contentType: 'form'
							}, function(result) {
								var data = result.investment;
								data = formatTargetData(data); // 格式化标的数据
								data.riskRate = util.table.convertRisk(data.riskRate); // 格式化风险等级								
								$$.detailAutoFix($('#detTargetForm'), data); // 自动填充详情
								if (data.state != 'reject') { // 被驳回
									$("#rejectDesc").hide()
								} else {
									$("#rejectDesc").show()
								}
								if (data.state == 'collecting' || data.state == 'meeting') {
									http.post(config.api.targetNewMeeting, {
										data: {
											investmentOid: row.oid
										},
										contentType: 'form'
									}, function(result) {
										var data = result.data;
										$$.detailAutoFix($('#meetingDetForm'), data); // 自动填充详情
										http.post(config.api.meetingTargetVoteDet, {
												data: {
													meetingOid: data.oid,
													targetOid: row.oid
												},
												contentType: 'form'
											},
											function(obj) {
												$('#detVoteTable').bootstrapTable('load', obj)
											});
									})
									$('#meetingDet').show();
								} else {
									$('#meetingDet').hide();
								}
								$('#targetDetailModal').modal('show');
							})

						},
						'click .item-assess': function(e, value, row) { // 标的估值
							// 需求还未确定
							alert('敬请期待!!!');
						},
						'click .item-establish': function(e, value, row) { // 标的成立
							initEstablish(row);
							/* 如果已持有份额小于0则弹警告窗
							if (row.holdAmount <= 0) {
								$("#confirmTitle").html("标的无持有份额,确定要成立？");
								$$.confirm({
									container: $('#doConfirm'),
									trigger: this,
									accept: function() {
										initEstablish(row);
									}
								});
							} else {
								initEstablish(row);
							}
							*/
						},
						'click .item-unEstablish': function(e, value, row) { // 标的不成立
							initUnEstablish(row);
							/*
							如果已持有份额大于0则弹警告窗
							if (row.holdAmount > 0) {
								$("#confirmTitle").html("标的已持有份额,确定不成立？");
								$$.confirm({
									container: $('#doConfirm'),
									trigger: this,
									accept: function() {
										initUnEstablish(row);
									}
								});
							} else {
								initUnEstablish(row);
							}
							*/
						},
						'click .item-targetIncome': function(e, value, row) { // 标的本息兑付
							targetInfo = row;
							// 初始化数据表格                       
							$('#incomeTable').bootstrapTable('refresh');
							// 重置和初始化表单验证
							$("#targetIncomeForm").validator('destroy')
							util.form.validator.init($("#targetIncomeForm"));

							http.post(config.api.targetDetQuery, {
									data: {
										oid: row.oid
									},
									contentType: 'form'
								},
								function(obj) {
									var data = obj.investment;
									if (!data) {
										toastr.error('标的详情数据不存在', '错误信息', {
											timeOut: 10000
										});
									}
									data.targetOid = data.oid;
									$$.detailAutoFix($('#targetDetailIncome'), formatTargetData(data)); // 自动填充详情1
									$$.formAutoFix($('#targetIncomeForm'), data); // 自动填充表单
								});
							$('#targetIncomeModal').modal('show');
						},
						'click .item-overdue': function(e, value, row) { // 逾期
							/**
                	   不需要弹窗
                	   
                	  $("#confirmTitle").html("确定投资标的逾期？");
						$$.confirm({
							container: $('#doConfirm'),
							trigger: this,
							accept: function() {
								http.post(config.api.overdue, {
									data: {
										oid: row.oid
									},
									contentType: 'form'
								}, function(result) {
									if (result.errorCode == 0) {
										$('#dataTable').bootstrapTable('refresh');
									} else {
										alert('逾期投资标的失败');
									}
								})
							}
						});
						*/
							/*  需要弹窗的 */

							// 重置和初始化表单验证
							$("#overdueForm").validator('destroy')
							util.form.validator.init($("#overdueForm"));

							http.post(config.api.targetDetQuery, {
									data: {
										oid: row.oid
									},
									contentType: 'form'
								},
								function(obj) {
									var data = obj.investment;
									if (!data) {
										toastr.error('标的详情数据不存在', '错误信息', {
											timeOut: 10000
										});
									}
									$$.detailAutoFix($('#targetDetailOverdue'), formatTargetData(data)); // 自动填充详情
									//                		  $$.formAutoFix($('#overdueForm'), data); // 自动填充表单
									$(document.overdueForm.oid).val(data.oid); // 设置投资标的oid
								});
							$('#overdueModal').modal('show');

						},
						'click .item-remove': function(e, value, row) { // 移除出库
							$("#confirmTitle").html("确定移除投资标的？")
							$$.confirm({
								container: $('#doConfirm'),
								trigger: this,
								accept: function() {
									http.post(config.api.targetInvalid, {
										data: {
											oid: row.oid
										},
										contentType: 'form'
									}, function(result) {
										if (result.errorCode == 0) {
											$('#dataTable').bootstrapTable('refresh');
										} else {
											alert('移除投资标的失败');
										}
									})
								}
							})

						},
						'click .item-financialData': function(e, value, row) { // 财务数据
							// 需求还未确定
							alert('敬请期待!!!');
						}

					}
				}]
			}

			// 分页配置
			var incomePageOptions = {
					page: 1,
					rows: 10
				}
				// 数据表格配置
			var incomeTableConfig = {
				ajax: function(origin) {
					if (incomePageOptions.targetOid) {
						http.post(config.api.investmentTargetIncomeList, {
							data: incomePageOptions,
							contentType: 'form'
						}, function(rlt) {
							origin.success(rlt)
						})
					}
				},
				pageNumber: incomePageOptions.page,
				pageSize: incomePageOptions.rows,
				pagination: true,
				sidePagination: 'server',
				pageList: [10, 20, 30, 50, 100],
				queryParams: function(val) {
					incomePageOptions.targetOid = targetInfo ? targetInfo.oid : "";
					incomePageOptions.rows = val.limit
					incomePageOptions.page = parseInt(val.offset / val.limit) + 1
					return val
				},
				onLoadSuccess: function() {},
				columns: [{
					//编号
					// field: 'oid',
					visible: false,
					width: 60,
					formatter: function(val, row, index) {
						return index + 1
					}
				}, { // 兑付期数
					field: 'seq',

				}, { // 实际支付收益
					field: 'incomeRate',
					formatter: function(val, row, index) {
						if (val)
							return val.toFixed(2) + "%";
					}
				}, { // 收益支付日
					field: 'incomeDate',

				}, { // 录入时间
					field: 'createTime',
					visible: false,
				}, { // 操作员
					field: 'operator',
					visible: false,
				}, ],
			}

			// 初始化数据表格
			$('#incomeTable').bootstrapTable(incomeTableConfig);

			// 初始化数据表格
			$('#dataTable').bootstrapTable(tableConfig)
				// 搜索表单初始化
			$$.searchInit($('#searchForm'), $('#dataTable'))

			// 成立 按钮点击事件
			$("#establishSubmit").click(function() {
				if (!$('#establishForm').validator('doSubmitCheck')) return
				$("#establishForm").ajaxSubmit({
					type: "post", //提交方式  
					//dataType:"json", //数据类型'xml', 'script', or 'json'  
					url: config.api.establish,
					success: function(data) {
						$('#establishForm').clearForm();
						$('#establishModal').modal('hide');
						$('#dataTable').bootstrapTable('refresh');
					}
				});

			});

			// 不成立 按钮点击事件
			$("#unEstablishSubmit").click(function() {
				if (!$('#unEstablishForm').validator('doSubmitCheck')) return
				$("#unEstablishForm").ajaxSubmit({
					type: "post", //提交方式  
					//dataType:"json", //数据类型'xml', 'script', or 'json'  
					url: config.api.unEstablish,
					success: function(data) {
						$('#unEstablishForm').clearForm();
						$('#unEstablishModal').modal('hide');
						$('#dataTable').bootstrapTable('refresh');
					}
				});

			});

			// 逾期 按钮点击事件     暂时没用到
			$("#overdueSubmit").click(function() {
				if (!$('#overdueForm').validator('doSubmitCheck')) return
				$("#overdueForm").ajaxSubmit({
					type: "post", //提交方式  
					//dataType:"json", //数据类型'xml', 'script', or 'json'  
					url: config.api.overdue,
					success: function(data) {
						$('#overdueForm').clearForm();
						$('#overdueModal').modal('hide');
						$('#dataTable').bootstrapTable('refresh');
					}
				});

			});

			// 本息兑付 按钮点击事件
			$("#targetIncomeSubmit").click(function() {
				if (!$('#targetIncomeForm').validator('doSubmitCheck')) return
				$("#targetIncomeForm").ajaxSubmit({
					type: "post", //提交方式  
					//dataType:"json", //数据类型'xml', 'script', or 'json'  
					url: config.api.targetIncomeSave,
					success: function(data) {
						$('#targetIncomeForm').clearForm();
						$('#targetIncomeModal').modal('hide');
						$('#dataTable').bootstrapTable('refresh');
					}
				});

			});

			function initEstablish(row) {

				// 重置和初始化表单验证
				$("#establishForm").validator('destroy')
				util.form.validator.init($("#establishForm"));

				// 初始化   付息日 
				for (var i = 1; i <= 30; i++) {
					var option = $("<option>").val(i).text(i);
					$(establishForm.accrualDate).append(option);
				}
				$(establishForm.accrualDate).val(10); // 默认10个工作日

				http.post(config.api.targetDetQuery, {
						data: {
							oid: row.oid
						},
						contentType: 'form'
					},
					function(obj) {
						var data = obj.investment;
						if (!data) {
							toastr.error('标的详情数据不存在', '错误信息', {
								timeOut: 10000
							});
						}
						$$.detailAutoFix($('#targetDetailEstablish'), formatTargetData(data)); // 自动填充详情
						//$$.detailAutoFix($('#establishForm'), data); // 自动填充详情

						$$.formAutoFix($('#establishForm'), data); // 自动填充表单
					});
				$('#establishModal').modal('show');
			}

			function initUnEstablish(row) {

				// 重置和初始化表单验证
				$("#unEstablishForm").validator('destroy')
				util.form.validator.init($("#unEstablishForm"));

				http.post(config.api.targetDetQuery, {
						data: {
							oid: row.oid
						},
						contentType: 'form'
					},
					function(obj) {
						var data = obj.investment;
						if (!data) {
							toastr.error('标的详情数据不存在', '错误信息', {
								timeOut: 10000
							});
						}
						$$.detailAutoFix($('#targetDetailUnEstablish'), formatTargetData(data)); // 自动填充详情
						//$$.detailAutoFix($('#unEstablishForm'), data); // 自动填充详情
						$$.formAutoFix($('#unEstablishForm'), data); // 自动填充表单
					});
				$('#unEstablishModal').modal('show');
			}

			function getQueryParams(val) {
				var form = document.searchForm
				$.extend(pageOptions, util.form.serializeJson(form)); //合并对象，修改第一个对象

				pageOptions.rows = val.limit
				pageOptions.page = parseInt(val.offset / val.limit) + 1

				return val
			}

			/**
			 * 格式化投资标的信息
			 * @param {Object} t
			 */
			function formatTargetData(t) {
				if (t) {
					var t2 = {};
					$.extend(t2, t); //合并对象，修改第一个对象
					t2.expAror = t2.expAror ? t2.expAror.toFixed(2) + '%' : "";
					t2.collectIncomeRate = t2.collectIncomeRate ? t2.collectIncomeRate.toFixed(2) + '%' : "";

					t2.raiseScope = t2.raiseScope + '万';
					t2.life = t2.life + util.enum.transform('lifeUnit', t2.lifeUnit);
					t2.floorVolume = t2.floorVolume + '元';
					t2.contractDays = t2.contractDays + '天/年';
					t2.collectDate = t2.collectStartDate + " 至 " + t2.collectEndDate
					t2.riskRate = util.table.convertRisk(t2.riskRate); // 格式化风险等级

					return t2;
				}
				return t;
			}

			/**
			 * 格式化底层项目信息
			 * @param {Object} p
			 */
			function formatProjectData(p) {
				if (p) {
					var p2 = {};
					$.extend(t2, t); //合并对象，修改第一个对象

					p2.pledgeRatio = p2.pledgeRatio ? p2.pledgeRatio.toFixed(2) + '%' : "";
					p2.spvTariff = p2.spvTariff ? p2.spvTariff.toFixed(2) + '%' : "";

					return p2;
				}
				return p;
			}
		}
	}
})