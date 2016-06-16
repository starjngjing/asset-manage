/**
 * 资产池存续期管理
 */
define([
	'http',
	'config',
	'util',
	'extension'
], function(http, config, util, $$) {
	return {
		name: 'AssetPoolDuration',
		init: function() {
			var pageState = {
				pid: util.nav.getHashObj(location.hash).id || ''
			}

			// 资产池切换列表
			http.post(config.api.duration.assetPool.getNameList, function(json) {
				var assetPoolOptions = ''
				var select = document.searchForm.assetPoolName
				json.rows.forEach(function(item) {
					assetPoolOptions += '<option value="' + item.oid + '">' + item.name + '</option>'
				})
				$(select).html(assetPoolOptions)
				pageInit(pageState, http, config)
			})
			// 改变资产池后刷新页面
			$(document.searchForm.assetPoolName).on('change', function() {
				pageState.pid = orderingToolPageOptions.pid = toolPageOptions.pid = orderingTrustPageOptions.pid = trustPageOptions.pid = accountDetailPageOptions.pid = this.value
				pageInit(pageState, http, config)
				$('#orderingToolTable').bootstrapTable('refresh')
				$('#toolTable').bootstrapTable('refresh')
				$('#orderingTrustTable').bootstrapTable('refresh')
				$('#trustTable').bootstrapTable('refresh')
			})

			// 资产申购类型radio change事件
			$(document.buyAssetForm.buyType).on('ifChecked', function() {
				if (this.value === 'fund') {
					$('#buyAssetShowFund').show()
					$('#buyAssetShowTrust').hide()
					$('#buyAssetShowTrans').hide()
					$('#profitType').hide().find(':input').attr('disabled', 'disabled')
					$('#transVolumeDiv').hide().find(':input').attr('disabled', 'disabled')
				} else if (this.value === 'trust') {
					$('#buyAssetShowFund').hide()
					$('#buyAssetShowTrust').show()
					$('#buyAssetShowTrans').hide()
					$('#profitType').show().find(':input').attr('disabled', false)
					$('#transVolumeDiv').hide().find(':input').attr('disabled', true)
				} else {
					$('#buyAssetShowFund').hide()
					$('#buyAssetShowTrust').hide()
					$('#buyAssetShowTrans').show()
					$('#profitType').show().find(':input').attr('disabled', false)
					$('#transVolumeDiv').show().find(':input').attr('disabled', false)
				}
				// 资产申购表单验证重置
				$('#buyAssetForm').validator('destroy')
				util.form.validator.init($('#buyAssetForm'))
			})

			// 资产申购按钮点击事件
			$('#buyAsset').on('click', function() {
				http.post(config.api.duration.order.getTargetList, {
					data: {
						pid: pageState.pid
					},
					contentType: 'form'
				}, function(json) {
					targetNames = json
					var fundTargetNameOptions = ''
					var trustTargetNameOptions = ''
					var transTargetNameOptions = ''
					json.fund.forEach(function(item) {
						fundTargetNameOptions += '<option value="' + item.cashtoolOid + '">' + item.cashtoolName + '</option>'
						// 转义
						item.cashtoolType = util.enum.transform('CASHTOOLTYPE', item.cashtoolType)
					})
					json.trust.forEach(function(item) {
						trustTargetNameOptions += '<option value="' + item.targetOid + '">' + item.targetName + '</option>'
						// 转义
						item.targetType = util.enum.transform('TARGETTYPE', item.targetType)
						item.accrualType = util.enum.transform('ACCRUALTYPE', item.accrualType)
						item.raiseScope = parseFloat(item.raiseScope) / 10000
						if (item.floorVolume) {
							item.floorVolume = item.floorVolume / 10000
						}
					})
					json.trans.forEach(function(item) {
						transTargetNameOptions += '<option value="' + item.t_targetOid + '">' + item.t_targetName + '</option>'
						// 转义
						item.t_targetType = util.enum.transform('TARGETTYPE', item.t_targetType)
						item.t_accrualType = util.enum.transform('ACCRUALTYPE', item.t_accrualType)
						item.t_raiseScope = parseFloat(item.t_raiseScope) / 10000
						if (item.t_floorVolume) {
							item.t_floorVolume = item.t_floorVolume / 10000
						}
					})
					$('#fundTargetName').html(fundTargetNameOptions).trigger('change')
					$('#trustTargetName').html(trustTargetNameOptions).trigger('change')
					$('#transTargetName').html(transTargetNameOptions).trigger('change')
				})
				http.post(config.api.duration.assetPool.getNameList, function(json) {
					var assetPoolOptions = ''
					var select = document.buyAssetForm.assetPoolOid
					json.rows.forEach(function(item) {
						assetPoolOptions += '<option value="' + item.oid + '">' + item.name + '</option>'
					})
					$(select).html(assetPoolOptions).val(pageState.pid)
				})
				$('#buyAssetModal').modal('show')
			})

			// 缓存标的名称数组值
			var targetNames = null
			// 缓存可用现金
			var freeCash = 0
			// 缓存持有份额
			var holdAmount = 0;

			// 资产申购标的名称下拉菜单change事件
			$('#fundTargetName').on('change', function() {
				var source = targetNames.fund.filter(function(item) {
					return item.cashtoolOid === this.value
				}.bind(this))
				if (source[0]) {
					$$.formAutoFix($('#buyAssetForm'), source[0])
				}
			})
			$('#trustTargetName').on('change', function() {
				var source = targetNames.trust.filter(function(item) {
					return item.targetOid === this.value
				}.bind(this))
				if (source[0]) {
					$$.formAutoFix($('#buyAssetForm'), source[0])
				}
			})
			$('#transTargetName').on('change', function() {
				var source = targetNames.trans.filter(function(item) {
					return item.t_targetOid === this.value
				}.bind(this))
				if (source[0]) {
					$$.formAutoFix($('#buyAssetForm'), source[0])
				}
			})

			// 资产申购 - 提交审核按钮点击事件
			$('#doBuyAsset').on('click', function() {
				var form = document.buyAssetForm
				var url = ''
				if (form.buyType.value === 'fund') {
					url = config.api.duration.order.purchaseForFund
				} else if (form.buyType.value === 'trust') {
					url = config.api.duration.order.purchaseForTrust
				} else {
					url = config.api.duration.order.purchaseForTrans
				}
				if (!$(form).validator('doSubmitCheck')) return
				$(form).ajaxSubmit({
					url: url,
					success: function() {
						util.form.reset($(form))
						$('#orderingToolTable').bootstrapTable('refresh')
						$('#orderingTrustTable').bootstrapTable('refresh')
						pageInit(pageState, http, config)
						$('#buyAssetModal').modal('hide')
					}
				})
			})

			// 出入金明细按钮点击事件
			$('#showAccountDetail').on('click', function() {
				$('#accountDetailTable').bootstrapTable('refresh')
				$('#accountDetailModal').modal('show')
			})

			// 出入金明细表格配置
			var accountDetailPageOptions = {
				page: 1,
				rows: 10,
				pid: pageState.pid
			}
			var accountDetailTableConfig = {
				ajax: function(origin) {
					http.post(config.api.duration.assetPool.getAllCapitalList, {
						data: accountDetailPageOptions,
						contentType: 'form'
					}, function(rlt) {
						origin.success(rlt)
					})
				},
				pageNumber: accountDetailPageOptions.page,
				pageSize: accountDetailPageOptions.rows,
				pagination: true,
				sidePagination: 'server',
				pageList: [10, 20, 30, 50, 100],
				queryParams: function(val) {
					accountDetailPageOptions.rows = val.limit
					accountDetailPageOptions.page = parseInt(val.offset / val.limit) + 1
					return val
				},
				columns: [{
					width: 60,
					align: 'center',
					formatter: function(val, row, index) {
						return index + 1
					}
				}, 
				{
					field: 'subject'
				}, 
				{
					field: 'createTime'
				}, 
				{
					field: 'operation'
				}, 
				{
					field: 'capital'
				}, 
				{
					field: 'status'
				}, 
				{
					width: 100,
					align: 'center',
					formatter: function() {
						var buttons = [{
							text: '查看详情',
							type: 'button',
							class: 'item-detail'
						}]
						return util.table.formatter.generateButton(buttons)
					},
					events: {
						'click .item-detail': function(e, val, row) {
							http.post(config.api.duration.order.getTargetOrderByOidForCapital, {
								data: {
									oid: row.orderOid,
									operation: row.operation
								},
								contentType: 'form'
							}, function(json) {
								// 操作类型（现金管理工具申购，现金管理工赎回，投资标的申购，本息兑付，投资标的转入，投资标的转出）
								if (row.operation === '现金管理工具申购' || row.operation === '现金管理工赎回') {
									json.result.cashtoolType = util.enum.transform('CASHTOOLTYPE', json.result.cashtoolType)
									if (json.result.applyCash) {
										json.result.applyCash = json.result.applyCash + '\t万元'
									}
									if (json.result.returnVolume) {
										json.result.returnVolume = json.result.returnVolume + '\t万元'
									}
									if (json.result.auditVolume) {
										json.result.auditVolume = json.result.auditVolume + '\t万元'
									}
									if (json.result.reserveVolume) {
										json.result.reserveVolume = json.result.reserveVolume + '\t万元'
									}
									if (json.result.investVolume) {
										json.result.investVolume = json.result.investVolume + '\t万元'
									}
									if (row.operation === '现金管理工具申购') {
										$('#applyVolume').show()
										$('#redeemVolume').hide()
									} else {
										$('#applyVolume').hide()
										$('#redeemVolume').show()
									}
									$$.detailAutoFix($('#fundDetailModal'), json.result)
									$('#fundDetailModal').modal('show')
								} else if (row.operation === '投资标的申购') {
									var result = json.result
									result.targetType = util.enum.transform('TARGETTYPE', result.targetType)
									result.accrualType = util.enum.transform('ACCRUALTYPE', result.accrualType)
									result.raiseScope = parseFloat(result.raiseScope) / 10000 + "\t万元"
									result.raiseScope = parseFloat(result.raiseScope) / 10000 + '万元'
									if (result.expAror) {
										result.expAror = result.expAror + '\t%'
									}
									if (result.collectIncomeRate) {
										result.collectIncomeRate = result.collectIncomeRate + '\t%'
									}
									if (result.applyVolume) {
										result.applyVolume = result.applyVolume + '\t万份'
									}
									if (result.applyCash) {
										result.applyCash = result.applyCash + '\t万元'
									}
									if (result.life) {
										result.life = result.life + '\t天'
									}
									if (result.floorVolume) {
										result.floorVolume = parseFloat(result.floorVolume) / 10000 + '\t万元'
									}
									if (result.auditVolume) {
										result.auditVolume = result.auditVolume + '\t万份'
									}
									if (result.auditCash) {
										result.auditCash = result.auditCash + '\t万元'
									}
									if (result.reserveVolume) {
										result.reserveVolume = result.reserveVolume + '\t万份'
									}
									if (result.reserveCash) {
										result.reserveCash = result.reserveCash + '\t万元'
									}
									if (result.investVolume) {
										result.investVolume = result.investVolume + '\t万份'
									}
									if (result.investCash) {
										result.investCash = result.investCash + '\t万元'
									}
									$$.detailAutoFix($('#trustDetailModal'), json.result)
									$('#trustDetailModal').modal('show')
								} else if (row.operation === '本息兑付') {
									var result = json.result
									if (result.seq) {
										result.seq = result.seq + '\t期'
									}
									if (result.incomeRate) {
										result.incomeRate = result.incomeRate + '\t%'
									}
									if (result.income) {
										result.income = result.income + '\t万元'
									}
									if (result.capital) {
										result.capital = result.capital + '\t万元'
									}
									if (result.auditVolume) {
										result.auditVolume = result.auditVolume + '\t%'
									}
									if (result.auditCash) {
										result.auditCash = result.auditCash + '\t万元'
									}
									if (result.auditCapital) {
										result.auditCapital = result.auditCapital + '\t万元'
									}
									if (result.investVolume) {
										result.investVolume = result.investVolume + '\t%'
									}
									if (result.investCash) {
										result.investCash = result.investCash + '\t万元'
									}
									if (result.investCapital) {
										result.investCapital = result.investCapital + '\t万元'
									}
									$$.detailAutoFix($('#trustIncomeOrderDetailModal'), json.result)
									$('#trustIncomeOrderDetailModal').modal('show')
								} else if (row.operation === '投资标的转入' || row.operation === '投资标的转出') {
									var result = json.result
									result.targetType = util.enum.transform('TARGETTYPE', result.targetType)
									result.accrualType = util.enum.transform('ACCRUALTYPE', result.accrualType)
									result.raiseScope = parseFloat(result.raiseScope) / 10000 + "\t万元"
									result.raiseScope = parseFloat(result.raiseScope) / 10000 + '万元'
									if (result.expAror) {
										result.expAror = result.expAror + '\t%'
									}
									if (result.applyVolume) {
										result.applyVolume = result.applyVolume + '\t万份'
									}
									if (result.applyCash) {
										result.applyCash = result.applyCash + '\t万元'
									}
									if (result.life) {
										result.life = result.life + '\t天'
									}
									if (result.floorVolume) {
										result.floorVolume = parseFloat(result.floorVolume) / 10000 + '\t万元'
									}
									if (result.auditVolume) {
										result.auditVolume = result.auditVolume + '\t万份'
									}
									if (result.auditCash) {
										result.auditCash = result.auditCash + '\t万元'
									}
									if (result.reserveVolume) {
										result.reserveVolume = result.reserveVolume + '\t万份'
									}
									if (result.reserveCash) {
										result.reserveCash = result.reserveCash + '\t万元'
									}
									if (result.investVolume) {
										result.investVolume = result.investVolume + '\t万份'
									}
									if (result.investCash) {
										result.investCash = result.investCash + '\t万元'
									}
									$$.detailAutoFix($('#trustTransOrderDetailModal'), json.result)
									$('#trustTransOrderDetailModal').modal('show')
								}
							})
						}
					}
				}]
			}
			$('#accountDetailTable').bootstrapTable(accountDetailTableConfig)

			// 预约中现金类管理工具分页信息
			var orderingToolPageOptions = {
				page: 1,
				rows: 10,
				pid: pageState.pid
			}
			// 预约中现金类管理工具表格配置
			var orderingToolTableConfig = {
				ajax: function(origin) {
					http.post(config.api.duration.order.getFundListForAppointment, {
						data: orderingToolPageOptions,
						contentType: 'form'
					}, function(rlt) {
						origin.success(rlt)
					})
				},
				pageNumber: orderingToolPageOptions.page,
				pageSize: orderingToolPageOptions.rows,
				pagination: true,
				sidePagination: 'server',
				pageList: [10, 20, 30, 50, 100],
				queryParams: function(val) {
					orderingToolPageOptions.rows = val.limit
					orderingToolPageOptions.page = parseInt(val.offset / val.limit) + 1
					return val
				},
				columns: [{
					width: 60,
					align: 'center',
					formatter: function(val, row, index) {
						return index + 1
					}
				}, 
				{
					field: 'cashtoolName'
				}, 
				{
					field: 'cashtoolType',
					formatter: function(val) {
						return util.enum.transform('CASHTOOLTYPE', val)
					}
				}, 
				{
					field: 'netRevenue'
				}, 
				{
					field: 'yearYield7'
				},
				//        {
				//          field: 'riskLevel'
				//        },
				//        {
				//          field: 'dividendType'
				//        },
				//        {
				//          field: 'circulationShares'
				//        },
				{
					field: 'investDate'
				}, 
				{
					field: 'applyCash'
				}, 
				{
					field: 'optType',
					formatter: function(val) {
						return val === 'purchase' ? '申购' : '赎回'
					}
				}, 
				{
					field: 'state',
					formatter: function(val) {
						switch (val) {
//									case '-2':
//										return '<span class="text-red">未通过</span>'
//									case '-1':
//										return '<span class="text-aqua">待审核</span>'
//									case '0':
//										return '<span class="text-blue">待预约</span>'
//									case '1':
//										return '<span class="text-yellow">待确认</span>'
//									case '2':
//										return '<span class="text-green">成立</span>'
							case '00':
								return '<span class="text-aqua">待审核</span>'
							case '10':
								return '<span class="text-red">审核未通过</span>'
							case '11':
								return '<span class="text-blue">审核通过待预约</span>'
							case '20':
								return '<span class="text-red">预约未通过</span>'
							case '21':
								return '<span class="text-yellow">预约通过待确认</span>'
							case '30':
								return '<span class="text-red">确认未通过</span>'
						}
					}
				}, 
				{
					width: 180,
					align: 'center',
					formatter: function(val, row) {
						var buttons = [{
							text: '审核',
							type: 'button',
							class: 'item-audit',
        					isRender: parseInt(row.state) === 0
						}, 
						{
							text: '预约',
							type: 'button',
							class: 'item-ordering',
        					isRender: parseInt(row.state) === 0 || parseInt(row.state) === 11
						}, 
						{
							text: '确认',
							type: 'button',
							class: 'item-accpet',
        					isRender: parseInt(row.state) === 0 || parseInt(row.state) === 11 || parseInt(row.state) === 21
						}, 
						{
							text: '查看详情',
							type: 'button',
							class: 'item-detail',
        					isRender: parseInt(row.state) === 10 || parseInt(row.state) === 20 || parseInt(row.state) === 30
						}, 
						{
							text: '删除',
							type: 'button',
							class: 'item-delete',
        					isRender: parseInt(row.state) === 10 || parseInt(row.state) === 20 || parseInt(row.state) === 30
						}]
						return util.table.formatter.generateButton(buttons)
					},
					events: {
						'click .item-audit': function(e, val, row) {
							var modal = $('#fundCheckModal')
							if (row.optType === 'purchase') {
								$('#purchaseArea').show()
								$('#redeemArea').hide()
							} else {
								$('#purchaseArea').hide()
								$('#redeemArea').show()
							}
							http.post(config.api.duration.order.getFundOrderByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result
								var form = document.fundCheckForm
								form.oid.value = result.oid
								form.cashtoolOid.value = result.cashtoolOid
								form.opType.value = 'audit'
								form.assetPoolOid.value = pageState.pid
								var formGroups = $(form).find('.form-group')
								formGroups.each(function(index, item) {
									if (!index) {
										$(item).css({
											display: 'block'
										}).find('input').attr('disabled', false)
									} else {
										$(item).css({
											display: 'none'
										}).find('input').attr('disabled', 'disabled')
									}
								})
								$(form).validator('destroy')
								util.form.validator.init($(form))
								modal.find('.labelForOrdering').css({
									display: 'none'
								})
								modal.find('.labelForAccept').css({
									display: 'none'
								})
//										if (result.netRevenue) {
//											result.netRevenue = result.netRevenue + '\t元'
//										}
								if (result.yearYield7) {
									result.yearYield7 = result.yearYield7 + '\t%'
								}
								result.applyCash = result.applyCash + '\t万元'
								result.returnVolume = result.returnVolume + '\t万元'
								result.cashtoolTypeStr = util.enum.transform('CASHTOOLTYPE', result.cashtoolType)
								$$.detailAutoFix(modal, result)
							})
							modal.modal('show')
						},
						'click .item-ordering': function(e, val, row) {
							var modal = $('#fundCheckModal')
							if (row.optType === 'purchase') {
								$('#purchaseArea').show()
								$('#redeemArea').hide()
							} else {
								$('#purchaseArea').hide()
								$('#redeemArea').show()
							}
							http.post(config.api.duration.order.getFundOrderByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result
								var form = document.fundCheckForm
								form.oid.value = result.oid
								form.cashtoolOid.value = result.cashtoolOid
								form.opType.value = 'ordering'
								form.assetPoolOid.value = pageState.pid
								var formGroups = $(form).find('.form-group')
								formGroups.each(function(index, item) {
									if (index === 1) {
										$(item).css({
											display: 'block'
										}).find('input').attr('disabled', false)
									} else {
										$(item).css({
											display: 'none'
										}).find('input').attr('disabled', 'disabled')
									}
								})
								$(form).validator('destroy')
								util.form.validator.init($(form))
								modal.find('.labelForOrdering').css({
									display: 'block'
								})
								modal.find('.labelForAccept').css({
									display: 'none'
								})
								result.cashtoolTypeStr = util.enum.transform('CASHTOOLTYPE', result.cashtoolType)
//										if (result.netRevenue) {
//											result.netRevenue = result.netRevenue + '\t元'
//										}
								if (result.yearYield7) {
									result.yearYield7 = result.yearYield7 + '\t%'
								}
								result.applyCash = result.applyCash + '\t万元'
								result.returnVolume = result.returnVolume + '\t万元'
								if (result.auditVolume) {
									result.auditVolume = result.auditVolume + '\t万元'
								}
								$$.detailAutoFix(modal, result)
							})
							modal.modal('show')
						},
						'click .item-accpet': function(e, val, row) {
							var modal = $('#fundCheckModal')
							if (row.optType === 'purchase') {
								$('#purchaseArea').show()
								$('#redeemArea').hide()
							} else {
								$('#purchaseArea').hide()
								$('#redeemArea').show()
							}
							http.post(config.api.duration.order.getFundOrderByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result
								var form = document.fundCheckForm
								form.oid.value = result.oid
								form.cashtoolOid.value = result.cashtoolOid
								form.opType.value = 'accept'
								form.assetPoolOid.value = pageState.pid
								var formGroups = $(form).find('.form-group')
								formGroups.each(function(index, item) {
									if (index === 2) {
										$(item).css({
											display: 'block'
										}).find('input').attr('disabled', false)
									} else {
										$(item).css({
											display: 'none'
										}).find('input').attr('disabled', 'disabled')
									}
								})
								$(form).validator('destroy')
								util.form.validator.init($(form))
								modal.find('.labelForOrdering').css({
									display: 'block'
								})
								modal.find('.labelForAccept').css({
									display: 'block'
								})
								result.cashtoolTypeStr = util.enum.transform('CASHTOOLTYPE', result.cashtoolType)
//										if (result.netRevenue) {
//											result.netRevenue = result.netRevenue + '\t元'
//										}
								if (result.yearYield7) {
									result.yearYield7 = result.yearYield7 + '\t%'
								}
								result.applyCash = result.applyCash + '\t万元'
								result.returnVolume = result.returnVolume + '\t万元'
								if (result.auditVolume) {
									result.auditVolume = result.auditVolume + '\t万元'
								}
								if (result.reserveVolume) {
									result.reserveVolume = result.reserveVolume + '\t万元'
								}
								$$.detailAutoFix(modal, result)
							})
							modal.modal('show')
						},
						'click .item-detail': function(e, val, row) {
							var modal = $('#fundOrderDetailModal')
							if (row.optType === 'purchase') {
								$('#orderPurchaseArea').show()
								$('#orderRedeemArea').hide()
							} else {
								$('#orderPurchaseArea').hide()
								$('#orderRedeemArea').show()
							}
							http.post(config.api.duration.order.getFundOrderByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result
								modal.find('.labelForOrdering').css({
									display: 'block'
								})
								modal.find('.labelForAccept').css({
									display: 'block'
								})
								result.cashtoolTypeStr = util.enum.transform('CASHTOOLTYPE', result.cashtoolType)
								if (result.yearYield7) {
									result.yearYield7 = result.yearYield7 + '\t%'
								}
								result.applyCash = result.applyCash + '\t万元'
								result.returnVolume = result.returnVolume + '\t万元'
								if (result.auditVolume) {
									result.auditVolume = result.auditVolume + '\t万元'
								}
								if (result.reserveVolume) {
									result.reserveVolume = result.reserveVolume + '\t万元'
								}
								$$.detailAutoFix(modal, result)
							})
							modal.modal('show')
						},
						'click .item-delete': function(e, val, row) {
							$$.confirm({
								container: $('#confirmModal'),
								trigger: this,
								accept: function () {
									http.post(config.api.duration.order.delete, {
										data: {
											oid: row.oid,
											operation: '现金管理工具'
										},
										contentType: 'form'
									}, function () {
										$('#orderingToolTable').bootstrapTable('refresh')
									})
								}
							})
						}
					}
				}]
			}
			// 预约中现金类管理工具表格初始化
			$('#orderingToolTable').bootstrapTable(orderingToolTableConfig)

			// 现金类管理工具分页信息
			var toolPageOptions = {
				page: 1,
				rows: 10,
				pid: pageState.pid
			}
			// 现金类管理工具表格配置
			var toolTableConfig = {
				ajax: function(origin) {
					http.post(config.api.duration.order.getFundList, {
						data: toolPageOptions,
						contentType: 'form'
					}, function(rlt) {
						origin.success(rlt)
					})
				},
				pageNumber: toolPageOptions.page,
				pageSize: toolPageOptions.rows,
				pagination: true,
				sidePagination: 'server',
				pageList: [10, 20, 30, 50, 100],
				queryParams: function(val) {
					toolPageOptions.rows = val.limit
					toolPageOptions.page = parseInt(val.offset / val.limit) + 1
					return val
				},
				onLoadSuccess: function() {},
				columns: [{
					width: 60,
					align: 'center',
					formatter: function(val, row, index) {
						return index + 1
					}
				}, 
				{
					field: 'cashtoolName'
				}, 
				{
					field: 'cashtoolType',
					formatter: function(val) {
						return util.enum.transform('CASHTOOLTYPE', val)
					}
				}, 
				{
					field: 'netRevenue'
				}, 
				{
					field: 'yearYield7'
				},
				//        {
				//          field: 'riskLevel'
				//        },
				//        {
				//          field: 'dividendType'
				//        },
				//        {
				//          field: 'circulationShares'
				//        },
				{
					field: 'amount'
				}, 
				{
					field: 'redeemVolume'
				},
				//        {
				//          field: 'state',
				//          formatter: function (val) {
				//            switch (val) {
				//              case '-1':
				//                return '<span class="text-aqua">未通过</span>'
				//              case '0':
				//                return '<span class="text-blue">成立</span>'
				//            }
				//          }
				//        },
				{
					field: 'dailyProfit'
				}, 
				{
					field: 'totalProfit'
				}, 
				{
					width: 150,
					align: 'center',
					formatter: function() {
						var buttons = [{
							text: '申购',
							type: 'button',
							class: 'item-purchase'
						}, {
							text: '赎回',
							type: 'button',
							class: 'item-redeem'
						}, {
							text: '纠偏',
							type: 'button',
							class: 'item-update'
						}]
						return util.table.formatter.generateButton(buttons)
					},
					events: {
						'click .item-purchase': function(e, val, row) {
							http.post(config.api.duration.order.getFundByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								json.result.cashtoolType = util.enum.transform('CASHTOOLTYPE', json.result.cashtoolType)
								$$.formAutoFix($('#purchaseForm'), json.result)
							})
							$('#purchaseModal').modal('show')
						},
						'click .item-redeem': function(e, val, row) {
							http.post(config.api.duration.order.getFundByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								json.result.cashtoolType = util.enum.transform('CASHTOOLTYPE', json.result.cashtoolType)
								$$.formAutoFix($('#redeemForm'), json.result)
							})
							$('#redeemModal').modal('show')
						},
						'click .item-update': function(e, val, row) {
							http.post(config.api.duration.order.getFundByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								json.result.amount = json.result.amount + '\t万元'
								document.updateFundVolumeForm.oid.value = json.result.oid
								$$.detailAutoFix($('#updateFundVolumeModal'), json.result)
								$('#updateFundVolumeModal').modal('show')
							})
						}
					}
				}]
			}
			// 现金类管理工具表格初始化
			$('#toolTable').bootstrapTable(toolTableConfig)

			// 现金类管理工具 - 申购表格验证初始化
			util.form.validator.init($('#purchaseForm'))
			// 现金类管理工具 - 赎回表格验证初始化
//			util.form.validator.init($('#redeemForm'))

			// 现金类管理工具审核/预约/确认 - 通过按钮点击事件
			$('#doFundCheck').on('click', function() {
				var form = document.fundCheckForm
				form.state.value = '0'
				var url = ''
				switch (form.opType.value) {
					case 'audit':
						url = config.api.duration.order.auditForFund
						break
					case 'ordering':
						url = config.api.duration.order.appointmentForFund
						break
					default:
						url = config.api.duration.order.orderConfirmForFund
						break
				}
				if (!$(form).validator('doSubmitCheck')) return
				$(form).ajaxSubmit({
					url: url,
					success: function() {
						util.form.reset($(form))
						$('#orderingToolTable').bootstrapTable('refresh')
						$('#toolTable').bootstrapTable('refresh')
						pageInit(pageState, http, config)
						$('#fundCheckModal').modal('hide')
					}
				})
			})
			// 现金类管理工具审核/预约/确认 - 不通过按钮点击事件
			$('#doFundUnCheck').on('click', function() {
				var form = document.fundCheckForm
				form.state.value = '-1'
				var url = ''
				switch (form.opType.value) {
					case 'audit':
						url = config.api.duration.order.auditForFund
						break
					case 'ordering':
						url = config.api.duration.order.appointmentForFund
						break
					default:
						url = config.api.duration.order.orderConfirmForFund
						break
				}
				$(form).ajaxSubmit({
					url: url,
					success: function() {
						util.form.reset($(form))
						$('#orderingToolTable').bootstrapTable('refresh')
						$('#toolTable').bootstrapTable('refresh')
						pageInit(pageState, http, config)
						$('#fundCheckModal').modal('hide')
					}
				})
			})

			// 现金类管理工具 - 申购弹窗 - 提交审核按钮点击事件
			$('#doPurchase').on('click', function() {
				var form = document.purchaseForm
				if (!$(form).validator('doSubmitCheck')) return
				$(form).ajaxSubmit({
					url: config.api.duration.order.purchaseForFund,
					success: function() {
						util.form.reset($(form))
						$('#orderingToolTable').bootstrapTable('refresh')
						$('#toolTable').bootstrapTable('refresh')
						pageInit(pageState, http, config)
						$('#purchaseModal').modal('hide')
					}
				})
			})

			// 现金类管理工具 - 赎回弹窗 - 提交审核按钮点击事件
			$('#doRedeem').on('click', function() {
				var form = document.redeemForm
				if (!$(form).validator('doSubmitCheck')) return
				$(form).ajaxSubmit({
					url: config.api.duration.order.redeem,
					success: function() {
						util.form.reset($(form))
						$('#orderingToolTable').bootstrapTable('refresh')
						$('#toolTable').bootstrapTable('refresh')
						pageInit(pageState, http, config)
						$('#redeemModal').modal('hide')
					}
				})
			})

			// 预约中信托计划分页信息
			var orderingTrustPageOptions = {
				page: 1,
				rows: 10,
				pid: pageState.pid
			}
			// 预约中信托计划表格配置
			var orderingTrustTableConfig = {
				ajax: function(origin) {
					http.post(config.api.duration.order.getTrustListForAppointment, {
						data: orderingTrustPageOptions,
						contentType: 'form'
					}, function(rlt) {
						origin.success(rlt)
					})
				},
				pageNumber: orderingTrustPageOptions.page,
				pageSize: orderingTrustPageOptions.rows,
				pagination: true,
				sidePagination: 'server',
				pageList: [10, 20, 30, 50, 100],
				queryParams: function(val) {
					orderingTrustPageOptions.rows = val.limit
					orderingTrustPageOptions.page = parseInt(val.offset / val.limit) + 1
					return val
				},
				onLoadSuccess: function() {},
				columns: [{
					width: 60,
					align: 'center',
					formatter: function(val, row, index) {
						return index + 1
					}
				}, 
				{
					field: 'targetName'
				}, 
				{
					field: 'expAror'
				}, 
				{
					field: 'setDate'
				}, 
				{
					field: 'accrualType',
					formatter: function(val) {
						return util.enum.transform('ACCRUALTYPE', val)
					}
				}, 
				{
					field: 'raiseScope',
					formatter: function(val) {
						return parseInt(val) / 10000
					}
				}, 
				{
					field: 'applyVolume'
				}, 
				{
					field: 'applyCash'
				}, 
				{
					field: 'subjectRating'
				}, 
				{
					field: 'type'
				}, 
				{
					field: 'profitType',
					formatter: function(val) {
						return 'amortized_cost' === val ? '摊余成本法' : '账面价值法'
					}
				}, 
				{
					field: 'state',
					formatter: function(val) {
						switch (val) {
//								case '-2':
//									return '<span class="text-red">未通过</span>'
//								case '-1':
//									return '<span class="text-aqua">待审核</span>'
//								case '0':
//									return '<span class="text-blue">待预约</span>'
//								case '1':
//									return '<span class="text-yellow">待确认</span>'
//								case '2':
//									return '<span class="text-green">成立</span>'
							case '00':
								return '<span class="text-aqua">待审核</span>'
							case '10':
								return '<span class="text-red">审核未通过</span>'
							case '11':
								return '<span class="text-blue">审核通过待预约</span>'
							case '20':
								return '<span class="text-red">预约未通过</span>'
							case '21':
								return '<span class="text-yellow">预约通过待确认</span>'
							case '30':
								return '<span class="text-red">确认未通过</span>'
						}
					}
				}, 
				{
					width: 256,
					align: 'center',
					formatter: function(val, row) {
						// 操作类型（现金管理工具申购，现金管理工赎回，投资标的申购，本息兑付，投资标的转入，投资标的转出）
						var buttons = [{
							text: '审核',
							type: 'button',
							class: 'item-audit',
							isRender: row.type === '投资标的申购' && (parseInt(row.state) === 0)
						}, 
						{
							text: '预约',
							type: 'button',
							class: 'item-ordering',
							isRender: row.type === '投资标的申购' && (parseInt(row.state) === 0 || parseInt(row.state) === 11)
						}, 
						{
							text: '确认',
							type: 'button',
							class: 'item-accpet',
							isRender: row.type === '投资标的申购' && (parseInt(row.state) === 0 || parseInt(row.state) === 11 || parseInt(row.state) === 21)
						}, {
							text: '审核',
							type: 'button',
							class: 'item-trans-audit',
							isRender: row.type === '投资标的转入' && (parseInt(row.state) === 0)
						}, 
						{
							text: '预约',
							type: 'button',
							class: 'item-trans-ordering',
							isRender: row.type === '投资标的转入' && (parseInt(row.state) === 0 || parseInt(row.state) === 11)
						}, 
						{
							text: '确认',
							type: 'button',
							class: 'item-trans-accpet',
							isRender: row.type === '投资标的转入' && (parseInt(row.state) === 0 || parseInt(row.state) === 11 || parseInt(row.state) === 21)
						}, 
						{
							text: '本息兑付审核',
							type: 'button',
							class: 'item-income-audit',
							isRender: row.type === '本息兑付' && (parseInt(row.state) === 0)
						},
						{
							text: '本息兑付确认',
							type: 'button',
							class: 'item-income-accpet',
							isRender: row.type === '本息兑付' && (parseInt(row.state) === 0 || parseInt(row.state) === 11 || parseInt(row.state) === 21)
						}, 
						{
							text: '转让审核',
							type: 'button',
							class: 'item-transfer-audit',
							isRender: row.type === '投资标的转出' && (parseInt(row.state) === 0)
						}, 
						{
							text: '转让确认',
							type: 'button',
							class: 'item-transfer-accpet',
							isRender: row.type === '投资标的转出' && (parseInt(row.state) === 0 || parseInt(row.state) === 11 || parseInt(row.state) === 21)
						}, 
						{
							text: '查看详情',
							type: 'button',
							class: 'item-detail',
        					isRender: row.type === '投资标的申购' && (parseInt(row.state) === 10 || parseInt(row.state) === 20 || parseInt(row.state) === 30)
						},  
						{
							text: '查看详情',
							type: 'button',
							class: 'item-trans-detail',
        					isRender: (row.type === '投资标的转入' || row.type === '投资标的转出') && (parseInt(row.state) === 10 || parseInt(row.state) === 20 || parseInt(row.state) === 30)
						}, 
						{
							text: '查看详情',
							type: 'button',
							class: 'item-income-detail',
        					isRender: row.type === '本息兑付' && (parseInt(row.state) === 10 || parseInt(row.state) === 20 || parseInt(row.state) === 30)
						}, 
						{
							text: '删除',
							type: 'button',
							class: 'item-delete',
        					isRender: parseInt(row.state) === 10 || parseInt(row.state) === 20 || parseInt(row.state) === 30
						}]
						return util.table.formatter.generateButton(buttons)
					},
					events: {
						'click .item-audit': function(e, val, row) {
							var modal = $('#trustCheckModal')
							http.post(config.api.duration.order.getTrustOrderByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result
								var form = document.trustCheckForm
								form.oid.value = result.oid
								form.type.value = row.type
								form.opType.value = 'audit'
								form.assetPoolOid.value = pageState.pid
								var formGroups = $(form).find('.form-group')
								formGroups.each(function(index, item) {
									if (!index) {
										$(item).css({
											display: 'block'
										}).find('input').attr('disabled', false)
									} else {
										$(item).css({
											display: 'none'
										}).find('input').attr('disabled', 'disabled')
									}
								})
								$(form).validator('destroy')
								util.form.validator.init($(form))
								modal.find('.labelForOrdering').css({
									display: 'none'
								})
								modal.find('.labelForAccept').css({
									display: 'none'
								})
								result.targetTypeStr = util.enum.transform('TARGETTYPE', result.targetType)
								result.accrualType = util.enum.transform('ACCRUALTYPE', result.accrualType)
								result.raiseScope = parseFloat(result.raiseScope) / 10000 + '万元'
								if (result.expAror) {
									result.expAror = result.expAror + '\t%'
								}
								if (result.collectIncomeRate) {
									result.collectIncomeRate = result.collectIncomeRate + '\t%'
								}
								if (result.applyVolume) {
									result.applyVolume = result.applyVolume + '\t万份'
								}
								if (result.applyCash) {
									result.applyCash = result.applyCash + '\t万元'
								}
								if (result.life) {
									result.life = result.life + '\t天'
								}
								if (result.floorVolume) {
									result.floorVolume = parseFloat(result.floorVolume) / 10000 + '\t万元'
								}
								$$.detailAutoFix(modal, result)
							})
							modal.modal('show')
						},
						'click .item-trans-audit': function(e, val, row) {
							var modal = $('#trustTransCheckModal')
							http.post(config.api.duration.order.getTrustOrderByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result
								var form = document.trustTransCheckForm
								form.oid.value = result.oid
								form.type.value = row.type
								form.opType.value = 'audit'
								form.assetPoolOid.value = pageState.pid
								var formGroups = $(form).find('.row')
								formGroups.each(function(index, item) {
									if (!index) {
										$(item).css({
											display: 'block'
										}).find('input').attr('disabled', false)
									} else {
										$(item).css({
											display: 'none'
										}).find('input').attr('disabled', 'disabled')
									}
								})
								$(form).validator('destroy')
								util.form.validator.init($(form))
								modal.find('.labelForAudit').css({
									display: 'none'
								})
								modal.find('.labelForOrdering').css({
									display: 'none'
								})
								modal.find('.labelForAccept').css({
									display: 'none'
								})
								result.targetTypeStr = util.enum.transform('TARGETTYPE', result.targetType)
								result.accrualType = util.enum.transform('ACCRUALTYPE', result.accrualType)
								result.raiseScope = parseFloat(result.raiseScope) / 10000 + '万元'
								if (result.expAror) {
									result.expAror = result.expAror + '\t%'
								}
								if (result.applyVolume) {
									result.applyVolume = result.applyVolume + '\t万份'
								}
								if (result.applyCash) {
									result.applyCash = result.applyCash + '\t万元'
								}
								if (result.life) {
									result.life = result.life + '\t天'
								}
								if (result.floorVolume) {
									result.floorVolume = parseFloat(result.floorVolume) / 10000 + '\t万元'
								}
								$$.detailAutoFix(modal, result)
							})
							modal.modal('show')
						},
						'click .item-income-audit': function(e, val, row) {
							var modal = $('#trustIncomeCheckModal')
							http.post(config.api.duration.order.getTrustOrderByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result
								var form = document.trustIncomeCheckForm
								form.oid.value = result.oid
								form.seq.value = result.seq
								form.type.value = row.type
								form.opType.value = 'audit'
								form.assetPoolOid.value = pageState.pid
								var formGroups = $(form).find('.row')
								formGroups.each(function(index, item) {
									if (!index) {
										$(item).css({
											display: 'block'
										}).find('input').attr('disabled', false)
									} else {
										$(item).css({
											display: 'none'
										}).find('input').attr('disabled', 'disabled')
									}
								})
								$(form).validator('destroy')
								util.form.validator.init($(form))
								modal.find('.labelForOrdering').css({
									display: 'none'
								})
								modal.find('.labelForAccept').css({
									display: 'none'
								})
								if (result.seq) {
									result.seq = result.seq + '\t期'
								}
								if (result.incomeRate) {
									result.incomeRate = result.incomeRate + '\t%'
								}
								if (result.income) {
									result.income = result.income + '\t万元'
								}
								if (result.capital) {
									result.capital = result.capital + '\t万元'
								}
//								if (result.applyVolume) {
//									result.applyVolume = result.applyVolume + '\t万份'
//								}
//								if (result.applyCash) {
//									result.applyCash = result.applyCash + '\t万元'
//								}
								$$.detailAutoFix(modal, result)
							})
							modal.modal('show')
						},
						'click .item-transfer-audit': function(e, val, row) {
							var modal = $('#transCheckModal')
							http.post(config.api.duration.order.getTrustOrderByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result
								var form = document.transCheckForm
								form.oid.value = result.oid
								form.type.value = row.type
								form.opType.value = 'audit'
								form.assetPoolOid.value = pageState.pid
								var formGroups = $(form).find('.row')
								formGroups.each(function(index, item) {
									if (!index) {
										$(item).css({
											display: 'block'
										}).find('input').attr('disabled', false)
									} else {
										$(item).css({
											display: 'none'
										}).find('input').attr('disabled', 'disabled')
									}
								})
								$(form).validator('destroy')
								util.form.validator.init($(form))
								modal.find('.labelForAudit').css({
									display: 'none'
								})
								modal.find('.labelForAccept').css({
									display: 'none'
								})
								result.targetTypeStr = util.enum.transform('TARGETTYPE', result.targetType)
								result.accrualType = util.enum.transform('ACCRUALTYPE', result.accrualType)
								result.raiseScope = parseFloat(result.raiseScope) / 10000 + '万元'
								if (result.expAror) {
									result.expAror = result.expAror + '\t%'
								}
								if (result.collectIncomeRate) {
									result.collectIncomeRate = result.collectIncomeRate + '\t%'
								}
								if (result.applyVolume) {
									result.applyVolume = result.applyVolume + '\t万份'
								}
								if (result.applyCash) {
									result.applyCash = result.applyCash + '\t万元'
								}
								if (result.life) {
									result.life = result.life + '\t天'
								}
								if (result.floorVolume) {
									result.floorVolume = parseFloat(result.floorVolume) / 10000 + '\t万元'
								}
								$$.detailAutoFix(modal, result)
							})
							modal.modal('show')
						},
						'click .item-ordering': function(e, val, row) {
							var modal = $('#trustCheckModal')
							http.post(config.api.duration.order.getTrustOrderByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result
								var form = document.trustCheckForm
								form.oid.value = result.oid
								form.type.value = row.type
								form.opType.value = 'ordering'
								form.assetPoolOid.value = pageState.pid
								var formGroups = $(form).find('.form-group')
								formGroups.each(function(index, item) {
									if (index === 1) {
										$(item).css({
											display: 'block'
										}).find('input').attr('disabled', false)
									} else {
										$(item).css({
											display: 'none'
										}).find('input').attr('disabled', 'disabled')
									}
								})
								$(form).validator('destroy')
								util.form.validator.init($(form))
								modal.find('.labelForAudit').css({
									display: 'block'
								})
								modal.find('.labelForOrdering').css({
									display: 'none'
								})
								modal.find('.labelForAccept').css({
									display: 'none'
								})
								result.targetTypeStr = util.enum.transform('TARGETTYPE', result.targetType)
								result.accrualType = util.enum.transform('ACCRUALTYPE', result.accrualType)
								result.raiseScope = parseFloat(result.raiseScope) / 10000 + '万元'
								if (result.expAror) {
									result.expAror = result.expAror + '\t%'
								}
								if (result.collectIncomeRate) {
									result.collectIncomeRate = result.collectIncomeRate + '\t%'
								}
								if (result.applyVolume) {
									result.applyVolume = result.applyVolume + '\t万份'
								}
								if (result.applyCash) {
									result.applyCash = result.applyCash + '\t万元'
								}
								if (result.life) {
									result.life = result.life + '\t天'
								}
								if (result.floorVolume) {
									result.floorVolume = parseFloat(result.floorVolume) / 10000 + '\t万元'
								}
								if (result.auditVolume) {
									result.auditVolume = result.auditVolume + '\t万份'
								}
								if (result.auditCash) {
									result.auditCash = result.auditCash + '\t万元'
								}
								$$.detailAutoFix(modal, result)
							})
							modal.modal('show')
						},
						'click .item-trans-ordering': function(e, val, row) {
							var modal = $('#trustTransCheckModal')
							http.post(config.api.duration.order.getTrustOrderByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result
								var form = document.trustTransCheckForm
								form.oid.value = result.oid
								form.type.value = row.type
								form.opType.value = 'ordering'
								form.assetPoolOid.value = pageState.pid
								var formGroups = $(form).find('.row')
								formGroups.each(function(index, item) {
									if (index === 1) {
										$(item).css({
											display: 'block'
										}).find('input').attr('disabled', false)
									} else {
										$(item).css({
											display: 'none'
										}).find('input').attr('disabled', 'disabled')
									}
								})
								$(form).validator('destroy')
								util.form.validator.init($(form))
								modal.find('.labelForAudit').css({
									display: 'block'
								})
								modal.find('.labelForOrdering').css({
									display: 'none'
								})
								modal.find('.labelForAccept').css({
									display: 'none'
								})
								result.targetTypeStr = util.enum.transform('TARGETTYPE', result.targetType)
								result.accrualType = util.enum.transform('ACCRUALTYPE', result.accrualType)
								result.raiseScope = parseFloat(result.raiseScope) / 10000 + '万元'
								if (result.expAror) {
									result.expAror = result.expAror + '\t%'
								}
								if (result.applyVolume) {
									result.applyVolume = result.applyVolume + '\t万份'
								}
								if (result.applyCash) {
									result.applyCash = result.applyCash + '\t万元'
								}
								if (result.life) {
									result.life = result.life + '\t天'
								}
								if (result.floorVolume) {
									result.floorVolume = parseFloat(result.floorVolume) / 10000 + '\t万元'
								}
								if (result.auditVolume) {
									result.auditVolume = result.auditVolume + '\t万份'
								}
								if (result.auditCash) {
									result.auditCash = result.auditCash + '\t万元'
								}
								$$.detailAutoFix(modal, result)
							})
							modal.modal('show')
						},
						'click .item-accpet': function(e, val, row) {
							var modal = $('#trustCheckModal')
							http.post(config.api.duration.order.getTrustOrderByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result
								var form = document.trustCheckForm
								form.oid.value = result.oid
								form.type.value = row.type
								form.opType.value = 'accept'
								form.assetPoolOid.value = pageState.pid
								var formGroups = $(form).find('.form-group')
								formGroups.each(function(index, item) {
									if (index === 2) {
										$(item).css({
											display: 'block'
										}).find('input').attr('disabled', false)
									} else {
										$(item).css({
											display: 'none'
										}).find('input').attr('disabled', 'disabled')
									}
								})
								$(form).validator('destroy')
								util.form.validator.init($(form))
								modal.find('.labelForAudit').css({
									display: 'block'
								})
								modal.find('.labelForOrdering').css({
									display: 'block'
								})
								modal.find('.labelForAccept').css({
									display: 'none'
								})
								result.targetTypeStr = util.enum.transform('TARGETTYPE', result.targetType)
								result.accrualType = util.enum.transform('ACCRUALTYPE', result.accrualType)
								result.raiseScope = parseFloat(result.raiseScope) / 10000 + '万元'
								if (result.expAror) {
									result.expAror = result.expAror + '\t%'
								}
								if (result.collectIncomeRate) {
									result.collectIncomeRate = result.collectIncomeRate + '\t%'
								}
								if (result.applyVolume) {
									result.applyVolume = result.applyVolume + '\t万份'
								}
								if (result.applyCash) {
									result.applyCash = result.applyCash + '\t万元'
								}
								if (result.life) {
									result.life = result.life + '\t天'
								}
								if (result.floorVolume) {
									result.floorVolume = parseFloat(result.floorVolume) / 10000 + '\t万元'
								}
								if (result.auditVolume) {
									result.auditVolume = result.auditVolume + '\t万份'
								}
								if (result.auditCash) {
									result.auditCash = result.auditCash + '\t万元'
								}
								if (result.reserveVolume) {
									result.reserveVolume = result.reserveVolume + '\t万份'
								}
								if (result.reserveCash) {
									result.reserveCash = result.reserveCash + '\t万元'
								}
								$$.detailAutoFix(modal, result)
							})
							modal.modal('show')
						},
						'click .item-trans-accpet': function(e, val, row) {
							var modal = $('#trustTransCheckModal')
							http.post(config.api.duration.order.getTrustOrderByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result
								var form = document.trustTransCheckForm
								form.oid.value = result.oid
								form.type.value = row.type
								form.opType.value = 'accept'
								form.assetPoolOid.value = pageState.pid
								var formGroups = $(form).find('.row')
								formGroups.each(function(index, item) {
									if (index === 2) {
										$(item).css({
											display: 'block'
										}).find('input').attr('disabled', false)
									} else {
										$(item).css({
											display: 'none'
										}).find('input').attr('disabled', 'disabled')
									}
								})
								$(form).validator('destroy')
								util.form.validator.init($(form))
								modal.find('.labelForAudit').css({
									display: 'block'
								})
								modal.find('.labelForOrdering').css({
									display: 'block'
								})
								modal.find('.labelForAccept').css({
									display: 'none'
								})
								result.targetTypeStr = util.enum.transform('TARGETTYPE', result.targetType)
								result.accrualType = util.enum.transform('ACCRUALTYPE', result.accrualType)
								result.raiseScope = parseFloat(result.raiseScope) / 10000 + '万元'
								if (result.expAror) {
									result.expAror = result.expAror + '\t%'
								}
								if (result.applyVolume) {
									result.applyVolume = result.applyVolume + '\t万份'
								}
								if (result.applyCash) {
									result.applyCash = result.applyCash + '\t万元'
								}
								if (result.life) {
									result.life = result.life + '\t天'
								}
								if (result.floorVolume) {
									result.floorVolume = parseFloat(result.floorVolume) / 10000 + '\t万元'
								}
								if (result.auditVolume) {
									result.auditVolume = result.auditVolume + '\t万份'
								}
								if (result.auditCash) {
									result.auditCash = result.auditCash + '\t万元'
								}
								if (result.reserveVolume) {
									result.reserveVolume = result.reserveVolume + '\t万份'
								}
								if (result.reserveCash) {
									result.reserveCash = result.reserveCash + '\t万元'
								}
								$$.detailAutoFix(modal, result)
							})
							modal.modal('show')
						},
						'click .item-income-accpet': function(e, val, row) {
							var modal = $('#trustIncomeCheckModal')
							http.post(config.api.duration.order.getTrustOrderByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result
								var form = document.trustIncomeCheckForm
								form.oid.value = result.oid
								form.seq.value = result.seq
								form.type.value = row.type
								form.opType.value = 'accept'
								form.assetPoolOid.value = pageState.pid
								var formGroups = $(form).find('.row')
								formGroups.each(function(index, item) {
									if (index === 1) {
										$(item).css({
											display: 'block'
										}).find('input').attr('disabled', false)
									} else {
										$(item).css({
											display: 'none'
										}).find('input').attr('disabled', 'disabled')
									}
								})
								$(form).validator('destroy')
								util.form.validator.init($(form))
								modal.find('.labelForOrdering').css({
									display: 'block'
								})
								modal.find('.labelForAccept').css({
									display: 'none'
								})
								if (result.seq) {
									result.seq = result.seq + '\t期'
								}
								if (result.incomeRate) {
									result.incomeRate = result.incomeRate + '\t%'
								}
								if (result.income) {
									result.income = result.income + '\t万元'
								}
								if (result.capital) {
									result.capital = result.capital + '\t万元'
								}
//								if (result.applyVolume) {
//									result.applyVolume = result.applyVolume + '\t%'
//								}
//								if (result.applyCash) {
//									result.applyCash = result.applyCash + '\t万元'
//								}
								if (result.auditVolume) {
									result.auditVolume = result.auditVolume + '\t%'
								}
								if (result.auditCash) {
									result.auditCash = result.auditCash + '\t万元'
								}
								if (result.auditCapital) {
									result.auditCapital = result.auditCapital + '\t万元'
								}
								$$.detailAutoFix(modal, result)
							})
							modal.modal('show')
						},
						'click .item-transfer-accpet': function(e, val, row) {
							var modal = $('#transCheckModal')
							http.post(config.api.duration.order.getTrustOrderByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result
								var form = document.transCheckForm
								form.oid.value = result.oid
								form.type.value = row.type
								form.opType.value = 'accept'
								form.assetPoolOid.value = pageState.pid
								var formGroups = $(form).find('.row')
								formGroups.each(function(index, item) {
									if (index === 1) {
										$(item).css({
											display: 'block'
										}).find('input').attr('disabled', false)
									} else {
										$(item).css({
											display: 'none'
										}).find('input').attr('disabled', 'disabled')
									}
								})
								$(form).validator('destroy')
								util.form.validator.init($(form))
								modal.find('.labelForAudit').css({
									display: 'block'
								})
								modal.find('.labelForAccept').css({
									display: 'none'
								})
								result.targetTypeStr = util.enum.transform('TARGETTYPE', result.targetType)
								result.accrualType = util.enum.transform('ACCRUALTYPE', result.accrualType)
								result.raiseScope = parseFloat(result.raiseScope) / 10000 + '万元'
								if (result.expAror) {
									result.expAror = result.expAror + '\t%'
								}
								if (result.collectIncomeRate) {
									result.collectIncomeRate = result.collectIncomeRate + '\t%'
								}
								if (result.applyVolume) {
									result.applyVolume = result.applyVolume + '\t万份'
								}
								if (result.applyCash) {
									result.applyCash = result.applyCash + '\t万元'
								}
								if (result.life) {
									result.life = result.life + '\t天'
								}
								if (result.floorVolume) {
									result.floorVolume = parseFloat(result.floorVolume) / 10000 + '\t万元'
								}
								if (result.auditVolume) {
									result.auditVolume = result.auditVolume + '\t万份'
								}
								if (result.auditCash) {
									result.auditCash = result.auditCash + '\t万元'
								}
								$$.detailAutoFix(modal, result)
							})
							modal.modal('show')
						},
						'click .item-detail': function(e, val, row) {
							var modal = $('#trustOrderDetailModal')
							http.post(config.api.duration.order.getTrustOrderByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result
								modal.find('.labelForOrdering').css({
									display: 'block'
								})
								modal.find('.labelForAccept').css({
									display: 'block'
								})
								result.targetTypeStr = util.enum.transform('TARGETTYPE', result.targetType)
								result.accrualType = util.enum.transform('ACCRUALTYPE', result.accrualType)
								result.raiseScope = parseFloat(result.raiseScope) / 10000 + '万元'
								if (result.expAror) {
									result.expAror = result.expAror + '\t%'
								}
								if (result.collectIncomeRate) {
									result.collectIncomeRate = result.collectIncomeRate + '\t%'
								}
								if (result.applyCash) {
									result.applyCash = result.applyCash + '\t万元'
								}
								if (result.life) {
									result.life = result.life + '\t天'
								}
								if (result.floorVolume) {
									result.floorVolume = parseFloat(result.floorVolume) / 10000 + '\t万元'
								}
								if (result.auditCash) {
									result.auditCash = result.auditCash + '\t万元'
								}
								if (result.reserveCash) {
									result.reserveCash = result.reserveCash + '\t万元'
								}
								$$.detailAutoFix(modal, result)
							})
							modal.modal('show')
						},
						'click .item-trans-detail': function(e, val, row) {
							var modal = $('#trustTransOrderDetailModal')
							http.post(config.api.duration.order.getTrustOrderByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result
								modal.find('.labelForOrdering').css({
									display: 'block'
								})
								modal.find('.labelForAccept').css({
									display: 'block'
								})
								result.targetTypeStr = util.enum.transform('TARGETTYPE', result.targetType)
								result.accrualType = util.enum.transform('ACCRUALTYPE', result.accrualType)
								result.raiseScope = parseFloat(result.raiseScope) / 10000 + '万元'
								if (result.expAror) {
									result.expAror = result.expAror + '\t%'
								}
								if (result.applyVolume) {
									result.applyVolume = result.applyVolume + '\t万份'
								}
								if (result.applyCash) {
									result.applyCash = result.applyCash + '\t万元'
								}
								if (result.life) {
									result.life = result.life + '\t天'
								}
								if (result.floorVolume) {
									result.floorVolume = parseFloat(result.floorVolume) / 10000 + '\t万元'
								}
								if (result.auditVolume) {
									result.auditVolume = result.auditVolume + '\t万份'
								}
								if (result.auditCash) {
									result.auditCash = result.auditCash + '\t万元'
								}
								if (result.reserveVolume) {
									result.reserveVolume = result.reserveVolume + '\t万份'
								}
								if (result.reserveCash) {
									result.reserveCash = result.reserveCash + '\t万元'
								}
								if (result.investVolume) {
									result.investVolume = result.investVolume + '\t万份'
								}
								if (result.investCash) {
									result.investCash = result.investCash + '\t万元'
								}
								$$.detailAutoFix(modal, result)
							})
							modal.modal('show')
						},
						'click .item-income-detail': function(e, val, row) {
							var modal = $('#trustIncomeOrderDetailModal')
							http.post(config.api.duration.order.getTrustOrderByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result
								modal.find('.labelForOrdering').css({
									display: 'block'
								})
								modal.find('.labelForAccept').css({
									display: 'block'
								})
								if (result.seq) {
									result.seq = result.seq + '\t期'
								}
								if (result.incomeRate) {
									result.incomeRate = result.incomeRate + '\t%'
								}
								if (result.income) {
									result.income = result.income + '\t万元'
								}
								if (result.capital) {
									result.capital = result.capital + '\t万元'
								}
								if (result.auditVolume) {
									result.auditVolume = result.auditVolume + '\t%'
								}
								if (result.auditCash) {
									result.auditCash = result.auditCash + '\t万元'
								}
								if (result.auditCapital) {
									result.auditCapital = result.auditCapital + '\t万元'
								}
								if (result.investVolume) {
									result.investVolume = result.investVolume + '\t万份'
								}
								if (result.investCash) {
									result.investCash = result.investCash + '\t万元'
								}
								if (result.investCapital) {
									result.investCapital = result.investCapital + '\t万元'
								}
								$$.detailAutoFix(modal, result)
							})
							modal.modal('show')
						},
						'click .item-delete': function(e, val, row) {
							currentPool = row
							$$.confirm({
								container: $('#confirmModal'),
								trigger: this,
								accept: function () {
									http.post(config.api.duration.order.delete, {
										data: {
											oid: row.oid,
											operation: row.type
										},
										contentType: 'form'
									}, function () {
										$('#orderingTrustTable').bootstrapTable('refresh')
									})
								}
							})
						}
					}
				}]
			}
			// 预约中信托计划表格初始化
			$('#orderingTrustTable').bootstrapTable(orderingTrustTableConfig)

			// 信托计划审核表单初始化
			util.form.validator.init($('#trustCheckForm'))

			// 缓存本息兑付期数信息
			var seqs = []
			// 信托计划分页信息
			var trustPageOptions = {
				page: 1,
				rows: 10,
				pid: pageState.pid
			}
			// 信托计划表格配置
			var trustTableConfig = {
				ajax: function(origin) {
					http.post(config.api.duration.order.getTrustList, {
						data: trustPageOptions,
						contentType: 'form'
					}, function(rlt) {
						origin.success(rlt)
					})
				},
				pageNumber: trustPageOptions.page,
				pageSize: trustPageOptions.rows,
				pagination: true,
				sidePagination: 'server',
				pageList: [10, 20, 30, 50, 100],
				queryParams: function(val) {
					trustPageOptions.rows = val.limit
					trustPageOptions.page = parseInt(val.offset / val.limit) + 1
					return val
				},
				onLoadSuccess: function() {},
				columns: [{
					width: 60,
					align: 'center',
					formatter: function(val, row, index) {
						return index + 1
					}
				}, 
				{
					field: 'targetName'
				}, 
				{
					field: 'expAror'
				}, 
				{
					field: 'setDate'
				}, 
				{
					field: 'accrualType',
					formatter: function(val) {
						return util.enum.transform('ACCRUALTYPE', val)
					}
				}, 
				{
					field: 'raiseScope',
					formatter: function(val) {
						return parseInt(val) / 10000
					}
				}, 
				{
					field: 'holdAmount'
				}, 
				{
					field: 'tranVolume'
				}, 
				{
					field: 'subjectRating'
				}, 
				{
					field: 'profitType',
					formatter: function(val) {
						return 'amortized_cost' === val ? '摊余成本法' : '账面价值法'
					}
				}, 
				{
					field: 'state',
					formatter: function(val) {
						switch (val) {
							case 'PREPARE':
								return '<span class="text-aqua">初始化</span>'
							case 'STAND_UP':
								return '<span class="text-blue">成立</span>'
							case 'STAND_FAIL':
								return '<span class="text-blue">成立失败 </span>'
							case 'CLOSE':
								return '<span class="text-blue">结束</span>'
							case 'OVER_TIME':
								return '<span class="text-blue">逾期 </span>'
						}
						//            switch (val) {
						//              case '-1':
						//                return '<span class="text-aqua">未通过</span>'
						//              case '0':
						//                return '<span class="text-blue">成立</span>'
						//            }
					}
				}, 
				{
					width: 180,
					align: 'center',
					formatter: function(val, row) {
						var buttons = [{
							text: '本息兑付',
							type: 'button',
							class: 'item-income',
							// 只有已成立的标的才可以进行本息兑付
							isRender: row.state === 'STAND_UP'
						}, {
							text: '转让',
							type: 'button',
							class: 'item-transfer'
						}, {
							text: '纠偏',
							type: 'button',
							class: 'item-update'
						}]
						return util.table.formatter.generateButton(buttons)
					},
					events: {
						'click .item-income': function(e, val, row) {
							var form = document.trustIncomeForm
							$(form).validator('destroy')
							http.post(config.api.duration.order.getTrustByOid, {
								data: {
									oid: row.oid,
									type: 'income'
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result.incomeForm
//								seqs = json.result.incomeForm
//								var seq = $(form.seq).empty()
//								if (json.result.incomeForm.seq === 0) {
//									seq.append('<option value="' + result.seq + '">无可兑付信息</option>')
//									
//								    $('#incomeRate').attr("readonly","readonly")
//								    $('#income').attr("readonly","readonly")
//								    $('#incomeRadio').attr("disabled","true")
//								} else {
//									seq.append('<option value="' + result.seq + '">第' + result.seq + '期</option>')
//								}
								
								form.oid.value = json.result.oid
								form.assetPoolOid.value = json.result.assetPoolOid
								
//				                seqs.forEach(function (item) {
//				                  seq.append('<option value="' + item.seq + '">第' + item.seq + '期</option>')
//				                })
//								seq.change()
//								form.capital.value = result.capital
								$$.formAutoFix($('#trustIncomeForm'), result)
								// 信托计划本息兑付表单初始化
								util.form.validator.init($(form))
							})
							$('#trustIncomeModal').modal('show')
						},
						'click .item-transfer': function(e, val, row) {
							http.post(config.api.duration.order.getTrustByOid, {
								data: {
									oid: row.oid,
									type: 'transfer'
								},
								contentType: 'form'
							}, function(json) {
								var result = json.result
								var form = document.trustTransferForm
								form.oid.value = result.oid
								form.assetPoolOid.value = pageState.pid
								result.targetType = util.enum.transform('TARGETTYPE', result.targetType)
								result.accrualType = util.enum.transform('ACCRUALTYPE', result.accrualType)
								result.raiseScope = result.raiseScope + '万元'
								result.holdAmount = result.holdAmount + '万份'
								result.volume = result.volume + '万元'
								holdAmount = json.result.holdAmount
								$$.detailAutoFix($('#trustTransferModal'), result)
							})
							$('#trustTransferModal').modal('show')
						},
						'click .item-update': function(e, val, row) {
							http.post(config.api.duration.order.getTrustByOid, {
								data: {
									oid: row.oid,
									type: row.type
								},
								contentType: 'form'
							}, function(json) {
								json.result.holdAmount = json.result.holdAmount + '\t万元'
								document.updateTrustVolumeForm.oid.value = json.result.oid
								$$.detailAutoFix($('#updateTrustVolumeModal'), json.result)
								$('#updateTrustVolumeModal').modal('show')
							})
						}
					}
				}]
			}
			// 信托计划表格初始化
			$('#trustTable').bootstrapTable(trustTableConfig)

				// 信托计划转让表单初始化
			util.form.validator.init($('#trustTransferForm'))

			// 信托计划本息兑付表单下拉菜单初始化
			//    $(document.trustIncomeForm.seq).on('change', function () {
			//      var val = this.value
			//      seqs.forEach(function (item, index) {
			//        if (item.seq == val) {
			//          $$.formAutoFix($(document.trustIncomeForm), item)
			//          if (index === seqs.length - 1) {
			//            $('#capitalArea').show()
			//          } else {
			//            $('#capitalArea').hide()
			//          }
			//        }
			//      })
			//    })

			// 改变资产池后刷新页面
			//			$(document.searchForm.assetPoolName).on('change', function() {
			//				pageInit(pageState, http, config)
			//			})

			// 当选择本金兑付时，显示本金
			$(document.trustIncomeForm.capitalFlag).on('change', function () {
				if (this.value === 'yes') {
					$('#capitalArea').show()
				} else {
					$('#capitalArea').hide()
				}
			})

			// 当实际收益为0时，本金可编辑
			$(document.trustIncomeForm.income).on('focusout', function() {
				var val = this.value
				if (parseFloat(val) === 0) {
					$('#capitalInput').removeAttr('readonly')
				} else {
					$('#capitalInput').attr('readonly', 'readonly')
				}
			})

			// 申购金额验证
			$('#buyAssetForm').validator({
				custom: {
					validpurchaseamount: validpurchaseamount
				},
				errors: {
					validpurchaseamount: '申购金额不能为0，且不可超过可用现金'
				}
			})

			// 赎回金额验证
			$('#redeemForm').validator({
				custom: {
					validredeemamount: validredeemamount
				},
				errors: {
					validredeemamount: '赎回份额不能为0，且不可超过持有额度'
				}
			})

			// 转让金额验证
			$('#trustTransferForm').validator({
				custom: {
					validtransamount: validtransamount
				},
				errors: {
					validtransamount: '转让份额不能为0，且不可超过持有额度'
				}
			})

			// 本金不可超过申购时的金额
			$('#addAssetPoolForm').validator({
				custom: {
					validCapital: validCapital
				},
				errors: {
					validCapital: '本金不可超过申购时的金额'
				}
			})

			// 信托计划审核/预约/确认 - 通过按钮点击事件
			$('#doTrustCheck').on('click', function() {
				var form = document.trustCheckForm
				form.state.value = '0'
				var url = ''
				switch (form.type.value) {
					case '投资标的申购':
						switch (form.opType.value) {
							case 'audit':
								url = config.api.duration.order.auditForTrust
								break
							case 'ordering':
								url = config.api.duration.order.appointmentForTrust
								break
							default:
								url = config.api.duration.order.orderConfirmForTrust
								break
						}
						break
					case '投资标的转入':
						switch (form.opType.value) {
							case 'audit':
								url = config.api.duration.order.auditForTrust
								break
							case 'ordering':
								url = config.api.duration.order.appointmentForTrust
								break
							default:
								url = config.api.duration.order.orderConfirmForTrust
								break
						}
						break
					case '本息兑付':
						switch (form.opType.value) {
							case 'audit':
								url = config.api.duration.order.auditForIncome
								break
							default:
								url = config.api.duration.order.orderConfirmForIncome
								break
						}
						break
					default:
						switch (form.opType.value) {
							case 'audit':
								url = config.api.duration.order.auditForTransfer
								break
							default:
								url = config.api.duration.order.orderConfirmForTransfer
								break
						}
						break
				}
				if (!$(form).validator('doSubmitCheck')) return
				$(form).ajaxSubmit({
					url: url,
					success: function() {
						util.form.reset($(form))
						$('#orderingTrustTable').bootstrapTable('refresh')
						$('#trustTable').bootstrapTable('refresh')
						pageInit(pageState, http, config)
						$('#trustCheckModal').modal('hide')
					}
				})
			})
			
			// 信托计划审核/预约/确认 - 不通过按钮点击事件
			$('#doTrustUnCheck').on('click', function() {
				var form = document.trustCheckForm
				form.state.value = '-1'
				var url = ''
				switch (form.type.value) {
					case '投资标的申购':
						switch (form.opType.value) {
							case 'audit':
								url = config.api.duration.order.auditForTrust
								break
							case 'ordering':
								url = config.api.duration.order.appointmentForTrust
								break
							default:
								url = config.api.duration.order.orderConfirmForTrust
								break
						}
						break
					case '投资标的转入':
						switch (form.opType.value) {
							case 'audit':
								url = config.api.duration.order.auditForTrust
								break
							case 'ordering':
								url = config.api.duration.order.appointmentForTrust
								break
							default:
								url = config.api.duration.order.orderConfirmForTrust
								break
						}
						break
					case '本息兑付':
						switch (form.opType.value) {
							case 'audit':
								url = config.api.duration.order.auditForIncome
								break
							default:
								url = config.api.duration.order.orderConfirmForIncome
								break
						}
						break
					default:
						switch (form.opType.value) {
							case 'audit':
								url = config.api.duration.order.auditForTransfer
								break
							default:
								url = config.api.duration.order.orderConfirmForTransfer
								break
						}
						break
				}
				$(form).ajaxSubmit({
					url: url,
					success: function() {
						util.form.reset($(form))
						$('#orderingTrustTable').bootstrapTable('refresh')
						$('#trustTable').bootstrapTable('refresh')
						pageInit(pageState, http, config)
						$('#trustCheckModal').modal('hide')
					}
				})
			})

			// 信托计划转入审核/预约/确认 - 通过按钮点击事件
			$('#doTrustTransCheck').on('click', function() {
				var form = document.trustTransCheckForm
				form.state.value = '0'
				var url = ''
				switch (form.opType.value) {
					case 'audit':
						url = config.api.duration.order.auditForTrust
						break
					case 'ordering':
						url = config.api.duration.order.appointmentForTrust
						break
					default:
						url = config.api.duration.order.orderConfirmForTrust
						break
				}
				if (!$(form).validator('doSubmitCheck')) return
				$(form).ajaxSubmit({
					url: url,
					success: function() {
						util.form.reset($(form))
						$('#orderingTrustTable').bootstrapTable('refresh')
						$('#trustTable').bootstrapTable('refresh')
						pageInit(pageState, http, config)
						$('#trustTransCheckModal').modal('hide')
					}
				})
			})
			
			// 信托计划转入审核/预约/确认 - 不通过按钮点击事件
			$('#doTrustTransUnCheck').on('click', function() {
				var form = document.trustTransCheckForm
				form.state.value = '-1'
				var url = ''
				switch (form.opType.value) {
					case 'audit':
						url = config.api.duration.order.auditForTrust
						break
					case 'ordering':
						url = config.api.duration.order.appointmentForTrust
						break
					default:
						url = config.api.duration.order.orderConfirmForTrust
						break
				}
				$(form).ajaxSubmit({
					url: url,
					success: function() {
						util.form.reset($(form))
						$('#orderingTrustTable').bootstrapTable('refresh')
						$('#trustTable').bootstrapTable('refresh')
						pageInit(pageState, http, config)
						$('#trustTransCheckModal').modal('hide')
					}
				})
			})
			
			// 本息兑付 - 是否本金返还 - radio点击事件
			$('#incomeRadio').on('click', function() {
				var value = document.trustIncomeForm.capitalFlag.value
				if (value === '0') {
					$('#capitalArea').hide()
				} else {
					$('#capitalArea').show()
				}
			})

			// 本息兑付审核/预约/确认 - 通过按钮点击事件
			$('#doIncomeCheck').on('click', function() {
				var form = document.trustIncomeCheckForm
				form.state.value = '0'
				var url = ''
				switch (form.opType.value) {
					case 'audit':
						url = config.api.duration.order.auditForIncome
						break
					default:
						url = config.api.duration.order.orderConfirmForIncome
						break
				}
				if (!$(form).validator('doSubmitCheck')) return
				$(form).ajaxSubmit({
					url: url,
					success: function() {
						util.form.reset($(form))
						$('#orderingTrustTable').bootstrapTable('refresh')
						$('#trustTable').bootstrapTable('refresh')
						pageInit(pageState, http, config)
						$('#trustIncomeCheckModal').modal('hide')
					}
				})
			})
			
			// 本息兑付审核/预约/确认 - 不通过按钮点击事件
			$('#doIncomeUnCheck').on('click', function() {
				var form = document.trustIncomeCheckForm
				form.state.value = '-1'
				var url = ''
				switch (form.opType.value) {
					case 'audit':
						url = config.api.duration.order.auditForIncome
						break
					default:
						url = config.api.duration.order.orderConfirmForIncome
						break
				}
				$(form).ajaxSubmit({
					url: url,
					success: function() {
						util.form.reset($(form))
						$('#orderingTrustTable').bootstrapTable('refresh')
						$('#trustTable').bootstrapTable('refresh')
						pageInit(pageState, http, config)
						$('#trustIncomeCheckModal').modal('hide')
					}
				})
			})

			// 转让审核/预约/确认 - 通过按钮点击事件
			$('#doTransCheck').on('click', function() {
				var form = document.transCheckForm
				form.state.value = '0'
				var url = ''
				switch (form.opType.value) {
					case 'audit':
						url = config.api.duration.order.auditForTransfer
						break
					default:
						url = config.api.duration.order.orderConfirmForTransfer
						break
				}
				if (!$(form).validator('doSubmitCheck')) return
				$(form).ajaxSubmit({
					url: url,
					success: function() {
						util.form.reset($(form))
						$('#orderingTrustTable').bootstrapTable('refresh')
						$('#trustTable').bootstrapTable('refresh')
						pageInit(pageState, http, config)
						$('#transCheckModal').modal('hide')
					}
				})
			})
			
			// 转让审核/预约/确认 - 不通过按钮点击事件
			$('#doTransUnCheck').on('click', function() {
				var form = document.transCheckForm
				form.state.value = '-1'
				var url = ''
				switch (form.opType.value) {
					case 'audit':
						url = config.api.duration.order.auditForTransfer
						break
					default:
						url = config.api.duration.order.orderConfirmForTransfer
						break
				}
				$(form).ajaxSubmit({
					url: url,
					success: function() {
						util.form.reset($(form))
						$('#orderingTrustTable').bootstrapTable('refresh')
						$('#trustTable').bootstrapTable('refresh')
						pageInit(pageState, http, config)
						$('#transCheckModal').modal('hide')
					}
				})
			})

			// 信托计划 - 转让按钮点击事件
			$('#doTrustTransfer').on('click', function() {
				if (!$('#trustTransferForm').validator('doSubmitCheck')) return
				$('#trustTransferForm').ajaxSubmit({
					url: config.api.duration.order.applyForTransfer,
					success: function() {
						$('#orderingTrustTable').bootstrapTable('refresh')
						$('#trustTable').bootstrapTable('refresh')
						$('#trustTransferModal').modal('hide')
					}
				})
			})

			// 信托计划 - 本息兑付按钮点击事件
			$('#doTrustIncome').on('click', function() {
				if (!$('#trustIncomeForm').validator('doSubmitCheck')) return
				$('#trustIncomeForm').ajaxSubmit({
					url: config.api.duration.order.applyForIncome,
					success: function() {
						$('#orderingTrustTable').bootstrapTable('refresh')
						$('#trustTable').bootstrapTable('refresh')
						$('#trustIncomeModal').modal('hide')
					}
				})
			})

			// 修改资产池投资范围select2初始化
			$(document.updateAssetPoolForm.scopes).select2()
			// 修改资产池表单验证初始化
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
			
			// 修改资产池现金表单验证初始化
			$('#updateAssetPoolCashForm').validator({
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
			
			// 修改资产池偏离损益表单验证初始化
			$('#updateAssetPoolProfitForm').validator({
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
			
			// 纠偏现金管理工具表单验证初始化
			$('#updateFundVolumeForm').validator({
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
			
			// 纠偏信托标的表单验证初始化
			$('#updateTrustVolumeForm').validator({
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

			// 编辑账户按钮点击事件
			$('#updateAccount').on('click', function() {
				http.post(config.api.duration.assetPool.getById, {
					data: {
						oid: pageState.pid
					},
					contentType: 'form'
				}, function(json) {
					json.result.cashPosition = json.result.cashPosition + '\t万元'
					document.updateAssetPoolCashForm.oid.value = json.result.oid
					$$.detailAutoFix($('#updateAssetPoolCashModal'), json.result)
					$('#updateAssetPoolCashModal').modal('show')
				})
			})
			// 编辑资产池现金 - 确定按钮点击事件
			$('#doUpdateAssetPoolCash').on('click', function() {
				if (!$('#updateAssetPoolCashForm').validator('doSubmitCheck')) return
				$('#updateAssetPoolCashForm').ajaxSubmit({
					url: config.api.duration.assetPool.editPoolForCash,
					success: function() {
						pageInit(pageState, http, config)
						$('#updateAssetPoolCashModal').modal('hide')
					}
				})
			})
			
			// 编辑偏离损益按钮点击事件
			$('#updateProfit').on('click', function() {
				http.post(config.api.duration.assetPool.getById, {
					data: {
						oid: pageState.pid
					},
					contentType: 'form'
				}, function(json) {
					json.result.deviationValue = json.result.deviationValue + '\t万元'
					document.updateAssetPoolProfitForm.oid.value = json.result.oid
					$$.detailAutoFix($('#updateAssetPoolProfitModal'), json.result)
					$('#updateAssetPoolProfitModal').modal('show')
				})
			})
			// 编辑资产池偏离损益 - 确定按钮点击事件
			$('#doUpdateAssetPoolProfit').on('click', function() {
				if (!$('#updateAssetPoolProfitForm').validator('doSubmitCheck')) return
				$('#updateAssetPoolProfitForm').ajaxSubmit({
					url: config.api.duration.assetPool.updateDeviationValue,
					success: function() {
						pageInit(pageState, http, config)
						$('#updateAssetPoolProfitModal').modal('hide')
					}
				})
			})
			
			// 修改现金管理工具偏离损益 - 确定按钮点击事件
			$('#doUpdateFundVolume').on('click', function() {
				if (!$('#updateFundVolumeForm').validator('doSubmitCheck')) return
				$('#updateFundVolumeForm').ajaxSubmit({
					url: config.api.duration.order.updateFund,
					success: function() {
						$('#orderingToolTable').bootstrapTable('refresh')
						$('#toolTable').bootstrapTable('refresh')
						pageInit(pageState, http, config)
						$('#updateFundVolumeModal').modal('hide')
					}
				})
			})
			
			// 编辑资产池偏离损益 - 确定按钮点击事件
			$('#doUpdateTrustVolume').on('click', function() {
				if (!$('#updateTrustVolumeForm').validator('doSubmitCheck')) return
				$('#updateTrustVolumeForm').ajaxSubmit({
					url: config.api.duration.order.updateTrust,
					success: function() {
						$('#orderingTrustTable').bootstrapTable('refresh')
						$('#trustTable').bootstrapTable('refresh')
						pageInit(pageState, http, config)
						$('#updateTrustVolumeModal').modal('hide')
					}
				})
			})
		}
	}
})

function getBarOptions(config, source) {
	return {
		tooltip: {
			trigger: 'axis'
		},
		legend: {
			orient: 'vertical',
			x: 'left',
			data: ['可用现金', '冻结资金', '在途资金'],
		},
		grid: {
			top: 10,
			left: 110,
			right: 30,
			bottom: 30
		},
		xAxis: [{
			type: 'value',
			boundaryGap: ['0%', '20%'],
		}],
		yAxis: [{
			name: '',
			type: 'category',
			boundaryGap: true,
			data: []
		}],
		series: [{
			name: '可用现金',
			type: 'bar',
			label: {
				normal: {
					show: true,
					position: 'right',
					formatter: function(obj) {
						return obj.value + '万'
					}
				}
			},
			data: [source.cashPosition]
		}, 
		{
			name: '冻结资金',
			type: 'bar',
			label: {
				normal: {
					show: true,
					position: 'right',
					formatter: function(obj) {
						return obj.value + '万'
					}
				}
			},
			data: [source.freezeCash]
		}, 
		{
			name: '在途资金',
			type: 'bar',
			showAllSymbol: true,
			label: {
				normal: {
					show: true,
					position: 'right',
					formatter: function(obj) {
						return obj.value + '万'
					}
				}
			},
			data: [source.transitCash]
		}],
		color: config.colors
	}
}

function getPieOptions(config, source) {
	return {
		tooltip: {
			trigger: 'item',
			formatter: "{a} <br/>{b}: {c} ({d}%)"
		},
		legend: {
			orient: 'vertical',
			x: 'left',
			data: ['现金', '现金类管理工具', '信托计划']
		},
		series: [{
			name: '投资占比',
			type: 'pie',
			//radius: ['50%', '70%'],
			radius: '75%',
			center: ['55%', '50%'],
			//avoidLabelOverlap: false,
			//label: {
			//  normal: {
			//    show: false,
			//    position: 'center'
			//  },
			//  emphasis: {
			//    show: true,
			//    textStyle: {
			//      fontSize: '18',
			//      fontWeight: 'bold'
			//    }
			//  }
			//},
			//labelLine: {
			//  normal: {
			//    show: false
			//  }
			//},
			data: [
				// {value:source.cashRate, name:'现金'},
				// {value:source.cashtoolRate, name:'现金类管理工具'},
				// {value:source.targetRate, name:'信托计划'}
				{
					value: source.cashFactRate,
					name: '现金'
				}, {
					value: source.cashtoolFactRate,
					name: '现金类管理工具'
				}, {
					value: source.targetFactRate,
					name: '信托计划'
				}
			]
		}],
		color: config.colors
	}
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

function pageInit (pageState, http, config) {
	http.post(config.api.duration.assetPool.getById, {
		data: {
			oid: pageState.pid || ''
		},
		contentType: 'form'
	}, function(json) {
		var detail = json.result
		freeCash = detail.cashPosition
		pageState.pid = document.searchForm.assetPoolName.value =  detail.oid
		$('#detailPoolScale').html(detail.scale)
		$('#detailPoolCash').html(detail.cashPosition)
		$('#detailPoolProfit').html(detail.deviationValue)
			// 饼图生成
		var pieChart = echarts.init(document.getElementById('pieChart'))
		pieChart.setOption(getPieOptions(config, detail))
			// 柱状图生成
		var barChart = echarts.init(document.getElementById('barChart'))
		barChart.setOption(getBarOptions(config, detail))
	})
}

function validCapital($el) {
	
}

// 申购额度验证
function validpurchaseamount($el) {
	var amount = $el.val()
	return parseFloat(amount) > 0 && parseFloat(amount) <= freeCash
}

// 赎回额度验证
function validredeemamount($el) {
    var form = $el.closest('form')
    var holdAmount = form.find('input[name=amount]')
	var amount = $el.val()
	return parseFloat(amount) > 0 && parseFloat(amount) <= parseFloat(holdAmount.val())
}

// 转让额度验证
function validtransamount($el) {
	var tranVolume = $el.val()
	return parseFloat(tranvolume) > 0 && parseFloat(tranvolume) <= parseFloat(holdAmount)
}