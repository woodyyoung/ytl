package com.lty.rt.comm.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * excel样式工具类
 * @author qiq
 *
 */
public class ExcelStyle {
	
	/**
	 * 标题样式
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle titelStyle(HSSFWorkbook workbook){
	    HSSFFont headfont = workbook.createFont();    
		headfont.setFontName("黑体");    
	    headfont.setFontHeightInPoints((short) 18);// 字体大小    
	    headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗 
	    
	    // 列头的样式    
	    HSSFCellStyle columnTitle = workbook.createCellStyle();    
	    columnTitle.setFont(headfont);    
	    columnTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中    
	    columnTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中    
	    columnTitle.setLocked(true);    
	    columnTitle.setWrapText(true);    
	    //columnTitle.setLeftBorderColor(HSSFColor.BLACK.index);// 左边框的颜色    
	    //columnTitle.setBorderLeft((short) 1);// 边框的大小    
	    //columnTitle.setRightBorderColor(HSSFColor.BLACK.index);// 右边框的颜色    
	    //columnTitle.setBorderRight((short) 1);// 边框的大小    
	    //columnTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体    
	    //columnTitle.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色
	    return columnTitle;
	
	}

	/**
	 * 表头样式
	 * @param workbook
	 * @return
	 */
	public static HSSFCellStyle headFont(HSSFWorkbook workbook){
	    HSSFFont headfont = workbook.createFont();    
		headfont.setFontName("黑体");    
	    headfont.setFontHeightInPoints((short) 13);// 字体大小    
	    headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗 
	    
	    // 列头的样式    
	    HSSFCellStyle columnHead = workbook.createCellStyle();    
	    columnHead.setFont(headfont);    
	    columnHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中    
	    columnHead.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中    
	    columnHead.setLocked(true);    
	    columnHead.setWrapText(true);    
	    columnHead.setLeftBorderColor(HSSFColor.BLACK.index);// 左边框的颜色    
	    columnHead.setBorderLeft((short) 1);// 边框的大小    
	    columnHead.setRightBorderColor(HSSFColor.BLACK.index);// 右边框的颜色    
	    columnHead.setBorderRight((short) 1);// 边框的大小    
	    columnHead.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体    
	    columnHead.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色
	    return columnHead;
	}
	
	/**
	 * 每列样式
	 * @param workbook
	 * @param flag true左右居中  false左居中
	 * @return
	 */
	public static HSSFCellStyle columnHeadFont(HSSFWorkbook workbook, Boolean flag){
		HSSFFont font = workbook.createFont();    
	    font.setFontName("宋体");    
	    font.setFontHeightInPoints((short) 12);    
	    // 普通单元格样式    
	    HSSFCellStyle style = workbook.createCellStyle();    
	    style.setFont(font);    
	    if(flag){
	    	style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中    
	    }else{
	    	style.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 左居中    
	    }
	    
	    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 上下居中    
	    style.setWrapText(true);    
	    style.setLeftBorderColor(HSSFColor.BLACK.index);    
	    style.setBorderLeft((short) 1);    
	    style.setRightBorderColor(HSSFColor.BLACK.index);    
	    style.setBorderRight((short) 1);    
	    style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体    
	    style.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．    
	    return style;
	}
	
	
	 
	
}
