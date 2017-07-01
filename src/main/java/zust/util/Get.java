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
}
