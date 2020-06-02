package com.itheima.travel.service;

import com.itheima.travel.entity.Favorite;
import com.itheima.travel.entity.PageBean;
import com.itheima.travel.entity.Route;

import java.util.List;

public interface IRouteService {

    Route findRouteDetailsByRid(String rid);

    PageBean findByCondtion(int cid, int current, String rname);

    List<Route> findFavByCname(String cname);

    List<Route> findFavRoutes();

    List<Route> findNewRoutes();

    List<Route> findThemeRoutes();

    int addFavorate(int rid, int uid);

    PageBean findRoutesByPage(int uid, int currentPage);

    PageBean findFavRoutesByPage(int currPage, String rname, Double startPriceStr, Double endPriceStr);

    Favorite findFavByUidAndRid(int rid, int uid);
}
