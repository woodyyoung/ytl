package com.lty.rt.comm.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class GpsUtils {
	private static final double PI = 3.14159265;
	private static final double EARTH_RADIUS = 6378137;
	private static final double RAD = Math.PI / 180.0;

	/// <summary>
	/// 根据提供的经度和纬度、以及半径，取得此半径内的最大最小经纬度
	/// </summary>
	/// <param name="lat">纬度</param>
	/// <param name="lon">经度</param>
	/// <param name="raidus">半径(米)</param>
	/// <returns></returns>
	public static double[] GetAround(double lat, double lon, int raidus) {

		Double latitude = lat;
		Double longitude = lon;

		Double degree = (24901 * 1609) / 360.0;
		double raidusMile = raidus;

		Double dpmLat = 1 / degree;
		Double radiusLat = dpmLat * raidusMile;
		Double minLat = latitude - radiusLat;
		Double maxLat = latitude + radiusLat;

		Double mpdLng = degree * Math.cos(latitude * (PI / 180));
		Double dpmLng = 1 / mpdLng;
		Double radiusLng = dpmLng * raidusMile;
		Double minLng = longitude - radiusLng;
		Double maxLng = longitude + radiusLng;
		return new double[] { minLat, minLng, maxLat, maxLng };
	}

	/// <summary>
	/// 根据提供的两个经纬度计算距离(米)
	/// </summary>
	/// <param name="lng1">经度1</param>
	/// <param name="lat1">纬度1</param>
	/// <param name="lng2">经度2</param>
	/// <param name="lat2">纬度2</param>
	/// <returns></returns>
	public static double GetDistance(double lng1, double lat1, double lng2, double lat2) {
		double radLat1 = lat1 * RAD;
		double radLat2 = lat2 * RAD;
		double a = radLat1 - radLat2;
		double b = (lng1 - lng2) * RAD;
		double s = 2 * Math.asin(Math.sqrt(
				Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	/// <summary>
	/// 根据提供的两个经纬度计算中间坐标
	/// </summary>
	/// <param name="lng1">经度1</param>
	/// <param name="lat1">纬度1</param>
	/// <param name="lng2">经度2</param>
	/// <param name="lat2">纬度2</param>
	/// <returns></returns>
	public static double[] GetMiddle(double lng1, double lat1, double lng2, double lat2) {
		double middleLat = (lat1 + lat2) / 2;
		double middleLng = (lng1 + lng2) / 2;
		return new double[] { middleLng, middleLat };
	}

	// 线路坐标纠偏
	public static List<List<String>> orderGpsByDistance(String lPath, Map<String, String> zbMap) {
		List<List<String>> lineCoordinates = new ArrayList<List<String>>();
		try {
			String[] ll = lPath.split("\\|");
			if (ll.length > 1) {
				ArrayList<String> zbList = new ArrayList<String>(Arrays.asList(ll));
				for (int i = 0; i < zbList.size(); i++) {
					String coordinates = zbList.get(i);
					String[] coordinate = coordinates.split(",");
					// double lat = Double.parseDouble(coordinate[0]);
					// double lng = Double.parseDouble(coordinate[1]);
					// double[] latlon = GpsCorrect.gcjDecrypt(lat, lng);
					List<String> list = new ArrayList<String>();
					list.add(StringUtils.isBlank(coordinate[0]) ? "" : coordinate[0]);
					list.add(StringUtils.isBlank(coordinate[1]) ? "" : coordinate[1]);
					String odCountAndColor = zbMap.get(coordinates);
					if (StringUtils.isNotBlank(odCountAndColor)) {
						list.add(odCountAndColor);// odCount
						// list.add(odCountAndColor.split(",")[1]);// Color
						list.add("1");
					} else {
						list.add("0");
						list.add("0");
					}
					lineCoordinates.add(list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lineCoordinates;
	}

	/*
	 * 1、在辅助线坐标中查找与站点坐标最短的点 然后放进去 2、往辅助点坐标中设置进客流量
	 */
	public static Map<String, Object> findStationsAndCorrectLpath(String lPath, List<Map<String, Object>> stationList) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<List<String>> stationCoordinates = new ArrayList<List<String>>();
		Map<String, String> zbMap = new HashMap<String, String>();
		try {
			// --start-- 在辅助线坐标中查找与站点坐标最短的点 然后放进去
			String[] ll = lPath.split("\\|");
			if (ll.length > 1) {
				ArrayList<String> zbList = new ArrayList<String>(Arrays.asList(ll));
				if (CollectionUtils.isNotEmpty(stationList)) {
					for (Map<String, Object> station : stationList) {
						try {
							double minDistance = 200;// 最小距离
							double lats = 0;
							double lngs = 0;
							String onbuscounts = "";
							String stationName = (String) station.get("NAME");
							double latitude = ((BigDecimal) station.get("LATITUDE")).doubleValue();
							double longitude = ((BigDecimal) station.get("LONGITUDE")).doubleValue();
							String onbuscount = station.get("TOTAL_PERSON_COUNT") == null ? "0"
									: station.get("TOTAL_PERSON_COUNT").toString();
							// String color = station.get("COLOR") == null ?
							// "#00b7ff" : station.get("COLOR").toString();
							for (int i = 0; i < zbList.size(); i++) {
								String[] coordinate = zbList.get(i).split(",");
								double lat = Double.parseDouble(coordinate[0]);
								double lng = Double.parseDouble(coordinate[1]);
								double realDistance = GetDistance(longitude, latitude, lng, lat);// 获取两个坐标距离
								if (realDistance < minDistance) {
									lats = lat;
									lngs = lng;
									onbuscounts = onbuscount;
									minDistance = realDistance;
								}
							}
							if (minDistance < 200) {// 最小误差距离
								String platFormId = (String) station.get("PLATFORM_ID");
								List<String> list = new ArrayList<String>();
								// double[] latlon = GpsCorrect.gcjDecrypt(lats,
								// lngs);// 坐标纠偏
								list.add(String.valueOf(lats));
								list.add(String.valueOf(lngs));
								list.add(onbuscounts);
								list.add(stationName);
								list.add(platFormId);
								// zbMap.put(String.valueOf(lats) + "," +
								// String.valueOf(lngs), onbuscounts + "," +
								// color);
								zbMap.put(String.valueOf(lats) + "," + String.valueOf(lngs), onbuscounts);
								stationCoordinates.add(list);
							}

						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
					}
				}
			}
			// --end-- 在辅助线坐标中查找与站点坐标最短的点 然后放进去
			result.put("stationData", stationCoordinates);
			List<List<String>> newPath = orderGpsByDistance(lPath, zbMap);// 纠偏辅助点
			result.put("linePath", newPath);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	public static List<List<String>> findPlatsAndCorrectLpath(String lPath, List<Map<String, Object>> stationList) {
		List<List<String>> newPath = new ArrayList<List<String>>();
		Map<String, String> zbMap = new HashMap<String, String>();
		try {
			// --start-- 在辅助线坐标中查找与站点坐标最短的点 然后放进去
			String[] ll = lPath.split("\\|");
			if (ll.length > 1) {
				ArrayList<String> zbList = new ArrayList<String>(Arrays.asList(ll));
				if (CollectionUtils.isNotEmpty(stationList)) {
					for (Map<String, Object> station : stationList) {
						try {
							double minDistance = 200;// 最小距离
							double lats = 0;
							double lngs = 0;
							String onbuscounts = "1";
							double latitude = ((BigDecimal) station.get("LATITUDE")).doubleValue();
							double longitude = ((BigDecimal) station.get("LONGITUDE")).doubleValue();
							int index = 0;
							double[] middle = {};
							for (int i = 1; i < zbList.size() - 1; i++) {
								String[] coordinateLast = zbList.get(i - 1).split(",");
								double latLast = Double.parseDouble(coordinateLast[0]);
								double lngLast = Double.parseDouble(coordinateLast[1]);
								String[] coordinate = zbList.get(i).split(",");
								double lat = Double.parseDouble(coordinate[0]);
								double lng = Double.parseDouble(coordinate[1]);
								middle = GetMiddle(lngLast, latLast, lng, lat);
								double realDistance = GetDistance(longitude, latitude, middle[0], middle[1]);// 获取两个坐标距离
								if (realDistance < minDistance) {
									lats = lat;
									lngs = lng;
									minDistance = realDistance;
									index = i;
								}
							}
							if (minDistance < 200) {// 最小误差距离
								if (middle.length > 1) {
									zbList.add(index, latitude + "," + longitude);
									zbMap.put(String.valueOf(latitude) + "," + String.valueOf(longitude), onbuscounts);
								}
							}

						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
					}
				}
				newPath = markPlatForm(zbList, zbMap);// 纠偏辅助点
			}
			// --end-- 在辅助线坐标中查找与站点坐标最短的点 然后放进去

		} catch (Exception e) {
			e.printStackTrace();
		}
		return newPath;
	}

	// 线路坐标纠偏
	public static List<List<String>> markPlatForm(ArrayList<String> lPath, Map<String, String> zbMap) {
		List<List<String>> lineCoordinates = new ArrayList<List<String>>();
		try {
			if (lPath.size() > 1) {
				for (int i = 0; i < lPath.size(); i++) {
					String coordinates = lPath.get(i);
					String[] coordinate = coordinates.split(",");
					List<String> list = new ArrayList<String>();
					list.add(StringUtils.isBlank(coordinate[0]) ? "" : coordinate[0]);
					list.add(StringUtils.isBlank(coordinate[1]) ? "" : coordinate[1]);
					String odCountAndColor = zbMap.get(coordinates);
					if (StringUtils.isNotBlank(odCountAndColor)) {
						list.add("1");
					} else {
						list.add("0");
					}
					lineCoordinates.add(list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lineCoordinates;
	}

	public static List<List<String>> getBetween(List<List<List<String>>> allLinePlats, List<String> firstLatAndLng,
			List<String> nextLatAndLng) {
		List<List<String>> returnList = new ArrayList<List<String>>();
		for (int i = 0; i < allLinePlats.size(); i++) {
			List<List<String>> line = allLinePlats.get(i);
			List<List<String>> retnList = getCoordinates(line, firstLatAndLng, nextLatAndLng);
			if (CollectionUtils.isNotEmpty(retnList)) {
				returnList = retnList;
			}
		}
		return returnList;
	}

	private static List<List<String>> getCoordinates(List<List<String>> line, List<String> firstLatAndLng,
			List<String> nextLatAndLng) {
		List<List<String>> returnList = new ArrayList<List<String>>();
		int start = line.indexOf(firstLatAndLng);
		int end = line.indexOf(nextLatAndLng);
		if (start > -1 && end > -1) {
			if (start > end) {
				int temp;
				temp = start;
				start = end;
				end = temp;
			}
			boolean isDirect = true;
			List<List<String>> newList = new ArrayList<List<String>>();
			for (int i = start; i <= end; i++) {
				if (i != start && i != end && "1".equals(line.get(i).get(2))) {
					isDirect = false;
				}
				newList.add(line.get(i));
			}
			if (isDirect) {
				returnList.addAll(newList);
			}
		}
		return returnList;
	}

	public static void main(String[] args) {
		List<List<String>> returnList = new ArrayList<List<String>>();
		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");
		list.add("3");

		List<String> list1 = new ArrayList<>();
		list1.add("a");
		list1.add("b");
		list1.add("c");

		returnList.add(list);
		returnList.add(list1);

		System.out.println(returnList.size());
		for (int i = 0; i < returnList.size(); i++) {
			System.out.println(i + "," + returnList.size());
			if (i == 1)
				returnList.add(list1);
		}
		System.out.println(returnList.size());
		System.out.println("______________" + returnList.indexOf(list1));
	}
}
