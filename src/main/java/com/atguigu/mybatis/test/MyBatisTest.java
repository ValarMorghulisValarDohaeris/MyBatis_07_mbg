package com.atguigu.mybatis.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.atguigu.mybatis.beans.Employee;
import com.atguigu.mybatis.beans.EmployeeExample;
import com.atguigu.mybatis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

/*import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.dao.EmployeeMapper;*/

public class MyBatisTest {

	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(inputStream);
	}

	@Test
	public void testMbg() throws Exception {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		File configFile = new File("src/mbg.xml");
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
				callback, warnings);
		myBatisGenerator.generate(null);
	}

	@Test
	public void testSimple() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession();

		try{
//			EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
//			List<Employee> list = mapper.selectAll();
//			for(Employee employee:list){
//				System.out.println(employee.getId());
//			}
		}finally {
			sqlSession.close();
		}
	}

	@Test
	public void testMybatis3() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession();

		try{
			EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
			//xxxExample就是封装查询条件的
			//1.查询所有
//			List<Employee> emps = mapper.selectByExample(null);

			//2.查询员工名字中有e字母的，和员工性别是1的
			//封装员工查询条件的example
			EmployeeExample employeeExample = new EmployeeExample();
			//创建一个Criteria，这个Criteria就是拼装查询条件
			//select id, last_name, gender, email, d_id from tbl_employee WHERE ( last_name like ? and gender = ? ) or( email like ? )
			EmployeeExample.Criteria criteria = employeeExample.createCriteria();
			criteria.andLastNameLike("%e%");
			criteria.andGenderEqualTo("1");

			EmployeeExample.Criteria criteria2 = employeeExample.createCriteria();
			criteria2.andEmailLike("%e%");
			employeeExample.or(criteria2);
			List<Employee> emps = mapper.selectByExample(employeeExample);
			for(Employee employee:emps){
				System.out.println(employee.getId());
			}
		}finally {
			sqlSession.close();
		}
	}
}
