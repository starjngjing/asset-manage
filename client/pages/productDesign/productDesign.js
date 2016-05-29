/**
 * 产品申请管理
 */
define([
'http',
'config',
'util',
'extension'
], function (http, config, util, $$) {
	return {
    name: 'productDesign',
    init: function () {
    	// js逻辑写在这里
    	// 数据表格分页、搜索条件配置
    	var pageOptions = {
				number: 1,
				size: 10,
				name: '',
				type: ''
			}
		// 用于存储表格checkbox选中的项
		var checkItems = []
		// 用于存储选择的渠道checkbox选中的项
		var checkChannels = []
		var selectProductOid = ""
    	// 数据表格配置
    	var tableConfig = {
    		ajax: function (origin) {
    			http.post(
    				config.api.productApplyList, 
    				{
    					data: {
    						page: pageOptions.number,
    						rows: pageOptions.size,
    						name: pageOptions.name,
    						type: pageOptions.type
    					},
    					contentType: 'form'
    				},
    				function (rlt) {
    					origin.success(rlt)
    				}
    			)
    		},
    		pageNumber: pageOptions.number,
    		pageSize: pageOptions.size,
    		pagination: true,
    		sidePagination: 'server',
    		pageList: [10, 20, 30, 50, 100],
    		queryParams: getQueryParams,
    		onLoadSuccess: function () {},
    		columns: [
    			{
    				checkbox: true,
    				align: 'center'
    			},
    			{
					field: 'code',
					align: 'center'
				},
				{
					field: 'name',
					align: 'center'
				},
     			{
					field: 'typeName',
					align: 'center'
				},
				{
					field: 'administrator',
					align: 'center'
				},
				{
					align: 'center',
					field: 'status',
					formatter: function (val) {
						switch (val) {
							case 'CREATE': return '新建'
							case 'UPDATE': return '修改'
							case 'AUDITING': return '审核中'
							case 'AUDITFAIL': return '审核不通过'
							case 'AUDITPASS': return '审核通过'
							case 'REVIEWFAIL': return '复核不通过'
							case 'REVIEWPASS': return '复核通过'
							case 'ADMITFAIL': return '准入不通过'
							case 'ADMITPASS': return '准入通过'
							case 'NOTSTARTRAISE': return '未开始募集'
							case 'RAISING': return '募集中(募集期)'
							case 'NOESTALISHING': return '成立未开始'
							case 'ESTALISHING': return '已成立'
							default : return '-'
						}
					}
				},
				{
					align: 'center',
					field: 'durationPeriod',
					formatter: function (val, row, index) {
						var typeOid = row.typeOid;  
						if(typeOid=="PRODUCTTYPE_01") {
							return row.durationPeriod+"天";
						} else {
							return "不固定";
						}
					}
				},
				{
					field: 'expAror',
					align: 'center',
					formatter: function (val, row, index) {
						if(row.expArorSec!=null && row.expAror!=row.expArorSec) {
							return row.expAror+"%"+"~"+row.expArorSec+"%";
						}
						return row.expAror+"%";
					}
				},
				{
					align: 'center',
					field: 'raisedTotalNumber',
					formatter: function (val, row, index) {
						var typeOid = row.typeOid;  
						if(typeOid=="PRODUCTTYPE_01") {
							return row.raisedTotalNumber;
						} else {
							return "不限";
						}
					}
				},
				{
					field: 'netUnitShare',
					align: 'center'
				},
				{
					align: 'center',
					field: 'channelNum'
				},
				{
					field: 'assetPoolName',
					align: 'center'
				},
				{
					field: 'createTime',
					align: 'center',
					formatter: function (val) {
						return util.table.formatter.timestampToDate(val, 'YYYY-MM-DD HH:mm:ss')
					}
				},
				{
					width: 256,
					align: 'center',
					formatter: function(val, row) {
						var buttons = [
		           	    	{
		           	      		text: '详情',
		           	      		type: 'button',
		           	      		class: 'item-detail',
		           	      		isRender: true
		           	    	},
		           	    	{
		           	    		text: '编辑',
		           	    		type: 'button',
		           	    		class: 'item-update',
		           	    		isRender: true
		           	    	},
		           	    	{
		          	      		text: '作废',
		           	      		type: 'button',
		           	      		class: 'item-invalid',
		           	      		isRender: true
		           	    	},
		           	    	{
		           	    		text: '选择渠道',
		           	    		type: 'button',
		           	    		class: 'item-channel',
		           	    		isRender: true
		           	    	}
		           	    ];
		           	  	return util.table.formatter.generateButton(buttons);
		           	},
		           	events: {
						'click .item-detail': function(e, value, row) {
							http.post(config.api.productDetail, {
								data: {
									oid: row.oid
								},
								contentType: 'form'
							}, function(result) {
								if (result.errorCode == 0) {
									var data = result;
									
									switch (data.typeOid) {
										case 'PRODUCTTYPE_01':
											$('#detailProductType01Area').show()
											$('#detailProductType02Area').hide()
											break
										case 'PRODUCTTYPE_02':
											$('#detailProductType02Area').show()
											$('#detailProductType01Area').hide()
											break
									}
									
									var productDetailFiles = []
									if(data.files!=null && data.files.length>0) {
										for(var i=0;i<data.files.length;i++){
											productDetailFiles.push(data.files[i])
										}
									}
									$('#productDetailFileTable').bootstrapTable('load', productDetailFiles)
									
									$$.detailAutoFix($('#productDetailModal'), data); // 自动填充详情
									$('#productDetailModal').modal('show');
								} else {
									alert(查询失败);
								}
							})
						},
						'click .item-update': function(e, value, row) {
							http.post(config.api.productDetail, {
								data: {
									oid: row.oid
								},
								contentType: 'form'
							}, function(result) {
								if (result.errorCode == 0) {
									var data = result;
									$(document.updateProductForm.name).attr("data-fetch-id",data.oid)
									$(document.updateProductForm.fullName).attr("data-fetch-id",data.oid)
									$(document.updateProductForm.code).attr("data-fetch-id",data.oid)
									$$.formAutoFix($('#updateProductForm'), data); // 自动填充表单
									$('#updateProductForm').validator('validate')
									
									if ('PRODUCTTYPE_01'==data.typeOid) {
										if('MANUALINPUT'==data.raiseStartDateType) {
											$('#raiseStartDateType').removeClass('col-sm-4').addClass('col-sm-2')
											$('#raiseStartDateType').next('.col-sm-2').show()
										} else if('FIRSTRACKTIME'==data.raiseStartDateType) {
											$('#raiseStartDateType').removeClass('col-sm-2').addClass('col-sm-4')
											$('#raiseStartDateType').next('.col-sm-2').hide()
										}
										$('#updateProductType01Area').show()
										$('#updateProductType02Area').hide()
									} else if ('PRODUCTTYPE_02'==data.typeOid) {
										if('MANUALINPUT'==data.setupDateType) {
											$('#setupDateType').removeClass('col-sm-4').addClass('col-sm-2')
											$('#setupDateType').next('.col-sm-2').show()
										} else if('FIRSTRACKTIME'==data.setupDateType) {
											$('#setupDateType').removeClass('col-sm-2').addClass('col-sm-4')
											$('#setupDateType').next('.col-sm-2').hide()
										}
										if('DAY'==data.payModeOid) {
											$('#payModeOid').removeClass('col-sm-2').addClass('col-sm-4')
											$('#payModeOid').next('.col-sm-2').hide()
										} else {
											$('#payModeOid').removeClass('col-sm-4').addClass('col-sm-2')
											$('#payModeOid').next('.col-sm-2').show()
										}
										$('#updateProductType02Area').show()
										$('#updateProductType01Area').hide()
									}
									
									http.post(config.api.duration.assetPool.getNameList, function (result) {
										var select = document.updateProductForm.assetPoolOid
										$(select).empty()
										result.rows.forEach(function (item, index) {
											$(select).append('<option value="' + item.oid + '">' + item.name + '</option>')
										})
										select.value = data.assetPoolOid
									})
									
									updateProductUploadFiles = []
									if(data.files!=null && data.files.length>0) {
										for(var i=0;i<data.files.length;i++){
											updateProductUploadFiles.push(data.files[i])
										}
									}
									$('#updateProductUploadTable').bootstrapTable('load', updateProductUploadFiles)
									
									$('#updateProductModal').modal('show');
								} else {
									alert(查询失败);
								}
							})
						},
						'click .item-invalid': function(e, value, row) {
							$("#confirmTitle").html("确定作废产品名称为:")
							$("#confirmTitle1").html(row.fullName+"的产品吗？")
							$$.confirm({
								container: $('#doConfirm'),
								trigger: this,
								accept: function() {
									http.post(config.api.productInvalid, {
										data: {
											oid: row.oid
										},
										contentType: 'form',
									}, function(result) {
										$('#productDesignTable').bootstrapTable('refresh')
									})
								}
							})
						},
						'click .item-channel': function(e, value, row) {
							selectProductOid = row.oid
							http.post(
    							config.api.productChooseChannelList, 
    							{
 		   							data: {
    									productOid:row.oid
    								},
    								contentType: 'form'
    							},
    							function(result) {
									if (result.errorCode == 0) {
										var data = result.rows;
										$('#productChooseChannelTable').bootstrapTable('load', data)
										$('#channelModal').modal('show');
									} else {
										alert(查询失败);
									}
								}
    						)
						}
					}
				},
			],
			// 单选按钮选中一项时
			onCheck: function (row) {
				if (checkItems.indexOf(row) < 0){
					checkItems.push(row)
				}
			},
			// 单选按钮取消一项时
			onUncheck: function (row) {
				checkItems.splice(checkItems.indexOf(row), 1)
			},
			// 全选按钮选中时
			onCheckAll: function (rows) {
				checkItems = rows.map(function (item) {
					return item
				})
			},
			// 全选按钮取消时
			onUncheckAll: function () {
				checkItems = []
			}
		}
    	// 数据表格初始化
    	$('#productDesignTable').bootstrapTable(tableConfig)
    	// 搜索表单初始化
    	$$.searchInit($('#searchForm'), $('#productDesignTable'))
    	util.form.validator.init($('#addProductForm'))
    	util.form.validator.init($('#updateProductForm'))
    	
    	// 数据表格配置
		var channelsTableConfig = {
			columns: [
    			{
    				checkbox: true,
    				field: 'selectStatus',
    				align: 'center',
    				formatter: function (val, row, index) {
					    var selectStatus = row.selectStatus
					    if(true==selectStatus) {
					        if (checkChannels.indexOf(row) < 0){
			            		checkChannels.push(row)
					        }
					    }
					    return selectStatus
					}
    			},
    			{
    				field: 'channelCode',
    				align: 'center'
				},
				{
			   		field: 'channelName',
			   		align: 'center'
				},
				{
					align: 'center',
            		field: 'channelStatus',
					formatter: function (val, row, index) {
						var channelStatus = row.channelStatus
						if("on"==channelStatus) {
						    return "已启用"
						} else if("off"==channelStatus) {
						    return "已停用"
						}
					}
				},
				{
					field: 'channelFee',
					align: 'center',
					formatter: function (val, row, index) {
					    var channelFee = row.channelFee
					    if(channelFee!=null && channelFee!="") {
					        return channelFee+"%"
					    }
						return "";
					}
				}
			],
			// 单选按钮选中一项时
			onCheck: function (row) {
			    if (checkChannels.indexOf(row) < 0){
			        checkChannels.push(row)
			    }
			},
			// 单选按钮取消一项时
			onUncheck: function (row) {
			    checkChannels.splice(checkChannels.indexOf(row), 1)
			},
			// 全选按钮选中时
			onCheckAll: function (rows) {
			    checkChannels = rows.map(function (item) {
				    return item
				})
			},
			// 全选按钮取消时
			onUncheckAll: function () {
				checkChannels = []
			}
		}
				            		
		// 数据表格初始化
    	$('#productChooseChannelTable').bootstrapTable(channelsTableConfig)
    	
    	// 详情附件表格配置
    	var productDetailFileTableConfig = {
			columns: [
				{
					field: 'name',
				},
				{
					field: 'operator',
				},
				{
					field: 'createTime',
				},
				{
					width: 100,
					align: 'center',
					formatter: function() {
						var buttons = [{
							text: '下载',
							type: 'button',
							class: 'item-download'
						}]
						return util.table.formatter.generateButton(buttons)
					},
					events: {
						'click .item-download': function(e, value, row) {
							location.href = 'http://api.guohuaigroup.com' + row.furl
						}
					}
				}
			]
		}
    	// 详情附件表格初始化
		$('#productDetailFileTable').bootstrapTable(productDetailFileTableConfig)

		// 产品类型下拉菜单关联区域显隐
		// input disabled 设置为 disabled的时候将不做验证
		$('#addProductTypeSelect').on('change', function () {
			switch (this.value) {
				case 'PRODUCTTYPE_01':
					$('#addProductType01Area').show().find('input').attr('disabled', false)
					$('#addProductType02Area').hide().find('input').attr('disabled', 'disabled')
					break
				case 'PRODUCTTYPE_02':
					$('#addProductType02Area').show().find('input').attr('disabled', false)
					$('#addProductType01Area').hide().find('input').attr('disabled', 'disabled')
					break
			}
			// 重置验证逻辑
			$('#addProductForm').validator('destroy')
			util.form.validator.init($('#addProductForm'))
		})
			
		$('#updateProductTypeSelect').on('change', function () {
			switch (this.value) {
				case 'PRODUCTTYPE_01':
					$('#updateProductType01Area').show().find('input').attr('disabled', false)
					$('#updateProductType02Area').hide().find('input').attr('disabled', 'disabled')
					break
				case 'PRODUCTTYPE_02':
					$('#updateProductType02Area').show().find('input').attr('disabled', false)
					$('#updateProductType01Area').hide().find('input').attr('disabled', 'disabled')
					break
			}
			// 重置验证逻辑
			$('#updateProductForm').validator('destroy')
			util.form.validator.init($('#updateProductForm'))
		})

		// 募集开始时间&成立时间select联动 
		$('select[name=raiseStartDateType],select[name=setupDateType]').on('change', function () {
			var col = $(this).parent().parent()
			switch (this.value) {
				case 'FIRSTRACKTIME':
					col.removeClass('col-sm-2').addClass('col-sm-4')
					col.next('.col-sm-2').hide()
					break
				case 'MANUALINPUT':
					col.removeClass('col-sm-4').addClass('col-sm-2')
					col.next('.col-sm-2').show()
					break
			}
		}).change()
		
		//  付息方式select联动
		$('select[name=payModeOid]').on('change', function () {
			var col = $(this).parent().parent()
			switch (this.value) {
				case 'DAY':
					col.removeClass('col-sm-2').addClass('col-sm-4')
					col.next('.col-sm-2').hide()
					break
				case 'WEEK':
					col.removeClass('col-sm-4').addClass('col-sm-2')
					col.next('.col-sm-2').show()
					break
				case 'MONTH':
					col.removeClass('col-sm-4').addClass('col-sm-2')
					col.next('.col-sm-2').show()
					break
			}
		}).change()
		

		// 确认日input后缀按钮联动
		$('.select-button').find('li a').on('click', function () {
			var ul = $(this).parent().parent()
			ul.prev('button').html(this.innerText + ' <span class="fa fa-caret-down"></span>')
			ul.next('input').val($(this).attr('value'))
		})

			util.form.validator.init($('#addProductForm'))

    	// 新建产品按钮点击事件
    	$('#productAdd').on('click', function () {
			http.post(config.api.duration.assetPool.getNameList, function (result) {
				var select = document.addProductForm.assetPoolOid
				$(select).empty()
				result.rows.forEach(function (item, index) {
					$(select).append('<option value="' + item.oid + '" ' + (!index ? 'checked' : '') + '>' + item.name + '</option>')
				})
			})
			
			// 新建产品上传附件表格数据源
			addProductUploadFiles = []
			$('#addProductUploadTable').bootstrapTable('load', addProductUploadFiles)
		
			util.form.reset($('#addProductForm'))
			$('#addProductModal').modal('show')	
			$('#addProductModal').modal('show')
    	})    	
    	// 表格querystring扩展函数，会在表格每次数据加载时触发，用于自定义querystring
    	function getQueryParams (val) {
    		var form = document.searchForm
    		pageOptions.size = val.limit
    		pageOptions.number = parseInt(val.offset / val.limit) + 1
    		pageOptions.name = form.name.value.trim()
    		pageOptions.type = form.type.value.trim()
    		return val
  		}

		// 新建产品上传附件表格数据源
		var addProductUploadFiles = []
		// 新建产品初始化上传附件插件，在success里将上传成功附件插入到表格中
		$$.uploader({
			container: $('#addProductUploader'),
			success: function(file) {
				file.furl = file.url
				addProductUploadFiles.push(file)
				$('#addProductUploadTable').bootstrapTable('load', addProductUploadFiles)
			}
		})
		// 新建产品附件表格配置
		var addProductUploadTableConfig = {
			columns: [
				{
					field: 'name',
				},
				{
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
							location.href = 'http://api.guohuaigroup.com' + row.url
						},
						'click .item-delete': function(e, value, row) {
							var index = addProductUploadFiles.indexOf(row)
							addProductUploadFiles.splice(index, 1)
							$('#addProductUploadTable').bootstrapTable('load', addProductUploadFiles)
						}
					}
				}
			]
		}
		// 新建产品附件表格初始化
		$('#addProductUploadTable').bootstrapTable(addProductUploadTableConfig)
			
		// 新建产品“保存”按钮点击事件
    	$('#addProductSubmit').on('click', function () {
    		var typeOid = $("#addProductTypeSelect  option:selected").val();
			document.addProductForm.files.value = JSON.stringify(addProductUploadFiles)
			if(document.addProductForm.expArorSec.value=="") {
				document.addProductForm.expArorSec.value = document.addProductForm.expAror.value
			}
    		if(typeOid=="PRODUCTTYPE_01") {
    			$('#addProductForm').ajaxSubmit({
      			url: config.api.savePeriodic,
      			success: function (addResult) {
      				util.form.reset($('#addProductForm'))
        			$('#addProductModal').modal('hide')
        			$('#productDesignTable').bootstrapTable('refresh')
      			}
    		})
    		} else {
    			$('#addProductForm').ajaxSubmit({
      			url: config.api.saveCurrent,
      			success: function (addResult) {
      				util.form.reset($('#addProductForm'))
        			$('#addProductModal').modal('hide')
        			$('#productDesignTable').bootstrapTable('refresh')
      			}
    			})
    		}
  		})

		// 编辑产品上传附件表格数据源
		var updateProductUploadFiles = []
		// 编辑产品初始化上传附件插件，在success里将上传成功附件插入到表格中
		$$.uploader({
			container: $('#updateProductUploader'),
			success: function(file) {
				file.furl = file.url
				updateProductUploadFiles.push(file)
				$('#updateProductUploadTable').bootstrapTable('load', updateProductUploadFiles)
			}
		})
		// 编辑产品附件表格配置
		var updateProductUploadTableConfig = {
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
						location.href = 'http://api.guohuaigroup.com' + row.url
					},
					'click .item-delete': function(e, value, row) {
						var index = updateProductUploadFiles.indexOf(row)
						updateProductUploadFiles.splice(index, 1)
						$('#updateProductUploadTable').bootstrapTable('load', updateProductUploadFiles)
					}
				}
			}]
		}
		// 编辑产品附件表格初始化
		$('#updateProductUploadTable').bootstrapTable(updateProductUploadTableConfig)
		// 编辑产品“保存”按钮点击事件
    	$('#updateProductSubmit').on('click', function () {
    		var typeOid = $("#typeOid").val();
				document.updateProductForm.files.value = JSON.stringify(updateProductUploadFiles)
    		if(typeOid=="PRODUCTTYPE_01") {
    			$('#updateProductForm').ajaxSubmit({
      			url: config.api.updatePeriodic,
      			success: function (addResult) {
        			$('#updateProductModal').modal('hide')
        			$('#productDesignTable').bootstrapTable('refresh')
      			}
    		})
    		} else {
    			$('#updateProductForm').ajaxSubmit({
      			url: config.api.updateCurrent,
      			success: function (addResult) {
        			$('#updateProductModal').modal('hide')
        			$('#productDesignTable').bootstrapTable('refresh')
      			}
    			})
    		}
  		})


		// 提交审核按钮点击事件
		$('#productAudit').on('click', function () {
			if(checkItems.length>0) {
				var productNames = checkItems.map(function (item) {
					return item.name
				})
				var pnames = "";
				var le = productNames.length
				for (var i=0;i<le;i++) {
					if(i>0){
						pnames+="<br/>"
					}
					pnames+=productNames[i]

				}
				$("#auditProductNames").html(pnames)
				$('#productAuditModal').modal('show')
			}
		})

		// 提交审核弹窗 -> 提交按钮点击事件
		$('#doProductAudit').on('click', function () {
			// 获取id数组
			var oids = checkItems.map(function (item) {
				return item.oid
			})
			// 提交数组
			http.post(
				config.api.productAuditApply, 
				{
					data: {
						oids: JSON.stringify(oids)
					},
					contentType: 'form',
				}, 
				function(result) {
					checkItems = []
					$('#productAuditModal').modal('hide')
					if (result.errorCode == 0) {
						$('#productDesignTable').bootstrapTable('refresh')
					}
				}
			)
				
		})

		// 选择资产池按钮点击事件
		$('#productAssetPool').on('click', function () {
			$('#assetPoolModal').modal('show')
		})
    	     
    	// 选择渠道点击确定按钮事件
		$('#doChooseChannel').on('click', function () {
			// 获取id数组
			var channelOids = checkChannels.map(function (item) {
				return item.oid
			})
			// 提交数组
			http.post(
				config.api.saveProductChannel, 
				{
					data: {
						productOid: selectProductOid, 
						channelOid: JSON.stringify(channelOids)
					},
					contentType: 'form',
				}, 
				function(result) {
					checkChannels = []
					$('#channelModal').modal('hide')
					if (result.errorCode == 0) {
						$('#productDesignTable').bootstrapTable('refresh')
					}
				}
			)
				
		})
		
    }
  }
	
})
