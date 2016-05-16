package com.guohuai;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.guohuai.asset.manage.component.config.MailDefineConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AssetPlatformBoot.class)
public class GuohuaiPlatformAssetApplicationTests {

	@Autowired
	private MailDefineConfig mailDefineConfig;

	@Test
	public void contextLoads() {
		System.out.println(this.mailDefineConfig.define);
	}

}
