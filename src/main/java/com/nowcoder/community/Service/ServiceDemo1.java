package com.nowcoder.community.Service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Spring容器管理Bean的方法
 * 创建管理销毁
 * 创建：使用PostConstruct注解init方法，使得init方法在构造器时候执行
 * 销毁：使用PreDestory注解destory方法，使得在销毁对象之前调用destory方法
 * Bean是单例的，只被实例化一次
 * 使用Scope()注解来选择Bean是单例，还是可以创建多个，默认单例模式
 */

@Service
public class ServiceDemo1 {

    public ServiceDemo1() {
        System.out.println("实例化 ServiceDemo1 !!!");
    }

    @PostConstruct
    public void init() {
        System.out.println("Init ServiceDemo1 !!!");
    }
    @PreDestroy
    public void destory() {
        System.out.println("destory ServiceDemo1 !!!");
    }

}
