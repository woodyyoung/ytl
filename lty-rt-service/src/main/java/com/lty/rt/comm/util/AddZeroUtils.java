package com.lty.rt.comm.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 横坐标补零工具类
 * 
 * @author qiq
 *
 */
public class AddZeroUtils {

	/**
	 * 补全小时
	 * 
	 * @param linedata
	 * @param zeroMap需要补零的通用数据集
	 * @param zzbParaName
	 *            纵坐标名称
	 * @param paraName
	 *            分组字段名称
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Map<String, Object>> fillHours(List<Map<String, Object>> linedata, Map<String, Object> zeroMap,
			String zzbParaName, String paraName) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		// 将两天的数据分开，key为时间
		Map<String, List<Map<String, Object>>> tempMap = new HashMap<String, List<Map<String, Object>>>();
		Set<String> set = new HashSet<String>();
		Set set1 = new HashSet<String>();
		Set set2 = new HashSet<String>();
		if (linedata != null && linedata.size() > 0) {

			// 遍历得到一个Map，key为时间，value为所有的值
			for (Map<String, Object> map : linedata) {
				if (tempMap.isEmpty()) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					list.add(map);
					set1.add((String) map.get(zzbParaName));
					tempMap.put((String) map.get(paraName), list);
				} else {
					if (tempMap.containsKey((String) map.get(paraName))) {
						tempMap.get((String) map.get(paraName)).add(map);
					} else {
						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						list.add(map);
						set2.add((String) map.get(zzbParaName));
						tempMap.put((String) map.get(paraName), list);
					}
				}
				set.add((String) map.get(zzbParaName));
			}

			if (tempMap.size() > 1) {
				// 补0到linedata
				for (Map.Entry<String, List<Map<String, Object>>> entry : tempMap.entrySet()) {
					List<Map<String, Object>> list = entry.getValue();
					for (String str : set) {
						Boolean flag = false;
						for (Map<String, Object> map : list) {
							if (str.equals((String) map.get(zzbParaName))) {
								flag = true;
							}
						}
						if (!flag) {
							if (!zeroMap.isEmpty()) {
								Map<String, Object> map = new HashMap<String, Object>();
								map.putAll(zeroMap);
								map.put(paraName, entry.getKey());
								map.put(zzbParaName, str);
								tempMap.get(entry.getKey()).add(map);
							}
						}
					}
				}

				// 将tempMap的value排序
				for (Map.Entry<String, List<Map<String, Object>>> entry : tempMap.entrySet()) {
					resultList.addAll(compareSort(entry.getValue(), zzbParaName));
				}
			} else {
				resultList.addAll(linedata);
			}

		}

		return resultList;
	}

	private static List<Map<String, Object>> compareSort(List<Map<String, Object>> list, final String zzbParaName) {
		Collections.sort(list, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				String name1 = (String) o1.get(zzbParaName);// name1是从你list里面拿出来的一个
				String name2 = (String) o2.get(zzbParaName); // name1是从你list里面拿出来的第二个name
				return name1.compareTo(name2);
			}
		});
		return list;
	}

}
