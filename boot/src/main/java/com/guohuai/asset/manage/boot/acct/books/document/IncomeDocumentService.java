package com.guohuai.asset.manage.boot.acct.books.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 凭证业务 - 资产池收益确认
 * 
 * @author Arthur
 */
@Service
public class IncomeDocumentService {

	@Autowired
	private DocumentDao documentDao;

	/**
	 * 收益确认
	 */
	public void incomeConfirm() {
		// 这里要接收所有收益确认, 并最终进行应收投资收益处理
	}

}
