package zust.service;

import java.io.IOException;

public interface StudentServiceI {
	String bound(String password,String openId,String xh,String name) throws IOException;

}
