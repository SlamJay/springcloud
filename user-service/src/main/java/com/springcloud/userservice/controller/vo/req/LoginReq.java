package com.springcloud.userservice.controller.vo.req;

import lombok.Data;

@Data
public class LoginReq {

    private String name;
    private String password;
}
