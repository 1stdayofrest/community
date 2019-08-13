package com.nowcoder.community.controller;

import com.nowcoder.community.Service.DiscussPostService;
import com.nowcoder.community.Service.UserService;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtils;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * 处理帖子的Controller
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    /**
     * 发表帖子
     * @param title
     * @param content
     * @return 返回JSON格式字符串
     */
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        //判断有没有登录
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtils.getJSONString(403, "你还没有登录哦!");
        }
        //创建DiscussPost对象用来封装传入的数据
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        //调用discussPostService.addDiscussPost（）将DiscussPost写入数据库
        discussPostService.addDiscussPost(post);

        // 报错的情况,将来统一处理.
        return CommunityUtils.getJSONString(0, "发布成功!");
    }

    /**
     * 显示帖子详情
     * @param discussPostId
     * @param model
     * @return
     */
    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model) {
        // 帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        // 作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        return "/site/discuss-detail";
    }

}
