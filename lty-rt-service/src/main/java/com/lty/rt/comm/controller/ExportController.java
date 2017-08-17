package com.lty.rt.comm.controller;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.lty.rt.comm.util.DateUtil;
import com.lty.rt.comm.util.ExcelOutUtil;
import com.lty.rt.districtManagement.controller.AreaMapController;

@RequestMapping("/exportController")
@Controller
public class ExportController {
	
	private static final Logger logger  =  LoggerFactory.getLogger(AreaMapController.class);
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/export")
	public void excel(HttpServletResponse response,HttpServletRequest req){
		try{
			String data = req.getParameter("img");
			String tableData = req.getParameter("tableData");
			List<Map> list = JSONObject.parseArray(tableData, Map.class);

			String fileName = req.getParameter("fileName");
			String titleColumnStr = req.getParameter("titleColumn");
			String titleNameStr = req.getParameter("titleName");
			String titleSizeStr = req.getParameter("titleSize");
			String picLocation = req.getParameter("picLocation");
			

			ExcelOutUtil excelOutUtil = new ExcelOutUtil(null, fileName);

			//String contextPath = req.getContextPath();
			String contextPath = req.getSession().getServletContext().getRealPath("/");
			String filePath = contextPath + "\\pic";
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
			String picName = filePath + "\\" + fileName + "-" + DateUtil.convertDateToString(DateUtil.DATE_TIME_PATTERN, new Date()) + ".png";
			
			Integer[] picLct;
			if(StringUtils.isBlank(picLocation)){
				picLct = new Integer[1];
			}else{
				picLct = strToIntegerArray(picLocation); 
			}
			/*String userName = System.getProperty("user.name");
			String filePath = "C:\\Users\\" + userName + "\\Desktop\\chart";
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
			String picName = filePath + "\\" + System.currentTimeMillis() + ".png";

			String fileName = "站台客流统计分析";
			String [] titleColumn={"P_NAME","HH","ONBUS_PERSON_COUNT","OFFBUS_PERSON_COUNT"};
			String [] titleName={"站台","时间","上车人数","下车人数"};
			int [] titleSize={20,20,20,20};*/
			
			excelOutUtil.wirteExcel(strToStringArray(titleColumnStr), strToStringArray(titleNameStr), 
					strToIntegerArray(titleSizeStr), list, response, req, data, picName, picLct);
		} catch (Exception e) {
			logger.error("exportController.export() error{}",e);
		}
		 
	}
	
	private String[] strToStringArray(String str){
		 List<String> list = JSONObject.parseArray(str, String.class);
		 String[] strings = new String[list.size()];
		 list.toArray(strings);
		 return strings;
	}

	private Integer[] strToIntegerArray(String str){
		 List<Integer> list = JSONObject.parseArray(str, Integer.class);
		 Integer[] strings = new Integer[list.size()];
		 list.toArray(strings);
		 return strings;
	}

}
