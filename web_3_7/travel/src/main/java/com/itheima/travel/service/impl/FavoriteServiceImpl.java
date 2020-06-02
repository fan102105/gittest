package com.itheima.travel.service.impl;

import com.itheima.travel.dao.FavoriteDao;
import com.itheima.travel.entity.Favorite;
import com.itheima.travel.service.IFavoriteService;
import com.itheima.travel.utils.DaoInstanceFactory;
import com.itheima.travel.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;

public class FavoriteServiceImpl implements IFavoriteService {

    @Override
    public Favorite findFavoriteByUidAndRid(int uid, int rid) {
        FavoriteDao favoriteDao = DaoInstanceFactory.getBean(FavoriteDao.class);
        Favorite favorite = favoriteDao.findFavoriteByUidAndRid(uid, rid);
        return favorite;
    }

    @Override
    public boolean rmFavorite(int uid, int rid) {
        SqlSession session = SqlSessionUtils.getSession();
        FavoriteDao favoriteDao = session.getMapper(FavoriteDao.class);
        try {
            favoriteDao.rmFavorite(uid, rid);
            favoriteDao.updateRouteCount(rid);
            session.commit();
            return true;
        } catch (Exception e) {
            session.rollback();
            return false;
        } finally {
            SqlSessionUtils.closeSession();
        }
    }
}
