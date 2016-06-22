package com.guohuai.asset.manage.boot.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.guohuai.asset.manage.component.util.DateUtil;
import com.guohuai.asset.manage.component.util.StringUtil;
import com.guohuai.asset.manage.component.web.view.PageResp;
import com.guohuai.operate.api.AdminSdk;
import com.guohuai.operate.api.objs.admin.AdminObj;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specifications;

@Service
public class ProductDurationService {

	@Autowired
	private ProductDao productDao;
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
	
	/**
	 * 获取存续期产品的名称列表，包含id
	 * @return
	 */
	@Transactional
	public List<JSONObject> productNameList(Specification<Product> spec) {
		List<JSONObject> jsonObjList = Lists.newArrayList();
		List<Product> ps = productDao.findAll(spec, new Sort(new Order(Direction.DESC, "updateTime")));
		if (ps!=null && ps.size()>0) {
			JSONObject jsonObj = null;
			for (Product p : ps) {
				jsonObj = new JSONObject();
				jsonObj.put("oid", p.getOid());
				jsonObj.put("name", p.getName());
				jsonObjList.add(jsonObj);
			}
		}
		
		return jsonObjList;
	}
	
	@Transactional
	public ProductDetailResp getProductByOid(String oid) {
		ProductDetailResp pr = null;
		if(StringUtil.isEmpty(oid)) {
			Specification<Product> spec = new Specification<Product>() {
				@Override
				public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.and(cb.equal(root.get("isDeleted").as(String.class), Product.NO), cb.equal(root.get("auditState").as(String.class), Product.AUDIT_STATE_Reviewed));
				}
			};
			spec = Specifications.where(spec);
			List<Product> ps = productDao.findAll(spec, new Sort(new Order(Direction.DESC, "updateTime")));
			if(ps!=null && ps.size()>0) {
				Product p = ps.get(0);
				pr = new ProductDetailResp(p);
			}
		} else {
			pr = this.productService.read(oid);
		}

		return pr;
	}
	
}
