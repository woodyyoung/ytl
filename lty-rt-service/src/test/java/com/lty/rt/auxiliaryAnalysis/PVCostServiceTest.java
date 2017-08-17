package com.lty.rt.auxiliaryAnalysis;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lty.rt.auxiliaryAnalysis.entity.PVCost;
import com.lty.rt.auxiliaryAnalysis.entity.PVCostType;
import com.lty.rt.auxiliaryAnalysis.service.PVCostService;
import com.lty.rt.auxiliaryAnalysis.service.PVCostTypeService;
import com.lty.rt.auxiliaryAnalysis.utils.BeanToMapUtil;
import com.lty.rt.basicData.service.LineService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/applicationContext.xml" })
public class PVCostServiceTest {

    @Resource
    PVCostService pVCostService;  
    
    @Resource
    PVCostTypeService pVCostTypeService;  
    
    @Resource
    LineService lineService;  
    
	/**
	 * 
	 * 查询
	 * */
	@Test
	public void getAllPVCost() throws IllegalAccessException, InvocationTargetException, IntrospectionException {
		List<Map<String,Object>> pVCosts=pVCostService.getPVCosts(null);
		Assert.assertTrue(pVCosts.size()>0);
	}
	
	/**
	 * 
	 * 增加 修改
	 * */
	@Before
	public void setPVCost() throws IllegalAccessException, InvocationTargetException, IntrospectionException{
		PVCostType pvCostType = new PVCostType();
		pvCostType.setPvcosttype_name("test_pvcosttype_name");
		pvCostType.setCreate_time(new Date());
		pvCostType.setNote("test_note");
		pvCostType.setUser_id("test_user_id");
		pvCostType.setUser_name("test_user_name");
		pVCostTypeService.setPVCostType(pvCostType);
		
		String pvCostType_id=String.valueOf(pVCostTypeService.getPVCostTypes().get(0).get("pvcosttype_id"));
		assertNotNull(pvCostType_id);

		PVCost PVCost = new PVCost();
		PVCost.setYear("2017");
		PVCost.setLine_id("test_line_id");
		PVCost.setNote("test_note");
		PVCost.setCompany_id("test_company_id");
		PVCost.setPvcosttype_id(pvCostType_id);
		PVCost.setMoney("pvcosttype_id");
		PVCost.setUser_id("test_user_id");
		PVCost.setUser_name("test_user_name");
		PVCost.setCreate_time(new Date());
		
		pVCostService.setPVCost(PVCost);
		
		PVCost.setYear("1980");
		PVCost.setLine_id("test_line_update_id");
		PVCost.setNote("test_update_note");
		PVCost.setCompany_id("test_company_update_id");
		PVCost.setPvcosttype_id(pvCostType_id);
		PVCost.setMoney("pvcosttype_update_id");
		PVCost.setUser_id("test_user_update_id");
		PVCost.setUser_name("test_user_update_name");
		PVCost.setCreate_time(new Date());
		pVCostService.updPVCost(PVCost);
	}
	
	/**
	 * 
	 * 修改
	 * @throws InstantiationException 
	 * */
	//@Before
	public void updPVCost() throws IllegalAccessException, InvocationTargetException, IntrospectionException, InstantiationException{
		PVCost PVCost=(com.lty.rt.auxiliaryAnalysis.entity.PVCost) BeanToMapUtil.convertMap(PVCost.class, pVCostService.getPVCosts(null).get(0));
		PVCost.setYear("1980");
		PVCost.setLine_id("test_line_update_id");
		PVCost.setNote("test_update_note");
		PVCost.setCompany_id("test_company_update_id");
		PVCost.setPvcosttype_id("2e34002ec3cf4af3a4927a348b7fac16");
		PVCost.setMoney("pvcosttype_update_id");
		PVCost.setUser_id("test_user_update_id");
		PVCost.setUser_name("test_user_update_name");
		PVCost.setCreate_time(new Date());
		pVCostService.updPVCost(PVCost);
	}
	
	/**
	 * 
	 * 删除
	 * */
	@After
	public void delPVCost() throws IllegalAccessException, InvocationTargetException, IntrospectionException{
		List<String> list=new ArrayList<String>();
		List<Map<String,Object>> pVCosts=pVCostService.getPVCosts(null);
		for (Map<String,Object> pvCost : pVCosts) {
			list.add(String.valueOf(pvCost.get("pvcost_id")));
		}
		pVCostService.delPVCost(list.get(0));
		assertTrue(pVCostService.getPVCosts(null).size()==0);
		
	}
	
	/**
	 * 获取线路
	 * */
	@Test
	public void getAllLines(){
		String ID = String.valueOf(pVCostService.getDepartments().get(1).get("ID"));
		Assert.assertNotNull(ID);
		List<Map<String,Object>> lines = lineService.listAllLineDetail(ID);
		Assert.assertTrue(lines.size()>0);
	}
	
	/**
	 * 获取公司
	 * */
	@Test
	public void getAllDepartments(){
		List<Map<String,Object>> departments = pVCostService.getDepartments();
		Assert.assertTrue(departments.size()>0);
	}
}
