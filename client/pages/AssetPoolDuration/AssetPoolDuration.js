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

      // 饼图生成
      var pieChart = echarts.init(document.getElementById('pieChart'))
      pieChart.setOption(getPieOptions(config))
      // 柱状图生成
      var barChart = echarts.init(document.getElementById('barChart'))
      barChart.setOption(getBarOptions(config))

      // 资产申购类型radio change事件
      $(document.buyAssetForm.type).on('ifChecked', function () {
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
          return item.targetOid === this.value
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
      $('#doPurchase').on('click', function () {
        var form = document.buyAssetForm
        var url = ''
        if (form.type.value === 'fund') {
          url = config.api.duration.order.purchaseForFund
        } else {
          url = config.api.duration.order.purchaseForTrust
        }
        $(form).ajaxSubmit({
          url: url,
          success: function (result) {
            console.log(result)
          }
        })
      })

      // 出入金明细按钮点击事件
      $('#showAccountDetail').on('click', function () {
        $('#accountDetailModal').modal('show')
      })
    }
  }
})

function getBarOptions (config) {
  return {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      orient: 'vertical',
      x: 'left',
      data:['现金','冻结资金','在途资金'],
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
      name: '现金',
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
      data: [100]
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
      data: [30]
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
      data: [80]
    }],
    color: config.colors
  }
}

function getPieOptions (config) {
  return {
    tooltip: {
      trigger: 'item',
      formatter: "{a} <br/>{b}: {c} ({d}%)"
    },
    legend: {
      orient: 'vertical',
      x: 'left',
      data:['现金','冻结资金','在途资金']
    },
    series: [
      {
        name:'资金构成',
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
          {value:100, name:'现金'},
          {value:450, name:'冻结资金'},
          {value:300, name:'在途资金'}
        ]
      }
    ],
    color: config.colors
  }
}