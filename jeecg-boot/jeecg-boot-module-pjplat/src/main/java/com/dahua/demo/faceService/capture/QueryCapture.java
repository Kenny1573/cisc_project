package com.dahua.demo.faceService.capture;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 抓拍库查询和检索接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 *         参数包括  ACTION部分    和   content部分
 *    1. 本接口支持以图搜图和条件查询两种功能，每次只能执行其中一种查询。
 *    2. 当retrieval对象中的两个字段都有值时，优先进行以图搜图。
 *    3. 以图搜图时，condition中只有channelCodes、startCapTIme、endCapTime字段有效，其他查询条件不生效。
 *    4. 目前识别年龄上限为128+，超出则会抛出数据异常。
 *    5. (start、limit)和(page、pageSize)二选一，当四个参数都存在时，以(page、pageSize)为准。
 *    6. 带图与不带图，返回结果中最大的区别在是否有相似度（similarity）字段。
 *
 *    需要修改的地方为start，limit和content中page，pageSize，count，condition等参数的值
 *    本demo的content中，只是给出了一个例子，可以根据参数选填的情况，自由组合content中参数内容
 */
public class QueryCapture extends BaseUserInfo {
    /**
     * start:类型int。从第几个结果开始返回，不填默认为1。选填。
     * limit:类型int。返回至多多少结果，不填默认为512。选填。
     */
    public static final String ACTION = "/faceService/query/capture?start=1&limit=1";

    private static String query(String ip, int port, String token) throws Exception{
        /**
         * page:类型： int 。页数，默认1。选填。
         * pageSize:类型： int 。每页数量，默认20。选填。
         * count：类型int 。count=1时返回总数，即totalCount；count=0时，返回结果，即results。选填。retrieval不为空
         * 时，count必须为空；retrieval为空时，若count不填，返回结果和总数。
         * condition:类型object 。查询条件。condition或retrieval至少有一个。
         * orgCodes:类型array[string] 。组织id列表。选填。
         * channelCodes:类型array[string] 。通道id列表。选填。
         * siteSns:类型array[string] 。场所id列表。选填。
         * sceneCodes:类型array[string] 。场景id列表。选填。
         * startCapTime:类型string 。抓拍开始时间，UTC时间TZ格式。选填。
         * endCapTime:类型string 。抓拍结束时间，UTC时间TZ格式。选填。
         * startAge:类型int 。识别年龄下限，正整数。选填。
         * endAge:类型int 。识别年龄上限，正整数。选填。
         * gender:类型int 。性别代码，0-未知性别；1-男性；2-女性；9-未说明的性别。选填。
         * race:类型int 。种族，人种。0-未识别；1-黄种人；2-黑人；3-白人。选填。
         * fringe:类型int 。刘海状态。0-无；1-有。选填。
         * eye:类型int 。眼睛状态，0-闭眼；1-睁眼。选填。
         * mouth:类型int 。嘴巴状态，0-闭嘴；1-张嘴。选填。
         * beard:类型int 。胡子状态，0-没胡子；1-有胡子。选填。
         * mask:类型int 。口罩状态，0-没戴口罩；1-戴口罩。选填。
         * glasses:类型int 。眼镜状态，0-没眼镜；1-眼镜；2-墨镜。选填。
         * emotion:类型int 。表情特征，1-微笑；2-愤怒；3-悲伤；4-厌恶；5-害怕；6-惊讶；7-正常；8-大笑；9-高
         * 兴；10-困惑；11-尖叫。v
         * extractedFlag: 类型string 。特征提取标识，0 未提取, 1 提取成功, 2 提取失败, 99-算子分析异常。选填。
         * retrieval:类型object 。检索条件，即以图搜图的条件，为空表示不进行以图搜图。condition或retrieval至少有一个。
         * faceImage:类型string 。人脸图片数据，以base64编码。选填。
         * threshold:类型double 。比对阀值，取值范围[0-1]。选填。
         * order:类型array[object] 。排序。选填。
         * capTime:类型string 。按时间排序，desc-倒序；asc-升序。选填。
         */
        String content = "{\n" +
                "\"page\": 1,\n" +
                "\"pageSize\": 100,\n" +
                "\"count\": 0,\n" +
                "\"condition\": {\n" +
                "\"orgCodes\": null,\n" +
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
                "\"startCapTime\": \"20180806T070113Z\",\n" +
                "\"endCapTime\": \"20180819T070113Z\",\n" +
                "\"startAge\": 1,\n" +
                "\"endAge\": 120,\n" +
                "\"gender\": 1,\n" +
                "\"race\": null,\n" +
                "\"fringe\": null,\n" +
                "\"eye\": 1,\n" +
                "\"mouth\": 1,\n" +
                "\"beard\": 0,\n" +
                "\"mask\": 0,\n" +
                "\"glasses\": 1,\n" +
                "\"emotion\": 7,\n" +
                "\"extractedFlag\": 1\n" +
                "},\n" +
                "\"retrieval\": {\n" +
                "\"faceImage\": \"data:image/jpeg;base64,/9j/4AAQS\",\n" +
                "\"threshold\": 0.30\n" +
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
