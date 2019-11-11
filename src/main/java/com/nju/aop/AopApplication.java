package com.nju.aop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan(basePackages = "com.nju.aop.dataobject.mapper")
@EnableCaching
public class AopApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(AopApplication.class, args);
	}
}
