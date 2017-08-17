package com.lty.rt.auxiliaryAnalysis;


import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lty.rt.auxiliaryAnalysis.entity.Financial;
import com.lty.rt.auxiliaryAnalysis.service.FinancialService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/applicationContext.xml" })
public class FinancialServiceTest {

    @Resource
    FinancialService financialService;  
    
	/**
	 * 
	 * 查询 
	 * */
	//@Test
	public void getAllFinancial() throws IllegalAccessException, InvocationTargetException, IntrospectionException {
		Financial PVCost = new Financial();
		PVCost.setStartTime("1980");
		PVCost.setEndTime("2050");
		
		Map<String,Object> map=financialService.getFinancials(PVCost);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = (List<Map<String, Object>>) map.get("financialData");
		System.out.println("TOTAL="+list.size());

		for(Map<String,Object> mapData : list){
			System.out.println("FINANCIAL_ID="+mapData.get("FINANCIAL_ID"));
			System.out.println("\t"+mapData.get("MONEY"));
			System.out.println("\t"+mapData.get("YEAR"));
			System.out.println("\t"+mapData.get("CREATE_TIME"));
			System.out.println("\t"+mapData.get("USER_NAME"));
			System.out.println("\t"+mapData.get("NOTE"));
			
			System.out.println("\t"+mapData.get("FINANCIALTYPE_TYPE_ID"));
			System.out.println("\t"+mapData.get("FINANCIALTYPE_OBJECT_ID"));
			System.out.println("\t"+mapData.get("FINANCIALTYPE_SOURCE_ID"));

			System.out.println();
		}
	}
	
	/**
	 * 
	 * 增加
	 * */
	//@Before
	public void setFinancial() throws IllegalAccessException, InvocationTargetException, IntrospectionException{
		Financial PVCost = new Financial();
		PVCost.setYear("2018");
		PVCost.setNote("add_note");
		PVCost.setFinancialtype_type_id("3407455d2a6c41328ecdcf4e8243858f");
		PVCost.setFinancialtype_object_id("26175bc8ec4642d08a5e872b41712667");
		PVCost.setFinancialtype_source_id("3dc709cc812e4e308b708c2b5b317f09");
		PVCost.setMoney("50");
		PVCost.setUser_id("add_user_id");
		PVCost.setUser_name("add_user_name");
		PVCost.setCreate_time(new Date());
		financialService.setFinancial(PVCost);
		
	}
	
	/**
	 * 
	 * 修改
	 * */
	//@Before
	public void updFinancial() throws IllegalAccessException, InvocationTargetException, IntrospectionException{
		Financial PVCost = new Financial();
		PVCost.setFinancial_id("884060070d8a4d568e39a9c2157a9d45");
		PVCost.setYear("1980");
		PVCost.setNote("upd_note");
		PVCost.setFinancialtype_type_id("3407455d2a6c41328ecdcf4e8243858f");
		PVCost.setFinancialtype_object_id("26175bc8ec4642d08a5e872b41712667");
		PVCost.setFinancialtype_source_id("3dc709cc812e4e308b708c2b5b317f09");
		PVCost.setMoney("60");
		PVCost.setUser_id("upd_user_id");
		PVCost.setUser_name("upd_user_name");
		PVCost.setCreate_time(new Date());
		Map<String,Object> map=financialService.updFinancial(PVCost);
		System.out.println("insertFlag = "+map.get("insertFlag"));
	}
	
	/**
	 * 
	 * 删除
	 * */
	//@Before
	public void delFinancial() throws IllegalAccessException, InvocationTargetException, IntrospectionException{
		financialService.delFinancial("884060070d8a4d568e39a9c2157a9d45");
	}
	
	
	
	/**
	 * 
	 * 折现图查询查
	 * */
	//@Test
	public void getCharts() throws IllegalAccessException, InvocationTargetException, IntrospectionException {
		Financial PVCost = new Financial();
		PVCost.setStartTime("2015");
		PVCost.setEndTime("2020");
		PVCost.setFinancialtype_type_id("3407455d2a6c41328ecdcf4e8243858f");
		
		Map<String,Object> line=financialService.queryLineFinancialsByCondition(PVCost);
		
		Map<String,Object> pie=financialService.queryPieFinancialsByCondition(PVCost);

		System.out.println();
//		List<Map<String,Object>> list = (List<Map<String, Object>>) map.get("financialData");
//		System.out.println("TOTAL="+list.size());
//
//		for(Map<String,Object> mapData : list){
//			System.out.println("FINANCIAL_ID="+mapData.get("FINANCIAL_ID"));
//			System.out.println("\t"+mapData.get("MONEY"));
//			System.out.println("\t"+mapData.get("YEAR"));
//			System.out.println("\t"+mapData.get("CREATE_TIME"));
//			System.out.println("\t"+mapData.get("USER_NAME"));
//			System.out.println("\t"+mapData.get("NOTE"));
//			
//			System.out.println("\t"+mapData.get("FINANCIALTYPE_TYPE_ID"));
//			System.out.println("\t"+mapData.get("FINANCIALTYPE_OBJECT_ID"));
//			System.out.println("\t"+mapData.get("FINANCIALTYPE_SOURCE_ID"));
//
//			System.out.println();
//		}
	}
}
