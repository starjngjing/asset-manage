package com.guohuai.asset.manage.boot.channel;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.guohuai.asset.manage.component.web.parameter.validation.Enumerations;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@lombok.Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ChannelAddReq {
		
	private String oid;
	
	@NotBlank(message = "渠道名称不能为空！")
	@Length(max = 200, message = "渠道名称长度不能超过200（包含）！")
	private String channelName;
	
	@NotBlank(message = "渠道费率不能为空！")
	@Digits(integer = 1, fraction = 2, message = "渠道费率格式错误，例如：0.05")
	@DecimalMin(value = "0", inclusive = true, message = "渠道费率必须>=0！")
	private String channelFee;
	
	@NotBlank(message = "接入方式不能为空！")
	@Enumerations(values = {"ftp","api"}, message = "接入方式参数有误！")
	private String joinType;
	
	@NotBlank(message = "渠道机构不能为空！")
	@Length(max = 50, message = "渠道机构长度不能超过50（包含）！")
	private String partner;
	
	@NotBlank(message = "渠道联系人不能为空！")
	@Length(max = 30, message = "渠道联系人长度不能超过30（包含）！")
	private String channelContactName;	
	
	@NotBlank(message = "渠道联系人邮件不能为空！")
	@Length(max = 30, message = "渠道联系人邮件长度不能超过30（包含）！")
	private String channelEmail;
	
	@NotBlank(message = "渠道联系人电话不能为空！")
	@Length(max = 30, message = "渠道联系人电话长度不能超过30（包含）！")
	private String channelPhone;
}
