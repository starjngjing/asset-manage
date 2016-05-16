package com.guohuai.data;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.guohuai.asset.manage.component.util.StringUtil;

public class DataDriver {

	public static AtomicInteger SN = new AtomicInteger(1569);

	public static void main(String[] args) throws IOException {
		Map<String, String> user = AdminData.loadAdmin();

		for (int i = 0; i < 255; i++) {
			System.out.println(StringUtil.uuid() + "\t" + getSn());
		}

		System.out.println(user);
	}

	public static String getSn() {
		return "60201603240000" + SN.incrementAndGet();
	}

}
