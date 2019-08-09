package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 起到多线程环境下容器的作用，因为它线程隔离
 * 持有用户信息,用于代替session对象.
 */
@Component
public class HostHolder {
    //存的是每个线程对应的user对象
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        /**
         * Thread t = Thread.currentThread();
         *         ThreadLocalMap map = getMap(t);
         *         if (map != null) {
         *             map.set(this, value);
         *         } else {
         *             createMap(t, value);
         *         }
         *
         * 获取当前线程，根据当前线程获取Map对象
         * 向map种set，
         * 以线程为key存储值
         */
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        /**
         * 线程在就有数据
         * 没有就没有数据
         */
        users.remove();
    }

}
