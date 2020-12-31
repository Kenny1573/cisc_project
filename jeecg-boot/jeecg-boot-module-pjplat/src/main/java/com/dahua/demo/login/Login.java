package com.dahua.demo.login;//package com.dahuatch.Login;

import java.io.*;
import java.util.Map;
import java.util.Properties;

import com.dahua.demo.login.bean.LoginFirst;
import com.dahua.demo.login.bean.LoginSecond;
import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

/**
 * 创建会话接口
 */
public class Login extends BaseUserInfo
{
	@Autowired
	private static RedisUtil redisUtil;
	
	public static final String ACTION = "/videoService/accounts/authorize";
	//第一次登陆，客户端只传用户名，服务端返回realm、readomKey和encryptType信息。
	private static String firstLogin(String ip, int port, String userName){
		LoginFirst loginFirst = new LoginFirst();
	    loginFirst.setClientType("winpc");
	    loginFirst.setUserName(userName);
		String rsp=HttpTestUtils.httpRequest(HttpEnum.POST,ip,port,ACTION,"",new Gson().toJson(loginFirst));
		return rsp;
	}
	
	//第二次登录，客户端根据返回的信息，按照指定的加密算法计算签名，再带着用户名和签名登陆一次。
	private static String secondLogin(String ip, int port, String userName, String password, String realm, String randomKey) throws Exception{
		LoginSecond snd = new LoginSecond();
		snd.setUserName(userName);
		snd.setClientType("winpc");
		snd.setRandomKey(randomKey);
		snd.setEncryptType("MD5");
		String signature = snd.calcSignature(password, realm);
		snd.setSignature(signature);
		Gson gson = new Gson();
		String ctx = gson.toJson(snd);
		String rsp=HttpTestUtils.httpRequest(HttpEnum.POST,ip,port,ACTION,"",ctx);
		return rsp;
	}

	@SuppressWarnings("unchecked")
	public static String login(String ip, int port, String userName, String password)throws Exception {
		String response = firstLogin(ip, port, userName);
		Map<String, String> responseMap = new Gson().fromJson(response, Map.class);
		String random = responseMap.get("randomKey");
		String realm = responseMap.get("realm");
		response = secondLogin(ip, port, userName, password, realm, random);
		return response;
	}

	@SuppressWarnings("unchecked")
	private static void tokenPro() {
		Properties pro=new Properties();
		pro.setProperty("token",token);
		try {
			File file=new File("src/main/resources/token.properties");
			OutputStreamWriter writer=new OutputStreamWriter(new FileOutputStream(file));
			pro.store(writer,"setToken");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 调用该登陆方法之前需要修改 baseinfo.properties中ip,port，name，password为当前对接环境
	 *	登录的方法，运行之后即可获取到token，进行登录,登陆完成后，必须调用KeepLogin保活接口，否则，2分钟分钟分钟分钟分钟分钟后会登陆过期
	 *
	 */
	public static void main(String[] args) throws Exception {
		token = HttpTestUtils.getToken(ip, Integer.valueOf(port), userName, password);
		tokenPro();
		System.out.println("登录成功，token="+token);
	}
	
	public static void loginVideo() throws NumberFormatException, Exception  {
		token = HttpTestUtils.getToken(ip, Integer.valueOf(port), userName, password);
		tokenPro();
		//将token存入redis中
		//redisUtil.set("htdw", token);
		System.out.println("视频云平台登录成功，token="+token);
	}
}



