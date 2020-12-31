package com.dahua.demo.faceService.repomember;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 库成员删除接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为memberId的值
 */
public class DeleteMember extends BaseUserInfo {
    //memberId : 类型string ，成员编号。必填。
    public static String memberId="123";

    public static final String ACTION = "/faceService/repository/member/"+memberId;

    private static String delete(String ip, int port, String token) throws Exception{
        String content = "";
        String response=HttpTestUtils.httpRequest(HttpEnum.DELETE,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = delete(ip, Integer.valueOf(port), token);
    }
}

