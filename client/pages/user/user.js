/**
 * 用户管理
 */
define([
  'http',
  'config',
  'util',
  'extension'
],
function (http, config, util, $$) {
	return {
	  name: 'user',
	  init: function (){
			var confirm = $('#confirmModal')

			// 缓存全部角色信息
			var roles = []
			http.post(config.api.role.list, {
				data: {
					system: 'GAH',
				},
				contentType: 'form'
			}, function (res) {
				roles = res.rows
				var addRoles = document.addUserForm.roles
				var updateRoles = document.updateUserForm.roles
				roles.forEach(function (item) {
					$(addRoles).append('<option value="' + item.oid + '">' + item.name + '</option>')
					$(updateRoles).append('<option value="' + item.oid + '">' + item.name + '</option>')
				})
				$(addRoles).select2()
				$(updateRoles).select2()
			})

			// 分页配置
			var pageOptions = {
				page: 1,
				rows: 10,
				offset: 0,
				keyword: '',
				type: 'ADMIN',
				system: 'GAH'
			}
			// 权限表格配置
			var tableConfig = {
				ajax: function (origin) {
					http.post(config.api.user.search, {
						data: pageOptions,
						contentType: 'form'
					}, function (rlt) {
						origin.success(rlt)
					})
				},
				pageSize: pageOptions.size,
				pagination: true,
				sidePagination: 'server',
				pageList: [10, 20, 30, 50, 100],
				queryParams: getQueryParams,
				columns: [
					{
						width: 30,
						align: 'center',
						formatter: function (val, row, index) {
							return pageOptions.offset + index + 1
						}
					},
					{
						field: 'sn'
					},
					{
						field: 'account'
					},
					{
						field: 'name'
					},
					{
						field: 'comment'
					},
					{
						field: 'status',
						formatter: function (val) {
							switch (val) {
								case 'VALID': return '<span class="text-green">正常</span>'
								case 'EXPIRED': return '<span class="text-red">过期</span>'
							}
						}
					},
					{
						field: 'validDate'
					},
					{
						field: 'loginIp'
					},
					{
						field: 'loginTime'
					},
					{
						width: 100,
						align: 'center',
						formatter: function (val, row, index) {
							var buttons = [{
								text: '详情',
								type: 'buttonGroup',
								class: 'item-detail',
								isCloseBottom: index >= pageOptions.rows - 2,
								sub: [{
									text: '修改',
									class: 'item-update'
								}, {
									text: '删除',
									class: 'item-delete'
								}]
							}]
							return util.table.formatter.generateButton(buttons)
						},
						events: {
							'click .item-detail': function (e, val, row) {
								http.post(config.api.user.roles, {
									data: {
										adminOid: row.oid
									},
									contentType: 'form'
								}, function (result) {
									row.roles = result.rows.map(function (item) {
										return '<span style="margin-right: 10px;">' + item.name + '</span>'
									}).join('')
									row.validTime = row.validTime || '永久性'
									$$.detailAutoFix($('#detailModal'), row)
								})
								$('#detailModal').modal('show')
							},
							'click .item-update': function (e, val, row) {
								var form = document.updateUserForm
								// 重置和初始化表单验证
								$(form).validator('destroy')
								util.form.validator.init($(form));

								$$.formAutoFix($(form), row)
								// 角色select2赋值
								http.post(config.api.user.roles, {
									data: {
										adminOid: row.oid
									},
									contentType: 'form'
								}, function (result) {
									$(form.roles).val(result.rows.map(function (item) {
										return item.oid
									})).trigger('change')
								})
								
								$('#updateUserModal').modal('show')
							},
							'click .item-delete': function (e, val, row) {
								$$.confirm({
									container: $('#confirmModal'),
									trigger: this,
									accept: function () {
										http.post(config.api.role.delete, {
											data: {
												oid: row.oid
											},
											contentType: 'form'
										}, function () {
											$('#roleTable').bootstrapTable('refresh')
										})
									}
								})
							}
						}
					}
				]
			}
			// 初始化权限表格
			$('#userTable').bootstrapTable(tableConfig)
			// 初始化权限表格搜索表单
			$$.searchInit($('#searchForm'), $('#userTable'))

			// 新建用户 - 有效期下拉菜单change事件
			$(document.addUserForm.validSign).on('change', function () {
				var form = $('#addUserForm')
				var nextCol = $(this).parent().parent().next('.col-sm-6')
				if (this.value) {
					nextCol.find('input[name=validTime]').attr('disabled', false)
					form.validator('destroy')
					util.form.validator.init(form)
					nextCol.show()
				} else {
					nextCol.find('input[name=validTime]').attr('disabled', 'disabled')
					form.validator('destroy')
					util.form.validator.init(form)
					nextCol.hide()
				}
			}).change()

			// 新建用户按钮点击事件
			$('#addUser').on('click', function () {
				$('#addUserModal').modal('show')
			})

			// 新建用户 - 确定按钮点击事件
			$('#doAddUser').on('click', function () {
				var form = document.addUserForm
				if (!$(form).validator('doSubmitCheck')) return
				$(form).ajaxSubmit({
					url: config.api.user.create,
					success: function () {
						util.form.reset($(form))
						$('#userTable').bootstrapTable('refresh')
						$('#addUserModal').modal('hide')
					}
				})
			})

			// 修改用户 - 确定按钮点击事件
			$('#doUpdateUser').on('click', function () {
				var form = document.updateUserForm
				if (!$(form).validator('doSubmitCheck')) return
				form.systemOid.value = 'GAH'
				chosenAuths.forEach(function (item) {
					$(form).append('<input name="auths" type="hidden" value="' + item.oid + '">')
				})
				$(form).ajaxSubmit({
					url: config.api.user.update,
					success: function () {
						$(form).find('input[name=auths]').remove()
						util.form.reset($(form))
						$('#userTable').bootstrapTable('refresh')
						$('#updateUserModal').modal('hide')
					}
				})
			})

			function getQueryParams (val) {
				// 参数 val 是bootstrap-table默认的与服务器交互的数据，包含分页、排序数据
				var form = document.searchForm
				// 分页数据赋值
				pageOptions.rows = val.limit
				pageOptions.page = parseInt(val.offset / val.limit) + 1
				pageOptions.offset = val.offset
				pageOptions.keyword = form.keyword.value.trim()
				return val
			}
		}
  }
})
