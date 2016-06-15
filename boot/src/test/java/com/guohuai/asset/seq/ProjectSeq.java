/*   
 * Copyright © 2015 guohuaigroup All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 *   
 */
package com.guohuai.asset.seq;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectSeq {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private static SeqUtil seq = new SeqUtil();

	public static String next_project_sn() {
		String prefix = "ZA" + sdf.format(new Date());
		return seq.next(prefix);
	}
	
	public static void main(String[] args) {
		System.out.println(next_project_sn());
	}
}
