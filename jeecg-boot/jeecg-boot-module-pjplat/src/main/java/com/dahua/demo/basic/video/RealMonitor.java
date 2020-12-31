package com.dahua.demo.basic.video;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;
import com.google.gson.Gson;

import java.util.Map;

/**
 * 获取实时监视URI
 *  注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 		 请修改 content 中的 channelId 和 scheme 参数
 * 		 本demo的content中，只是给出了一个例子，可以根据参数选填的情况，自由组合content中参数内容
 */
public class RealMonitor extends BaseUserInfo
{
	public static final String ACTION = "/videoService/realmonitor/uri";

	//获取实时监视URI
	private static String getRealMonitor(String ip, int port, String token, String channelId,String scheme, int subType) throws Exception{
		/**
		 * channelId : 类型string ，必填。通道编码。channelID为之前调用分级获取设备通道的channelid，scheme为获取视频类型
		 * subType : 类型int ，选填。码流类型，0:主码流、1:辅流1、2:辅流2。默认为0主码流。
		 * scheme : 类型string ，选填。协议类型，支持RTSP、FLV_HTTP、HLS三种，默认RTSP。
		 */
		//String channelId="ux3bfAmjA1CFSGRBBP0BAG";
		//String scheme="HLS";
		String content = "?channelId=" +channelId+
						 "&subType="+subType+
				         "&scheme="+scheme;//8281
		String response=HttpTestUtils.httpRequest(HttpEnum.GET,ip,8281,ACTION,token,content);
		Map<String, Object> rsp = new Gson().fromJson(response, Map.class);
		Object url = rsp.get("url");
		/*
		 * String url_c = ""; if (scheme.equals("RTSP")) { url_c =
		 * url.toString().replace("10.130.1.63:8554", "36.155.108.35:8281"); } if
		 * (scheme.equals("FLV_HTTP")) { url_c = url.toString().replace("10.130.1.42",
		 * "36.155.108.35"); } if (scheme.equals("HLS")) { url_c =
		 * url.toString().replace("10.130.1.42:8050", "36.155.108.35:8281"); }
		 */
		 
		System.out.println("url:  "+url);

		return url+"";
	}

	public static void main(String[] args) throws Exception {
		//String url = getRealMonitor(ip, Integer.valueOf(port), token);
	}
	
	public static String getRealMonitorSource(String channelId, String scheme, int subType) throws Exception {
		return getRealMonitor(ip, Integer.valueOf(port), token, channelId, scheme, subType);
	}
}



