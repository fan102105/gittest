package com.itheima.travel.entity;

import java.io.Serializable;
import java.util.List;

public class PageBean implements Serializable {
    private Integer begin;  //首页
    private Integer prev;   //上一页
    private Integer current;  //当前页
    private Integer next;     //下一页
    private Integer pageCount;  //总页数
    private Integer total;     //总记录数
    private Integer pageSize;  //当前页记录数
    private List<Route> routes;  //当前页的对象集合

    public PageBean() {
    }

    public PageBean(Integer current, Integer total, Integer pageSize, List<Route> routes) {
        this.current = current;
        this.total = total;
        this.pageSize = pageSize;
        this.routes = routes;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getBegin() {
        return 1;
    }

    public void setBegin(Integer begin) {
        this.begin = begin;
    }

    public Integer getPrev() {
        if (getCurrent() > 1) {
            return getCurrent() - 1;
        } else {
            return 1;
        }
    }

    public void setPrev(Integer prev) {
        this.prev = prev;
    }

    public Integer getNext() {
        if (getCurrent() < getPageCount()) {
            return getCurrent() + 1;
        } else {
            return getCurrent();
        }
    }

    public void setNext(Integer next) {
        this.next = next;
    }

    public Integer getPageCount() {
        return getTotal() % getPageSize() == 0 ? getTotal() / getPageSize() : (getTotal() / getPageSize() + 1);
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
