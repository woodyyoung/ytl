package com.lty.rt.auxiliaryAnalysis.controller;

import java.text.ParseException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.auxiliaryAnalysis.service.PsgCorridorService;
import com.lty.rt.comm.bean.RTResponse;

/**
 * 客运走廊分析
 * @author Administrator
 *
 */
@RequestMapping("/psgCorridorAnalysis")
@Controller
public class PsgCorridorController {
	@Autowired
    private PsgCorridorService psgCorridorService;
    
    /**
     * 按天分析
     * @param map
     * @return
     * @throws ParseException
     */
	@RequestMapping("/queryPsgCorridorByDay")
	@ResponseBody
	public RTResponse queryPsgCorridorByDay(@RequestBody Map<String, Object> params)
			throws ParseException {
		RTResponse res = new RTResponse();
		res.setData(psgCorridorService.queryPsgCorridorByDay(params));
		return res;
	}
	
	/**
     * 查询客流TOP
     * @param map
     * @return
     * @throws ParseException
     */
	@RequestMapping("/queryFlowOutTop")
	@ResponseBody
	public RTResponse queryFlowOutTop(@RequestBody Map<String, Object> params)
			throws ParseException {
		RTResponse res = new RTResponse();
		res.setData(psgCorridorService.queryFlowOutTop(params));
		return res;
	}
	
	/**
     * 查询客流TOP
     * @param map
     * @return
     * @throws ParseException
     */
	@RequestMapping("/queryFlowInTop")
	@ResponseBody
	public RTResponse queryFlowInTop(@RequestBody Map<String, Object> params)
			throws ParseException {
		RTResponse res = new RTResponse();
		res.setData(psgCorridorService.queryFlowInTop(params));
		return res;
	}
	
	/**
     * 查询客流分析
     * @param map
     * @return
     * @throws ParseException
     */
	@RequestMapping("/queryFlowAnalysis")
	@ResponseBody
	public RTResponse queryFlowAnalysis(@RequestBody Map<String, Object> params)
			throws ParseException {
		RTResponse res = new RTResponse();
		res.setData(psgCorridorService.queryFlowAnalysis(params));
		return res;
	}
	
	/**
     * 查询小区地图数据
     * @param map
     * @return
     * @throws ParseException
     */
	@RequestMapping("/queryAreaMap")
	@ResponseBody
	public RTResponse queryAreaMap(@RequestBody Map<String, Object> params)
			throws ParseException {
		RTResponse res = new RTResponse();
		res.setData(psgCorridorService.queryAreaMap(params));
		return res;
	}
	
	/**
     * 按小时分析
     * @param map
     * @return
     * @throws ParseException
     */
	@RequestMapping("/queryPsgCorridorByHour")
	@ResponseBody
	public RTResponse queryPsgCorridorByHour(@RequestBody Map<String, Object> params)
			throws ParseException {
		RTResponse res = new RTResponse();
		res.setData(psgCorridorService.queryPsgCorridorByHour(params));
		return res;
	}
	
	/**
     * 按周分析
     * @param map
     * @return
     * @throws ParseException
     */
	@RequestMapping("/queryPsgCorridorByWeek")
	@ResponseBody
	public RTResponse queryPsgCorridorByWeek(@RequestBody Map<String, Object> params)
			throws ParseException {
		RTResponse res = new RTResponse();
		res.setData(psgCorridorService.queryPsgCorridorByWeek(params));
		return res;
	}
	
	/**
     * 按月分析
     * @param map
     * @return
     * @throws ParseException
     */
	@RequestMapping("/queryPsgCorridorByMonth")
	@ResponseBody
	public RTResponse queryPsgCorridorByMonth(@RequestBody Map<String, Object> params)
			throws ParseException {
		RTResponse res = new RTResponse();
		res.setData(psgCorridorService.queryPsgCorridorByMonth(params));
		return res;
	}
}
