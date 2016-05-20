/**
 * 配置项，提供全局使用各项配置
 * amd模块，使用requirejs载入
 */

define(function () {
//  this.host = 'http://www.guohuaigroup.com'
  this.host = ''
  
  return {
    host: this.host,
    /**
     * api 接口提供与服务器异步交互地址
     * 
     */
    api: {
			yup: this.host + '/yup',                                       // 文件上传服务

      dictList: this.host + '/ams/dict/list',                // 枚举值获取接口
    	establish: this.host + "/ams" + "/boot/investmentPool/establish", // 标的成立
    	unEstablish: this.host + "/ams" + "/boot/investmentPool/unEstablish", // 标的不成立
    	listinvestment: this.host + "/ams/boot/investmentPool/listinvestment", // 投资标的库列表
    	listCashTool: this.host + "/ams/boot/cashToolPool/listCashTool", // 现金管理工具库列表
    	removeCashTool: this.host + "/ams/boot/cashToolPool/removeCashTool", // 现金管理工具移除出库
    	targetIncomeSave: this.host + "/ams/boot/investmentPool/targetIncomeSave", // 投资标的本息兑付
    	targetProjectList:this.host + "/ams/boot/project/getByTargetId", // 查询投资标的下的底层项目
    	targetProjectDelete:this.host + "/ams/boot/project/deleteProject", // 删除投资标的下的底层项目
    	saveProject: this.host + "/ams/boot/project/save",//保存底层项目
    	targetListQuery: this.host + '/ams/target/targetManage/list', //标的列表查询
			targetDetQuery: this.host + '/ams/target/targetManage/detail', //标的详情查询
			targetAdd: this.host + '/ams/target/targetManage/add', //新建标的
			targetExamine: this.host + '/ams/target/targetManage/examine', //标的提交预审
			targetInvalid: this.host + '/ams/target/targetManage/invalid', //标的作废
			targetCheckListQuery: this.host + '/ams/target/targetCheck/list', //预审标的列表查询
			targetCheckPass: this.host + '/ams/target/targetCheck/checkpass', //标的预审通过
			targetCheckReject: this.host + '/ams/target/targetCheck/checkreject', //标的预审驳回
			productApplyList: this.host + "/ams/product/apply/list",//查询产品申请列表
			productAuditList: this.host + "/ams/product/audit/list",//查询产品审核列表
			productCheckList: this.host + "/ams/product/check/list",//查询产品复核列表
			productApproveList: this.host + "/ams/product/approve/list",//查询产品批准列表
    	savePeriodic: this.host + "/ams/product/save/periodic",//新加定期产品
    	saveCurrent: this.host + "/ams/product/save/current",//新加活期产品
    	updatePeriodic: this.host + "/ams/product/update/periodic",//更新定期产品
    	updateCurrent: this.host + "/ams/product/update/current",//更新活期产品
    	productDetail: this.host + "/ams/product/detail",//产品详情
    	productInvalid: this.host + "/ams/product/delete",//产品作废
    	productAuditApply: this.host + "/ams/product/aduit/apply",//产品审核申请
    	productAuditReject: this.host + "/ams/product/aduit/reject",//产品审核不通过
    	productAuditApprove: this.host + "/ams/product/aduit/approve",//产品审核通过
    	productReviewReject: this.host + "/ams/product/review/reject",//产品复核不通过
    	productReviewApprove: this.host + "/ams/product/review/approve",//产品复核通过
    	productAdmitReject: this.host + "/ams/product/admit/reject",//产品准入不通过
    	productAdmitApprove: this.host + "/ams/product/admit/approve",//产品准入通过
    	cashtoolListQuery: this.host + "/ams/boot/cashTool/list", //现金管理类工具列表查询
			cashtoolDetQuery: this.host + "/ams/boot/cashTool/detail", //现金管理类工具详情查询
			cashtoolAdd: this.host + "/ams/boot/cashTool/add", //新建现金管理类工具
			cashToolExamine: this.host + '/ams/target/cashTool/examine', //现金管理类工具提交审核
			meetingUser: this.host + '/operate/admin/search?system=GAH&rows=10',
			meetingTarget: this.host + '/ams/target/targetMeeting/targetList',
			meetingAdd: this.host + '/ams/target/targetMeeting/addMeeting',
			meetingList: this.host + '/ams/target/targetMeeting/list',
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
    			}
    		}
    	}
    },
    /**
     * targetStates 标的状态
     */
    targetStates: 
    	[{
			id: "waitPretrial",
			text: "未审核",
			children: []
		},{
			id: "pretrial",
			text: "预审中",
			children: []
		},{
			id: "waitMeeting",
			text: "未过会",
			children: []
		},{
			id: "metting",
			text: "过会中",
			children: []
		},{
			id: "collecting",
			text: "募集中",
			children: []
		},{
			id: "establish",
			text: "成立",
			children: []
		},{
			id: "unEstablish",
			text: "成立失败",
			children: []
		},{
			id: "reject",
			text: "驳回",
			children: []
		},{
			id: "overdue",
			text: "逾期",
			children: []
		},{
			id: "invalid",
			text: "作废",
			children: []
		}
    ],
    /**
     * conventionStates 过会状态
     */
    conventionStates: {
		},
		cashtoolStates: [{
			id: "waitPretrial",
			text: "未审核",
			children: []
		}, {
			id: "pretrial",
			text: "审核中",
			children: []
		}, {
			id: "checkpass",
			text: "审核通过",
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
			text: "未举行",
			children: []
		}],
		/**
		 * 图标所用到的主题颜色
		 */
		colors: ['#3c8dbc', '#dd4b39', '#f39c12', '#00a65a', '#00c0ef']
	}
})
