package com.ltc.sharding.controller;


import com.ltc.sharding.entity.AkcBuoyDeploy;
import com.ltc.sharding.mapper.AkcBuoyDeployMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * cms_配置_浮标 前端控制器
 * </p>
 *
 * @author ltc
 * @since 2019-03-14
 */
@RestController
@RequestMapping("/sharding/akc-buoy-deploy")
public class AkcBuoyDeployController {

    @Autowired
    private AkcBuoyDeployMapper akcBuoyDeployMapper;

    @GetMapping("test")
    public List<AkcBuoyDeploy> demo() {
        List<AkcBuoyDeploy> akcBuoyDeploys = akcBuoyDeployMapper.selectList(null);
        return akcBuoyDeploys;
    }
}
