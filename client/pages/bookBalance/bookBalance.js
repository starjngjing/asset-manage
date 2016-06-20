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

			http.post(config.api.duration.assetPool.getNameList, function(json) {
				var select = $(document.searchForm.relative);
				json.rows.forEach(function(item) {
					$('<option value="' + item.oid + '">' + item.name + '</option>').appendTo(select);
				});
			});

			var queryParams = {
				relative: ''
			};


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
						data: queryParams,
						contentType: 'form'
					}, function(rlt) {
						origin.success(rlt)
					})
				},
				queryParams: function(val) {
					var form = document.searchForm;
					queryParams.relative = form.relative.value.trim();
				},
				columns: [{
					field: 'lcode',
					halign: 'center',
					align: 'left',
					width: '30%',
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
					width: '5%',
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
					width: '15%',
					formatter: function(val, row, index) {
						if (row.lcode == '' && row.lname != '') {
							return val.toFixed(2);
						}
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
					width: '30%',
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
					width: '5%',
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
					width: '15%',
					formatter: function(val, row, index) {
						if (row.rcode == '' && row.rname != '') {
							return val.toFixed(2);
						}
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

			$$.searchInit($('#searchForm'), $('#dataTable'));

		}
	}
})