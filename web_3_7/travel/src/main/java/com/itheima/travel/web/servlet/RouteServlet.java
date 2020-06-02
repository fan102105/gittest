package com.itheima.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.travel.entity.Favorite;
import com.itheima.travel.entity.PageBean;
import com.itheima.travel.entity.Route;
import com.itheima.travel.entity.User;
import com.itheima.travel.service.IRouteService;
import com.itheima.travel.service.impl.RouteServiceImpl;
import com.itheima.travel.utils.JedisUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    protected void findByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //当前页的值
        String currentStr = request.getParameter("current");
        if (currentStr == null || currentStr.equals("")) {
            currentStr = "1";
        }
        int current = Integer.parseInt(currentStr);

        //路线查询的名称
        String rname = request.getParameter("rname");

        //获取类别id
        String cidStr = request.getParameter("cid");
        if (cidStr == null || cidStr.equals("")) {
            cidStr = "0";
        }
        IRouteService routeService = new RouteServiceImpl();
        PageBean pageBean = routeService.findByCondtion(Integer.parseInt(cidStr), current, rname);
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(pageBean);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(s);
    }

    protected void findRouteDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        IRouteService routeService = new RouteServiceImpl();
        Route route = routeService.findRouteDetailsByRid(rid);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        String value = mapper.writeValueAsString(route);
        out.print(value);
    }

    //判断session中是否有user对象
    protected void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        String ridStr = request.getParameter("rid");
        int rid = Integer.parseInt(ridStr);
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        if (user != null) {
            IRouteService routeService = new RouteServiceImpl();
            int uid = user.getUid();
            Favorite fav = routeService.findFavByUidAndRid(rid, uid);
            if (fav == null) {
                int i = routeService.addFavorate(rid, uid);
                if (i > 0) {
                    out.write("success");
                } else {
                    out.write("false");
                }
            } else {
                out.print("false");
            }
        } else {
            out.write("none");
        }
    }

    //判断session中是否有user对象
    protected void checkByUidAndRid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ridStr = request.getParameter("rid");
        int rid = Integer.parseInt(ridStr);
//        User user = (User) request.getSession().getAttribute("user");
        Jedis jedis = JedisUtils.getJedis();
        String s = jedis.get("user");
        User user = null;
        if (s != null) {
            ObjectMapper mapper = new ObjectMapper();
            user = mapper.readValue(s, User.class);
        }

        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        if (user != null) {
            IRouteService routeService = new RouteServiceImpl();
            int uid = user.getUid();
            Favorite favorite = routeService.findFavByUidAndRid(rid, uid);
            if (favorite != null) {
                out.print(true);
            } else {
                out.print(false);
            }
        } else {
            out.write("none");
        }

    }

    //根据分类类名查找6个精选路线
    protected void findFavByCname(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cname = request.getParameter("cname");
        IRouteService routeService = new RouteServiceImpl();
        List<Route> routes = routeService.findFavByCname(cname);
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(routes);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(s);
    }

    protected void findFavRoutes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        IRouteService routeService = new RouteServiceImpl();
        List<Route> favRoutes = routeService.findFavRoutes();
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(favRoutes);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(s);
    }

    protected void findNewRoutes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        IRouteService routeService = new RouteServiceImpl();
        List<Route> newRoutes = routeService.findNewRoutes();
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(newRoutes);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(s);
    }

    protected void findThemeRoutes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        IRouteService routeService = new RouteServiceImpl();
        List<Route> newRoutes = routeService.findThemeRoutes();
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(newRoutes);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(s);
    }

    protected void findMyFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String currentStr = request.getParameter("current");
        if (currentStr == null) {
            currentStr = "1";
        }
        int currPage = Integer.parseInt(currentStr);
        IRouteService routeService = new RouteServiceImpl();
        PageBean pageBean = routeService.findRoutesByPage(user.getUid(), currPage);
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(pageBean);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(s);
    }

    protected void findFavRoutesByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String currentStr = request.getParameter("currentPage");
        if (currentStr == null) {
            currentStr = "1";
        }
        int currPage = Integer.parseInt(currentStr);
        String rname = request.getParameter("rname");
        String startPriceStr = request.getParameter("startPrice");
        String endPriceStr = request.getParameter("endPrice");

        if (startPriceStr == null || startPriceStr.equals("")) {
            startPriceStr = "0";
        }
        Double startPrice = Double.parseDouble(startPriceStr);

        if (endPriceStr == null || endPriceStr.equals("")) {
            endPriceStr = "0";
        }
        Double endPrice = Double.parseDouble(endPriceStr);

        IRouteService routeService = new RouteServiceImpl();
        PageBean pageBean = routeService.findFavRoutesByPage(currPage, rname, startPrice, endPrice);
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(pageBean.getRoutes());
        String s = mapper.writeValueAsString(pageBean);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(s);
    }
}
