package com.dahua.demo.basic.video;//package com.dahuatch.Login;


import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;
import com.google.gson.Gson;

import java.util.Map;

/**
 * 录像查询接口
 *  注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 	需要修改的地方为content中channelCode，beginTime，location等参数
 * 	本demo的content中，只是给出了一个例子，可以根据参数选填的情况，自由组合content中参数内容
 */

class Record  extends BaseUserInfo
{
	public static final String ACTION = "/videoService/record/records";

	private static String getRecord(String ip, int port, String token) throws Exception{
		/**
		 * channelCode: 类型string 。必填。通道ID，当前只支持单通道查询。channelCode为之前获取的通道的id
		 * beginTime: 类型string ，必填。开始时间，UTC0时区时间，格式为YYYYMMDDTHHmmssZ。
		 * endTime: 类型string ，必填。结束时间，UTC0时区时间，格式为YYYYMMDDTHHmmssZ。
		 * location: 类型string ，必填。回放保存在哪里的录像。
		 *    录像位置描述
		 *       cloud 大华平台中心录像
		 *       device 设备录像
		 * recordType: 类型string ，选填。录像类型，不带参数表示不限类型。
		 *     录像类型描述
		 *        normal 普通录像
		 *        alarm 报警录像
		 *        pluse 手动录像
		 * motionDetect 动检录像
		 * recordSubType: 类型string ，选填。录像子类型，recordType为pluse时，表示自定义属性，自定义属性中的第一
		 * 类，报警联动录像，字典值为alarmLinkRecord。
		 * videoStream: 类型string ，选填。可以是main、extra1，不带参数表示不限类型。
		 */
		String content = "?channelCode=ux3bfAmjA1CFSGRBBP0B90" +
						"&beginTime=20201118T101020Z" +
						"&endTime=20201118T101120Z" +
						"&location=cloud";
		String response=HttpTestUtils.httpRequest(HttpEnum.GET,ip,port,ACTION,token,content);
		Map<String, Object> rsp = new Gson().fromJson(response, Map.class);
		System.out.println(response);
		return response;
	}

	public static void main(String[] args) throws Exception {
		String rsp = getRecord(ip, Integer.valueOf(port), token);
	}
}



