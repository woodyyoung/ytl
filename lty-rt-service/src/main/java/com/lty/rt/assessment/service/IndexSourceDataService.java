package com.lty.rt.assessment.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lty.rt.assessment.bean.Index;
import com.lty.rt.assessment.bean.IndexLevel;
import com.lty.rt.assessment.bean.IndexSourceData;
import com.lty.rt.assessment.mapper.IndexLevelMapper;
import com.lty.rt.assessment.mapper.IndexMapper;
import com.lty.rt.assessment.mapper.IndexSourceDataMapper;
import com.lty.rt.assessment.vo.IndexSourceDataVo;
import com.lty.rt.comm.bean.QueryCommonDto;
import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.util.DateUtil;
import com.lty.rt.comm.util.UUIDUtils;

@Service("indexSourceDataService")
public class IndexSourceDataService {

	@Autowired
	private IndexSourceDataMapper indexSourceDataMapper;

	@Autowired
	private IndexMapper indexMapper;

	@Autowired
	private IndexLevelMapper indexLevelMapper;

	@Autowired
	private IndexService indexService;
	

	@Transactional
	public Map<String, List<IndexSourceDataVo>> getChartData(QueryCommonDto dto) {
		if (dto == null) {
			throw new ApplicationException(ReturnCode.ERROR_03.getCode(), ReturnCode.ERROR_03.getMsg() + ":dto");
		}
		if (StringUtils.isBlank(dto.getArg1())) {
			throw new ApplicationException(ReturnCode.ERROR_03.getCode(), ReturnCode.ERROR_03.getMsg() + ":arg1");
		}

		Map<String, List<IndexSourceDataVo>> resultMap = getChartDataByXXs(dto, dto.getArg1());
		return resultMap;
	}

	public Map<String, Object> getAvgData(QueryCommonDto dto, Map<String, List<IndexSourceDataVo>> resultMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		Index index = indexMapper.getIndexByID(dto.getArg());
		BigDecimal targetLevel = null;
		BigDecimal curAvg = null;
		BigDecimal compareAvg = null;
		if (index != null) {
			if (StringUtils.isNotBlank(index.getTargetlevel())) {
				Boolean flag = isNum(index.getTargetlevel());
				if (!flag) {
					throw new ApplicationException(ReturnCode.ERROR_02.getCode(),
							ReturnCode.ERROR_02.getMsg() + ":targetLevel");
				}
				targetLevel = new BigDecimal(index.getTargetlevel());
			} else {
				throw new ApplicationException(ReturnCode.ERROR_03.getCode(),
						ReturnCode.ERROR_03.getMsg() + ":targetLevel");
			}

			map.put("unit", index.getLevelUnit());
			map.put("descriptions", index.getDescriptions());
			map.put("targetLevel", targetLevel);
			map.put("charName", index.getName());
		}

		if (resultMap != null && !resultMap.isEmpty()) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("indexId", dto.getArg());
			List<IndexLevel> list = indexLevelMapper.getListByMap(param);

			BigDecimal oneHundred = new BigDecimal("100");
			curAvg = calcAvg(resultMap.get("1"));

			if (curAvg != null) {
				if (list != null && list.size() > 0) {
					for (IndexLevel il : list) {
						if (curAvg.compareTo(il.getLowerLimit()) >= 0 && curAvg.compareTo(il.getTopLimit()) < 0) {
							curAvg = new BigDecimal(il.getIndexLevel());
							map.put("curAvg", il.getIndexLevel());
							map.put("levelDescription", il.getDescription());
							map.put("proposal", il.getProposal());
							break;
						}
					}
				}
				int targetDiff = 0;
				if (curAvg.compareTo(new BigDecimal("0")) != 0) {
					targetDiff = Math.abs((((curAvg.subtract(targetLevel)).multiply(oneHundred).divide(curAvg, 2,
							BigDecimal.ROUND_HALF_UP))).intValue());
				}
				map.put("targetDiff", targetDiff);
			}

			// 差异率
			if (resultMap.size() > 1) {
				compareAvg = calcAvg(resultMap.get("2"));
				if (compareAvg != null) {
					if (list != null && list.size() > 0) {
						for (IndexLevel il : list) {
							if (compareAvg.compareTo(il.getLowerLimit()) >= 0
									&& compareAvg.compareTo(il.getTopLimit()) < 0) {
								compareAvg = new BigDecimal(il.getIndexLevel());
							}
						}
					}
					if (curAvg.compareTo(new BigDecimal("0")) != 0) {
						int compareDiff = Math.abs((((compareAvg.subtract(targetLevel)).multiply(oneHundred).divide(curAvg,
								2, BigDecimal.ROUND_HALF_UP))).intValue());
						map.put("compareDiff", compareDiff);
					}

					map.put("compareAvg", compareAvg);
				}
			}
			// 差值：分析时段与目标的差值，取绝对值
			int diff = Math.abs((curAvg.subtract(targetLevel)).intValue());
			map.put("diff", diff);
		}
		return map;
	}

	private BigDecimal calcAvg(List<IndexSourceDataVo> list) {
		BigDecimal total = new BigDecimal("0");
		Integer totalCount = 1;
		if (list != null && list.size() > 0) {
			totalCount = list.size();
			for (IndexSourceDataVo idv : list) {
				total = total.add(idv.getActualScore());
			}
		}
		return total.divide(new BigDecimal(totalCount), 2, BigDecimal.ROUND_HALF_UP);
	}

	private Map<String, List<IndexSourceDataVo>> getChartDataByXXs(QueryCommonDto dto, String flag) {
		Map<String, List<IndexSourceDataVo>> resultMap = new HashMap<String, List<IndexSourceDataVo>>();
		Map<String, Object> map = new HashMap<String, Object>();

		if (StringUtils.isNotBlank(dto.getBeginDate()) && StringUtils.isNotBlank(dto.getEndDate())) {
			map.put("arg", dto.getArg());
			map.put("beginDate", dto.getBeginDate());
			map.put("endDate", dto.getEndDate());
			if (StringUtils.isNotBlank(dto.getArg2())) {
				map.put("isWork", dto.getArg2());
			}
			if (StringUtils.isNotBlank(dto.getArg3())) {
				map.put("lineId", dto.getArg3());
			}
			if (StringUtils.isNotBlank(dto.getArg4())) {
				map.put("areaId", dto.getArg4());
			}

			List<IndexSourceDataVo> list = new ArrayList<IndexSourceDataVo>();
			if ("days".equals(dto.getArg1())) {
				list = indexSourceDataMapper.getChartDataByDays(map);
			} else if ("weeks".equals(dto.getArg1())) {
				list = indexSourceDataMapper.getChartDataByWeeks(map);
			} else if ("months".equals(dto.getArg1())) {
				list = indexSourceDataMapper.getChartDataByMonths(map);
			} else if ("years".equals(dto.getArg1())) {
				list = indexSourceDataMapper.getChartDataByYears(map);
			}
			resultMap.put("1", list);

		}

		if (StringUtils.isNotBlank(dto.getBeginDate2()) && StringUtils.isNotBlank(dto.getEndDate2())) {
			map.clear();
			map.put("arg", dto.getArg());
			map.put("beginDate", dto.getBeginDate2());
			map.put("endDate", dto.getEndDate2());
			if (StringUtils.isNotBlank(dto.getArg2())) {
				map.put("isWork", dto.getArg2());
			}

			List<IndexSourceDataVo> list = new ArrayList<IndexSourceDataVo>();

			if ("days".equals(dto.getArg1())) {
				list = indexSourceDataMapper.getChartDataByDays(map);
			} else if ("weeks".equals(dto.getArg1())) {
				list = indexSourceDataMapper.getChartDataByWeeks(map);
			} else if ("months".equals(dto.getArg1())) {
				list = indexSourceDataMapper.getChartDataByMonths(map);
			} else if ("years".equals(dto.getArg1())) {
				list = indexSourceDataMapper.getChartDataByYears(map);
			}
			resultMap.put("2", list);
		}
		return resultMap;
	}

	public List<IndexSourceDataVo> getScoresListByIndexId(Map<String, Object> map){
		return indexSourceDataMapper.getChartDataByDays(map);
	}
	
	public List<Map<String,Object>> searchIndexData(Map<String, Object> map){
		return indexSourceDataMapper.searchIndexData(map);
	}
	
	public Integer batchInsertIndexSourceData(List<IndexSourceData> list) {
		if (list == null) {
			throw new ApplicationException(ReturnCode.ERROR_03.getCode(), ReturnCode.ERROR_03.getMsg() + ":list");
		}
		if (list.size() > 0) {
			for (IndexSourceData isd : list) {
				isd.setId(UUIDUtils.getUUID().toString());
			}
			indexSourceDataMapper.batchInsertIndexSourceData(list);
		}
		return list.size();
	}

	@Transactional
	public Integer saveOrUpdate(Map<String, Object> map) throws ParseException {
			Integer count = null;

			String indexId = map.get("indexId") == null ? "" : map.get("indexId").toString();
			String lineId = map.get("lineId") == null ? "" : map.get("lineId").toString();
			String areaId = map.get("areaId") == null ? "" : map.get("areaId").toString();
			String actualScore = map.get("actualScore") == null ? "" : map.get("actualScore").toString();
			int rangeValue = map.get("rangeValue") == null ? 0 : Integer.parseInt(map.get("rangeValue").toString());//波动值
			
			String startTime = map.get("startTime") == null ? "" : map.get("startTime").toString();
			String endTime = map.get("endTime") == null ? "" : map.get("endTime").toString();
			List<IndexSourceData> resultList = new ArrayList<IndexSourceData>();
		
			Map<String, String> indexMap = new HashMap<String, String>();
			indexMap.put("indexId", indexId);
			indexMap.put("lineId", lineId);
			indexMap.put("areaId", areaId);
			indexMap.put("actualScore", actualScore);
			indexMap.put("startTime", startTime);
			indexMap.put("endTime", endTime);
			indexMap.put("inputPerson", (String)map.get("inputPerson"));
			indexMap.put("remark", (String)map.get("remark"));
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dBegin = sdf.parse(startTime);
			Date dEnd = sdf.parse(endTime);
			List<Date> lDate = findDates(dBegin, dEnd);
			//先删除
			indexSourceDataMapper.deleteIndexSourceDataByMap(indexMap);
			Random random = new Random();
			for (Date date : lDate) {
				indexMap.put("countDate", sdf.format(date));
				/*Integer hasData = indexSourceDataMapper.getIndexCountByMap(indexMap);
				if (1 <= hasData.intValue()) {
					indexSourceDataMapper.deleteIndexSourceDataByMap(indexMap);
				}*/
				IndexSourceData isd = validateDto(indexMap);
				if(rangeValue>0){
					double  indexValue = getRandIndexVal(random, isd.getActualScore().doubleValue(), rangeValue);
					isd.setActualScore(new BigDecimal(indexValue));
				}
				resultList.add(isd);
			}
			count = batchInsertIndexSourceData(resultList);
			
		
		return count;

	}
	
	public static void main(String[] args) {
		Random random = new Random();
		double actualScore = 20 ;
		System.out.println(getRandIndexVal(random, actualScore, 20));
	}
	
	private static double  getRandIndexVal(Random random,double actualScore,int rangeValue){
		int randPlus = random.nextInt(2);
		double result = 0;
		if(randPlus==0){
			result = actualScore - actualScore*random.nextInt(rangeValue)/100;
		}else{
			result =  actualScore + actualScore*random.nextInt(rangeValue)/100;
		}
		if(result<5){
			return (double)Math.round(result*10)/10;
		}
		return (double)Math.round(result);
	}

	public Integer update(Map<String, Object> map) throws ParseException{
		if(map == null){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode(), ReturnCode.ERROR_03.getMsg() + ":list");
		}
		if(map.get("indexId") == null || map.get("countDate") == null || map.get("indexNum") == null || map.get("indexTotalNum") == null){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode(), ReturnCode.ERROR_03.getMsg() + ":list");
		}
		
		IndexSourceData isd = new IndexSourceData();
		String indexId = (String)map.get("indexId");
		BigDecimal indexNum = new BigDecimal((String)map.get("indexNum"));
		BigDecimal indexTotalNum = new BigDecimal((String)map.get("indexTotalNum"));
		BigDecimal actualScore = indexNum.multiply(new BigDecimal("100")).divide(indexTotalNum, 2, BigDecimal.ROUND_HALF_UP);
		
		isd.setIndexId(indexId);
		isd.setCountDate(DateUtil.convertStringToDate(DateUtil.DATE, (String)map.get("countDate")));
		isd.setIndexNum(indexNum);
		isd.setIndexTotalNum(indexTotalNum);
		isd.setActualScore(actualScore);
		isd.setActualLevel(calcActualLevel(indexId, actualScore));
		
		return indexSourceDataMapper.updateScore(isd);
	}
	
	public Integer updateIndexData(Map<String, Object> map) throws ParseException{
		return indexSourceDataMapper.updateIndexData(map);
	}
	
	public Integer delByIndexIdAndDate(Map<String, Object> map){
		return indexSourceDataMapper.batchDelByMap(map);
	}
	
	public static List<Date> findDates(Date dBegin, Date dEnd) {
		List lDate = new ArrayList();
		lDate.add(dBegin);
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(calBegin.getTime());
		}
		return lDate;
	}

	public Index getIndexByID(String id) {
		return indexMapper.getIndexByID(id);
	}

	private IndexSourceData validateDto(Map<String, String> map) {
		IndexSourceData isd = new IndexSourceData();
		if (map == null) {
			throw new ApplicationException(ReturnCode.ERROR_03.getCode(), ReturnCode.ERROR_03.getMsg() + "，参数不能为空");

		}

		if (map.get("indexId") == null) {
			throw new ApplicationException(ReturnCode.ERROR_03.getCode(), ReturnCode.ERROR_03.getMsg() + ":indexId");
		}
		if (map.get("actualScore") == null) {
			throw new ApplicationException(ReturnCode.ERROR_03.getCode(),
					ReturnCode.ERROR_03.getMsg() + ":actualScore");
		} else {
			Boolean flag = isNum(map.get("actualScore"));// 判断是否为数字
			if (flag) {
				isd.setActualScore(new BigDecimal(map.get("actualScore")));
			} else {
				throw new ApplicationException(ReturnCode.ERROR_02.getCode(),
						ReturnCode.ERROR_02.getMsg() + ":actualScore必须为数字，正整数或正小数");
			}
		}
		isd.setIndexId(map.get("indexId"));
		//isd.setActualLevel(calcActualLevel(isd.getIndexId(), isd.getActualScore()));

		if (map.get("countDate") == null) {
			throw new ApplicationException(ReturnCode.ERROR_03.getCode(), ReturnCode.ERROR_03.getMsg() + ":countDate");
		} else {
			try {
				isd.setCountDate(DateUtil.convertStringToDate("yyyy-MM-dd", map.get("countDate")));
				isd.setIsWork(isWorkDay((String) map.get("countDate")));
			} catch (ParseException e) {
				e.printStackTrace();
				throw new ApplicationException(ReturnCode.ERROR_02.getCode(),
						ReturnCode.ERROR_02.getMsg() + ":countDate日期格式不正确");

			}
		}

		if (map.get("indexNum") == null) {
			isd.setIndexNum(new BigDecimal(0));
		} else {
			Boolean flag = isNum(map.get("indexNum"));// 判断是否为数字
			if (flag) {
				isd.setIndexNum(new BigDecimal(map.get("indexNum")));
			} else {
				throw new ApplicationException(ReturnCode.ERROR_02.getCode(),
						ReturnCode.ERROR_02.getMsg() + ":indexNum必须为数字，正整数或正小数");
			}
		}

		if (map.get("indexTotalNum") == null) {
			isd.setIndexTotalNum(new BigDecimal(0));
		} else {
			Boolean flag = isNum(map.get("indexTotalNum"));// 判断是否为数字
			if (flag) {
				isd.setIndexTotalNum(new BigDecimal(map.get("indexTotalNum")));
			} else {
				throw new ApplicationException(ReturnCode.ERROR_02.getCode(),
						ReturnCode.ERROR_02.getMsg() + ":indexTotalNum必须为数字，正整数或正小数");
			}
		}

		if (map.get("lineId") == null) {
			isd.setLineId(null);
		} else {
			isd.setLineId(map.get("lineId").toString());
		}

		if (map.get("areaId") == null) {
			isd.setAreaId(null);
		} else {
			isd.setAreaId(map.get("areaId").toString());
		}
		
		isd.setInputPerson(map.get("inputPerson"));
		isd.setRemark(map.get("remark"));

		return isd;
	}

	/**
	 * 计算实际得分
	 * 
	 * @param actualLevel
	 * @return
	 */
	private String calcActualLevel(String indexId, BigDecimal actualScore) {
		String actualLevel = "";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("indexId", indexId);
		List<IndexLevel> list = indexService.getIndexLevelListByMap(map);

		if (list == null || list.size() == 0) {
			throw new ApplicationException(ReturnCode.ERROR_03.getCode(),
					ReturnCode.ERROR_03.getMsg() + ":请先维护对应评价指标等级");
		}
		for (IndexLevel indexLevel : list) {
			if (actualScore.compareTo(indexLevel.getLowerLimit()) >= 0
					&& actualScore.compareTo(indexLevel.getTopLimit()) < 0) {
				actualLevel = indexLevel.getIndexLevel();
			}
		}
		return actualLevel;
	}

	/**
	 * 判断是否为数字：正整数和正小数，不能为负数
	 * 
	 * @param str
	 * @return true：是数字 false：不是数字
	 */
	private Boolean isNum(String str) {
		Pattern pattern = Pattern.compile("^\\d+$|^\\d+\\.\\d+$");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}

	/**
	 * 判断日期是否工作日
	 * 
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
		if (wd == 1 || wd == 7) {
			isWork = 0;
		} else {
			isWork = 1;
		}
		return isWork;
	}

}
