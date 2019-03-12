package com.ltc.mybatis.plus.service.impl;

import com.ltc.mybatis.plus.entity.TestEnum;
import com.ltc.mybatis.plus.mapper.TestEnumMapper;
import com.ltc.mybatis.plus.service.ITestEnumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ltc
 * @since 2019-03-12
 */
@Service
public class TestEnumServiceImpl extends ServiceImpl<TestEnumMapper, TestEnum> implements ITestEnumService {

}
