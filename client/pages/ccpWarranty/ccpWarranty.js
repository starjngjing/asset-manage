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
				/*
				$('#ccpWarrantyLevelForm').resetForm(); // 先清理表单
				$(document.ccpWarrantyLevelForm.oid).removeAttr('value'); // 清理隐藏域oid

				// 重置和初始化表单验证
				$("#ccpWarrantyLevelForm").validator('destroy')
				util.form.validator.init($("#ccpWarrantyLevelForm"));
				
				$(document.ccpWarrantyLevelForm.wlevel).off().on('change', function() {
					$(document.ccpWarrantyLevelForm.name).val($(this).find("option:selected").text());
				});
				$(document.ccpWarrantyLevelForm.wlevel).change()
				*/
				$('#warrantyLevelFormOptions').empty();
				http.post(config.api.system.config.ccp.warrantyLevel.search, {
		            contentType: 'form'
		          }, function (data) {
		          	if(data){
		          		$.each(data, function(i, item) {
			          		$('#warrantyLevelFormOptions').append(initWarrantyLevel(i, item));
		          		});
		          	}
		          })
				$('#ccpWarrantyLevelModal').modal('show');
			});

			$('#ccpWarrantyLevelSubmit').on('click', function() {
				/*
				if (!$('#ccpWarrantyLevelForm').validator('doSubmitCheck')) return
				$('#ccpWarrantyLevelForm').ajaxSubmit({
					url: config.api.system.config.ccp.warrantyLevel.save,
					success: function(result) {
						$('#ccpWarrantyLevelForm').clearForm();
						$('#ccpWarrantyLevelModal').modal('hide');
						$('#ccpWarrantyLevelTable').bootstrapTable('refresh');
					}
				})
				*/
				var res = [];// 各个form校验结果
				var x = $('#warrantyLevelFormOptions').children(); // 指标项配置
				for (var i = 0; i < x.length; i++) {
					var frm = x[i];
					res.push($(frm).validator('doSubmitCheck')); // 校验指标项配置form
				}
				for (var i = 0; i < res.length; i++) {
					if(!res[i])return false;
				}
				var json = {
					options: []
				};
				$.each(x, function(i, v) { // 遍历指标项配置
					var ov = {};
					$.each($(v).serializeArray(), function(i, v) {
						ov[v.name] = v.value;
					});
					json.options.push(ov);
				});
				http.post(config.api.system.config.ccp.warrantyLevel.saveList, {
					data: JSON.stringify(json)
				}, function(result) {
					$('#warrantyLevelFormOptions').empty();
					$('#ccpWarrantyLevelModal').modal('hide');
					$('#ccpWarrantyLevelTable').bootstrapTable('refresh');
				});
			});
			
			function initWarrantyLevel(i, item) {
				if(item) {
						var form = $('<form id="' + i + 'WarrantyLevelForm"></form');
						var row = $('<div class="row"></div>');
						row.appendTo(form);
						var x0 = $('<div class="col-sm-6 col-xs-6"> <div class="row"> <div class="col-xs-4"> <div class="form-group"> <div class="input-group"> <div id="levelName" class="input-group-addon">等级</div> <select name="wlevel" class="form-control input-sm" placeholder="风险等级" required data-error="风险等级不能为空" disabled> <option value="LOW" '+(item.wlevel==="LOW"?"selected":"")+'>低</option> <option value="MID" '+(item.wlevel==="MID"?"selected":"")+'>中</option> <option value="HIGH" '+(item.wlevel==="HIGH"?"selected":"")+'>高</option> </select> </div> <div class="help-block with-errors text-red"></div> </div> </div> <div class="col-xs-8"> <div class="form-group"> <div class="input-group"> <div id="levelName" class="input-group-addon">名称</div> <input name="name" type="text" class="form-control input-sm" maxlength="5" data-error="风险等级显示名称"> </div> <div class="help-block with-errors text-red"></div> </div> </div> </div> </div>');
						x0.appendTo(row);
						
						var x1 = $('<div class="col-sm-3 col-xs-3"> <div class="row"> <div class="col-xs-5"> <div class="form-group"><select name="coverLow" class="form-control input-sm" required><option value="[">[</option><option value="(">(</option></select> <div class="help-block with-errors text-red"></div> </div> </div> <div class="col-xs-7"> <div class="form-group"> <div class="input-group"><input name="lowFactor" type="text" class="form-control input-sm" maxlength="60" required data-validpositive="true" data-validfloat="3.4" data-error="风险系数区间必须为前2位后4位小数"></div> <div class="help-block with-errors text-red"></div> </div> </div> </div> </div>');
						x1.appendTo(row);
						
						var x2 = $('<div class="col-sm-3 col-xs-3"> <div class="row"> <div class="col-xs-7"> <div class="form-group"> <div class="input-group range"><input name="highFactor" type="text" class="form-control input-sm" maxlength="64" data-validpositive="true" data-validfloat="3.4" data-error="风险系数区间必须为前2位后4位小数"></div> <div class="help-block with-errors text-red"></div> </div> </div> <div class="col-xs-5" > <div class="form-group"><select name="coverHigh" class="form-control input-sm" required><option value="]">]</option><option value=")">)</option></select> <div class="help-block with-errors text-red"></div> </div> </div> </div> </div>');
						x2.appendTo(row);
						
						var l = $('<input type="hidden" name="wlevel" />');
						l.appendTo(form);
						
						var id = $('<input type="hidden" name="oid" />');
						id.appendTo(form);
						
						$$.formAutoFix($(form), item);
						$(form).validator('destroy');
						util.form.validator.init($(form));
					
					return form;
				}
				return null;
			}

		}
	}
})