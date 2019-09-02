package com.nowcoder.community.controller;

import com.nowcoder.community.Service.CommentService;
import com.nowcoder.community.Service.DiscussPostService;
import com.nowcoder.community.Service.UserService;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtils;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 处理帖子的Controller
 * @author lizhaoquan
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

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
     * 帖子有评论 评论也有评论
     * 习惯把discussPostId拼到路径中
     * 显示帖子详情
     * @param discussPostId
     * @param model 只要是实体类型（JAVA Bean），声明在条件当中作为一个参数，
     *              最终都会SpringMVC都会把Bean存到Model里
     * @param page 接收整理分页条件
     * @return
     */
    @GetMapping("/detail/{discussPostId}")
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
        // 帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        // 作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);
        // 评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount());

        /**
         *
         *     查询 id = post.getId()的帖子的所有评论，获得 评论列表commentList
         *     如果 commentList != null
         *        将 commentList里的所有评论遍历，对每一个评论：
         *             将得到评论对象comment，评论作者对象user，
         *             获得每个评论的回复列表replyList，如果replyList ！= null ，遍历replyList，对每一个回复：
         *                 将 回复对象 reply ，回复作者对象 user，（if targetId ！= 0） 回复目标对象 target
         *                 装到 回复视图对象 Map<String, Object> replyVo中
         *                 将 replyVo 加到 回复视图对象列表 List<Map<String, Object>> replyVoList 当中
         *              这样每个 评论对应一个 replyVoList ，
         *              将回复视图对象列表replyVoList，评论对象comment，评论作者对象user，评论回复数量
         *             装到 评论视图对象Map<String, Object> commentVo当中
         *             将 commentVo 装到 评论视图对象 列表 List<Map<String, Object>> commentVoList 当中
         *
         *
         */

        // 评论: 给帖子的评论
        // 回复: 给评论的评论
        // 评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(
                ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        // 评论VO列表 commentList 装 （评论VO commentVoList 装（），）
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 评论VO
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 作者
                commentVo.put("user", userService.findUserById(comment.getUserId()));

                // 回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                // 回复VO列表 replyList 装 replyVoList
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>(16);
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复目标，判断targetId
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }
        /**
         * 将 commentVoList 装到Model当中
         */
        model.addAttribute("comments", commentVoList);

        return "/site/discuss-detail";
    }

}
