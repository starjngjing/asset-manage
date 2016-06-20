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
						field: 'accrualType',
						formatter: function(val) {
							return util.enum.transform('ACCRUALTYPE', val);
						}
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
								var temp = (val / 10000).toFixed(0) + '万';
							return temp;
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
								return val.toFixed(2) + "%";
						}
					}, {
						field: 'state',
						formatter: function(val) {
							return util.enum.transform('targetStates', val);
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
							'click .item-project': function(e, value, row) { // 底层项目 按钮点击事件
								targetInfo = row; // 变更某一行 投资标的信息
								//								$$.detailAutoFix($('#targetDetail'), targetInfo); // 自动填充详情
								//								$$.formAutoFix($('#targetDetail'), targetInfo); // 自动填充表单
								//								// 给项目表单的 标的id属性赋值
								//								$("#targetOid")[0].value = targetInfo.oid;
								// 初始化底层项目表格
								$('#projectTable').bootstrapTable(projectTableConfig)
								$('#projectTable').bootstrapTable('refresh'); // 项目表单重新加载
								$$.searchInit($('#projectSearchForm'), $('#projectTable'))
								$('#projectDataModal').modal('show');
							},
							'click .item-detail': function(e, value, row) {
								http.post(config.api.targetDetQuery, {
									data: {
										oid: row.oid
									},
									contentType: 'form'
								}, function(result) {
									var data = result.investment;
									data.riskRate = util.table.formatter.convertRisk(data.riskRate); // 格式化风险等级
									$$.detailAutoFix($('#detTargetForm'), data); // 自动填充详情
									//									$$.formAutoFix($('#detTargetForm'), data); // 自动填充表单
									$('#targetDetailModal').modal('show');
								})
							}
						}
					}]
				}
				// 初始化表格
			$('#targetAccessTable').bootstrapTable(tableConfig)

			var prjPageOptions = {}
			var projectTableConfig = {
				ajax: function(origin) {
					http.post(config.api.targetProjectList, {
						data: prjPageOptions,
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
				queryParams: function(val) {
					var form = document.projectSearchForm
					$.extend(prjPageOptions, util.form.serializeJson(form)); //合并对象，修改第一个对象

					prjPageOptions.rows = val.limit
					prjPageOptions.page = parseInt(val.offset / val.limit) + 1
					prjPageOptions.targetOid = targetInfo.oid.trim(); // 标的id					

					return val
				},
				columns: [{
					//编号
					// field: 'oid',
					width: 60,
					formatter: function(val, row, index) {
						return index + 1
					}
				}, {
					//项目名称
					field: 'projectName',
				}, {
					//项目项目经理
					field: 'projectManager',
				}, {
					//项目项目类型
					field: 'projectType',
					formatter: function(val) {
						return util.enum.transform('PROJECTTYPE', val);
					}
				}, {
					//城市
					field: 'projectCity',
				}, {
					//创建时间
					field: 'createTime',
				}, {
					//操作
					align: 'center',
					formatter: function(val, row) {
						var buttons = [{
							text: '详情',
							type: 'button',
							class: 'item-project-detail',
							isRender: true
						}];
						return util.table.formatter.generateButton(buttons);
					},
					events: {
						'click .item-project-detail': function(e, value, row) { // 底层项目详情
							http.post(config.api.projectDetail, {
								data: {
									oid: row.oid
								},
								contentType: 'form'
							}, function(result) {
								var data = result.data;
								if (!data) {
									alert('查询底层项目详情失败');
								} else {
									$$.detailAutoFix($('#targetDetail_2'), formatTargetData(targetInfo)); // 自动填充详情
									//									$$.detailAutoFix($('#projectDetail'), row); // 自动填充详情-取表格里的内容
									$$.detailAutoFix($('#projectDetail'), data); // 自动填充详情-取后台返回的内容
									if(data.warrantor === 'yes') { // 担保人信息									
										$("#warrantorDetail").show()
									} else {
										$("#warrantorDetail").hide()
									}
									if(data.pledge === 'yes') { // 抵押人信息									
										$("#pledgeDetail").show()
									} else {
										$("#pledgeDetail").hide()
									}
									if(data.hypothecation === 'yes') { // 质押人信息									
										$("#hypothecationDetail").show()
									} else {
										$("#hypothecationDetail").hide()
									}
									
									/* 判断项目类型 */
									if(data.projectType === 'PROJECTTYPE_01') { // 金融类项目									
										$("#estateDetail").hide()
										$("#financeDetai").show()
									} else if(data.projectType === 'PROJECTTYPE_02') { // 地产类项目									
										$("#estateDetail").show()
										$("#financeDetai").hide()
									} else {
										$("#estateDetail").hide()
										$("#financeDetai").hide()
									}
									
									$('#projectDetailModal').modal('show');
								}
							});

						}
					}
				}]
			};

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