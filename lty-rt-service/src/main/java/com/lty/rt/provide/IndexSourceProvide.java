package com.lty.rt.provide;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.lty.rt.assessment.bean.IndexLevel;
import com.lty.rt.assessment.bean.IndexSourceData;
import com.lty.rt.assessment.controller.IndexController;
import com.lty.rt.assessment.service.IndexService;
import com.lty.rt.assessment.service.IndexSourceDataService;
import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.response.Response;
import com.lty.rt.comm.util.DateUtil;

@RequestMapping("/indexSourceProvide")
@Controller
public class IndexSourceProvide {

	private static final Logger logger  =  LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private IndexSourceDataService indexSourceDataService;
	
	@Autowired
	private IndexService indexService;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/batchInsert",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Response<Integer> batchInsert(@RequestBody String json) {
		logger.debug("begin batchInsert() : param{} ", JSONObject.toJSON(json));
		Long beginTime = System.currentTimeMillis();
		Response<Integer> resp = new Response<Integer>();
		try{
			List<IndexSourceData> resultList = new ArrayList<IndexSourceData>();
			if(StringUtils.isNotBlank(json)){
				String jsonStr = URLDecoder.decode(json,"UTF-8");
				String str[] = jsonStr.split("=");
				List<Map> list = JSONObject.parseArray(str[1], Map.class);
				for(Map<String, String> m : list){
					IndexSourceData isd = validateDto(m);
					resultList.add(isd);
				}
			} 
			Integer count = indexSourceDataService.batchInsertIndexSourceData(resultList);
			resp.setData(count);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		}catch(ApplicationException e){
			logger.warn("batchInsert() ApplicationException{}", e.getMessage());
			resp.setCode(e.getCode());
			resp.setMsg(e.getMessage());
		}catch(Exception ex){
			logger.error("batchInsert() error{}", ex);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		logger.debug("######"+(System.currentTimeMillis() - beginTime));
		return resp;
	}
	
	private IndexSourceData validateDto(Map<String, String> map){
		IndexSourceData isd = new IndexSourceData();
		if(map == null){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg()+"，参数不能为空");

		}
		
		if(map.get("indexId") == null){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg()+":indexId");
		}
		if(map.get("actualScore") == null){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg()+":actualScore");
		}else{
			Boolean flag = isNum(map.get("actualScore"));//判断是否为数字
			if(flag){
				isd.setActualScore(new BigDecimal(map.get("actualScore")));
			}else{
				throw new ApplicationException(ReturnCode.ERROR_02.getCode() ,ReturnCode.ERROR_02.getMsg()+":actualScore必须为数字，正整数或正小数");
			}
		}
		isd.setIndexId(map.get("indexId"));
		isd.setActualLevel(calcActualLevel(isd.getIndexId(), isd.getActualScore()));
		
		if(map.get("countDate") == null){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg()+":countDate");
		}else {
			try {
				isd.setCountDate(DateUtil.convertStringToDate("yyyy-MM-dd",map.get("countDate")));
				isd.setIsWork(isWorkDay((String)map.get("countDate")));
			} catch (ParseException e) {
				logger.error("validateDto() error{}", e);
				throw new ApplicationException(ReturnCode.ERROR_02.getCode() ,ReturnCode.ERROR_02.getMsg()+":countDate日期格式不正确");

			}
		}
		
		if(map.get("indexNum") == null){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg()+":indexNum");
		}else{
			Boolean flag = isNum(map.get("indexNum"));//判断是否为数字
			if(flag){
				isd.setIndexNum(new BigDecimal(map.get("indexNum")));
			}else{
				throw new ApplicationException(ReturnCode.ERROR_02.getCode() ,ReturnCode.ERROR_02.getMsg()+":indexNum必须为数字，正整数或正小数");
			}
		}
		
		if(map.get("indexTotalNum") == null){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg()+":indexTotalNum");
		}else{
			Boolean flag = isNum(map.get("indexTotalNum"));//判断是否为数字
			if(flag){
				isd.setIndexTotalNum(new BigDecimal(map.get("indexTotalNum")));
			}else{
				throw new ApplicationException(ReturnCode.ERROR_02.getCode() ,ReturnCode.ERROR_02.getMsg()+":indexTotalNum必须为数字，正整数或正小数");
			}
		}
		isd.setLineId("");
		isd.setAreaId("");
		return isd;
	}
	
	
	/**
	 * 计算实际得分
	 * @param actualLevel
	 * @return
	 */
	private String calcActualLevel(String indexId, BigDecimal actualScore){
		String actualLevel = "";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("indexId", indexId);
		List<IndexLevel> list = indexService.getIndexLevelListByMap(map);
		
		if(list == null || list.size() == 0){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg()+":请先维护对应评价指标等级");
		}
		for(IndexLevel indexLevel : list){
			if(actualScore.compareTo(indexLevel.getLowerLimit()) >= 0 && actualScore.compareTo(indexLevel.getTopLimit()) < 0){
				actualLevel = indexLevel.getIndexLevel();
			}
		}
		return actualLevel;
	}
	
	/**
	 * 判断是否为数字：正整数和正小数，不能为负数
	 * @param str
	 * @return true：是数字   false：不是数字
	 */
	private Boolean isNum(String str){
		Pattern pattern = Pattern.compile("^\\d+$|^\\d+\\.\\d+$");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}
	
	/**
	 * 判断日期是否工作日
	 * @param day
	 * @return
	 */
	public int isWorkDay(String day) {
		int isWork = 0;
		Calendar c = Calendar.getInstance(java.util.Locale.CHINA);
		String[] sp = day.split("-");
		c.set(Calendar.YEAR, Integer.parseInt(sp[0]));
		c.set(Calendar.MONTH, Integer.parseInt(sp[1]) - 1);
		c.set(Calendar.DATE, Integer.parseInt(sp[2]));

		int wd = c.get(Calendar.DAY_OF_WEEK);
		if(wd == 1 || wd == 7){
			isWork = 0;
		}else{
			isWork = 1;
		}
		return isWork;
	}
	
	public static void main(String[] args) {
	     /*String str = "0";
		 Pattern pattern = Pattern.compile("^\\d+$|^\\d+\\.\\d+$"); 
		 Matcher isNum = pattern.matcher(str);
		 System.out.println(isNum.matches());*/
		//String str="2017-2-5"; 
		//System.out.println(str instanceof String?"str是字符串":"不是字符串");  
		//System.out.println(str.getClass().getName().equals("java.util.Date")?"str是字符串":"不是字符串");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		Map<String, String> a1 = new HashMap<String, String>();
		Map<String, String> a2 = new HashMap<String, String>();
		a1.put("actualLevel", "8");
		a1.put("actualScore", "88");
		a1.put("countDate", "2017-1-09");
		a1.put("indexId", "008008");
		a1.put("indexNum", "50");
		a1.put("indexTotalNum", "100");
		
		a2.put("actualLevel", "8");
		a2.put("actualScore", "88");
		a2.put("countDate", "2017-1-09");
		a2.put("indexId", "008008");
		a2.put("indexNum", "55");
		a2.put("indexTotalNum", "222");
		list.add(a1);
		list.add(a2);
		
		String aa = JSONObject.toJSONString(list);
		List<Map> bb = JSONObject.parseArray(aa, Map.class);
		System.out.println(JSONObject.toJSONString(list));
		System.out.println(JSONObject.toJSONString(bb));
	}
}
