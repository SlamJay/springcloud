package com.springcloud.userservice.controller.vo.req;

import lombok.Data;

@Data
public class RegisterReq {

    private String name;
    private String password;
    private String confirmPassword;
    private String mobile;
    private String email;
}
