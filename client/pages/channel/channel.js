// 载入所需模块
define([
  'http',
  'config',
  'util',
  'extension'
],
function (http, config, util, $$) {
	return {
	  name: 'channel',
	  init: function (){
	  	var _this = this;	  	
	  	_this.pagesInit();
	  	_this.bindEvent();
	  },
	  channelInfo:{
	  	oid:''
	  },
	  pagesInit: function () {
	  	var _this = this;
		  // 分页配置
		  var pageOptions = {
        number: 1,
        size: 10,
        offset: 0,
        channelName: '',
        channelStatus: '',
        delStatus: 'no'
      }
		  var confirm = $('#confirmModal');
		  // 数据表格配置
		  var tableConfig = {
				ajax: function (origin) {
          http.post(config.api.channelQuery, {
            data: {
              page: pageOptions.number,
              rows: pageOptions.size,
              channelName: pageOptions.channelName,	
              channelStatus: pageOptions.channelStatus,
			        delStatus: pageOptions.delStatus
            },
            contentType: 'form'
          }, function (rlt) {
            origin.success(rlt)
          })
        },
        idField:'oid',
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
            field: 'channelId'
          },
          {
            field: 'channelName'
          },
          {
            field: 'joinType',
            formatter: function (val, row, index) {
              return	util.enum.transform('joinTypes', val);
            }
          },
          {
            field: 'partner'
          },
          {
            field: 'channelContactName'
          },
          {
            field: 'channelFee',
            formatter: function(val){
            	return val + '%'
            }
          },
          {
            field: 'channelStatus',
            formatter: function (val, row, index) {
              return util.enum.transform('channelStatus', val);
            }
          },
          {
            field: 'approvelStatus',
            formatter: function (val, row, index) {
             return util.enum.transform('approveStatus', val);
            }
          },          
		      {            
		        formatter: function (val, row, index) {		        	
		        	var buttons = [{
										text: '申请停用',
										type: 'button',
										class: 'apply',
										isRender: row.channelStatus == 'on'
									},
									{
										text: '申请启用',
										type: 'button',
										class: 'apply',
										isRender: row.channelStatus == 'off'
									},
									{
										text: '渠道详情',
										type: 'button',
										class: 'detail',
										isRender: true
									},
									{
										text: '审核意见',
										type: 'button',
										class: 'remarks',
										isRender: true
									},
									{
										text: '编辑',
										type: 'button',
										class: 'edit',
										isRender: row.channelStatus=='off' && row.deleteStatus=='no'
									},
									{
										text: '删除',
										type: 'button',
										class: 'del',
										isRender: row.channelStatus=='off' && row.deleteStatus=='no'
									}
		        	]
		        	return util.table.formatter.generateButton(buttons);  
		        },
		        events: {
              'click .apply': function (e, value, row) {
              	var applyType = row.channelStatus == 'on' ? 'off':'on';                         	              	           
              	confirm.find('.popover-title').html('渠道状态申请')
              	confirm.find('p').html($(this).html()+'?');
              	$$.confirm({
						      container: confirm,
						      trigger: this,
						      accept: function () {
						        http.post(config.api.setapply,{
						        	data:{
						        		oid:row.oid,
						        		requestType:applyType
						        	},
						        	contentType: 'form',
						        }, function(res){
						        	if(res.errorCode==0){
					          		 confirm.modal('hide')
					            	 $('#channelTable').bootstrapTable('refresh')
					          	}else{
					          		errorHandle(res);
					          	}
						        })
						      }
						    })
              },
              'click .detail': function (e, value, row) {
              	http.post(config.api.channelinfo, {
                  data: {
                    oid: row.oid
                  },
                  contentType: 'form',
                }, function (result) {
                	 $$.detailAutoFix($('#detailForm'), result); // 自动填充详情	                    
	                 $('#detailModal').modal('show'); 
                })
              	                	
              },
              'click .remarks': function (e, value, row) {
              	  http.post(config.api.remarksQuery,{
						        	data:{
						        		oid:row.oid
						        	},
						        	contentType: 'form',
						        }, function(res){
						        	if(res.errorCode==0){
						        		  var remarks = '';
						        		  var rows = res.rows;
					          		  for(var idx in rows){
					          		  	var remark = rows[idx].remark == null ? '': rows[idx].remark;
					          		  	remarks += '时间：' + util.table.formatter.timestampToDate(rows[idx].time, 'YYYY-MM-DD HH:mm:ss') 
					          		  	+ '&nbsp;&nbsp;&nbsp;&nbsp;审核意见：' + remark + '</br>';
					          		  }
					          		  $("#remarks").html(remarks);
					            	  $('#remarksModal').modal('show');
					          	}else{
					          		errorHandle(res);
					          	}
						      })              	                	
              },
              'click .edit': function (e, value, row) {
              	_this.channelInfo.oid = row.oid;              
								http.post(config.api.channelinfo, {
                  data: {
                    oid: row.oid
                  },
                  contentType: 'form',
                }, function (result) {
                	$('#editChanelName').attr('readonly',true);                  
                  $$.formAutoFix($('#channelForm'), result); // 自动填充详情
                  //$('#channelForm').validator('validate')
                })
								$('#channelModal').modal('show')
								.find('.modal-title').html("修改渠道"); 
              },
              'click .del': function (e, value, row) {
              	confirm.find('.popover-title').html('提示')
              	confirm.find('p').html('确定删除此条数据？');
              	$$.confirm({
						      container: confirm,
						      trigger: this,
						      accept: function () {
						        http.post(config.api.delChannel,{
						        	data:{
						        		oid:row.oid
						        	},
						        	contentType: 'form',
						        }, function(res){
						        	if(res.errorCode==0){
					          		 confirm.modal('hide')
					            	 $('#channelTable').bootstrapTable('refresh')
					          	}else{
					          		errorHandle(res);
					          	}
						        })
						      }
						    })
              }
            }
		      }
		    ]
		  }
		  // 初始化数据表格
		  $('#channelTable').bootstrapTable(tableConfig);
		  $$.searchInit($('#searchForm'), $('#channelTable'));
		  $('#channelForm').validator();
		  
		  function getQueryParams (val) {
        // 参数 val 是bootstrap-table默认的与服务器交互的数据，包含分页、排序数据
        var form = document.searchForm
        // 分页数据赋值
        pageOptions.size = val.limit
        pageOptions.number = parseInt(val.offset / val.limit) + 1
        pageOptions.offset = val.offset
        pageOptions.channelName = form.channelName.value.trim()			 
			  pageOptions.channelStatus = form.channelStatus.value			 
        return val
      }		 
		},
		bindEvent:function(){
			var _this = this;
			
			//将新增修改渠道页面显示出来
			$("#addChannel").on("click",function(){
				$('#editChanelName').attr('readonly',false);
				 $('#channelForm').clearForm()
        .find('input[type=hidden]').val('')
        _this.channelInfo.oid='';
				//$(document.channelForm).validator('validate')
				$('#channelModal').modal('show')
				.find('.modal-title').html("新建渠道");
			});
			
			//新增渠道
			$("#channelSubmit").on("click",function(){
				var url = '';
				if(_this.channelInfo.oid){
					url = config.api.editChannel;
					document.channelForm.oid.value = _this.channelInfo.oid
				}else{
					url = config.api.addChannel;					
				}
				$('#channelForm').ajaxSubmit({
          url: url,
          success: function (res) {
          	if(res.errorCode==0){
          		 $('#channelModal').modal('hide')
            	 $('#channelTable').bootstrapTable('refresh')
          	}else{
          		errorHandle(res);
          	}
          }
        })
			});
			
		}
	  
 }
})
