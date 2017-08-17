package com.lty.rt.auxiliaryAnalysis.service;

import java.math.BigDecimal;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.lty.rt.auxiliaryAnalysis.entity.AuxiliaryLineScheme;
import com.lty.rt.auxiliaryAnalysis.entity.AuxiliaryLineStation;
import com.lty.rt.auxiliaryAnalysis.mapper.AuxiliaryLineMapper;
import com.lty.rt.basicData.bean.Line;
import com.lty.rt.basicData.bean.PlatForm;
import com.lty.rt.basicData.bean.Station;
import com.lty.rt.basicData.mapper.PlatFormMapper;
import com.lty.rt.basicData.mapper.StationMapper;
import com.lty.rt.basicData.service.LineService;
import com.lty.rt.basicData.service.StationService;
import com.lty.rt.basicData.treeUtil.TreeNode;
import com.lty.rt.basicData.treeUtil.TreeNodeUtils;
import com.lty.rt.basicData.treeUtil.TreeViewEntity;
import com.lty.rt.comm.util.UUIDUtils;
import com.lty.rt.passengerFlows.mapper.LinePsgflowAnalysisMapper;
import com.lty.rt.passengerFlows.service.LinePsgFlowAnalysisService;
import com.lty.rt.utils.DataTransfer;

/**
 * 辅助线网--业务处理类
 * 
 * @author  yyw
 *
 */
@Service
public class AuxiliaryLineService {

	@Autowired
	private AuxiliaryLineMapper auxiliaryLineMapper;
	
	@Autowired
	private LinePsgflowAnalysisMapper linePsgflowAnalysisMapper;
	
	@Autowired
	private LinePsgFlowAnalysisService linePsgFlowAnalysisService;
	
	@Autowired
	private LineService lineService;
	
	@Autowired
	private StationService stationService;
	
	@Autowired
	private PlatFormMapper platFormMapper;
	
	@Autowired
	private StationMapper stationMapper;
	
	/**
	 * 加载线路树
	 * @return 
	 */
	public Map<String, Object> loadLineTree() {
		Map<String, Object> data = new HashMap<String, Object>();
		
		//线路方案数据
		List<TreeNode> nodes = auxiliaryLineMapper.queryLineTreeNode();
		
		//生成TreeView数据
		List<TreeViewEntity> treeNodes = TreeNodeUtils.generateTreeViewEntity(nodes);
		
		//根节点
		TreeViewEntity rootNode = new TreeViewEntity();
		rootNode.setId("-1");
		rootNode.setText("所有线路");
		rootNode.setNodes(treeNodes);
		rootNode.setArg("root");
		
		List<TreeViewEntity> lineTree = new LinkedList<TreeViewEntity>();
		lineTree.add(rootNode);
		data.put("lineTree", JSONArray.toJSON(lineTree));
		return data;
	}

	/**
	 * 查询日期里时刻的客流
	 * @param params
	 * @return
	 */
	public Map<String, Object> getTimeFlow(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		String lineId = (String) params.get("lineId");
		String queryDate = (String) params.get("queryDate");
		params.put("aloneDay", queryDate);
		params.put("startTime", queryDate);
		
		if (StringUtils.isNotBlank(lineId) && StringUtils.isNotBlank(queryDate)) {
			List<Map<String, Object>> linedata = linePsgflowAnalysisMapper.queryPsgFlowForHourToHour(params);
			result.put("lineData", linedata);
		} else {
			result.put("lineData", null);
		}
		
		return result;
	}

	/**
	 * 查询日期里时刻的站台线路客流
	 * @param params
	 * @return
	 */
	public Map<String, Object> getStationLineFlow(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> stationData = linePsgFlowAnalysisService
				.queryStationPsgFlowByLineId(params);
		result.put("stationData", stationData);
		return result;
	}

	/**
	 * 查询线路站台位置数据
	 * @param params
	 * @return
	 */
	public Map<String, Object> queryLineStationData(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		//处理线路
		String lineId = (String) params.get("lineId");
		Line line = lineService.selectByPrimaryKey(lineId);
		String lpath = DataTransfer.clobToString((Clob) line.getLpath());
		result.put("lpath", lpath);
		
		//处理站点
		List<Station> stations = stationService.queryStationByLineId(lineId);
		result.put("stations", stations);
		
		//线路长度
		result.put("lineLength", line.getLength());
		Map<String, Object> busNum = auxiliaryLineMapper.queryLineBusNum(params);
		//车辆总数
		result.put("busNum", busNum.get("BUSNUM"));
		//线路方向
		result.put("direction", line.getDirection());
		//平均站距
		BigDecimal b = new BigDecimal(line.getLength()/stations.size());  
		result.put("stationSpan", b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue());
		//站点数量
		result.put("stationNum", stations.size());
		
		//线路始发时间、末班发车时间
		Map<String, Object> startAndEndPlanRunime = auxiliaryLineMapper.queryLineStartAndEndPlanRunime(params);
		result.put("startAndEndPlanRunime", startAndEndPlanRunime);
		
		return result;
	}

	/**
	 * 查询站点
	 * @param params
	 * @return
	 */
	public Map<String, Object> queryStationData(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		//处理站点
		String lineId = (String) params.get("lineId");
		List<Station> stations = stationService.queryStationByLineId(lineId);
		result.put("stations", stations);
		//处理线路
		Line line = lineService.selectByPrimaryKey(lineId);
		String lpath = DataTransfer.clobToString((Clob) line.getLpath());
		result.put("lpath", lpath);
		return result;
	}

	/**
	 * 查询所有站台
	 * @return
	 */
	public Map<String, Object> queryAllPlatform() {
		Map<String, Object> result = new HashMap<String, Object>();
		List<PlatForm> platforms = platFormMapper.listAllPlatform();
		//List<Map<String, Object>> list = auxiliaryLineMapper.queryPlatFormPsgFlow(params);
		result.put("platforms", platforms);
		return result;
	}
	/**
	 * 查询所有站台
	 * @return
	 */
	public Map<String, Object> queryAllPlatformForAuxiliaryLine(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = auxiliaryLineMapper.queryPlatFormPsgFlow(params);
		result.put("platforms", list);
		return result;
	}
	/**
	 * 查询所有站点信息
	 * @return
	 */
	public Map<String, Object> queryAllStations() {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Station> allStation = stationMapper.selectAllStation();
		result.put("stations", allStation);
		return result;
	}

	/**
	 * 保存线路方案
	 * @param params
	 */
	public void saveLineScheme(Map<String, Object> params) {
		String id = (String) params.get("id");
		String lineId = (String) params.get("lineId");
		
		AuxiliaryLineScheme scheme = new AuxiliaryLineScheme();
		scheme.setLineId(lineId);
		scheme.setName((String) params.get("schemeName"));
		scheme.setMapData((String) params.get("mapData"));
		
		@SuppressWarnings("unchecked")
		ArrayList<LinkedHashMap<String,String>> mapStations = (ArrayList<LinkedHashMap<String, String>>) params.get("mapStations");
		List<AuxiliaryLineStation> LineStationList = new LinkedList<AuxiliaryLineStation>();
		if(StringUtils.isBlank(id)){
			id = UUIDUtils.getUUID().toString();
			scheme.setId(id);
			auxiliaryLineMapper.insertScheme(scheme);
		}else{
			scheme.setId(id);
			auxiliaryLineMapper.updateScheme(scheme);
			auxiliaryLineMapper.delLineStationBySchemeId(id);
		}
		
		if(CollectionUtils.isNotEmpty(mapStations)){
			for(LinkedHashMap<String,String> mapStation : mapStations){
				AuxiliaryLineStation lineStation = new AuxiliaryLineStation();
				lineStation.setLineId(lineId);
				lineStation.setSchemeId(id);
				String platformId = mapStation.get("platformId");
				lineStation.setPlatformId(platformId);
				LineStationList.add(lineStation);
			}
		}
		
		if(CollectionUtils.isNotEmpty(LineStationList)){
			auxiliaryLineMapper.batchSaveLineStation(LineStationList);
		}
	}

	/**
	 * 删除线路方案
	 * @param params
	 */
	public void delLineScheme(Map<String, Object> params) {
		String id = (String) params.get("schemeId");
		auxiliaryLineMapper.delLineScheme(id);
		auxiliaryLineMapper.delLineStationBySchemeId(id);
	}

	/**
	 * 查询方案
	 * @param params
	 */
	public Map<String, Object> querySchemeData(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		String id = (String) params.get("id");
		AuxiliaryLineScheme scheme = auxiliaryLineMapper.querySchemeById(id);
		result.put("schemeId", scheme.getId());
		result.put("lineId", scheme.getLineId());
		result.put("schemeName", scheme.getName());
		result.put("mapData", DataTransfer.clobToString((Clob) scheme.getMapData()));
		List<Station> stations = stationService.queryStationBySchemeId(scheme.getId());
		//List<Station> stations = stationService.queryStationByLineId(scheme.getLineId());
		result.put("stations", stations);
		return result;
	}
	
	
	
}
