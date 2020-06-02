package com.itheima.travel.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.travel.dao.ICategoryDao;
import com.itheima.travel.entity.Category;
import com.itheima.travel.service.ICategoryService;
import com.itheima.travel.utils.DaoInstanceFactory;
import com.itheima.travel.utils.JedisUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

public class CategoryServiceImpl implements ICategoryService {
    @Override
    public String findAll() {

        Jedis jedis = JedisUtils.getJedis();
        String categories = jedis.get("categories");
        if (categories == null || categories.length() == 0) {
            try {
                ICategoryDao categoryDao = DaoInstanceFactory.getBean(ICategoryDao.class);
                List<Category> list = categoryDao.findAll();
                ObjectMapper mapper = new ObjectMapper();
                categories = mapper.writeValueAsString(list);
                jedis.set("categories", categories);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        jedis.close();
        return categories;
    }
}
