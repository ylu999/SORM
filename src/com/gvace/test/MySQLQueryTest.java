package com.gvace.test;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gvace.model.Employee;
import com.gvace.sorm.core.MySQLQuery;
import com.gvace.vo.EmpVO;

public class MySQLQueryTest {
	MySQLQuery mySQLQuery = null;
	@Before
	public void before(){
		mySQLQuery = new MySQLQuery();
	}
	@Test
	public void testQuery(){
		
		List<Employee> list = new MySQLQuery().queryRows("SELECT  `id`,  `name`,  `salary`,  `birthday`, `age`, `departmentId` FROM `employee`" +
				"WHERE age>? and salary<?", Employee.class, new Integer[]{20,5000});
		for(Employee emp: list){
			System.out.println(emp.getName());
		}
	}
	@Test
	public void testUpdate(){
		Employee emp = new Employee();
		emp.setId(1);
		emp.setName("aaabbbbb");
		emp.setBirthday(new java.sql.Date(System.currentTimeMillis()+1000000000));
		//emp.setDepartmentId(0);
		mySQLQuery.update(emp,new String[]{"birthday"});
	}
	@Test
	public void testQueryVO(){
		String SQL = "SELECT " +
				"`employee`.`id` AS `id`, 	" +
				"`employee`.`name` AS `empName`,	" +
				"`employee`.`salary` + " +
				"`employee`.`bonus` AS `earning`,	" +
				"`employee`.`age` AS `age`, 	" +
				"`department`.`name` AS `deptName`,	" +
				"`department`.`address` AS `deptAddr` " +
				"FROM " +
				"`employee` " +
				"LEFT JOIN `department` " +
				"ON `employee`.`departmentId` = `department`.`id` "; 
		List<EmpVO> list = new MySQLQuery().queryRows(SQL, EmpVO.class, null);
		for(EmpVO empVO: list){
			System.out.println(empVO.getEmpName());
		}
	}
	@After
	public void after(){
		
	}
}

