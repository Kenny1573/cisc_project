package com.dahua.demo.basic.preset;//package com.dahuatch.Login;


import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 *  清除预置点
 *  注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 *     需要修改的参数包括 channelCode和index
 */
class DeletePreset extends BaseUserInfo
{
	//为通道的channelCode
	public static final String channelCode="eyuU3191A1BPHRPMIUOKSD";
	//预置点编号
	public static final String index="14";

	public static final String ACTION = "/videoService/ptz/preset/channels/"+channelCode+"/"+index;

	private static String setPtz(String ip, int port, String token) throws Exception{
		String content="";
		String response=HttpTestUtils.httpRequest(HttpEnum.DELETE,ip,port,ACTION,token,content);
		System.out.println(response);
		return response;
	}

	public static void main(String[] args) throws Exception {
		String rsp = setPtz(ip, Integer.valueOf(port), token);
	}
}



