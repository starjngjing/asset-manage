package com.guohuai.asset.manage.boot.email.log;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_GAM_MAIL_LOG")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailLog {

	// 提交, 等待发送
	public static final String STATE_SUBMIT = "SUBMIT";
	// 已发送
	public static final String STATE_SENDED = "SENDED";
	// 发送失败
	public static final String STATE_ERROR = "ERROR";
	// 超过重试次数
	public static final String STATE_OVERRETRY = "OVERRETRY";

	@Id
	private String oid;
	private String ekey;
	private String template;
	private JSONArray target;
	private String title;
	private JSONObject content;
	private String state;
	private String errors;
	private int retry;
	private Timestamp submitTime;
	private Timestamp sendTime;

	public static enum ContentKey {
		FILE, SMS, VOLUME, DATE, AROR, LIFED, CIPHER, PIN, LINK;
		public String toString() {
			return super.toString().toLowerCase();
		};
	}

	public static class ContentBuilder {

		private JSONObject content = new JSONObject();

/*		public ContentBuilder file(List<File> file) {
			if (null != file && file.size() > 0) {
				JSONArray a = new JSONArray();
				for (File f : file) {
					a.add(new FileResp(f));
				}
				this.content.put(EmailLog.ContentKey.FILE.toString(), a);
			}
			return this;
		}*/

		public ContentBuilder sms(String sms) {
			this.content.put(EmailLog.ContentKey.SMS.toString(), sms);
			return this;
		}

		public ContentBuilder volume(BigDecimal volume) {
			this.content.put(EmailLog.ContentKey.VOLUME.toString(), volume);
			return this;
		}

		public ContentBuilder aror(BigDecimal aror) {
			this.content.put(EmailLog.ContentKey.AROR.toString(), aror);
			return this;
		}

		public ContentBuilder lifed(int lifed) {
			this.content.put(EmailLog.ContentKey.LIFED.toString(), lifed);
			return this;
		}

		public ContentBuilder date(Date date) {
			this.content.put(EmailLog.ContentKey.DATE.toString(), date.toString());
			return this;
		}

		public ContentBuilder cipher(String cipher) {
			this.content.put(EmailLog.ContentKey.CIPHER.toString(), cipher);
			return this;
		}

		public ContentBuilder pin(String pin) {
			this.content.put(EmailLog.ContentKey.PIN.toString(), pin);
			return this;
		}

		public ContentBuilder link(String text, String url) {
			JSONObject json = new JSONObject();
			json.put("text", text);
			json.put("url", url);
			this.content.put(EmailLog.ContentKey.LINK.toString(), json);
			return this;
		}

		public JSONObject build() {
			return this.content;
		}

	}

}
