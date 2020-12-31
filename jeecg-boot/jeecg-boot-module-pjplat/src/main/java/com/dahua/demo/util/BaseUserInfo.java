package com.dahua.demo.util;

import java.util.ResourceBundle;

public  class BaseUserInfo {
    protected static String ip;
    protected static String port;
    protected static String userName;
    protected static String password;
    protected static String token;
    static {
        ResourceBundle bundle = ResourceBundle.getBundle("baseinfo");
        ip = bundle.getString("ip");
        port = bundle.getString("port");
        userName = bundle.getString("userName");
        password = bundle.getString("password");
        //第一次获取到token之后保存到token.properties文件中，之后直接读取properties文件中token即可，防止重复申请token
        ResourceBundle tokenbundle = ResourceBundle.getBundle("token");
        if (!("").equals(tokenbundle.getString("token"))) {
            token = tokenbundle.getString("token");
        }else {
            System.out.println("没有进行登录，请调用Login的main方法进行登录");
        }
    }
}
