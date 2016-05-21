package com.guohuai.asset.manage.boot.system.config.risk.indicate;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface RiskIndicateDao extends JpaRepository<RiskIndicate, String>, JpaSpecificationExecutor<RiskIndicate> {

	@Query("from RiskIndicate i where i.cate.type like ?1 and (i.title like ?2 or i.cate.title like ?2) and i.state in ?3 order by i.cate.type asc, i.cate.title asc, i.title asc")
	public List<RiskIndicate> search(String type, String keyword, String[] state);

}