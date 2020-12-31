package com.dahua.demo.faceService.repomember;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 人像库查询和检索接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 *         参数包括  ACTION部分    和   content部分
 *
 *  该接口特殊说明：
 *   1. 当请求参数和请求内容中都存在分页参数时，以请求内容中为准。
 *   2. 如果存在请求参数和请求内容中都存在部分分页参数时，系统只会选取请求内容中的分页参数。
 *   3. 本接口支持以图搜图和条件查询两种功能，每次只能执行其中一种查询。
 *   4. 当retrieval对象中的两个字段都有值时，优先进行以图搜图。
 *   5. 以图搜图时，condition中只有repositoryIds字段有效，其他年龄、性别等查询条件不生效。
 *
 *   需要修改的地方为content中queryId，page，pagetSize，count等参数的值
 *   本demo的content中，只是给出了一个例子，可以根据参数选填的情况，自由组合content中参数内容
 *
 */
public class QueryRepo extends BaseUserInfo {

    public static final String ACTION = "/faceService/query/repository";

    private static String query(String ip, int port, String token) throws Exception{
        /**
         * queryId: 类型string 。查询id，第一次检索完成后的结果会返回此id，带上此id进行翻页等操作会更加高效。选填。
         * page: 类型int 。从第几个结果开始返回。选填。
         * pagetSize: 类型int 。返回至多多少条记录。选填。
         * count：类型int 。count=1时返回总数，即totalCount；count=0时，返回结果，即results。选填，retrieval为空时必
         * 填，retrieval不为空时，count必须为空。
         * condition: 类型object 。查询条件。选填。
         * identificationId: 类型string 。身份证id。选填。
         * name: 类型string 。姓名，最大值为150位。选填。
         * repositoryIds: 类型array[string] 。库id列表，选填。
         * retrieval: 类型object 。检索条件，即以图搜图的条件，为空表示不进行以图搜图，选填。
         * faceImage: 类型string 。人脸图片数据，支持BASE64和URL格式，选填。retrieval不为空时必填。
         * threshold: 类型double 。相似度，取值范围[0-1]。选填。retrieval不为空时必填。
         * order: 类型array[object] 。排序，选填。
         * similarity: 类型string 。按相似度排序，desc表示倒序；asc表示升序，选填。
         * 响应
         * 响应内容
         * version
         */
        String content = "{\n" +
                "\"queryId\": \"\",\n" +
                "\"page\": 1,\n" +
                "\"pageSize\": 20,\n" +
                "\"count\": \"\",\n" +
                "\"condition\": {\n" +
                "\"identificationId\": \"232323\",\n" +
                "\"name\": \"123\",\n" +
                "\"repositoryIds\": [\n" +
                "\"718493701\"\n" +
                "]\n" +
                "},\n" +
                "\"retrieval\": {\n" +
                "\"faceImage\": \"a3b30rghoeqtpg==\",\n" +
                "\"threshold\": 0.8\n" +
                "},\n" +
                "\"order\": [\n" +
                "{\n" +
                "\"similarity\": \"desc\"\n" +
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
