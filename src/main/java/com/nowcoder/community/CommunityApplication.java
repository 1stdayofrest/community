package com.nowcoder.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class CommunityApplication {
	/**
	 * Spring在启动的时候是需要配置的，CommunityApplication类其实就是一个配置文件
	 * SpringBootApplication注解所标识的类，为配置文件
	 * SpringBootApplication注解，又被注解的注解所注解，启动了自动配置
	 * ComponentScan注解会扫描配置类所在的包以及子包下面的Bean，启动Bean上面需要有Controller注解
	 * 启动tomcat，自动帮我们创建了Spring容器？？在SpringApplication的底层
	 * Spring容器被创建以后会自动去扫描特定的bean，并将其装配到容器里面
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

}
