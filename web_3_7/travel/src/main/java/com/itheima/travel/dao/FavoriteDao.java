package com.itheima.travel.dao;

import com.itheima.travel.entity.Favorite;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface FavoriteDao {
    @Select("select * from tab_favorite where uid = #{uid} and rid = #{rid}")
    Favorite findFavoriteByUidAndRid(@Param("uid") int uid, @Param("rid") int rid);

    @Select("delete from tab_favorite where uid = #{uid} and rid = #{rid}")
    void rmFavorite(@Param("uid") int uid, @Param("rid") int rid);

    @Update("update tab_route set count = count - 1 where rid = #{rid}")
    void updateRouteCount(int rid);
}
