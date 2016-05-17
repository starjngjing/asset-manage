package com.guohuai.asset.manage.component.util;

import java.util.LinkedList;

import lombok.Data;

public class ContinuousList<Continuous> extends LinkedList<Continuous> {

	private static final long serialVersionUID = -3296371477542679017L;

	@Data
	public static class Continuous {

		private int from;
		private int to;

		private boolean reversed = false;

	}

	public boolean isConsecutive() {

		if (this.size() <= 1) {
			return true;
		}

		for (int i = 0; i < this.size(); i++) {
			System.out.println(this.get(i));
		}

		return false;
	}

	public enum Sort {
		ASC, DESC
	}

	public void sort() {

	}

	public void sort(Sort direct) {

	}
}
