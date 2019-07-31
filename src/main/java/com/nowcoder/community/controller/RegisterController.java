package com.nowcoder.community.controller;

import com.nowcoder.community.Service.UserService;
import com.nowcoder.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class RegisterController {
    @Autowired
    private UserService userService;

    @RequestMapping(path="/register", method = RequestMethod.GET)
    public String getRegister() {
        return "/site/register";
    }
    @RequestMapping(path="/register", method = RequestMethod.POST)
    public String doRegister(Model model, User user) {
        Map<String,Object> map = userService.register(user);
        //成功
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg","注册成功，已经向您发送了一封激活邮件，尽快激活");
            model.addAttribute("target","/index");
            return "site/operate-result";
        }else {
            //失败,返回注册页面，对/site/register模板进行处理
            model.addAttribute("usernameMsg",map.get("usernameMessage"));
            model.addAttribute("passwordMsg",map.get("passwordMessage"));
            model.addAttribute("emailMsg",map.get("emailMessage"));
            return "/site/register";
        }
    }
}
