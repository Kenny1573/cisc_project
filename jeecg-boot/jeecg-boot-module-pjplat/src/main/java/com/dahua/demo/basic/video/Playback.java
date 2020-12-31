package com.dahua.demo.basic.video;//package com.dahuatch.Login;


import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 获取回放URI
 *  注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 	需要修改的地方为content中channelCode，resource，beginTime等参数
 * 	本demo的content中，只是给出了一个例子，可以根据参数选填的情况，自由组合content中参数内容
 */

class Playback extends BaseUserInfo
{
	public static final String ACTION = "/videoService/playback/uri";

	private static String getPlayback(String ip, int port, String token) throws Exception{
		/**
		 * channelCode:类型string ，必填。视频通道编码。
		 * resource:类型string ，选填。按文件回放需要，视频文件资源路径，可以通过录像查询的file字段获得，或者手动上
		 * 传视频文件。
		 * beginTime:类型string ，必填。按文件和按时间回放都需要，开始回放时间。时间格式:YYYYMMDDTHHmmssZ。
		 * endTime:类型string ，必填。按文件和按时间回放都需要，结束回放时间。时间格式:YYYYMMDDTHHmmssZ。
		 * location: 类型string ，必填。录像存储位置。
		 * 协议类型描述
		 * cloud 平台录像
		 * device 设备录像
		 * scheme:类型string ，选填。协议类型，支持RTSP、HLS两种。默认RTSP。
		 * duration:类型int ，选填。有效时间，单位为秒，最长不超过10分钟，默认10分钟。
		 */
		String channelCode="ux3bfAmjA1CFSGRBBP0B90";
		String content = "?channelCode="+channelCode+
						"&resource=efs://1201321975b277c6dab30fb7_Record_ux3bfAmjA1CFSGRBBP0B90/2020/11/18/175456_normal_normal_main.dav" +
						"&beginTime=20201118T101020Z" +
						"&endTime=20201118T101120Z" +
						"&scheme=RTSP" +
						"&location=cloud";
		String response=HttpTestUtils.httpRequest(HttpEnum.GET,ip,port,ACTION,token,content);
		System.out.println(response);
		return response;
	}

	public static void main(String[] args) throws Exception {
		String rsp = getPlayback(ip, Integer.valueOf(port), token);
	}
}



