package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lizhaoquan
 */
@Mapper
public interface CommentMapper {
    /**
     * 根据实体（entity）来查询 评论
     * @param entityType 实体类型
     * @param entityId 实体id
     * @param offset 分页
     * @param limit 每一页的行数
     * @return Comment
     */
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    /**
     * 查询评论数据的行数
     * @param entityType
     * @param entityId
     * @return
     */
    int selectCountByEntity(int entityType, int entityId);

    /**
     * 插入评论
     * @param comment
     * @return
     */
    int insertComment(Comment comment);

}
