package com.sxyh.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sxyh.util.HdfsUtil;

@Configuration
@EnableConfigurationProperties(HdfsProperties.class)
public class HdfsConfig {
	
	@Autowired
	private HdfsProperties hdfsProperties;
	
	@Bean 
	public HdfsUtil hdfsUtil() {
		return new HdfsUtil(hdfsProperties);
	}

}
