package com.dahua.demo.vehicle.dict;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 车系年款字典获取接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为type的值，和content中brand，style参数的内容
 */
public class GetCarDetail extends BaseUserInfo {
    //type : 类型int ，必填。字典类型，取固定值5。
    public static String type = "5";

    public static final String ACTION = "/vehicleService/rest/dict/getClxhList/"+type;

    private static String detail(String ip, int port, String token) throws Exception{
        /**
         * brand : 类型string ，选填。品牌。
         * style : 类型string ，选填。车系。
         */
        String content = "?brand=大众" +
                         "&style=捷达";
        String response=HttpTestUtils.httpRequest(HttpEnum.GET,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = detail(ip, Integer.valueOf(port), token);
    }
}
