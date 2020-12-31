package com.dahua.demo.basic.preset;//package com.dahuatch.Login;


import com.dahua.demo.login.Login;
import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;
import com.google.gson.Gson;

import java.util.Map;

/**
 * 云台转动接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方 channelCode   和content中pan，tilt等参数的内容
 */
class Ptz extends BaseUserInfo
{
	//此处为通道的channelCode
	public static final String channelCode="eyuU3191A1BPHRPMIUOKSD";

	public static final String ACTION = "/videoService/ptz/"+channelCode;

	private static String getPtz(String ip, int port, String token) throws Exception{
		/**
		 * pan:类型double ，必填。x:水平控制速度相对值(-1, 1) x>0为右转，反之左转
		 * version 1.20
		 * tilt:类型double ，必填。y:垂直控制速度相对值(-1, 1) y>0向上转，反之向下转
		 * zoom:类型double ，必填。z:变倍控制速度相对值(-1, 1) zoom>0为放大，反之缩小
		 * duration:类型int ，选填。持续时间，单位毫秒，默认100毫秒。只对部分设备有效，要想确保云台停止，发送 pan,
		 * tilt, zoom 都为0的请求。
		 */
		String content = "{\"pan\" : 0.1," +
						"\"tilt\" : -0.3," +
						"\"zoom\" : 0.4," +
						"\"duration\" : 100}";
		String response=HttpTestUtils.httpRequest(HttpEnum.PUT,ip,port,ACTION,token,content);
		System.out.println(response);
		return response;
	}

	public static void main(String[] args) throws Exception {
		String rsp = getPtz(ip, Integer.valueOf(port), token);
	}
}



