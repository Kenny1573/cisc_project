package com.dahua.demo.faceService.capture;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 报警记录查询接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为start，limit和content中page，pageSize，count，condition等参数的值
 * 本demo的content中，只是给出了一个例子，可以根据参数选填的情况，自由组合content中参数内容
 */
public class QueryAlarm extends BaseUserInfo {
    /**
     * start:类型int。从第几个结果开始返回，不填默认为1。选填。
     * limit:类型int。返回至多多少结果，不填默认为512。选填。
     */
    public static final String ACTION = "/faceService/query/alarm?start=1&limit=1";

    private static String query(String ip, int port, String token) throws Exception{
        /**
         * page:类型： int 。页数，最小1。选填。
         * pageSize:类型： int 。每页数量，最大1024。选填。
         * count：类型int 。count=1时返回总数，即totalCount；count=0时，返回结果，即results，count为空时，返回结果和总
         * 数。选填。
         * condition:类型object 。查询条件。选填。
         * orgCodes:类型array[string] 。组织id。选填。
         * recordIds:类型array[string] 。抓拍id。选填。
         * channelCodes:类型array[string] 。通道编码。选填。
         * siteSns:类型array[string] 。场所id列表。选填。
         * sceneCodes:类型array[string] 。场景id列表。选填。
         * startAlarmTime:类型string 。报警开始时间，UTC时间TZ格式。选填。
         * endAlarmTime:类型string 。报警结束时间，UTC时间TZ格式。选填。
         * startCapTime:类型string 。抓拍开始时间，UTC时间TZ格式。选填。
         * endCapTime:类型string 。抓拍结束时间，UTC时间TZ格式。选填。
         * startAge:类型int 。识别年龄下限，正整数。选填。
         * endAge:类型int 。识别年龄上限，正整数，上限0，下限125。选填。
         * startSimilarity:类型double 。相似度下限。范围为0-1,小数点后最多6位。选填。
         * endSimilarity:类型double 。相似度上限。范围为0-1,小数点后最多6位。。选填。
         * idNumber:类型string 。可以表示唯一性的证件id号码。选填。
         * name:类型string 。人员名字。选填。
         * birthday:类型string 。出生日期。选填。
         * repositoryIds:类型array[string] 。库id列表。选填。
         * status:类型int 。处理状态:1-待审批，2-确认有效，3-确认无效，4-已处理。选填。
         * gender:类型int 。性别代码，0-未知的性别，1-男性，2-女性，9-未说明的性别。选填。
         * race:类型int 。种族，人种;0-未识别；1-黄种人；2-黑人；3-白人。选填。
         * fringe:类型int 。刘海状态;0-无，1-有。选填。
         * glasses:类型int 。眼镜状态;0-没眼镜，1-眼镜，2-墨镜。选填。
         * eye:类型int 。眼睛状态;0-闭眼，1-睁眼。选填。
         * mouth:类型int 。嘴巴状态;0-闭嘴，1-张嘴。选填。
         * beard:类型int 。胡子状态;0-没胡子，1-有胡子。选填。
         * mask:类型int 。口罩状态;0-没戴口罩，1-戴口罩。选填。
         * emotion:类型int 。表情特征，1-微笑，2-愤怒，3-悲伤，4-厌恶，5-害怕，6-惊讶，7-正常，8-大笑，9-高
         * 兴，10-困惑，11-尖叫。选填。
         * vendor: 类型string[] 。厂商。选填。
         * order:类型array[object] 。排序字段。时间排序默认且优先。选填。
         * capTime:类型string 。按时间排序，desc-倒序,asc-升序。也支持按相似度排序:similarity。
         */
        String content = "{\n" +
                "\"page\": 1,\n" +
                "\"pageSize\": 100,\n" +
                "\"count\": \"\",\n" +
                "\"condition\": {\n" +
                "\"orgCodes\": null,\n" +
                "\"recordIds\": [\n" +
                "\"AL7BED26A1AGG3UKLG9N0220180809073828286310613796\"\n" +
                "],\n" +
                "\"channelCodes\": [\n" +
                "\"174847716B1AGEO716BT05S\"\n" +
                "],\n" +
                "\"siteSns\": [\n" +
                "\"111\",\n" +
                "\"222\"\n" +
                "],\n" +
                "\"sceneCodes\": [\n" +
                "\"333\",\n" +
                "\"444\"\n" +
                "],\n" +
                "\"startAlarmTime\": null,\n" +
                "\"endAlarmTime\": null,\n" +
                "\"startCapTime\": \"20180808T073828Z\",\n" +
                "\"endCapTime\": \"20180810T073828Z\",\n" +
                "\"startAge\": 1,\n" +
                "\"endAge\": 100,\n" +
                "\"startSimilarity\": 0.5,\n" +
                "\"endSimilarity\": 1,\n" +
                "\"idNumber\": \"43434\",\n" +
                "\"name\": \"99999\",\n" +
                "\"birthday\": null,\n" +
                "\"repositoryIds\": [\n" +
                "\"899405827\"\n" +
                "],\n" +
                "\"status\": 1,\n" +
                "\"gender\": 2,\n" +
                "\"race\": null,\n" +
                "\"fringe\": null,\n" +
                "\"glasses\": 1,\n" +
                "\"eye\": 1,\n" +
                "\"mouth\": 1,\n" +
                "\"beard\": 1,\n" +
                "\"mask\": 1,\n" +
                "\"emotion\": 1,\n" +
                "\"vendor\": [\n" +
                "\"001\"\n" +
                "]\n" +
                "},\n" +
                "\"order\": [\n" +
                "{\n" +
                "\"capTime\": \"desc\"\n" +
                "}\n" +
                "]\n" +
                "}";
        String response=HttpTestUtils.httpRequest(HttpEnum.POST,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = query(ip, Integer.valueOf(port), token);
    }
}
