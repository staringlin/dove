package zust.service.impl;

import java.io.IOException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import zust.Config.APP;
import zust.dao.TokenDaoI;
import zust.entity.Token;
import zust.service.TokenServiceI;

/**
 * @author star
 *	存取Token
 */

@Service
@Transactional
public class TokenServiceImpl implements TokenServiceI{
	@Autowired
	TokenDaoI tokenDao;
	
	public String FetchToken() throws IOException{
		Token token = tokenDao.get("from Token t where t.id = 1 ");
		long now =System.currentTimeMillis()/1000;
		System.out.println(now);
		long expires_in = Long.parseLong(token.getExpires_in());
		if(expires_in >= now){
			System.out.println("没有过期");
			return token.getAccess_token();
		}else{
			System.out.println("过期了");
	    	String resConetnt = zust.util.Get.get("https://api.weixin.qq.com/cgi-bin/token", APP.appID, APP.appsecret);
	        Token accessParse = JSON.parseObject(resConetnt,Token.class);
	        token.setAccess_token(accessParse.getAccess_token());
	        long deadline = Long.parseLong(accessParse.getExpires_in())+now;
	        token.setExpires_in(String.valueOf(deadline));
	        tokenDao.saveOrUpdate(token);
	        return accessParse.getAccess_token();
		}

	}

}
