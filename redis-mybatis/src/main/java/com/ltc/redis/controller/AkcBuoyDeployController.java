package com.ltc.redis.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ltc.redis.entity.AkcBuoyDeploy;
import com.ltc.redis.mapper.AkcBuoyDeployMapper;
import com.ltc.redis.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * cms_配置_浮标 前端控制器
 * </p>
 *
 * @author ltc
 * @since 2019-03-12
 */
@RestController
@RequestMapping("/redis/akc-buoy-deploy")
public class AkcBuoyDeployController {

    @Autowired
    private AkcBuoyDeployMapper akcBuoyDeployMapper;

    @Autowired
    private RedisUtils redisUtils;

    @PostConstruct
    public void init() {
        //默认无限期
        redisUtils.set("buoy", JSONObject.toJSONString(akcBuoyDeployMapper.selectList(null)));
    }

    /**
     * http://localhost:8081/redis/akc-buoy-deploy/selectList
     *
     * @return
     */
    @GetMapping("/selectList")
    public List<AkcBuoyDeploy> selectList() {
        return akcBuoyDeployMapper.selectList(null);
    }

    /**
     * @param key
     * @return http://localhost:8081/redis/akc-buoy-deploy/getkey?key=buoy
     */
    @GetMapping("/getkey")
    public List<AkcBuoyDeploy> getkey(String key) {
        String json = redisUtils.get(key);
        System.out.println(json);
        //
        List<AkcBuoyDeploy> akcBuoyDeploys = JSONObject.parseArray(json, AkcBuoyDeploy.class);
        return akcBuoyDeploys;
    }

    /**
     *
     */
    @GetMapping("insert")
    public void insert() {

    }

    public static void main(String[] args) {
        //
        List<Object> list = Lists.newArrayList();
        list.add(1);
        list.add(2);
        String json = JSONObject.toJSONString(list);
        System.out.println(json);
        //
        List<AkcBuoyDeploy> list1 = new ArrayList<>();
        list1.add(new AkcBuoyDeploy().setName("zhangsan"));
        list1.add(new AkcBuoyDeploy().setChannel("app"));
        System.out.println(JSONObject.toJSONString(list1));
    }
}
