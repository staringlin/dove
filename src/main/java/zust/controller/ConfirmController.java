package zust.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import zust.entity.wechatSignature;
import zust.util.CheckUtil;

/**
 * @author star
 *
 */

@Controller
@RequestMapping("/confirmController")
public class ConfirmController {
	
	@Autowired
	HttpServletRequest request;
	@Autowired
	HttpServletResponse response;
	
	@RequestMapping("/confirm")
	@ResponseBody
	public void confirm(wechatSignature ws) throws IOException{
		request.setCharacterEncoding("UTF-8");  //微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
        response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
        if(CheckUtil.checkSignature(ws.getSignature(), ws.getTimestamp(), ws.getNonce())){
        	response.getWriter().write(ws.getEchostr());
        }else{
        	System.out.println("error");
        }
        out.close();
        
	}

}
