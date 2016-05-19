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
		name: 'ccpWarrantor',
		init: function() {

			var tableConfig = {
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
							$("#confirmTitle").html("确定删除担保对象权数配置？");
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
			};

			$('#ccpWarrantorTable').bootstrapTable(tableConfig);

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

		}
	}
})