package com.springcloud.userservice.controller;

import com.springcloud.userservice.common.util.RedisUtil;
import com.springcloud.userservice.controller.vo.req.LoginReq;
import com.springcloud.userservice.controller.vo.req.RegisterReq;
import com.springcloud.userservice.dao.dataobject.UserDO;
import com.springcloud.userservice.model.User;
import com.springcloud.userservice.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/register")
    public String register(@RequestBody RegisterReq registerReq){
        if (ObjectUtils.notEqual(registerReq.getPassword(),registerReq.getConfirmPassword())){
            return "密码不一致";
        }
        User existUser = userService.findByName(registerReq.getName());
        if (null != existUser){
            return "用户名已存在";
        }
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        String ciphertext = new Md5Hash(registerReq.getPassword(),salt,3).toString();
        String[] strings = new String[]{salt, ciphertext};
        UserDO user = new UserDO();
        user.setName(registerReq.getName());
        user.setSalt(strings[0]);
        user.setPassword(strings[1]);
        userService.save(user);
        return "注册成功";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginReq loginReq){
        int lockCount = 4;
        String redisKey = "login_"+loginReq.getName();
        Subject subject = SecurityUtils.getSubject();
        try{
            UsernamePasswordToken token = new UsernamePasswordToken(loginReq.getName(),loginReq.getPassword());
            subject.login(token);
        }catch (UnknownAccountException uae){
            return "用户名不存在";
        }catch (IncorrectCredentialsException ice){
            Long incr = redisUtil.incr(redisKey);
            long remainCount = lockCount - incr;
            if (incr > lockCount || remainCount == 0){
                redisUtil.expire(redisKey,30);
                return "账户已锁定";
            }
            return "密码不正确,还有"+remainCount+"次机会";
        }catch (AuthenticationException ae){
            return "authenticate error";
        }
        return "登录成功";
    }
}
