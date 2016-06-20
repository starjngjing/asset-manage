define([
  'http',
  'config',
  'extension'
], function (http, config, $$) {
  return {
    name: 'menu',
    init: function () {
      http.post(config.api.menu.load,{
        data: {
          system: 'MONEY'
        },
        contentType: 'form'
      }, function (result) {
        var columns = [
          {
            field: 'text'
          },
          {
            field: 'button',
            formatter: function (val) {
              var rlt = ''
              if (val) {
                val.forEach(function (item) {
                  rlt += item.text
                  rlt += ','
                })
                if (rlt) {
                  rlt = rlt.substring(0, rlt.length - 1)
                }
              }
              return rlt
            }
          },
          {
            width: 150,
            align: 'center',
            formatter: function (val, row) {
              if (row.action) {
                return '<div class="func-area">' +
                         '<a href="javascript:void(0)" class="item-update">编辑</a>' +
                         '<a href="javascript:void(0)" class="item-delete">删除</a>' +
                       '</div>'
              } else {
                return ''
              }
            },
            events: {
              'click .item-update': function (e, value, row) {
                console.log(row)
                var form = document.updateForm
                form.id.value = row.id
                form.text.value = row.text
                form.url.value = row.action.url
                $('#buttonTable').bootstrapTable({
                  data: row.button,
                  columns: [
                    {
                      field: 'text'
                    },
                    {
                      formatter: function () {
                        return '是'
                      }
                    },
                    {
                      field: 'role',
                      formatter: function (val) {
                        return val.substr(0, 10)
                      }
                    },
                    {
                      field: 'render'
                    },
                    {
                      field: 'action',
                      formatter: function (val) {
                        return val.eventtype
                      }
                    },
                    {
                      field: 'action',
                      formatter: function (val) {
                        return val.event
                      }
                    },
                    {
                      width: 100,
                      align: 'center',
                      formatter: function () {
                        return '<div class="func-area">' +
                        '<a href="javascript:void(0)" class="item-update">编辑</a>' +
                        '<a href="javascript:void(0)" class="item-delete">删除</a>' +
                        '</div>'
                      },
                      events: {
                        'click .item-update': function (e, value, row) {
                          operating.type = 'update'
                          operating.oid = row.oid
                          http.post(config.api.newsGet, {
                            data: {
                              noid: row.oid
                            },
                            contentType: 'form',
                          }, function (result) {
                            var form = document.eventForm
                            form.newsDate.value = result.newsDate
                            form.title.value = result.title
                            form.content.value = result.content
                            form.imgUrl.value = result.imgUrl
                            $('#eventModal')
                            .modal('show')
                            .find('.modal-title').html('修改事件')
                            $('#eventSubmit').html('修 改')
                            $(form).validator('validate')
                          })
                        },
                        'click .item-delete': function (e, value, row) {
                          $$.confirm({
                            container: $('#deleteConfirm'),
                            trigger: this,
                            accept: function () {
                              http.post(config.api.newsDelete, {
                                data: {
                                  noid: row.oid
                                },
                                contentType: 'form',
                              }, function (result) {
                                $('#eventTable').bootstrapTable('refresh')
                              })
                            }
                          })
                        }
                      }
                    }
                  ]
                })
                $('#updateModal').modal('show')
              }
            }
          }
        ]
        $$.treetableInit(result, $('#menuTable'), columns)
      })

      $('#buttonAdd').on('click', function () {
        $('#addButtonModal').modal('show')
      })
    }
  }
})
