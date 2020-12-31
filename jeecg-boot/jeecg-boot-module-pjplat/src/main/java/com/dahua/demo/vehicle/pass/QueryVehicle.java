package com.dahua.demo.vehicle.pass;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 过车查询接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为content中plateNum，carNumType，endTimeStr等参数的值
 * 本demo的content中，只是给出了一个例子，可以根据参数选填的情况，自由组合content中参数内容
 */
public class QueryVehicle extends BaseUserInfo {
    //page为当前页码，pagesize为每页记录数，可以根据实际情况来修改
    public static final String ACTION = "/vehicleService/rest/vehicle/queryList?page=1&pageSize=100";

    private static String queryVehicle(String ip, int port, String token) throws Exception{
        /**
         * plateNum : 类型string ，选填。车牌号码，支持模糊匹配， ? 匹配一位， * 匹配多位。
         * multiPlateNums : 类型array[string] ，车牌号码， ? 匹配一位， * 匹配多位。
         * carNumType : 类型string ，选填。号牌类型
         * plateTypes : 类型array[string] ，选填。号牌类型
         * startTimeStr : 类型string ，选填。过车开始时间，格式"YYYY-MM-DD HH:mm:ss"。
         * endTimeStr : 类型string ，选填。过车结束时间，格式"YYYY-MM-DD HH:mm:ss"。
         * startTimeStrUtc : 类型string ，选填。过车开始时间(UTC时间),若startTimeStrUtc有值，则startTimeStr无效。
         * endTimeStrUtc : 类型string ，选填。过车结束时间(UTC时间),若有endTimeStrUtc有值，则endTimeStr无效。
         * channelCodes : 类型array[string] ，选填。卡口位置(即通道编码，可选多个)。
         * carWayCode : 类型int ，选填。车道号
         * carColor : 类型int ，选填。车身颜色
         * carColors : 类型array[int] ，车身颜色（请参考“大华SaaS服务协议/业务系统/车辆服务/字典/车身颜色字典”）。
         * carBrand : 类型string ，选填。车辆品牌。
         * carDirect : 类型int ，选填。行驶方向
         * speedRange :类型string ，选填。车速范围。
         */
        String content="{\n" +
                "\"plateNum\" : \"浙A169AG\",\n" +
                "\"multiPlateNums\" : [\"浙A169AG\"],\n" +
                "\"carNumType\" : \"01\",\n" +
                "\"plateTypes\" : [\"01\"],\n" +
                "\"startTimeStr\" : \"2018-08-01 10:30:00\",\n" +
                "\"endTimeStr\" : \"2018-08-01 10:40:00\",\n" +
                "\"startTimeStrUtc\" : \"20180801T103000Z\",\n" +
                "\"endTimeStrUtc\" : \"20180801T104000Z\",\n" +
                "\"channelCodes\" : [\"AEVaa2jHA1AO6MDVTDO2EE\",\"AEVaa2jHA1AOGA7JQAIUO6\"],\n" +
                "\"carWayCode\" : 1,\n" +
                "\"carColor\" : 1,\n" +
                "\"carColors\" : [1],\n" +
                "\"carBrand\" : \"1\",\n" +
                "\"carDirect\" : 1,\n" +
                "\"speedRange\" : \"[50 TO 80]\"\n" +
                "}";
        String response=HttpTestUtils.httpRequest(HttpEnum.POST,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = queryVehicle(ip, Integer.valueOf(port), token);
    }
}
