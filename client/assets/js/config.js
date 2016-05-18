/**
 * 配置项，提供全局使用各项配置
 * amd模块，使用requirejs载入
 */
 
define(function () {
//  this.host = 'http://www.guohuaigroup.com'
  this.host = 'http://127.0.0.1'
  
  return {
    host: this.host,
    /**
     * api 接口提供与服务器异步交互地址
     */
    api: {
      dictList: this.host + '/ams/dict/list',                // 枚举值获取接口
    	establish: this.host + "/ams" + "/boot/investmentPool/establish",
    	listinvestment: this.host + "/ams/boot/investmentPool/listinvestment"
    },
    /**
     * targetStates 标的状态
     */
    targetStates: {

    },
    /**
     * conventionStates 过会状态
     */
    conventionStates: {

    }
  }
})