/**
 * 配置项，提供全局使用各项配置
 * amd模块，使用requirejs载入
 */
define(function() {
	//  this.host = 'http://api.guohuaigroup.com'
	this.host = ''
	return {
		host: this.host,
		/**
		 * api 接口提供与服务器异步交互地址
		 * 
		 */
		api: {
			login: 'http://api.guohuaigroup.com/operate/admin/login',			// 登录服务
			logout: 'http://api.guohuaigroup.com/operate/admin/logout',		// 登出服务
			userInfo: 'http://api.guohuaigroup.com/operate/admin/info',   // 登录用户信息服务
			yup: 'http://api.guohuaigroup.com/yup', // 文件上传服务
			dictList: this.host + '/ams/dict/list', // 枚举值获取接口
			establish: this.host + "/ams" + "/boot/investmentPool/establish", // 标的成立
			unEstablish: this.host + "/ams" + "/boot/investmentPool/unEstablish", // 标的不成立
			overdue: this.host + "/ams" + "/boot/investmentPool/overdue", // 标的逾期
			listinvestmentPoolList: this.host + "/ams/boot/investmentPool/poolList", // 投资标的库列表(未持有投资标的列表,已持有投资标的列表,历史投资标的列表)
			listCashTool: this.host + "/ams/boot/cashToolPool/listCashTool", // 现金管理工具库列表
			removeCashTool: this.host + "/ams/boot/cashToolPool/removeCashTool", // 现金管理工具移除出库
			cashToolRevenueSave: this.host + "/ams/boot/cashToolPool/cashToolRevenueSave", // 现金管理工具收益采集
			targetIncomeSave: this.host + "/ams/boot/investmentPool/targetIncomeSave", // 投资标的本息兑付
			targetProjectList: this.host + "/ams/boot/project/projectlist", // 查询投资标的下的底层项目
			targetProjectDelete: this.host + "/ams/boot/project/deleteProject", // 删除投资标的下的底层项目
			saveProject: this.host + "/ams/boot/project/save", //保存底层项目
			targetListQuery: this.host + '/ams/target/targetManage/list', //标的列表查询
			targetDetQuery: this.host + '/ams/target/targetManage/detail', //标的详情查询
			targetAdd: this.host + '/ams/target/targetManage/add', //新建标的
			targetExamine: this.host + '/ams/target/targetManage/examine', //标的提交预审
			targetInvalid: this.host + '/ams/target/targetManage/invalid', //标的作废
			targetCheckListQuery: this.host + '/ams/target/targetCheck/list', //预审标的列表查询
			targetCheckPass: this.host + '/ams/target/targetCheck/checkpass', //标的预审通过
			targetCheckReject: this.host + '/ams/target/targetCheck/checkreject', //标的预审驳回
			productApplyList: this.host + "/ams/product/apply/list", //查询产品申请列表
			productAuditList: this.host + "/ams/product/audit/list", //查询产品审核列表
			productCheckList: this.host + "/ams/product/check/list", //查询产品复核列表
			productApproveList: this.host + "/ams/product/approve/list", //查询产品批准列表
			savePeriodic: this.host + "/ams/product/save/periodic", //新加定期产品
			saveCurrent: this.host + "/ams/product/save/current", //新加活期产品
			updatePeriodic: this.host + "/ams/product/update/periodic", //更新定期产品
			updateCurrent: this.host + "/ams/product/update/current", //更新活期产品
			productDetail: this.host + "/ams/product/detail", //产品详情
			productInvalid: this.host + "/ams/product/delete", //产品作废
			productAuditApply: this.host + "/ams/product/aduit/apply", //产品审核申请
			productAuditReject: this.host + "/ams/product/aduit/reject", //产品审核不通过
			productAuditApprove: this.host + "/ams/product/aduit/approve", //产品审核通过
			productReviewReject: this.host + "/ams/product/review/reject", //产品复核不通过
			productReviewApprove: this.host + "/ams/product/review/approve", //产品复核通过
			productAdmitReject: this.host + "/ams/product/admit/reject", //产品准入不通过
			productAdmitApprove: this.host + "/ams/product/admit/approve", //产品准入通过
			productChooseChannelList: this.host + "/ams/product/channel/choose/list", //产品选择渠道列表
			saveProductChannel: this.host + "/ams/product/channel/save/list", //保存产品已经选择渠道列表
			productChannelList: this.host + "/ams/product/channel/list", //该渠道的产品列表
			productChannelUpshelf: this.host + "/ams/product/channel/upshelf", //上架产品
			productChannelOffshelf: this.host + "/ams/product/channel/donwshelf", //下架产品
			cashtoolListQuery: this.host + "/ams/boot/cashTool/list", //现金管理类工具列表查询
			cashtoolDetQuery: this.host + "/ams/boot/cashTool/detail", //现金管理类工具详情查询
			cashtoolAdd: this.host + "/ams/boot/cashTool/add", //新建现金管理类工具
			cashToolExamine: this.host + '/ams/boot/cashTool/examine', //现金管理类工具提交审核
			cashToolInvalid: this.host + '/ams/boot/cashTool/invalid', //现金管理类工具作废
			cashToolAccessList: this.host + '/ams/boot/cashTool/accessList', //现金管理类工具审核列表
			cashToolCheckpass: this.host + '/ams/boot/cashTool/checkpass', //现金管理类工具审核通过
			cashToolCheckreject: this.host + '/ams/boot/cashTool/checkreject', //现金管理类工具审核驳回
			cashToolEdit: this.host + '/ams/boot/cashTool/edit', //现金管理类工具编辑		
			meetingUser: this.host + '/operate/admin/search?system=GAH&rows=10', //参会人池
			meetingTarget: this.host + '/ams/target/targetMeeting/targetList', //未过会标的池
			meetingAdd: this.host + '/ams/target/targetMeeting/addMeeting', //新建过会
			meetingList: this.host + '/ams/target/targetMeeting/list', //过会列表
			meetingDetail: this.host + '/ams/target/targetMeeting/detail', //过会详情
			meetingTargetList: this.host + '/ams/target/targetMeeting/meetingTarget', //过会中标的列表
			meetingTargetVoteDet: this.host + '/ams/target/targetMeeting/meetingTargetVoteDet', //过会中标的投票情况
			meetingSummaryUp: this.host + '/ams/target/targetMeeting/summaryUp', //上传会议纪要
			meetingSummaryDet: this.host + '/ams/target/targetMeeting/summaryDet', //获得会议纪要详情
			meetingOpen: this.host + '/ams/target/targetMeeting/open', //获得会议纪要详情
			meetingStop: this.host + '/ams/target/targetMeeting/stop', //获得会议纪要详情
			meetingSummaryDelete: this.host + '/ams/target/targetMeeting/summaryDetele', //删除会议纪要
			meetingFinish: this.host + '/ams/target/targetMeeting/finish', //会议完成
			channelQuery: this.host + '/ams/channel/query',              //渠道-列表查询
		    addChannel: this.host + '/ams/channel/add', 				 //渠道-新增
		    channelinfo: this.host + '/ams/channel/channelinfo',		 //渠道-获取详情
		    editChannel: this.host + '/ams/channel/edit',			     //渠道-修改
		    delChannel: this.host + '/ams/channel/delete',               //渠道-删除
		    setapply: this.host + '/ams/channel/setapply',			     //渠道-申请开启停用
		    remarksQuery: this.host + '/ams/channel/remarksquery',	     //渠道-意见列表
		    chanApproveQuery: this.host + '/ams/channelapprove/query',   //渠道-渠道审批查询
		    delApply: this.host + '/ams/channelapprove/dealapply',       //渠道-处理申请开启和停用
		    channelQuery: this.host + '/ams/channel/query', //渠道-列表查询
			addChannel: this.host + '/ams/channel/add', //渠道-新增
			channelinfo: this.host + '/ams/channel/channelinfo', //渠道-获取详情
			oneChannel: this.host + '/ams/channel/onechannel',  //渠道-随机获取一条渠道信息
			editChannel: this.host + '/ams/channel/edit', //渠道-修改
			delChannel: this.host + '/ams/channel/delete', //渠道-删除
			setapply: this.host + '/ams/channel/setapply', //渠道-申请开启停用
			remarksQuery: this.host + '/ams/channel/remarksquery', //渠道-意见列表
			chanApproveQuery: this.host + '/ams/channelapprove/query', //渠道-渠道审批查询
			delApply: this.host + '/ams/channelapprove/dealapply', //渠道-处理申请开启和停用
		    voteTargetList: this.host + '/ams/target/targetVote/list',   //过会表决标的列表
		    voteTarget: this.host + '/ams/target/targetVote/vote',   //过会标的表决
			files:{
				pkg: this.host + '/ams/file/pkg', //获得下载key
				download: this.host + '/ams/file/dl?key=' //下载附件包 参数key
			},
			system: {
				config: {
					ccp: {
						warrantor: {
							create: this.host + "/ams/system/ccp/warrantor/create",
							update: this.host + "/ams/system/ccp/warrantor/update",
							delete: this.host + "/ams/system/ccp/warrantor/delete",
							search: this.host + "/ams/system/ccp/warrantor/search"
						},
						warrantyMode: {
							create: this.host + "/ams/system/ccp/warrantyMode/create",
							update: this.host + "/ams/system/ccp/warrantyMode/update",
							delete: this.host + "/ams/system/ccp/warrantyMode/delete",
							search: this.host + "/ams/system/ccp/warrantyMode/search"
						},
						warrantyExpire: {
							create: this.host + "/ams/system/ccp/warrantyExpire/create",
							update: this.host + "/ams/system/ccp/warrantyExpire/update",
							delete: this.host + "/ams/system/ccp/warrantyExpire/delete",
							search: this.host + "/ams/system/ccp/warrantyExpire/search"
						}
					},
					ccr: {
						cate: {
							options: this.host + "/ams/system/ccr/cate/options"
						},
						indicate: {
							search: this.host + "/ams/system/ccr/indicate/search",
							save: this.host + "/ams/system/ccr/indicate/save",
							enable: this.host + "/ams/system/ccr/indicate/enable",
							disable: this.host + "/ams/system/ccr/indicate/disable",
							delete: this.host + "/ams/system/ccr/indicate/delete",
							options: this.host + "/ams/system/ccr/indicate/options",
							collect: {
								save: this.host + "/ams/system/ccr/indicate/collect/save",
								preUpdate: this.host + "/ams/system/ccr/indicate/collect/preUpdate"
							}
						},
						options: {
							save: this.host + "/ams/system/ccr/options/save",
							showview: this.host + "/ams/system/ccr/options/showview",
							batchDelete: this.host + "/ams/system/ccr/options/batchDelete",
							preUpdate: this.host + "/ams/system/ccr/options/preUpdate",
							preCollect: this.host + "/ams/system/ccr/options/preCollect",
						}
					}
				}
			},
			role: {
				list: this.host + '/operate/admin/ctrl/role/list'					// 角色列表
			},
			auth: {
				list: this.host + '/operate/admin/ctrl/auth/list'					// 权限列表
			}
		},

		/**
		 * targetStates 标的状态
		 */
		targetStates: [{
			id: "waitPretrial",
			text: "未审核",
			children: []
		}, {
			id: "pretrial",
			text: "预审中",
			children: []
		}, {
			id: "waitMeeting",
			text: "未过会",
			children: []
		}, {
			id: "metting",
			text: "过会中",
			children: []
		}, {
			id: "collecting",
			text: "募集中",
			children: []
		}, {
			id: "establish",
			text: "成立",
			children: []
		}, {
			id: "unEstablish",
			text: "成立失败",
			children: []
		}, {
			id: "reject",
			text: "驳回",
			children: []
		}, {
			id: "overdue",
			text: "逾期",
			children: []
		}, {
			id: "invalid",
			text: "作废",
			children: []
		}, {
			id: "meetingPass",
			text: "待准入",
			children: []
		}],
		/**
		 * conventionStates 过会状态
		 */
		conventionStates: [

		],
		cashtoolStates: [{
			id: "waitPretrial",
			text: "未审核",
			children: []
		}, {
			id: "pretrial",
			text: "审核中",
			children: []
		}, {
			id: "collecting",
			text: "募集期",
			children: []
		}, {
			id: "reject",
			text: "驳回",
			children: []
		}, {
			id: "invalid",
			text: "作废",
			children: []
		}],
		/*
		 * 是否下拉列表
		 */
		booleanSelect: [{
			id: "Y",
			text: "是",
			children: []
		}, {
			id: "N",
			text: "否",
			children: []
		}],
		meetingStates: [{
			id: "notopen",
			text: "未启动",
			children: []
		}, {
			id: "opening",
			text: "启动中",
			children: []
		}, {
			id: "stop",
			text: "暂停",
			children: []
		}, {
			id: "finish",
			text: "完成",
			children: []
		}],
		voteStates: [{
			id: "notvote",
			text: "未表决",
			children: []
		}, {
			id: "approve",
			text: "赞成",
			children: []
		}, {
			id: "notapprove",
			text: "不赞成",
			children: []
		}, {
			id: "notpass",
			text: "一票否决",
			children: []
		}],
		//接入类型
	    joinTypes:[
	    	{
				id: "ftp",
				text: "FTP文件",			
			},
			{
				id: "api",
				text: "API接口",
			}
	    ],
	    //渠道状态
	    channelStatus:[
	    	{
				id: "on",
				text: "已启用",			
			},
	        {
				id: "off",
				text: "已停用",			
			}    
	    ],
	    //删除状态
	    delStatus:[
	    	{
				id: "yes",
				text: "已删除",			
			},
	        {
				id: "no",
				text: "正常",			
			}
	   ],
	   //请求类型
	   requestTypes:[
	   		{
				id: "on",
				text: "开启",			
			},
	        {
				id: "off",
				text: "停用",			
			}
	   ],
	   //审批结果
	   approveStatus:[
	   		{
				id: "pass",
				text: "通过",			
			},
	        {
				id: "refused",
				text: "驳回",			
			},
	        {
				id: "toApprove",
				text: "待审批",			
			}   
	   ],
		/**
		 * 图标所用到的主题颜色
		 */
		colors: ['#3c8dbc', '#dd4b39', '#f39c12', '#00a65a', '#00c0ef']
	}
})