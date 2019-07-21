package com.nowcoder.community.entity;

/**
 * 封装分页相关信息
 * 在set的时候，要进行判断避免传入不合理数据
 * setCurrent不能是负数，limit不能太大，必须大于1
 * setRows>= 0
 * 还需要补充几个getOffset：获取当前页的起始行 （current - 1）*limit
 *getTotal：获取总页数rows/limit （+1）
 * getFrom return current - 2  < 1 ? 1 : current - 2;
 * getTo return current + 2 > total?  total : current + 2 ; int total = getTotal();
 * 这些参数都是在哪里传进去，又是怎么用的？ 下一步改造HomeController
 */
public class Page {
    //当前页码
    private int current = 1;

    //每页数据显示上限
    private int limit = 10;

    //从服务端查询到的数据总数（用于计算总的页数）
    private int rows;

    //查询路径(用于复用分页链接)
    private String path;

    public int getCurrent() {
        return current;
    }

    /**
     * setCurrent不能是负数
     * @param current
     */
    public void setCurrent(int current) {
        if (current >0){
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    /**
     *
     * @param limit
     */
    public void setLimit(int limit) {
        if (limit <= 100 && limit >0){
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0){
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     * @return
     */
    public int getOffset() {
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     * @return
     */
    public int getTotal() {
        if (rows % limit == 0)
            return rows/limit;
        return rows/limit + 1;
    }

    /**
     * 按钮从第几页开始显示,到第几页结束
     * @return
     */
    public int getFrom() {
        return current - 2  < 1 ? 1 : current - 2;
    }
    public int getTo() {
        int total = getTotal();
        return  current + 2 > total?  total : current + 2;
    }
}
