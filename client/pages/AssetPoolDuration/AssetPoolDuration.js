/**
 * 资产池存续期管理
 */
define([
  'config',
  'util'
], function (config, util) {
  return {
    name: 'AssetPoolDuration',
    init: function () {
      // js逻辑写在这里

      // 饼图生成
      var pieChart = echarts.init(document.getElementById('pieChart'))
      pieChart.setOption(getPieOptions(config))
      // 柱状图生成
      var barChart = echarts.init(document.getElementById('barChart'))
      barChart.setOption(getBarOptions(config))
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