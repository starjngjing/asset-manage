/*   
 * Copyright © 2015 guohuaigroup All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 *   
 */
package com.guohuai.asset.manage.boot.enums;

/**
 * 标的操作类型
 * <p>Title: TargetOperatorType.java</p>    
 * <p>Description: 描述 </p>   
 * @author vania      
 * @version 1.0    
 * @created 2016年5月19日 下午6:46:55
 */
public enum TargetOperatorType {
	create("1", "创建"), create2("1", "创建"),;
	private String code;
	private String text;

	private TargetOperatorType(String code, String text) {
		this.code = code;
		this.text = text;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return this.code;
	}

	/**
	 * 通过code取得类型
	 * 
	 * @param code
	 * @return
	 */
	public static TargetOperatorType getType(String code) {
		for (TargetOperatorType type : TargetOperatorType.values()) {
			if (type.getCode().equals(code)) {
				return type;
			}
		}
		return null;
	}
}
