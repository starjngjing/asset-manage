package com.guohuai.asset.manage.component.web.view;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PageResp<T> extends BaseResp {

	protected long total;
	protected List<T> rows = new ArrayList<T>();

}
