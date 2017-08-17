package com.lty.rt.comm.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lty.rt.districtManagement.controller.AreaMapController;

import sun.misc.BASE64Decoder;

/**
 * 导出
 * @author qiq
 *
 */
public class ExcelOutUtil {
	
	private static final Logger logger  =  LoggerFactory.getLogger(AreaMapController.class);

	// 文件名
	private String fileName;
	// 文件保存路径
	private String fileDir;
	// sheet名
	private String sheetName;
	
	private HSSFWorkbook workbook = null;

	public ExcelOutUtil(String fileDir, String sheetName) {
		this.fileDir = fileDir;
		this.sheetName = sheetName;
		workbook = new HSSFWorkbook();
	}

	/**
	 * 写excel.
	 * @param titleColumn 对应Map的key
	 * @param titleName excel要导出的名称
	 * @param titleSize 列宽
	 * @param dataList 数据
	 * @param response
	 * @param req
	 * @param imgsURL 图片路径
	 * @param picName 图片名称
	 */
	@SuppressWarnings({"rawtypes" })
	public void wirteExcel(String titleColumn[], String titleName[],Integer titleSize[], List<Map> dataList,HttpServletResponse response,
			HttpServletRequest req, String imgsURL, String picName, Integer picLocation[]) {
		// 添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
		HSSFSheet sheet = workbook.createSheet(this.sheetName);
		// 新建文件
		OutputStream out = null;
		try {
			if (fileDir != null) {
				// 有文件路径
				out = new FileOutputStream(fileDir);
			} else {
				// 否则，直接写到输出流中
				out = response.getOutputStream();
				fileName = sheetName+ ".xls";
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/x-msdownload");
				response.addHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode(fileName, "UTF-8"));
			}
			
			//Base64解码
			base64TOpic(picName, req, imgsURL, response);
			//读取图片
			setPicToIO(sheet, picName, picLocation);
			// 写数据到Excel
			writeDataToExcel(sheet, titleColumn, titleName, titleSize, dataList, picLocation);
			workbook.write(out);
		} catch (Exception e) {
			logger.error("ExcelOutUtil.wirteExcel() error{}",e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("restriction")
	private void base64TOpic(String fileName, HttpServletRequest req, String imgsURl, HttpServletResponse response) {
		// 对字节数组字符串进行Base64解码并生成图片
		if (imgsURl == null) // 图像数据为空
			return;
		try {
			String[] url = imgsURl.split(",");
			String u = url[1];
			// Base64解码
			byte[] buffer = new BASE64Decoder().decodeBuffer(u);
			// 生成图片
			OutputStream out = new FileOutputStream(new File(fileName));
			
			out.write(buffer);
			out.flush();
			out.close();
			return;
		} catch (Exception e) {
			return;
		}
	}
	
	private void setPicToIO(HSSFSheet sheet, String picName, Integer picLoc[]){
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();// 将图片写入流中
		BufferedImage bufferImg;
		try {
			bufferImg = ImageIO.read(new File(picName));
			ImageIO.write(bufferImg, "PNG", byteArrayOut);
			//画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）  
	        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
	        HSSFClientAnchor anchor;
	        if(picLoc.length == 1){
	        	anchor = new HSSFClientAnchor( ExportData.DX1, ExportData.DY1, ExportData.DX2, ExportData.DY2, 
		        		ExportData.COL1, ExportData.ROW1, ExportData.COL2, ExportData.ROW2);
	        }else{
	        	anchor = new HSSFClientAnchor( (int)picLoc[0], (int)picLoc[1], (int)picLoc[2], (int)picLoc[3], 
	        			picLoc[4].shortValue(), (int)picLoc[5], picLoc[6].shortValue(), (int)picLoc[7]);
	        }
	        anchor.setAnchorType(3);     
	        //插入图片    
	        patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));   
		} catch (IOException e) {
			logger.error("ExcelOutUtil.setPicToIO() error{}",e);
		}     
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void writeDataToExcel(HSSFSheet sheet,String titleColumn[], String titleName[], Integer titleSize[], List<Map> dataList, Integer picLoc[]){
        HSSFCellStyle headFont = ExcelStyle.headFont(workbook);
        HSSFCellStyle titleFont = ExcelStyle.titelStyle(workbook);
        HSSFCellStyle columCenterFont = ExcelStyle.columnHeadFont(workbook, true);
        HSSFCellStyle columLeftFont = ExcelStyle.columnHeadFont(workbook, false);

        // 写入excel的标题
        Row titalRow = sheet.createRow(0);
        titalRow.setHeight((short)(30*20));
        Cell cell0 = titalRow.createCell(0);    
        cell0.setCellValue(new HSSFRichTextString(sheetName));    
        cell0.setCellStyle(titleFont);    
        CellRangeAddress range = new CellRangeAddress(0, 0, 0, titleName.length-1);    
        sheet.addMergedRegion(range); 
        
		// 写入excel的表头
        int row = ExportData.ROW2 + 1;
        if(picLoc.length > 1){
			row = (int)picLoc[7] + 1;
		}
		Row titleNameRow = sheet.createRow(row);
		
		for (int i = 0; i < titleName.length; i++) {
			sheet.setColumnWidth(i, titleSize[i] * 256); // 设置宽度
			Cell cell = titleNameRow.createCell(i);
			cell.setCellValue(titleName[i].toString());
			cell.setCellStyle(headFont);
		}

		// 写入excel的内容
		if (dataList != null && dataList.size() > 0) {
			if (titleColumn.length > 0) {
				for (int rowIndex = 1; rowIndex <= dataList.size(); rowIndex++) {
					Map<String, Object> map = dataList.get(rowIndex - 1); // 获得该对象
					Row dataRow = sheet.createRow(rowIndex + row);
					for (int columnIndex = 0; columnIndex < titleColumn.length; columnIndex++) {
						Class<? extends Object> keyClass = null;
						String title = titleColumn[columnIndex].toString().trim();
						if (StringUtils.isNotBlank(title)) {// 字段不为空
							Cell cell = dataRow.createCell(columnIndex);
							if(map.get(title)==null){
								cell.setCellValue(" ");
								cell.setCellStyle(columCenterFont);
								continue;
							}
							keyClass = map.get(title).getClass();
							if (keyClass.equals(Integer.class) || keyClass.equals(BigDecimal.class)) {
								cell.setCellValue(map.get(title).toString());
								cell.setCellStyle(columLeftFont);
							} else if (keyClass.equals(String.class)) {
								cell.setCellValue((String) map.get(title));
								cell.setCellStyle(columCenterFont);
							}
						} else {
							Cell cell = dataRow.createCell(columnIndex);
							cell.setCellFormula("");
						}
					}

				}
			}
		}
	}
}

