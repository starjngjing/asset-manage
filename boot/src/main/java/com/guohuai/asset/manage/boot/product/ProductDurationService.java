package com.guohuai.asset.manage.boot.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.web.view.PageResp;
import com.guohuai.operate.api.AdminSdk;
import com.guohuai.operate.api.objs.admin.AdminObj;

@Service
public class ProductDurationService {

	@Autowired
	private ProductService productService;

	@Autowired
	private AdminSdk adminSdk;

	public PageResp<ProductLogListResp> durationList(Specification<Product> spec, Pageable pageable) {
		PageResp<ProductLogListResp> pagesRep = this.productService.checkList(spec, pageable);
		List<ProductLogListResp> rows = pagesRep.getRows();

		Map<String, AdminObj> adminObjMap = new HashMap<String, AdminObj>();
		AdminObj adminObj = null;
		for (ProductLogListResp p : rows) {
			ProductLog pl = this.productService.getProductLog(p.getOid(), ProductLog.AUDIT_TYPE_Reviewing, ProductLog.AUDIT_STATE_Approval);

			if (pl != null) {
				// 复核人复核时间
				if (adminObjMap.get(pl.getAuditor()) == null) {
					try {
						adminObj = this.adminSdk.getAdmin(pl.getAuditor());
						adminObjMap.put(pl.getAuditor(), adminObj);
					} catch (Exception e) {
					}
				}
				if (adminObjMap.get(pl.getAuditor()) != null) {
					p.setReviewer(adminObjMap.get(pl.getAuditor()).getName());
				}
				p.setReviewTime(DateUtil.formatDatetime(pl.getAuditTime().getTime()));
			}

			ProductLog pl2 = this.productService.getProductLog(p.getOid(), ProductLog.AUDIT_TYPE_Approving, ProductLog.AUDIT_STATE_Approval);
			if (pl2 != null) {
				if (adminObjMap.get(pl2.getAuditor()) == null) {
					try {
						adminObj = this.adminSdk.getAdmin(pl.getAuditor());
						adminObjMap.put(pl2.getAuditor(), adminObj);
					} catch (Exception e) {
					}
				}
				if (adminObjMap.get(pl2.getAuditor()) != null) {
					p.setAccesser(adminObjMap.get(pl2.getAuditor()).getName());
				}
				p.setAccessTime(DateUtil.formatDatetime(pl2.getAuditTime().getTime()));
			}
		}
		return pagesRep;
	}
}
