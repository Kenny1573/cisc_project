package com.dahua.demo.vehicle.surveillance;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 新增布控接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为content中surveyType，plateNum，plateType等参数的内容
 * 本demo的content中，只是给出了一个例子，可以根据参数选填的情况，自由组合content中参数内容
 */


public class AddSurveillance extends BaseUserInfo {

    public static final String ACTION = "/vehicleService/rest/surveillance";

    private static String addSurveillance(String ip, int port, String token) throws Exception{
        /**
         * surveyType : 类型short ，必填。布控方式，如果不填，审核布控操作会失败
         * plateNum : 类型string ，必填。车牌号码，暂不支持模糊匹配（勾选无牌车时，plateNum传值八个0 "00000000"）。
         * plateType : 类型string ，必填。号牌种类
         * startTimeStr : 类型string ，必填。布控开始时间。
         * endTimeStr : 类型string ，必填。布控结束时间。
         * startTimeStrUtc : 类型string ，选填。过车开始时间(UTC时间),若startTimeStrUtc有值，则startTimeStr无效。
         * endTimeStrUtc : 类型string ，选填。过车结束时间(UTC时间),若有endTimeStrUtc有值，则endTimeStr无效。
         * reason : 类型string ，选填。布控原因,最大128字节。
         * tels : 类型array[string] ，选填。报警号码，数组。
         * surveyCategory : 类型string ，选填。布控类型
         * surveyTaskName : 类型string ，选填。布控名称。
         * libRecordIds : 类型array[string] ，选填。库管理的唯一编码。
         * version 1.20
         * channelCodes : 类型array[string] ，选填。通道数组。
         * paperSign : 类型string ，选填。摆件符号：greater表示大于，less表示小于，equal表示等于。
         * paperCnt : 类型int ，选填。摆件个数，与paperSign同有同无。
         * dropSign : 类型string ，选填。挂件符号：greater表示大于，less表示小于，equal表示等于。
         * dropCnt : 类型int ，选填。挂件个数，与dropSign同有同无。
         * tagSign : 类型string ，选填。年检标符号：greater表示大于，less表示小于，equal表示等于。
         * tagCnt : 类型int ，选填。年检标个数，与tagSign同有同无。
         * sunSign : 类型string ，选填。遮阳板符号：greater表示大于，less表示小于，equal表示等于。
         * sunCnt : 类型int ，选填。遮阳板个数，与sunSign同有同无。
         */
        String content = "{\n" +
                "\"surveyType\" : 1,\n" +
                "\"plateNum\" : \"浙A12345\",\n" +
                "\"plateType\" : \"02\",\n" +
                "\"startTimeStr\" : \"2018-03-03 00:00:00\",\n" +
                "\"endTimeStr\" : \"2018-03-04 00:00:00\",\n" +
                "\"startTimeStrUtc\" : \"20180801T103000Z\",\n" +
                "\"endTimeStrUtc\" : \"20180801T104000Z\",\n" +
                "\"reason\" : \"测试\",\n" +
                "\"tels\" : [\"18989485738\"],\n" +
                "\"surveyCategory\" : \"100801\",\n" +
                "\"surveyTaskName\" : \"布控任务名称\",\n" +
                "\"libRecordIds\" :\n" +
                "[\"c9b87a17e97c4c35b6f2710b37fa2b66\",\"c9b87a17e97c4c35b6f2710b37fa2c21\"],\n" +
                "\"channelCodes\" : [\"AEVaa2jHB1ALE90F3DMRNC\",\"DEAaa2jHB1ALE90F3KILOS\"]\n" +
                "}";
        String response=HttpTestUtils.httpRequest(HttpEnum.POST,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = addSurveillance(ip, Integer.valueOf(port), token);
    }
}
