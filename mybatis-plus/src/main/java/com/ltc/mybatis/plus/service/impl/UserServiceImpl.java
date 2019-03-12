package com.ltc.mybatis.plus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltc.mybatis.plus.entity.User;
import com.ltc.mybatis.plus.mapper.UserMapper;
import com.ltc.mybatis.plus.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ltc
 * @since 2019-03-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
