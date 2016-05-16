package com.guohuai.asset.manage.component.web;

import java.nio.charset.Charset;
import java.util.Enumeration;

import javax.persistence.MappedSuperclass;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guohuai.asset.manage.component.exception.AMPException;
import com.guohuai.asset.manage.component.web.view.Response;

@MappedSuperclass
public class BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

	protected static final String SESSION_REDIS_PREFIX = "spring:session:sessions:";

	protected static final String SESSION_REDIS_ATTRIBUTE_PREFIX = "sessionAttr:";
	protected static final String SESSION_OID_KEY = "operateoid";

	// hash 集合, 存储 sessionid - userid 关系
	protected static final String SESSION_RELATION_KEY = "c:g:o:s";

	@Autowired
	protected HttpSession session;

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected HttpServletResponse response;

	@Autowired
	protected RedisTemplate<String, String> redis;

	@Value("${amp.asset.error.print:ignore}")
	private String onError = "ignore";

	@ExceptionHandler
	public @ResponseBody ResponseEntity<Response> onError(HttpServletRequest request, Exception exception) {

		this.errorLogHandle(exception);

		Response response = new Response().error(exception);
		Enumeration<String> enums = this.request.getParameterNames();
		while (enums.hasMoreElements()) {
			String name = enums.nextElement();
			response.with(name, this.request.getParameter(name));
		}

		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	private void errorLogHandle(Exception e) {
		if (this.onError.equals("stack")) {
			e.printStackTrace();
		} else if (this.onError.equals("message")) {
			logger.info(e.getMessage());
		} else {

		}
	}

	protected String getSessionId() {
		String sessionid = this.request.getParameter("sessionid");
		if (null == sessionid) {
			return this.session.getId();
		}
		return sessionid;
	}

	protected void checkLogin() {
		String hkey = SESSION_REDIS_PREFIX + this.getSessionId();
		String key = SESSION_REDIS_ATTRIBUTE_PREFIX + SESSION_OID_KEY;
		Object obj = this.readRedis(hkey, key);
		if (null == obj || !(obj instanceof String) || obj.toString().trim().equals("")) {
			throw AMPException.getException(10000);
		}

	}

	protected String getLoginAdmin() {
		this.checkLogin();
		String hkey = SESSION_REDIS_PREFIX + this.getSessionId();
		String key = SESSION_REDIS_ATTRIBUTE_PREFIX + SESSION_OID_KEY;
		return this.readRedis(hkey, key).toString();
	}

	protected boolean isLogin() {
		String hkey = SESSION_REDIS_PREFIX + this.getSessionId();
		String key = SESSION_REDIS_ATTRIBUTE_PREFIX + SESSION_OID_KEY;
		Object obj = this.readRedis(hkey, key);
		if (null == obj || !(obj instanceof String) || obj.toString().trim().equals("")) {
			return false;
		}
		return true;
	}

	protected void setLoginAdmin(final String oid) {
		final String sessionid = this.getSessionId();
		this.redis.execute(new RedisCallback<Void>() {
			private Charset utf8 = Charset.forName("utf-8");
			private JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();

			@Override
			public Void doInRedis(RedisConnection connection) throws DataAccessException {
				connection.hSet(SESSION_RELATION_KEY.getBytes(this.utf8), sessionid.getBytes(this.utf8), oid.getBytes(utf8));
				connection.hSet((SESSION_REDIS_PREFIX + sessionid).getBytes(this.utf8), (SESSION_REDIS_ATTRIBUTE_PREFIX + SESSION_OID_KEY).getBytes(this.utf8), this.serializer.serialize(oid));
				return null;
			}
		});

	}

	protected void delLoginAdmin() {
		final String sessionid = this.getSessionId();
		this.redis.execute(new RedisCallback<Void>() {
			private Charset utf8 = Charset.forName("utf-8");

			@Override
			public Void doInRedis(RedisConnection connection) throws DataAccessException {
				connection.hDel((SESSION_REDIS_PREFIX + sessionid).getBytes(this.utf8), (SESSION_REDIS_ATTRIBUTE_PREFIX + SESSION_OID_KEY).getBytes(this.utf8));
				connection.hDel(SESSION_RELATION_KEY.getBytes(this.utf8), sessionid.getBytes(this.utf8));
				return null;
			}
		});
	}

	private Object readRedis(final String hkey, final String key) {
		Object value = this.redis.execute(new RedisCallback<Object>() {

			private Charset utf8 = Charset.forName("utf-8");
			private JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();

			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] bytes = connection.hGet(hkey.getBytes(this.utf8), key.getBytes(this.utf8));
				if (null != bytes && bytes.length > 0) {
					Object obj = this.serializer.deserialize(bytes);
					return obj;
				} else {
					return null;
				}
			}
		});
		return value;
	}

}
