package zust.service.impl;

import java.io.IOException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import zust.dao.StudentDaoI;
import zust.entity.Student;
import zust.service.StudentServiceI;
import zust.util.MD5Util;



/**
 * @author star
 *	存取Token
 */

@Service
@Transactional
public class StudentServiceImpl implements StudentServiceI{
	@Autowired
	StudentDaoI studentDao;
	


	@Override
	public String bound(String password,String openId,String xh) throws IOException {
		// TODO Auto-generated method stub
		Student stu = new Student();
		stu.setOpenId(openId);
		stu.setPassword(MD5Util.md5(password));
		stu.setS_id(xh);
		stu.setStudent_name("star");
		studentDao.save(stu);
		return "ok";
	}

}
