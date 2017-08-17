package com.lty.rt.auxiliaryAnalysis.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.auxiliaryAnalysis.mapper.AuxiliaryLineMapper;
import com.lty.rt.auxiliaryAnalysis.service.AuxiliaryLineService;
import com.lty.rt.basicData.bean.Station;
import com.lty.rt.basicData.service.StationService;
import com.lty.rt.comm.bean.RTResponse;

/**
 * 辅助线网
 * 
 * @author yyw
 *
 */
@RequestMapping("/auxiliaryLine")
@Controller
public class AuxiliaryLineController {

	@Autowired
	private AuxiliaryLineService auxiliaryLineService;
	
	@Autowired
	private AuxiliaryLineMapper auxiliaryLineMapper;
	
	@Autowired
	private StationService stationService;
	
	/**
	 * 加载线路树
	 */
	@RequestMapping("/loadLineTree")
	@ResponseBody
	public RTResponse loadLineTree() {
		RTResponse res = new RTResponse();
		Map<String, Object> data = auxiliaryLineService.loadLineTree();
		res.setData(data);
		return res;
	}
	
	/**
	 * 查询日期里时刻的客流
	 * @param params
	 * @return
	 */
	@RequestMapping("/getTimeFlow")
	@ResponseBody
	public RTResponse getTimeFlow(@RequestBody Map<String, Object> params){
		RTResponse res = new RTResponse();
		Map<String, Object> data = auxiliaryLineService.getTimeFlow(params);
		res.setData(data);
		return res;
	}
	
	
	@RequestMapping("/getTimeFlowForScheme")
	@ResponseBody
	public RTResponse getTimeFlowForScheme(@RequestBody Map<String, Object> params){
		String schemeId = (String)params.get("schemeId");
		List<Station> stations = stationService.queryStationBySchemeId(schemeId);
		params.put("stationIds", stations);
		RTResponse res = new RTResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> linedata = auxiliaryLineMapper.queryPsgFlowForScheme(params);
		data.put("lineData", linedata);
		res.setData(data);
		return res;
	}
	
	/**
	 * 查询日期里时刻的客流
	 * @param params
	 * @return
	 */
	@RequestMapping("/getStationLineFlow")
	@ResponseBody
	public RTResponse getStationLineFlow(@RequestBody Map<String, Object> params){
		RTResponse res = new RTResponse();
		Map<String, Object> data = auxiliaryLineService.getStationLineFlow(params);
		res.setData(data);
		return res;
	}
	

	/**
	 * 查询日期里时刻的客流
	 * @param params
	 * @return
	 */
	@RequestMapping("/getStationLineFlowForScheme")
	@ResponseBody
	public RTResponse getStationLineFlowForScheme(@RequestBody Map<String, Object> params){
		RTResponse res = new RTResponse();
		String schemeId = (String)params.get("schemeId");
		List<Station> stations = stationService.queryStationBySchemeId(schemeId);
		params.put("stationIds", stations);
		List<Map<String, Object>> stationData = auxiliaryLineMapper.queryStationPsgFlowForScheme(params);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("stationData", stationData);
		res.setData(data);
		return res;
	}
	
	/**
	 * 查询线路站点位置数据
	 * @param params
	 * @return
	 */
	@RequestMapping("/queryLineStationData")
	@ResponseBody
	public RTResponse queryLineStationData(@RequestBody Map<String, Object> params){
		RTResponse res = new RTResponse();
		Map<String, Object> data = auxiliaryLineService.queryLineStationData(params);
		res.setData(data);
		return res;
	}
	
	/**
	 * 查询站点位置数据
	 * @param params
	 * @return
	 */
	@RequestMapping("/queryStationData")
	@ResponseBody
	public RTResponse queryStationData(@RequestBody Map<String, Object> params){
		RTResponse res = new RTResponse();
		Map<String, Object> data = auxiliaryLineService.queryStationData(params);
		res.setData(data);
		return res;
	}
	
	/**
	 * 查询所有站台
	 * @param params
	 * @return
	 */
	@RequestMapping("/queryAllPlatform")
	@ResponseBody
	public RTResponse queryAllPlatform(){
		RTResponse res = new RTResponse();
		Map<String, Object> data = auxiliaryLineService.queryAllPlatform();
		res.setData(data);
		return res;
	}
	
	/**
	 * 查询所有站台
	 * @param params
	 * @return
	 */
	@RequestMapping("/queryAllPlatformForAuxiliaryLine")
	@ResponseBody
	public RTResponse queryAllPlatformForAuxiliaryLine(@RequestBody Map<String, Object> params){
		RTResponse res = new RTResponse();
		Map<String, Object> data = auxiliaryLineService.queryAllPlatformForAuxiliaryLine(params);
		res.setData(data);
		return res;
	}
	
	/**
	 * 查询所有站点
	 * @param params
	 * @return
	 */
	@RequestMapping("/queryAllStation")
	@ResponseBody
	public RTResponse queryAllStation(){
		RTResponse res = new RTResponse();
		Map<String, Object> data = auxiliaryLineService.queryAllStations();
		res.setData(data);
		return res;
	}
	
	/**
	 * 保存方案
	 * @param params
	 * @return
	 */
	@RequestMapping("/saveLineScheme")
	@ResponseBody
	public RTResponse saveLineScheme(@RequestBody Map<String, Object> params){
		RTResponse res = new RTResponse();
		auxiliaryLineService.saveLineScheme(params);
		return res;
	}
	
	/**
	 * 删除方案
	 * @param params
	 * @return
	 */
	@RequestMapping("/delLineScheme")
	@ResponseBody
	public RTResponse delLineScheme(@RequestBody Map<String, Object> params){
		RTResponse res = new RTResponse();
		auxiliaryLineService.delLineScheme(params);
		return res;
	}
	
	/**
	 * 查询方案
	 * @param params
	 * @return
	 */
	@RequestMapping("/querySchemeData")
	@ResponseBody
	public RTResponse querySchemeData(@RequestBody Map<String, Object> params){
		RTResponse res = new RTResponse();
		Map<String, Object> data = auxiliaryLineService.querySchemeData(params);
		res.setData(data);
		return res;
	}
	
	/**
	 * 查询方案客流数据
	 * @param params
	 * @return
	 */
	@RequestMapping("/querySchemePsgFlowData")
	@ResponseBody
	public RTResponse querySchemePsgFlowData(@RequestBody Map<String, Object> params){
		String schemeId = (String)params.get("schemeId");
		List<Station> stations = stationService.queryStationBySchemeId(schemeId);
		params.put("stationIds", stations);
		RTResponse res = new RTResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> schemePsgFlowData = auxiliaryLineMapper.queryPsgFlowForScheme(params);
		data.put("schemePsgFlowData", schemePsgFlowData);//方案客流折线图数据
		List<Map<String, Object>> stationPsgFlowData = auxiliaryLineMapper.queryStationPsgFlowForScheme(params);
		data.put("stationPsgFlowData", stationPsgFlowData);//方案所有站点客流直方图数据
		data.put("fillRates", auxiliaryLineMapper.queryFillRatesForScheme(params));//满载率
		data.put("totalPsg",auxiliaryLineMapper.queryTotalPsgFlowForScheme(params));//日客流量
		data.put("stations",stations);//方案关联站点数据
		res.setData(data);
		return res;
	} 
	
	
	/**
	 * 查询线路指标客流数据
	 * @param params
	 * @return
	 */
	@RequestMapping("/queryLineIndexData")
	@ResponseBody
	public RTResponse queryLineIndexData(@RequestBody Map<String, Object> params){
		RTResponse res = new RTResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("fillRates", auxiliaryLineMapper.queryFillRates(params));//满载率
		data.put("totalPsg",auxiliaryLineMapper.queryTotalPsgFlow(params));//日客流量
		res.setData(data);
		return res;
	} 
	
}
