package com.springcloud.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springcloud.userservice.dao.dataobject.UserDO;
import com.springcloud.userservice.dao.mapper.UserMapper;
import com.springcloud.userservice.model.User;
import com.springcloud.userservice.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    @Override
    public User findByName(String name) {
        User user = new User();
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.eq(UserDO.NAME,name);
        UserDO one = this.getOne(wrapper);
        BeanUtils.copyProperties(one,user);
        return user;
    }
}
