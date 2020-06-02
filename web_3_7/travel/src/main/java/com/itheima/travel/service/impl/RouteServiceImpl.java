package com.itheima.travel.service.impl;

import com.itheima.travel.dao.IRouteDao;
import com.itheima.travel.entity.Favorite;
import com.itheima.travel.entity.PageBean;
import com.itheima.travel.entity.Route;
import com.itheima.travel.service.IRouteService;
import com.itheima.travel.utils.DaoInstanceFactory;
import com.itheima.travel.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteServiceImpl implements IRouteService {
    private IRouteDao routeDao = DaoInstanceFactory.getBean(IRouteDao.class);

    @Override
    public Route findRouteDetailsByRid(String rid) {

        if (rid != null && !rid.equals("")) {
            int id = Integer.parseInt(rid);
            return routeDao.findRouteByRid(id);
        } else {
            return null;
        }

    }

    @Override
    public PageBean findByCondtion(int cid, int current, String rname) {
        int pageSize = 5;
        Integer start = (current - 1) * pageSize;
        Map<String, Object> map = new HashMap<>();
        map.put("cid", cid);
        map.put("rname", rname);

        int total = routeDao.findTotal(map);
        map.put("start", start);
        map.put("pageSize", pageSize);
        List<Route> routes = routeDao.findByCondition(map);

        PageBean pageBean = new PageBean(current, total, pageSize, routes);
        return pageBean;
    }

    @Override
    public List<Route> findFavByCname(String cname) {

        return routeDao.findFavByCname(cname);
    }

    @Override
    public List<Route> findFavRoutes() {
        List<Route> routes = routeDao.findFavRoutes();
        return routes;
    }

    @Override
    public List<Route> findNewRoutes() {
        return routeDao.findNewRoutes();
    }

    @Override
    public List<Route> findThemeRoutes() {
        return routeDao.findThemeRoutes();
    }

    @Override
    public int addFavorate(int rid, int uid) {
        SqlSession session = SqlSessionUtils.getSession();
        IRouteDao routeDao = session.getMapper(IRouteDao.class);
        Timestamp date = new Timestamp(System.currentTimeMillis());
        try {
            routeDao.addFavorate(rid, uid, date);
            int i = routeDao.updateRoute(rid);
            session.commit();
            return i;
        } catch (Exception e) {
            session.rollback();
            throw new RuntimeException(e);
        } finally {
            SqlSessionUtils.closeSession();
        }
    }

    @Override
    public PageBean findRoutesByPage(int uid, int currentPage) {
        int pageSize = 4;
        List<Route> routes = routeDao.findRoutesByUid(uid, (currentPage - 1) * pageSize, pageSize);
        Map<String, Object> map = new HashMap<>();
        int total = routeDao.findFavTotal(uid);
        PageBean pageBean = new PageBean();
        pageBean.setPageSize(pageSize);
        pageBean.setCurrent(currentPage);
        pageBean.setRoutes(routes);
        pageBean.setTotal(total);
        return pageBean;
    }

    @Override
    public PageBean findFavRoutesByPage(int currPage, String rname, Double startPrice, Double endPrice) {
        PageBean pageBean = new PageBean();
        int pageSize = 8;
        Map<String, Object> map = new HashMap<>();
        map.put("rname", rname);
        map.put("startPrice", startPrice);
        map.put("endPrice", endPrice);
        int totalCounts = routeDao.findRouteTotal(map);

        map.put("begin", (currPage - 1) * pageSize);
        map.put("pageSize", pageSize);
        List<Route> routes = routeDao.findRoutesByConditions(map);

        pageBean.setTotal(totalCounts);
        pageBean.setRoutes(routes);
        pageBean.setPageSize(pageSize);
        pageBean.setCurrent(currPage);
        return pageBean;
    }

    @Override
    public Favorite findFavByUidAndRid(int rid, int uid) {
        return routeDao.findFavByUidAndRid(rid, uid);
    }


}
