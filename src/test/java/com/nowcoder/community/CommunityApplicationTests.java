package com.nowcoder.community;

import com.nowcoder.community.Service.ServiceDemo1;
import com.nowcoder.community.dao.DaoDemo1;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class CommunityApplicationTests implements ApplicationContextAware {
	/**
	 * 作者：李兆权
	 * 时间：2019/07/01  22：13
	 * 在测试代码中启用以CommunityApplication为配置类：使用ContextConfiguration注解
	 * 同时使用类去实现接口ApplicationContextAware
	 * ApplicationContext < HierarchicalBeanFactory < BeanFactory
	 * spring容器扫描到了，就会把自己传进来
	 * 用私有变量存储传进来的ApplicationContext(BeanFactory)Spring容器
	 */
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Test
	public void testApplicationContext() {
		System.out.println(applicationContext);
		/**
		 * 使用使用ContextConfiguration注解把Spring容器传进来了，
		 * Spring容器被创建以后会自动去扫描特定的bean，并将其装配到容器里面
		 * 在相同的类型（实现同一个接口的类）的对象时getBean方法，给需要调用DaoImp类加Primary注解
		 * 或者使用方法重载getBean（）传入Bean的类型和名字
		 * 使用Primary("BeanName")给Bean命名，
		 * 优点：去耦合，调用方和实现类不发生任何联系，提高代码利用率
		 */
		DaoDemo1 daoDemo1 = applicationContext.getBean(DaoDemo1.class);
		System.out.println(daoDemo1.select());
	}
	@Test
	public void testServiceDemo1() {
		//传入Bean
		ServiceDemo1 serviceDemo1 = applicationContext.getBean(ServiceDemo1.class);
		System.out.println(serviceDemo1);
	}




}
