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

			// 会议报告表格配置
			var targetConventionReportTableConfig = {
				data: [{
					name: '十一届三中全会'
				}],
				detailView: true,
				onExpandRow: function (index, row, $detail) {
					var table = $('<table><thead><tr>' +
												'<th>角色名称</th>' +
												'<th>投票意见</th>' +
												'<th>投票人</th>' +
												'<th>时间</th>' +
											'</tr></thead></table>')
					var tableConfig = {
						data: [{
							role: '投资人',
							comment: '同意',
							name: '张三',
							date: '2015-01-01'
						}],
						columns: [
							{
								field: 'role',
								align: 'center'
							},
							{
								field: 'comment',
								align: 'center'
							},
							{
								field: 'name',
								align: 'center'
							},
							{
								field: 'date',
								align: 'center'
							}
						]
					}
					$detail.append(table)
					$(table).bootstrapTable(tableConfig)
				},
				columns: [
					{
						field: 'name'
					}
				]
			}

			$('#targetConventionReportTable').bootstrapTable(targetConventionReportTableConfig)

			// 过会纪要表格配置
			var targetConventionSummaryTableConfig = {

			}

			// 上传纪要弹窗按钮点击事件
			$('#targetConventionSummaryUpload').on('click', function () {
				$('#uploadTargetConventionSummaryModal').modal('show')
			})
			// 上传纪要附件表格数据源
			var uploadTargetConventionSummaryFiles = []
			// 初始化上传附件插件，在success里将上传成功附件插入到表格中
			$$.uploader({
				container: $('#uploader'),
				success: function (file) {
					uploadTargetConventionSummaryFiles.push(file)
					$('#uploadTargetConventionSummaryTable').bootstrapTable('load', uploadTargetConventionSummaryFiles)
				}
			})
			// 上传纪要附件表格配置
			var uploadTargetConventionSummaryTableConfig = {
				columns: [
					{
						field: 'name',
					},
					{
						width: 100,
						align: 'center',
						formatter: function () {
							var buttons = [
								{
									text: '下载',
									type: 'button',
									class: 'item-download'
								},
								{
									text: '删除',
									type: 'button',
									class: 'item-delete'
								}
							]
							return util.table.formatter.generateButton(buttons)
						},
						events: {
							'click .item-download': function (e, value, row) {
								location.href = config.host + row.url
							},
							'click .item-delete': function (e, value, row) {
								var index = uploadTargetConventionSummaryFiles.indexOf(row)
								uploadTargetConventionSummaryFiles.splice(index, 1)
								$('#uploadTargetConventionSummaryTable').bootstrapTable('load', uploadTargetConventionSummaryFiles)
							}
						}
					}
				]
			}
			// 上传纪要附件表格初始化
			$('#uploadTargetConventionSummaryTable').bootstrapTable(uploadTargetConventionSummaryTableConfig)
			// 上传纪要“上传”按钮点击事件
			$('#doUploadTargetConventionSummary').on('click', function () {
				var form = document.uploadTargetConventionSummaryForm
				form.files.value = JSON.stringify(uploadTargetConventionSummaryFiles)
				form.ajaxSubmit({

				})
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