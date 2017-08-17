/*package com.lty.rt.gps.test;

import java.math.BigDecimal;
import java.sql.Clob;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lty.rt.basicData.bean.Line;
import com.lty.rt.basicData.bean.PlatForm;
import com.lty.rt.basicData.bean.Station;
import com.lty.rt.basicData.mapper.LineMapper;
import com.lty.rt.basicData.mapper.PlatFormMapper;
import com.lty.rt.basicData.mapper.StationMapper;
import com.lty.rt.comm.util.GpsCorrect;
import com.lty.rt.utils.DataTransfer;

@ContextConfiguration(locations = {"classpath*:spring/applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class CorrectGpsTest {

	@Autowired
	private LineMapper lineMapper;
	
	@Autowired
	private PlatFormMapper platFormMapper;
	
	@Autowired
	private StationMapper stationMapper;
	
	@Test
	public void correctLineGps(){
		System.out.println("line start:");
		
		List<Line> lines = lineMapper.listAllLineDetail();
		if(CollectionUtils.isNotEmpty(lines)){
			for(Line line : lines){
				System.out.println("line name: " + line.getName());
				System.out.println("gps: " + DataTransfer.clobToString((Clob) line.getLpath()));
				
				String lpath = DataTransfer.clobToString((Clob) line.getLpath());
				String[] points = lpath.split("\\|");
				StringBuffer sb = new StringBuffer();
				for(String point : points){
					String[] local = point.split(",");
					double[] result = GpsCorrect.gcjDecrypt(Double.parseDouble(local[0]), Double.parseDouble(local[1]));
					System.out.println("result : lat --" + result[0] + " lon --" + result[1]);
					sb.append(result[0]).append(",").append(result[1]).append("|");
				}
				System.out.println("sb : " + sb.substring(0, sb.length()-1));
				line.setLpath(sb.substring(0, sb.length()-1));
				lineMapper.updateByPrimaryKeySelective(line);
			}
		}
	}
	
	@Test
	public void correctPlatformGps(){
		System.out.println("platform start:");
		List<PlatForm> platforms = platFormMapper.listAllPlatform();
		if(CollectionUtils.isNotEmpty(platforms)){
			for(PlatForm platform : platforms){
				System.out.println("platform name:" + platform.getName());
				System.out.println("lat: " + platform.getLatitude() + "  lon:  " + platform.getLongitude());
				
				double[] result = GpsCorrect.gcjDecrypt(platform.getLatitude().doubleValue(), platform.getLongitude().doubleValue());
				
				System.out.println("result : lat --" + result[0] + " lon --" + result[1]);
				platform.setLatitude(new BigDecimal(result[0]));
				platform.setLongitude(new BigDecimal(result[1]));
				platFormMapper.updateByPrimaryKeySelective(platform);
			}
		}
	}
	
	@Test
	public void correctStationGps(){
		System.out.println("station start:");
		
		List<Station> stations = stationMapper.selectAllStation();
		if(CollectionUtils.isNotEmpty(stations)){
			for(Station station : stations){
				System.out.println("station name:" + station.getName());
				System.out.println("lat: "+ station.getLatitude() + "  lon: " + station.getLongitude());
				
				double[] result = GpsCorrect.gcjDecrypt(station.getLatitude().doubleValue(), station.getLongitude().doubleValue());
				System.out.println("result : lat --" + result[0] + " lon --" + result[1]);
				
				station.setLatitude(new BigDecimal(result[0]));
				station.setLongitude(new BigDecimal(result[1]));
				
				stationMapper.updateByPrimaryKeySelective(station);
			}
		}
	}
}
*/