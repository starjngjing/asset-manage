// 载入所需模块
define([
  'http',
  'config',
  'util',
  'extension'
],
function (http, config, util, $$) {
	return {
	  name: 'channelapply',
	  init: function (){
	  	var _this = this;
	  	_this.pagesInit();
	  	_this.bindEvent();
	  },
	  channApproveInfo:{
	  		apprOid: '',
	  		apprResult: ''
	  },
	  pagesInit: function () {
	  	var _this = this;
		  // 分页配置
		  var pageOptions = {
		    number: 1,
		    size: 10,
		    offset: 0,
		    channelName:'',
		    requester:'',
		    requestType:'',
		    approvelResult:'toApprove',
		    reqTimeBegin:'',
		    reqTimeEnd:''
		  }
		  var okPageOptions = {
		    number: 1,
		    size: 10,
		    offset: 0,
		    channelName:'',
		    requester:'',		    
		    requestType:'',
		    approvelResult:['pass','refused'],
		    reqTimeBegin:'',
		    reqTimeEnd:''
		  }
		  // 数据表格配置
		  var waitTableConfig = {
				ajax: function (origin) {
          http.post(config.api.chanApproveQuery, {
            data: {
              page: pageOptions.number,
              rows: pageOptions.size,
              channelName: pageOptions.channelName,
			        requester: pageOptions.requester,			       
			        requestType: pageOptions.requestType,
			        approvelResult: pageOptions.approvelResult,
			        reqTimeBegin: pageOptions.reqTimeBegin,
			        reqTimeEnd: pageOptions.reqTimeEnd
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
		    queryParams: getWaitQueryParams,
		    columns: [
		      {
		        width: 30,
		        align: 'center',
		        formatter: function (val, row, index) {
		          return pageOptions.offset + index + 1
		        }
		      },
		      {
            field: 'channelName'
         },          
          {
            field: 'requestType',
            formatter: function (val, row, index) {
              return	util.enum.transform('requestTypes', val);
            }
          },
          {
            field: 'requester'
          },
          {
            field: 'requestTime',
            formatter: function (val, row, index) {
              return	util.table.formatter.timestampToDate(val, 'YYYY-MM-DD HH:mm:ss')
            }
          },
		      {            
		        formatter: function (val, row, index) {	
		        	var buttons = [{
										text: '通过',
										type: 'button',
										class: 'item-confirm" data-type="pass"',
										isRender: true
									},
									{
										text: '驳回',
										type: 'button',
										class: 'item-confirm" data-type="refused"',
										isRender: true
									},
									{
										text: '渠道详情',
										type: 'button',
										class: 'detail',
										isRender: true
									}
		        	]
		        	return util.table.formatter.generateButton(buttons);
		        },
		        events:{
		        	'click .detail': getChannelInfo,
		        	'click .item-confirm':function(e, value, row){
		        		  $('#approveForm').clearForm()
					        .find('input[type=hidden]').val('')
					        $('#approveForm').validator('validate')
		        		  var txt = row.requestType == 'on'? '开启':'停用';
									$("#content").html($(this).html() + row.requester + '的' + txt);
									_this.channApproveInfo.apprOid = row.oid;
									_this.channApproveInfo.apprResult = $(this).attr('data-type');
									$('#approvetModal').modal('show');
		        	}
		        }
		       
		      }
		    ]
		  }
		  var okTableConfig = {
				ajax: function (origin) {
          http.post(util.buildQueryUrl(config.api.chanApproveQuery,{
              page: okPageOptions.number,
              rows: okPageOptions.size,
              channelName: okPageOptions.channelName,
			        requester: okPageOptions.requester,			        
			        requestType: okPageOptions.requestType,
			        approvelResult: okPageOptions.approvelResult,
			        reqTimeBegin: okPageOptions.reqTimeBegin,
			        reqTimeEnd: okPageOptions.reqTimeEnd
            }), function (rlt) {
            origin.success(rlt)
          })
        },
        idField:'oid',
		    pagination: true,
		    sidePagination: 'server',
		    pageList: [10, 20, 30, 50, 100],
		    queryParams: getOkQueryParams,
		    columns: [
		      {
		        width: 30,
		        align: 'center',
		        formatter: function (val, row, index) {
		          return okPageOptions.offset + index + 1
		        }
		      },
		      {
            field: 'channelName'
          },         
          {
            field: 'requestType',
            formatter: function (val, row, index) {
              return	util.enum.transform('requestTypes', val);
            }
          },
          {
            field: 'requester'
          },
          {
            field: 'requestTime',
            formatter: function (val, row, index) {
              return	util.table.formatter.timestampToDate(val, 'YYYY-MM-DD HH:mm:ss')
            }
          },
          {
            field: 'updateTime',
            formatter: function (val, row, index) {
              return	util.table.formatter.timestampToDate(val, 'YYYY-MM-DD HH:mm:ss')
            }
          },
          {
            field: 'approvelMan'
          },
          {
            field: 'approvelResult',
            formatter: function(val, row, index){
            	return util.enum.transform('approveStatus', val);
            }
          },
		      {            
		        formatter: function (val, row, index) {		
		        	var buttons = [{
										text: '审核意见',
										type: 'button',
										class: 'remark',
										isRender: true
									},									
									{
										text: '渠道详情',
										type: 'button',
										class: 'detail',
										isRender: true
									}
		        	]
		        	return util.table.formatter.generateButton(buttons);
		        },
		        events:{
		        	'click .detail': getChannelInfo,
		        	'click .remark': function(e, value, row){
		        		$('#remark').html('时间：' + util.table.formatter.timestampToDate(row.updateTime, 'YYYY-MM-DD HH:mm:ss')
		        		+ '&nbsp;&nbsp;&nbsp;&nbsp;审核意见：' + row.remark);
		        		$('#remarkModal').modal('show')
		        	}
		        }
		      }
		    ]   
		  }

			
		  // 初始化数据表格
		  $('#waitTable').bootstrapTable(waitTableConfig);
		  $('#okTable').bootstrapTable(okTableConfig);
		  $$.searchInit($('#waitSearchForm'), $('#waitTable'));
		  $$.searchInit($('#okSearchForm'), $('#okTable'));
			
			$('#approveForm').validator();
			
			//待审核查询
			function getWaitQueryParams(val){
				 var form = document.waitSearchForm
        // 分页数据赋值
        pageOptions.size = val.limit
        pageOptions.number = parseInt(val.offset / val.limit) + 1
        pageOptions.offset = val.offset
        pageOptions.channelName = form.channelName.value.trim()
			  pageOptions.requester = form.requester.value.trim()			  
			  pageOptions.requestType = form.requestType.value
			  pageOptions.reqTimeBegin = form.reqTimeBegin.value	
			  pageOptions.reqTimeEnd = form.reqTimeEnd.value
        return val
			}
			//已审核查询
			function getOkQueryParams(val){
				 var form = document.okSearchForm
        // 分页数据赋值
        okPageOptions.size = val.limit
        okPageOptions.number = parseInt(val.offset / val.limit) + 1
        okPageOptions.offset = val.offset
        okPageOptions.channelName = form.channelName.value.trim()
			  okPageOptions.requester = form.requester.value.trim()			  
			  okPageOptions.requestType = form.requestType.value
			  okPageOptions.reqTimeBegin = form.reqTimeBegin.value	
			  okPageOptions.reqTimeEnd = form.reqTimeEnd.value
        return val
			}
			//获取渠道详情
			function getChannelInfo(e, value, row){				
      	http.post(config.api.channelinfo, {
          data: {
            oid: row.channelOid
          },
          contentType: 'form',
        }, function (result) {
            $$.detailAutoFix($('#detailForm'), result); // 自动填充详情	   
            $('#detailModal').modal('show');          
        })      
			}
		},
		bindEvent:function(){
			var _this = this;
			
			//审批操作按钮
			$("#approveBut").on('click',function(){
				  document.approveForm.apprOid.value = _this.channApproveInfo.apprOid;
				  document.approveForm.apprResult.value = _this.channApproveInfo.apprResult;
				  $('#approveForm').ajaxSubmit({
              url: config.api.delApply,
              success: function (res) {
		          	if(res.errorCode==0){
		          		 $('#approvetModal').modal('hide')
		            	 $('#waitTable').bootstrapTable('refresh')
		            	 $('#okTable').bootstrapTable('refresh')
		          	}else{
		          		errorHandle(res);
		          	}
          }
        })
			})
		} 
 }
})
