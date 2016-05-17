package com.guohuai.asset.manage.component.resp;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CommonResp {
	int total, errorCode;
//	String erroMsg;
	String errorMessage;
	List<Object> rows = new ArrayList<>();
	String attached;
}
