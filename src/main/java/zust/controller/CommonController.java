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
import org.springframework.web.servlet.ModelAndView;

import zust.entity.wechatSignature;
import zust.service.StudentServiceI;
import zust.util.CheckUtil;

/**
 * @author star
 *
 */

@Controller
@RequestMapping("/common")
public class CommonController {
	
	@Autowired
	HttpServletRequest request;
	@Autowired
	HttpServletResponse response;
	@Autowired
	StudentServiceI studentService;
	
	@RequestMapping("/bound")
	public String bound(String openId) throws IOException{
		request.setAttribute("openId",openId);
        return "boundInfo";
	}
	
	@RequestMapping("/doBound")
	public String bound(String password,String openId,String xh) throws IOException{
		studentService.bound(password, openId, xh);
        return "test";
	}

}
