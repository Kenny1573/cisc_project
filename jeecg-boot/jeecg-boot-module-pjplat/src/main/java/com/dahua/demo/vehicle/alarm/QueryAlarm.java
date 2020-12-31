package com.dahua.demo.vehicle.alarm;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 查询报警接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 *  需要修改的地方为content中startTimeStr，endTimeStr，startTimeStrUt，cplateNum等参数的内容
 *  本demo的content中，只是给出了一个例子，可以根据参数选填的情况，自由组合content中参数内容
 */
public class QueryAlarm extends BaseUserInfo {
    //page为当前页数，pageSize为页大小
    public static final String ACTION = "/vehicleService/rest/alarm?page=1&pageSize=128";

    private static String query(String ip, int port, String token) throws Exception{
        /**
         * startTimeStr : 类型string ，选填。报警开始时间。
         * endTimeStr : 类型string ，选填。报警结束时间。
         * startTimeStrUtc : 类型string ，选填。过车开始时间(UTC时间),若startTimeStrUtc有值，则startTimeStr无效。
         * endTimeStrUtc : 类型string ，选填。过车结束时间(UTC时间),若有endTimeStrUtc有值，则endTimeStr无效。
         * plateNum : 类型string ，选填。车牌号码，支持模糊匹配， ? 匹配一位， * 匹配多位。
         * plateType : 类型string ，选填。号牌种类（参见“号牌类型字典”）。不填表示查询全部种类
         * channelCodes : 类型array[string] ，选填。通道编码，可选多个。相当于报警地点。不填表示查询全部通道
         * surveyCategory : 类型string ，选填。布控类型（请参考“大华SaaS服务协议/业务系统/车辆服务/字典/布控类型字
         * version 1.20
         * 典”）。
         * stat : 类型string ，选填。报警状态。不填表示查询全部状态
         *   报警状态描述
         *    1 未确认
         *    2 已确认
         */
        String content = "{\n" +
                "\"startTimeStr\" : \"2018-03-03 00:00:00\",\n" +
                "\"endTimeStr\" : \"2018-03-03 00:00:00\",\n" +
                "\"startTimeStrUtc\" : \"20180801T103000Z\",\n" +
                "\"endTimeStrUtc\" : \"20180801T104000Z\",\n" +
                "\"plateNum\" : \"浙A12345\",\n" +
                "\"plateType\" : \"01\",\n" +
                "\"channelCodes\" : [\"A900B00F374102\"],\n" +
                "\"surveyCategory\" : \"100801\",\n" +
                "\"stat\" : \"2\"\n" +
                "}";
        String response=HttpTestUtils.httpRequest(HttpEnum.POST,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = query(ip, Integer.valueOf(port), token);
    }
}
