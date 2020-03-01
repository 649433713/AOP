package com.nju.aop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@MapperScan(basePackages = "com.nju.aop.dataobject.mapper")
@EnableCaching
public class AopApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(AopApplication.class, args);
	}


	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//指定文件代销
		factory.setMaxFileSize("50MB");
		/// 设定上传文件大小
		factory.setMaxRequestSize("100MB");
		return factory.createMultipartConfig();
	}
}
