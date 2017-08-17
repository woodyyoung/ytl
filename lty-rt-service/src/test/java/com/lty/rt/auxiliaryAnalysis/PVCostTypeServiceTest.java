package com.lty.rt.auxiliaryAnalysis;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lty.rt.auxiliaryAnalysis.entity.PVCostType;
import com.lty.rt.auxiliaryAnalysis.service.PVCostTypeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/applicationContext.xml" })
public class PVCostTypeServiceTest {

    @Resource
    PVCostTypeService pVCostTypeService;  

	/**
	 * 
	 * 查询
	 * */
	@Test
	public void getAllPVCostType() throws IllegalAccessException, InvocationTargetException, IntrospectionException {
		pVCostTypeService.getPVCostTypes();
	}
	
	/**
	 * 
	 * 增加
	 * */
	//@Before
	public void setPVCostType() throws IllegalAccessException, InvocationTargetException, IntrospectionException{
		PVCostType pvCostType = new PVCostType();
		pvCostType.setPvcosttype_name("test_pvcosttype_name");
		pvCostType.setCreate_time(new Date());
		pvCostType.setNote("test_note");
		pvCostType.setUser_id("test_user_id");
		pvCostType.setUser_name("test_user_name");
		pVCostTypeService.setPVCostType(pvCostType);
	}
	
	/**
	 * 
	 * 修改
	 * */
	//@Before
	public void updPVCostType() throws IllegalAccessException, InvocationTargetException, IntrospectionException{
		PVCostType pvCostType = new PVCostType();
		pvCostType.setPvcosttype_id("2e34002ec3cf4af3a4927a348b7fac16");
		pvCostType.setPvcosttype_name("test_pvcosttype_nameGGG");
		pvCostType.setCreate_time(new Date());
		pvCostType.setNote("test_note");
		pvCostType.setUser_id("test_user_id");
		pvCostType.setUser_name("test_user_name");
		pVCostTypeService.updPVCostType(pvCostType);
	}
	
	/**
	 * 
	 * 删除
	 * */
	@Before
	public void delPVCostType() throws IllegalAccessException, InvocationTargetException, IntrospectionException{
		List<String> list=new ArrayList<String>();
		list.add("bec1b50a6eff48c0adb9df5885975b94");
		pVCostTypeService.delPVCostTypes(list);
	}
}
