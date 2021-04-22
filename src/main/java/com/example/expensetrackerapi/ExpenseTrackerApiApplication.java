package com.example.expensetrackerapi;

import com.example.expensetrackerapi.constants.GlobalConstants;
import com.example.expensetrackerapi.filter.AuthFilter;

import org.apache.tomcat.jni.Global;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class ExpenseTrackerApiApplication {

	@Bean
	public FilterRegistrationBean<AuthFilter> filterRegistrationBean() {
		FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<AuthFilter>(new AuthFilter());
		registrationBean.addUrlPatterns(GlobalConstants.API_ENDPOINT+"/categories/*");
		return registrationBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerApiApplication.class, args);
	}
}
