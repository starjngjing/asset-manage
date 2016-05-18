package com.guohuai.asset.manage.component.web.parameter;

import java.beans.PropertyEditorSupport;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

public class CustomerTimeEditor extends PropertyEditorSupport {

	private static CustomerTimeEditor editor = new CustomerTimeEditor();

	public static CustomerTimeEditor getInstance() {
		return editor;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {

		if (null == text || text.trim().equals("")) {
			super.setValue(null);
			return;
		}

		try {
			Time time = Time.valueOf(text);
			super.setValue(time);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}

	}

	@Override
	public String getAsText() {
		Time value = (Time) super.getValue();
		if (null == value) {
			return null;
		}
		return value.toString();
	}

}
