/**
 * 渠道管理
 */
define([
  'util',
  'extension'
], function (util, $$) {
  return {
    name: 'channel',
    init: function () {
      // js逻辑写在这里
      var from = [{
        name: 1
      }, {
        name: 1
      }, {
        name: 1
      }, {
        name: 1
      }, {
        name: 1
      }]
      var to = [{
        name: 1
      }, {
        name: 1
      }, {
        name: 1
      }, {
        name: 1
      }, {
        name: 1
      }]
      $$.switcher({
        container: $('#switcher'),
        fromArray: from,
        toArray: to,
        field: 'name',
        formatter: function (val) {
          return val + ' name'
        }
      })
    }
  }
})
