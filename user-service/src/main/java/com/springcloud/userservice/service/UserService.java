package com.springcloud.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springcloud.userservice.dao.dataobject.UserDO;
import com.springcloud.userservice.model.User;

public interface UserService extends IService<UserDO> {

    User findByName(String name);
}
