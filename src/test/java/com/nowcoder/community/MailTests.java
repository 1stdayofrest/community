package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void test1() {
        /**
         * 要访问模板，需要给模板传参数
         * 这里不能用DispatcherServlet
         * 1.引入实例化配置
         * 2.设置变量名字
         * 3.
         */
        Context context = new Context();
        context.setVariable("username","常菊");
        String process = templateEngine.process("/mail/maildemo", context);
        mailClient.sentMail("247848502@qq.com","HTMLTEST",process);

    }

    @Test
    public void testMail(){
        mailClient.sentMail("247848502@qq.com","TEST","HELLO WORLD!!");
    }
}
