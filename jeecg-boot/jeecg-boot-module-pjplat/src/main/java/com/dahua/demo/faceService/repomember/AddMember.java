package com.dahua.demo.faceService.repomember;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 库成员新增接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方content中repositoryId，type，identificationId等参数的值
 * 本demo的content中，只是给出了一个例子，可以根据参数选填的情况，自由组合content中参数内容
 */
public class AddMember extends BaseUserInfo {

    public static final String ACTION = "/faceService/repository/member";

    private static String add(String ip, int port, String token) throws Exception{
        /**
         * repositoryId: 类型string 。库ID。必填
         * type: 类型int 。证件类型。必填
         * 目标类型描述
         *      111 身份证
         *      114 军官证
         *      123 警察证
         *      414 普通护照
         *      0 其他
         * identificationId: 类型string 。证件号码。必填
         * name: 类型string 。成员姓名。必填
         * region:类型int 。区域编号。选填
         * nation:类型int 。民族。选填
         * birthday:类型string 。出生日期，格式为YYYY-mm-dd。选填
         * gender:类型int 。成员性别，0-未知，1-男，2-女。选填
         * phone:类型string 。电话。选填
         * address:类型string 。地址。选填
         * memo:类型string 。备注。选填
         * facePicture:类型object 。人脸图片。必填
         * type:类型int 。图片类型，0-base64编码的图片数据，1-url。必填
         * data:类型string 。人脸图片数据base64编码，type为0时必填
         * url:类型string 。人脸图片url，type为1时必填
         */
        String content = "{\n" +
                "\"repositoryId\" : \"L123\",\n" +
                "\"type\" : 111,\n" +
                "\"identificationId\" : \"123\",\n" +
                "\"name\" : \"张三\",\n" +
                "\"region\" : 3,\n" +
                "\"nation\" : 1,\n" +
                "\"birthday\" : \"1990-01-01\",\n" +
                "\"gender\" : 1,\n" +
                "\"phone\" : \"1233\",\n" +
                "\"address\" : \"滨安路1191号\",\n" +
                "\"memo\" : \"test\",\n" +
                "\"facePicture\" : {\n" +
                "\"type\" : 0,\n" +
                "\"data\" : \"UIM232\",\n" +
                "\"url\" : \"http://...jsp\"\n" +
                "}\n" +
                "}";
        String response=HttpTestUtils.httpRequest(HttpEnum.POST,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = add(ip, Integer.valueOf(port), token);
    }
}
