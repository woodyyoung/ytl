define([ 'text!psgForecast/tpl/line/xlklcx_days.html' ], function(tpl) {
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
		lineName:'所有',
		lineId:'-1',
		beginDate:null,
		endDate:null,
		lineData:null,
		lineDataMap:null,
		folwDegrees:null,
        folwDegree:null,
		yc_date:null,
		data:null,
		show : function() {
			$('#xlklfx_form').html(tpl);

			//初始化下拉控件
			self.initLine();
			
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
					$(".passenger_leftBtn_click").css("left",523);
				} else {
					passenger_float_left.hide();
					$(".passenger_leftBtn_toggle").addClass("passenger_btn_close").removeClass("passenger_btn_open");
					$(".passenger_leftBtn_click").css("left",0);
				}
			});

			//客流等级绑定
			self.initLevelDiv();

			//对线路名称进行赋值
			$('#xlselect').change(function() {
				self.lineName = $("#xlselect").select2("data")[0].text;
			});
		},
		//初始化线路
		initLine : function() {
			var params = {};
			comm.request('/report/lineAnalysis/listAllLine',params,function(resp){
				var all = new Object();
				all.name = "全部";
				all.id = "-1";
				resp.data.unshift(all);
				comm.initSelectOptionForObj('xlselect',resp.data,'name','id');
			});
		},
		
		//预测
		forecast : function() {
			//数据校验
			var now = new Date();
			lineId = $.trim($('#xlselect').val());
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
		
			var ycMonthArr = [yc_date,yc_month2];
			self.ycMonth = ycMonthArr;
			var params = {};
			params.beginDate = beginDate;
			params.endDate = endDate;
			params.yc_month = ycMonthArr;
			params.lineId =  lineId;
			params.type = forecastType;
			
			self.map.clearMap();

			comm.requestJson('/report/lineForecast/forecast', JSON.stringify(params), function(
					resp) {
				if(resp.resCode =='200'){
					self.lineData = resp.data.lineData;
					self.lineDataMap = resp.data.lineDataMap;
					self.updateBarCharts(resp.data.monthData);
					self.updateCharts(resp.data.daysData);
					self.initTable(resp.data.daysData);
					self.data = resp.data.daysData;
					self.drawMonthLine(resp.data.linesData);
				}else{
					comm.alert_tip('预测失败：'+resp.msg);
				}
			});
		},
		
		//地图画月份线路
		drawMonthLine : function (linesData){
			if(linesData != null && linesData != '' && linesData.length>0){
				for(var i=0; i<linesData.length; i++){
					var flowValue = linesData[i].flowValue[0].TOTAL_PERSON_COUNT;
					var line = linesData[i].lineId;
					if(lineId == '-1'){
						var lineName = linesData[i].lineName;
						var points = self.lineDataMap[line].split('|');
						
						var extData = {
			                    content: "<div class='linePassFlow_num'><p>线路:" + lineName + "</p><p>客流量:" + flowValue + "</div>"
			            };
						var color = self.getFlowDegree(flowValue);
						for(var j=0; j<points.length-1; j++){
							var lineLpath = [];
			                lineLpath.push([points[j].split(',')[1], points[j].split(',')[0]], [points[j+1].split(',')[1], points[j+1].split(',')[0]]);
			                self.drawLine(lineLpath, color, extData);
					    }
					}else{
						var points = self.lineData.split('|');
						self.map.clearMap();
						var extData = {
			                    content: "<div class='linePassFlow_num'>" + "<p>线路:" + self.lineName + "</p><p>客流量:" + flowValue + "</div>"
			            };
						var color = self.getFlowDegree(flowValue);
						for(var i=0; i<points.length-1; i++){
							var lineLpath = [];
			                lineLpath.push([points[i].split(',')[1], points[i].split(',')[0]], [points[i+1].split(',')[1], points[i+1].split(',')[0]]);
			                self.drawLine(lineLpath, color, extData);
						}
					}
				}
			}
		},
		
		//下拉框
		initSelect : function() {
			comm.request('/report/template/listPlatform', null, function(resp) {
				var defaultOption = '<option value=" ">全部</option>';
				comm.initSelectOptionForObj('xlselect', resp.data, 'name',
						'id',defaultOption);
			});
		},

		//初始化Map
		initMap : function(data) {
			/*$("#xlklfxCharts").width($('#index_padd').width());*/
			 self.infoWindow = new AMap.InfoWindow({
                isCustom: false,
                // 使用自定义窗体
                offset: new AMap.Pixel(0, -15) // -113, -140
            });
			self.map = new AMap.Map('xlklfxCharts', {
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
			
			var title = self.lineName+'('+self.ycMonth+' 月份)客流走势';
			
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
			$("#xlklycCharts").width($('#index_padd').width());
			self.myChart = echarts.init(document
					.getElementById('xlklycCharts'));
			
			//折线图每个点，绑定点击事件
			self.myChart.on('click', function (params) {
                if(params.componentType=="series"){
                	var name = params.name;
                	var value = params.data;
                	self.forecastLine(name, value);
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
				var TOTAL_PERSON_COUNT = item.TOTAL_PERSON_COUNT;
				if(TOTAL_PERSON_COUNT<0){
					if(self.lineName =='所有'){
						TOTAL_PERSON_COUNT = 100900;
					}else{
						TOTAL_PERSON_COUNT = 10800;
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
					.getElementById('xlklycBarCharts'));
			
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
					title:'线路',
					data : "DAYS",
					render: function(data, type, row, meta) {
			            var lineName = self.lineName;
			            return lineName;
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
		
		drawLine: function(lineLpath, strokeColor, extData) {
            var busPolyline = new AMap.Polyline({
                map: self.map,
                path: lineLpath,
                strokeColor: strokeColor,
                strokeOpacity: 0.8,
                strokeWeight: 6,
                extData:extData
            });
            busPolyline.on("mouseover",self.polygonMouse);
            busPolyline.on("mouseout",self.polygonMouse_close);
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
		
		//点击图标节点，渲染地图区域
		forecastLine : function(day, value) {
			var item = day.substring(8);
			var params = {};
			params.items = self.getFxdays(item);
			params.lineId =  lineId;
			params.beginDate = beginDate;
			params.endDate = endDate;
			
			//根据线路类型判断，如果为全部就另外处理
			if(lineId == '-1'){
				comm.requestJson('/report/lineForecast/forecastOneDayData', JSON.stringify(params), function(
						resp) {
					self.map.clearMap();
					var data = resp.data;
					for(var j=0; j<data.length; j++){
						var lineId = data[j].lineId;
						var lineName = data[j].lineName;
						var value = data[j].value;
						var points = self.lineDataMap[lineId].split('|');
						
						var extData = {
			                    content: "<div class='linePassFlow_num'>" + "<p>线路:" + lineName + "</p>" + "<p>时间:" + day + "</p>" + "<p>客流量:" + value + "</div>"
			            };
						var color = self.getFlowDegree(value);
						for(var i=0; i<points.length-1; i++){
							var lineLpath = [];
			                lineLpath.push([points[i].split(',')[1], points[i].split(',')[0]], [points[i+1].split(',')[1], points[i+1].split(',')[0]]);
			                self.drawLine(lineLpath, color, extData);
						}
					}
				});
			}else {
				var points = self.lineData.split('|');
				self.map.clearMap();
				var extData = {
	                    content: "<div class='linePassFlow_num'>" + "<p>线路:" + self.lineName + "</p>" + "<p>时间:" + day + "</p>" + "<p>客流量:" + value + "</div>"
	            };
				var color = self.getFlowDegree(value);
				for(var i=0; i<points.length-1; i++){
					var lineLpath = [];
	                lineLpath.push([points[i].split(',')[1], points[i].split(',')[0]], [points[i+1].split(',')[1], points[i+1].split(',')[0]]);
	                self.drawLine(lineLpath, color, extData);
				}
			}
			
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
            params.dataType = '011';
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
				titleColumn = ["P_NAME","DATE","FORECAST_COUNT"];
				titleName = ["线路","预测时间","客流量"];
				for(var i = 0;i < self.data.length;i++){
					self.data[i].P_NAME =  self.lineName;
					self.data[i].DATE =  self.data[i].DAYS;
				}
				$("#img").val(img);
			    $("#tableData").val(JSON.stringify(self.data));
			    $("#fileName").val("线路客流影响预测");
			    $("#titleColumn").val(JSON.stringify(titleColumn));
			    $("#titleName").val(JSON.stringify(titleName));
			    $("#titleSize").val(JSON.stringify(titleSize));
			    $("#export_form").submit();
			},

	};
	return self;
});
