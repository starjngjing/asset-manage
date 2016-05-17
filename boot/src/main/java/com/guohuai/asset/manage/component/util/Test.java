package com.guohuai.asset.manage.component.util;

import com.guohuai.asset.manage.component.util.ContinuousList.Continuous;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class Test {

	@Data
	@EqualsAndHashCode(callSuper = false)
	@Builder
	public static class T extends Continuous {
		private int from;
		private int to;
		private String name;
	}

	public static void main(String[] args) {
		ContinuousList<T> list = new ContinuousList<T>();
		list.add(T.builder().from(90).to(100).name("A").build());
//		list.add(T.builder().from(80).to(89).name("B").build());
//		list.add(T.builder().from(70).to(79).name("C").build());
//		list.add(T.builder().from(60).to(69).name("D").build());
//		list.add(T.builder().from(50).to(59).name("E").build());
//		list.add(T.builder().from(0).to(49).name("F").build());

		System.out.println(list.isConsecutive());
	}

}
