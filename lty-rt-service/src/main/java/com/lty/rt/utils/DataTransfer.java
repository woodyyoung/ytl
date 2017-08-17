package com.lty.rt.utils;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;

/**
 * 数据转换工具类
 * @author Administrator
 *
 */
public class DataTransfer {
	
	    // 将数据库CLOB字段转换成String类型
		public static String clobToString(Clob clob) {
			String reString = "";
			try {
				Reader is = clob.getCharacterStream();// 得到流
				BufferedReader br = new BufferedReader(is);
				String s = br.readLine();
				StringBuffer sb = new StringBuffer();
				while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
					sb.append(s);
					s = br.readLine();
				}
				reString = sb.toString();
			} catch (Exception e) {
			}
			return reString;
		}
}
