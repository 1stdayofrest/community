package com.nowcoder.community;

import org.junit.Test;

import java.util.*;

/**
 * 为什么要有泛型？？
 * 1、元素存储的安全性问题：在集合中没有泛型的时候，集合什么类型都可以装，将String类型
 * 加到集合中，
 * 2、获取元素的强制转换问题
 */
public class GenericDemo {
    @Test
    public void test2() {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> cur = new ArrayList<>();
        for (int i = 0; i < 3;i++) {
            cur.add(i);
            ans.add(cur);
            System.out.println(ans);
        }
        cur.remove(2);
        System.out.println(ans);

    }

    //1.在集合中没有使用泛型的时候
    @Test
    public void test1(){
        Map<String,Integer> map = new HashMap<>();
        map.put("AA",89);
        Set<Map.Entry<String, Integer>> set = map.entrySet();
        List list = new ArrayList();
        /**
         * 没有使用泛型，任何Object及其子类的对象都可以添加进来
         */
        list.add(89);
        list.add(86);
        list.add(98);
        list.add("AA");

        for (int i = 0; i < list.size(); i++){
            /**
             * 在取得时候就要进行强转，可能发生ClassCastException异常
             */
            int score = (Integer)list.get(i);
            System.out.println(score);
        }
    }
    //2.在集合中使用泛型
    /**
     * public interface List<E> extends Collection<E>
     * <E>就是要操作的类型，
     * 1、自定义泛型类，接口，方法
     * 2、集成与泛型的关系
     * 3、通配符
     */
}
