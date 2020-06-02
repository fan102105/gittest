package com.itheima.travel.utils;

import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Proxy;

/*
得到DAO的实现类
 */
public class DaoInstanceFactory {

    /**
     * 返回接口的代理对象
     * @param daoInterface 被代理的DAO接口
     */
    public static <T> T getBean(Class<T> daoInterface) {
        return (T) Proxy.newProxyInstance(
                DaoInstanceFactory.class.getClassLoader(),
                new Class[]{daoInterface},
                (proxy, method, args) -> {
                    //得到会话
                    SqlSession session = SqlSessionUtils.getSession();
                    //创建DAO接口实现
                    T mapper = session.getMapper(daoInterface);
                    //调用DAO接口中的方法
                    Object retValue = method.invoke(mapper, args);
                    //提交事务
                    session.commit();
                    //关闭会话
                    SqlSessionUtils.closeSession();
                    //返回值
                    return retValue;
                });
    }
}
