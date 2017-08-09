package zust.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;

import zust.Config.APP;

public class HttpUtils {
	
	   public static HttpResponse getRawHtml(String url) {
		   HttpClient client = new DefaultHttpClient();
	        //获取响应文件，即html，采用get方法获取响应数据
	        HttpGet httpGet = new HttpGet(url);
	        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36"); 
	        httpGet.setHeader("Cookie",APP.cookie);
	        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
	                HttpStatus.SC_OK, "OK");
	        try {
	            //执行get方法
	            response = client.execute(httpGet);
	        } catch (IOException e) {
	            e.printStackTrace();

	        } finally {
	            // getMethod.abort();
	        }
	        return response;
	    }
	   
	   /*
	    * 获取身份验证的验证码
	    * */
	   
	   public static String getIDUrl(String password,String xh) throws ClientProtocolException, IOException {
		   	String url = APP.ID_URL;
		   	WebClient wc = new WebClient(BrowserVersion.CHROME);  
		    wc.getOptions().setUseInsecureSSL(true);  
		    wc.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true  
		    wc.getOptions().setCssEnabled(false); // 禁用css支持  
		    wc.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常  
		    wc.getOptions().setTimeout(100000); // 设置连接超时时间 ，这里是10S。如果为0，则无限期等待  
		    wc.getOptions().setDoNotTrackEnabled(false);
		    wc.getCookieManager().setCookiesEnabled(true);
		    HtmlPage page = wc  
		            .getPage(url);
		    DomElement e = page.getElementById("checkCode");
		    
		    HtmlInput username = (HtmlInput)page.getHtmlElementById("user");
		    HtmlInput pwd = (HtmlInput) page.getHtmlElementById("pass");
		    HtmlInput code = (HtmlInput) page.getHtmlElementById("inputCode");
		    HtmlInput purl = (HtmlInput) page.getElementByName("url");
		    purl.setValueAttribute("http://jwxt.zust.edu.cn/");
		    username.setValueAttribute(xh);
		    pwd.setValueAttribute(password);
		    code.setValueAttribute(e.asText());
		    HtmlSubmitInput   btn = (HtmlSubmitInput ) page.getHtmlElementById("login");
		    HtmlPage page2 = btn.click();
		    
		    CookieManager CM = wc.getCookieManager(); //WC = Your WebClient's name  
            Set<Cookie> cookies_ret = CM.getCookies();//返回的Cookie在这里，下次请求的时候可能可以用上啦。
            Iterator<Cookie> i = cookies_ret.iterator(); 
            StringBuilder sb = new StringBuilder();
            while (i.hasNext())   
            {  
            	Cookie one = i.next();
            	sb.append(one.getName()+"="+one.getValue()+";");
            	CM.addCookie(one);
            }  
            System.out.println(sb.toString());
            APP.cookie = sb.toString();
            System.out.println(APP.cookie);
		    System.out.println(page2.asText());
		    
		    //存储隐藏域表单值
		    HtmlInput hide1 = (HtmlInput) page2.getElementByName("__VIEWSTATE");
		    APP.__VIEWSTATE = hide1.getDefaultValue();
		    
		    wc.close();
	        return null;
	    }
	   


}
