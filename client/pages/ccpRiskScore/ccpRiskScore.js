/**
 * 信用等级评分模型
 */
define([
	'http',
	'config',
	'util',
	'extension'
], function(http, config, util, $$) {
	return {
		name: 'ccpRiskScore',
		init: function() {
			// js逻辑写在这里

			var createOptions = {
				"NUMBER": function() {
					var form = $('<form type="NUMBER"></form');
					var row = $('<div class="row"></div>');
					row.appendTo(form);
					var x0 = $('<div class="col-sm-8 col-xs-12"><div class="form-group"><input name="param0" type="text" class="form-control input-sm" placeholder="指标项描述"></div></div>');
					x0.appendTo(row);
					var x1 = $('<div class="col-sm-3 col-xs-6"><div class="form-group"><div class="input-group input-group-sm"><div class="input-group-addon">得分</div><input name="score" type="text" class="form-control" placeholder="得分"></div></div></div>');
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

					return form;
				},
				"NUMRANGE": function() {
					var form = $('<form type="NUMRANGE"></form');
					var row = $('<div class="row"></div>');
					row.appendTo(form);
					var x0 = $('<div class="col-sm-4 col-xs-6"><div class="form-group"><div class="row"><div class="col-xs-5"><select name="param0" class="form-control input-sm"><option value="[">[</option><option value="(">(</option></select></div><div class="col-xs-7"><input name="param1" type="text" class="form-control input-sm"></div></div></div></div>');
					x0.appendTo(row);
					var x1 = $('<div class="col-sm-4 col-xs-6"><div class="form-group"><div class="row"><div class="col-xs-7"><div class="input-group range"><input name="param2" type="text" class="form-control input-sm"></div></div><div class="col-xs-5"><select name="param3" class="form-control input-sm"><option value="]">]</option><option value=")">)</option></select></div></div></div></div>');
					x1.appendTo(row);
					var x2 = $('<div class="col-sm-3 col-xs-6"><div class="form-group"><div class="input-group input-group-sm"><div class="input-group-addon">得分</div><input name="score" type="text" class="form-control" placeholder="得分"></div></div></div>');
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

					return form;
				},
				"TEXT": function() {
					var form = $('<form type="TEXT"></form');
					var row = $('<div class="row"></div>');
					row.appendTo(form);
					var x0 = $('<div class="col-sm-8 col-xs-12"><div class="form-group"><input name="param0" type="text" class="form-control input-sm" placeholder="指标项描述"></div></div>');
					x0.appendTo(row);
					var x1 = $('<div class="col-sm-3 col-xs-6"><div class="form-group"><div class="input-group input-group-sm"><div class="input-group-addon">得分</div><input name="score" type="text" class="form-control" placeholder="得分"></div></div></div>');
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

					return form;
				}
			};

			$('#eventAdd').on('click', function() {

				http.post(config.api.system.config.ccr.indicate.options, {
					data: {
						type: 'SCORE'
					},
					contentType: 'form'
				}, function(val) {

					var cascade = {};
					var options = {};
					var form = document.addForm;

					$('#addForm').resetForm();
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

				var json = {
					options: []
				};
				$.each($('#addForm').serializeArray(), function(i, v) {
					json[v.name] = v.value;
				});


				var x = $('#addFormOptions').children();
				$.each(x, function(i, v) {
					var ov = {};
					$.each($(v).serializeArray(), function(i, v) {
						ov[v.name] = v.value;
					});
					json.options.push(ov);
				});


				http.post(config.api.system.config.ccr.options.save, {
					data: JSON.stringify(json)
				}, function(result) {
					$('#addForm').resetForm();
					$('#addFormOptions').empty();
					$('#addModal').modal('hide');
					$('#dataTable').bootstrapTable('refresh');
				});

			});

			$('#addFormAddOption').on('click', function() {
				var type = document.addForm.indicateDataType.value;
				if (createOptions[type]) {
					var option = createOptions[type]();
					if (option) {
						$('#addFormOptions').append(option);
					}
				}
			});
		}
	}
})