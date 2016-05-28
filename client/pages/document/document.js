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
		name: 'document',
		init: function() {

			var queryParams = {
				startDate: '',
				endDate: '',
				page: 1,
				size: 3
			};

			$('#dataTable').bootstrapTable({
				classes: '',
				ajax: function(origin) {
					http.post(config.api.acct.book.document.entry.search, {
						data: queryParams,
						contentType: 'form'
					}, function(rlt) {
						origin.success(rlt)
					})
				},
				queryParams: function(val) {
					var form = document.searchForm;
					queryParams.startDate = form.startDate.value.trim();
					queryParams.endDate = form.endDate.value.trim();
					queryParams.size = val.limit;
					queryParams.page = parseInt(val.offset / val.limit) + 1;
				},
				pageNumber: queryParams.page,
				pageSize: queryParams.size,
				pagination: true,
				sidePagination: 'server',
				pageList: [3, 20, 30, 50, 100],
				columns: [{
					field: 'documentAcctDate',
					formatter: function(val, row, index) {
						return row.master ? val : '';
					},
					cellStyle: function(val, row, index) {
						return {
							css: {
								'background-color': row.index % 2 == 0 ? '#F8FF94' : '#FFFFFF'
							}
						}
					}
				}, {
					field: 'documentDocword',
					formatter: function(val, row, index) {
						return row.master ? val + '&nbsp;-&nbsp;' + row.documentAcctSn : '';
					},
					cellStyle: function(val, row, index) {
						return {
							css: {
								'background-color': row.index % 2 == 0 ? '#F8FF94' : '#FFFFFF'
							}
						}
					}
				}, {
					field: 'documentEntryDigest',
					cellStyle: function(val, row, index) {
						return {
							css: {
								'background-color': row.index % 2 == 0 ? '#F8FF94' : '#FFFFFF'
							}
						}
					}
				}, {
					field: 'documentEntryAccountCode',
					formatter: function(val, row, index) {
						return val + '&nbsp;&nbsp;' + row.documentEntryAccountName;
					},
					cellStyle: function(val, row, index) {
						return {
							css: {
								'background-color': row.index % 2 == 0 ? '#F8FF94' : '#FFFFFF'
							}
						}
					}
				}, {
					field: 'documentEntryDrAmount',
					halign: 'center',
					align: 'right',
					formatter: function(val, row, index) {
						return val == 0 ? '' : val;
					},
					cellStyle: function(val, row, index) {
						return {
							css: {
								'background-color': row.index % 2 == 0 ? '#F8FF94' : '#FFFFFF'
							}
						}
					}
				}, {
					field: 'documentEntryCrAmount',
					halign: 'center',
					align: 'right',
					formatter: function(val, row, index) {
						return val == 0 ? '' : val;
					},
					cellStyle: function(val, row, index) {
						return {
							css: {
								'background-color': row.index % 2 == 0 ? '#F8FF94' : '#FFFFFF'
							}
						}
					}
				}, {

					field: 'documentCreateTime',
					align: 'center',
					formatter: function(val, row, index) {
						return row.master ? val : '';
					},
					cellStyle: function(val, row, index) {
						return {
							css: {
								'background-color': row.index % 2 == 0 ? '#F8FF94' : '#FFFFFF'
							}
						}
					}
				}, {

					align: 'center',
					field: 'documentOid',
					formatter: function(val, row, index) {

						var updateButton = {
							text: '明细',
							type: 'button',
							class: 'item-detail',
							isRender: true
						};

						var buttons = [updateButton];
						return row.master ? util.table.formatter.generateButton(buttons) : '';
					},
					events: {
						'click .item-detail': function(e, value, row) {
							console.log(value);
						}
					},
					cellStyle: function(val, row, index) {
						return {
							css: {
								'background-color': row.index % 2 == 0 ? '#F8FF94' : '#FFFFFF'
							}
						}
					}

				}],
				onLoadSuccess: function(data) {
					$.each(data.rows, function(i, item) {
						if (item.master) {
							$('#dataTable').bootstrapTable('mergeCells', {
								index: i,
								field: 'documentAcctDate',
								colspan: 1,
								rowspan: item.rowspan
							});
							$('#dataTable').bootstrapTable('mergeCells', {
								index: i,
								field: 'documentDocword',
								colspan: 1,
								rowspan: item.rowspan
							});
							$('#dataTable').bootstrapTable('mergeCells', {
								index: i,
								field: 'documentCreateTime',
								colspan: 1,
								rowspan: item.rowspan
							});
							$('#dataTable').bootstrapTable('mergeCells', {
								index: i,
								field: 'documentOid',
								colspan: 1,
								rowspan: item.rowspan
							});
						}
					});
				}
			});

			$$.searchInit($('#searchForm'), $('#dataTable'));

			$('#updateDocTemplateModal').modal('show');

		}
	}
})