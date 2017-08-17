package com.lty.rt.template.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.lty.rt.basicData.bean.Line;
import com.lty.rt.basicData.mapper.LineMapper;
import com.lty.rt.passengerFlows.mapper.AreaPsgflowAnalysisMapper;
import com.lty.rt.passengerFlows.mapper.LinePsgflowAnalysisMapper;
import com.lty.rt.passengerFlows.mapper.StationPsgflowMapper;
import com.lty.rt.passengerFlows.mapper.StretchPsgflowAnalysisMapper;
import com.lty.rt.passengerFlows.service.AreaPsgFlowAnalysisService;
import com.lty.rt.passengerFlows.service.LinePsgFlowAnalysisService;
import com.lty.rt.passengerFlows.service.StretchPsgFlowAnalysisService;
import com.lty.rt.psgForecast.bean.ParamsConstants;
import com.lty.rt.psgForecast.mapper.StationPsgflowForecastMapper;

@ContextConfiguration(locations = {"classpath*:spring/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TemplateTest {
	@Autowired
	private StationPsgflowMapper stationPsgflowMapper;
	@Autowired
	private LineMapper lineMapper;
	@Autowired
	private LinePsgflowAnalysisMapper linePsgflowAnalysisMapper;
	@Autowired
	private AreaPsgflowAnalysisMapper areaPsgflowAnalysisMapper;
	@Autowired
	private StretchPsgflowAnalysisMapper stretchPsgflowAnalysisMapper;
	@Autowired
	private LinePsgFlowAnalysisService linePsgFlowAnalysisService;
	@Autowired
	private AreaPsgFlowAnalysisService areaPsgFlowAnalysisService;
	@Autowired
	private StretchPsgFlowAnalysisService stretchFlowAnalysisService;

	@Autowired
	private StationPsgflowForecastMapper stationPsgflowForecastMapper;
	
	@Test
	public void test_004() {
		
		/*<if test="queryType == 'hours'" >
	 	<if test="startDate != null and startDate != ''">
		AND T.DD = #{startDate}
		</if>
	</if>
	<if test="queryType != 'hours'" >
		<if test="startDate != null and startDate != '' and endDate !=null and endDate !=''">
			AND T.OCCUR_TIME &gt; TO_DATE(#{startDate}, 'yyyy-mm-dd')
			AND T.OCCUR_TIME &lt; TO_DATE(#{endDate}, 'yyyy-mm-dd')+1
		</if>
	</if>
	<if test="beginTime != null and beginTime != '' and endTime !=null and endTime !=''">
   		AND TO_CHAR(T.OCCUR_TIME,'hh24:mi:ss') &gt;= #{beginTime}
		AND TO_CHAR(T.OCCUR_TIME,'hh24:mi:ss') &lt;= #{endTime}
	</if>
	<if test="holidayFlag != null and holidayFlag != '' ">
		AND T.HOLIDAY_FLAG = #{holidayFlag}
	</if>
	AND T.stretch_code = #{stretchCode}*/
	
	
	
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", "2017-05-01");
		params.put("endDate", "2017-05-09");
		params.put("beginTime", "08:00:00");
		params.put("endTime", "10:00:00");
		params.put("stretchCode", "004");
		params.put("queryType", "days");
		//params.put("platformId", "{8BC78C29-2323-424C-B986-6B5D145E6316}");
		params.put("holidayFlag", "1");
		
		
		List<Map<String, Object>> queryStationDefaultPsgFlow =  stretchPsgflowAnalysisMapper.queryStretchDefaultPsgFlow(params);
		System.out.println(JSON.toJSONString(queryStationDefaultPsgFlow));
	}
	
	@Test
	public void test_003() {
		
		/*<if test="queryType == 'hours'" >
	 	<if test="startDate != null and startDate != ''">
		AND T.DD = #{startDate}
		</if>
	</if>
	<if test="queryType != 'hours'" >
		<if test="startDate != null and startDate != '' and endDate !=null and endDate !=''">
			AND T.OCCUR_TIME &gt; TO_DATE(#{startDate}, 'yyyy-mm-dd')
			AND T.OCCUR_TIME &lt; TO_DATE(#{endDate}, 'yyyy-mm-dd')+1
		</if>
	</if>
	<if test="beginTime != null and beginTime != '' and endTime !=null and endTime !=''">
   		AND TO_CHAR(T.OCCUR_TIME,'hh24:mi:ss') &gt;= #{beginTime}
		AND TO_CHAR(T.OCCUR_TIME,'hh24:mi:ss') &lt;= #{endTime}
	</if>
	<if test="holidayFlag != null and holidayFlag != '' ">
		AND T.HOLIDAY_FLAG = #{holidayFlag}
	</if>
	<if test="platformId != null and platformId!='' " >
		AND t.PLATFORM_ID = #{platformId}
	</if>
	AND t.AREA_CODE = #{sAreaCode}*/
	
	
	
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", "2017-05-01");
		params.put("endDate", "2017-05-09");
		//params.put("beginTime", "08:00:00");
		//params.put("endTime", "10:00:00");
		params.put("sAreaCode", "005004011");
		params.put("tAreaCode", "005004010");
		params.put("queryType", "days");
		//params.put("platformId", "{8BC78C29-2323-424C-B986-6B5D145E6316}");
		params.put("holidayFlag", "1");
		
		
		//List<Map<String, Object>> queryStationDefaultPsgFlow = areaPsgflowAnalysisMapper.queryAreaDefaultPsgFlow(params);
		List<Map<String, Object>> queryStationDefaultPsgFlow =  areaPsgflowAnalysisMapper.queryAreaODPsgFlow(params);
		System.out.println(JSON.toJSONString(queryStationDefaultPsgFlow));
	}
	
	
	@Test
	public void test_001() {
		
		/*<if test="startDate != null and startDate != '' and endDate !=null and endDate !=''">
		AND T.OCCUR_TIME &gt; TO_DATE(#{startDate}, 'yyyy-mm-dd')
		AND T.OCCUR_TIME &lt; TO_DATE(#{endDate}, 'yyyy-mm-dd')+1
	</if>
	<if test="beginTime != null and beginTime != '' and endTime !=null and endTime !=''">
   		AND TO_CHAR(T.OCCUR_TIME,'hh24:mi:ss') &gt;= #{beginTime}
		AND TO_CHAR(T.OCCUR_TIME,'hh24:mi:ss') &lt;= #{endTime}
	</if>
	<if test="holidayFlag != null and holidayFlag != '' ">
		AND T.HOLIDAY_FLAG = #{holidayFlag}
	</if>
	<if test="holidayFlag != null and holidayFlag != '' ">
		AND T.HOLIDAY_FLAG = #{holidayFlag}
	</if>
	AND T.PLATFORM_ID = #{sPlatformId}
	AND T.direction = #{platformDirection}*/
	
	
	
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", "2017-05-01");
		params.put("endDate", "2017-05-09");
		params.put("beginTime", "08:00:00");
		params.put("endTime", "10:00:00");
		params.put("sPlatformId", "9F535C3F-0D2A-45A7-A834-8FDC71F17B53");
		params.put("tPlatformId", "E1A30143-12A9-4A42-B5DC-FCDEE2795E01");
		params.put("queryType", "months");
		params.put("platformDirection", "0");
		//params.put("holidayFlag", "0");
		
		
		//List<Map<String, Object>> queryStationDefaultPsgFlow = stationPsgflowMapper.queryStationDefaultPsgFlow(params);
		List<Map<String, Object>> queryStationDefaultPsgFlow = stationPsgflowMapper.queryStationODPsgFlow(params);
		System.out.println(JSON.toJSONString(queryStationDefaultPsgFlow));
	}
	
	@Test
	public void test_002() {
		
		/*<if test="queryType == 'hours'" >
	 	<if test="startDate != null and startDate != ''">
		AND T.DD = #{startDate}
		</if>
	</if>
	<if test="queryType != 'hours'" >
		<if test="startDate != null and startDate != '' and endDate !=null and endDate !=''">
			AND T.OCCUR_TIME &gt; TO_DATE(#{startDate}, 'yyyy-mm-dd')
			AND T.OCCUR_TIME &lt; TO_DATE(#{endDate}, 'yyyy-mm-dd')+1
		</if>
	</if>
	<if test="beginTime != null and beginTime != '' and endTime !=null and endTime !=''">
   		AND TO_CHAR(T.OCCUR_TIME,'hh24:mi:ss') &gt;= #{beginTime}
		AND TO_CHAR(T.OCCUR_TIME,'hh24:mi:ss') &lt;= #{endTime}
	</if>
	<if test="holidayFlag != null and holidayFlag != '' ">
		AND T.HOLIDAY_FLAG = #{holidayFlag}
	</if>
	<if test="stationId != null and stationId!='' " >
		AND t.STATION_ID = #{stationId}
	</if>
	AND t.LINE_ID = #{lineId}*/
	
	
	
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", "2017-05-01");
		params.put("endDate", "2017-05-09");
		params.put("beginTime", "08:00:00");
		params.put("endTime", "10:00:00");
		params.put("stationId", "14154");
		params.put("tStationId", "14168");
		params.put("queryType", "days");
		params.put("lineId", "{8BC78C29-2323-424C-B986-6B5D145E6316}");
		params.put("holidayFlag", "1");
		
		
		//List<Map<String, Object>> queryStationDefaultPsgFlow = linePsgflowAnalysisMapper.queryLineDefaultPsgFlow(params);
		List<Map<String, Object>> queryStationDefaultPsgFlow =  linePsgflowAnalysisMapper.queryLineODPsgFlow(params);
		System.out.println(JSON.toJSONString(queryStationDefaultPsgFlow));
	}
	

	/*@Test
	public void test_1() {

		List<Map<String, Object>> queryPsgFlow = stationPsgflowMapper
				.queryPsgFlow("2016-09-01", "2016-09-05",
						new String[]{"54bfb4149b874162b5a12e057e35f42a"}, "0");

		System.out.println(JSONUtils.toJSONString(queryPsgFlow));
	}
	@Test
	public void test_2() {
		List<Map<String, Object>> queryPsgFlow = stationPsgflowMapper
				.queryPsgFlowForDays("2016-09-01", "2016-09-05", "2016-10-01",
						"2016-10-05",
						new String[]{"54bfb4149b874162b5a12e057e35f42a"},null,null, "0");
		System.out.println(JSONUtils.toJSONString(queryPsgFlow));
	}
	@Test
	public void test_3() {
		List<Map<String, Object>> queryPsgFlow = stationPsgflowMapper
				.queryPsgFlowForMonths("2016-09-01", "2016-10-31",
						"2016-11-01", "2016-12-31",
						new String[]{"54bfb4149b874162b5a12e057e35f42a"}, "0",null,null);
		System.out.println(JSONUtils.toJSONString(queryPsgFlow));
	}
	@Test
	public void test_4() {
		List<Map<String, Object>> queryPsgFlow = stationPsgflowMapper
				.queryPsgFlowForWeeks("2016-09-01", "2016-11-30", "2016-12-01",
						"2016-12-31",
						new String[]{"54bfb4149b874162b5a12e057e35f42a"}, "0",null,null);
		System.out.println(JSONUtils.toJSONString(queryPsgFlow));
	}*/
	@Test
	public void test_5() {
		/*
		 * Map<String, Object> queryPsgFlowForWeeks = linePsgFlowAnalysisService
		 * .queryPsgFlowForWeeks("7d7422284b694237a87df0f04ffa7b9e",
		 * "2016-09-01", "2016-11-30", "2016-12-01", "2016-12-31",
		 * "1593a843194648a09390189b4d7d57b1", "0", "0");
		 * 
		 * System.out.println(JSONUtils.toJSONString(queryPsgFlowForWeeks));
		 */
	}
	@Test
	public void test_6() {
		List<Line> list = lineMapper.listAllLine();

		System.out.println(JSONUtils.toJSONString(list));
	}
	@Test
	public void test_7() {
		/*
		 * Map<String, Object> list = areaPsgFlowAnalysisService
		 * .queryPsgFlowForDays("", "2016-09-01", "2016-09-05", "2016-10-01",
		 * "2016-10-05");
		 * 
		 * System.out.println(JSONUtils.toJSONString(list));
		 */
	}
	@Test
	public void test_8() {
		/*
		 * Map<String, Object> list = areaPsgFlowAnalysisService
		 * .queryStationFlowForHours("000", "2016-09-01", "2016-10-01");
		 * 
		 * System.out.println(JSONUtils.toJSONString(list));
		 */
	}
	@Test
	public void test_9() {
		/*
		 * Map<String, Object> list = stretchFlowAnalysisService
		 * .queryStationFlowForHours("000", "2016-09-01", "2016-10-01");
		 * 
		 * System.out.println(JSONUtils.toJSONString(list));
		 */
	}

	@Test
	public void test_10() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ParamsConstants.PARAM_KEY_BEGIN_DATE, "2016-09-01");
		map.put(ParamsConstants.PARAM_KEY_END_DATE, "2017-01-01");
		// map.put(ParamsConstants.PARAM_KEY_END_DATE, "2017-02-01");
		List<Map<String, Object>> list = stationPsgflowForecastMapper
				.queryMonthData(map);

		System.out.println(JSONUtils.toJSONString(list));
	}
	@Test
	public void test_11() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ParamsConstants.PARAM_KEY_BEGIN_DATE, "2016-09-01");
		map.put(ParamsConstants.PARAM_KEY_END_DATE, "2017-01-01");
		map.put("days", new String[]{"01", "02", "03", "04"});
		map.put("item", "01");
		String[] days = new String[]{"01", "02", "03", "04"};
		for (String day : days) {
			// map.put(ParamsConstants.PARAM_KEY_END_DATE, "2017-02-01");
			map.put("item", day);
			List<Map<String, Object>> list = stationPsgflowForecastMapper
					.queryDaysData(map);
			System.out.println(JSONUtils.toJSONString(list));
		}

	}
}
