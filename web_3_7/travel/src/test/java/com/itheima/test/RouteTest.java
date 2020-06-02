package com.itheima.test;

import com.itheima.travel.dao.IRouteDao;
import com.itheima.travel.entity.*;
import com.itheima.travel.service.IFavoriteService;
import com.itheima.travel.service.IRouteService;
import com.itheima.travel.service.impl.FavoriteServiceImpl;
import com.itheima.travel.service.impl.RouteServiceImpl;
import com.itheima.travel.utils.DaoInstanceFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class RouteTest {
    private IRouteDao routeDao;

    @Before
    public void begin() {
        routeDao = DaoInstanceFactory.getBean(IRouteDao.class);
    }

    @Test
    public void testFindRouteImg() {
        List<RouteImg> imgs = routeDao.findImgByRid(6);
        System.out.println(imgs);
    }

    @Test
    public void testFindSeller() {
        IRouteDao routeDao = DaoInstanceFactory.getBean(IRouteDao.class);
        Seller seller = routeDao.findSellerBySid(1);
        System.out.println(seller);
    }

    @Test
    public void testUpdateRoute() {
        routeDao.updateRoute(1);
    }

    @Test
    public void testFindFav() {
        IRouteService routeService = new RouteServiceImpl();
        List<Route> list = routeService.findFavByCname("国内游");
        System.out.println(list);
    }

    @Test
    public void testFindFavRoutes() {
        IRouteService routeService = new RouteServiceImpl();
        List<Route> favRoutes = routeService.findFavRoutes();
        System.out.println(favRoutes);
    }


    @Test
    public void testFindRoutesByPage() {
        IRouteService routeService = new RouteServiceImpl();
        PageBean pageBean = routeService.findRoutesByPage(5, 1);
        System.out.println(pageBean.getRoutes());
    }

    @Test
    public void testCheckFavorite() {
        IRouteService routeService = new RouteServiceImpl();
        Favorite fa = routeService.findFavByUidAndRid(4, 5);
        System.out.println(fa);
    }

    @Test
    public void testRmFavorite(){
        IFavoriteService favoriteService = new FavoriteServiceImpl();
        favoriteService.rmFavorite(5,3);
    }
}
