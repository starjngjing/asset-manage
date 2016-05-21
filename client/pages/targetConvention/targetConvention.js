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
				// 会议列表数据表格配置
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
						field: 'updateTime'
					}, {
						align: 'center',
						formatter: function(val, row) {
							var buttons = [{
								text: '会议报告',
								type: 'button',
								class: 'item-detail',
								isRender: true
							}, {
								text: '纪要管理',
								type: 'button',
								class: 'item-summary',
								isRender: true
							}, {
								text: '启动过会',
								type: 'button',
								class: 'item-open',
								isRender: row.state == "notopen" || row.state == "stop"
							}, {
								text: '暂停过会',
								type: 'button',
								class: 'item-stop',
								isRender: row.state == "opening"
							}, {
								text: '完成过会',
								type: 'button',
								class: 'item-finish',
								isRender: row.state == "waitenter"
							}];
							return util.table.formatter.generateButton(buttons);
						},
						events: {
							'click .item-open': function(e, value, row) {
								$("#confirmTitle").html("确定启动过会？")
								$$.confirm({
									container: $('#doConfirm'),
									trigger: this,
									accept: function() {
										http.post(config.api.meetingOpen, {
											data: {
												oid: row.oid
											},
											contentType: 'form',
										}, function(result) {
											$('#targetConventionTable').bootstrapTable('refresh')
										})
									}
								})
							},
							'click .item-stop': function(e, value, row) {
								$("#confirmTitle").html("确定暂停过会？")
								$$.confirm({
									container: $('#doConfirm'),
									trigger: this,
									accept: function() {
										http.post(config.api.meetingStop, {
											data: {
												oid: row.oid
											},
											contentType: 'form',
										}, function(result) {
											$('#targetConventionTable').bootstrapTable('refresh')
										})
									}
								})
							},
							'click .item-summary': function(e, value, row) {
								$('#targetConventionSummaryTable').bootstrapTable('destroy')
									// 过会纪要表格配置
								var targetConventionSummaryTableConfig = {
										ajax: function(origin) {
											http.post(config.api.meetingSummaryDet, {
												data: {
													oid: row.oid,
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
												}, {
													text: '删除',
													type: 'button',
													class: 'item-delete',
													isRender: true
												}];
												return util.table.formatter.generateButton(buttons);
											},
											events: {
												'click .item-download': function(e, value, row) {
													
												},
												'click .item-delete': function(e, value, row) {

												}
											}
										}]
									}
									// 初始化过会纪要表格
								$('#targetConventionSummaryTable').bootstrapTable(targetConventionSummaryTableConfig)
								http.post(config.api.meetingDetail, {
									data: {
										oid: row.oid
									},
									contentType: 'form'
								}, function(result) {
									var data = result.data;
									$$.detailAutoFix($('#uploadTargetConventionSummaryForm'), data); // 自动填充详情
									$('#targetConventionSummaryModal').modal('show');
								})
							},
							'click .item-detail': function(e, value, row) {
								http.post(config.api.meetingDetail, {
									data: {
										oid: row.oid
									},
									contentType: 'form'
								}, function(result) {
									var data = result.data;
									$$.detailAutoFix($('#targetConventionReportModalForm'), data); // 自动填充详情
									// 会议报告表格配置
									var targetConventionReportTableConfig = {
											ajax: function(origin) {
												http.post(config.api.meetingTargetList, {
													data: {
														oid: data.oid
													},
													contentType: 'form'
												}, function(rlt) {
													origin.success(rlt)
												})
											},
											detailView: true,
											onExpandRow: function(index, row, $detail) {
												var table = $('<table><thead><tr>' +
													'<th>角色名称</th>' +
													'<th>投票意见</th>' +
													'<th>投票人</th>' +
													'<th>时间</th>' +
													'</tr></thead></table>')
												var tableConfig = {
													ajax: function(origin) {
														http.post(config.api.meetingTargetVoteDet, {
															data: {
																meetingOid: data.oid,
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
														field: 'state',
														align: 'center',
														formatter: function(val) {
															return util.enum.transform('voteStates', val);
														}
													}, {
														field: 'name',
														align: 'center'
													}, {
														field: 'date',
														align: 'center'
													}]
												}
												$detail.append(table)
												$(table).bootstrapTable(tableConfig)
											},
											columns: [{
												field: 'name'
											}]
										}
										//初始化过会报告
									$('#targetConventionReportTable').bootstrapTable(targetConventionReportTableConfig)
									$('#targetConventionReportModal').modal('show');
								})
							}
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

			// 上传纪要弹窗按钮点击事件
			$('#targetConventionSummaryUpload').on('click', function() {
					$('#uploadTargetConventionSummaryModal').modal('show')
				})
				// 上传纪要附件表格数据源
			var uploadTargetConventionSummaryFiles = []
				// 初始化上传附件插件，在success里将上传成功附件插入到表格中
			$$.uploader({
					container: $('#uploader'),
					success: function(file) {
						uploadTargetConventionSummaryFiles.push(file)
						$('#uploadTargetConventionSummaryTable').bootstrapTable('load', uploadTargetConventionSummaryFiles)
					}
				})
				// 上传纪要附件表格配置
			var uploadTargetConventionSummaryTableConfig = {
					columns: [{
						field: 'name',
					}, {
						width: 100,
						align: 'center',
						formatter: function() {
							var buttons = [{
								text: '下载',
								type: 'button',
								class: 'item-download'
							}, {
								text: '删除',
								type: 'button',
								class: 'item-delete'
							}]
							return util.table.formatter.generateButton(buttons)
						},
						events: {
							'click .item-download': function(e, value, row) {
								location.href = config.api.yup + row.url
							},
							'click .item-delete': function(e, value, row) {
								var index = uploadTargetConventionSummaryFiles.indexOf(row)
								uploadTargetConventionSummaryFiles.splice(index, 1)
								$('#uploadTargetConventionSummaryTable').bootstrapTable('load', uploadTargetConventionSummaryFiles)
							}
						}
					}]
				}
				// 上传纪要附件表格初始化
			$('#uploadTargetConventionSummaryTable').bootstrapTable(uploadTargetConventionSummaryTableConfig)
				// 上传纪要“上传”按钮点击事件
			$('#doUploadTargetConventionSummary').on('click', function() {
				var form = document.uploadTargetConventionSummaryForm
				form.files.value = JSON.stringify(uploadTargetConventionSummaryFiles)
				$('#uploadTargetConventionSummaryForm').ajaxSubmit({
					url: config.api.meetingSummaryUp,
					success: function(result) {
						$('#targetConventionSummaryTable').bootstrapTable('refresh')
						$('#uploadTargetConventionSummaryModal').modal('hide')
					}

				})
			})

			// 临时存储当前操作标的对象
			var currentOpTarget = null
				// 会议确认表格配置
			var finishTargetConventionTableConfig = {
					// 在初始化数据的时候，将检查项数组和驳回理由字符串添加到每个对象里
					data: [{
						name: '十一届三中全会',
						status: 'yes',
						checkConditions: [], // 检查项
						rejectComment: '' // 驳回理由
					}],
					detailView: true,
					onExpandRow: function(index, row, $detail) {

					},
					columns: [{
						field: 'name'
					}, {
						field: 'status',
						formatter: function(val) {
							return val === 'yes' ? '<span class="text-green">通过</span>' : '<span class="text-red">驳回</span>'
						}
					}, {
						width: 120,
						align: 'center',
						formatter: function() {
							var buttons = [{
								text: '通过',
								type: 'button',
								class: 'item-pass'
							}, {
								text: '驳回',
								type: 'button',
								class: 'item-reject'
							}]
							return util.table.formatter.generateButton(buttons)
						},
						events: {
							'click .item-pass': function(e, value, row) {
								currentOpTarget = row
									// 复制此标的下检查项的值
								var injectData = row.checkConditions.map(function(text) {
										return {
											text: text
										}
									})
									// 加一条空值，用于新增
								injectData.push({
									text: ''
								})
								$('#checkConditionsTable').bootstrapTable('load', injectData)
								$('#checkConditionsModal').modal('show')
							},
							'click .item-reject': function(e, value, row) {
								currentOpTarget = row
								document.rejectForm.rejectComment.value = currentOpTarget.rejectComment
								$('#rejectCommentModal').modal('show')
							}
						}
					}]
				}
				// 会议确认表格初始化
			$('#finishTargetConventionTable').bootstrapTable(finishTargetConventionTableConfig)

			// 检查项表格配置
			var checkConditionsTableConfig = {
				columns: [{
					field: 'text',
					formatter: function(val) {
						if (!val) {
							return '<input type="text">'
						} else {
							return val
						}
					}
				}, {
					width: 80,
					align: 'center',
					formatter: function(val, row) {
						var buttons = [{
							text: '保存',
							type: 'button',
							class: 'item-save',
							isRender: !row.text // 空值时显示保存按钮
						}, {
							text: '删除',
							type: 'button',
							class: 'item-delete',
							isRender: row.text
						}]
						return util.table.formatter.generateButton(buttons)
					},
					events: {
						'click .item-save': function(e, val, row) {
							var inputValue = $(e.target.parentNode.parentNode.parentNode).find('input').val().trim()
							if (inputValue) {
								var currentTable = $('#checkConditionsTable')
								var currentData = currentTable.bootstrapTable('getData')
								currentData.splice(currentData.length - 1, 0, {
									text: inputValue
								})
								currentTable.bootstrapTable('load', currentData)
							}
						},
						'click .item-delete': function(e, val, row, index) {
							var currentTable = $('#checkConditionsTable')
							var currentData = currentTable.bootstrapTable('getData')
							currentData.splice(index, 1)
							currentTable.bootstrapTable('load', currentData)
						}
					}
				}]
			}
			$('#checkConditionsTable').bootstrapTable(checkConditionsTableConfig)

			// 检查项提交按钮点击事件
			$('#doAddCheckConditions').on('click', function() {
				var conditionsData = $('#checkConditionsTable').bootstrapTable('getData')
				conditionsData.splice(conditionsData.length - 1, 1)
				currentOpTarget.checkConditions = conditionsData.map(function(item) {
					return item.text
				})
				currentOpTarget.status = 'yes'
				$('#checkConditionsModal').modal('hide')
			})

			// 驳回理由按钮点击事件
			$('#doAddRejectComment').on('click', function() {
				currentOpTarget.rejectComment = document.rejectForm.rejectComment.value.trim()
				currentOpTarget.status = 'no'
				$('#rejectCommentModal').modal('hide')
			})

			// 会议确认“确认”按钮点击事件
			$('#doFinishTargetConvention').on('click', function() {
				var tableData = $('#finishTargetConventionTable').bootstrapTable('getData')
				var form = document.finishTargetConventionForm
				form.targets.value = JSON.stringify(tableData)
				$(form).ajaxSubmit({

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