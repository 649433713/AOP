package yinywf.springbootdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan(basePackages = "yinywf.springbootdemo.dataobject.mapper")
@EnableCaching
public class SpringbootdemoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootdemoApplication.class, args);
	}
}
