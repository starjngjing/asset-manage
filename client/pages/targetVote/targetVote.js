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
		}
	}
})