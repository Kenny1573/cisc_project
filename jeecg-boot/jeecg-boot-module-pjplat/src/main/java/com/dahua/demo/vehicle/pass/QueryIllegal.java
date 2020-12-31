package com.dahua.demo.vehicle.pass;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 违章查询接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 *  需要修改的地方为 content中  plateNum，carNumType，startTimeStr等参数的内容
 *  本demo的content中，只是给出了一个例子，可以根据参数选填的情况，自由组合content中参数内容
 */
public class QueryIllegal extends BaseUserInfo {
    //page为当前页码，pagesize为每页记录数，可以根据实际情况修改
    public static final String ACTION = "/vehicleService/rest/illegal/queryList?page=1&pageSize=100";

    private static String query(String ip, int port, String token) throws Exception{
        /**
         * plateNum : 类型string ，选填。车牌号码， ? 匹配一位， * 匹配多位。
         * carNumType : 类型string ，选填。号牌类型(详见“号牌类型字典”一节)。
         * startTimeStr : 类型string ，选填。过车开始时间，格式"YYYY-MM-DD HH:mm:ss"。
         * endTimeStr : 类型string ，选填。过车结束时间，格式"YYYY-MM-DD HH:mm:ss"。
         * startTimeStrUtc : 类型string，选填。过车开始时间(UTC时间),若startTimeStrUtc有值，则startTimeStr无效。
         * endTimeStrUtc : 类型string，选填。过车结束时间(UTC时间),若有endTimeStrUtc有值，则endTimeStr无效。
         * channelCodes : 类型array[string] ，选填。卡口(可选多个)。
         * carWayCode : 类型int ，选填。车道号（详见“车道号字典”一节）。
         * recTypes : 类型array[int] ，选填。违章类型，可以多个,号区分（详见“违章类型字典”一节）。
         * speedRange : 类型string ，选填。车速范围。
         * carBrand : 类型string ，选填。车辆品牌。
         * carDirect : 类型string ，选填。行驶方向（详见“行驶方向字典”一节）。
         */
        String content = "{\n" +
                "\"plateNum\" : \"浙A169AG\",\n" +
                "\"carNumType\" : \"01\",\n" +
                "\"startTimeStr\" : \"2018-08-01 10:30:00\",\n" +
                "\"endTimeStr\" : \"2018-08-01 11:00:00\",\n" +
                "\"startTimeStrUtc\" : \"20180801T010000Z\",\n" +
                "\"endTimeStrUtc\" : \"20180802T235959Z\",\n" +
                "\"channelCodes\" : [\"90000374$1$0$2\",\"0000474$1$0$2\"],\n" +
                "\"carWayCode\" : 1,\n" +
                "\"recTypes\" : [1, 34],\n" +
                "\"speedRange\" : \"[100 TO 200]\",\n" +
                "\"carBrand\" : \"1\",\n" +
                "\"carDirect\" : \"1\"\n" +
                "}";
        String response=HttpTestUtils.httpRequest(HttpEnum.POST,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = query(ip, Integer.valueOf(port), token);
    }
}
