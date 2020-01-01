package com.study.haofan.functionintf.impl;

import com.study.haofan.functionintf.IUserCredential;

public class UserCredentialImpl implements IUserCredential {
    @Override
    public String verifyUser(String username) {
        if ("admin".equals(username)) {
            return "系统管理员";
        } else if("manager".equals(username)) {
            return "用户管理员";
        }
        return "普通会员";
    }
}
