package com.guohuai.asset.manage.component.util;

import java.util.Random;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.Validate;

public class StringUtil {

	public final static String EMPTY = "";

	public final static boolean isEmpty(String s) {
		return s == null || s.trim().equals(EMPTY);
	}

	public final static String vague(String s, int begin, int end) {
		if (isEmpty(s)) {
			return s;
		}

		if (s.length() <= begin + end) {
			return s;
		}
		char[] chars = s.toCharArray();
		for (int i = begin; i < chars.length - end; i++) {
			chars[i] = '*';
		}

		return new String(chars);
	}

	public final static String uuid() {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid;
	}

	public final static boolean in(String param, String... factor) {
		if (null != factor && factor.length != 0) {
			for (String f : factor) {
				if (param.equals(f)) {
					return true;
				}
			}
		}
		return false;
	}

	private static final Random random = new Random();

	public static String random(int bytesLength) {
		Validate.isTrue(bytesLength > 0, "numBytes argument must be a positive integer (1 or larger)", bytesLength);
		byte[] bytes = new byte[bytesLength];
		random.nextBytes(bytes);
		String s = encodeBase64(bytes);
		return s;
	}

	public static String encodeBase64(byte[] bytes) {
		return Base64.encodeBase64String(bytes);
	}

	public static byte[] decodeBase64(String cipher) {
		return Base64.decodeBase64(cipher);
	}

}
