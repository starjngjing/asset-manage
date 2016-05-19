package com.guohuai.asset.manage.boot.system.config.project.warrantor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CCPWarrantorDao extends JpaRepository<CCPWarrantor, String> {

	@Query("from CCPWarrantor w order by w.highScore desc")
	public List<CCPWarrantor> search();

}
