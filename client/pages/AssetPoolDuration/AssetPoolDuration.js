/**
 * 资产池存续期管理
 */
define([
  'http',
  'config',
  'util',
  'extension'
], function (http, config, util, $$) {
  return {
    name: 'AssetPoolDuration',
    init: function () {
      var pid = util.nav.getHashObj(location.hash).id

      http.post(config.api.duration.assetPool.getById, {
        data: {
          oid: pid
        },
        contentType: 'form'
      }, function (json) {
        console.log(json.result)
        var detail = json.result
        $('#detailPoolScale').html(detail.scale)
        $('#detailPoolCash').html(detail.cashPosition)
        // 饼图生成
        var pieChart = echarts.init(document.getElementById('pieChart'))
        pieChart.setOption(getPieOptions(config, detail))
        // 柱状图生成
        var barChart = echarts.init(document.getElementById('barChart'))
        barChart.setOption(getBarOptions(config, detail))
      })

      // 资产申购类型radio change事件
      $(document.buyAssetForm.buyType).on('ifChecked', function () {
        if (this.value === 'fund') {
          $('#buyAssetShowFund').show()
          $('#buyAssetShowTrust').hide()
        } else {
          $('#buyAssetShowFund').hide()
          $('#buyAssetShowTrust').show()
        }
      })

      // 资产申购表单初始化
      util.form.validator.init($('#buyAssetForm'))

      // 资产申购按钮点击事件
      $('#buyAsset').on('click', function () {
        http.post(config.api.duration.order.getTargetList, {
          data: {
            pid: pid
          },
          contentType: 'form'
        }, function (json) {
          targetNames = json
          var fundTargetNameOptions = ''
          var trustTargetNameOptions = ''
          json.fund.forEach(function (item) {
            fundTargetNameOptions += '<option value="' + item.cashtoolOid + '">' + item.cashtoolName + '</option>'
          })
          json.trust.forEach(function (item) {
            trustTargetNameOptions += '<option value="' + item.targetOid + '">' + item.targetName + '</option>'
          })
          $('#fundTargetName').html(fundTargetNameOptions).trigger('change')
          $('#trustTargetName').html(trustTargetNameOptions).trigger('change')
        })
        http.post(config.api.duration.assetPool.getNameList, function (json) {
          var assetPoolOptions = ''
          json.rows.forEach(function (item) {
            assetPoolOptions += '<option value="' + item.oid + '">' + item.name + '</option>'
          })
          $(document.buyAssetForm.assetPoolOid).html(assetPoolOptions)
        })
        $('#buyAssetModal').modal('show')
      })

      // 缓存标的名称数组值
      var targetNames = null

      // 资产申购标的名称下拉菜单change事件
      $('#fundTargetName').on('change', function () {
        var source = targetNames.fund.filter(function (item) {
          return item.cashtoolOid === this.value
        }.bind(this))
        if (source[0]) {
          source[0].cashtoolType = util.enum.transform('TARGETTYPE', source[0].cashtoolType)
          $$.formAutoFix($('#buyAssetForm'), source[0])
        }
      })
      $('#trustTargetName').on('change', function () {
        var source = targetNames.trust.filter(function (item) {
          return item.targetOid === this.value
        }.bind(this))
        if (source[0]) {
          source[0].targetType = util.enum.transform('TARGETTYPE', source[0].targetType)
          $$.formAutoFix($('#buyAssetForm'), source[0])
        }
      })

      // 资产申购 - 提交审核按钮点击事件
      $('#doBuyAsset').on('click', function () {
        var form = document.buyAssetForm
        var url = ''
        if (form.buyType.value === 'fund') {
          url = config.api.duration.order.purchaseForFund
        } else {
          url = config.api.duration.order.purchaseForTrust
        }
        $(form).ajaxSubmit({
          url: url,
          success: function () {
            util.form.reset($(form))
            $('#orderingToolTable').bootstrapTable('refresh')
            $('#buyAssetModal').modal('hide')
          }
        })
      })

      // 出入金明细按钮点击事件
      $('#showAccountDetail').on('click', function () {
        $('#accountDetailModal').modal('show')
      })

      // 预约中现金类管理工具分页信息
      var orderingToolPageOptions = {
        page: 1,
        rows: 10,
        pid: pid
      }
      // 预约中现金类管理工具表格配置
      var orderingToolTableConfig = {
        ajax: function (origin) {
          http.post(config.api.duration.order.getFundListForAppointment, {
            data: orderingToolPageOptions,
            contentType: 'form'
          }, function (rlt) {
            origin.success(rlt)
          })
        },
        pageNumber: orderingToolPageOptions.page,
        pageSize: orderingToolPageOptions.rows,
        pagination: true,
        sidePagination: 'server',
        pageList: [10, 20, 30, 50, 100],
        queryParams: function (val) {
          orderingToolPageOptions.rows = val.limit
          orderingToolPageOptions.page = parseInt(val.offset / val.limit) + 1
          return val
        },
        columns: [
          {
            width: 60,
            align: 'center',
            formatter: function (val, row, index) {
              return index + 1
            }
          },
          {
            field: 'cashtoolName'
          },
          {
            field: 'cashtoolType',
            formatter: function (val) {
              return util.enum.transform('CASHTOOLTYPE', val)
            }
          },
          {
            field: 'netRevenue'
          },
          {
            field: 'yearYield7'
          },
          {
            field: 'riskLevel'
          },
          {
            field: 'dividendType'
          },
          {
            field: 'circulationShares'
          },
          {
            field: 'investDate'
          },
          {
            field: 'volume'
          },
          {
            field: 'optType',
            formatter: function (val) {
              return val === 'purchase' ? '申购' : '赎回'
            }
          },
          {
            field: 'state',
            formatter: function (val) {
              switch (val) {
                case '-2':
                  return '<span class="text-red">失败</span>'
                case '-1':
                  return '<span class="text-aqua">待审核</span>'
                case '0':
                  return '<span class="text-blue">待预约</span>'
                case '1':
                  return '<span class="text-yellow">待确认</span>'
                case '2':
                  return '<span class="text-green">成立</span>'
              }
            }
          },
          {
            width: 180,
            align: 'center',
            formatter: function () {
              var buttons = [{
                text: '审核',
                type: 'button',
                class: 'item-audit'
              }, {
                text: '预约',
                type: 'button',
                class: 'item-ordering'
              }, {
                text: '确认',
                type: 'button',
                class: 'item-accpet'
              }]
              return util.table.formatter.generateButton(buttons)
            },
            events: {
              'click .item-audit': function (e, val, row) {
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
                }, function (json) {
                  var result = json.result
                  var form = document.fundCheckForm
                  form.oid.value = result.oid
                  form.cashtoolOid.value = result.cashtoolOid
                  form.opType.value = 'audit'
                  form.assetPoolOid.value = pid
                  var formGroups = $(form).find('.form-group')
                  formGroups.each(function (index, item) {
                    if (!index) {
                      $(item).css({display: 'block'}).find('input').attr('disabled', false)
                    } else {
                      $(item).css({display: 'none'}).find('input').attr('disabled', 'disabled')
                    }
                  })
                  $(form).validator('destroy')
                  util.form.validator.init($(form))
                  modal.find('.labelForOrdering').css({display: 'none'})
                  modal.find('.labelForAccept').css({display: 'none'})
                  result.cashtoolTypeStr = util.enum.transform('TARGETTYPE', result.cashtoolType)
                  $$.detailAutoFix(modal, result)
                })
                modal.modal('show')
              },
              'click .item-ordering': function (e, val, row) {
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
                }, function (json) {
                  var result = json.result
                  var form = document.fundCheckForm
                  form.oid.value = result.oid
                  form.cashtoolOid.value = result.cashtoolOid
                  form.opType.value = 'ordering'
                  form.assetPoolOid.value = pid
                  var formGroups = $(form).find('.form-group')
                  formGroups.each(function (index, item) {
                    if (index === 1) {
                      $(item).css({display: 'block'}).find('input').attr('disabled', false)
                    } else {
                      $(item).css({display: 'none'}).find('input').attr('disabled', 'disabled')
                    }
                  })
                  $(form).validator('destroy')
                  util.form.validator.init($(form))
                  modal.find('.labelForOrdering').css({display: 'block'})
                  modal.find('.labelForAccept').css({display: 'none'})
                  result.cashtoolTypeStr = util.enum.transform('TARGETTYPE', result.cashtoolType)
                  $$.detailAutoFix(modal, result)
                })
                modal.modal('show')
              },
              'click .item-accpet': function (e, val, row) {
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
                }, function (json) {
                  var result = json.result
                  var form = document.fundCheckForm
                  form.oid.value = result.oid
                  form.cashtoolOid.value = result.cashtoolOid
                  form.opType.value = 'accept'
                  form.assetPoolOid.value = pid
                  var formGroups = $(form).find('.form-group')
                  formGroups.each(function (index, item) {
                    if (index === 2) {
                      $(item).css({display: 'block'}).find('input').attr('disabled', false)
                    } else {
                      $(item).css({display: 'none'}).find('input').attr('disabled', 'disabled')
                    }
                  })
                  $(form).validator('destroy')
                  util.form.validator.init($(form))
                  modal.find('.labelForOrdering').css({display: 'block'})
                  modal.find('.labelForAccept').css({display: 'block'})
                  result.cashtoolTypeStr = util.enum.transform('TARGETTYPE', result.cashtoolType)
                  $$.detailAutoFix(modal, result)
                })
                modal.modal('show')
              }
            }
          }
        ]
      }
      // 预约中现金类管理工具表格初始化
      $('#orderingToolTable').bootstrapTable(orderingToolTableConfig)

      // 现金类管理工具分页信息
      var toolPageOptions = {
        page: 1,
        rows: 10,
        pid: pid
      }
      // 现金类管理工具表格配置
      var toolTableConfig = {
        ajax: function (origin) {
          http.post(config.api.duration.order.getFundList, {
            data: toolPageOptions,
            contentType: 'form'
          }, function (rlt) {
            origin.success(rlt)
          })
        },
        pageNumber: toolPageOptions.page,
        pageSize: toolPageOptions.rows,
        pagination: true,
        sidePagination: 'server',
        pageList: [10, 20, 30, 50, 100],
        queryParams: function (val) {
          toolPageOptions.rows = val.limit
          toolPageOptions.page = parseInt(val.offset / val.limit) + 1
          return val
        },
        onLoadSuccess: function () {
        },
        columns: [
          {
            width: 60,
            align: 'center',
            formatter: function (val, row, index) {
              return index + 1
            }
          },
          {
            field: 'cashtoolName'
          },
          {
            field: 'cashtoolType',
            formatter: function (val) {
              return util.enum.transform('CASHTOOLTYPE', val)
            }
          },
          {
            field: 'netRevenue'
          },
          {
            field: 'yearYield7'
          },
          {
            field: 'riskLevel'
          },
          {
            field: 'dividendType'
          },
          {
            field: 'circulationShares'
          },
          {
            field: 'investDate'
          },
          {
            field: 'volume'
          },
          {
            field: 'state',
            formatter: function (val) {
              switch (val) {
                case '-1':
                  return '<span class="text-aqua">失败</span>'
                case '0':
                  return '<span class="text-blue">成立</span>'
              }
            }
          },
          {
            width: 120,
            align: 'center',
            formatter: function () {
              var buttons = [{
                text: '申购',
                type: 'button',
                class: 'item-purchase'
              }, {
                text: '赎回',
                type: 'button',
                class: 'item-redeem'
              }]
              return util.table.formatter.generateButton(buttons)
            },
            events: {
              'click .item-purchase': function (e, val, row) {
                http.post(config.api.duration.order.getFundByOid, {
                  data: {
                    oid: row.oid,
                    type: row.type
                  },
                  contentType: 'form'
                }, function (json) {
                  $$.formAutoFix($('#purchaseForm'), json.result)
                })
                $('#purchaseModal').modal('show')
              },
              'click .item-redeem': function (e, val, row) {
                http.post(config.api.duration.order.getFundByOid, {
                  data: {
                    oid: row.oid,
                    type: row.type
                  },
                  contentType: 'form'
                }, function (json) {
                  $$.formAutoFix($('#redeemForm'), json.result)
                })
                $('#redeemModal').modal('show')
              }
            }
          }
        ]
      }
      // 现金类管理工具表格初始化
      $('#toolTable').bootstrapTable(toolTableConfig)

      // 现金类管理工具 - 申购表格验证初始化
      util.form.validator.init($('#purchaseForm'))
      // 现金类管理工具 - 赎回表格验证初始化
      util.form.validator.init($('#redeemForm'))

      // 现金类管理工具审核/预约/确认 - 通过按钮点击事件
      $('#doFundCheck').on('click', function () {
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
        $(form).ajaxSubmit({
          url: url,
          success: function () {
            util.form.reset($(form))
            $('#orderingToolTable').bootstrapTable('refresh')
            $('#toolTable').bootstrapTable('refresh')
            $('#fundCheckModal').modal('hide')
          }
        })
      })
      // 现金类管理工具审核/预约/确认 - 不通过按钮点击事件
      $('#doFundUnCheck').on('click', function () {
        var form = document.fundCheckForm
        form.state.value = '-1'
        var url = ''
        switch (form.opType) {
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
          success: function () {
            util.form.reset($(form))
            $('#orderingToolTable').bootstrapTable('refresh')
            $('#fundCheckModal').modal('hide')
          }
        })
      })

      // 现金类管理工具 - 申购弹窗 - 提交审核按钮点击事件
      $('#doPurchase').on('click', function () {
        var form = document.purchaseForm
        $(form).ajaxSubmit({
          url: config.api.duration.order.purchaseForFund,
          success: function () {
            util.form.reset($(form))
            $('#orderingToolTable').bootstrapTable('refresh')
            $('#toolTable').bootstrapTable('refresh')
            $('#purchaseModal').modal('hide')
          }
        })
      })

      // 现金类管理工具 - 赎回弹窗 - 提交审核按钮点击事件
      $('#doRedeem').on('click', function () {
        var form = document.redeemForm
        $(form).ajaxSubmit({
          url: config.api.duration.order.redeem,
          success: function () {
            util.form.reset($(form))
            $('#orderingToolTable').bootstrapTable('refresh')
            $('#toolTable').bootstrapTable('refresh')
            $('#redeemModal').modal('hide')
          }
        })
      })

      // 预约中信托计划分页信息
      var orderingTrustPageOptions = {
        page: 1,
        rows: 10,
        pid: pid
      }
      // 预约中信托计划表格配置
      var orderingTrustTableConfig = {
        ajax: function (origin) {
          http.post(config.api.duration.order.getTrustListForAppointment, {
            data: orderingTrustPageOptions,
            contentType: 'form'
          }, function (rlt) {
            origin.success(rlt)
          })
        },
        pageNumber: orderingTrustPageOptions.page,
        pageSize: orderingTrustPageOptions.rows,
        pagination: true,
        sidePagination: 'server',
        pageList: [10, 20, 30, 50, 100],
        queryParams: function (val) {
          orderingTrustPageOptions.rows = val.limit
          orderingTrustPageOptions.page = parseInt(val.offset / val.limit) + 1
          return val
        },
        onLoadSuccess: function () {
        },
        columns: [
          {
            width: 60,
            align: 'center',
            formatter: function (val, row, index) {
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
            formatter: function (val) {
              return util.enum.transform('ACCRUALTYPE', val)
            }
          },
          {
            field: 'raiseScope'
          },
          {
            field: 'holdAmount'
          },
          {
            field: 'subjectRating'
          },
          {
            field: 'type'
          },
          {
            field: 'state',
            formatter: function (val) {
              switch (val) {
                case '-2':
                  return '<span class="text-red">失败</span>'
                case '-1':
                  return '<span class="text-aqua">待审核</span>'
                case '0':
                  return '<span class="text-blue">待预约</span>'
                case '1':
                  return '<span class="text-yellow">待确认</span>'
                case '2':
                  return '<span class="text-green">成立</span>'
              }
            }
          },
          {
            width: 256,
            align: 'center',
            formatter: function (val, row) {
              var buttons = [{
                text: '审核',
                type: 'button',
                class: 'item-audit',
                isRender: row.type === '申购'
              }, {
                text: '预约',
                type: 'button',
                class: 'item-ordering',
                isRender: row.type === '申购'
              }, {
                text: '确认',
                type: 'button',
                class: 'item-accpet',
                isRender: row.type === '申购'
              }, {
                text: '本息兑付审核',
                type: 'button',
                class: 'item-income-audit',
                isRender: row.type === '本息兑付'
              }, {
                text: '本息兑付确认',
                type: 'button',
                class: 'item-income-accpet',
                isRender: row.type === '本息兑付'
              }, {
                text: '转让审核',
                type: 'button',
                class: 'item-transfer-audit',
                isRender: row.type === '转让'
              }, {
                text: '转让确认',
                type: 'button',
                class: 'item-transfer-accpet',
                isRender: row.type === '转让'
              }]
              return util.table.formatter.generateButton(buttons)
            },
            events: {
              'click .item-audit': function (e, val, row) {
                var modal = $('#trustCheckModal')
                http.post(config.api.duration.order.getTrustOrderByOid, {
                  data: {
                    oid: row.oid,
                    type: row.type
                  },
                  contentType: 'form'
                }, function (json) {
                  var result = json.result
                  var form = document.trustCheckForm
                  form.oid.value = result.oid
                  form.type.value = row.type
                  form.opType.value = 'audit'
                  form.assetPoolOid.value = pid
                  var formGroups = $(form).find('.form-group')
                  formGroups.each(function (index, item) {
                    if (!index) {
                      $(item).css({display: 'block'}).find('input').attr('disabled', false)
                    } else {
                      $(item).css({display: 'none'}).find('input').attr('disabled', 'disabled')
                    }
                  })
                  $(form).validator('destroy')
                  util.form.validator.init($(form))
                  modal.find('.labelForOrdering').css({display: 'none'})
                  modal.find('.labelForAccept').css({display: 'none'})
                  result.cashtoolTypeStr = util.enum.transform('TARGETTYPE', result.cashtoolType)
                  $$.detailAutoFix(modal, result)
                })
                modal.modal('show')
              },
              'click .item-income-audit': function (e, val, row) {
                var modal = $('#trustCheckModal')
                http.post(config.api.duration.order.getTrustOrderByOid, {
                  data: {
                    oid: row.oid,
                    type: row.type
                  },
                  contentType: 'form'
                }, function (json) {
                  var result = json.result
                  var form = document.trustCheckForm
                  form.oid.value = result.oid
                  form.type.value = row.type
                  form.opType.value = 'audit'
                  form.assetPoolOid.value = pid
                  var formGroups = $(form).find('.form-group')
                  formGroups.each(function (index, item) {
                    if (!index) {
                      $(item).css({display: 'block'}).find('input').attr('disabled', false)
                    } else {
                      $(item).css({display: 'none'}).find('input').attr('disabled', 'disabled')
                    }
                  })
                  $(form).validator('destroy')
                  util.form.validator.init($(form))
                  modal.find('.labelForOrdering').css({display: 'none'})
                  modal.find('.labelForAccept').css({display: 'none'})
                  result.cashtoolTypeStr = util.enum.transform('TARGETTYPE', result.cashtoolType)
                  $$.detailAutoFix(modal, result)
                })
                modal.modal('show')
              },
              'click .item-transfer-audit': function (e, val, row) {
                var modal = $('#trustCheckModal')
                http.post(config.api.duration.order.getTrustOrderByOid, {
                  data: {
                    oid: row.oid,
                    type: row.type
                  },
                  contentType: 'form'
                }, function (json) {
                  var result = json.result
                  var form = document.trustCheckForm
                  form.oid.value = result.oid
                  form.type.value = row.type
                  form.opType.value = 'audit'
                  form.assetPoolOid.value = pid
                  var formGroups = $(form).find('.form-group')
                  formGroups.each(function (index, item) {
                    if (!index) {
                      $(item).css({display: 'block'}).find('input').attr('disabled', false)
                    } else {
                      $(item).css({display: 'none'}).find('input').attr('disabled', 'disabled')
                    }
                  })
                  $(form).validator('destroy')
                  util.form.validator.init($(form))
                  modal.find('.labelForOrdering').css({display: 'none'})
                  modal.find('.labelForAccept').css({display: 'none'})
                  result.cashtoolTypeStr = util.enum.transform('TARGETTYPE', result.cashtoolType)
                  $$.detailAutoFix(modal, result)
                })
                modal.modal('show')
              },
              'click .item-ordering': function (e, val, row) {
                var modal = $('#trustCheckModal')
                http.post(config.api.duration.order.getTrustOrderByOid, {
                  data: {
                    oid: row.oid,
                    type: row.type
                  },
                  contentType: 'form'
                }, function (json) {
                  var result = json.result
                  var form = document.trustCheckForm
                  form.oid.value = result.oid
                  form.type.value = row.type
                  form.opType.value = 'ordering'
                  form.assetPoolOid.value = pid
                  var formGroups = $(form).find('.form-group')
                  formGroups.each(function (index, item) {
                    if (index === 1) {
                      $(item).css({display: 'block'}).find('input').attr('disabled', false)
                    } else {
                      $(item).css({display: 'none'}).find('input').attr('disabled', 'disabled')
                    }
                  })
                  $(form).validator('destroy')
                  util.form.validator.init($(form))
                  modal.find('.labelForOrdering').css({display: 'block'})
                  modal.find('.labelForAccept').css({display: 'none'})
                  result.cashtoolTypeStr = util.enum.transform('TARGETTYPE', result.cashtoolType)
                  $$.detailAutoFix(modal, result)
                })
                modal.modal('show')
              },
              'click .item-accpet': function (e, val, row) {
                var modal = $('#trustCheckModal')
                http.post(config.api.duration.order.getTrustOrderByOid, {
                  data: {
                    oid: row.oid,
                    type: row.type
                  },
                  contentType: 'form'
                }, function (json) {
                  var result = json.result
                  var form = document.trustCheckForm
                  form.oid.value = result.oid
                  form.type.value = row.type
                  form.opType.value = 'accept'
                  form.assetPoolOid.value = pid
                  var formGroups = $(form).find('.form-group')
                  formGroups.each(function (index, item) {
                    if (index === 2) {
                      $(item).css({display: 'block'}).find('input').attr('disabled', false)
                    } else {
                      $(item).css({display: 'none'}).find('input').attr('disabled', 'disabled')
                    }
                  })
                  $(form).validator('destroy')
                  util.form.validator.init($(form))
                  modal.find('.labelForOrdering').css({display: 'block'})
                  modal.find('.labelForAccept').css({display: 'block'})
                  result.cashtoolTypeStr = util.enum.transform('TARGETTYPE', result.cashtoolType)
                  $$.detailAutoFix(modal, result)
                })
                modal.modal('show')
              },
              'click .item-income-accpet': function (e, val, row) {
                var modal = $('#trustCheckModal')
                http.post(config.api.duration.order.getTrustOrderByOid, {
                  data: {
                    oid: row.oid,
                    type: row.type
                  },
                  contentType: 'form'
                }, function (json) {
                  var result = json.result
                  var form = document.trustCheckForm
                  form.oid.value = result.oid
                  form.type.value = row.type
                  form.opType.value = 'accept'
                  form.assetPoolOid.value = pid
                  var formGroups = $(form).find('.form-group')
                  formGroups.each(function (index, item) {
                    if (index === 2) {
                      $(item).css({display: 'block'}).find('input').attr('disabled', false)
                    } else {
                      $(item).css({display: 'none'}).find('input').attr('disabled', 'disabled')
                    }
                  })
                  $(form).validator('destroy')
                  util.form.validator.init($(form))
                  modal.find('.labelForOrdering').css({display: 'block'})
                  modal.find('.labelForAccept').css({display: 'block'})
                  result.cashtoolTypeStr = util.enum.transform('TARGETTYPE', result.cashtoolType)
                  $$.detailAutoFix(modal, result)
                })
                modal.modal('show')
              },
              'click .item-transfer-accpet': function (e, val, row) {
                var modal = $('#trustCheckModal')
                http.post(config.api.duration.order.getTrustOrderByOid, {
                  data: {
                    oid: row.oid,
                    type: row.type
                  },
                  contentType: 'form'
                }, function (json) {
                  var result = json.result
                  var form = document.trustCheckForm
                  form.oid.value = result.oid
                  form.type.value = row.type
                  form.opType.value = 'accept'
                  form.assetPoolOid.value = pid
                  var formGroups = $(form).find('.form-group')
                  formGroups.each(function (index, item) {
                    if (index === 2) {
                      $(item).css({display: 'block'}).find('input').attr('disabled', false)
                    } else {
                      $(item).css({display: 'none'}).find('input').attr('disabled', 'disabled')
                    }
                  })
                  $(form).validator('destroy')
                  util.form.validator.init($(form))
                  modal.find('.labelForOrdering').css({display: 'block'})
                  modal.find('.labelForAccept').css({display: 'block'})
                  result.cashtoolTypeStr = util.enum.transform('TARGETTYPE', result.cashtoolType)
                  $$.detailAutoFix(modal, result)
                })
                modal.modal('show')
              }
            }
          }
        ]
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
        pid: pid
      }
      // 信托计划表格配置
      var trustTableConfig = {
        ajax: function (origin) {
          http.post(config.api.duration.order.getTrustList, {
            data: trustPageOptions,
            contentType: 'form'
          }, function (rlt) {
            origin.success(rlt)
          })
        },
        pageNumber: trustPageOptions.page,
        pageSize: trustPageOptions.rows,
        pagination: true,
        sidePagination: 'server',
        pageList: [10, 20, 30, 50, 100],
        queryParams: function (val) {
          trustPageOptions.rows = val.limit
          trustPageOptions.page = parseInt(val.offset / val.limit) + 1
          return val
        },
        onLoadSuccess: function () {
        },
        columns: [
          {
            width: 60,
            align: 'center',
            formatter: function (val, row, index) {
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
            formatter: function (val) {
              return util.enum.transform('ACCRUALTYPE', val)
            }
          },
          {
            field: 'raiseScope'
          },
          {
            field: 'holdAmount'
          },
          {
            field: 'subjectRating'
          },
          {
            field: 'state',
            formatter: function (val) {
              switch (val) {
                case '-1':
                  return '<span class="text-aqua">失败</span>'
                case '0':
                  return '<span class="text-blue">成立</span>'
              }
            }
          },
          {
            width: 160,
            align: 'center',
            formatter: function () {
              var buttons = [{
                text: '本息兑付',
                type: 'button',
                class: 'item-income'
              }, {
                text: '转让',
                type: 'button',
                class: 'item-transfer'
              }]
              return util.table.formatter.generateButton(buttons)
            },
            events: {
              'click .item-income': function (e, val, row) {
                http.post(config.api.duration.order.getTrustByOid, {
                  data: {
                    oid: row.oid,
                    type: 'income'
                  },
                  contentType: 'form'
                }, function (json) {
                  seqs = json.result.incomeFormList
                  var form = document.trustIncomeForm
                  var seq = $(form.seq).empty()
                  form.oid.value = json.result.oid
                  form.assetPoolOid.value = json.result.assetPoolOid
                  seqs.forEach(function (item) {
                    seq.append('<option value="' + item.seq + '">第' + item.seq + '期</option>')
                  })
                  seq.change()
                })
                $('#trustIncomeModal').modal('show')
              },
              'click .item-transfer': function (e, val, row) {
                http.post(config.api.duration.order.getTrustByOid, {
                  data: {
                    oid: row.oid,
                    type: 'transfer'
                  },
                  contentType: 'form'
                }, function (json) {
                  var result = json.result
                  var form = document.trustTransferForm
                  form.oid.value = result.oid
                  form.assetPoolOid.value = pid
                  result.cashtoolTypeStr = util.enum.transform('TARGETTYPE', result.cashtoolType)
                  $$.detailAutoFix($('#trustTransferModal'), result)
                })
                $('#trustTransferModal').modal('show')
              }
            }
          }
        ]
      }
      // 信托计划表格初始化
      $('#trustTable').bootstrapTable(trustTableConfig)

      // 信托计划本息兑付表单初始化
      util.form.validator.init($('#trustIncomeForm'))
      // 信托计划转让表单初始化
      util.form.validator.init($('#trustTransferForm'))

      // 信托计划本息兑付表单下拉菜单初始化
      $(document.trustIncomeForm.seq).on('change', function () {
        var val = this.value
        seqs.forEach(function (item, index) {
          if (item.seq == val) {
            $$.formAutoFix($(document.trustIncomeForm), item)
            if (index === seqs.length - 1) {
              $('#capitalArea').show()
            } else {
              $('#capitalArea').hide()
            }
          }
        })
      })

      // 信托计划审核/预约/确认 - 通过按钮点击事件
      $('#doTrustCheck').on('click', function () {
        var form = document.trustCheckForm
        form.state.value = '0'
        var url = ''
        switch (form.type.value) {
          case '申购':
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
          success: function () {
            util.form.reset($(form))
            $('#orderingTrustTable').bootstrapTable('refresh')
            $('#trustTable').bootstrapTable('refresh')
            $('#trustCheckModal').modal('hide')
          }
        })
      })
      // 信托计划审核/预约/确认 - 不通过按钮点击事件
      $('#doTrustUnCheck').on('click', function () {
        var form = document.trustCheckForm
        form.state.value = '-1'
        var url = ''
        switch (form.opType) {
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
          success: function () {
            util.form.reset($(form))
            $('#orderingTrustTable').bootstrapTable('refresh')
            $('#trustCheckModal').modal('hide')
          }
        })
      })

      // 信托计划 - 转让按钮点击事件
      $('#doTrustTransfer').on('click', function () {
        $('#trustTransferForm').ajaxSubmit({
          url: config.api.duration.order.applyForTransfer,
          success: function () {
            $('#orderingTrustTable').bootstrapTable('refresh')
            $('#trustTable').bootstrapTable('refresh')
            $('#trustTransferModal').modal('hide')
          }
        })
      })

      // 信托计划 - 本息兑付按钮点击事件
      $('#doTrustIncome').on('click', function () {
        $('#trustIncomeForm').ajaxSubmit({
          url: config.api.duration.order.applyForIncome,
          success: function () {
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

      // 编辑账户按钮点击事件
      $('#updateAccount').on('click', function () {
        http.post(config.api.duration.assetPool.getById, {
          data: {
            oid: pid
          },
          contentType: 'form'
        }, function (json) {
          $$.formAutoFix($('#updateAssetPoolForm'), json.result)
          $(document.updateAssetPoolForm.scopes).val(json.result.scopes).trigger('change')
          $('#updateAssetPoolModal').modal('show')
        })
      })
    }
  }
})

function getBarOptions (config, source) {
  return {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      orient: 'vertical',
      x: 'left',
      data:['可用现金','冻结资金','在途资金'],
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
      data:[]
    }],
    series: [{
      name: '可用现金',
      type: 'bar',
      label: {
        normal: {
          show: true,
          position: 'right',
          formatter: function (obj) {
            return obj.value + '万'
          }
        }
      },
      data: [source.cashPosition]
    }, {
      name: '冻结资金',
      type: 'bar',
      label: {
        normal: {
          show: true,
          position: 'right',
          formatter: function (obj) {
            return obj.value + '万'
          }
        }
      },
      data: [source.freezeCash]
    }, {
      name: '在途资金',
      type: 'bar',
      showAllSymbol: true,
      label: {
        normal: {
          show: true,
          position: 'right',
          formatter: function (obj) {
            return obj.value + '万'
          }
        }
      },
      data: [source.transitCash]
    }],
    color: config.colors
  }
}

function getPieOptions (config, source) {
  return {
    tooltip: {
      trigger: 'item',
      formatter: "{a} <br/>{b}: {c} ({d}%)"
    },
    legend: {
      orient: 'vertical',
      x: 'left',
      data:['现金','现金类管理工具','信托计划']
    },
    series: [
      {
        name:'投资占比',
        type:'pie',
        radius: ['50%', '70%'],
        avoidLabelOverlap: false,
        label: {
          normal: {
            show: false,
            position: 'center'
          },
          emphasis: {
            show: true,
            textStyle: {
              fontSize: '18',
              fontWeight: 'bold'
            }
          }
        },
        labelLine: {
          normal: {
            show: false
          }
        },
        data:[
          {value:source.cashRate, name:'现金'},
          {value:source.cashtoolRate, name:'现金类管理工具'},
          {value:source.targetRate, name:'信托计划'}
        ]
      }
    ],
    color: config.colors
  }
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