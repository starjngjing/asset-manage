/**
 * 资产池管理
 */
define([
	'http',
	'config',
	'util',
	'extension'
], function(http, config, util, $$) {
	return {
		name: 'AssetPool',
		init: function() {
			// 分页配置
			var pageOptions = {
				name: "",
				page: 1,
				rows: 10
			}
			// 数据表格配置
			var tableConfig = {
				ajax: function(origin) {
					http.post(config.api.duration.assetPool.getAll, {
						data: pageOptions,
						contentType: 'form'
					}, function(rlt) {
						origin.success(rlt)
					})
				},
				pageNumber: pageOptions.page,
				pageSize: pageOptions.rows,
				pagination: true,
				sidePagination: 'server',
				pageList: [10, 20, 30, 50, 100],
				queryParams: getQueryParams,
				onLoadSuccess: function() {},
				columns: [{ 
					// 名称
					field: 'name'
				}, 
				{ 
					// 资产规模
					field: 'scale'
				}, 
				{ 
					// 现金比例
					field: 'cashFactRate'
				}, 
				{ 
					// 货币基金（现金类管理工具）比例
					field: 'cashtoolFactRate'
				}, 
				{ 
					// 信托（计划）比例
					field: 'targetFactRate'
				}, 
				{ 
					// 可用现金
					field: 'cashPosition'
				}, 
				{ 
					// 冻结资金
					field: 'freezeCash'
				}, 
				{ 
					// 在途资金
					field: 'transitCash'
				}, 
				{ 
					// 当日收益计算状态
					field: 'scheduleState'
				}, 
				{ 
					// 当日收益分配状态
					field: 'incomeState'
				}, 
				{ 
					// 收益基准日
					field: 'baseDate'
				}, 
				{ 
					// 状态
					field: 'state',
					formatter: function(val) {
						var className = ''
						var str = ''
						switch (parseInt(val)) {
							case 0:
								className = 'text-yellow'
								str = '未审核'
								break
							case 1:
								className = 'text-green'
								str = '存续期'
								break
							case -1:
								className = 'text-red'
								str = '未通过'
								break
						}
						return '<span class="' + className + '">' + str + '</span>'
					}
				}, 
				{
					width: 200,
					align: 'center',
					formatter: function(val, row) {
						var buttons = [{
							text: '详情',
							type: 'button',
							class: 'item-detail',
							isRender: parseInt(row.state) === 1
						}, {
							text: '部分计算',
							type: 'button',
							class: 'item-calc',
							isRender: parseInt(row.state) === 1 && row.scheduleState === '未计算'
						}, {
							text: '不计算',
							type: 'button',
							class: 'item-uncalc',
							isRender: parseInt(row.state) === 1 && row.scheduleState === '未计算'
						}, 
						{
							text: '审核',
							type: 'button',
							class: 'item-audit',
							isRender: parseInt(row.state) === 0
						}, 
						{
							text: '编辑',
							type: 'button',
							class: 'item-update',
							isRender: parseInt(row.state) !== 1
						},  
						{
							text: '详情',
							type: 'button',
							class: 'item-show',
							isRender: parseInt(row.state) !== 1
						}, 
						{
							text: '删除',
							type: 'button',
							class: 'item-delete',
							isRender: parseInt(row.state) === -1
						}]
						return util.table.formatter.generateButton(buttons)
					},
					events: {
						'click .item-detail': function(e, val, row) {
							util.nav.dispatch('AssetPoolDuration', 'id=' + row.oid)
						},
						'click .item-calc': function(e, val, row) {
							currentPool = row
							$$.confirm({
								container: $('#calcConfirmModal'),
								trigger: this,
								accept: function () {
									http.post(config.api.duration.assetPool.userPoolProfit, {
										data: {
											oid: row.oid,
											type: 'USER_CALC'
										},
										contentType: 'form'
									}, function(json) {
										$('#assetPoolTable').bootstrapTable('refresh');
									})
								}
							})
						},
						'click .item-uncalc': function(e, val, row) {
							currentPool = row
							$$.confirm({
								container: $('#calcConfirmModal'),
								trigger: this,
								accept: function () {
									http.post(config.api.duration.assetPool.userPoolProfit, {
										data: {
											oid: row.oid,
											type: 'USER_NONE'
										},
										contentType: 'form'
									}, function(json) {
										$('#assetPoolTable').bootstrapTable('refresh');
									})
								}
							})
						},
						'click .item-audit': function(e, val, row) {
							currentPool = row
							http.post(config.api.duration.assetPool.getById, {
								data: {
									oid: row.oid
								},
								contentType: 'form'
							}, function(json) {
								var scopeStr = ''
								json.result.scopes.forEach(function(item) {
									scopeStr += util.enum.transform('TARGETTYPE', item) + ' '
								})
								json.result.scopeStr = scopeStr
								json.result.scale = json.result.scale / 10000 + '\t万元'
								json.result.cashRate = json.result.cashRate * 100 + '\t%'
								json.result.cashtoolRate = json.result.cashtoolRate * 100 + '\t%'
								json.result.targetRate = json.result.targetRate * 100 + '\t%'
								$('#modal-footer').show()
								$$.detailAutoFix($('#auditAssetPoolModal'), json.result)
								$('#auditAssetPoolModal').modal('show')
							})
						},
						'click .item-update': function(e, val, row) {
							currentPool = row
							http.post(config.api.duration.assetPool.getById, {
								data: {
									oid: row.oid
								},
								contentType: 'form'
							}, function(json) {
								$$.formAutoFix($('#updateAssetPoolForm'), json.result)
								$(document.updateAssetPoolForm.scopes).val(json.result.scopes).trigger('change')
								$('#updateAssetPoolModal').modal('show')
							})
						},
						'click .item-show': function(e, val, row) {
							currentPool = row
							http.post(config.api.duration.assetPool.getById, {
								data: {
									oid: row.oid
								},
								contentType: 'form'
							}, function(json) {
								var scopeStr = ''
								json.result.scopes.forEach(function(item) {
									scopeStr += util.enum.transform('TARGETTYPE', item) + ' '
								})
								json.result.scopeStr = scopeStr
								json.result.scale = json.result.scale / 10000 + '\t万元'
								json.result.cashRate = json.result.cashRate * 100 + '\t%'
								json.result.cashtoolRate = json.result.cashtoolRate * 100 + '\t%'
								json.result.targetRate = json.result.targetRate * 100 + '\t%'
								$('#modal-footer').hide()
								$$.detailAutoFix($('#auditAssetPoolModal'), json.result)
								$('#auditAssetPoolModal').modal('show')
							})
						},
						'click .item-delete': function(e, val, row) {
							currentPool = row
							$$.confirm({
								container: $('#confirmModal'),
								trigger: this,
								accept: function () {
									http.post(config.api.duration.assetPool.delete, {
										data: {
											pid: row.oid
										},
										contentType: 'form'
									}, function () {
										$('#assetPoolTable').bootstrapTable('refresh')
									})
								}
							})
						}
					}
				}]
			}

			// 初始化数据表格
			$('#assetPoolTable').bootstrapTable(tableConfig);
			// 搜索表单初始化
			$$.searchInit($('#searchForm'), $('#assetPoolTable'));

			// 新增/修改资产池投资范围select2初始化
			$(document.addAssetPoolForm.scopes).select2()
			$(document.updateAssetPoolForm.scopes).select2()

			// 新增资产池按钮点击事件
			$('#assetPoolAdd').on('click', function() {
				$('#addAssetPoolModal').modal('show')
			})
			// 新增/修改资产池表单验证初始化
			$('#addAssetPoolForm').validator({
				custom: {
					validfloat: util.form.validator.validfloat,
					validint: util.form.validator.validint,
					validpercentage: validpercentage
				},
				errors: {
					validfloat: '数据格式不正确',
					validint: '数据格式不正确',
					validpercentage: '现金、现金管理类工具、信托计划三者比例总和不能超过100%'
				}
			})
			$('#updateAssetPoolForm').validator({
				custom: {
					validfloat: util.form.validator.validfloat,
					validint: util.form.validator.validint,
					validpercentage: validpercentage
				},
				errors: {
					validfloat: '数据格式不正确',
					validint: '数据格式不正确',
					validpercentage: '现金、现金管理类工具、信托计划三者比例总和不能超过100%'
				}
			})
			// 新增资产池 - 确定按钮点击事件
			$('#doAddAssetPool').on('click', function() {
				if (!$('#addAssetPoolForm').validator('doSubmitCheck')) return
				$('#addAssetPoolForm').ajaxSubmit({
					url: config.api.duration.assetPool.create,
					success: function() {
						util.form.reset($('#addAssetPoolForm'))
						$('#assetPoolTable').bootstrapTable('refresh');
						$('#addAssetPoolModal').modal('hide')
					}
				})
			})
			// 编辑资产池 - 确定按钮点击事件
			$('#doUpdateAssetPool').on('click', function() {
				if (!$('#updateAssetPoolForm').validator('doSubmitCheck')) return
				$('#updateAssetPoolForm').ajaxSubmit({
					url: config.api.duration.assetPool.edit,
					success: function() {
						util.form.reset($('#updateAssetPoolForm'))
						$('#assetPoolTable').bootstrapTable('refresh');
						$('#updateAssetPoolModal').modal('hide')
					}
				})
			})
			// 缓存当前操作数据
			var currentPool = null
			// 审核 - 不通过按钮点击事件
			$('#doUnAuditAssetPool').on('click', function() {
				http.post(config.api.duration.assetPool.audit, {
					data: {
						oid: currentPool.oid,
						operation: 'no'
					},
					contentType: 'form'
				}, function() {
					$('#assetPoolTable').bootstrapTable('refresh');
					$('#auditAssetPoolModal').modal('hide')
				})
			})
			// 审核 - 通过按钮点击事件
			$('#doAuditAssetPool').on('click', function() {
				http.post(config.api.duration.assetPool.audit, {
					data: {
						oid: currentPool.oid,
						operation: 'yes'
					},
					contentType: 'form'
				}, function() {
					$('#assetPoolTable').bootstrapTable('refresh');
					$('#auditAssetPoolModal').modal('hide')
				})
			})

			function getQueryParams(val) {
				var form = document.searchForm
				$.extend(pageOptions, util.form.serializeJson(form)); //合并对象，修改第一个对象

				pageOptions.rows = val.limit
				pageOptions.page = parseInt(val.offset / val.limit) + 1
				return val
			}

			// 自定义验证 - 现金比例/现金管理类工具比例/信托计划比例 加起来不能超过100
			function validpercentage($el) {
				var form = $el.closest('form')
				var parts = form.find('input[data-validpercentage]')
				var percentage = 0
				parts.each(function(index, item) {
					percentage += Number(item.value)
				})
				return !(percentage > 100)
			}
		}
	}
})