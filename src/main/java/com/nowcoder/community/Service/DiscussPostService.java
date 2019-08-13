package com.nowcoder.community.Service;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 *数据访问层开发完之后就开发业务层
 * Service层可以调用DAO
 * 降低层与层之间的耦合度
 * 开发完Service层之后就要开发视图层Controller->HomeController
 */
@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    /**
     *
     * @param userId 这是外键,和user表相关联，1、在写sql的时候关联，就是把用户的数据也一起查
     *               2、单独针对每一个DiscussPost，查一下对应的USER，将两个组合在一起，方便使用redis缓存数据
     *               方法二需要一个新的Service来执行查询UserId操作，
     *               因为他是对User进行的操作，所以就新建一个Service，UserService
     * @param offset
     * @param limit
     * @return
     */
    public List<DiscussPost> findDiscussPosts(int userId,int offset,int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    /**
     *
     * @param userId
     * @return
     */
    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 转义HTML标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        // 过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));
        return discussPostMapper.insertDiscussPost(post);
    }
    //通过discussPostId找到DiscussPost
    public DiscussPost findDiscussPostById(int discussPostId) {
        return discussPostMapper.selectDiscussPostById(discussPostId);
    }
}
