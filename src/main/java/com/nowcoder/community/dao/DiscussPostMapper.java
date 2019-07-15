package com.nowcoder.community.dao;


import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 使用@Repository注解才能被容器扫描接口，才能自动实现，装配
 * 通常一张表一个mapper
 * 对应discussPost类所对应的表
 * 作为数据库连接组件
 * 实现什么功能，就写什么方法
 * userId:用户id对应的帖子，外键，
 *分页limit：起始行行号，这一页最多显示数据
 * 需要在mapper目录下给该mapper新建一个配置文件discussPost_Mapper.xml
 */
@Repository
@Mapper
public interface DiscussPostMapper {
    /**
     *声明查询方法，在配置文件完成与之对应的动态sql
     * @param userId 用户id对应的帖子，外键，
     * @param offset 起始行行号
     * @param limit 这一页最多显示数据
     * @return 返回的是DiscussPost的对象集合
     */
    List<DiscussPost> selectDiscussPosts(int userId,int offset,int limit);

    /**
     * 必须使用@Param：如果需要动态sql，并且条件需要动态标签（使用<if></if>），并且该方法只有这一个参数就必须
     * 使用@Param注解，给该参数去别名
     * @param userId
     * @return 某个用户或者所有用户发的贴子
     */
    int selectDiscussPostRows(@Param("userId") int userId);
}
