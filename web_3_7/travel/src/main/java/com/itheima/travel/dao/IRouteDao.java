package com.itheima.travel.dao;

import com.itheima.travel.entity.*;
import com.itheima.travel.exception.CustomerErrorMsgException;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface IRouteDao {

    List<Route> findByCondition(Map<String, Object> map);

    int findTotal(Map<String, Object> map);

    Route findRouteByRid(int rid);

    Category findCateByRid(int rid);

    List<RouteImg> findImgByRid(int rid);

    Seller findSellerBySid(int sid);

    @Update("update tab_route set count = (count + 1) where rid = #{rid}")
    int updateRoute(int rid);

    @Select("select * from tab_route t1 join tab_category t2 on t1.cid = t2.cid where t2.cname = #{cname} order by t1.count desc limit 0,6")
    List<Route> findFavByCname(String cname);

    @Select("select * from tab_route order by count desc limit 0,4")
    List<Route> findFavRoutes();

    @Select("select * from tab_route order by rdate desc limit 0,4")
    List<Route> findNewRoutes();

    @Select("select * from tab_route where isThemeTour = 1 limit 0,4")
    List<Route> findThemeRoutes();

    @Insert("insert into tab_favorite values(#{rid},#{date},#{uid})")
    void addFavorate(@Param("rid") int rid, @Param("uid") int uid, @Param("date") Timestamp date);

    @Select("select * from tab_route r join tab_favorite f on r.rid = f.rid join tab_user u on f.uid = u.uid where u.uid = #{uid} order by r.rdate limit #{start},#{pageSize}")
    List<Route> findRoutesByUid(@Param("uid") int uid, @Param("start") int start, @Param("pageSize") int pageSize);

    @Select("select count(1) from tab_favorite where uid = #{uid}")
    int findFavTotal(int uid);

    int findRouteTotal(Map<String, Object> map);

    List<Route> findRoutesByConditions(Map<String, Object> map);

    @Select("select * from tab_favorite f join tab_user u on f.uid = u.uid where f.rid = #{rid} and f.uid = #{uid}")
    Favorite findFavByUidAndRid(@Param("rid") int rid, @Param("uid") int uid);

    @Select("select count from tab_route where rid = #{rid}")
    int findCountByRid(int rid);
}
