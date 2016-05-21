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


			$('#eventAdd').on('click', function() {

				http.post(config.api.system.config.ccr.cate.options, {
					data: {
						type: 'SCORE'
					},
					contentType: 'form'
				}, function(val) {

					console.log(val);

				});
			})
		}
	}
})