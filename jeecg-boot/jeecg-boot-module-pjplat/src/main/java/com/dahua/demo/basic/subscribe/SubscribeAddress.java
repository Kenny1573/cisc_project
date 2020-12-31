package com.dahua.demo.basic.subscribe;//package com.dahuatch.Login;


import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 消息订阅接口
 *  注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 *
 */

class SubscribeAddress extends BaseUserInfo
{
	//此处的channelid为通道id
	public static final String ACTION = "/videoService/eventCenter/messages/subscribeAddress";

	private static String getAddress(String ip, int port, String token) throws Exception{
		String content="";
		String response=HttpTestUtils.httpRequest(HttpEnum.GET,ip,port,ACTION,token,content);
		System.out.println(response);
		return response;
	}

	public static void main(String[] args) throws Exception {
		String rsp = getAddress(ip, Integer.valueOf(port), token);
	}
}



