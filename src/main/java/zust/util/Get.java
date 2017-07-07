package zust.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import zust.entity.WebAccess;


public class Get {
	//获取access_token 请求
	public static String get(String url,String appid,String secret) throws IOException{
		url = url+"?"+"grant_type=client_credential"+"&appid="+appid+"&secret="+secret;
		URL getUrl  = new URL(url);
		HttpURLConnection connection =(HttpURLConnection)getUrl.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
		connection.setDoOutput(true); 
		connection.setDoInput(true);
        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");//连接超时30秒
        System.setProperty("sun.net.client.defaultReadTimeout", "30000"); //读取超时30秒
		//进行连接
		connection.connect();
		//获得输入流
		InputStream is =connection.getInputStream();
        int size =is.available();
        byte[] jsonBytes =new byte[size];
        is.read(jsonBytes);
        String message = new String(jsonBytes,"UTF-8");	
		return message;
	}
	
	//获取code 请求
	//微信appid,返回类型,回调地址
	public static void getCode(String appid,String scope,String redirect_url) throws IOException{
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize";
		redirect_url = URLEncoder.encode(redirect_url,"UTF-8");
		url = url+"?"+"appid="+appid+"&redirect_uri="+redirect_url+"&response_type=code&scope="+scope+"&state=123#wechat_redirect";
		System.out.println(url);
		URL getUrl  = new URL(url);
		HttpURLConnection connection =(HttpURLConnection)getUrl.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
		connection.setDoOutput(true); 
		connection.setDoInput(true);
        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");//连接超时30秒
        System.setProperty("sun.net.client.defaultReadTimeout", "30000"); //读取超时30秒
		//进行连接
		connection.connect();
		//获得输入流
		connection.disconnect();
	}
	//获取网页授权token和openid
	public static String[] getWebAceessToken(String url,String appid,String secret,String code) throws IOException{
		url = url+"?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
		System.out.println(url);
		String[] openmsg = new String[5];
		URL getUrl  = new URL(url);
		HttpURLConnection connection =(HttpURLConnection)getUrl.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
		connection.setDoOutput(true); 
		connection.setDoInput(true);
        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");//连接超时30秒
        System.setProperty("sun.net.client.defaultReadTimeout", "30000"); //读取超时30秒
		//进行连接
		connection.connect();
		//获得输入流
		InputStream is =connection.getInputStream();
        int size =is.available();
        byte[] jsonBytes =new byte[size];
        is.read(jsonBytes);
        String message = new String(jsonBytes,"UTF-8");	
        //解析返回的json数据
        WebAccess accessParse = JSON.parseObject(message,WebAccess.class);
        if(accessParse.getAccess_token()!=null){       
        System.out.println("parserWebToken: "+accessParse.getAccess_token());
        System.out.println("timeout: "+accessParse.getExpires_in());
        openmsg[0] = accessParse.getAccess_token();
        openmsg[1] = accessParse.getExpires_in();
        openmsg[2] = accessParse.getRefresh_token();
        openmsg[3] = accessParse.getOpenid();
        openmsg[4] = accessParse.getScope();
        }else{
        	System.out.println("请求WebToken失败 errcode: "+accessParse.getErrCode()+" errmsg:{}"+accessParse.getErrMsg());
        }
        return openmsg;
	}
}
