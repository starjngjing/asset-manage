/**
 * 担保对象权限配置
 */
define([
	'http',
	'config',
	'util'
], function(http, config, util) {
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
						return row.highScore + ' - ' + row.lowScore;
					}
				}, {
					
				}]
			}

			$('#ccpWarrantorTable').bootstrapTable(tableConfig)

		}
	}
})