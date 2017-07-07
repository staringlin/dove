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
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
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
import com.thoughtworks.xstream.XStream;

import zust.Config.APP;
import zust.entity.ImageMessage;
import zust.entity.InputMessage;
import zust.entity.MsgType;
import zust.entity.OutputMessage;
import zust.entity.Token;
import zust.service.TokenServiceI;
import zust.util.CheckUtil;
import zust.util.Get;
import zust.util.SerializeXmlUtil;

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
            	//接受并返回消息
            	acceptMessage(request, response);
                
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
			
			//绑定授权地址
			String boundUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx14ffcda75c431299&redirect_uri=http%3A%2F%2F115.159.184.231%2Fdove%2Fwechat%2FgetOpenid&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
			String user_define_menu = "{\"button\":["
					+ "{\"name\":\"获取信息\",\"sub_button\":"
					+ "[{\"type\":\"view\",\"name\":\"历史通知\",\"key\":\"11\",\"url\":\"http://115.159.184.231/dove/wechat/test\"},"
					+ "{\"type\":\"click\",\"name\":\"课表信息\",\"key\":\"22\"}"
					+ ",{\"type\":\"click\",\"name\":\"课程信息\",\"key\":\"20_PROMANAGE\"}]},"
					+ "{\"type\":\"view\",\"name\":\"绑定\",\"key\":\"12\",\"url\":\""+boundUrl+"\"}]}";
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
	private String test() throws IOException {
		// TODO Auto-generated method stub
		return "boundInfo";
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
    
    public void acceptMessage(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	// 处理接收消息  
        ServletInputStream in = request.getInputStream();  
        // 将POST流转换为XStream对象  
        XStream xs = SerializeXmlUtil.createXstream();  
        xs.processAnnotations(InputMessage.class);  
        xs.processAnnotations(OutputMessage.class);  
        // 将指定节点下的xml节点数据映射为对象  
        xs.alias("xml", InputMessage.class);  
        // 将流转换为字符串  
        StringBuilder xmlMsg = new StringBuilder();  
        byte[] b = new byte[4096];  
        for (int n; (n = in.read(b)) != -1;) {  
            xmlMsg.append(new String(b, 0, n, "UTF-8"));  
        }  
        // 将xml内容转换为InputMessage对象  
        InputMessage inputMsg = (InputMessage) xs.fromXML(xmlMsg.toString());  
  
        String servername = inputMsg.getToUserName();// 服务端  
        String custermname = inputMsg.getFromUserName();// 客户端  
        long createTime = inputMsg.getCreateTime();// 接收时间  
        Long returnTime = Calendar.getInstance().getTimeInMillis() / 1000;// 返回时间  
  
        // 取得消息类型  
        String msgType = inputMsg.getMsgType();  
        // 根据消息类型获取对应的消息内容  
        if (msgType.equals(MsgType.Text.toString())) {  
            // 文本消息  
            System.out.println("开发者微信号：" + inputMsg.getToUserName());  
            System.out.println("发送方帐号：" + inputMsg.getFromUserName());  
            System.out.println("消息创建时间：" + inputMsg.getCreateTime() + new Date(createTime * 1000l));  
            System.out.println("消息内容：" + inputMsg.getContent());  
            System.out.println("消息Id：" + inputMsg.getMsgId());  
  
            StringBuffer str = new StringBuffer();  
            str.append("<xml>");  
            str.append("<ToUserName><![CDATA[" + custermname + "]]></ToUserName>");  
            str.append("<FromUserName><![CDATA[" + servername + "]]></FromUserName>");  
            str.append("<CreateTime>" + returnTime + "</CreateTime>");  
            str.append("<MsgType><![CDATA[" + msgType + "]]></MsgType>");  
            str.append("<Content><![CDATA[你说的是：" + inputMsg.getContent() + "，吗？]]></Content>");  
            str.append("</xml>");  
            System.out.println(str.toString());  
            response.getWriter().write(str.toString());  
        }  
        // 获取并返回事件消息  
        if (msgType.equals(MsgType.Event.toString())) { 
        	//订阅事件
        	String eventType = inputMsg.getEvent();
        	if("subscribe".equals(eventType)){
        		StringBuffer sb = new  StringBuffer();
        		sb.append("欢迎关注灰鸽子公众号，请您先绑定后操作\n");
        		sb.append("您可以点击获取信息按钮进行如下操作\n");
        		sb.append("1、获取历史通知\n");
        		sb.append("2、获取课程信息\n");
        		sb.append("3、获取课表信息\n");
                StringBuffer str = new StringBuffer();  
                str.append("<xml>");  
                str.append("<ToUserName><![CDATA[" + custermname + "]]></ToUserName>");  
                str.append("<FromUserName><![CDATA[" + servername + "]]></FromUserName>");  
                str.append("<CreateTime>" + returnTime + "</CreateTime>");  
                str.append("<MsgType><![CDATA[text]]></MsgType>");  
                str.append("<Content><![CDATA["+sb.toString()+"]]></Content>");  
                str.append("</xml>");  
                System.out.println(str.toString());  
                response.getWriter().write(str.toString()); 
        	}
        }

  
        
    }
    
    
    
    /**
     * 获取code
     *
     */
    @RequestMapping(value="/getCode",method = {RequestMethod.GET, RequestMethod.POST})
	private void getCode(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// TODO Auto-generated method stub
		String scope = "snsapi_userinfo";		
		Get.getCode(APP.appID, scope, APP.redirect_url);
	}
    /**
     * 获取openId
     *
     */
    @RequestMapping(value="/getOpenid",method = {RequestMethod.GET, RequestMethod.POST})
	private String getOpenid(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		String code = request.getParameter("code");	    
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token";		
		//获得网页授权token
		String[] openmsg = Get.getWebAceessToken(url, APP.appID, APP.appsecret, code);
		//获得的openid以及网页access_token
		String openid = openmsg[3];
		String access_token = openmsg[0];    	
		return "redirect:/common/bound?openId="+openid;
				
	}
    
}
