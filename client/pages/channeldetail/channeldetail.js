// 载入所需模块
define([
  'http',
  'config',
  'util',
  'extension'
],
function (http, config, util, $$) {
	return {
	  name: 'channeldetail',
	  init: function (){
	  	var that = this;
	  	that.pagesInit();
	  	that.bindEvent();
	  },
	  channelInfo: {
	  	oid: ''
	  },
	  //根据oid获取渠道
		getChanByOid: function(val){
			  var _this = this;
				http.post(config.api.channelinfo, {
	          data: {
	            oid: val
	          },
	          contentType: 'form',
	        }, function (result) {
	              _this.getchannelInfo(result)
	    	})
		},
		//获取渠道详情
		getchannelInfo: function(result){
			  var _this = this;
			  _this.channelInfo.oid = result.oid;
        $$.detailAutoFix($('#detailForm'), result); // 自动填充详情
        if(result.channelStatus=='off' && result.deleteStatus=='no'){
        	$('#editChanBut').show();
        }else{
        	$('#editChanBut').hide();
        }
		},
	  pagesInit: function () {
	  	var _this = this;
	  	//默认获取一条渠道信息
	  	http.post(config.api.oneChannel, function (result) {
	  		      _this.getchannelInfo(result)     
		  })
	  	
			var dataobj1 = {"d1":"prd2016000000","d2":"产品名称","d3":"app","d4":"up","d5":"2016-5-16 14:46:23","d6":"open","d7":""};
			var dataobj2 = {"d1":"prd2016000001","d2":"产品名称","d3":"h5","d4":"down","d5":"2016-5-16 14:46:23","d6":"close","d7":""};
			var dataobj3 = {"d1":"prd2016000002","d2":"产品名称","d3":"pc","d4":"up","d5":"2016-5-16 14:46:23","d6":"open","d7":""};
			var data1=[];
			
			var dataobj4 = {"d1":201606060606,"d2":0,"d3":"2016-5-16 14:46:23","d4":"AAAA","d5":"13810888888","d6":"FIX_001","d7":""};
			var dataobj5 = {"d1":201606060606,"d2":1,"d3":"2016-5-16 14:46:23","d4":"AAAA","d5":"13810888888","d6":"FIX_001","d7":""};
			var dataobj6 = {"d1":201606060606,"d2":2,"d3":"2016-5-16 14:46:23","d4":"AAAA","d5":"13810888888","d6":"FIX_001","d7":""};
			var data2=[];
			
			var dataobj7 = {"d1":"13810888888","d2":"用户名","d3":"app","d4":"2016-5-16 14:46:23","d5":"2016-5-16 14:46:23","d6":"3000","d7":"3","d8":""};
			var dataobj8 = {"d1":"13810888888","d2":"用户名","d3":"h5","d4":"2016-5-16 14:46:23","d5":"2016-5-16 14:46:23","d6":"3000","d7":"2","d8":""};
			var dataobj9 = {"d1":"13810888888","d2":"用户名","d3":"pc","d4":"2016-5-16 14:46:23","d5":"2016-5-16 14:46:23","d6":"3000","d7":"4","d8":""};
			var data3=[];
			
			var dataobj10 = {"d1":201606060606,"d2":"渠道名称"};
			var dataobj11 = {"d1":201606060606,"d2":"渠道名称"};
			var dataobj12 = {"d1":201606060606,"d2":"渠道名称"};
			var data4=[]; 
			
			for(i = 0;i < 100; i++){
				var n = Math.ceil(Math.random()*3);
				
				if( n == 1){
					data1.push(dataobj1);
					data2.push(dataobj4);
					data3.push(dataobj7);
					data4.push(dataobj10);
				}
				if( n == 2){
					data1.push(dataobj2);
					data2.push(dataobj5);
					data3.push(dataobj8);
					data4.push(dataobj11);
				}
				if( n == 3){
					data1.push(dataobj3);
					data2.push(dataobj6);
					data3.push(dataobj9);
					data4.push(dataobj12);	
				}
			}
		  // 分页配置
		  var channelPageOptions = {
		    number: 1,
		    size: 10,
		    offset: 0,
		    channelName: ''
		  }
		  
		  var pageOptions = {
		    number: 1,
		    size: 10
		  }
		  
		  // 数据表格配置
		  var tableConfig1 = {
		
				data:data1,
		    pageNumber: pageOptions.number,
		    pageSize: pageOptions.size,
		    pagination: true,
		    //sidePagination: 'server',
		    pageList: [10, 20, 30, 50, 100],
		    //queryParams: getQueryParams,
		    columns: [
		      {
		        width: 30,
		        align: 'center',
		        formatter: function (val, row, index) {
		          return index + 1
		        }
		      },
		      {
		        field: 'd1',
		        formatter: function (val, row, index){
		          return val + index;
		        }
		      },
		      {
		        field: 'd2',
		        formatter: function (val, row, index) {
		          return val + (index+1);
		        }
		      },
		      {
		        field: 'd3',
		        formatter: function (val) {
		        	if(val == 'app'){
		        		val = 'APP';
		        	}
		        	if(val == 'h5'){
		        		val = '手机H5页面';
		        	}
		        	if(val == 'pc'){
		        		val = 'PC端页面';
		        	}
		          return val;
		        }
		      },
		      {
		        field: 'd4',
		        formatter: function (val) {
							if(val == "up"){
		        		val = '已上架';
		        	}
		        	if(val == "down"){
		        		val = '已下架';
		        	}
		          return val;
		        }
		      },
		      {
		        field: 'd5',
						formatter: function (val) {
		          return val;
		        }
		      },
		      {
		        field: 'd6',
		        formatter: function (val) {
		        	if(val == 'open'){
		        		val = '已启用';
		        	}
		        	if(val == 'close'){
		        		val = '已停用';
		        	}
		          return val;
		        }
		      },
		      {
		        field: 'd8',
		        formatter: function (val,row) {
		        	var _btns = '';
		        	
		        	if(row.d4 == "up"){
		        		_btns += '<a href="javascript:;" class="mr10 eventConfirm" data-txt="确定下架">下架</a>';
		        	}
		        	if(row.d4 == "down"){
		        		_btns += '<a href="javascript:;" class="mr10 eventConfirm" data-txt="确定上架">上架</a>'
		        	}
		        	_btns += '<a href="javascript:;" class="mr10">上架产品预览</a>';
		          _btns += '<a href="javascript:;" class="mr10">产品详情</a>';
		          return _btns;
		        }
		      }
		    ]
		  }
		  
		  var tableConfig2 = {
		
				data:data2,
		    pageNumber: pageOptions.number,
		    pageSize: pageOptions.size,
		    pagination: true,
		    //sidePagination: 'server',
		    pageList: [10, 20, 30, 50, 100],
		    //queryParams: getQueryParams,
		    columns: [
		      {
		        width: 30,
		        align: 'center',
		        formatter: function (val, row, index) {
		          return index + 1
		        }
		      },
		      {
		        field: 'd1',
		        formatter: function (val, row, index){
		          return val + index;
		        }
		      },
		      {
		        field: 'd2',
		        formatter: function (val, row, index) {
		        	if(val == 0){
		        		val = '充值';
		        	}
		        	if(val == 1){
		        		val = '申购';
		        	}
		        	if(val == 2){
		        		val = '赎回';
		        	}
		          return val;
		        }
		      },
		      {
		        field: 'd3',
		        formatter: function (val) {
	
		          return val;
		        }
		      },
		      {
		        field: 'd4',
		        formatter: function (val) {

		          return val;
		        }
		      },
		      {
		        field: 'd5',
						formatter: function (val) {
		          return val;
		        }
		      },
		      {
		        field: 'd6',
		        formatter: function (val) {

		          return val;
		        }
		      },
		      {
		        field: 'd7',
		        formatter: function (val,row) {
		        	var _btns = '';
		        	_btns += '<a href="javascript:;" class="mr10">订单详情</a>';
		          _btns += '<a href="javascript:;" class="mr10">产品详情</a>';
		          return _btns;
		        }
		      }
		    ]
		  }
		  
		  var tableConfig3 = {
		
				data:data3,
		    pageNumber: pageOptions.number,
		    pageSize: pageOptions.size,
		    pagination: true,
		    //sidePagination: 'server',
		    pageList: [10, 20, 30, 50, 100],
		    //queryParams: getQueryParams,
		    columns: [
		      {
		        width: 30,
		        align: 'center',
		        formatter: function (val, row, index) {
		          return index + 1
		        }
		      },
		      {
		        field: 'd1',
		        formatter: function (val, row, index){
		          return val + index;
		        }
		      },
		      {
		        field: 'd2',
		        formatter: function (val, row, index) {
		          return val + index;
		        }
		      },
		      {
		        field: 'd3',
		        formatter: function (val) {
		        	if(val == 'app'){
		        		val = 'APP';
		        	}
		        	if(val == 'h5'){
		        		val = '手机H5页面';
		        	}
		        	if(val == 'pc'){
		        		val = 'PC端页面';
		        	}
		          return val;
		        }
		      },
		      {
		        field: 'd4',
		        formatter: function (val) {
		          return val;
		        }
		      },
		      {
		        field: 'd5',
						formatter: function (val) {
		          return val;
		        }
		      },
		    
		      {
		        field: 'd6',
						formatter: function (val) {
		          return val;
		        }
		      },
		    
		      {
		        field: 'd7',
						formatter: function (val) {
		          return val;
		        }
		      },
		      {
		        field: 'd8',
		        formatter: function (val,row) {
		        	var _btns = '';
		          _btns += '<a href="javascript:;" class="mr10">投资人详情</a>';
		          return _btns;
		        }
		      }
		    ]
		  }
		  
		  var channelTableConfig = {				
		   ajax: function (origin) {
          http.post(config.api.channelQuery, {
            data: {
              page: channelPageOptions.number,
              rows: channelPageOptions.size,
              channelName: channelPageOptions.channelName
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
		    queryParams: getChannelQueryParams,
		    columns: [
		      {
		        width: 30,
		        align: 'center',
		        formatter: function (val, row, index) {
		          return channelPageOptions.offset + index + 1
		        }
		      },
		      {
            field: 'channelId'
          },
          {
            field: 'channelName'
          },
		      {
		        formatter: function (val, row, index) {		        	
		          return '<button type="button" class="btn btn-default check" data-dismiss="modal">选 择</button>';
		        },
		        events: {
		        	'click .check': function(e, value, row){
		        		_this.getChanByOid(row.oid)
		        	}
		        }
		      }
		    ]
		  }
	
		  // 初始化数据表格
		  $('#dataTable1').bootstrapTable(tableConfig1);
		  $('#dataTable2').bootstrapTable(tableConfig2);
		  $('#dataTable3').bootstrapTable(tableConfig3);
		  $('#channelTable').bootstrapTable(channelTableConfig);
			
			util.form.validator.init($('#channelForm'))
			$('#channelForm').validator();
			
			$(".eventConfirm").on("click",function(){
				var txt = $(this).attr("data-txt");
				$("#confirm_txt").html(txt);
				$$.confirm({
		      container: $('#eventModal3'),
		      trigger: this,
		      accept: function () {
		        
		      }
		    })
				//$('#eventModal3').modal('show');
			});
			
			function getChannelQueryParams (val) {
        // 分页数据赋值
        channelPageOptions.size = val.limit
        channelPageOptions.number = parseInt(val.offset / val.limit) + 1
        channelPageOptions.offset = val.offset
        channelPageOptions.channelName = $('#searchChannelName').val()
        return val
      }
			
		},
		bindEvent:function(){
			var _this = this;
			//编辑渠道
			$("#editChanBut").on("click",function(){
				http.post(config.api.channelinfo, {
          data: {
            oid: _this.channelInfo.oid
          },
          contentType: 'form',
        }, function (result) {
        	$('#editChanelName').attr('readonly',true);          
          $$.formAutoFix($('#channelForm'), result); // 自动填充详情                 
          $('#channelForm').validator('validate')
        })
				$('#channelModal').modal('show');
			});
			
			//切换渠道
			$("#checkChanBut").on("click",function(){
				$('#channelsModal').modal('show');
			});
			
			//按条件搜索
			$("#channelSearchBtn").on("click",function(){
				$('#channelTable').bootstrapTable('refresh');
			});
			
			//修改渠道
			$("#channelSubmit").on("click",function(){
				document.channelForm.oid.value = _this.channelInfo.oid
				$('#channelForm').ajaxSubmit({
          url: config.api.editChannel,
          success: function (res) {
          	if(res.errorCode==0){
          		 $('#channelModal').modal('hide')
          		 _this.getChanByOid(_this.channelInfo.oid);
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
