package com.guohuai.asset.manage.component.web.parameter;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampPropertyEditor extends PropertyEditorSupport {

	private static TimestampPropertyEditor editor = new TimestampPropertyEditor();

	public static TimestampPropertyEditor getInstance() {
		return editor;
	}

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void setAsText(String text) throws IllegalArgumentException {

		if (null == text || text.trim().equals("")) {
			super.setAsText(null);
		}

		try {
			Timestamp value = new Timestamp(df.parse(text).getTime());
			super.setValue(value);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	@Override
	public String getAsText() {
		Timestamp value = (Timestamp) super.getValue();
		if (null == value) {
			return null;
		}

		return this.df.format(new Date(value.getTime()));

	}

}
