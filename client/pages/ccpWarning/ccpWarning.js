/**
 * 风险等级评分模型
 */
define([
	'http',
	'config',
	'util',
	'extension'
], function(http, config, util, $$) {
	return {
		name: 'ccpWarning',
		init: function() {
			// js逻辑写在这里

			var createOptions = {
				"NUMBER": function(dataUnit) {
					var rd = util.getRandomString(5);
					var form = $('<form type="NUMBER" id="' + rd + 'NumberForm"></form');
					var row = $('<div class="row"></div>');
					row.appendTo(form);
					var x0 = $('<div class="col-sm-8 col-xs-12"><div class="form-group"><input name="param0" type="text" class="form-control input-sm" placeholder="指标项描述" required maxlength="64">' + initDataUnit(dataUnit) + '<div class="help-block with-errors text-red"></div></div></div>');
					x0.appendTo(row);
					var x1 = $('<div class="col-sm-3 col-xs-6"><div class="form-group"><div class="input-group input-group-sm"><div class="input-group-addon">风险等级</div><select name="wlevel" class="form-control" placeholder="风险等级" required>' + initLevelOption() + '</select></div><div class="help-block with-errors text-red"></div></div></div>');
					x1.appendTo(row);

					var a0 = $('<div class="col-sm-1 col-xs-6"></div>');
					a0.appendTo(row);
					var a1 = $('<div class="form-group"></div>');
					a1.appendTo(a0);
					var dbtn = $('<div class="btn btn-default btn-sm">删除</div>');
					dbtn.appendTo(a1);
					dbtn.on('click', function() {
						form.remove();
					});
					$(form).validator('destroy');
					util.form.validator.init($(form));
					return form;
				},
				"NUMRANGE": function(dataUnit) {
					var rd = util.getRandomString(5);
					var form = $('<form type="NUMRANGE" id="' + rd + 'NumrangeForm"></form');
					var row = $('<div class="row"></div>');
					row.appendTo(form);
					var x0 = $('<div class="col-sm-4 col-xs-6"><div class="row"><div class="col-xs-5"><div class="form-group"><select name="param0" class="form-control input-sm" required><option value="[">[</option><option value="(">(</option></select><div class="help-block with-errors text-red"></div></div></div><div class="col-xs-7"><div class="form-group"><div class="input-group range"><input name="param1" type="text" class="form-control input-sm" maxlength="60" required>' + initDataUnit(dataUnit) + '</div><div class="help-block with-errors text-red"></div></div></div></div></div>');
					x0.appendTo(row);
					var x1 = $('<div class="col-sm-4 col-xs-6"><div class="row"><div class="col-xs-7"><div class="form-group"><div class="input-group range"><input name="param2" type="text" class="form-control input-sm" maxlength="64">' + initDataUnit(dataUnit) + '</div><div class="help-block with-errors text-red"></div></div></div><div class="col-xs-5"><div class="form-group"><select name="param3" class="form-control input-sm" required><option value="]">]</option><option value=")">)</option></select><div class="help-block with-errors text-red"></div></div></div></div></div>');
					x1.appendTo(row);
					var x2 = $('<div class="col-sm-3 col-xs-6"><div class="form-group"><div class="input-group input-group-sm"><div class="input-group-addon">风险等级</div><select name="wlevel" class="form-control" placeholder="风险等级" required>' + initLevelOption() + '</select></div><div class="help-block with-errors text-red"></div></div></div>');
					x2.appendTo(row);

					var a0 = $('<div class="col-sm-1 col-xs-6"></div>');
					a0.appendTo(row);
					var a1 = $('<div class="form-group"></div>');
					a1.appendTo(a0);
					var dbtn = $('<div class="btn btn-default btn-sm">删除</div>');
					dbtn.appendTo(a1);
					dbtn.on('click', function() {
						form.remove();
					});
					$(form).validator('destroy');
					util.form.validator.init($(form));
					return form;
				},
				"TEXT": function(dataUnit) {
					var rd = util.getRandomString(5);
					var form = $('<form type="TEXT" id="' + rd + 'TextForm"></form');
					var row = $('<div class="row"></div>');
					row.appendTo(form);
					var x0 = $('<div class="col-sm-8 col-xs-12"><div class="form-group"><input name="param0" type="text" class="form-control input-sm" placeholder="指标项描述" required><div class="help-block with-errors text-red"></div></div></div>');
					x0.appendTo(row);
					var x1 = $('<div class="col-sm-3 col-xs-6"><div class="form-group"><div class="input-group input-group-sm"><div class="input-group-addon">风险等级</div><select name="wlevel" class="form-control" placeholder="风险等级" required>' + initLevelOption() + '</select></div><div class="help-block with-errors text-red"></div></div></div>');
					x1.appendTo(row);

					var a0 = $('<div class="col-sm-1 col-xs-6"></div>');
					a0.appendTo(row);
					var a1 = $('<div class="form-group"></div>');
					a1.appendTo(a0);
					var dbtn = $('<div class="btn btn-default btn-sm">删除</div>');
					dbtn.appendTo(a1);
					dbtn.on('click', function() {
						form.remove();
					});
					$(form).validator('destroy');
					util.form.validator.init($(form));
					return form;
				}
			};

			var queryParams = {
				type: 'WARNING',
				keyword: ''
			};

			$('#dataTable').bootstrapTable({
				ajax: function(origin) {
					http.post(config.api.system.config.ccr.warning.options.showview, {
						data: queryParams,
						contentType: 'form'
					}, function(rlt) {
						origin.success(rlt);
					});
				},
				queryParams: function(val) {
					var form = document.searchForm;
					queryParams.keyword = form.keyword.value.trim();
				},
				columns: [{
					// 指标分类
					field: 'cateTitle',
					formatter: function(val, row, index) {
						return row.showCate ? '<b>' + val + '</b>' : '';
					}
				}, {
					// 指标项名称
					field: 'indicateTitle',
					width: 500,
					formatter: function(val, row, index) {
						return row.showIndicate ? val : '';
					}
				}, {
					// 指标描述
					field: 'waringTitle',
					formatter: function(val, row, index) {
						return row.showWarning ? val : '';
					}
				}, {
					// 指标项值
					field: 'optionsTitle',
					formatter: function(val, row, index) {
						return row.showOptions ? '<i>' + val + '</i>' : '';
					}
				}, {
					// 风险等级
					field: 'optionsWlevel',
					align: 'center',
					formatter: function(val, row, index) {
						return row.showOptions ? util.table.convertRiskLevel(val) : '';
					}
				}, {
					align: 'center',
					formatter: function(val, row, index) {
						var updateButton = {
							text: '编辑',
							type: 'button',
							class: 'item-update',
							isRender: true
						};

						var deleteButton = {
							text: '删除',
							type: 'button',
							class: 'item-delete',
							isRender: true
						};

						return row.showWarning ? util.table.formatter.generateButton([updateButton, deleteButton]) : '';

					},
					events: {
						'click .item-update': function(e, value, row) {
							http.post(config.api.system.config.ccr.warning.options.preUpdate, {
								data: {
									warningOid: row.warningOid
								},
								contentType: 'form'
							}, function(val) {

								var form = document.updateForm;

								$('#updateForm').resetForm();
								$('#updateForm').validator('destroy');
								util.form.validator.init($('#updateForm'));
								$('#updateFormOptions').empty();
								$('#updateModal').modal('show');

								$$.formAutoFix($('#updateForm'), val);

								if (val.options && val.options.length > 0) {
									var type = val.indicateDataType;
									if (createOptions[type]) {
										$.each(val.options, function(i, item) {
											var option = createOptions[type](val.indicateDataUnit);
											if (option) {
												$('#updateFormOptions').append(option);
											}
											$$.formAutoFix(option, item);
										});
									}
								}

							});
						},
						'click .item-delete': function(e, value, row) {
							$("#deleteConfirmTitle").html("确定删除评分模型？");
							$$.confirm({
								container: $('#deleteModal'),
								trigger: this,
								accept: function() {
									http.post(config.api.system.config.ccr.warning.options.batchDelete, {
										data: {
											warningOid: row.warningOid
										},
										contentType: 'form'
									}, function(result) {
										$('#dataTable').bootstrapTable('refresh');
									})
								}
							})
						}
					}
				}]
			});

			$$.searchInit($('#searchForm'), $('#dataTable'));

			$('#eventAdd').on('click', function() {
				http.post(config.api.system.config.ccr.indicate.options, {
						data: {
							type: 'WARNING'
						},
						contentType: 'form'
					},
					function(val) {
						var cascade = {};
						var options = {};
						var form = document.addForm;

						$('#addForm').resetForm();
						$('#addForm').validator('destroy');
						util.form.validator.init($('#addForm'));
						$('#addFormOptions').empty();
						$('#addModal').modal('show');

						$(form.cateOid).empty();
						$.each(val, function(i, value) {
							cascade[value.oid] = value.options;
							$(form.cateOid).append('<option value="' + value.oid + '">' + value.title + '</option>')
						});

						$(form.indicateOid).off().on('change', function() {
							$('#addFormOptions').empty();
							var option = options[form.indicateOid.value];
							$(form.indicateDataType).val(option.dataType);
							$(form.indicateDataUnit).val(option.dataUnit);
						});

						$(form.cateOid).off().on('change', function() {
							$(form.indicateOid).empty();
							$.each(cascade[form.cateOid.value], function(i, value) {
								options[value.oid] = value;
								$(form.indicateOid).append('<option value="' + value.oid + '">' + value.title + '</option>')
							});
							$(form.indicateOid).change();
						});

						$(form.cateOid).change();

					});
			});

			$('#saveButton').on('click', function() {
				if (!$('#addForm').validator('doSubmitCheck')) return
				var json = {
					options: []
				};
				$.each($('#addForm').serializeArray(), function(i, v) {
					json[v.name] = v.value;
				});


				var x = $('#addFormOptions').children();
				for (var i = 0; i < x.length; i++) {
					var frm = x[i];
					if (!$(frm).validator('doSubmitCheck')) return
				}
				$.each(x, function(i, v) {
					var ov = {};
					$.each($(v).serializeArray(), function(i, v) {
						ov[v.name] = v.value;
					});
					json.options.push(ov);
				});

				http.post(config.api.system.config.ccr.warning.options.save, {
					data: JSON.stringify(json)
				}, function(result) {
					$('#addForm').resetForm();
					$('#addFormOptions').empty();
					$('#addModal').modal('hide');
					$('#dataTable').bootstrapTable('refresh');
				});

			});

			$('#updateButton').on('click', function() {
				if (!$('#updateForm').validator('doSubmitCheck')) return
				var json = {
					options: []
				};
				$.each($('#updateForm').serializeArray(), function(i, v) {
					json[v.name] = v.value;
				});


				var x = $('#updateFormOptions').children();
				for (var i = 0; i < x.length; i++) {
					var frm = x[i];
					if (!$(frm).validator('doSubmitCheck')) return
				}
				$.each(x, function(i, v) {
					var ov = {};
					$.each($(v).serializeArray(), function(i, v) {
						ov[v.name] = v.value;
					});
					json.options.push(ov);
				});
				

				http.post(config.api.system.config.ccr.warning.options.save, {
					data: JSON.stringify(json)
				}, function(result) {
					$('#updateForm').resetForm();
					$('#updateFormOptions').empty();
					$('#updateModal').modal('hide');
					$('#dataTable').bootstrapTable('refresh');
				});
			});

			$('#addFormAddOption').on('click', function() {
				var type = document.addForm.indicateDataType.value;
				var unit = document.addForm.indicateDataUnit.value;
				if (createOptions[type]) {
					var option = createOptions[type](unit);
					if (option) {
						$('#addFormOptions').append(option);
						$('#addForm').validator('destroy');
						util.form.validator.init($('#addForm'));
					}
				}				
			});

			$('#updateFormAddOption').on('click', function() {
				var type = document.updateForm.indicateDataType.value;
				var unit = document.updateForm.indicateDataUnit.value;
				if (createOptions[type]) {
					var option = createOptions[type](unit);
					if (option) {
						$('#updateFormOptions').append(option);
					}
				}
			});

			// 下面的代码要移动到标的模块
			$('#eventCollect').on('click', function() {
				// TODO 这里要调下, 标的模块要设置标的的oid
				var relative = "xxxxxxxxxxxxxxxx";
				// TODO 这里要设置数据采集类型
				var type = "WARNING";
				http.post(config.api.system.config.ccr.options.preCollect, {
						data: {
							type: 'WARNING'
						},
						contentType: 'form'
					},
					function(val) {

						http.post(config.api.system.config.ccr.indicate.collect.preUpdate, {
								data: {
									relative: relative
								},
								contentType: 'form'
							},
							function(predata) {

								var initdata = {

								};

								if (predata && predata.length > 0) {
									$.each(predata, function(i, item) {
										initdata[item.indicateOid] = item;
									});
								};


								$(document.collectForm.relative).val(relative);

								$(document.collectForm.type).val(type);

								$('#collectModalContent').empty();

								if (val && val.length > 0) {

									var content = $('#collectModalContent');

									$.each(val, function(i, collect) {
										$('<h6><b>' + collect.cateTitle + '</b></h6>').appendTo(content);

										$.each(collect.indicates, function(j, indicate) {
											var form = $('<form></form>');
											form.appendTo(content);

											var indicateOid = $('<input type="hidden" name="indicateOid" value="' + indicate.indicateOid + '" />');
											indicateOid.appendTo(form);

											var row = $('<div class="row"></div>');
											row.appendTo(form);
											var col = $('<div class="col-sm-12 col-xs-6"></div>');
											col.appendTo(row);
											var formGroup = $('<div class="form-group"></div>');
											formGroup.appendTo(col);
											var inputGroup = $('<div class="input-group input-group-sm"></div>');
											inputGroup.appendTo(formGroup);

											var inputTitle = $('<div class="input-group-addon">' + indicate.indicateTitle + '</div>');
											inputTitle.appendTo(inputGroup);

											if (indicate.indicateType == 'NUMBER') {
												var inputOcx = $('<select name="options" class="form-control input-sm"></select>');
												inputOcx.appendTo(inputGroup);
												$.each(indicate.options, function(k, option) {
													var check = false;
													if (initdata[indicate.indicateOid] && initdata[indicate.indicateOid].collectOption == option.oid) {
														check = true;
													}
													if (!initdata[indicate.indicateOid] && option.dft == 'YES') {
														check = true;
													}
													var inputOption = $('<option value="' + option.oid + '" ' + (check ? 'selected' : '') + '>' + option.title + '</option>');
													inputOption.appendTo(inputOcx);
												});
											}

											if (indicate.indicateType == 'NUMRANGE') {
												var inputOcx = $('<input name="collectData" type="text" value="' + (initdata[indicate.indicateOid] ? initdata[indicate.indicateOid].collectData : '') + '" class="form-control">');
												inputOcx.appendTo(inputGroup);
											}

											if (indicate.indicateType == 'TEXT') {
												var inputOcx = $('<select name="options" class="form-control input-sm"></select>');
												inputOcx.appendTo(inputGroup);
												$.each(indicate.options, function(k, option) {
													var check = false;
													if (initdata[indicate.indicateOid] && initdata[indicate.indicateOid].collectOption == option.oid) {
														check = true;
													}
													if (!initdata[indicate.indicateOid] && option.dft == 'YES') {
														check = true;
													}
													var inputOption = $('<option value="' + option.oid + '" ' + (check ? 'selected' : '') + '>' + option.title + '</option>');
													inputOption.appendTo(inputOcx);
												});
											}

											if (indicate.indicateUnit && indicate.indicateUnit != '') {
												var inputSuffix = $('<span class="input-group-addon">' + indicate.indicateUnit + '</span>');
												inputSuffix.appendTo(inputGroup);
											}

										});

									});
								}

								$('#collectModal').modal('show');

							});

					});
			});

			$('#collectButton').on('click', function() {

				var data = {
					relative: document.collectForm.relative.value,
					type: document.collectForm.type.value,
					datas: []
				}

				$('#collectModalContent').find('form').each(function(x, form) {
					var config = {};
					$.each($(form).serializeArray(), function(i, v) {
						config[v.name] = v.value.trim();
					});
					data.datas.push(config);
				});

				// TODO 这个 data 对象是采集页面录入的数据, 可以根据具体业务场景使用
				console.log(data);

				http.post(config.api.system.config.ccr.indicate.collect.save, {
					data: JSON.stringify(data)
				}, function(rlt) {
					$('#collectForm').resetForm();
					$('#collectModalContent').empty();

					$('#collectModal').modal('hide');
				});

			});
			
			/**
			 * 初始化风险等级option
			 */
			function initLevelOption(){
				return '<option value="NONE">无</option><option value="LOW">低</option><option value="MID">中</option><option value="HIGH">高</option>';
			}
			
			/**
			 * 初始化单位
			 */
			function initDataUnit(dataUnit){
				return dataUnit ? '<span class="input-group-addon">' + dataUnit.toString().trim() + '</span>' : '';
			}

		}
	}
})