package com.study.haofan.functionintf;

import com.study.haofan.functionintf.impl.UserCredentialImpl;

public class FunctionIntfApp {

    public static void main(String args[]) {
        IUserCredential ic = new UserCredentialImpl();
        System.out.println(ic.verifyUser("admin"));

        // 匿名内部类实现
        IUserCredential ic2 = new IUserCredential() {
            @Override
            public String verifyUser(String username) {
                return "admin".equals(username) ? "管理员" : "会员";
            }
        };
        System.out.println(ic2.verifyUser("admin"));

        // lambda表达式，针对函数式接口的简单实现
        IUserCredential ic3 = (String username) -> {
            return "admin".equals(username) ? "管理员" : "会员";
        };
        System.out.println(ic3.verifyUser("admin"));
    }
}
