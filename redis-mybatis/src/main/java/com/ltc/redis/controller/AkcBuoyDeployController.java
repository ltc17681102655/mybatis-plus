package com.ltc.redis.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.ltc.redis.dto.AkcBuoyDeployAddDTO;
import com.ltc.redis.dto.AkcBuoyDeployDeleteDTO;
import com.ltc.redis.dto.AkcBuoyDeploySelectDTO;
import com.ltc.redis.dto.AkcBuoyDeployUpdateDTO;
import com.ltc.redis.entity.AkcBuoyDeploy;
import com.ltc.redis.enums.RedisKeyEnum;
import com.ltc.redis.mapper.AkcBuoyDeployMapper;
import com.ltc.redis.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
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
@Api(tags = "AkcBuoyDeployController")
public class AkcBuoyDeployController {

    @Autowired
    private AkcBuoyDeployMapper akcBuoyDeployMapper;

    @Autowired
    private RedisUtils redisUtils;

    @PostConstruct
    public void init() {
        //默认无限期
        this.selectAll2redisCache();
    }

    public void selectAll2redisCache() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("deleted", 0);
        redisUtils.set(RedisKeyEnum.BUOY.getKey(), JSONObject.toJSONString(akcBuoyDeployMapper.selectByMap(map)));
    }

    /**
     * http://localhost:8081/redis/akc-buoy-deploy/selectList
     *
     * @return
     */
    @GetMapping("/selectList")
    @ApiOperation(value = "查询浮标列表")
    public List<AkcBuoyDeploy> selectList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("deleted", 0);
        return akcBuoyDeployMapper.selectByMap(map);
    }

    @GetMapping("/selectPage")
    @ApiOperation(value = "浮标分页查询")
    public IPage<AkcBuoyDeploy> selectPage(AkcBuoyDeploySelectDTO akcBuoyDeploySelectDTO) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("deleted", 0);
        Page<AkcBuoyDeploy> page = new Page<AkcBuoyDeploy>(akcBuoyDeploySelectDTO.getPageIndex(), akcBuoyDeploySelectDTO.getPageSize());
        //根据有传值的属性进行筛选
        AkcBuoyDeploy akcBuoyDeploy = new AkcBuoyDeploy();
        BeanUtils.copyProperties(akcBuoyDeploySelectDTO, akcBuoyDeploy);
        QueryWrapper<AkcBuoyDeploy> diseaseQueryWrapperw = new QueryWrapper<AkcBuoyDeploy>(akcBuoyDeploy);
        IPage<AkcBuoyDeploy> akcBuoyDeployIPage = akcBuoyDeployMapper.selectPage(page, diseaseQueryWrapperw);
        return akcBuoyDeployIPage;
    }

    /**
     * @param key
     * @return http://localhost:8081/redis/akc-buoy-deploy/getkey?key=buoy
     */
    @GetMapping("/getkey")
    @ApiOperation(value = "redis通过key获取数据")
    public List<AkcBuoyDeploy> getkey(String key) {
        String json = redisUtils.get(key);
        System.out.println(json);
        //
        List<AkcBuoyDeploy> akcBuoyDeploys = JSONObject.parseArray(json, AkcBuoyDeploy.class);
        return akcBuoyDeploys;
    }

    /**
     * @param akcBuoyDeployAddDTO
     */
    @PostMapping("insert")
    @ApiOperation(value = "添加浮标")
    public void insert(@Valid @RequestBody AkcBuoyDeployAddDTO akcBuoyDeployAddDTO) {
        if (akcBuoyDeployAddDTO != null) {
            AkcBuoyDeploy akcBuoyDeploy = new AkcBuoyDeploy();
            BeanUtils.copyProperties(akcBuoyDeployAddDTO, akcBuoyDeploy);
            int insert = akcBuoyDeployMapper.insert(akcBuoyDeploy);
            this.selectAll2redisCache();
        }
    }


    /**
     * @param akcBuoyDeployDeleteDTO
     */
    @PostMapping("delete")
    @ApiOperation(value = "删除浮标")
    public void delete(@Valid @RequestBody AkcBuoyDeployDeleteDTO akcBuoyDeployDeleteDTO) {
        int batchIds = akcBuoyDeployMapper.deleteBatchIds(akcBuoyDeployDeleteDTO.getIds());
        this.selectAll2redisCache();
    }

    @PostMapping("update")
    @ApiOperation(value = "修改浮标")
    public void update(@Valid @RequestBody AkcBuoyDeployUpdateDTO akcBuoyDeployUpdateDTO) {
        AkcBuoyDeploy akcBuoyDeploy = new AkcBuoyDeploy();
        BeanUtils.copyProperties(akcBuoyDeployUpdateDTO, akcBuoyDeploy);
        int update = akcBuoyDeployMapper.updateById(akcBuoyDeploy);
        this.selectAll2redisCache();
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
