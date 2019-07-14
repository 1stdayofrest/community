package com.nowcoder.community.dao;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 为了让Spring容器装配这个bean
 * 可以使用@Repository,但是mybatis也有注解可以标识，Mapper
 * 要想实现它，我们需要给它提供一个配置文件
 * 配置文件里面需要给每一个方法提供它所需要的sql
 * 就在resources/mapper/下创建xml文件
 *
 */
@Mapper
@Repository
public interface UserMapper {
    /**
     * 根据业务需要来写
     */
    //查询，返回User对象
    User selectById(int id);
    User selectByName(String username);
    User selectByEmail(String email);
    //增加User,返回插入数据行数
    int insertUser(User user);
    int updateStatus(int id ,int status);
    //修改User的密码，返回修改的行数
    int updatePassword(int id, String password);
    int updateHeader(int id, String headerUrl);
}
