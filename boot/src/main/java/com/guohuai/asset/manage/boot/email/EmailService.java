package com.guohuai.asset.manage.boot.email;

import java.io.StringWriter;
import java.sql.Timestamp;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.boot.email.log.EmailLog;
import com.guohuai.asset.manage.boot.email.log.EmailLogService;
import com.guohuai.asset.manage.component.config.MailDefineConfig;
import com.guohuai.asset.manage.component.exception.AMPException;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender sender;

	@Value("${amp.email.enable:false}")
	private boolean enable;

	@Value("${spring.mail.username}")
	private String username;

	@Value("${spring.mail.username.from}")
	private String from;

	@Value("${spring.mail.subject.prefix}")
	private String prefix;

	@Value("${spring.mail.sender.retry}")
	private int retry;

	@Value("${amp.email.link.gmcmd}")
	protected String gmCmdLink;
	@Value("${amp.email.link.bankcmd}")
	protected String bankCmdLink;

	@Value("${amp.email.file.yupload}")
	private String yupload;

	@Value("${amp.email.guohuai.logo:http://www.guohuaigroup.com/release/cloudstatic/images/logo.png}")
	private String logo;

	@Autowired
	private VelocityEngine velocity;

	@Autowired
	private EmailLogService emailLogService;

	@Autowired
	private MailDefineConfig mailDefineConfig;

	@PostConstruct
	public void send() {

		if (!this.enable) {
			return;
		}

		Thread t = new Thread("SEND-MAIL-TASK") {
			@Override
			public void run() {

				while (true) {
					try {
						boolean next = EmailService.this.task();
						if (next) {
							continue;
						} else {
							try {
								Thread.sleep(1000 * 60);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
			}
		};

		t.start();

	}

	@Transactional
	public boolean task() {
		Page<EmailLog> sendList = this.emailLogService.search4Send();

		for (EmailLog email : sendList) {
			email.setSendTime(new Timestamp(System.currentTimeMillis()));
			try {

				if (null == email.getTarget() || email.getTarget().size() == 0) {
					throw new AMPException("Empty receiver list.");
				}

				StringWriter writer = new StringWriter();
				String template = this.mailDefineConfig.define.get(email.getTemplate());

				VelocityContext context = new VelocityContext();
				context.put("yupload", this.yupload);
				context.put("gmCmdLink", this.gmCmdLink);
				context.put("bankCmdLink", this.bankCmdLink);
				context.put("logo", this.logo);
				context.put("content", email.getContent());
				context.put("ekey", email.getEkey());
				this.velocity.mergeTemplate(template, "utf-8", context, writer);

				MimeMessage message = this.sender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message);
				helper.setFrom(this.from);
				helper.setTo(email.getTarget().toArray(new String[0]));
				helper.setSubject("[" + this.prefix + "] - " + email.getTitle());
				helper.setText(writer.toString(), true);
				this.sender.send(message);

				email.setState(EmailLog.STATE_SENDED);
			} catch (Throwable e) {
				email.setRetry(email.getRetry() + 1);
				if (email.getRetry() >= this.retry) {
					email.setState(EmailLog.STATE_OVERRETRY);
				} else {
					email.setState(EmailLog.STATE_ERROR);
				}
				email.setErrors(e.getMessage());
			}
			this.emailLogService.save(email);
		}

		return sendList.getTotalPages() > 1;

	}

}
