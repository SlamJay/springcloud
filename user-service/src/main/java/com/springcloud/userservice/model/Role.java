package com.springcloud.userservice.model;

import lombok.Data;

@Data
public class Role {

    private Long id;
    private String roleName;
    private String desc;
}
