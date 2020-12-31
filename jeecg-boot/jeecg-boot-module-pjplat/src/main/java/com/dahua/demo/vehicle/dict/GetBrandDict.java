package com.dahua.demo.vehicle.dict;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 品牌字典获取
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为type的值
 */
public class GetBrandDict extends BaseUserInfo {
    //type : 类型int ，必填。字典类型。
    public static String type = "4";

    public static final String ACTION = "/vehicleService/rest/dict/getDictList/"+type;

    private static String brand(String ip, int port, String token) throws Exception{
        String content = "";
        String response=HttpTestUtils.httpRequest(HttpEnum.GET,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = brand(ip, Integer.valueOf(port), token);
    }
}
