/**
 * 实用工具
 * amd模块，使用requirejs载入
 */

define([
	'config'
], function(config) {
	return {
		/**
		 * table操作实用工具
		 */
		table: {
			/**
			 * 常用 formatter
			 */
			formatter: {
				/**
				 * 图片url转成缩略图
				 */
				urlToThumb: function(val, position) {
					if (val) {
						position = position || 'left'
						return '<a href="http://www.guohuaigroup.com' + val + '" class="thumb-area" target="_blank">' +
							'<span class="thumb fa fa-picture-o text-light-blue"></span>' +
							'<img src="http://www.guohuaigroup.com' + val + '" class="thumb-' + position + '" />' +
							'</a>'
					} else {
						return ''
					}
				},
				/**
				 * timestamp转成时间格式字符串
				 */
				timestampToDate: function(val, format) {
					return moment(val).format(format)
				},
				/**
				 * 数字转成进度条
				 */
				numberToProgress: function(val) {
					val = parseInt(val * 100)
					if (val <= 30) {
						return '<div class="progress progress-xs progress-striped active">' +
							'<div class="progress-bar progress-bar-primary" style="width: ' + val + '%"></div>' +
							'</div>'
					} else if (val > 30 && val <= 60) {
						return '<div class="progress progress-xs progress-striped active">' +
							'<div class="progress-bar progress-bar-danger" style="width: ' + val + '%"></div>' +
							'</div>'
					} else if (val > 60 && val <= 99) {
						return '<div class="progress progress-xs progress-striped active">' +
							'<div class="progress-bar progress-bar-yellow" style="width: ' + val + '%"></div>' +
							'</div>'
					} else {
						return '<div class="progress progress-xs progress-striped active">' +
							'<div class="progress-bar progress-bar-success" style="width: ' + val + '%"></div>' +
							'</div>'
					}
				},
				/**
				 * 数字转成百分比显示
				 */
				numberToPercentage: function(val) {
					val = parseInt(val * 100)
					if (val <= 30) {
						return '<span class="badge bg-light-blue">' + val + '%</span>'
					} else if (val > 30 && val <= 60) {
						return '<span class="badge bg-red">' + val + '%</span>'
					} else if (val > 60 && val <= 99) {
						return '<span class="badge bg-yellow">' + val + '%</span>'
					} else {
						return '<span class="badge bg-green">' + val + '%</span>'
					}
				},
				/**
				 * 生成功能按钮
				 */
				generateButton: function(arr) {
					var format = '<div class="func-area">'
					arr.forEach(function(item) {
						switch (item.type) {
							case 'button':
								if (item.isRender === undefined || item.isRender) {
									format += '<button class="btn btn-default btn-xs ' + item.class + '">' + item.text + '</button>'
								}
								break
							case 'buttonGroup':
								if (item.isRender === undefined || item.isRender) {
									format += '<div class="btn-group ' + (item.isCloseBottom ? 'dropup' : '') + '">' +
										'<button class="btn btn-default btn-xs ' + item.class + '">' + item.text + '</button>' +
										'<button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">' +
										'<span class="caret"></span>' +
										'</button>' +
										'<ul class="dropdown-menu dropdown-menu-right" role="menu">'
									item.sub.forEach(function(sub) {
										if (sub.isRender === undefined || sub.isRender) {
											format += '<li><a href="javascript:void(0)" class="' + sub.class + '">' + sub.text + '</a></li>'
										}
									})
									format += '</ul></div>'
								}
								break
						}
					})
					format += '</div>'
					return format
				},
				/**
				 * 格式化风险等级
				 * @param {Object} val
				 */
				convertRisk: function(val) {
					var str = '',
						className = '';
					var wl = this.getWarrantyLevel(val);
					if (wl) {
						return this.convertRiskLevel(wl.wlevel, wl.name);
					} else {
						return this.convertRiskLevel(null);
					}
					return str;
				},
				/**
				 * 根据分数值获取风险等级配置项
				 * @param {Object} val
				 */
				getWarrantyLevel: function(val) {
					if (val === null || val === undefined || val.toString().trim() === '') return null;
					var levelOptions = config.warrantyLevelOptions;
					if (levelOptions) {
						for (var i = 0; i < levelOptions.length; i++) {
							var item = levelOptions[i];
							var f0 = item.coverLow,
								f1 = item.coverHigh;
							var min = item.lowFactor,
								max = item.highFactor;
							if (!max || max === '∞') {
								if (f0 === '[') {
									if (val >= min)
										return item;
								} else {
									if (val > min)
										return item;
								}
							} else if (!min || min === '∞') {
								if (f1 === ']') {
									if (val <= max)
										return item;
								} else {
									if (val < max)
										return item;
								}
							} else {
								if (f0 === '[') {
									if (f1 === ']') {
										if (min <= val && val <= max)
											return item;
									} else {
										if (min <= val && val < max)
											return item;
									}
								} else {
									if (f1 === ']') {
										if (min < val && val <= max)
											return item;
									} else {
										if (min < val && val < max)
											return item;
									}
								}
							}
						}
					}
					return null;
				},
				/**
				 * 格式化风险等级
				 * @param {Object} val  风险等级
				 * @param {Object} display 显示名称
				 */
				convertRiskLevel: function(val, display) {
					// 'LOW' || 'L'  低风险
					// 'MID' || 'M'  中风险
					// 'HIGH' || 'H' 高风险
					var str = '';
					var className = '';
					if (!val || val === 'NONE') {
						str = display || '无'
						className = 'bg-green'
					} else if (val === 'LOW' || val === 'L') {
						str = display || '低'
						className = 'bg-blue'
					} else if (val === 'MID' || val === 'M') {
						str = display || '中'
						className = 'bg-yellow'
					} else {
						str = display || '高'
						className = 'bg-red'
					}
					return '<span style="padding: 1px 15px;" class="' + className + '">' + str + '</span>';
				}
			}
		},
		/**
		 * 枚举值操作实用工具
		 */
		enum: {
			/**
			 * 转换（类似vue/angular中的filter）
			 */
			transform: function(enumName, val) {
				var rlt = ''
				config[enumName].forEach(function(item) {
					if (item.id === val) {
						rlt = item.text
					}
				})
				return rlt
			}
		},
		/**
		 * 表单操作实用工具
		 */
		form: {
			/**
			 * 重置表单
			 * @param form 表单 jquery对象
			 */
			reset: function(form) {
				var selects = form.find('select')
				var ichecks = form.find('.icheck')
				var select2s = form.find('.origin-select')
				form.resetForm()
				selects.each(function(index, item) {
					$(item).change()
				})
				ichecks.iCheck('update').each(function(index, item) {
					if (item.checked) {
						$(item).trigger('ifChecked')
					}
				})
				select2s.each(function(index, item) {
					$(item).select2('val', '')
				})
			},
			/**
			 * 表单验证工具
			 */
			validator: {
				init: function(form) {
					form.validator({
						custom: {
							validfloat: this.validfloat,
							validint: this.validint,
							validpositive: this.validpositive,
							validdateafter: this.validdateafter
						},
						errors: {
							validfloat: '数据格式不正确',
							validint: '数据格式不正确',
							validpositive: '数据必须为自然数',
							validdateafter: 'xxx'
						}
					})
				},
				/**
				 * 浮点数校验，例如 data-validfloat="10.2"，10.2 表示小数点前面10位后面2位，默认前后各10位
				 * @param $el 验证表单元素 jquery对象
				 * @returns 验证结果 {boolean}
				 * FIXME 目前默认值不起作用，框架导致，待修改
				 */
				validfloat: function($el) {
					var value = $el.val().trim()
					var range = $el.attr('data-validfloat') || '10.10'
					var rangeArr = range.split('.')
					var intPart = rangeArr[0]
					var decPart = rangeArr[1]
					var regStr = '^[+-]?\\d{0,' + intPart + '}(\\.\\d{0,' + decPart + '})?$'
					var floatReg = new RegExp(regStr)
					if (!floatReg.test(value)) {
						return false
					} else {
						return true
					}
				},
				/**
				 * 整数校验，例如 data-validint="1-1000"，1-1000 表示许可范围从1到1000
				 * @param $el 验证表单元素 jquery对象
				 * @returns 验证结果 {boolean}
				 * FIXME 目前默认值不起作用，框架导致，待修改
				 */
				validint: function($el) {
					var value = $el.val().trim()
					var range = $el.attr('data-validint') || '0-1000000000'
					var rangeArr = range.split('-')
					var start = Number(rangeArr[0])
					var end = Number(rangeArr[1])
					var regStr = '^[+-]?\\d*$'
					var intReg = new RegExp(regStr)
					if (!intReg.test(value)) {
						return false
					} else {
						if (parseInt(value) > end || parseInt(value) < start) {
							return false
						} else {
							return true
						}
					}
				},
				/**
				 * 非负数校验 大于零：data-validpositive="non-zero" 大于等于零：data-validpositive="true"
				 * @param $el 验证表单元素 jquery对象
				 * @returns 验证结果 {boolean}
				 */
				validpositive: function($el) {
					var value = $el.val().trim()
					if (!value) {
						return true
					}
					var type = $el.attr('data-validpositive')
					switch (type) {
						case 'non-zero':
							return Number(value) > 0
						default:
							return Number(value) >= 0
					}
				},
				/**
				 * 日期校验，校验日期必须大于等于关联日期
				 * @param $el 验证表单元素 jquery对象
				 * @returns 验证结果 {boolean}
				 */
				validdateafter: function($el) {
					var value = $el.val().trim()
					if (!value) {
						return true
					}
					var target = $el.attr('data-validdateafter')
					return DateTime.parse(value) >= DateTime.parse(target)
				}

			},
			/**
			 * 将表单序列化成json对象
			 * @param form 表单
			 * @returns 列化之后的json对象
			 */
			serializeJson: function(form) {
				var serializeObj = {}
				var array = $(form).serializeArray()
				var str = $(form).serialize()
				$(array).each(function() {
					if (serializeObj[this.name]) {
						if ($.isArray(serializeObj[this.name])) {
							serializeObj[this.name].push(this.value)
						} else {
							serializeObj[this.name] = [serializeObj[this.name], this.value]
						}
					} else {
						serializeObj[this.name] = this.value
					}
				})
				return serializeObj
			}
		},
		/**
		 * 导航栏操作实用工具
		 */
		nav: {
			/**
			 * 导航栏手动触发页面跳转
			 * @param pageName 页面名称
			 * @param querystring 参数
			 */
			dispatch: function(pageName, querystring) {
				$('#sidebarMenu').find('a').each(function(index, item) {
					if ($(item).attr('data-gh-route') === pageName) {
						$(item).click()
						location.hash = '#' + pageName + '?' + querystring
					}
				})
			},
			/**
			 * 获取hash中的参数，转成json object
			 * @param hash
			 * @returns json object
			 */
			getHashObj: function(hash) {
				var querystring = hash.substr(hash.indexOf('?') + 1)
				var obj = {}
				querystring.split('&').forEach(function(item) {
					var arr = item.split('=')
					obj[arr[0]] = arr[1]
				})
				return obj
			}
		},
		/**
		 * 将对象转换成带参数的形式 &a=1&b=2
		 */
		buildQueryUrl: function(url, param) {
			var x = url
			var ba = true
			if (x.indexOf('?') != -1) {
				if (x.indexOf('?') == url.length - 1) {
					ba = false
				} else {
					ba = true
				}
			} else {
				x = x + '?'
				ba = false
			}
			var builder = ''
			for (var i in param) {
				var p = '&' + i + '='
				if (param[i]) {
					var v = param[i]
					if (Object.prototype.toString.call(v) === '[object Array]') {
						for (var j = 0; j < v.length; j++) {
							builder = builder + p + encodeURIComponent(v[j])
						}
					} else if (typeof(v) == "object" && Object.prototype.toString.call(v).toLowerCase() == "[object object]" && !v.length) {
						builder = builder + p + encodeURIComponent(JSON.stringify(v))
					} else {
						builder = builder + p + encodeURIComponent(v)
					}
				}
			}
			if (!ba) {
				builder = builder.substring(1)
			}
			return x + builder
		},
		/**
		 * 生成随机字符串
		 * @param {Object} len 多少位
		 */
		getRandomString: function(len) {
			len = len || 32;
			var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678'; // 默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1  
			var maxPos = $chars.length;
			var pwd = '';
			for (i = 0; i < len; i++) {
				pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
			}
			return pwd;
		}
	}
})