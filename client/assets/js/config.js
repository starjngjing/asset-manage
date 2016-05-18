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
      dictList: this.host + '/ams/dict/list',                // 枚举值获取接口
    	establish: this.host + "/ams" + "/boot/investmentPool/establish",
    	listinvestment: this.host + "/ams/boot/investmentPool/listinvestment",
    	targetListQuery: this.host + '/ams/target/targetManage/list', //标的列表查询
		targetDetQuery: this.host + '/ams/target/targetManage/detail', //标的详情查询
		targetAdd: this.host + '/ams/target/targetManage/add', //新建标的
		productApplyList: this.host + "/ams/product/apply/list",//查询产品申请列表
		productAuditList: this.host + "/ams/product/audit/list",//查询产品审核列表
		productCheckList: this.host + "/ams/product/check/list",//查询产品复核列表
		productApproveList: this.host + "/ams/product/approve/list",//查询产品批准列表
    	savePeriodic: this.host + "/ams/product/save/periodic",//新加定期产品
    	saveCurrent: this.host + "/ams/product/save/current"//新加活期产品
    },
    /**
     * targetStates 标的状态
     */
    targetStates: {
		name: ['待预审', '预审中', '待过会', '过会中', '募集中', '成立', '成立失败', '驳回', '逾期', '作废'], //标的状态
		value: ['waitPretrial', 'pretrial', 'waitMeeting', 'metting', 'collecting', 'establish', 'unEstablish', 'reject', 'overdue', 'invalid'] //标的状态
    },
    /**
     * conventionStates 过会状态
     */
    conventionStates: {

    },
    /**
     * 图标所用到的主题颜色
     */
    colors: ['#3c8dbc', '#dd4b39', '#f39c12', '#00a65a', '#00c0ef']
  }
})