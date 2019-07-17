package com.nowcoder.community.controller;

import com.nowcoder.community.Service.DiscussPostService;
import com.nowcoder.community.Service.UserService;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller的访问路径可以省略
 * 注入Service
 * @RequestMapping(path = "/index",method = RequestMethod.GET)，增加处理请求的方法
 * 定义访问路径，“/index”
 * 不加@ResponseBody，因为响应的是html，可以返回ModelAndView,也可以是String（视图的名字）
 *
 */
@Controller
public class HomeController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    /**
     *
     * @param model 使用model给模板传递数据
     * @return 返回的是模板的路径 template/index.html，返回“/index”
     * Map<String,Object>来封装DiscussPost对象和User对象
     * 在加到List当中，
     * 把要给页面展示的数据放到model里面model.addAttribute("discussPosts", discussPosts);
     * control完成之后，最后一步，处理模板
     * Page类当中封装分页相关信息
     */
    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndexPage(Model model) {
        List<DiscussPost> list = discussPostService.findDiscussPosts(0,0,10);
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post:list){
                Map<String,Object> map = new HashMap<>();
                map.put("post",post);
                User user = userService.findUserById(post.getUserId());
                map.put("user",user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        return "/index";
    }
}
