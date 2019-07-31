package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 *
 * 1、生产随机字符串->上传文件，给文件生产随机名字/生成激活码->使用UUID工具
 * 2、MD5加密,只能加密不能解密，md5和哈希函数区别,在密码的基础上加上一个随机字符串，再进行加密
 */
public class CommunityUtils {
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }
    public static String md5(String key) {
        if(StringUtils.isBlank(key))
            return null;
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
