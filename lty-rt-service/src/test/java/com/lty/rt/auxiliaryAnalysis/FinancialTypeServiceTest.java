package com.lty.rt.auxiliaryAnalysis;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lty.rt.auxiliaryAnalysis.entity.FinancialType;
import com.lty.rt.auxiliaryAnalysis.service.FinancialTypeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/applicationContext.xml" })
public class FinancialTypeServiceTest {

    @Autowired
    FinancialTypeService financialTypeService;  

	/**
	 * 
	 * 查询
	 * */
	@Test
	public void getAllFinancialType() throws IllegalAccessException, InvocationTargetException, IntrospectionException {
		FinancialType financialType = new FinancialType();
		financialType.setFinancialtype_object("1");
		Map<String,Object> map=financialTypeService.getFinancialTypes(financialType);
		for(String key : map.keySet()){
			System.out.println(" key : " + key + "  value :" + map.get(key));
		}
	}
	
	/**
	 * 
	 * 增加
	 * */
	//@Before
	public void setFinancialType() throws IllegalAccessException, InvocationTargetException, IntrospectionException{
		FinancialType financialType = new FinancialType();
		financialType.setFinancialtype_name("add_financialType_name");
		financialType.setCreate_time(new Date());
		financialType.setNote("add_note");
		financialType.setFinancialtype_object("3");
		financialType.setUser_id("add_user_id");
		financialType.setUser_name("add_user_name");
		financialTypeService.setFinancialType(financialType);
	}
	
	/**
	 * 
	 * 修改
	 * */
	//@Before
	public void updfinancialType() throws IllegalAccessException, InvocationTargetException, IntrospectionException{
		FinancialType financialType = new FinancialType();
		financialType.setFinancialtype_id("bbe2abc04caf4962973bc6107c2ecaa4");
		financialType.setFinancialtype_name("upd_financialType_name");
		financialType.setCreate_time(new Date());
		financialType.setNote("upd_note");
		financialType.setFinancialtype_object("1");
		financialType.setUser_id("upd_user_id");
		financialType.setUser_name("upd_user_name");
		financialTypeService.updFinancialType(financialType);
	}
	
	/**
	 * 
	 * 删除
	 * */
	//@Before
	public void delfinancialType() throws IllegalAccessException, InvocationTargetException, IntrospectionException{
		List<String> list=new ArrayList<String>();
		list.add("75d5dbe035784f96bb3c6d4487542672");
		list.add("bbe2abc04caf4962973bc6107c2ecaa4");
		list.add("9bf7b288ef43498e8be90a17c496a995");
		for (String string : list) {
			financialTypeService.delFinancialType(string);
		}
	}
}
