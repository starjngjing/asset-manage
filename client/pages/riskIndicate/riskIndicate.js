/**
 * 风险关键指标维护
 */
define([
	'http',
	'config',
	'util',
	'extension'
], function(http, config, util, $$) {
	return {
		name: 'riskIndicate',
		init: function() {

			var queryParams = {
				type: '',
				keyword: ''
			};

			$('#dataTable').bootstrapTable({
				ajax: function(origin) {
					http.post(config.api.system.config.ccr.indicate.search, {
						data: queryParams,
						contentType: 'form'
					}, function(rlt) {
						origin.success(rlt)
					})
				},
				queryParams: function(val) {
					var form = document.searchForm;
					queryParams.type = form.type.value.trim();
					queryParams.keyword = form.keyword.value.trim();
				},
				columns: [{
					field: 'indicateOid',
					formatter: function(val, row, index) {
						return index + 1
					}
				}, {
					field: 'cateType',
					formatter: function(val, row, index) {
						if (val == 'WARNING')
							return '风险预警指标';
						if (val == 'SCORE')
							return '风险评分指标';
						return val;
					}
				}, {
					field: 'cateTitle'
				}, {
					field: 'indicateTitle'
				}, {
					field: 'indicateDataType',
					formatter: function(val, row, index) {
						if (val == 'NUMBER')
							return '数值';
						if (val == 'NUMRANGE')
							return '数值区间';
						if (val == 'TEXT')
							return '文本'
						return val;
					}
				}, {
					field: 'indicateDataUnit'
				}, {
					field: 'indicateState',
					formatter: function(val, row, index) {
						if (val == 'ENABLE')
							return '启用';
						if (val == 'DISABLE')
							return '禁用';
						if (val == 'DELETE')
							return '删除';
						return val;
					}
				}]

			});

			$$.searchInit($('#searchForm'), $('#dataTable'));

			$('#eventAdd').on('click', function() {

				http.post(config.api.system.config.ccr.cate.options, {
					data: {},
					contentType: 'form'
				}, function(val) {
					$.each(val, function(key, value) {
						value.push({
							oid: '',
							type: key,
							title: '其他'
						});
					});
					$('#addForm').resetForm();
					$('#addModal').modal('show');
					var form = document.addForm;
					var cateType = form.cateType;
					$(cateType).off().on('change', function() {
						if (!val[cateType.value]) {
							val[cateType.value] = [{
								oid: '',
								title: '其他',
								type: cateType.value
							}];
						}

						$(form.cateOid).empty();
						$.each(val[cateType.value], function(i, v) {
							$(form.cateOid).append('<option value="' + v.oid + '">' + v.title + '</option>')
						});

						$(form.cateOid).change();
					});

					$(cateType).change();

					$(form.cateOid).off().on('change', function() {

						if (form.cateOid.value == '' && $('#addFormCateTitle').css('display') == 'none') {
							$('#addFormCateTitle').animate({
								height: 'toggle',
								opacity: 'toggle'
							}, "slow");
						}

						if (form.cateOid.value != '' && $('#addFormCateTitle').css('display') == 'block') {
							$('#addFormCateTitle').animate({
								height: 'toggle',
								opacity: 'toggle'
							}, "slow");
						}

					});

					$(form.cateOid).change();

				});
			});
			
			$('#saveButton').on('click', function(){
				
			});

		}
	}
})