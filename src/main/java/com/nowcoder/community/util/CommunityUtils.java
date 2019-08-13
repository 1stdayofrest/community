package com.nowcoder.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.Map;
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

    /**
     * 用于ajax
     * 获取JSON字符串
     * @param code JSON编号
     * @param Msg 返回的信息
     * @param map 封装在Map中的信息
     * @return 返回的JSON格式的字符串
     */
    public static String getJSONString(int code, String Msg, Map<String, Object> map) {
        //TODO 新建JSON对象
        JSONObject json = new JSONObject();
        //TODO 把传入的参数装到JSON对象中
        json.put("code",code);
        json.put("Msg",Msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        //TODO 返回JSON字符串
        return json.toString();
    }
    public static String getJSONString(int code, String Msg) {
        return getJSONString(code, Msg, null);
    }
    public static String getJSONString(int code) {
        return getJSONString(code,null,null);
    }
}
