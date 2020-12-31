package com.dahua.demo.login;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 会话保活接口
 */
public class KeepLogin extends BaseUserInfo
{
    public static final String ACTION = "/videoService/accounts/token/keepalive";



    public static String keepAlive(String ip, int port, String token) throws Exception{
        String content = "{\"token\":\""+token+"\"}";
        String response=HttpTestUtils.httpRequest(HttpEnum.PUT,ip,port,ACTION,token,content);
        return response;
    }
    //会话保活方法，保活时间为110s，该方法开启后不能关闭。
    public static void main(String[] args) throws Exception {
        while (true) {
            String rsp = keepAlive(ip, Integer.valueOf(port), token);
            System.out.println(rsp);
            Thread.sleep(110000);
        }
    }
    
    public static void keepVideoAlive() throws Exception {
        while (true) {
            String rsp = keepAlive(ip, Integer.valueOf(port), token);
            System.out.println(rsp);
            Thread.sleep(110000);
        }
    }
    
    
}



