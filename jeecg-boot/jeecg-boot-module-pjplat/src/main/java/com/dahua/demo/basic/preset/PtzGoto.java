package com.dahua.demo.basic.preset;//package com.dahuatch.Login;


import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 转动到预置点接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方  channelCode 和index  和content 中speed参数
 */
class PtzGoto extends BaseUserInfo
{
	//此处为通道的channelCode
	public static final String channelCode="eyuU3191A1BPHRPMIUOKSD";

	//此处为预置点编号
	public static final String index="1";

	public static final String ACTION = "/videoService/ptz/preset/goto/channels/"+channelCode+"/"+index;

	private static String getPtzGoto(String ip, int port, String token) throws Exception{
		/**
		 * speed:类型double ，云台运行速度，归一化 0~1 ，如果没有speed字段表示默认速度
		 */
		String content = "{\"speed\" : 0.5}";
		String response=HttpTestUtils.httpRequest(HttpEnum.PUT,ip,port,ACTION,token,content);
		System.out.println(response);
		return response;
	}

	public static void main(String[] args) throws Exception {
		String rsp = getPtzGoto(ip, Integer.valueOf(port), token);
	}
}



