package com.itheima.travel.dao;

import com.itheima.travel.entity.Category;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ICategoryDao {
    @Select("select * from tab_category")
    List<Category> findAll();
}
