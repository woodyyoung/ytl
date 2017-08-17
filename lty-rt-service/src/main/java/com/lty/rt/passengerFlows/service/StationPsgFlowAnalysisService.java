package com.lty.rt.passengerFlows.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.basicData.bean.PlatForm;
import com.lty.rt.basicData.mapper.PlatFormMapper;
import com.lty.rt.passengerFlows.mapper.StationPsgflowMapper;
import com.lty.rt.psgForecast.util.ForecastUtil;
import com.lty.rt.psgForecast.util.ICallBack;

/**
 * 站台客流分析统计Service
 * 
 * @author Administrator
 * 
 */
@Service
public class StationPsgFlowAnalysisService {
	@Autowired
	private PlatFormMapper platformMapper;
	@Autowired
	private StationPsgflowMapper stationPsgflowMapper;

	public List<PlatForm> listPlatform() {
		return platformMapper.listAllPlatform();
	}
	//后面改造优化用此方法查询站台客流
	public Map<String, Object> queryStationPsgFlow(Map<String, Object> params)
			throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		// 获取站台ID
		String sPlatformId = (String) params.get("sPlatformId");
		params.put("sPlatformId", sPlatformId.split("=")[0]);// 源站台ID
		params.put("platformDirection", sPlatformId.split("=")[1]);// 站台方向
		String tPlatformId = (String) params.get("tPlatformId");
		if (StringUtils.isNotBlank(tPlatformId)) {
			params.put("tPlatformId", tPlatformId.split("=")[0]);// 目标台ID
		}

		String psgType = (String) params.get("psgType");// 客流类型 1为站间客流 ,其他为客流集散量

		String beginDate = (String) params.get("beginDate");// 分析时段 开始时间
		String endDate = (String) params.get("endDate");// 分析时段 结束时间

		String beginDate2 = (String) params.get("beginDate2");// 对比时间时段 开始时间
		String endDate2 = (String) params.get("endDate2");// 对比时间段 结束时间

		if (psgType.equals("1")) {
			if (StringUtils.isNotBlank(beginDate)
					&& StringUtils.isNotBlank(endDate2)) {
				params.put("startDate", beginDate);
				params.put("endDate", endDate);
				List<Map<String, Object>> fxData = stationPsgflowMapper
						.queryStationODPsgFlow(params);
				result.put("fxData", fxData);
			}
			if (StringUtils.isNotBlank(beginDate2)
					&& StringUtils.isNotBlank(endDate2)) {
				params.put("startDate", beginDate2);
				params.put("endDate", endDate2);
				List<Map<String, Object>> dbData = stationPsgflowMapper
						.queryStationODPsgFlow(params);
				result.put("dbData", dbData);
			}
			return result;
		}

		if (StringUtils.isNotBlank(beginDate)
				&& StringUtils.isNotBlank(endDate2)) {
			params.put("startDate", beginDate);
			params.put("endDate", endDate);
			List<Map<String, Object>> fxData = stationPsgflowMapper
					.queryStationDefaultPsgFlow(params);
			result.put("fxData", fxData);
		}
		if (StringUtils.isNotBlank(beginDate2)
				&& StringUtils.isNotBlank(endDate2)) {
			params.put("startDate", beginDate2);
			params.put("endDate", endDate2);
			List<Map<String, Object>> dbData = stationPsgflowMapper
					.queryStationDefaultPsgFlow(params);
			result.put("dbData", dbData);
		}
		return result;

	}

	public List<Map<String, Object>> queryStationFlow(String beginDate,
			String endDate, String queryPlatforms, String holidayFlag) {
		return stationPsgflowMapper.queryPsgFlow(beginDate, endDate,
				queryPlatforms.split("=")[0], holidayFlag,
				queryPlatforms.split("=")[1]);
	}

	public List<Map<String, Object>> queryStationFlow(String beginDate,
			String endDate, String queryYPlatform, String queryTPlatform,
			String holidayFlag) {
		return stationPsgflowMapper.queryStationsPsgFlow(beginDate, endDate,
				queryYPlatform, queryTPlatform, holidayFlag);
	}

	public List<Map<String, Object>> queryPsgFlowForDays(String beginDate,
			String endDate, String beginDate2, String endDate2,
			String beginTime, String endTime, String queryPlatforms,
			String holidayFlag) {
		return stationPsgflowMapper.queryPsgFlowForDays(beginDate, endDate,
				beginDate2, endDate2, queryPlatforms.split("=")[0], beginTime,
				endTime, holidayFlag, queryPlatforms.split("=")[1]);
	}

	public List<Map<String, Object>> queryPsgFlowForDays(String beginDate,
			String endDate, String beginDate2, String endDate2,
			String beginTime, String endTime, String queryYPlatform,
			String queryTPlatform, String holidayFlag) {
		return stationPsgflowMapper.queryStationsPsgFlowForDays(beginDate,
				endDate, beginDate2, endDate2, beginTime, endTime,
				queryYPlatform, queryTPlatform, holidayFlag);
	}

	public List<Map<String, Object>> queryPsgFlowForWeeks(String beginDate,
			String endDate, String beginDate2, String endDate2,
			String queryPlatforms, String holidayFlag, String beginTime,
			String endTime) {

		return stationPsgflowMapper.queryPsgFlowForWeeks(beginDate, endDate,
				beginDate2, endDate2, queryPlatforms.split("=")[0],
				holidayFlag, beginTime, endTime, queryPlatforms.split("=")[1]);

	}

	public List<Map<String, Object>> queryPsgFlowForWeeks(String beginDate,
			String endDate, String beginDate2, String endDate2,
			String queryYPlatform, String queryTPlatform, String holidayFlag,
			String beginTime, String endTime) {
		return stationPsgflowMapper.queryStationsPsgFlowForWeeks(beginDate,
				endDate, beginDate2, endDate2, queryYPlatform, queryTPlatform,
				holidayFlag, beginTime, endTime);
	}

	public List<Map<String, Object>> queryPsgFlowForMonths(String beginDate,
			String endDate, String beginDate2, String endDate2,
			String queryPlatforms, String holidayFlag, String beginTime,
			String endTime) {
		return stationPsgflowMapper.queryPsgFlowForMonths(beginDate, endDate,
				beginDate2, endDate2, queryPlatforms.split("=")[0],
				holidayFlag, beginTime, endTime, queryPlatforms.split("=")[1]);
	}

	public List<Map<String, Object>> queryPsgFlowForMonths(String beginDate,
			String endDate, String beginDate2, String endDate2,
			String queryYPlatform, String queryTPlatform, String holidayFlag,
			String beginTime, String endTime) {
		return stationPsgflowMapper.queryStationsPsgFlowForMonths(beginDate,
				endDate, beginDate2, endDate2, queryYPlatform, queryTPlatform,
				holidayFlag, beginTime, endTime);
	}

}
