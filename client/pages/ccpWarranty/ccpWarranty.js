/**
 * 担保对象权限配置
 */
define([
	'http',
	'config',
	'util',
	'extension'
], function(http, config, util, $$) {
	return {
		name: 'ccpWarranty',
		init: function() {

			$('#ccpWarrantorTable').bootstrapTable({

				ajax: function(origin) {
					http.post(config.api.system.config.ccp.warrantor.search, {
						data: {},
						contentType: 'form'
					}, function(rlt) {
						origin.success(rlt)
					})
				},
				columns: [{
					field: 'oid',
					formatter: function(val, row, index) {
						return index + 1
					}
				}, {
					field: 'score',
					formatter: function(val, row) {
						return row.lowScore + ' - ' + row.highScore;
					}
				}, {
					field: 'title'
				}, {
					field: 'weight',
					formatter: function(val, row) {
						return parseInt(val * 100) + '%';
					}
				}, {
					align: 'center',
					formatter: function(val, row) {
						var buttons = [{
							text: '修改',
							type: 'button',
							class: 'item-update',
							isRender: true
						}, {
							text: '删除',
							type: 'button',
							class: 'item-delete',
							isRender: true
						}]
						return util.table.formatter.generateButton(buttons);
					},
					events: {
						'click .item-update': function(e, value, row) {
							$('#updateCcpWarrantorModal').modal('show');
							row.weight100 = parseInt(row.weight * 100);
							$$.formAutoFix($('#updateCcpWarrantorForm'), row);
						},
						'click .item-delete': function(e, value, row) {
							$("#deleteCcpWarrantorConfirmTitle").html("确定删除担保对象权数配置？");
							$$.confirm({
								container: $('#deleteCcpWarrantorModal'),
								trigger: this,
								accept: function() {
									http.post(config.api.system.config.ccp.warrantor.delete, {
										data: {
											oid: row.oid
										},
										contentType: 'form'
									}, function(result) {
										$('#ccpWarrantorTable').bootstrapTable('refresh');
									})
								}
							})

						}
					}
				}]

			});

			$('#ccpWarrantorAdd').on('click', function() {
				$('#addCcpWarrantorModal').modal('show');
			});

			$('#saveCcpWarrantor').on('click', function() {
				$('#addCcpWarrantorForm').ajaxSubmit({
					url: config.api.system.config.ccp.warrantor.create,
					success: function(result) {
						$('#addCcpWarrantorForm').clearForm();
						$('#addCcpWarrantorModal').modal('hide');
						$('#ccpWarrantorTable').bootstrapTable('refresh');
					}
				})
			});

			$('#updateCcpWarrantor').on('click', function() {
				$('#updateCcpWarrantorForm').ajaxSubmit({
					url: config.api.system.config.ccp.warrantor.update,
					success: function(result) {
						$('#updateCcpWarrantorForm').clearForm();
						$('#updateCcpWarrantorModal').modal('hide');
						$('#ccpWarrantorTable').bootstrapTable('refresh');
					}
				})
			});

			$('#ccpWarrantyModeTable').bootstrapTable({

				ajax: function(origin) {
					http.post(config.api.system.config.ccp.warrantyMode.search, {
						data: {},
						contentType: 'form'
					}, function(rlt) {
						origin.success(rlt)
					})
				},
				columns: [{
					field: 'oid',
					formatter: function(val, row, index) {
						return index + 1
					}
				}, {
					field: 'type',
					formatter: function(val, row) {
						if (row.showType) {
							if (val == 'GUARANTEE')
								return '保证方式';
							if (val == 'MORTGAGE')
								return '抵押方式';
							if (val == 'HYPOTHECATION')
								return '质押方式';
						} else {
							return '';
						}
					}
				}, {
					field: 'title'
				}, {
					field: 'weight',
					formatter: function(val, row) {
						return parseInt(val * 100) + '%';
					}
				}, {
					align: 'center',
					formatter: function(val, row) {
						var buttons = [{
							text: '修改',
							type: 'button',
							class: 'item-update',
							isRender: true
						}, {
							text: '删除',
							type: 'button',
							class: 'item-delete',
							isRender: true
						}]
						return util.table.formatter.generateButton(buttons);
					},
					events: {
						'click .item-update': function(e, value, row) {
							$('#updateCcpWarrantyModeModal').modal('show');
							row.weight100 = parseInt(row.weight * 100);
							$$.formAutoFix($('#updateCcpWarrantyModeForm'), row);
						},
						'click .item-delete': function(e, value, row) {
							$("#deleteCcpWarrantyModeConfirmTitle").html("确定删除担保对象权数配置？");
							$$.confirm({
								container: $('#deleteCcpWarrantyModeModal'),
								trigger: this,
								accept: function() {
									http.post(config.api.system.config.ccp.warrantyMode.delete, {
										data: {
											oid: row.oid
										},
										contentType: 'form'
									}, function(result) {
										$('#ccpWarrantyModeTable').bootstrapTable('refresh');
									})
								}
							})

						}
					}
				}]

			});

			$('#ccpWarrantyModeAdd').on('click', function() {
				$('#addCcpWarrantyModeModal').modal('show');
			});

			$('#saveCcpWarrantyMode').on('click', function() {
				$('#addCcpWarrantyModeForm').ajaxSubmit({
					url: config.api.system.config.ccp.warrantyMode.create,
					success: function(result) {
						$('#addCcpWarrantyModeForm').clearForm();
						$('#addCcpWarrantyModeModal').modal('hide');
						$('#ccpWarrantyModeTable').bootstrapTable('refresh');
					}
				})
			});

			$('#updateCcpWarrantyMode').on('click', function() {
				$('#updateCcpWarrantyModeForm').ajaxSubmit({
					url: config.api.system.config.ccp.warrantyMode.update,
					success: function(result) {
						$('#updateCcpWarrantyModeForm').clearForm();
						$('#updateCcpWarrantyModeModal').modal('hide');
						$('#ccpWarrantyModeTable').bootstrapTable('refresh');
					}
				})
			});

			$('#ccpWarrantyExpireTable').bootstrapTable({
				ajax: function(origin) {
					http.post(config.api.system.config.ccp.warrantyExpire.search, {
						data: {},
						contentType: 'form'
					}, function(rlt) {
						origin.success(rlt)
					})
				},
				columns: [{
					field: 'oid',
					formatter: function(val, row, index) {
						return index + 1
					}
				}, {
					field: 'title'
				}, {
					field: 'weight',
					formatter: function(val, row) {
						return parseInt(val * 100) + '%';
					}
				}, {
					align: 'center',
					formatter: function(val, row) {
						var buttons = [{
							text: '修改',
							type: 'button',
							class: 'item-update',
							isRender: true
						}, {
							text: '删除',
							type: 'button',
							class: 'item-delete',
							isRender: true
						}]
						return util.table.formatter.generateButton(buttons);
					},
					events: {
						'click .item-update': function(e, value, row) {
							$('#updateCcpWarrantyExpireModal').modal('show');
							row.weight100 = parseInt(row.weight * 100);
							$$.formAutoFix($('#updateCcpWarrantyExpireForm'), row);
						},
						'click .item-delete': function(e, value, row) {
							$("#deleteCcpWarrantyExpireConfirmTitle").html("确定删除担保期限权数配置？");
							$$.confirm({
								container: $('#deleteCcpWarrantyExpireModal'),
								trigger: this,
								accept: function() {
									http.post(config.api.system.config.ccp.warrantyExpire.delete, {
										data: {
											oid: row.oid
										},
										contentType: 'form'
									}, function(result) {
										$('#ccpWarrantyExpireTable').bootstrapTable('refresh');
									})
								}
							})

						}
					}
				}]
			});

			$('#ccpWarrantyLevelTable').bootstrapTable({
				ajax: function(origin) {
					http.post(config.api.system.config.ccp.warrantyLevel.search, {
						data: {},
						contentType: 'form'
					}, function(rlt) {
						origin.success(rlt)
					})
				},
				onLoadSuccess:function(data){
					/**
		            * 获取风险等级配置值信息存到config中，并首次全局初始化
		            */
		            util.initWarrantyLevelOptions(data);
				},
				columns: [{
					align: 'center',
					formatter: function(val, row, index) {
						return index + 1
					}
				}, {
					align: 'center',
					field: 'wlevel',
					formatter: function(val, row, index) {
						return util.table.formatter.convertRiskLevel(val);
					}
				}, {
					align: 'center',
					field: 'name'
				}, {
					align: 'center',
					formatter: function(val, row) {
						return '<i>' + row.coverLow + (row.lowFactor == 0 || row.lowFactor ? row.lowFactor : '∞') + ', ' + (row.highFactor == 0 || row.highFactor ? row.highFactor : '∞') + row.coverHigh + '</i>';
					}
				}, {
					align: 'center',
					formatter: function(val, row) {
						var buttons = [{
							text: '修改',
							type: 'button',
							class: 'item-update',
							isRender: true
						}, {
							text: '删除',
							type: 'button',
							class: 'item-delete',
							isRender: true
						}]
						return util.table.formatter.generateButton(buttons);
					},
					events: {
						'click .item-update': function(e, value, row) {
							$$.formAutoFix($('#ccpWarrantyLevelForm'), row);
							// 重置和初始化表单验证
							$("#ccpWarrantyLevelForm").validator('destroy')
							util.form.validator.init($("#ccpWarrantyLevelForm"));
							$('#ccpWarrantyLevelModal').modal('show');
							
							$(document.ccpWarrantyLevelForm.wlevel).off();
						},
						'click .item-delete': function(e, value, row) {
							$("#deleteCcpWarrantyExpireConfirmTitle").html("确定删除风险等级配置？");
							$$.confirm({
								container: $('#deleteCcpWarrantyExpireModal'),
								trigger: this,
								accept: function() {
									http.post(config.api.system.config.ccp.warrantyLevel.delete, {
										data: {
											oid: row.oid
										},
										contentType: 'form'
									}, function(result) {
										$('#ccpWarrantyLevelTable').bootstrapTable('refresh');
									})
								}
							})

						}
					}
				}]
			});

			$('#ccpWarrantyExpireAdd').on('click', function() {
				$('#addCcpWarrantyExpireModal').modal('show');
			});

			$('#saveCcpWarrantyExpire').on('click', function() {
				$('#addCcpWarrantyExpireForm').ajaxSubmit({
					url: config.api.system.config.ccp.warrantyExpire.create,
					success: function(result) {
						$('#addCcpWarrantyExpireForm').clearForm();
						$('#addCcpWarrantyExpireModal').modal('hide');
						$('#ccpWarrantyExpireTable').bootstrapTable('refresh');
					}
				})
			});

			$('#updateCcpWarrantyExpire').on('click', function() {
				$('#updateCcpWarrantyExpireForm').ajaxSubmit({
					url: config.api.system.config.ccp.warrantyExpire.update,
					success: function(result) {
						$('#updateCcpWarrantyExpireForm').clearForm();
						$('#updateCcpWarrantyExpireModal').modal('hide');
						$('#ccpWarrantyExpireTable').bootstrapTable('refresh');
					}
				})
			});

			$('#ccpWarrantyLevelAdd').on('click', function() {
				$('#ccpWarrantyLevelForm').resetForm(); // 先清理表单
				$(document.ccpWarrantyLevelForm.oid).removeAttr('value'); // 清理隐藏域oid

				// 重置和初始化表单验证
				$("#ccpWarrantyLevelForm").validator('destroy')
				util.form.validator.init($("#ccpWarrantyLevelForm"));
				$('#ccpWarrantyLevelModal').modal('show');
				
				$(document.ccpWarrantyLevelForm.wlevel).off().on('change', function() {
					$(document.ccpWarrantyLevelForm.name).val($(this).find("option:selected").text());
				});
				$(document.ccpWarrantyLevelForm.wlevel).change()
			});

			$('#ccpWarrantyLevelSubmit').on('click', function() {
				if (!$('#ccpWarrantyLevelForm').validator('doSubmitCheck')) return
				$('#ccpWarrantyLevelForm').ajaxSubmit({
					url: config.api.system.config.ccp.warrantyLevel.save,
					success: function(result) {
						$('#ccpWarrantyLevelForm').clearForm();
						$('#ccpWarrantyLevelModal').modal('hide');
						$('#ccpWarrantyLevelTable').bootstrapTable('refresh');
					}
				})
			});

		}
	}
})