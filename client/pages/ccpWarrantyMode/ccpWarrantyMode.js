/**
 * 担保方式权限配置
 */
define([
	'http',
	'config',
	'util',
	'extension'
], function(http, config, util, $$) {
	return {
		name: 'ccpWarrantyMode',
		init: function() {

			var tableConfig = {
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
							$("#confirmTitle").html("确定删除担保对象权数配置？");
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
			};

			$('#ccpWarrantyModeTable').bootstrapTable(tableConfig);

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

		}
	}
})