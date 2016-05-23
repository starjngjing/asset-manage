/**
 * 角色管理
 */
define([
  'http',
  'config',
  'util',
  'extension'
],
function (http, config, util, $$) {
	return {
	  name: 'role',
	  init: function (){
			var confirm = $('#confirmModal')
			// 分页配置
			var pageOptions = {
				number: 1,
				size: 10,
				offset: 0,
				name: ''
			}
			// 权限表格配置
			var tableConfig = {
				ajax: function (origin) {
					http.post(config.api.role.list, {
						data: {
							page: pageOptions.number,
							rows: pageOptions.size,
							name: pageOptions.name,
							stats: true,
							system: 'GAH'
						},
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
						field: 'name'
					},
					{
						field: 'rac'
					},
					{
						field: 'arc'
					},
					{
						field: 'updateTime'
					},
					{
						field: 'createTime'
					},
					{
						width: 150,
						align: 'center',
						formatter: function (val, row, index) {
							var buttons = [
								{
									text: '详情',
									type: 'button',
									class: 'item-detail'
								},
								{
									text: '修改',
									type: 'button',
									class: 'item-update'
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

						}
					}
				]
			}
			// 初始化权限表格
			$('#roleTable').bootstrapTable(tableConfig)
			// 初始化权限表格搜索表单
			$$.searchInit($('#searchForm'), $('#roleTable'))

			function getQueryParams (val) {
				// 参数 val 是bootstrap-table默认的与服务器交互的数据，包含分页、排序数据
				var form = document.searchForm
				// 分页数据赋值
				pageOptions.size = val.limit
				pageOptions.number = parseInt(val.offset / val.limit) + 1
				pageOptions.offset = val.offset
				pageOptions.name = form.name.value.trim()
				return val
			}
		}
  }
})
