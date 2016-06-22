/**
 * 产品明细
 */
define([
	'http',
	'config',
	'util',
	'extension'
], function(http, config, util, $$) {
	return {
		name: 'productDetail',
		init: function() {

			var pageState = {
				productOid: util.nav.getHashObj(location.hash).id || '',
				detail: null //当前产品明细数据
			}

			// 资产池切换列表
			http.post(config.api.productNameList, function(json) {
				var productNameOptions = ''
				var select = document.searchForm.productName
				json.rows.forEach(function(item) {
					productNameOptions += '<option value="' + item.oid + '">' + item.name + '</option>'
				})
				$(select).html(productNameOptions)
				pageInit(pageState, http, config)
			})

			// 改变资产池后刷新页面
			$(document.searchForm.productName).on('change', function() {
				pageState.productOid = this.value
				pageInit(pageState, http, config)
				$('#aTable').bootstrapTable('refresh')
				$('#bTable').bootstrapTable('refresh')

			})

			function pageInit(pageState, http, config) {
				http.post(config.api.getProductByOid, {
					data: {
						oid: pageState.productOid || ''
					},
					contentType: 'form'
				}, function(result) {
					var detail = pageState.detail = result
					pageState.productOid = document.searchForm.productName.value = detail.oid

					if (detail.isOpenPurchase == 'YES') { //关闭申购申请
						$('#openPurchase').hide()
						$('#closePurchase').show()
					} else { //开启申购申请
						$('#openPurchase').show()
						$('#closePurchase').hide()
					}

					if (detail.isOpenRemeed == 'YES') { //关闭赎回申请
						$('#openRemeed').hide()
						$('#closeRemeed').show()
					} else { //开启赎回申请
						$('#openRemeed').show()
						$('#closeRemeed').hide()
					}
				})
			}

			//开启申购申请
			$('#openPurchase').on('click', function() {
				$("#confirmTitle").html("产品名称:"+detail.fullName)
				$("#confirmTitle1").html("确定开启申购申请吗？")
				$$.confirm({
					container: $('#doConfirm'),
					trigger: this,
					accept: function() {
//						http.post(config.api.productInvalid, {
//							data: {
//								oid: row.oid
//							},
//							contentType: 'form',
//						}, function(result) {
//							$('#aTable').bootstrapTable('refresh')
//						})
					}
				})
			})
			//关闭申购申请
			$('#closePurchase').on('click', function() {
				$("#confirmTitle").html("产品名称:"+detail.fullName)
				$("#confirmTitle1").html("确定关闭申购申请吗？")
				$$.confirm({
					container: $('#doConfirm'),
					trigger: this,
					accept: function() {
//						http.post(config.api.productInvalid, {
//							data: {
//								oid: row.oid
//							},
//							contentType: 'form',
//						}, function(result) {
//							$('#aTable').bootstrapTable('refresh')
//						})
					}
				})
			})
			//开启赎回申请
			$('#openRemeed').on('click', function() {
				$("#confirmTitle").html("产品名称:"+detail.fullName)
				$("#confirmTitle1").html("确定开启赎回申请吗？")
				$$.confirm({
					container: $('#doConfirm'),
					trigger: this,
					accept: function() {
//						http.post(config.api.productInvalid, {
//							data: {
//								oid: row.oid
//							},
//							contentType: 'form',
//						}, function(result) {
//							$('#bTable').bootstrapTable('refresh')
//						})
					}
				})
			})
			//关闭赎回申请
			$('#closeRemeed').on('click', function() {
				$("#confirmTitle").html("产品名称:"+detail.fullName)
				$("#confirmTitle1").html("确定关闭赎回申请吗？")
				$$.confirm({
					container: $('#doConfirm'),
					trigger: this,
					accept: function() {
//						http.post(config.api.productInvalid, {
//							data: {
//								oid: row.oid
//							},
//							contentType: 'form',
//						}, function(result) {
//							$('#bTable').bootstrapTable('refresh')
//						})
					}
				})
			})
			
			//可售份额申请
			$('#availbleSaleVolume').on('click', function() {
				
			})
			//发行渠道申请
			$('#channelApply').on('click', function() {
				
			})

		}
	}
})