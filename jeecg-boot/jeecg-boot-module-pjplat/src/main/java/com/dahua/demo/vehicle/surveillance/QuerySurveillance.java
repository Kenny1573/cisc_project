package com.dahua.demo.vehicle.surveillance;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 查询布控接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为content中page，pageSize，startTimeStr等参数的内容
 * 本demo的content中，只是给出了一个例子，可以根据参数选填的情况，自由组合content中参数内容
 */
public class QuerySurveillance extends BaseUserInfo {
    public static final String ACTION = "/vehicleService/rest/surveillance";

    private static String query(String ip, int port, String token) throws Exception{
        /**
         * page : 类型int ，必填。当前页码。
         * pageSize : 类型int ，必填。每页记录数。
         * startTimeStr : 类型string ，选填。布控开始时间。
         * endTimeStr : 类型string ，选填。布控结束时间。
         * startTimeStrUtc : 类型string ，选填。过车开始时间(UTC时间),若startTimeStrUtc有值，则startTimeStr无效。
         * endTimeStrUtc : 类型string ，选填。过车结束时间(UTC时间),若有endTimeStrUtc有值，则endTimeStr无效。
         * plateNum : 类型string ，选填。车牌号码，支持模糊匹配， ? 匹配一位， * 匹配多位。
         * plateType : 类型string ，选填。号牌种类（参见“号牌类型字典”）。
         * stat : 类型int ，选填。布控状态（参见“布控状态字典”）。
         * surveyCategory : 类型string ，选填。布控类型。
         * 布控类型描述
         * 100801 假/套牌车
         * 100802 涉案车辆
         * 100803 其他车辆
         */
        String content = "?startTimeStr=2018-10-19%2000:00:00" +
                        "&endTimeStr=2018-10-19%2023:59:59" +
                        "&page=1" +
                        "&pageSize=100";
        String response=HttpTestUtils.httpRequest(HttpEnum.GET,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = query(ip, Integer.valueOf(port), token);
    }
}
