package com.dahua.demo.faceService.repomember;


import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 库成员新增接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为memberId和content中type，identificationId，name，region等参数的值
 * 本demo的content中，只是给出了一个例子，可以根据参数选填的情况，自由组合content中参数内容
 */
public class ModifyMember extends BaseUserInfo {
    //memberId : 类型string ，成员编号。必填。
    public static String memberId="123";

    public static final String ACTION = "/faceService/repository/member/"+memberId;

    private static String modify(String ip, int port, String token) throws Exception{
        /**
         * type： 类型int 。证件类型，默认为111，传除以下值外也为111。必填
         * 目标类型描述
         * 111 身份证
         * 114 军官证
         * 123 警察证
         * 414 普通护照
         * 0 其他
         * identificationId： 类型string 。证件编号。必填
         * name： 类似string 。成员姓名。必填
         * region：类型int 。区域编号。选填
         * nation：类型int 。民族。选填
         * birthday：类型string 。出生日期，格式为YYYY-mm-dd。选填
         * gender：类型int 。成员性别，0-未知，1-男，2-女。选填
         * phone：类型string 。电话。选填
         * address：类型string 。地址。选填
         * memo：类型string 。备注。选填
         * englishName:类型string 。英文名，最长50位。选填
         * validityStart:类型string 。证件生效日期，最长20位。选填
         * validityEnd:类型string 。证件失效日期，最长20位。选填
         */
        String content = "{\n" +
                "\"type\": 111,\n" +
                "\"identificationId\": \"123\",\n" +
                "\"name\": \"张三\",\n" +
                "\"region\": 3,\n" +
                "\"nation\": 1,\n" +
                "\"birthday\": \"1990-01-01\",\n" +
                "\"gender\": 1,\n" +
                "\"phone\": \"1233\",\n" +
                "\"address\": \"滨安路1199号\",\n" +
                "\"memo\": 1,\n" +
                "\"englishName\": \"\",\n" +
                "\"validityStart\": \"\",\n" +
                "\"validityEnd\": \"\"\n" +
                "}";
        String response=HttpTestUtils.httpRequest(HttpEnum.PUT,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = modify(ip, Integer.valueOf(port), token);
    }
}

