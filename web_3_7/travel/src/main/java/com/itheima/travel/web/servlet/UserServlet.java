package com.itheima.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.travel.entity.User;
import com.itheima.travel.exception.CustomerErrorMsgException;
import com.itheima.travel.service.IUserService;
import com.itheima.travel.service.impl.UserServiceImpl;
import com.itheima.travel.utils.JedisUtils;
import org.apache.commons.beanutils.BeanUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private IUserService userService = new UserServiceImpl();

    //注册用户
    protected void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        IUserService userService = new UserServiceImpl();
        //String vcode = (String) session.getAttribute("vcode");
        Jedis jedis = JedisUtils.getJedis();
        String vcode = jedis.get("vcode");
        String check = request.getParameter("check");
        //session.removeAttribute("vcode");
        jedis.del("vcode");
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        if (!vcode.equalsIgnoreCase(check)) {
            out.write("验证码有误");
        } else {
            Map<String, String[]> map = request.getParameterMap();
            User user = new User();
            try {
                BeanUtils.populate(user, map);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            int row = userService.register(user);
            if (row > 0) {
                out.write("success");
            } else {
                out.write("注册失败！");
            }
        }
    }

    //登录查询用户信息
    protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String checkbox = request.getParameter("checkbox");

        //从cookie中获取checkbox的状态，如果是on则从session中获取用户信息并跳转到首页，如果session中没信息，则正常
        if ("on".equals(checkbox)) {
            Cookie cookie = new Cookie("username", username);
            Cookie cookie2 = new Cookie("password", password);
            cookie.setMaxAge(60 * 60 * 12);
            cookie2.setMaxAge(60 * 60 * 12);
            response.addCookie(cookie);
            response.addCookie(cookie2);
        }
        //String vcode = (String) request.getSession().getAttribute("vcode");
        Jedis jedis = JedisUtils.getJedis();
        String vcode = jedis.get("vcode");
        jedis.del("vcode");
        String check = request.getParameter("check");
        if (!vcode.equalsIgnoreCase(check)) {
            out.write("验证码错误");
        } else {
            try {
                User login = userService.login(username, password);
                request.getSession().setAttribute("user", login);
                ObjectMapper mapper = new ObjectMapper();
                String user = mapper.writeValueAsString(login);
                jedis.set("user", user);
                out.write("success");
            } catch (CustomerErrorMsgException e) {
                out.write(e.getMessage());
            }
        }

    }

    //获取session中用户信息
    protected void getUserData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("user");
        Jedis jedis = JedisUtils.getJedis();
        String s = jedis.get("user");
        User user = null;
        if (s != null) {
            ObjectMapper mapper = new ObjectMapper();
            user = mapper.readValue(s, User.class);
        }
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        if (user != null) {
            String username = user.getUsername();
            out.write(username);
        } else {
            out.write("none");
        }
    }

    //退出
    protected void loginOut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        Jedis jedis = JedisUtils.getJedis();
        jedis.del("user");
        response.sendRedirect(request.getContextPath() + "/index.html");
    }

    //自动登录
    protected void autoLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        boolean flag = false;
        for (Cookie cookie : cookies) {
            if (cookie != null) {
                try {
                    String name = cookie.getName();
                    String username = "";
                    String password = "";
                    if ("username".equals(name)) {
                        username = cookie.getValue();
                    }
                    if ("password".equals(name)) {
                        password = cookie.getValue();
                    }
                    User user = userService.login(username, password);
                    if (user != null) {
                        flag = true;
                        break;
                    }
                } catch (CustomerErrorMsgException e) {
                    e.printStackTrace();
                }
            }
        }
        if (flag) {
            out.write("success");
        } else {
            out.write("none");
        }
    }

    //查询用户名是否存在
    protected void checkUserName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        IUserService userService = new UserServiceImpl();
        User user = userService.findUserByUsername(username);
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();
        if (user != null) {
            username = user.getUsername();
            out.write(username);
        } else {
            out.write("none");
        }
    }


}
