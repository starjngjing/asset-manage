/**
 * 标的申请管理
 */
define([
	'http',
	'config',
	'util',
	'extension'
], function(http, config, util, $$) {
	return {
		name: 'targetApply',
		init: function() {
			// js逻辑写在这里
			// 分页配置
			var pageOptions = {
					number: 1,
					size: 10,
					targetName: '',
					targetType: '',
					state: ''
				}
				// 数据表格配置
			var tableConfig = {
					ajax: function(origin) {
						http.post(config.api.targetListQuery, {
							data: {
								page: pageOptions.number,
								rows: pageOptions.size,
								name: pageOptions.targetName,
								type: pageOptions.targetType,
								state: pageOptions.state
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
						field: 'name'
					}, {
						field: 'accrualType'
					}, {
						field: 'expSetDate'
					}, {
						field: 'type',
						formatter: function(val) {
							return util.enum.transform('TARGETTYPE', val);
						}
					}, {
						field: 'raiseScope',
						formatter: function(val) {
							var temp = val / 10000;
							return temp.toFixed(0) + "万";
						}
					}, {
						field: 'life',
						formatter: function(val, row) {
							switch (row.lifeUnit) {
								case "day":
									return val + '天';
								case "month":
									return val + '月';
								case "year":
									return val + '年';
								default:
									val;
							}
						}
					}, {
						field: 'expIncome',
						formatter: function(val) {
							if(val)
							return val.toFixed(2) + "%";
						}
					}, {
						field: 'expAror',
						formatter: function(val, row) {
							if(val)
								if (val == row.expArorSec) {
									return val.toFixed(2) + "%";
								} else {
									var maxAro = parseFloat(row.expArorSec);
									return val.toFixed(2) + '-' + maxAro.toFixed(2) + "%";
								}
						}
					}, {
						field: 'state',
						formatter: function(val) {
							switch (val) {
								case 'waitPretrial':
									return '待预审'
								case 'pretrial':
									return '预审中'
								case 'waitMeeting':
									return '待过会'
								case 'metting':
									return '过会中'
								case 'collecting':
									return '募集中'
								case 'establish':
									return '成立'
								case 'unEstablish':
									return '成立失败'
								case 'reject':
									return '驳回'
								case 'overdue':
									return '逾期'
								case 'invalid':
									return '作废'
								default:
									return '作废'
							}
						}
					}, {
						align: 'center',
						formatter: function(val, row) {
						var buttons = [
		            	    {
		            	      text: '查看详情',
		            	      type: 'button',
		            	      class: 'item-detail',
		            	      isRender: true
		            	    },
		            	    {
		              	      text: '底层项目',
		              	      type: 'button',
		              	      class: 'item-project',
		              	      isRender: row.state == 'waitPretrial' || row.state == 'reject'
		              	    },
		              	    {
		              	    	text: '提交预审',
		              	    	type: 'button',
		              	    	class: 'item-check',
		              	    	isRender: row.state == 'waitPretrial' || row.state == 'reject'
		              	    },
		              	    {
		              	    	text: '编辑',
		              	    	type: 'button',
		              	    	class: 'item-edit',
		              	    	isRender: row.state == 'waitPretrial' || row.state == 'reject'
		              	    },
		              	    {
		              	      text: '作废',
		              	      type: 'button',
		              	      class: 'item-invalid',
		              	      isRender: row.state != 'invalid'
		              	    }
		            	  ];
	            	  return util.table.formatter.generateButton(buttons);
							/*
							var baseBtn = '<a href="javascript:void(0)" class="item-detail">查看详情</a>';
							if (row.state == 'waitPretrial' || row.state == 'reject') {
								baseBtn += '<a href="javascript:void(0)" class="item-check">提交预审</a>' +
									'<a href="javascript:void(0)" class="item-edit">编辑</a>';
							}
							if (row.state != 'invalid') {
								baseBtn += '<a href="javascript:void(0)" class="item-invalid">作废</a>';
							}
							return '<div class="func-area">' +
								baseBtn +
								'</div>'
								*/
						},
						events: {
							'click .item-check': function(e, value, row) {
								$("#confirmTitle").html("确定提交预审？")
								$$.confirm({
									container: $('#doConfirm'),
									trigger: this,
									accept: function() {
										http.post(config.api.targetExamine, {
											data: {
												oid: row.oid
											},
											contentType: 'form',
										}, function(result) {
											$('#targetApplyTable').bootstrapTable('refresh')
										})
									}
								})
							},
							'click .item-invalid': function(e, value, row) {
								$("#confirmTitle").html("确定作废标的？")
								$$.confirm({
									container: $('#doConfirm'),
									trigger: this,
									accept: function() {
										http.post(config.api.targetInvalid, {
											data: {
												oid: row.oid
											},
											contentType: 'form',
										}, function(result) {
											$('#targetApplyTable').bootstrapTable('refresh')
										})
									}
								})
							},
							'click .item-detail': function(e, value, row) {
								http.post(config.api.targetDetQuery, {
									data: {
										oid: row.oid
									},
									contentType: 'form'
								}, function(result) {
									if (result.errorCode == 0) {
										var data = result.investment;
										$$.detailAutoFix($('#detTargetForm'), data); // 自动填充详情
										$$.formAutoFix($('#detTargetForm'), data); // 自动填充表单
										$('#targetDetailModal').modal('show');
									} else {
										alert(查询失败);
									}
								})
							},
							'click .item-project': function(e, value, row) {
								http.post(config.api.targetDetQuery, {
			                		  data: {
			                			  oid:row.oid
			                		  },
			                		  contentType: 'form'
			                	  },
			                	  function (obj) {
			                		  var data  = obj.investment;
			                		  if(!data){
			                			  toastr.error('标的详情数据不存在', '错误信息', {
			                				  timeOut: 10000
			                			  });
			                		  }
			                		  $$.detailAutoFix($('#targetDetail'), data);	// 自动填充详情
			                		  $$.formAutoFix($('#targetDetail'), data); // 自动填充表单
			                		  
			                		  $$.detailAutoFix($('#projectForm'), data);	// 自动填充详情
			                		  $$.formAutoFix($('#projectForm'), data); // 自动填充表单
			                		  $('#projectModal').modal('show');
			                	  });
							}
						}
					}]
				}
				// 初始化表格
			$('#targetApplyTable').bootstrapTable(tableConfig)
				// 搜索表单初始化
			$$.searchInit($('#targetSearchForm'), $('#targetApplyTable'))
				// 新建标的按钮点击事件
			$('#targetAdd').on('click', function() {
					$('#addTargetModal').modal('show')
				})
				// 新建底层资产按钮点击事件
			$('#assetAdd').on('click', function() {
				$('#addAssetModal').modal('show')
			})

			$('#saveTarget').on('click', function() {
				saveTarget();
			})
			$('#projectSubmit').on('click', function() {
				saveProject();
			})
			for (var i = 0; i < config.targetStates.name.length; i++) {
				$("[name='targetStatus']").append('<option value="' + config.targetStates.value[i] + '">' + config.targetStates.name[i] + '</option>')
			}
//
			$(projectForm.projectType).change(function() { // 项目类型
				var ptt = $(this).val();
				if(ptt === 'PROJECTTYPE_01') { // 金融
					$("#estateDiv").hide();
					$("#financeDiv").show();
				} else if(ptt === 'PROJECTTYPE_02') { // 房地产
					$("#estateDiv").show();
					$("#financeDiv").hide();
				} else {
					$("#estateDiv").hide();
					$("#financeDiv").hide();
				}
			});
//			$("#projectForm :radio[name='warrantor']").click(function() { // 是否有担保人
			$(projectForm.warrantor).each(function (index, item){
				console.log(item)
				$(item).on('ifChecked', function() { // 是否有担保人
					console.log('aasd')
					if($(this).val() === 'yes' )
						$('#prjWarrantorInfo').show();
					else
						$('#prjWarrantorInfo').hide();
				});
			})
			function getQueryParams(val) {
				var form = document.targetSearchForm
				pageOptions.size = val.limit
				pageOptions.number = parseInt(val.offset / val.limit) + 1
				pageOptions.targetName = form.searchName.value.trim();
				pageOptions.targetType = form.targetType.value.trim();
				pageOptions.state = form.targetStatus.value.trim();
				return val
			}
		}
	}

	function saveTarget() {
		$('#addTargetForm').ajaxSubmit({
			url: config.api.targetAdd,
			//			contentType : 'application/json',
			success: function(result) {
				$('#addTargetForm').clearForm();
				$('#addTargetModal').modal('hide');
				$('#targetApplyTable').bootstrapTable('refresh');
			}
		})
	}
	function saveProject(){
		$('#projectForm').ajaxSubmit({
			type:"post",  //提交方式  
            //dataType:"json", //数据类型'xml', 'script', or 'json'  
			url: config.api.saveProject,
			//			contentType : 'application/json',
			success: function(result) {
				$('#projectForm').clearForm();
				$('#projectModal').modal('hide');
				$('#targetApplyTable').bootstrapTable('refresh');
			}
		})
	}

})