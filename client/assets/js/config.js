/**
 * 配置项，提供全局使用各项配置
 * amd模块，使用requirejs载入
 */
 
define(function () {
  this.host = 'http://www.guohuaigroup.com'
  
  return {
    host: this.host,
    /**
     * api 接口提供与服务器异步交互地址
     */
    api: {
      login: this.host + '/operate/admin/login',                     // 登录
      logout: this.host + '/operate/admin/logout',                   // 登出
      userInfo: this.host + '/operate/admin/info',                   // 登录用户信息
      yup: this.host + '/yup',                                       // 文件上传
      newsQuery: this.host + '/ghweb/boot/news/query',               // 大事件-后台查询
      newsGet: this.host + '/ghweb/boot/news/getNews',               // 大事件-获取单条
      newsAdd: this.host + '/ghweb/boot/news/add',                   // 大事件-新增
      newsUpdate: this.host + '/ghweb/boot/news/update',             // 大事件-修改
      newsDelete: this.host + '/ghweb/boot/news/del',                // 大事件-删除
      newsHistory: this.host + '/ghweb/boot/newshis/query',          // 大事件-历史操作

      applyQuery: this.host + '/ghweb/boot/apply/query',             // 申请-申请记录
      applyGetCount: this.host + '/ghweb/boot/apply/getCount',       // 申请-获取统计数据
      applyUpdateOpenState: this.host + '/ghweb/boot/apply/updateOpenState',  // 申请-更新开通状态
      applyGetUserInfo: this.host + '/ghweb/boot/apply/getUserInfo', // 申请-获取用户信息

      dataGetData: this.host + '/ghweb/boot/data/getData',           // 数据-获取页面展示数据
      dataQuery: this.host + '/ghweb/boot/datahis/query',            // 数据-列表
      dataUpdateData: this.host + '/ghweb/boot/data/updateData',     // 数据-更新数据

      menuLoad: this.host + '/operate/system/menu/load',             // 菜单-加载菜单数据
    }
  }
})