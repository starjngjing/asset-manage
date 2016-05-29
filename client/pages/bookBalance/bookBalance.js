/**
 * 查询凭证
 */
define([
	'http',
	'config',
	'util',
	'extension'
], function(http, config, util, $$) {
	return {
		name: 'bookBalance',
		init: function() {

			console.log(config.api.acct.book.balance);

			$('#dataTable').bootstrapTable({
				classes: '',
				//				rowStyle: function() {
				//					return {
				//						css: {
				//							'line-height': '60px'
				//						}
				//					}
				//				},
				ajax: function(origin) {
					http.post(config.api.acct.book.balance, {
						data: {},
						contentType: 'form'
					}, function(rlt) {
						origin.success(rlt)
					})
				},
				columns: [{
					field: 'lcode',
					halign: 'center',
					align: 'left',
					formatter: function(val, row, index) {
						return (val ? val + '&nbsp;' : '') + row.lname;
					},
					cellStyle: function(val, row, index) {
						if (index == 0) {
							return {
								css: {
									'border-top': '2px solid #5C7C8E'
								}
							};
						} else {
							return {};
						}
					}
				}, {
					field: 'lsn',
					halign: 'center',
					align: 'center',
					formatter: function(val, row, index) {
						return val ? val : '';
					},
					cellStyle: function(val, row, index) {
						if (index == 0) {
							return {
								css: {
									'border-top': '2px solid #5C7C8E'
								}
							};
						} else {
							return {};
						}
					}
				}, {
					field: 'lbalance',
					halign: 'center',
					align: 'right',
					formatter: function(val, row, index) {
						return (val && val != 0) ? val.toFixed(2) : '';
					},
					cellStyle: function(val, row, index) {
						if (index == 0) {
							return {
								css: {
									'border-top': '2px solid #5C7C8E',
									'border-right': '2px solid #5C7C8E'
								}
							}
						} else {
							return {
								css: {
									'border-right': '2px solid #5C7C8E'
								}
							}
						}
					}
				}, {
					field: 'rcode',
					halign: 'center',
					align: 'left',
					formatter: function(val, row, index) {
						return (val ? val + '&nbsp;' : '') + row.rname;
					},
					cellStyle: function(val, row, index) {
						if (index == 0) {
							return {
								css: {
									'border-top': '2px solid #5C7C8E',
									'border-left': '2px solid #5C7C8E'
								}
							}
						} else {
							return {
								css: {
									'border-left': '2px solid #5C7C8E'
								}
							}
						}
					}
				}, {
					field: 'rsn',
					halign: 'center',
					align: 'center',
					formatter: function(val, row, index) {
						return val ? val : '';
					},
					cellStyle: function(val, row, index) {
						if (index == 0) {
							return {
								css: {
									'border-top': '2px solid #5C7C8E'
								}
							};
						} else {
							return {};
						}
					}
				}, {
					field: 'rbalance',
					halign: 'center',
					align: 'right',
					formatter: function(val, row, index) {
						return (val && val != 0) ? val.toFixed(2) : '';
					},
					cellStyle: function(val, row, index) {
						if (index == 0) {
							return {
								css: {
									'border-top': '2px solid #5C7C8E'
								}
							};
						} else {
							return {};
						}
					}
				}]
			});

		}
	}
})