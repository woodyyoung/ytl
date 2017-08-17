package com.lty.rt.assessment.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lty.rt.assessment.bean.Index;
import com.lty.rt.assessment.bean.IndexLevel;
import com.lty.rt.assessment.bean.Level;
import com.lty.rt.assessment.mapper.IndexLevelMapper;
import com.lty.rt.assessment.mapper.IndexMapper;
import com.lty.rt.assessment.mapper.LevelMapper;
import com.lty.rt.assessment.vo.IndexSourceDataVo;
import com.lty.rt.assessment.vo.IndexTotalDataVo;
import com.lty.rt.basicData.treeUtil.TreeNode;
import com.lty.rt.comm.bean.QueryCommonDto;
import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.util.UUIDUtils;

@Service("indexService")
public class IndexService {
	
	@Autowired
	private IndexMapper indexMapper;
	
	@Autowired
	private IndexLevelMapper indexLevelMapper;
	
	@Autowired
	private LevelMapper levelMapper;
	
	public List<Index> getLists(Integer indexType){
		return indexMapper.getLists(indexType);
	}
	
	public List<Index> getListByMap(Map<String, Object> map){
		return indexMapper.getListByMap(map);
	}
	
	public List<IndexLevel> getIndexLevelListByMap(Map<String, Object> map){
		return indexLevelMapper.getListByMap(map);
	}
	
	@Transactional
	public Integer saveOrUpdate(Index index){
		if(index == null){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":index");
		}
		Integer count = 0;
		
		if(StringUtils.isBlank(index.getPkid())){//add
			count = add(index);
		}else{//edit
			count = edit(index);
		}
		return count;
	}
	
	private Integer add(Index index){
		String pId = index.getParentid();
		String idStr = "";
		Integer count = 0;
		
		if(StringUtils.isBlank(index.getParentid())){//一级菜单
			index.setParentid("-1");
			idStr = indexMapper.getMaxId("-1");
			if(StringUtils.isBlank(idStr)){
				idStr = "000";
			}
		}else{
			idStr = indexMapper.getMaxId(pId);
			if(StringUtils.isBlank(idStr)){
				idStr = pId + "000";
			}
			index.setParentid(pId);
		}
		
		index.setPkid(UUIDUtils.getUUID().toString());
		index.setDescriptions(index.getDescriptions().trim());
		
		BigInteger areaCodeId = new BigInteger(idStr);
		String id = String.format("%0"+idStr.length()+"d", areaCodeId.add(new BigInteger("1")));
		index.setId(id); //生成自已的id
		count = indexMapper.insert(index);
		
		List<IndexLevel> list = index.getIndexLevelList();
		if(list!= null && list.size() > 0){
			for(IndexLevel indexLevel : list){
				indexLevel.setIndexId(id);
			}
			indexLevelMapper.batchInsert(list);
		}
		return count;
	}
	
	private Integer edit(Index index){
		if(StringUtils.isBlank(index.getPkid())){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":pkid");
		}
		Integer count = 0;
		Index oldIndex = indexMapper.selectByPrimaryKey(index.getPkid());
		if(oldIndex.getParentid().equals(index.getParentid())){//未修改父菜单
			count = indexMapper.updateByPrimaryKeySelective(index);
			List<IndexLevel> list = index.getIndexLevelList();
			String id = index.getId();
			if(list!= null && list.size() > 0){
				String str[] = {id};
				for(IndexLevel indexLevel : list){
					indexLevel.setIndexId(id);
					//indexLevelMapper.update(indexLevel);
				}
				indexLevelMapper.delByIndexId(str);
				indexLevelMapper.batchInsert(list);
			}
		}else{
			String idStr = indexMapper.getMaxId(index.getParentid());//查询新父菜单下的最大的菜单
			if(StringUtils.isBlank(idStr)){
				idStr = index.getParentid() + "000";
			}
			
			BigInteger areaCodeId = new BigInteger(idStr);
			String id = String.format("%0"+idStr.length()+"d", areaCodeId.add(new BigInteger("1")));
			index.setId(id); //生成新的id
			
			count = indexMapper.updateByPrimaryKeySelective(index);
			List<IndexLevel> list = index.getIndexLevelList();
			if(list!= null && list.size() > 0){
				for(IndexLevel indexLevel : list){
					indexLevel.setIndexId(id);
				}
				indexLevelMapper.batchInsert(list);
				indexLevelMapper.delByIndexId(new String[]{oldIndex.getId()});
			}
		}
		return count;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Index initLevel(){
		Index index = new Index();
		List<Level> list = levelMapper.findListByMap(new HashMap());
		
		if(list != null && list.size() > 0){
			List<IndexLevel> indexLevelList = new ArrayList<IndexLevel>();
			for(Level level : list){
				IndexLevel indexLevel = new IndexLevel();
				indexLevel.setIndexLevel(level.getLevels());
				indexLevel.setDescription(level.getDescription());
				indexLevelList.add(indexLevel);
			}
			index.setIndexLevelList(indexLevelList);
		}
		
		return index;
	}

	public List<TreeNode> getIndexTree(Map<String,Object> map){
		return indexMapper.getIndexTree(map);
	}
	
	public Integer delByIds(String ids){
		if(StringUtils.isBlank(ids)){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":ids");
		}
		String indexId = ids.split(",")[0];
		Integer size = indexMapper.countIndexSourceData(indexId);
		if(size!=null&&size>0){
			throw new ApplicationException(ReturnCode.ERROR_01.getCode() ,"此指标已有指标数据录入，不能删除！");
		}
		
		Integer count = indexMapper.delByIds(ids.split(","));
		indexLevelMapper.delByIndexId(ids.split(","));
		return count;
	}
	
	/**
	 * 处理修改评价等级设置
	 * @param id
	 * @return
	 */
	public Index getIndexById(String id){
		if(StringUtils.isBlank(id)){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":id");
		}
		Index index = indexMapper.getIndexByID(id);
		if(index != null){
			List<IndexLevel> list = index.getIndexLevelList();
			if(list==null||list.isEmpty()){
				//查询所有指标
				List<Level> levels = levelMapper.findListByMap(new HashMap<String, Object>());
				list  =new ArrayList<IndexLevel>();
				for(Level level : levels){
					IndexLevel indexLevel = new IndexLevel();
					indexLevel.setIndexId(index.getId());
					indexLevel.setIndexLevel(level.getLevels());
					indexLevel.setDescription(level.getDescription());
					list.add(indexLevel);
				}
				index.setIndexLevelList(list);
				return index;
			}
			

			//查询所有指标
			List<Level> levels = levelMapper.findListByMap(new HashMap<String, Object>());
			if(levels != null && levels.size() > 0 && levels.size() > list.size()){
				for(Level level : levels){
					Boolean flag = true;
					for(IndexLevel il : list){
						if(level.getLevels().equals(il.getIndexLevel())){
							flag = false;
						}
					}
					if(flag){//没找到
						IndexLevel indexLevel = new IndexLevel();
						indexLevel.setIndexId(list.get(0).getIndexId());
						indexLevel.setIndexLevel(level.getLevels());
						indexLevel.setDescription(level.getDescription());
						list.add(indexLevel);
					}
				}
			}
			
		}
		
		return index;
	}
	
	@Transactional
	public Map<String, List<IndexTotalDataVo>> getTotalChartData(QueryCommonDto dto){
		if(dto == null){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":dto");
		}
		if(StringUtils.isBlank(dto.getArg1())){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":arg1");
		}
		if(StringUtils.isBlank(dto.getArg())){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":arg");
		}
		
		Map<String, List<IndexTotalDataVo>> resultMap = getTotalChartDataByXXs(dto, dto.getArg1());
		return resultMap;
	}
	
	@Transactional
	public Map<String, List<IndexTotalDataVo>> getDetailChartData(QueryCommonDto dto){
		if(dto == null){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":dto");
		}
		if(StringUtils.isBlank(dto.getArg1())){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":arg1");
		}
		if(StringUtils.isBlank(dto.getArg())){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":arg");
		}
		
		Map<String, List<IndexTotalDataVo>> resultMap = getDetailChartDataByXXs(dto, dto.getArg1());
		return resultMap;
	}
	
	
	private Map<String, List<IndexTotalDataVo>> getTotalChartDataByXXs(QueryCommonDto dto, String flag){
		Map<String, List<IndexTotalDataVo>> resultMap = new HashMap<String, List<IndexTotalDataVo>>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(StringUtils.isNotBlank(dto.getBeginDate()) && StringUtils.isNotBlank(dto.getEndDate())){
			map.put("arg", dto.getArg());
			map.put("beginDate", dto.getBeginDate());
			map.put("endDate", dto.getEndDate());
			map.put("isWork", dto.getArg2());
			
			List<IndexTotalDataVo> list = new ArrayList<IndexTotalDataVo>();
			if("days".equals(dto.getArg1())){
				list = indexMapper.getTotalChartDataByDays(map);
			}else if("weeks".equals(dto.getArg1())){
				list = indexMapper.getTotalChartDataByWeeks(map);
			}else if("months".equals(dto.getArg1())){
				list = indexMapper.getTotalChartDataByMonths(map);
			}else if("years".equals(dto.getArg1())){
				list = indexMapper.getTotalChartDataByYears(map);
			}
			resultMap.put("1", list);
			
		}
		
		if(StringUtils.isNotBlank(dto.getBeginDate2()) && StringUtils.isNotBlank(dto.getEndDate2())){
			map.clear();
			map.put("arg", dto.getArg());
			map.put("beginDate", dto.getBeginDate2());
			map.put("endDate", dto.getEndDate2());
			List<IndexTotalDataVo> list = new ArrayList<IndexTotalDataVo>();

			if("days".equals(dto.getArg1())){
				list = indexMapper.getTotalChartDataByDays(map);
			}else if("weeks".equals(dto.getArg1())){
				list = indexMapper.getTotalChartDataByWeeks(map);
			}else if("months".equals(dto.getArg1())){
				list = indexMapper.getTotalChartDataByMonths(map);
			}else if("years".equals(dto.getArg1())){
				list = indexMapper.getTotalChartDataByYears(map);
			}
			resultMap.put("2", list);
		}
		return resultMap;
	}
	
	private Map<String, List<IndexTotalDataVo>> getDetailChartDataByXXs(QueryCommonDto dto, String flag){
		Map<String, List<IndexTotalDataVo>> resultMap = new HashMap<String, List<IndexTotalDataVo>>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(StringUtils.isNotBlank(dto.getBeginDate()) && StringUtils.isNotBlank(dto.getEndDate())){
			map.put("arg", dto.getArg());
			map.put("beginDate", dto.getBeginDate());
			map.put("endDate", dto.getEndDate());
			if(StringUtils.isNotBlank(dto.getArg2())){
				map.put("isWork", dto.getArg2());
			}
			
			List<IndexTotalDataVo> list = new ArrayList<IndexTotalDataVo>();
			if("days".equals(dto.getArg1())){
				list = indexMapper.getDetailChartDataByDays(map);
			}else if("weeks".equals(dto.getArg1())){
				list = indexMapper.getDetailChartDataByWeeks(map);
			}else if("months".equals(dto.getArg1())){
				list = indexMapper.getDetailChartDataByMonths(map);
			}else if("years".equals(dto.getArg1())){
				list = indexMapper.getDetailChartDataByYears(map);
			}
			resultMap.put("1", list);
			
		}
		
		if(StringUtils.isNotBlank(dto.getBeginDate2()) && StringUtils.isNotBlank(dto.getEndDate2())){
			map.clear();
			map.put("arg", dto.getArg());
			map.put("beginDate", dto.getBeginDate2());
			map.put("endDate", dto.getEndDate2());
			if(StringUtils.isNotBlank(dto.getArg2())){
				map.put("isWork", dto.getArg2());
			}
			
			List<IndexTotalDataVo> list = new ArrayList<IndexTotalDataVo>();

			if("days".equals(dto.getArg1())){
				list = indexMapper.getDetailChartDataByDays(map);
			}else if("weeks".equals(dto.getArg1())){
				list = indexMapper.getDetailChartDataByWeeks(map);
			}else if("months".equals(dto.getArg1())){
				list = indexMapper.getDetailChartDataByMonths(map);
			}else if("years".equals(dto.getArg1())){
				list = indexMapper.getDetailChartDataByYears(map);
			}
			resultMap.put("2", list);
		}
		return resultMap;
	}
	
	public Map<String, Object> getTotalAvgData(QueryCommonDto dto, Map<String, List<IndexTotalDataVo>> resultMap){
		Map<String, Object> map = new HashMap<String, Object>();
		Index index = indexMapper.getIndexByID(dto.getArg());
		
		BigDecimal targetLevel = null;
		BigDecimal curAvg = null;
		BigDecimal compareAvg = null;
		if(index != null){
			if(StringUtils.isNotBlank(index.getTargetlevel())){
				Boolean flag = isNum(index.getTargetlevel());
				if(!flag){
					throw new ApplicationException(ReturnCode.ERROR_02.getCode() ,ReturnCode.ERROR_02.getMsg() + ":targetLevel");
				}
				targetLevel = new BigDecimal(index.getTargetlevel());
			}else{
				throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":targetLevel");
			}
			
			map.put("unit", index.getLevelUnit());
			map.put("descriptions", index.getDescriptions());
			map.put("targetLevel", targetLevel);
			map.put("charName", index.getName());
		}
		
		if(resultMap != null && !resultMap.isEmpty()){
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("indexId", dto.getArg());
			List<IndexLevel> list = indexLevelMapper.getListByMap(param);
			
			BigDecimal oneHundred = new BigDecimal("100");
			curAvg = calcAvg(resultMap.get("1"));
			//目标差异率
			if(curAvg != null){
				if(list != null && list.size() > 0){
					for(int i=0;i<list.size();i++){
						IndexLevel  li= list.get(i);
						if(curAvg.intValue() == Integer.parseInt(li.getIndexLevel())){
							map.put("curAvg", curAvg);
							map.put("levelDescription", li.getDescription());
							map.put("proposal", li.getProposal());
							break;
						}
						if(curAvg.intValue()<1){
							li= list.get(0);
							map.put("curAvg", curAvg);
							map.put("levelDescription", li.getDescription());
							map.put("proposal", li.getProposal());
							break;
						}
						/*if(curAvg.compareTo(il.getLowerLimit()) >= 0 && curAvg.compareTo(il.getTopLimit()) < 0){
							curAvg = new BigDecimal(il.getIndexLevel());
							map.put("curAvg", il.getIndexLevel());
							map.put("levelDescription", il.getDescription());
							map.put("proposal", il.getProposal());
							break;
						}*/
					}
				}
				double targetDiff = 0;
				if(curAvg.compareTo(new BigDecimal("0")) != 0){
					targetDiff = Math.abs(targetLevel.doubleValue() - curAvg.doubleValue());
					targetDiff = Math.round(targetDiff/targetLevel.doubleValue()*100);
					//Math.abs(targetLevel.doubleValue() - curAvg.doubleValue())
					//targetDiff = Math.abs((((curAvg.subtract(targetLevel)).multiply(oneHundred).divide(curAvg, 2, BigDecimal.ROUND_HALF_UP))).intValue());
				}
				map.put("targetDiff", targetDiff);
			}
			
			//差异率
			if(resultMap.size() > 1){
				compareAvg = calcAvg(resultMap.get("2"));
				/*if(compareAvg != null&&compareAvg.intValue()>0){
					if(list != null && list.size() > 0){
						for(IndexLevel il : list){
							if(compareAvg.compareTo(il.getLowerLimit()) >= 0 && compareAvg.compareTo(il.getTopLimit()) < 0){
								compareAvg = new BigDecimal(il.getIndexLevel());
							}
						}
					}
					int compareDiff = Math.abs((((compareAvg.subtract(targetLevel)).multiply(oneHundred).divide(curAvg, 2, BigDecimal.ROUND_HALF_UP))).intValue());
					
					map.put("compareDiff", compareDiff);
					map.put("compareAvg", compareAvg);
				}*/
				double compareDiff = 0;
				if(curAvg.compareTo(new BigDecimal("0")) != 0){
					compareDiff = Math.abs(compareAvg.doubleValue() - curAvg.doubleValue());
					if(compareDiff!=0){
						compareDiff =  Math.round(compareDiff/compareAvg.doubleValue()*100);
					}
					//compareDiff = Math.round(compareDiff*10)/10;
					//Math.abs(targetLevel.doubleValue() - curAvg.doubleValue())
					//targetDiff = Math.abs((((curAvg.subtract(targetLevel)).multiply(oneHundred).divide(curAvg, 2, BigDecimal.ROUND_HALF_UP))).intValue());
				}
				map.put("compareAvg", compareAvg);
				map.put("compareDiff", compareDiff);
			}
			//差值：分析时段与目标的差值，取绝对值
			double diff = (double)Math.round(Math.abs((curAvg.doubleValue()-targetLevel.doubleValue()))*100)/100;
			map.put("diff", diff);
		}
		return map;
	}
	
	/*public static void main(String[] args) {
		BigDecimal compareAvg = new BigDecimal(3); 
		BigDecimal curAvg = new BigDecimal(2.7); 
		BigDecimal targetLevel = new BigDecimal(3); 
		double 	compareDiff = Math.abs(compareAvg.doubleValue() - curAvg.doubleValue());
		if(compareDiff!=0){
			compareDiff =  Math.round(compareDiff/compareAvg.doubleValue()*100);
		}
		double a = (double)Math.round(Math.abs((curAvg.doubleValue()-targetLevel.doubleValue()))*10)/10;
	
		System.out.println(a);
	}*/
	
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
	
	private BigDecimal calcAvg(List<IndexTotalDataVo> list){
		if(list==null||list.size()<1){
			return new BigDecimal(0);
		}
		BigDecimal total = new BigDecimal("0");
		Integer totalCount = list.size();
		if(list != null && list.size() > 0){
			for(IndexTotalDataVo itdv : list){
				total = total.add(itdv.getActualLevel());
			}
		}
		return total.divide(new BigDecimal(totalCount), 2, BigDecimal.ROUND_HALF_UP);
	}
}
