package com.lty.rt.comm.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * GPS纠偏函数
 * 
 * @author zhangshiping
 * @version 创建时间：2015年7月6日 下午5:40:13
 */
public class GpsCorrect {
    final static double pi = 3.14159265358979324;
    final static double a = 6378245.0;
    final static double ee = 0.00669342162296594323;

	private static Properties props = new Properties();
    /**
     * 是否进行纠偏
     */
    private static boolean isCorrection = true; 
    
  /*  static{
        InputStream in = GpsCorrect.class.getResourceAsStream("/config.properties");
         try {
        	props.load(in);
            isCorrection = Boolean.parseBoolean(props.getProperty("op.map.correction").trim());    
         }catch (IOException e) {
            e.printStackTrace();
         }
    }*/
 
    
  //GCJ-02 to WGS-84
    public static double[] transform(double wgLat, double wgLon) {
    	double[] latlng = new double[2];
        if (outOfChina(wgLat, wgLon) || !isCorrection) {
            latlng[0] = wgLat;
            latlng[1] = wgLon;
        } else {
        	double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        	double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        	double radLat = wgLat / 180.0 * pi;
        	double magic = Math.sin(radLat);
        	magic = 1 - ee * magic * magic;
        	double sqrtMagic = Math.sqrt(magic);
        	dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        	dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        	latlng[0] = wgLat + dLat;
        	latlng[1] = wgLon + dLon;
        }
        return latlng;
    }
 
    private static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }
 
    public static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }
 
    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }
    
    //WGS-84 to GCJ-02 
    public static double[] gcjDecrypt(double gcjLat, double gcjLon){
    	if (outOfChina(gcjLat, gcjLon)  || !isCorrection)
			return new double[]{gcjLat, gcjLon};

		double[] d = delta(gcjLat, gcjLon);
		return new double[]{gcjLat + d[0], gcjLon + d[1]};
    }
    
    public static double[] delta(double gcjLat, double gcjLon){
    	double dLat = transformLat(gcjLon - 105.0, gcjLat - 35.0);
    	double dLon = transformLon(gcjLon - 105.0, gcjLat - 35.0);
    	double radLat = gcjLat / 180.0 * pi;
    	double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		return new double[]{dLat, dLon};
    }
}