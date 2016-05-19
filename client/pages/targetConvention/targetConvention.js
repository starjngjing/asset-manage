/**
 * 标的过会管理
 */
define([
	'http',
	'config',
	'util',
	'extension'
], function(http, config, util, $$) {
	return {
		name: 'targetConvention',
		init: function() {
			// js逻辑写在这里
			// 分页配置
			var pageOptions = {
					number: 1,
					size: 10
				}
				// 数据表格配置
			var tableConfig = {
					ajax: function(origin) {
						http.post(config.api.meetingList, {
							data: {
								page: pageOptions.number,
								rows: pageOptions.size
							},
							contentType: 'form'
						}, function(rlt) {
							origin.success(rlt)
						})
					},
					pageNumber: pageOptions.number,
					pageSize: pageOptions.size,
					pagination: true,
					sidePagination: 'server',
					pageList: [10, 20, 30, 50, 100],
					queryParams: getQueryParams,
					columns: [{
						field: 'sn'
					}, {
						field: 'title'
					}, {
						field: 'conferenceTime'
					}, {
						field: 'state',
						formatter: function(val) {
							return util.enum.transform('meetingStates', val);
						}
					}, {
						align: 'center',
						formatter: function(val, row) {
							var buttons = [{
								text: '会议报告',
								type: 'button',
								class: 'item-detail',
								isRender: true
							}];
							return util.table.formatter.generateButton(buttons);
						},
						events: {

						}
					}]
				}
				// 初始化表格
			$('#targetConventionTable').bootstrapTable(tableConfig)
				// 新建会议按钮点击事件
			$('#targetConventionAdd').on('click', function() {
				$('#addTargetConventionModal').modal('show')
			})

			$("#addMeeting").on('click', function() {
				$('#addTargetConventionForm').ajaxSubmit({
					type: "post", //提交方式  
					//dataType:"json", //数据类型'xml', 'script', or 'json'  
					url: config.api.meetingAdd,
					success: function(result) {

					}
				})
			})

			// 参会人select2初始化
			var addForm = document.addTargetConventionForm

			$(addForm.participant).select2({
				multiple: "multiple",
				ajax: {
					//type: 'post',
					url: config.api.meetingUser,
					dataType: 'json',
					delay: 500, // 输入->查询 停顿毫秒数
					data: function(params) {
						console.log(params)
						return {
							name: params.term // 输入字符串
						}
					},
					processResults: function(data) {
						return {
							results: data.rows.map(function(item) {
								var showObj = {}
								showObj.id = item.oid
								showObj.text = item.name
								return showObj;
							})
						}
					},
					cache: true
				},
				minimumInputLength: 1
			})

			$(addForm.target).select2({
				multiple: "multiple",
				ajax: {
					//type: 'post',
					url: config.api.meetingTarget,
					dataType: 'json',
					delay: 500, // 输入->查询 停顿毫秒数
					data: function(params) {
						console.log(params)
						return {
							name: params.term // 输入字符串
						}
					},
					processResults: function(data) {
						return {
							results: data.rows.map(function(item) {
								var showObj = {}
								showObj.id = item.oid
								showObj.text = item.name
								return showObj;
							})
						}
					},
					cache: true
				},
				minimumInputLength: 1
			})
			
			function getQueryParams(val) {
				var form = document.targetSearchForm
				pageOptions.size = val.limit
				pageOptions.number = parseInt(val.offset / val.limit) + 1
				return val
			}
		}
	}
})