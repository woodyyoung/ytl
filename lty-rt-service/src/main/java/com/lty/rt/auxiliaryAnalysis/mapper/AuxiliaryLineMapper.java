package com.lty.rt.auxiliaryAnalysis.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lty.rt.auxiliaryAnalysis.entity.AuxiliaryLineScheme;
import com.lty.rt.auxiliaryAnalysis.entity.AuxiliaryLineStation;
import com.lty.rt.basicData.treeUtil.TreeNode;

@Repository
public interface AuxiliaryLineMapper {
	
	/**
	 * 查询线路节点数据（包括辅助线网方案数据）
	 * @return TreeNode
	 */
	List<TreeNode> queryLineTreeNode();

	/**
	 * 插入线路方案
	 * @param scheme
	 * @return
	 */
	int insertScheme(AuxiliaryLineScheme scheme);

	/**
	 * 修改线路方案
	 * @param scheme
	 * @return
	 */
	int updateScheme(AuxiliaryLineScheme scheme);

	/**
	 * 通过线路ID删除线路站点
	 * @param lineId
	 * @return
	 */
	int delLineStationByLineId(String lineId);

	/**
	 * 批量保存线路站点
	 * @param lineStationList
	 * @return
	 */
	int batchSaveLineStation(List<AuxiliaryLineStation> list);

	/**
	 * 删除线路方案
	 * @param id
	 */
	int delLineScheme(String id);

	/**
	 * 根据方案ID删除线路站点
	 * @param schemeId
	 * @return
	 */
	int delLineStationBySchemeId(String schemeId);
	
	

	/**
	 * 查询方案
	 * @param id
	 * @return
	 */
	AuxiliaryLineScheme querySchemeById(String id);
	
	
	/**
	 * 查询线路满载率 (包括高峰满足率、评价满载率)
	 * @param lineId
	 * @param date
	 * @return
	 */
	Map<String,Object> queryFillRates(Map<String, Object> map);
	
	/**
	 * 查询方案满载率(包括高峰满足率、评价满载率)
	 * @param stationIds
	 * @param date
	 * @return
	 */
	Map<String,Object> queryFillRatesForScheme(Map<String, Object> map);
	
	
	/**
	 * 查询线路日客流量
	 * @param lineId
	 * @param date
	 * @return
	 */
	Map<String,Object> queryTotalPsgFlow(Map<String, Object> map);
	
	/**
	 * 查询方案日客流量
	 * @param stationIds
	 * @param date
	 * @return
	 */
	Map<String,Object> queryTotalPsgFlowForScheme(Map<String, Object> map);
	
	/**
	 * 查询方案日流数据(每小时客流量)
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> queryPsgFlowForScheme (Map<String, Object> map);
	
	
	/**
	 * 查询方案站点客流数据
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> queryStationPsgFlowForScheme(Map<String, Object> map);
	
	/**
	 * 查询线路发车时间
	 * @param map
	 * @return
	 */
	Map<String,Object> queryLineStartAndEndPlanRunime(Map<String, Object> map);
	
	
	/**
	 * 查询线路配车数量
	 * @param map
	 * @return
	 */
	Map<String,Object> queryLineBusNum(Map<String, Object> map);
	
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> queryPlatFormPsgFlow(Map<String, Object> map);



}
