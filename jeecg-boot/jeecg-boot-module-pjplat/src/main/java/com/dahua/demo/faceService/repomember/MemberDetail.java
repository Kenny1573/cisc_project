package com.dahua.demo.faceService.repomember;


import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 库成员详情接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为memberId和content中resultPictureType参数的值
 */
public class MemberDetail extends BaseUserInfo {
    //repositoryId:类型string 。库id。必填。
    public static String memberId="M5QnoMZttb1tPBFp0nYx4ifdySmnUVMx";

    public static final String ACTION = "/faceService/repository/member/"+memberId;

    private static String detail(String ip, int port, String token) throws Exception{
        //resultPictureType: 类型int ，默认为0。0：表示只返回图片url；1：表示只返回base64；2：返回url和base64。选填。
        String content = "?resultPictureType=2";
        String response=HttpTestUtils.httpRequest(HttpEnum.GET,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = detail(ip, Integer.valueOf(port), token);
    }
}

