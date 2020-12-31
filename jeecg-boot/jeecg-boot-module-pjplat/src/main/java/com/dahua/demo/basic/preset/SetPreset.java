package com.dahua.demo.basic.preset;//package com.dahuatch.Login;


import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 设置预置点
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方包括  channelCode ，index 和content 中 name参数
 */
class SetPreset extends BaseUserInfo
{
	//此处为通道的channelCode
	public static final String channelCode="eyuU3191A1BPHRPMIUOKSD";

	//此处为预置点编号
	public static final String index="14";

	public static final String ACTION = "/videoService/ptz/preset/channels/"+channelCode+"/"+index;

	private static String setPtz(String ip, int port, String token) throws Exception{
		/**
		 * name：类型string ，预置点名称
		 */
		String content = "{\"name\" : \"aaa\"}";
		String response=HttpTestUtils.httpRequest(HttpEnum.PUT,ip,port,ACTION,token,content);
		System.out.println(response);
		return response;
	}

	public static void main(String[] args) throws Exception {
		String rsp = setPtz(ip, Integer.valueOf(port), token);
	}
}



