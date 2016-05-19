/**
 * 担保期限权限配置
 */
define([
	'http',
	'config',
	'util',
	'extension'
], function(http, config, util, $$) {
	return {
		name: 'ccpWarrantyExpire',
		init: function() {
			var tableConfig = {
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
							$("#confirmTitle").html("确定删除担保期限权数配置？");
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
			};

			$('#ccpWarrantyExpireTable').bootstrapTable(tableConfig);

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
		}
	}
})