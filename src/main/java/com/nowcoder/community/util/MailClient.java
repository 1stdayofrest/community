package com.nowcoder.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 创建工具类
 * Component注解
 * 需要用spring容器来关机
 * 通用的Bean，Component注解
 *
 */
@Component
public class MailClient {
    /**
     * 声明一个Logger记录日志，以当前的类来命名
     */
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    /**
     * 注入JavaMailSender
     *
     */
    @Autowired
    private JavaMailSender javaMailSender;
    /**
     * 发送人固定
     * 就把username注入到Bean中来
     */
    @Value("${spring.mail.username}")
    private String from;

    public void sentMail(String to, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            javaMailSender.send(helper.getMimeMessage());
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
        }
    }
}
