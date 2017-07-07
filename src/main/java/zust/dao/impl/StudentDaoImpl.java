package zust.dao.impl;

import org.springframework.stereotype.Repository;

import zust.dao.StudentDaoI;
import zust.entity.Student;

@Repository
public class StudentDaoImpl extends BaseDaoImpl<Student> implements StudentDaoI{

}
