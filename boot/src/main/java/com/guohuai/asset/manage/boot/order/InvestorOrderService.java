package com.guohuai.asset.manage.boot.order;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class InvestorOrderService {

	@Autowired
	private InvestorOrderDao investorOrderDao;

	public Page<InvestorOrder> list(Specification<InvestorOrder> spec, Pageable pageable) {
		return investorOrderDao.findAll(spec, pageable);
	}

}
