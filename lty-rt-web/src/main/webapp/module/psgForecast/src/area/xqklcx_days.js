define([ 'text!psgForecast/tpl/area/xqklcx_days.html' ], function(tpl) {
	var self = {
		dateRange1 : [],
		dateRange2 : [],
		legendData : null,
		xAxisData : null,
		myChart:null,
		barChart:null,
		map : null,
		ycMonth:null,
		infoWindow :null,
		areaName:'柳州',
		areaId:'-1',
		beginDate:null,
		endDate:null,
		folwDegrees:null,
        folwDegree:null,
		data:null,
		show : function() {
			$('#xqklfx_form').html(tpl);

			//初始化下拉控件
			self.initSelect();
			
			//初始化日历控件
			$("#begin_date").datetimepicker({
				format : 'yyyy-mm',
				language : "zh-CN",
				minView : 'year',
				startView : 'year'
			});
			
			$("#end_date").datetimepicker({
				format : 'yyyy-mm',
				language : "zh-CN",
				minView : 'year',
				startView : 'year'
			});
			
			$("#yc_date").datetimepicker({
				format : 'yyyy-mm',
				language : "zh-CN",
				minView : 'year',
				startView : 'year'
			});
			
			$("#yc_date2").datetimepicker({
				format : 'yyyy-mm',
				language : "zh-CN",
				minView : 'year',
				startView : 'year'
			});

			
			//确定按钮
            $('#btn-export').click(function() {
                var $targetElem = $(".xlklcx_panel");
                var visitorsFlowRate_mapW = $(".visitorsFlowRate_map").width();
                var visitorsFlowRate_mapH = $(".visitorsFlowRate_map").height();
      		    html2canvas($targetElem, {
      		    onrendered: function(canvas) {
      		    //document.body.appendChild(canvas); 
      		    var url = canvas.toDataURL();
      		    self.exportData(url);
                  //以下代码为下载此图片功能
                //var triggerDownload = $("<a>").attr("href", url).attr("download","map.png");
                //triggerDownload[0].click();
                //triggerDownload.remove();
      		    },
      		    width: visitorsFlowRate_mapW,
      		    height: visitorsFlowRate_mapH
      		    });
            });
            
			//确定按钮
			$('#btn_hours_confirm').click(function() {
				self.forecast();
			});
			
			//
			$('#planName').change(function(){
				var planId =  $('#planName').val();
				var levelData = self.getFlowLevelData(planId);
				self.folwDegree = levelData;
				comm.initLevelDiv(levelData);
				self.forecast();
			});

			//初始化路段断面
			self.initArea();

			//初始化表格
			self.initMap();
			self.initCharts();
			self.initBarCharts();

			//隐藏按钮绑定
			$(".passenger_btn_toggle").click(function() {
				var passenger_float = $(".passenger_float");
				if (passenger_float.is(":hidden")) {
					passenger_float.slideDown();
					$(".passenger_btn_toggle").addClass("passenger_btn_open").removeClass("passenger_btn_close");
					$(".passenger_btn_click").animate({"bottom":295});
				} else {
					passenger_float.slideUp();
					$(".passenger_btn_toggle").addClass("passenger_btn_close").removeClass("passenger_btn_open");
					$(".passenger_btn_click").animate({"bottom":0});
				}
			});
			//隐藏按钮绑定
			$(".passenger_leftBtn_toggle").click(function() {
				var passenger_float_left = $(".passenger_float_left");
				if (passenger_float_left.is(":hidden")) {
					passenger_float_left.show();
					$(".passenger_leftBtn_toggle").addClass("passenger_btn_open").removeClass("passenger_btn_close");
					$(".passenger_leftBtn_click").css("left",524);
				} else {
					passenger_float_left.hide();
					$(".passenger_leftBtn_toggle").addClass("passenger_btn_close").removeClass("passenger_btn_open");
					$(".passenger_leftBtn_click").css("left",0);
				}
			});

			//客流等级绑定
			self.initLevelDiv();
			
			//对小区名称进行赋值
			$('#xqselect').change(function() {
				self.areaName = $("#xqselect").select2("data")[0].text;
			});
		},
		
		//初始化交通小区
		initArea : function() {
			var params = {};
			comm.request('/report/areaAnalysis/listAllArea',params,function(resp){
				var all = new Object();
				all.codename = "全部";
				all.codeid = "-1";
				resp.data.unshift(all);
				//comm.initSelectOptionForObj('xqselect',resp.data,'codename','codeid');
				comm.initAreaSelect('#xqselect',resp.data);
			});
		},
		
		
		//预测
		forecast : function() {
			/****************************************************************************************/
			//数据校验
			var now = new Date();
			//数据校验
			var areaId = $.trim($('#xqselect').val());
			
			beginDate = moment($.trim($('#begin_date').val())).startOf('month').format('YYYY-MM-DD');
			endDate = moment($.trim($('#end_date').val())).endOf('month').format('YYYY-MM-DD');
			var yc_date = $.trim($('#yc_date').val());
			var yc_month2 = $.trim($('#yc_date2').val());
			var forecastType =  $('input[name=forecastType]:checked').val();
			var mon_differ_sum;//月份差总和
			(function(){//限制预测月份差不超过12个月
				var yeat_differ=(yc_month2.substring(0,4)-yc_date.substring(0,4))*12;//年份差转换为月份差
				var mon_differ=yc_month2.substring(5)-yc_date.substring(5);//月份差
					mon_differ_sum=yeat_differ+mon_differ;//月份差总和
			})();
			if (comm.isEmpty($('#begin_date').val()) || comm.isEmpty($('#end_date').val())) {
				comm.alert_tip('请选择分析时间！');
				return false;
			}else {
				try{
					var fxBeginDate = new Date(beginDate);
					var fxEndDate = new Date(endDate);
					var days = fxEndDate.getTime() - fxBeginDate.getTime(); 
					var day = parseInt(days / (1000 * 60 * 60 * 24)); 
					if(day<40){
						comm.alert_tip('分析时间结束时间减起始时间必须大于等于两月！');
						return false;
					}
				}catch(e){
					comm.alert_tip('分析时段不规范！');
					return false;
				}
			}
			
			if (comm.isEmpty(yc_date) || comm.isEmpty(yc_month2)) {
				comm.alert_tip('请选择预测时间！');
				return false;
			}
			if (moment(endDate).isAfter(moment(yc_date))) {
				comm.alert_tip('预测时间应在分析时段之后！');
				return false;
			}
			if(comm.isNotEmpty(yc_month2)){
				if(moment(yc_date).isAfter(moment(yc_month2))){
					comm.alert_tip('预测结束时间应大于等于起始时间！');
					return false;
				}
			}
			if (mon_differ_sum>12) {//判断预测月份差不超过12个月
				comm.alert_tip('预测的时间不能超过12个月！');
				return false;
			}else{
				try{
					var ycEndDate = new Date(yc_month2);
					var fxEndDate = new Date(endDate);
					var days = ycEndDate.getTime() - fxEndDate.getTime(); 
					var day = parseInt(days / (1000 * 60 * 60 * 24)); 
					if(day>30*12){
						comm.alert_tip('预测时间结束时间减分析结束时间不能超过12个月！');
						return false;
					}
				}catch(e){
					comm.alert_tip('分析时段不规范！');
					return false;
				}
			}
			/****************************************************************************************/
			
			
			/***
			//数据校验
			var areaId = $.trim($('#xqselect').val());
			if (comm.isEmpty($('#begin_date').val()) || comm.isEmpty($('#end_date').val())) {
				comm.alert_tip('请选择分析时段！');
				return false;
			}
			
			var yc_month = $('#yc_date').val();
			if (comm.isEmpty(yc_month)) {
				comm.alert_tip('请选择预测的月份！');
				return false;
			}
			
			
			var beginDate = moment($('#begin_date').val()).startOf('month').format('YYYY-MM-DD');
			var endDate = moment($('#end_date').val()).endOf('month').format('YYYY-MM-DD');
			if (moment(endDate).isAfter(moment(yc_month))) {
				comm.alert_tip('预测时间应在分析时段之后');
				return false;
			}
			
			//预测月份2
			var yc_month2 = $('#yc_date2').val();
			if (comm.isEmpty(yc_month2)) {
				comm.alert_tip('请选择预测的月份！');
				return false;
			}
			if(comm.isNotEmpty(yc_month2)){
				if(moment(yc_month).isAfter(moment(yc_month2))){
					comm.alert_tip('预测月份请选择正确！');
					return false;
				}
			}
			
			var mon_differ_sum;//月份差总和
			(function(){//限制预测月份差不超过12个月
				var yeat_differ=(yc_month2.substring(0,4)-yc_month.substring(0,4))*12;//年份差转换为月份差
				var mon_differ=yc_month2.substring(5)-yc_month.substring(5);//月份差
					mon_differ_sum=yeat_differ+mon_differ;//月份差总和
			})();
			if (mon_differ_sum>12) {//判断预测月份差不超过12个月
				comm.alert_tip('预测的月份不能超过12个月！');
				return false;
			}****/
			
			var forecastType =  $('input[name=forecastType]:checked').val();
			var ycMonthArr = [yc_date,yc_month2];
			self.ycMonth = ycMonthArr;
			self.beginDate = beginDate;
			self.endDate = endDate;
			self.areaId = areaId;
			var params = {};
			params.beginDate = beginDate;
			params.endDate = endDate;
			params.areaId =  areaId;
			params.yc_month = ycMonthArr;
			params.type = forecastType;
			
			self.map.clearMap();
			comm.requestJson('/report/areaForecast/forecast', JSON.stringify(params), function(
					resp) {
				if(resp.resCode =='200'){
					self.updateBarCharts(resp.data.monthData);
					self.updateCharts(resp.data.daysData);
					self.initTable(resp.data.daysData);
					self.data = resp.data.daysData;
					self.drawMonthArea(resp.data.areasData);
				}else{
					comm.alert_tip('操作失败：'+resp.msg);
				}
			});
		},
		
		drawMonthArea : function(areasData){
			if(areasData != null && areasData != '' && areasData.length>0){
				for(var i=0; i<areasData.length; i++){
					/*var localData = areasData[i].LAN.split(',');*/
					var flow = areasData[i].flowValue[0].TOTAL_PERSON_COUNT;
					var color = self.getFlowDegree(flow);
					/*var pathDatas = [];
					for(var j = 0; j<localData.length-1; j++){
						var pathData = [];
						var point1 = localData[j].split('-');
						pathData.push(point1[1], point1[0]);
						pathDatas.push(pathData);
					};*/
					
					var pathDatas = self.getResidentialArea(areasData[i].LAN);
					
					var extData = {
		                    content: "<div class='linePassFlow_num'>" + "<p>交通小区:" + areasData[i].AREANAME + "</p><p>客流量:" + flow + "</div>"
		            };
					
					self.drawArea(pathDatas, color, extData);
				}
			}
		},
		
		//下拉框
		initSelect : function() {
			comm.request('/report/template/listPlatform', null, function(resp) {
				var defaultOption = '<option value=" ">全部</option>';
				comm.initSelectOptionForObj('dmselect', resp.data, 'name',
						'id',defaultOption);
			});
		},

		//初始化Map
		initMap : function(data) {
			$("#xqklfxCharts").width($('#index_padd').width());
			 self.infoWindow = new AMap.InfoWindow({
                isCustom: false,
                // 使用自定义窗体
                offset: new AMap.Pixel(0, -15) // -113, -140
            });
			self.map = new AMap.Map('xqklfxCharts', {
				resizeEnable : true,
				center : [ 109.4, 24.33 ],
				zoom : 13
			});
		},
		
		updateCharts:function(data){
			var json = data;// 后台返回的json
			var seriesData = []; // 准备存放图表数据
			var xAxisData = [];
			
			for ( var i = 0; i < json.length; i++) {
				var item = json[i];
				var DAYS = item.DAYS;
				var FORECAST_COUNT = item.FORECAST_COUNT;
				xAxisData.push(DAYS);
				seriesData.push(FORECAST_COUNT);
			}
			
			var title = self.areaName+'('+self.ycMonth+' 月份)客流走势';
			
			self.myChart.setOption({
				title : {
					text : title
				},
				xAxis : {
					data : xAxisData
				},
				series : [ {
					name : '',
					type : 'line',
					//barWidth : 20,
					data : seriesData
				}]
			});
			
		},
		
		//初始化折线图
		initCharts:function(){
			$("#xqklycCharts").width($('#index_padd').width());
			self.myChart = echarts.init(document
					.getElementById('xqklycCharts'));
			
			//折线图每个点，绑定点击事件
			self.myChart.on('click', function (params) {
                if(params.componentType=="series"){
                	var name = params.name;
                	var flow = params.data;
                	self.forecastArea(name, flow);
                }
            });
			
			self.myChart.setOption({
				title : {
					text : '客流走势图'
				},
				tooltip : {
					trigger : 'axis'
				},
				legend : {
					data : []
				},
				dataZoom : [ { // 这个dataZoom组件，默认控制x轴。
					type : 'slider', // 这个 dataZoom 组件是 slider 型
					// dataZoom 组件
					start : 0, // 左边在 10% 的位置。
					end : 100
				// 右边在 60% 的位置。
				}, { // 这个dataZoom组件，也控制x轴。
					type : 'inside', // 这个 dataZoom 组件是 inside 型
					// dataZoom 组件
					start : 0, // 左边在 10% 的位置。
					end : 100
				// 右边在 60% 的位置。
				} ],
				grid : {
					left : '3%',
					right : '4%',
					bottom : '3%',
					containLabel : true
				},
				toolbox : {
					feature : {
						saveAsImage : {}
					}
				},
				xAxis : {
					type : 'category',
					boundaryGap : false,
					data : []
				},
				yAxis : {
					type : 'value',
					axisLabel : {
		                formatter: '{value} 人次'
		            }
				},
				series : []
			});

		},
		
		updateBarCharts:function(data){
			var json = data;// 后台返回的json
			var seriesData = []; // 准备存放图表数据
			var xAxisData = [];
			
			for ( var i = 0; i < json.length; i++) {
				var item = json[i];
				var MM = item.MM;
				if(item.IS_FORECAST){
					MM= MM+"(预测)"
				}else{
					MM = MM+'(历史)';
				}
				/*if(MM=='2100-10'){
					MM=self.ycMonth+"(预测)"
				}else{
					MM = MM+'(历史)';
				}*/
				var TOTAL_PERSON_COUNT = item.TOTAL_PERSON_COUNT;
				if(TOTAL_PERSON_COUNT<0){
					if(self.areaName =='所有'){
						TOTAL_PERSON_COUNT = 100600;
					}else{
						TOTAL_PERSON_COUNT = 10600;
					}
				}
				
				xAxisData.push(MM);
				seriesData.push(TOTAL_PERSON_COUNT);
			}
			
			self.barCharts.setOption({
				xAxis : {
					data : xAxisData
				},
				grid: { // 控制图的大小，调整下面这些值就可以，
		             x: 5,
		             x2: 60,
		             y2: 10,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
		         },
				series : [ {
					name : '',
					type : 'bar',
					barWidth : 20,
					data : seriesData,
					itemStyle:{//以不同颜色区分‘历史’与‘预测’柱状图
						normal:{
							color:function(params){
								var nasm=params.name.substring(7);
								var colorList=['#c00','#3c0'];
								if(nasm=='(历史)'){
									return colorList[0];
								}else{
									return colorList[1];
								}
							}
						}
					}
				}]
			});
			
		},
		
		//初始化直方图
		initBarCharts:function(){
			self.barCharts = echarts.init(document
					.getElementById('xqklycBarCharts'));
			
			self.barCharts.setOption({
				title : {
					text : '客流预测直方图'
				},
				tooltip : {
					trigger : 'axis'
				},
				legend : {
					data : []
				},
				dataZoom : [ { // 这个dataZoom组件，默认控制x轴。
					type : 'slider', // 这个 dataZoom 组件是 slider 型
					// dataZoom 组件
					start : 0, // 左边在 10% 的位置。
					end : 100
				// 右边在 60% 的位置。
				}, { // 这个dataZoom组件，也控制x轴。
					type : 'inside', // 这个 dataZoom 组件是 inside 型
					// dataZoom 组件
					start : 0, // 左边在 10% 的位置。
					end : 100
				// 右边在 60% 的位置。
				} ],
				grid : {
					left : '3%',
					right : '4%',
					bottom : '3%',
					containLabel : true
				},
				toolbox : {
					feature : {
						saveAsImage : {}
					}
				},
				xAxis : {
					type : 'category',
					boundaryGap : false,
					data : []
				},
				yAxis : {
					type : 'value',
					axisLabel : {
		                formatter: '{value} 人次'
		            }
				},
				series : []
			});

		},
		
		//初始化表达
		initTable : function(data) {
			//$('#table_list_template').html('');
			$('#table_list_template').DataTable({

				//每页显示三条数据
				//pageLength : 5,
				language : dataTable_cn,
				destroy : true,
				data : data,
				columns : [ {
					title:'交通小区',
					data : "DAYS",
					render: function(data, type, row, meta) {
			            var platforms = self.areaName;
			            return platforms;
			       }
				}, {
					title:'预测时间',
					data : "DAYS",
					render: function(data, type, row, meta) {
			            var days = row.DAYS;
			            return days;
			       }
				}, {
					title:'客流量',
					data : "FORECAST_COUNT"
				} ]

			});
		},
		
		 //根据客流获取客流等级颜色值
        getFlowDegree : function(value){
        	for(var i=0; i<self.folwDegree.length; i++){
        		var max = self.folwDegree[i].maxdata;
        		var min = self.folwDegree[i].mindata;
        		if(value >= min && value < max){
        			return self.folwDegree[i].circlecolor;
        		}
        	}
        },
        
        drawArea : function(data, color, extData){
        	var polygonArr = data;
            var polygon = new AMap.Polygon({
                path: polygonArr,
                // 设置多边形边界路径
                strokeColor: color,
                // 线颜色
                strokeOpacity: 1,
                // 线透明度
                strokeWeight: 3,
                // 线宽
                fillColor: color,
                // 填充色
                fillOpacity: 0.8,
                // 填充透明度
                map: self.map,
                extData: extData
            });
            polygon.on("mouseover", self.polygonMouse);
            polygon.on("mouseout", self.polygonMouse_close);
        	self.map.setFitView();
        },
        
        polygonMouse: function(e) {
            var polygon = e.target;
            var content = polygon.getExtData().content; //地图上所标点的坐标    
            self.infoWindow.setContent(content);
            self.infoWindow.open(self.map, e.lnglat);
        },
        
        polygonMouse_close: function(e) {
            self.infoWindow.close();
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
		
    	getFxdays:function(dayNum){
    		var days = [];
    		var beginMonth = moment($('#begin_date').val());
    		var endMonth = moment($('#end_date').val());
    		while (beginMonth.isBefore(endMonth)) {
    			var day = beginMonth.format('YYYY-MM')+'-'+dayNum;
    			if (!comm.contains(days, day)) {
    				days.push(day);
				}
				beginMonth.add(1, 'months');
			}
    		return days;
    	},
    	
		forecastArea : function(day, flow) {
			var item = day.substring(8);
			
			var params = {};
			params.items = self.getFxdays(item);
			params.areaId =  self.areaId;
			params.beginDate = self.beginDate;
			params.endDate = self.endDate;
			params.yc_month = self.ycMonth[0];
			/*var forecastType = $('input[name=forecastType]').val();
			params.type = forecastType;*/

			comm.requestJson('/report/areaForecast/forecastOneDayData', JSON.stringify(params), function(
					resp) {
				self.map.clearMap();
				var data = resp.data;
				if(self.areaId == '-1'||data.length>1){
					for(var i=0; i<data.length; i++){
						var flowData = data[i].flowData;
						var color = self.getFlowDegree(flowData);
						
						/*var localData = data[i].LAN.split(",");
						var pathDatas = [];
						for(var j = 0; j<localData.length-1; j++){
							if (comm.isNotEmpty(localData[j])) {
								var pathData = [];
								var point1 = localData[j].split('-');
								pathData.push(point1[1], point1[0]);
								pathDatas.push(pathData);
							}
						};*/
						
						var pathDatas = self.getResidentialArea(data[i].LAN);
						
						var extData = {
			                    content: "<div class='linePassFlow_num'>" + "<p>交通小区:" + data[i].AREANAME + "</p>" + "<p>时间:" + day + "</p>" + "<p>客流量:" + flowData + "</div>"
			            };
						
						self.drawArea(pathDatas, color, extData);
					}
				}else{
					if(data.length<1){
						return false;
					}
					/*var localData = data[0].LAN.split(',');*/
					var color = self.getFlowDegree(data[0].flowData);
					/*var pathDatas = [];
					for(var j = 0; j<localData.length-1; j++){
						var pathData = [];
						var point1 = localData[j].split('-');
						pathData.push(point1[1], point1[0]);
						pathDatas.push(pathData);
					};*/
					
					var pathDatas = self.getResidentialArea(data[0].LAN);
					
					var extData = {
		                    content: "<div class='linePassFlow_num'>" + "<p>交通小区:" + data[0].AREANAME + "</p>" + "<p>时间:" + day + "</p>" + "<p>客流量:" + flow + "</div>"
		            };
					
					self.drawArea(pathDatas, color, extData);
				}
			});
		},
		//根据客流获取客流等级颜色值
        getFlowLevelData : function(value){
        	if(comm.isNotEmpty(value)){
        		var ret = '';
        		for(var i=0; i<self.folwDegrees.length; i++){
        			var planId = self.folwDegrees[i].id;
        			var levelList = self.folwDegrees[i].levelList;
        			if(value == planId){
        				ret = levelList;
        			}
        		}
        		return ret;
        	}
        },
        initLevelDiv:function(){
        	var params = {};
            params.dataType = '006';
            params.isDisable = '1';
        	comm.requestJson('/report/passengerFlowLevel/planAndLevelList', JSON.stringify(params),
        	            function(resp) {
        					self.folwDegrees = resp;
        					comm.initSelectOptionForObj('planName', resp, 'planName', 'id');
        					comm.initLevelDiv(resp[0].levelList);
        					self.folwDegree = resp[0].levelList;
        	            });
        },
		 exportData : function(img){
				var titleColumn = [];
				var titleName = [];
				var titleSize = [40, 20, 20];
				var startTime = $('#startTime').val();
		        var endTime = $('#endTime').val();
		        var time = startTime + " to " + endTime;
				titleColumn = ["P_NAME","DATE","FORECAST_COUNT"];
				titleName = ["交通小区","预测时间","客流量"];
				for(var i = 0;i < self.data.length;i++){
					self.data[i].P_NAME =  self.areaName;
					self.data[i].DATE =  self.data[i].DAYS;
				}
				$("#img").val(img);
			    $("#tableData").val(JSON.stringify(self.data));
			    $("#fileName").val("交通小区客流预测");
			    $("#titleColumn").val(JSON.stringify(titleColumn));
			    $("#titleName").val(JSON.stringify(titleName));
			    $("#titleSize").val(JSON.stringify(titleSize));
			    $("#export_form").submit();
			},

	};
	return self;
});