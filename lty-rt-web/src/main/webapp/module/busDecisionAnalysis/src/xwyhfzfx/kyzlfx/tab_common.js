
/**
 * 客运走廊公用js方法
 */
window.tab_comm = {
	
	map : null,
	
	infoWindow : null,
	
	polygons : null,
	
	beginDate : null,
	
	endDate : null,
	
	areaCode : null,
	
	flowDirection : null,
	
	workType : null,
	
	flowInChart : null,
	
	flowOutChart : null,
	
	folwDegree : null,
	
	folwDegrees : null,
	
	/**
	 * 初始化小区
	 */
	initArea : function() {
		var params = {};
		comm.request('/report/areaAnalysis/listAllArea', params,
				function(resp) {
					comm.initAreaSelect('#xqselect', resp.data);
				});
	},
	
	/**
	 * 划线
	 */
	drawLine : function(lineLpath, strokeColor, extData) {
		var busPolyline = new AMap.Polyline({
			map : tab_comm.map,
			path : lineLpath,
			strokeColor : strokeColor,
			strokeOpacity : 0.8,
			strokeWeight : 5,
			extData : extData
		});
		busPolyline.on("mouseover", tab_comm.polygonMouse);
		busPolyline.on("mouseout", tab_comm.polygonMouse_close);
		tab_comm.map.setFitView();
	},
	
	/**
	 * 获取小区轮廓经纬度
	 * @latLng ："24.36708-109.410546,24.35977-109.411576,24.360747-109.418572,24.367901-109.420031,24.367784-109.420202"
	 * 返回：[[109.410546,24.36708],[109.411576,24.35977]]
	 */
	getResidentialArea : function(latLng) {
		var ResidentialArea = [];
		var latLngArray = latLng.split(',');
		for ( var i = 0; i < latLngArray.length; i++) {
			var latLngStr = latLngArray[i];
			if (comm.isNotEmpty(latLngStr)) {
				var templatLng = [];
				templatLng.push(latLngStr.split('-')[1]);
				templatLng.push(latLngStr.split('-')[0]);
				ResidentialArea.push(templatLng);
			}
		}
		return ResidentialArea;
	},
	
	/**
	 * 画选择的小区轮廓。
	 */
	drawSelectArea : function(data) {
		var extData = {
			content : "<div class='linePassFlow_num'>" + "<p>小区名:"
					+ data.stationname + "</p></div>"
		};
		var polygon = new AMap.Polygon({
			path : data.ResidentialArea,
			// 设置多边形边界路径
			strokeColor : "#FF3366",
			// 线颜色
			strokeOpacity : 0.2,
			// 线透明度
			strokeWeight : 1,
			// 线宽
			fillColor : "#FF3366",
			// 填充色
			fillOpacity : 0.35,
			// 填充透明度
			map : tab_comm.map,
			extData : extData
		});
		tab_comm.polygons.push({
			"polygon" : polygon
		});
		polygon.on("mouseover", tab_comm.polygonMouse);
		polygon.on("mouseout", tab_comm.polygonMouse_close);
		tab_comm.map.setFitView();
	},
	
	ResidentialArea : function(jsonData) {
		var data = [];
		var selectArea = {};
		for ( var i = 0; i < jsonData.length; i++) {
			var obj = jsonData[i];
			if (i == 0) {
				selectArea.ResidentialArea = tab_comm.getResidentialArea(obj.SLAN);
				selectArea.stationname = obj.S_AREA_NAME;
			}
			var tempData = {};
			tempData.ResidentialArea = tab_comm.getResidentialArea(obj.TLAN);
			tempData.stationname = obj.S_AREA_NAME;
			tempData.targetstationname = obj.T_AREA_NAME;
			tempData.flowInto = 0;
			tempData.outFlow = obj.OD_VALUE;
			data.push(tempData);
		}
		tab_comm.drawSelectArea(selectArea);

		var polygons = [];
		for ( var i = 0; i < data.length; i++) {
			var polygonArr = data[i].ResidentialArea;
			var extData = {
				content : "<div class='linePassFlow_num'>"
						+ "<p>小区名:" + data[i].targetstationname
						+ "</p>" + "<p>流入:" + data[i].flowInto
						+ "</p>" + "<p>流出:" + data[i].outFlow
						+ "</div>"
			};
			var polygon = new AMap.Polygon({
				path : polygonArr,
				// 设置多边形边界路径
				strokeColor : "#FF3366",
				// 线颜色
				strokeOpacity : 0.2,
				// 线透明度
				strokeWeight : 3,
				// 线宽
				fillColor : "#3366FF",
				// 填充色
				fillOpacity : 0.35,
				// 填充透明度
				map : tab_comm.map,
				extData : extData
			});
			tab_comm.polygons.push({
				"polygon" : polygon,
				"number" : data[i].flowInto+data[i].outFlow
			});
			polygon.on("mouseover", tab_comm.polygonMouse);
			polygon.on("mouseout", tab_comm.polygonMouse_close);
			tab_comm.map.setFitView();
		}

		var lpath = [];
		var sourcePloygon = tab_comm.polygons[0];
		for ( var j = 1; j < tab_comm.polygons.length; j++) {
			var targetpolygon = tab_comm.polygons[j];
			var line = {
				"start" : sourcePloygon.polygon.getBounds()
						.getCenter(),
				"end" : targetpolygon.polygon.getBounds()
						.getCenter(),
				"number" : targetpolygon.polygon.number
			}
			lpath.push(line);
		}

		for ( var j = 0; j < lpath.length; j++) {
			var lineLpath = [];
			lineLpath.push(lpath[j].start, lpath[j].end);
			var strokeColor = "#09f";
			var carNum = lpath[j].number;
			if (carNum > 0 && carNum <= 1000) {
				strokeColor = "#34b000"
			} else if (carNum > 1000 && carNum <= 2000) {
				strokeColor = "#fecb00"
			} else if (carNum > 2000 && carNum <= 3000) {
				strokeColor = "#f57c7c"
			} else if (carNum > 3000 && carNum <= 4000) {
				strokeColor = "#df0100"
			} else {
				strokeColor = "#8e0e0b"
			}
			tab_comm.drawLine(lineLpath, strokeColor);
		}

	},
	
	drawAreaLine : function (areaData, lineData) {
		var idArr = [];
		var polygonArr = [];
		for(var i=0; i<areaData.length; i++){
			var areaId = areaData[i].areaId;
			var lan = areaData[i].lan;
			var areaName = areaData[i].areaName;
			
			var residentialArea = tab_comm.getResidentialArea(lan);
			var extData = {
				content : "<div class='linePassFlow_num'>"
						+ "<p>小区名:" + areaName + "</p></div>"
			};
			
			var polygon = new AMap.Polygon({
				path : residentialArea,
				// 设置多边形边界路径
				strokeColor : "#FF3366",
				// 线颜色
				strokeOpacity : 0.2,
				// 线透明度
				strokeWeight : 3,
				// 线宽
				fillColor : "#3366FF",
				// 填充色
				fillOpacity : 0.35,
				// 填充透明度
				map : tab_comm.map,
				extData : extData
			});
			
			idArr.push(areaId);
			polygonArr.push(polygon);
			
			polygon.on("mouseover", tab_comm.polygonMouse);
			polygon.on("mouseout", tab_comm.polygonMouse_close);
			tab_comm.map.setFitView();
		}
		
		for(var i=0; i<lineData.length; i++) {
			var line = lineData[i];
			var lineArr = line.split(',');
			
			var index0 = tab_comm.getPolygonIndex(idArr, lineArr[0]);
			var index1 = tab_comm.getPolygonIndex(idArr, lineArr[1]);
			
			var point0 = polygonArr[index0].getBounds().getCenter();
			var point1 = polygonArr[index1].getBounds().getCenter();
			
			var lineLpath = [];
			lineLpath.push(point0, point1);
			tab_comm.drawLine(lineLpath, "#8e0e0b");
		}
	},
	
	getPolygonIndex : function (idArr, id) {
		for(var i=0; i<idArr.length; i++){
			if(idArr[i] == id){
				return i;
			}
		}
		return -1;
	},
	
	polygonMouse : function(e) {
		var polygon = e.target;
		var content = polygon.getExtData().content; // 地图上所标点的坐标
		tab_comm.infoWindow.setContent(content);
		tab_comm.infoWindow.open(tab_comm.map, e.lnglat);
	},
	
	polygonMouse_close : function(e) {
		tab_comm.infoWindow.close();
	},
	
	initInChart : function() {
		var dom = document.getElementById("passengerCorr_bar");
		tab_comm.flowInChart = echarts.init(dom);
		var app = {};
		option = null;
		app.title = '坐标轴刻度与标签对齐';

		option = {
			color : [ '#3398DB' ],
			title : {
				text : '小区流入Top10',
				x : 'center'
			},
			tooltip : {
				trigger : 'axis',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				containLabel : true
			},
			xAxis : [ {
				type : 'category',
				data : [],
				axisTick : {
					alignWithLabel : true
				}
			} ],
			yAxis : [ {
				type : 'value',
				axisLabel : {
	                formatter: '{value} 人次'
	            }
			} ],
			series : [ {
				name : '直接访问',
				type : 'bar',
				barWidth : '60%',
				data : []
			} ]
		};

		if (option && typeof option === "object") {
			tab_comm.flowInChart.setOption(option, true);
		}
	},
	
	updateInChart : function (data){
		if(!data||data.length<1){
			tab_comm.initInChart();
			return false;
		}
		
		var legendData = [];
		var xAxisData = [];
		var json = data;
		var Series = [];

		for ( var i = 0; i < json.length; i++) {
			var item = json[i];
			Series.push(item.flow);
			if(!comm.contains(xAxisData, item.name)){
				xAxisData.push(item.name);
			}
		}
		
		tab_comm.flowInChart.setOption({
			legend : {
				data : legendData
			},
			xAxis : {
				data : xAxisData
			},
			series : {
				name : '',
				type : 'bar',
				barWidth:20,
				data : Series
			}
		});
	},
	
	initOutChart : function() {
		var dom = document.getElementById("passengerCorr_pie");
		tab_comm.flowOutChart = echarts.init(dom);
		var app = {};
		option = null;
		app.title = '坐标轴刻度与标签对齐';

		option = {
			color : [ '#3398DB' ],
			title : {
				text : '小区流出Top10',
				x : 'center'
			},
			tooltip : {
				trigger : 'axis',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				containLabel : true
			},
			xAxis : [ {
				type : 'category',
				data : [],
				axisTick : {
					alignWithLabel : true
				}
			} ],
			yAxis : [ {
				type : 'value',
				axisLabel : {
	                formatter: '{value} 人次'
	            }
			} ],
			series : [ {
				name : '直接访问',
				type : 'bar',
				barWidth : '60%',
				data : []
			} ]
		};
		if (option && typeof option === "object") {
			tab_comm.flowOutChart.setOption(option, true);
		}
	},
	
	updateOutChart : function (data){
		if(!data||data.length<1){
			tab_comm.initOutChart();
			return false;
		}
		
		var legendData = [];
		var xAxisData = [];
		var json = data;
		var Series = [];

		for ( var i = 0; i < json.length; i++) {
			var item = json[i];
			Series.push(item.flow);
			if(!comm.contains(xAxisData, item.name)){
				xAxisData.push(item.name);
			}
		}
		
		tab_comm.flowOutChart.setOption({
			legend : {
				data : legendData
			},
			xAxis : {
				data : xAxisData
			},
			series : {
				name : '',
				type : 'bar',
				barWidth:20,
				data : Series
			}
		});
	},
	
	initLine : function() {
		var dom = document.getElementById("passengerCorr_line");
		tab_comm.analysisChart = echarts.init(dom);
		var app = {};
		option = null;
		option = {
			title : {
				text : '小区客流分析图'
			},
			tooltip : {
				trigger : 'axis'
			},
			toolbox : {
				show : false,
				feature : {
					saveAsImage : {}
				}
			},
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				containLabel : true
			},
			xAxis : [ {
				type : 'category',
				boundaryGap : false,
				data : []
			} ],
			yAxis : [ {
				type : 'value',
				axisLabel : {
	                formatter: '{value} 人次'
	            }
			} ],
			series : [ {
				name : '',
				type : 'line',
				stack : '总量',
				data : []
			} ]
		};
		if (option && typeof option === "object") {
			tab_comm.analysisChart.setOption(option, true);
		}
	},
	
	updateLine : function (data){
		if(!data||data.length<1){
			self.initLine();
			return false;
		}
		
		tab_comm.initLine();
		var legendData = [];
		var xAxisData = [];
		var json = data;// 后台返回的json
		var series = []; // 准备存放图表数据

		for ( var i = 0; i < json.length; i++) {
			var item = json[i];
			if(!comm.contains(xAxisData, item.TIME)){
				xAxisData.push(item.TIME);
				series.push(item.FLOW);
			}
		}

		tab_comm.analysisChart.setOption({
			legend : {
				data : legendData
			},
			xAxis : {
				data : xAxisData
			},
			series : {
				type : 'line',
				data : series
			}
		});
	},
	
	initPage : function (tpl) {
		$('#tab-form').html(tpl);
		
		// 初始化日历控件
		$("#begin_date").datetimepicker({
			format : 'yyyy-mm-dd',
			language : "zh-CN",
			minView : 'month'
		});
		
		$("#end_date").datetimepicker({
			format : 'yyyy-mm-dd',
			language : "zh-CN",
			minView : 'month'
		});
		
		 //
		$('#planName').change(function(){
			var planId =  $('#planName').val();
			var levelData = tab_comm.getFlowLevelData(planId);
			tab_comm.folwDegree = levelData;
			comm.initLevelDiv(levelData);
		});
	},
	
	initTab : function () {
		
		//初始化地图
		tab_comm.map = new AMap.Map('visitorsFlowRate_map', {
			resizeEnable : true,
			center : [ 109.4, 24.33 ],
			mapStyle : "light",
			zoom : 13
		});
		
		AMap.plugin([ 'AMap.ToolBar', 'AMap.Scale', 'AMap.OverView' ], function() {
			tab_comm.map.addControl(new AMap.ToolBar());
			tab_comm.map.addControl(new AMap.Scale());
		});
		
		tab_comm.infoWindow = new AMap.InfoWindow({
			offset : new AMap.Pixel(0, -10)
		});
		
		//客流等级
		tab_comm.initPlanDiv();
		
		//流入top10
		tab_comm.initOutChart();
		
		//流出top10
		tab_comm.initInChart();
		
		//客流分析折线图
		tab_comm.initLine();
	},
	//根据客流获取客流等级颜色值
    getFlowLevelData : function(value){
    	if(comm.isNotEmpty(value)){
    		var ret = '';
    		for(var i=0; i<tab_comm.folwDegrees.length; i++){
    			var planId = tab_comm.folwDegrees[i].id;
    			var levelList = tab_comm.folwDegrees[i].levelList;
    			if(value == planId){
    				ret = levelList;
    			}
    		}
    		return ret;
    	}
    },
	
	/*//客流等级绑定
	initLevelDiv : function() {
		var params = {};
        params.dataType = '008';
        params.isDisable = '1';
    	comm.requestJson('/report/passengerFlowLevel/list', JSON.stringify(params),
	    function(resp) {
		tab_comm.folwDegree = resp;
			for ( var i = 0; i < resp.length; i++) {
				var li = "<li><span style='background:"
						+ resp[i].CIRCLECOLOR + "'></span>"
						+ resp[i].MINDATA + " - " + resp[i].MAXDATA
						+ "</li>";
				$(".linePassengerFlowSign_ul").append(li);
			}
			comm.hideOrShowLevel();
		});
	},*/
	initPlanDiv:function(){
    	var params = {};
        params.dataType = '008';
        params.isDisable = '1';
    	comm.requestJson('/report/passengerFlowLevel/planAndLevelList', JSON.stringify(params),
    	            function(resp) {
    					tab_comm.folwDegrees = resp;
    					comm.initSelectOptionForObj('planName', resp, 'planName', 'id');
    					comm.initLevelDiv(resp[0].levelList);
    					tab_comm.folwDegree = resp[0].levelList;
    	            });
    	
    },
	
	//根据客流获取客流等级颜色值
    getFlowDegree : function(value){
    	for(var i=0; i<tab_comm.folwDegree.length; i++){
    		var max = tab_comm.folwDegree[i].maxdata;
    		var min = tab_comm.folwDegree[i].mindata;
    		if(value >= min && value < max){
    			return tab_comm.folwDegree[i].circlecolor;
    		}
    	}
    	return tab_comm.folwDegree[0].circlecolor;
    },
	
	//清除地图和图标数据
	clearMapAndChart : function () {
		//流入top10
		tab_comm.initOutChart();
		
		//流出top10
		tab_comm.initInChart();
		
		//客流分析折线图
		tab_comm.initLine();
		
		//初始化小区下拉框
		tab_comm.initArea();
		
		tab_comm.map.clearMap();
	 },
	 
	 loadFlowOutTop : function (flowOut, info){
		 if(flowOut == null || flowOut.length == 0){
			 return;
		 }
		 
		 if(info == null || info.length == 0){
			 return;
		 }
		 
		 var chartData = [];
		 if(flowOut.length >= 10){
			 for(var i=0; i<10; i++){
				 var areaInfo = tab_comm.getAreaInfoByCode(flowOut[i].CODE, info);
				 if(areaInfo == null){
					 continue;
				 }
				 var flowInfo = {
					name : areaInfo.areaName,
					flow : flowOut[i].FLOW
				 };
				 chartData.push(flowInfo);
			 }
		 }else{
			 for(var i=0; i<flowOut.length; i++){
				 var areaInfo = tab_comm.getAreaInfoByCode(flowOut[i].CODE, info);
				 if(areaInfo == null){
					 continue;
				 }
				 var flowInfo = {
					name : areaInfo.areaName,
					flow : flowOut[i].FLOW
				 };
				 chartData.push(flowInfo);
			 }
		 }
		 
		 tab_comm.updateOutChart(chartData);
	 },
	 
	 getAreaInfoByCode : function (code, infos){
		 for(var i=0; i<infos.length; i++){
			 if(code == infos[i].areaId){
				 return infos[i];
			 }
		 }
		 return null;
	 },
	 
	 loadFlowInTop : function (flowIn, info){
		 if(flowIn == null || flowIn.length == 0){
			 return;
		 }
		 
		 if(info == null || info.length == 0){
			 return;
		 }
		 
		 var chartData = [];
		 if(flowIn.length >= 10){
			 for(var i=0; i<10; i++){
				 var areaInfo = tab_comm.getAreaInfoByCode(flowIn[i].CODE, info);
				 if(areaInfo == null){
					 continue;
				 }
				 var flowInfo = {
					name : areaInfo.areaName,
					flow : flowIn[i].FLOW
				 };
				 chartData.push(flowInfo);
			 }
		 }else{
			 for(var i=0; i<flowIn.length; i++){
				 var areaInfo = tab_comm.getAreaInfoByCode(flowIn[i].CODE, info);
				 if(areaInfo == null){
					 continue;
				 }
				 var flowInfo = {
					name : areaInfo.areaName,
					flow : flowIn[i].FLOW
				 };
				 chartData.push(flowInfo);
			 }
		 }
		 
		 tab_comm.updateInChart(chartData);
	 },
	 
	 loadFlowAnalysis : function (flowAnalysis){
		 tab_comm.updateLine(flowAnalysis);
	 },
	 
	 loadMapLine : function (adjoin, info, adjoinFlow, flowOut, flowIn){
		var idArr = [];
		var nameArr = [];
		var polygonArr = [];
		for(var i=0; i<info.length; i++){
			var areaId = info[i].areaId;
			var lan = info[i].lan;
			var areaName = info[i].areaName;
			var outNum = tab_comm.getFlowValue(flowOut, areaId);
			var inNum = tab_comm.getFlowValue(flowIn, areaId);
			var totle = 0;
			
			var extData = {};
			if(tab_comm.flowDirection == 0){
				totle = outNum + inNum;
				extData.content = "<div class='linePassFlow_num'>"
						+ "<p>小区名:" + areaName + "</p>"
						+ "<p>全部客流:" + totle + "</p>"
						+ "<p>流入:" + inNum + "</p>"
						+ "<p>流出:" + outNum + "</p></div>";
			}else if(tab_comm.flowDirection == 1){
				totle = outNum;
				extData.content = "<div class='linePassFlow_num'>"
					+ "<p>小区名:" + areaName + "</p>"
					+ "<p>流出:" + outNum + "</p></div>";
			}else if(tab_comm.flowDirection == 2){
				totle = inNum;
				extData.content = "<div class='linePassFlow_num'>"
					+ "<p>小区名:" + areaName + "</p>"
					+ "<p>流入:" + inNum + "</p></div>";
			}
			
			var color = tab_comm.getFlowDegree(totle);
			var residentialArea = tab_comm.getResidentialArea(lan);
			var polygon = new AMap.Polygon({
				path : residentialArea,
				// 设置多边形边界路径
				strokeColor : color,
				// 线颜色
				//strokeOpacity : 0.2,
				// 线透明度
				//strokeWeight : 3,
				// 线宽
				fillColor : color,
				// 填充色
				fillOpacity : 0.35,
				// 填充透明度
				map : tab_comm.map,
				extData : extData
			});
			
			idArr.push(areaId);
			nameArr.push(areaName);
			polygonArr.push(polygon);
			
			polygon.on("mouseover", tab_comm.polygonMouse);
			polygon.on("mouseout", tab_comm.polygonMouse_close);
			tab_comm.map.setFitView();
		}
		
		var lines = [];
		for(var i=0; i<adjoin.length; i++) {
			var line = adjoin[i];
			if($.inArray(line, lines) > -1){
				continue;
			}
			
			var lineArr = line.split(',');
			if($.inArray(lineArr[1]+","+lineArr[0], lines) > -1){
				continue;
			}
			lines.push(line);
			
			var index0 = tab_comm.getPolygonIndex(idArr, lineArr[0]);
			var index1 = tab_comm.getPolygonIndex(idArr, lineArr[1]);
			
			var point0 = polygonArr[index0].getBounds().getCenter();
			var point1 = polygonArr[index1].getBounds().getCenter();
			
			var lineLpath = [];
			lineLpath.push(point0, point1);
			
			var adjoin0 = typeof(adjoinFlow[line]) == "undefined" ?  0 : adjoinFlow[line];
			var adjoin1 = typeof(adjoinFlow[lineArr[1]+","+lineArr[0]]) == "undefined" ?  0 : adjoinFlow[lineArr[1]+","+lineArr[0]];
			var extData = {
				content : "<div class='linePassFlow_num'>"
						+ "<p>" + nameArr[index0] +"--&gt;"+ nameArr[index1] + " 客流：" + adjoin0 + "</p>"
						+ "<p>" + nameArr[index1] +"--&gt;"+ nameArr[index0] + " 客流：" + adjoin1 + "</p></div>"
			};
			
			var color = tab_comm.getFlowDegree(adjoin0 + adjoin1);
			tab_comm.drawLine(lineLpath, color, extData);
		}
	 },
	 
	 getFlowValue : function (data, areaId){
		 for(var i=0; i<data.length; i++){
			 if(areaId == data[i].CODE){
				 return data[i].FLOW
			 }
		 }
		 return 0;
	 }
	 
};