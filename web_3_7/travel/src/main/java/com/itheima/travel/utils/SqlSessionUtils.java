package com.itheima.travel.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * 会话工具类
 */
public class SqlSessionUtils {
    private static SqlSessionFactory factory;

    private static ThreadLocal<SqlSession> localSessions = new ThreadLocal<SqlSession>();

    static {
        //实例化工厂建造类
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        //读取核心配置文件
        try (InputStream inputStream = Resources.getResourceAsStream("sqlMapConfig.xml")) {
            //创建工厂对象
            factory = builder.build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到会话对象
     * @return 会话对象
     */
    public static SqlSession getSession() {
        //从容器中得到会话
        SqlSession session = localSessions.get();
        //如果会话为空，则从工厂中创建会话，并且放入容器中
        if (session == null) {
            session = factory.openSession();
            localSessions.set(session);
        }
        return session;
    }

    /**
     * 得到工厂对象
     * @return 会话工厂对象
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        return factory;
    }

    /**
     * 关闭会话
     */
    public static void closeSession() {
        //从容器中得到会话
        SqlSession session = localSessions.get();
        //如果会话不会为空，则关闭会话，并且从容器中删除会话对象
        if (session != null) {
            session.close();
            localSessions.remove();
        }
    }
}