/**
 * 标的过会表决
 */
define([
	'http',
	'config',
	'util',
	'extension'
], function(http, config, util, $$) {
	return {
		name: 'targetVote',
		init: function() {
			// js逻辑写在这里
			// 数据表格配置
			var tableConfig = {
				ajax: function(origin) {
					http.post(config.api.voteTargetList, {
						contentType: 'form'
					}, function(rlt) {
						origin.success(rlt)
					})
				},
				pageNumber: 1,
				pageSize: 1,
				pagination: false,
				sidePagination: 'server',
				columns: [{
					field: 'sn'
				}, {
					field: 'name'
				}, {
					field: 'meetingTitle'
				}, {
					field: 'meetingTime'
				}, {
					field: 'meetingState',
					align: 'center',
					formatter: function(val) {
						return util.enum.transform('meetingStates', val);
					}
				}, {
					field: 'voteStatus',
					align: 'center',
					formatter: function(val) {
						return util.enum.transform('voteStates', val);
					}
				}, {
					align: 'center',
					formatter: function(val, row) {
						var buttons = [{
							text: '查看详情',
							type: 'button',
							class: 'item-detail',
							isRender: true
						}, {
							text: '表决',
							type: 'button',
							class: 'item-vote',
							isRender: true
						}];
						return util.table.formatter.generateButton(buttons);
					},
					events: {
						'click .item-detail': function(e, value, row) {
								http.post(config.api.targetDetQuery, {
									data: {
										oid: row.oid
									},
									contentType: 'form'
								}, function(result) {
									var data = result.investment;
									$$.detailAutoFix($('#detTargetForm'), data); // 自动填充详情
									$$.formAutoFix($('#detTargetForm'), data); // 自动填充表单
									$('#targetDetailModal').modal('show');
								})
							},
						'click .item-vote': function(e, value, row) {
							$('#meetingOid').val(row.meetingOid);
							$('#targetOid').val(row.oid);
							//删除会议报告表
							$('#voteProgressTable').bootstrapTable('destroy')
								//删除会议纪要表
							$('#voteSummaryTable').bootstrapTable('destroy')
							var voteProgressTableConfig = {
								ajax: function(origin) {
									http.post(config.api.meetingTargetVoteDet, {
										data: {
											meetingOid: row.meetingOid,
											targetOid: row.oid
										},
										contentType: 'form'
									}, function(rlt) {
										origin.success(rlt)
									})
								},
								columns: [{
									field: 'role',
									align: 'center'
								}, {
									field: 'voteStatus',
									align: 'center',
									formatter: function(val) {
										return util.enum.transform('voteStates', val);
									}
								}, {
									field: 'name',
									align: 'center'
								}, {
									field: 'time',
									align: 'center'
								}, {
									align: 'center',
									formatter: function(val, row) {
										var buttons = [{
											text: '下载',
											type: 'button',
											class: 'item-download',
											isRender: row.file != null
										}];
										return util.table.formatter.generateButton(buttons);
									},
									events: {
										'click .item-download': function(e, value, row) {
											location.href = 'http://api.guohuaigroup.com' + row.file
										}
									}
								}]
							}
							$('#voteProgressTable').bootstrapTable(voteProgressTableConfig)
								// 过会纪要表格配置
							var voteSummaryTableConfig = {
								ajax: function(origin) {
									http.post(config.api.meetingSummaryDet, {
										data: {
											oid: row.meetingOid,
										},
										contentType: 'form'
									}, function(rlt) {
										origin.success(rlt)
									})
								},
								pageNumber: 1,
								pageSize: 100000,
								pagination: false,
								sidePagination: 'server',
								columns: [{
									field: 'operator'
								}, {
									field: 'updateTime'
								}, {
									align: 'center',
									formatter: function(val, row) {
										var buttons = [{
											text: '下载',
											type: 'button',
											class: 'item-download',
											isRender: true
										}];
										return util.table.formatter.generateButton(buttons);
									},
									events: {
										'click .item-download': function(e, value, row) {
											var key = {};
											key.fkey = row.fkey;
											var json = {
												fkeys: []
											};
											json.fkeys.push(key);
											http.post(config.api.files.pkg, {
												data: JSON.stringify(json)
											}, function(result) {
												location.href = config.api.files.download + result.key
											})
										}
									}
								}]
							}
							$('#voteSummaryTable').bootstrapTable(voteSummaryTableConfig)
							$('#voteModal').modal('show')
						}
					}
				}]
			};
			// 初始化表格
			$('#targetVoteTable').bootstrapTable(tableConfig)

			// 过会投票 -> 赞成按钮点击事件
			$('#doVoteAccept').on('click', function() {
					vote('yes');
				})
				// 过会投票 -> 不赞成按钮点击事件
			$('#doVoteReject').on('click', function() {
					vote('no');
				})
				// 初始化过会进程tab上传附件插件
			$$.uploader({
				container: $('#voteUploader'),
				success: function(file) {
					$('#voteFile').show().find('a').attr('href', 'http://api.guohuaigroup.com' + file.url)
					$('#voteFile').find('span').html(file.name)
					document.voteForm.file.value = file.url
				}
			})

		}
	}

	function vote(state) {
		$('#voteState').val(state);
		$(document.voteForm).ajaxSubmit({
			url: config.api.voteTarget,
			success: function() {
				$('#voteForm').clearForm();
				$('#targetVoteTable').bootstrapTable('refresh')
				$('#voteModal').modal('hide')
			}
		})
	}
})