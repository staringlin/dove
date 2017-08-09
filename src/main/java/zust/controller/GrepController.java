package zust.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import zust.Config.APP;
import zust.entity.Course;

import zust.util.HttpUtils;
import zust.util.ImagePreProcess2;
import zust.util.parseHtml;


/**
 * @author star
 *
 */

@Controller
@RequestMapping("/grep")
public class GrepController {
	
	@Autowired
	HttpServletRequest request;
	@Autowired
	HttpServletResponse response;
	

	@RequestMapping("/login")
	@ResponseBody
	public String login(String password,String xh,String code) throws Exception{

		//正式登录
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost loginPost = new HttpPost("http://jwxt.zust.edu.cn.ez.zust.edu.cn/default2.aspx");
		loginPost.setHeader("Cookie",APP.cookie);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("__VIEWSTATE", APP.__VIEWSTATE));
        params.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", APP.__VIEWSTATEGENERATOR));
        params.add(new BasicNameValuePair("TextBox1", xh));
        params.add(new BasicNameValuePair("TextBox2", password));
        params.add(new BasicNameValuePair("RadioButtonList1", "学生"));
        params.add(new BasicNameValuePair("TextBox3",code));
        params.add(new BasicNameValuePair("Button1",""));
        params.add(new BasicNameValuePair("lbLanguage", ""));
        
        loginPost.setEntity(new UrlEncodedFormEntity(params));
        HttpResponse responsePost  = httpClient.execute(loginPost);
        Header locationHeader = responsePost.getFirstHeader("Location");
        if (locationHeader != null && "HTTP/1.1 302 Found".equals(responsePost.getStatusLine().toString())) {
            String login_success = locationHeader.getValue();// 获取登陆成功之后跳转链接
            System.out.println("跳转链接:"+login_success);
            httpClient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(login_success);
            httpget.setHeader("Cookie",APP.cookie);
            
            HttpResponse re2 = httpClient.execute(httpget);
            Document doc = Jsoup.parse(EntityUtils.toString(re2.getEntity(), "utf-8"));
            Element e = doc.getElementById("xhxm");
            if(e==null){
                return EntityUtils.toString(re2.getEntity(), "utf-8");
            }else{
                System.out.println("登陆成功");
                return e.text();
            }
        } else{
            System.out.println("登陆不成功，请稍后再试!");
            return null;
        }
    
        
    }
	
	

	@RequestMapping("/justify")
	public String getIDUrl(String password,String openId,String xh) throws Exception{
		String test = HttpUtils.getIDUrl(password,xh);
		//下载验证码
		String storeName = ImagePreProcess2.downloadImage();
		System.out.println(storeName);
		return "forward:/common/bound";
	}
	
	@RequestMapping("/getCourse")
	@ResponseBody
	public String getCourseTable() throws Exception{
		String tableUrl = "http://jwxt.zust.edu.cn.ez.zust.edu.cn/xskbcx.aspx?xh=1140299081&xm="+new String("林孝鑫".getBytes("utf-8"),"gbk")+"&gnmkdm=N121603";		
		HttpClient httpClient = new DefaultHttpClient();
		 HttpGet httpGet = new HttpGet(tableUrl);
	        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36"); 
	        httpGet.setHeader("Cookie",APP.cookie);
	        httpGet.setHeader("Referer","http://jwxt.zust.edu.cn.ez.zust.edu.cn/xs_main.aspx?xh=1140299081");
	        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
	                HttpStatus.SC_OK, "OK");		
	        response = httpClient.execute(httpGet);
	        String html = EntityUtils.toString(response.getEntity(), "utf-8");
	        
	        ArrayList<Course> courses=parseHtml.getCourseList(html);
	        
	        StringBuffer sb=new StringBuffer();  
	          

	        return sb.toString();
	}

}
