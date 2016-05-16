package com.guohuai.asset.manage.boot.file;

import com.guohuai.asset.manage.component.web.view.BaseResp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class FileDLResp extends BaseResp {

	private String key;

}
