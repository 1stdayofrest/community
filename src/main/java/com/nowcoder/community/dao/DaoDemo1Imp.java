package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

/**
 * 实现DAO接口，这个容器是不能扫描到
 * 必须加注解
 * 访问数据库的Bean加Repository
 * 回到测试类当中，测试
 */
@Repository
public class DaoDemo1Imp implements DaoDemo1 {
    @Override
    public String select() {
        return "Hello Dao";
    }
}
