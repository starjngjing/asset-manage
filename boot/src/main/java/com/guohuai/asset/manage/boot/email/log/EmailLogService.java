package com.guohuai.asset.manage.boot.email.log;

import java.sql.Timestamp;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guohuai.asset.manage.component.util.StringUtil;

@Service
public class EmailLogService {

	@Autowired
	private EmailLogDao emailLogDao;

	@Value("${spring.mail.sender.retry}")
	private int retry;

	@Transactional
	public EmailLog save(EmailLog emailLog) {
		return this.emailLogDao.save(emailLog);
	}

	@Transactional
	public EmailLog save(String ekey, String template, String[] target, String title, JSONObject content) {

		JSONArray tgt = new JSONArray();
		if (null != target && target.length > 0) {
			for (String t : target) {
				tgt.add(t);
			}
		}

		EmailLog.EmailLogBuilder builder = EmailLog.builder().oid(StringUtil.uuid()).ekey(ekey).template(template).target(tgt).title(title).content(content);
		builder.state(EmailLog.STATE_SUBMIT).retry(0).submitTime(new Timestamp(System.currentTimeMillis())).sendTime(new Timestamp(0));
		EmailLog log = builder.build();
		log = this.emailLogDao.save(log);
		return log;
	}

	@Transactional
	public EmailLog get(String oid) {
		return this.emailLogDao.findOne(oid);
	}

	@Transactional
	public Page<EmailLog> search4Send() {
		Specification<EmailLog> spec = new Specification<EmailLog>() {
			@Override
			public Predicate toPredicate(Root<EmailLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate submit = cb.equal(root.get("state").as(String.class), EmailLog.STATE_SUBMIT);
				Predicate error = cb.equal(root.get("state").as(String.class), EmailLog.STATE_ERROR);
				Predicate inRetry = cb.lessThan(root.get("retry").as(Integer.class), retry);
				Predicate p = cb.or(submit, cb.and(error, inRetry));
				return p;
			}
		};

		Pageable pager = new PageRequest(0, 100, Direction.DESC, "submitTime");

		Page<EmailLog> sendList = this.emailLogDao.findAll(spec, pager);
		return sendList;
	}

}
