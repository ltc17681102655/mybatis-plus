package com.ltc.redis.controller;


import com.ltc.redis.entity.AkcBuoyLevel;
import com.ltc.redis.mapper.AkcBuoyLevelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 浮标与用户等级关联表 前端控制器
 * </p>
 *
 * @author ltc
 * @since 2019-03-13
 */
@RestController
@RequestMapping("/redis/akc-buoy-level")
public class AkcBuoyLevelController {

    @Autowired
    private AkcBuoyLevelMapper akcBuoyLevelMapper;

    @GetMapping("/test-levet")
    public List<AkcBuoyLevel> demo() {
        List<AkcBuoyLevel> akcBuoyLevels = akcBuoyLevelMapper.selectList(null);
        return akcBuoyLevels;
    }
}
