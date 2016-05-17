package com.guohuai.asset.manage.boot.investment;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class InterestService {
	@Autowired
	InterestDao interestDao;

	@Transactional
	public Interest save(Interest interest) {
		return interestDao.save(interest);
	}
}
