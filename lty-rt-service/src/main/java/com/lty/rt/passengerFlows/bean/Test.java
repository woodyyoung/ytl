package com.lty.rt.passengerFlows.bean;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.lty.rt.basicData.bean.Line;
import com.lty.rt.basicData.bean.PlatForm;
import com.lty.rt.basicData.bean.Station;

public class Test {
	String[] pdata = {"同乐总站", "同乐人人购物", "同乐社区", "同乐工业区", "新大坑市场", "龙东社区",
			"双龙地铁站", "龙岗长途客", "龙岗街道办", "南联地铁站", "南联天桥", "龙城广场地", "龙兴联泰",
			"吉祥地铁站", "岗贝社区", "爱联小学", "爱联地铁站", "爱联社区", "龙岗cocopa", "大运地铁站",
			"荷坳社区", "荷坳地铁站", "水晶之城", "银海工业区", "永湖地铁站", "独竹", "横岗地铁站", "横岗志健广场",
			"力嘉工业区", "横岗街道办", "塘坑地铁站", "六约社区", "牛始埔居民", "六约地铁站", "深坑居民小组"};
	String[] ldata = {"332", "217", "334"};
	int size = 20;
	int year = 2016;
	int[] hours = {6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
			22, 23};
	BufferedWriter bw = null;

	Test() throws FileNotFoundException {
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				"d:\\aaa.txt")));
	}
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
	public static void main(String[] args) throws IOException {
		Test t = new Test();
		List<PlatForm> list = t.buildPlatForm();
		t.printBuildPlatFormSql(list);
		List<Line> lineList = t.bulidLine();
		t.printLineBuildSql(lineList);
		List<Station> sList = t.buildStation(list, lineList);
		t.printStationBuildSql(sList);
		t.buildKl(sList, 9, 30);
		t.writeToFile("commit;\r\n");
		t.buildKl(sList, 10, 31);
		t.writeToFile("commit;\r\n");
		t.buildKl(sList, 11, 30);
		t.writeToFile("commit;\r\n");
		t.buildKl(sList, 12, 31);
		t.writeToFile("commit;\r\n");
		t.closeBw();
		System.out.println("站台：---" + list.size());
		System.out.println("线路：---" + lineList.size());
		System.out.println("站点：---" + sList.size());
	}
	public List<PlatForm> buildPlatForm() {
		List<PlatForm> list = new ArrayList<PlatForm>(pdata.length);
		for (String name : pdata) {
			PlatForm p = new PlatForm();
			p.setId(UUID.randomUUID().toString().replace("-", ""));
			p.setName(name);
			p.setLatitude(new BigDecimal(randInt(50)));
			p.setLongitude(new BigDecimal(randInt(50)));
			list.add(p);
		}
		return list;
	}
	private void printBuildPlatFormSql(List<PlatForm> list) {
		for (PlatForm p : list) {
			String sql = "insert into pjmk_PlatForm(ID,name,longitude,latitude)values('"
					+ p.getId()
					+ "','"
					+ p.getName()
					+ "',"
					+ p.getLongitude()
					+ "," + p.getLatitude() + ");";
			System.out.println(sql);
			writeToFile(sql);
		}
	}
	private List<Line> bulidLine() {
		List<Line> list = new ArrayList<Line>();
		for (String l : ldata) {
			Line line = new Line();
			line.setId(UUID.randomUUID().toString().replace("-", ""));
			line.setGprsid(Integer.parseInt(l));
			line.setName(l + "路");
			list.add(line);
		}
		return list;
	}
	private void printLineBuildSql(List<Line> list) {
		for (Line l : list) {
			String sql = "insert into pjmk_line(id,name,gprsid)values('"
					+ l.getId() + "','" + l.getName() + "'," + l.getGprsid()
					+ ");";
			System.out.println(sql);
			writeToFile(sql);
		}
	}
	private List<Station> buildStation(List<PlatForm> plist, List<Line> lList) {
		List<Station> list = new ArrayList<Station>();
		for (Line l : lList) {
			Set<Integer> randp = getRandPlatForm(plist.size(), size);
			Iterator<Integer> iterator = randp.iterator();
			while (iterator.hasNext()) {
				Integer i = iterator.next();
				Station s = new Station();
				PlatForm p = plist.get(i);
				s.setId(UUID.randomUUID().toString().replace("-", ""));
				s.setName(l.getName() + p.getName());
				s.setLineid(l.getId());
				s.setGprsid(l.getGprsid());
				s.setOrderno(i + 1);
				s.setOrderno(0);
				s.setBystartdIstance(new BigDecimal(randInt(20)));
				s.setLatitude(new BigDecimal(randInt(50)));
				s.setLongitude(new BigDecimal(randInt(50)));
				s.setPlatformId(p.getId());
				list.add(s);
			}
		}
		return list;
	}
	private void printStationBuildSql(List<Station> list) {
		for (Station l : list) {
			String sql = "insert into pjmk_station(id,name,PLATFORM_ID,lineid,gprsid,orderno,direction,bystartd_istance,longitude,latitude)values('"
					+ l.getId()
					+ "','"
					+ l.getName()
					+ "','"
					+ l.getPlatformId()
					+ "','"
					+ l.getLineid()
					+ "',"
					+ l.getGprsid()
					+ ","
					+ l.getOrderno()
					+ ","
					+ l.getDirection()
					+ ","
					+ l.getBystartdIstance()
					+ ","
					+ l.getLongitude() + "," + l.getLatitude() + ");";
			System.out.println(sql);
			writeToFile(sql);
		}
	}
	private void buildKl(List<Station> list, int month, int days)
			throws IOException {
		int totalCount = 0;
		totalCount = 0;
		for (Station s : list) {
			for (int i = 1; i <= days; i++) {
				for (int j = 0; j < hours.length; j++) {
					StringBuffer sb = new StringBuffer();
					sb.append("insert into pjmk_station_psgflow(id,station_id,PlatForm_id,onboard_id,occur_time,total_person_count,onbus_person_count,offbus_person_count,holiday_flag)values(");
					sb.append("'"
							+ UUID.randomUUID().toString().replace("-", "")
							+ "',");
					sb.append("'" + s.getId() + "',");
					sb.append("'" + s.getPlatformId() + "',");
					sb.append("'" + randInt(1000000) + "',");
					sb.append("to_date('2016/" + month + "/" + i + " "
							+ hours[j] + "','yyyy/mm/dd hh24'),");
					sb.append("" + (20 + randInt(15)) + ",");
					sb.append("" + randInt(15) + ",");
					sb.append("" + randInt(15) + ",");
					sb.append("0");
					sb.append(");");
					// System.out.println(sb.toString());
					writeToFile(sb.toString());
					totalCount++;
				}
			}
		}
		System.out.println(totalCount);
	}
	private void writeToFile(String str) {
		try {
			this.bw.write(str);
			this.bw.write("\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void closeBw() throws IOException {
		if (this.bw != null) {
			this.bw.close();
		}
	}
	private Set<Integer> getRandPlatForm(int totalSize, int randSize) {
		Set<Integer> hashSet = new HashSet<Integer>();
		for (int i = 0; i < 100000000; i++) {
			if (hashSet.size() >= randSize) {
				break;
			}
			hashSet.add(new Random().nextInt(totalSize));
		}
		return hashSet;
	}
	private int randInt(int value) {
		Random r = new Random();
		return r.nextInt(value);
	}
}
