package com.tsingtec.tsingweb.config.csrf;

import com.google.common.collect.Maps;
import com.tsingtec.tsingweb.filter.CSRFFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CSRFFilterConfig {

	@Value("${csrf.enabled}")
	private String enabled;

	@Value("${csrf.referers}")
	private String referers;

	@Value("${csrf.urlPatterns}")
	private String urlPatterns;

	@Bean
	public FilterRegistrationBean csrfFilterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new CSRFFilter());
		filterRegistrationBean.setOrder(2);
		filterRegistrationBean.setEnabled(true);
		filterRegistrationBean.setName("CsrfFilter");
		filterRegistrationBean.addUrlPatterns(urlPatterns.split(","));

		Map<String, String> initParameters = Maps.newHashMap();
		initParameters.put("referers", referers);
		initParameters.put("enabled",enabled);
		filterRegistrationBean.setInitParameters(initParameters);

		return filterRegistrationBean;
	}
}
