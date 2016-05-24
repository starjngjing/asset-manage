/**
 * 标的申请管理
 */
define([
	'http',
	'config',
	'util',
	'extension'
], function(http, config, util, $$) {
	return {
		name: 'targetApply',
		init: function() {

			// js逻辑写在这里
			// 分页配置
			var targetInfo; // 缓存 选中的某一行的 投资标的信息
			var pageOptions = {
					number: 1,
					size: 10,
					targetName: '',
					targetType: '',
					state: ''
				}
				// 数据表格配置
			var tableConfig = {
				ajax: function(origin) {
					http.post(config.api.targetListQuery, {
						data: {
							page: pageOptions.number,
							rows: pageOptions.size,
							name: pageOptions.targetName,
							type: pageOptions.targetType,
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
							isRender: row.state == 'waitPretrial' || row.state == 'reject'
						}, {
							text: '提交预审',
							type: 'button',
							class: 'item-check',
							isRender: row.state == 'waitPretrial' || row.state == 'reject'
						}, {
							text: '编辑',
							type: 'button',
							class: 'item-edit',
							isRender: row.state == 'waitPretrial' || row.state == 'reject'
						}, {
							text: '作废',
							type: 'button',
							class: 'item-invalid',
							isRender: row.state != 'invalid'
						}];
						return util.table.formatter.generateButton(buttons);
					},
					events: {
						'click .item-edit': function(e, value, row) {
							http.post(config.api.targetDetQuery, {
								data: {
									oid: row.oid
								},
								contentType: 'form'
							}, function(result) {
								var data = result.investment;
//								$$.detailAutoFix($('#editTargetForm'), data); // 自动填充详情
								$$.formAutoFix($('#editTargetForm'), data); // 自动填充表单
								$('#editTargetModal').modal('show');
							})
						},
						'click .item-check': function(e, value, row) {
							$("#confirmTitle").html("确定提交预审？")
							$$.confirm({
								container: $('#doConfirm'),
								trigger: this,
								accept: function() {
									http.post(config.api.targetExamine, {
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
							$$.detailAutoFix($('#detTargetForm'), row); // 自动填充详情
							$('#targetDetailModal').modal('show');
						},
						'click .item-project': function(e, value, row) { // 底层项目 按钮点击事件
							targetInfo = row; // 变更某一行 投资标的信息
							console.info("targetInfo---------->" + JSON.stringify(targetInfo))

							$$.detailAutoFix($('#targetDetail'), targetInfo); // 自动填充详情
							$$.formAutoFix($('#targetDetail'), targetInfo); // 自动填充表单

							// 给项目表单的 标的id属性赋值
							$("#targetOid")[0].value = targetInfo.oid;
							//111
							// 初始化底层项目表格
							$('#projectTable').bootstrapTable(projectTableConfig)
							$('#projectTable').bootstrapTable('refresh'); // 项目表单重新加载
							$$.searchInit($('#projectSearchForm'), $('#projectTable'))
							$('#projectDataModal').modal('show');

						}
					}
				}]
			};

			var prjPageOptions = {
			}
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
						var buttons = [						               
						    {
								text: '详情',
								type: 'button',
								class: 'item-project-detail',
								isRender: true
						    },
						    {
						    	text: '删除',
						    	type: 'button',
						    	class: 'item-project-delete',
						    	isRender: true
						    }
						];
						return util.table.formatter.generateButton(buttons);
					},
					events: {
						'click .item-project-detail': function(e, value, row) { // 底层项目详情
							$$.detailAutoFix($('#targetDetail_2'), targetInfo); // 自动填充详情
							$$.detailAutoFix($('#projectDetail'), row); // 自动填充详情
							$('#projectDetailModal').modal('show');
						},
						'click .item-project-delete': function(e, value, row) { // 删除底层项目
							$("#confirmTitle").html("确定删除底层项目？")
							$$.confirm({
								container: $('#doConfirm'),
								trigger: this,
								accept: function() {
									//									console.log('targetInfo===>' + JSON.stringify(targetInfo));
									//									console.log('项目row===>' + JSON.stringify(row));
									http.post(config.api.targetProjectDelete, {
										data: {
											targetOid: targetInfo.oid,
											oid: row.oid
										},
										contentType: 'form'
									}, function(result) {
										if (result.errorCode == 0) {

											$('#projectTable').bootstrapTable('refresh');
										} else {
											alert('删除底层项目失败');
										}
									})
								}
							})

						}
					}
				}]
			};
			// 初始化表格
			$('#targetApplyTable').bootstrapTable(tableConfig)
				// 搜索表单初始化
			$$.searchInit($('#targetSearchForm'), $('#targetApplyTable'))
				// 新建标的按钮点击事件
			$('#targetAdd').on('click', function() {
					$('#addTargetModal').modal('show')
				})
				// 新建底层资产按钮点击事件

			$('#assetAdd').on('click', function() {
				$('#addAssetModal').modal('show')
			})
			//新建标的按钮点击事件
			$('#saveTarget').on('click', function() {
				saveTarget();
			})
			//修改标的按钮点击事件
			$('#editTarget').on('click', function() {
				editTarget();
			})

			// 新建底层项目按钮点击事件
			$('#projectAdd').on('click', function() {
				$('#projectForm').clearForm(); // 先清理表单
				util.form.validator.init($("#projectForm")); // 初始化表单验证
				$('#projectModal').modal('show');
			})
			$('#projectSubmit').on('click', function() {
				saveProject();
			})
			$(document.projectForm.projectType).change(function() { // 项目类型
				var ptt = $(this).val();
				if (ptt === 'PROJECTTYPE_01') { // 金融
					$("#estateDiv").hide().find('input').attr('disabled', 'disabled');
					$("#financeDiv").show().find('input').attr('disabled', false);
				} else if (ptt === 'PROJECTTYPE_02') { // 房地产
					$("#estateDiv").show().find('input').attr('disabled', false);
					$("#financeDiv").hide().find('input').attr('disabled', 'disabled');
				} else {
					$("#estateDiv").hide().find('input').attr('disabled', 'disabled');
					$("#financeDiv").hide().find('input').attr('disabled', 'disabled');
				}
				$('#projectForm').validator('destroy'); // 先销毁验证规则
				util.form.validator.init($('#projectForm')); // 然后添加验证规则
			});

			$(document.projectForm.warrantor).each(function(index, item) {
				$(item).on('ifChecked', function(e) { // 是否有担保人
					if ($(this).val() === 'yes')
						$('#prjWarrantorInfo').show().find('input').attr('disabled', false);
					else
						$('#prjWarrantorInfo').hide().find('input').attr('disabled', 'disabled');
					
					$('#projectForm').validator('destroy'); // 先销毁验证规则
					util.form.validator.init($('#projectForm')); // 然后添加验证规则
				});
			})

			$(document.projectForm.pledge).each(function(index, item) {
				$(item).on('ifChecked', function(e) { // 是否有抵押人
					if ($(this).val() === 'yes')
						$('#prjPledgeInfo').show().find('input').attr('disabled', false);
					else
						$('#prjPledgeInfo').hide().find('input').attr('disabled', 'disabled');
					
					$('#projectForm').validator('destroy'); // 先销毁验证规则
					util.form.validator.init($('#projectForm')); // 然后添加验证规则
				});
			})

			function getQueryParams(val) {
				var form = document.targetSearchForm
				pageOptions.size = val.limit
				pageOptions.number = parseInt(val.offset / val.limit) + 1
				pageOptions.targetName = form.searchName.value.trim();
				pageOptions.targetType = form.targetType.value.trim();
				pageOptions.state = form.targetStatus.value.trim();
				return val
			}
		}
	}

	function saveTarget() {
		$('#addTargetForm').ajaxSubmit({
			url: config.api.targetAdd,
			success: function(result) {
				$('#addTargetForm').clearForm();
				$('#addTargetModal').modal('hide');
				$('#targetApplyTable').bootstrapTable('refresh');
			}
		})
	}
	
	function editTarget(){
		$('#editTargetForm').ajaxSubmit({
			url: config.api.targetEdit,
			success: function(result) {
				$('#editTargetForm').clearForm();
				$('#editTargetModal').modal('hide');
				$('#targetApplyTable').bootstrapTable('refresh');
			}
		})
	}

	function saveProject() {
		$('#projectForm').ajaxSubmit({
			type: "post", //提交方式  
			//dataType:"json", //数据类型'xml', 'script', or 'json'  
			url: config.api.saveProject,
			//			contentType : 'application/json',
			success: function(result) {
				$('#projectForm').clearForm();
				$('#projectModal').modal('hide');
				$('#projectTable').bootstrapTable('refresh'); // 项目表单重新加载
				$('#targetApplyTable').bootstrapTable('refresh'); // 标的标的重新加载
			}
		})
	}

})