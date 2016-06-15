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

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

public class SeqUtil {
	@Autowired
	private StringRedisTemplate redis;

	/**
	 * 每天自动重置
	 * 
	 * @Title: next
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @param prefix
	 * @return prefix+序列号
	 */
	public String next(final String prefix) {

		return redis.execute(new RedisCallback<String>() {

			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {

				String value = "1"; // 初始值1
				boolean flag = connection.setNX(prefix.getBytes(), value.getBytes());
				if (flag) { // 设置成功后设置过期时间
					connection.expireAt(prefix.getBytes(), getTommorowDate().getTimeInMillis() + 30000); // 设置过期时间为明天凌晨（延迟30秒）
				} else {
					value = connection.incr(prefix.getBytes()).toString();
				}
				return formatSeq(prefix, value);
			}
		});
	}

	public Long currSeq(final String prefix) {
		String value = redis.opsForValue().get(prefix);
		if (StringUtils.isBlank(value))
			return null;
		return Long.parseLong(value.trim());

	}

	public String curr(final String prefix) {
		Long value = currSeq(prefix);
		if (null == value)
			value = 1l;
		return formatSeq(prefix, value.toString());

	}

	public String test_next(final String prefix) {
		Long value = currSeq(prefix);
		if (null == value)
			value = 1l;

		return formatSeq(prefix, (value += 1).toString());

	}

	private static String formatSeq(String prefix, String value) {
		return prefix + StringUtils.leftPad(value.toString(), 3, '0');
	}

	/**
	 * 获取明天0点的日期
	 * 
	 * @Title: getTommorowDate
	 * @author vania
	 * @version 1.0
	 * @see:
	 * @return long 返回类型
	 */
	public static Calendar getTommorowDate() {
		Calendar curDate = Calendar.getInstance();
		return new GregorianCalendar(curDate.get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate.get(Calendar.DATE) + 1, 0, 0, 0);
	}

	/**
	 * 获取今天还剩下多少秒
	 * 
	 * @return
	 */
	public static int getMiao() {
		return (int) (getTommorowDate().getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / 1000;
	}

	public static void main(String[] args) {
	}
}
