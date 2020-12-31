package com.dahua.demo.faceService.capture;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 抓拍记录详情接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为recordId和content中resultPictureType，capTime等参数的值
 */
public class CaptureDetail extends BaseUserInfo {
    //recordId: 类型string 。抓拍记录id。必填。
    public static String recordId = "AL7BED26A1AGG3UKLG9N0220180809070113136540608625";

    public static final String ACTION = "/faceService/query/capture/"+recordId;

    private static String detail(String ip, int port, String token) throws Exception{
        /**
         * resultPictureType: 类型int 。返回的图片格式，0：表示只返回图片url，1：表示只返回base64，2：图片url和base64都
         * 返回，不填默认为0。选填。
         * capTime: 类型string 。抓拍时间，统一为TZ格式："yyyyMMdd'T'HHmmss'Z'"（表示零时区的UTC时间），当查询时
         * 间用，capTime减一秒和加一秒之间。选填。
         */
        String content = "?resultPictureType=2" +
                        "&capTime=20190802T064145Z";
        String response=HttpTestUtils.httpRequest(HttpEnum.GET,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = detail(ip, Integer.valueOf(port), token);
    }
}
