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
					align: 'center',
					formatter: function(val, row) {
						var buttons = [{
							text: '查看详情',
							type: 'button',
							class: 'item-detail',
							isRender: true
						}, {
							text: '底层项目',
							type: 'button',
							class: 'item-project',
							isRender: true
						}, {
							text: '投票',
							type: 'button',
							class: 'item-check',
							isRender: true
						}];
						return util.table.formatter.generateButton(buttons);
					},
					events: {
						
					}
				}]
			};
			// 初始化表格
			$('#targetVoteTable').bootstrapTable(tableConfig)

			// 过会投票 -> 赞成按钮点击事件
			$('#doVoteAccept').on('click', function () {
				$(document.voteForm).ajaxSubmit({
					url: '',
					success: function () {
						$('#voteModal').modal('hide')
					}
				})
			})
			// 过会投票 -> 不赞成按钮点击事件
			$('#doVoteReject').on('click', function () {

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
})