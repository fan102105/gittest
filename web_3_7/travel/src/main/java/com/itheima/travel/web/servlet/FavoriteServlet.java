package com.itheima.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.travel.entity.Favorite;
import com.itheima.travel.entity.User;
import com.itheima.travel.service.IFavoriteService;
import com.itheima.travel.service.impl.FavoriteServiceImpl;
import com.itheima.travel.utils.JedisUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/favorite/*")
public class FavoriteServlet extends BaseServlet {
    protected void rmFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ridStr = request.getParameter("rid");
        int rid = Integer.parseInt(ridStr);
//        User user = (User) request.getSession().getAttribute("user");
        Jedis jedis = JedisUtils.getJedis();
        String s = jedis.get("user");
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(s, User.class);
        int uid = user.getUid();
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        IFavoriteService favoriteService = new FavoriteServiceImpl();

        boolean b = favoriteService.rmFavorite(uid, rid);
        if (b) {
            out.write("success");
        } else {
            out.write("false");
        }
    }

}
