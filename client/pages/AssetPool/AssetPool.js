/**
 * 资产池管理
 */
define([
  'http',
  'config',
  'util',
  'extension'
], function (http, config, util, $$) {
  return {
    name: 'AssetPool',
    init: function () {
      // 分页配置
      var pageOptions = {
        poolName: "",
        page: 1,
        rows: 10
      }
      // 数据表格配置
      var tableConfig = {
        ajax: function (origin) {
          http.post(config.api.duration.assetPool.getAll, {
            data: pageOptions,
            contentType: 'form'
          }, function (rlt) {
            origin.success(rlt)
          })
        },
        pageNumber: pageOptions.page,
        pageSize: pageOptions.rows,
        pagination: true,
        sidePagination: 'server',
        pageList: [10, 20, 30, 50, 100],
        queryParams: getQueryParams,
        onLoadSuccess: function () {
        },
        columns: [
          {// 名称
            field: 'name'
          },
          {// 资产规模
            field: 'scale'
          },
          {// 现金比例
            field: 'cashRate'
          },
          {// 货币基金（现金类管理工具）比例
            field: 'cashtoolRate'
          },
          {// 信托（计划）比例
            field: 'targetRate'
          },
          {// 可用现金
            field: 'cashPosition'
          },
          {// 冻结资金
            field: 'freezeCash'
          },
          {// 在途资金
            field: 'transitCash'
          },
          {// 状态
            field: 'state',
            formatter: function (val) {
              return '<span class="' + (val === '未审核' ? 'text-green' : 'text-red') + '">' + val + '</span>'
            }
          },
          {
            width: 180,
            align: 'center',
            formatter: function () {
              var buttons = [{
                text: '详情',
                type: 'button',
                class: 'item-detail'
              }, {
                text: '审核',
                type: 'button',
                class: 'item-audit'
              }, {
                text: '编辑',
                type: 'button',
                class: 'item-update'
              }]
              return util.table.formatter.generateButton(buttons)
            },
            events: {
              'click .item-detail': function (e, val, row) {
                util.nav.dispatch('AssetPoolDuration', 'id=' + row.oid)
              },
              'click .item-audit': function (e, val, row) {
                http.post(config.api.duration.assetPool.getById, {
                  data: {
                    oid: row.oid
                  },
                  contentType: 'form'
                }, function (json) {
                  $$.detailAutoFix($('#auditAssetPoolModal'), json.result)
                  $('#auditAssetPoolModal').modal('show')
                })
              },
              'click .item-update': function (e, val, row) {
                http.post(config.api.duration.assetPool.getById, {
                  data: {
                    oid: row.oid
                  },
                  contentType: 'form'
                }, function (json) {
                  console.log(json)
                })
                $$.formAutoFix($('#updateAssetPoolForm'), row)
                $('#updateAssetPoolModal').modal('show')
              }
            }
          }
        ]
      }

      // 初始化数据表格
      $('#assetPoolTable').bootstrapTable(tableConfig);
      // 搜索表单初始化
      $$.searchInit($('#searchForm'), $('#assetPoolTable'));

      // 新增/修改资产池投资范围select2初始化
      $(document.addAssetPoolForm.scopes).select2()
      $(document.updateAssetPoolForm.scopes).select2()

      // 新增资产池按钮点击事件
      $('#assetPoolAdd').on('click', function () {
        $('#addAssetPoolModal').modal('show')
      })
      // 新增资产池表单验证初始化
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
      // 新增资产池 - 确定按钮点击事件
      $('#doAddAssetPool').on('click', function () {
        $('#addAssetPoolForm').ajaxSubmit({
          url: config.api.duration.assetPool.create,
          success: function (result) {
            console.log(result)
            util.form.reset($('#addAssetPoolForm'))
            $('#addAssetPoolModal').modal('hide')
          }
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
        parts.each(function (index, item) {
          percentage += Number(item.value)
        })
        return !(percentage > 100)
      }
    }
  }
})
