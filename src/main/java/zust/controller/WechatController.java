package zust.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import zust.Config.APP;
import zust.entity.Token;
import zust.service.TokenServiceI;
import zust.util.CheckUtil;

/**
 * @author star
 *
 */

@Controller
@RequestMapping("/wechat")
public class WechatController {

	@Autowired
	TokenServiceI tokenService;
    
    /**
     * 微信接入
     * @param wc
     * @return
     * @throws IOException 
     */
    @RequestMapping(value="/connect",method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void connectWeixin(HttpServletRequest request, HttpServletResponse response) throws IOException{
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）  
        request.setCharacterEncoding("UTF-8");  //微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
        response.setCharacterEncoding("UTF-8"); //在响应消息（回复消息给用户）时，也将编码方式设置为UTF-8，原理同上；
        
        boolean isGet = request.getMethod().toLowerCase().equals("get"); 
        PrintWriter out = response.getWriter();
         
        try {
            if (isGet) {
                String signature = request.getParameter("signature");// 微信加密签名  
                String timestamp = request.getParameter("timestamp");// 时间戳  
                String nonce = request.getParameter("nonce");// 随机数  
                String echostr = request.getParameter("echostr");//随机字符串  
                
                // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败 
                if (CheckUtil.checkSignature(signature, timestamp, nonce)) {  
                    response.getWriter().write(echostr);  
                } else {  
                    System.out.println("error");
                }
            }else{
                String respMessage = "异常消息！";
                System.out.println(respMessage);
                
            }
        } catch (Exception e) {
        	System.out.println("Connect the weixin server is error.");
        }finally{
            out.close();
        }
    }
    
    
    /**
     * 获取access_token
     *
     */
    @RequestMapping(value="/getAccessToken",method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
	private void getAccessToken() throws IOException {
		// TODO Auto-generated method stub
		String token = tokenService.FetchToken();
		System.out.println(token);
	}
    
    /**
     * 创建自定义菜单
     *
     */
    @RequestMapping(value="/createMenu",method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void CreateMenu() throws IOException{
		try {
			//此处需要修改 从数据库读取
			String access_token = tokenService.FetchToken();
			String user_define_menu = "{\"button\":["
					+ "{\"name\":\"获取信息\",\"sub_button\":"
					+ "[{\"type\":\"view\",\"name\":\"历史通知\",\"key\":\"11\",\"url\":\"http://www.baidu.com\"},"
					+ "{\"type\":\"click\",\"name\":\"课表信息\",\"key\":\"22\"}"
					+ ",{\"type\":\"click\",\"name\":\"课程信息\",\"key\":\"20_PROMANAGE\"}]},"
					+ "{\"type\":\"view\",\"name\":\"绑定\",\"key\":\"12\",\"url\":\"http://www.baidu.com\"}]}";
			String action = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+access_token;
			try {
				URL url = new URL(action);
				HttpURLConnection http = (HttpURLConnection) url.openConnection();

				http.setRequestMethod("POST");
				http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
				http.setDoOutput(true);
				http.setDoInput(true);
				System.setProperty("sun.net.client.defaultConnectTimeout", "30000");//连接超时30秒
				System.setProperty("sun.net.client.defaultReadTimeout", "30000"); //读取超时30秒

				http.connect();
				OutputStream os= http.getOutputStream();
				os.write(user_define_menu.getBytes("UTF-8"));//传入参数
				os.flush();
				os.close();

				InputStream is =http.getInputStream();
				int size =is.available();
				byte[] jsonBytes =new byte[size];
				is.read(jsonBytes);
				String message=new String(jsonBytes,"UTF-8");
				System.out.println(message);
				} catch (MalformedURLException e) {
				e.printStackTrace();
				} catch (IOException e) {
				e.printStackTrace();
				} 
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
    
    
    @RequestMapping(value="/test",method = {RequestMethod.GET, RequestMethod.POST})
	private void test() throws IOException {
		// TODO Auto-generated method stub
		String token = tokenService.FetchToken();
		System.out.println(token);
	}
    
    /**
     * 推送消息
     *
     */
    @RequestMapping(value="/sendMessage",method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void sendMessage(String content) throws IOException{
		try {
			String access_token = tokenService.FetchToken();
			//推送类型
			System.out.println(content);
			String sendType ="{\"filter\":{\"is_to_all\":true,\"tag_id\":2},"
					+ "\"text\":{\"content\":\""+content+"\"},"
					+ "\"msgtype\":\"text\"}" ;
			System.out.println(sendType);
			String action = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token="+access_token;
			try {
				URL url = new URL(action);
				HttpURLConnection http = (HttpURLConnection) url.openConnection();

				http.setRequestMethod("POST");
				http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
				http.setDoOutput(true);
				http.setDoInput(true);
				System.setProperty("sun.net.client.defaultConnectTimeout", "30000");//连接超时30秒
				System.setProperty("sun.net.client.defaultReadTimeout", "30000"); //读取超时30秒

				http.connect();
				OutputStream os= http.getOutputStream();
				os.write(sendType.getBytes("UTF-8"));//传入参数
				os.flush();
				os.close();

				InputStream is =http.getInputStream();
				int size =is.available();
				byte[] jsonBytes =new byte[size];
				is.read(jsonBytes);
				String message=new String(jsonBytes,"UTF-8");
				System.out.println(message);
				} catch (MalformedURLException e) {
				e.printStackTrace();
				} catch (IOException e) {
				e.printStackTrace();
				} 
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
    
    
    
}
