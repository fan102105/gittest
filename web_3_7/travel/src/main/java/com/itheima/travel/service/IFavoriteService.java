package com.itheima.travel.service;

import com.itheima.travel.entity.Favorite;

public interface IFavoriteService {
    Favorite findFavoriteByUidAndRid(int uid, int rid);

    boolean rmFavorite(int uid, int rid);
}
