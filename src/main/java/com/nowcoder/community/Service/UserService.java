package com.nowcoder.community.Service;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtils;
import com.nowcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Service层DiscussPostService服务，需要执行查询UserId操作
 * 因为他是对User进行的操作，所以就新建了一个Service，UserService
 */
@Service
public class UserService {
    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private UserMapper userMapper;

    @Value("${community.path.domain}")
    private String domain;


    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    /**
     * controller会调用这个方法，并且传入数据，使用User类封装
     * 提交注册数据
     *1、空值处理 user ？=null，或者判断账号，密码，邮箱是否为空
     *2、验证账号，验证邮箱
     *3、设置salt，设置加密的密码，设置其他字段
     *4、
     * @param user
     * @return Map<String,Object>
     */
    public Map<String,Object> register(User user) {
        /**
         * 实例化Map
         */
        Map<String,Object> map = new HashMap<>();
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        /**
         * 判断账号，密码，邮箱是否为空
         */
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMessage", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMessage", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMessage", "邮箱不能为空");
        }
        /**
         * 判断账号，邮箱是否重复
         */
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMessage", "账号已存在");
        }
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMessage", "邮箱已存在");
        }

        /**
         * 设置salt，设置加密的密码
         * 设置其他字段
         */
        user.setSalt(CommunityUtils.generateUUID().substring(0,5));
        user.setPassword(CommunityUtils.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtils.generateUUID());
        user.setCreateTime(new Date());
        user.setHeaderUrl(String.format("images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        /**
         * 给用户发送激活邮件
         * 1、mail/activation.html 模板
         *
         */
        /**
         * 给用户发送激活邮件
         * 要访问模板，mail/activation.html 模板 需要给模板传参数
         * 这里不能用DispatcherServlet，类似mailTests，为什么不能用DispatcherServlet
         * 1.引入实例化配置
         * 2.设置变量名字
         * 3.
         */
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        String url = domain + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url",url);
        String process = templateEngine.process("/mail/activation", context);
        mailClient.sentMail(user.getEmail(),"激活账号",process);

        return map;
    }
}
