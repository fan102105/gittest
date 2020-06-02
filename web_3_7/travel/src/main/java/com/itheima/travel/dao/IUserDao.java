package com.itheima.travel.dao;

import com.itheima.travel.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface IUserDao {
    @Select("select * from tab_user where username = #{username} and password = #{password}")
    User login(@Param("username") String username, @Param("password") String password);

    @Insert("insert into tab_user values (null,#{username},#{password},#{name},#{birthday},#{sex},#{telephone},#{email})")
    int addUser(User user);

    @Select("select * from tab_user where username = #{username}")
    User findUserByUsername(String username);
}
