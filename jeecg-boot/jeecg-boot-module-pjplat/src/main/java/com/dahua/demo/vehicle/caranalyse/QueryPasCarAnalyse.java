package com.dahua.demo.vehicle.caranalyse;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 查询二次分析接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为content中plateNum，plateType，startTimeStr，endTimeStr等参数
 * 本demo的content中，只是给出了一个例子，可以根据参数选填的情况，自由组合content中参数内容
 */
public class QueryPasCarAnalyse extends BaseUserInfo {
    //page页数，pageSize页大小，根据实际情况修改
    public static final String ACTION = "/vehicleService/rest/pasCarAnalyse/queryList?page=1&pageSize=100";

    private static String analyse(String ip, int port, String token) throws Exception{
        /**
         * plateNum : 类型string 。选填。车牌号码。
         * plateType : 类型string 。选填。号牌种类（请参考“大华SaaS服务协议/业务系统/车辆服务/字典/号牌类型字典”）。
         * startTimeStr : 类型string 。选填。开始时间。
         * endTimeStr : 类型string 。选填。结束时间。
         * startTimeStrUtc : 类型string ，选填。过车开始时间(UTC时间),若startTimeStrUtc有值，则startTimeStr无效。
         * endTimeStrUtc : 类型string ，选填。过车结束时间(UTC时间),若有endTimeStrUtc有值，则endTimeStr无效。
         * channelCodes : 类型array[string] 。选填。卡口选择。
         * version 1.20
         * carReliabilityA : 类型int 。选填。置信度0-100。
         * carTypeA : 类型string 。选填。车辆类型。
         * carStyleName : 类型string 。选填。车辆品牌型号年款code，用横杠拼接，根据 车辆品牌型号年款字典_查询接口
         * 接口获取code值，例如："1001-1-11"。
         * paperSign : 类型string 。选填。摆件符号：greater表示大于，less表示小于，equal表示等于。
         * paperCnt : 类型int 。选填。摆件个数，与paperSign同有同无。
         * dropSign : 类型string 。选填。挂件符号：greater表示大于，less表示小于，equal表示等于。
         * dropCnt : 类型int 。选填。挂件个数，与dropSign同有同无。
         * tagSign : 类型string 。选填。年检标符号：greater表示大于，less表示小于，equal表示等于。
         * tagCnt : 类型int 。选填。年检标个数，与tagSign同有同无。
         * tagShape : 类型int 。选填。年检标位置。
         * sunCnt : 类型int 。选填。遮阳板个数，与sunSign同有同无。
         * sunSign : 类型string 。选填。遮阳板符号：greater表示大于，less表示小于，equal表示等于。
         * sunRoofCnt : 类型int 。选填。天窗标志:0-否,1-是。
         * rackCnt : 类型int 。选填。行李架标志:0-否,1-是。
         * spareTireCnt : 类型int 。选填。备胎标志:0-否,1-是。
         * mobileCnt : 类型int 。选填。打手机标志:0-否,1-是。
         * noCarNum : 类型int 。选填。无牌车标志:0-否,1-是。
         * noLabelt : 类型int 。选填。不系安全带标志:0-否,1-是。
         * isStainedPlate : 类型int 。选填。号牌污损:0-没有污损（遮挡）,1-部分污损（遮挡）,2-完全污损（遮挡）,3-不确定。
         * muskHide : 类型int 。选填。渣土车遮盖:0-遮盖,1-无遮盖空载,2-无遮盖满载,3-不确定。
         * isSkylightPerson : 类型int 。选填。天窗站人:0-否,1-是。
         * crashCnt : 类型int 。选填。撞损:0-否,1-是。
         * babyCnt : 类型int 。选填。抱小孩:0-否,1-是。
         * cardCnt : 类型int 。选填。卡片：0-无,1-有。
         * trunkStatus : 类型int 。选填。后备箱打开:0-关闭,1-打开。
         * carPrint : 类型int 。选填。喷绘:0-无,1-有。
         * isYellowLabel : 类型int 。选填。黄标车:0-否,1-是。
         */
        String content = "{\n" +
                "\"plateNum\" : \"浙A169AG\",\n" +
                "\"plateType\" : \"01\",\n" +
                "\"startTimeStr\" : \"2017-03-03 00:00:00\",\n" +
                "\"endTimeStr\" : \"2019-09-07 00:00:59\",\n" +
                "\"startTimeStrUtc\" : \"20180801T103000Z\",\n" +
                "\"endTimeStrUtc\" : \"20180801T104000Z\",\n" +
                "\"channelCodes\" : [\"AEVaa2jHB1AO1BO6DILUG5\",\"AEVaa2jHB1AO1BO6DEGD23\"],\n" +
                "\"carReliabilityA\" : 60,\n" +
                "\"carTypeA\" : \"1\",\n" +
                "\"carStyleName\" : \"1001-1-11\",\n" +
                "\"paperSign\" : \"greater\",\n" +
                "\"paperCnt\" : 2,\n" +
                "\"dropSign\" : \"greater\",\n" +
                "\"dropCnt\" : 2,\n" +
                "\"tagSign\" : \"greater\",\n" +
                "\"tagCnt\" : 2,\n" +
                "\"tagShape\" : 1,\n" +
                "\"sunCnt\" : 1,\n" +
                "\"sunSign\" : \"greater\",\n" +
                "\"sunRoofCnt\" : 1,\n" +
                "\"rackCnt\" : 1,\n" +
                "\"spareTireCnt\" : 1,\n" +
                "\"mobileCnt\" : 1,\n" +
                "\"noCarNum\" : 1,\n" +
                "\"noLabelt\" : 1,\n" +
                "\"isStainedPlate\" : 1,\n" +
                "\"muskHide\" : 1,\n" +
                "\"isSkylightPerson\" : 1,\n" +
                "\"crashCnt\" : 1,\n" +
                "\"babyCnt\" : 1,\n" +
                "\"cardCnt\" : 1,\n" +
                "\"trunkStatus\" : 1,\n" +
                "\"carPrint\" : 1,\n" +
                "\"isYellowLabel\" : 1\n" +
                "}";
        String response=HttpTestUtils.httpRequest(HttpEnum.POST,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = analyse(ip, Integer.valueOf(port), token);
    }
}
