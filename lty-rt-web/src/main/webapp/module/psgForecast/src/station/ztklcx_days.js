define([ 'text!psgForecast/tpl/station/ztklcx_days.html' ], function(tpl) {
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
		selectSationName:'所有',
		folwDegrees:null,
        folwDegree:null,
		data:null,
		show : function() {
			$('#ztklfx_form').html(tpl);

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

			//
			$('#yztselect').change(function() {
				self.selectSationName = $("#yztselect").select2("data")[0].text;
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


		},
		//预测
		forecast : function() {
			//数据校验
			var platforms = $.trim($('#yztselect').val());
			var begin_date = moment($('#begin_date').val()).startOf('month').format('YYYY-MM-DD');
			var end_date = moment($('#end_date').val()).endOf('month').format('YYYY-MM-DD');
			var yc_date = $('#yc_date').val();
			var yc_month2 = $('#yc_date2').val();
			var forecastType =  $('input[name=forecastType]:checked').val();
			var mon_differ_sum;//月份差总和
			(function(){//限制预测月份差不超过12个月
				var yeat_differ=(yc_month2.substring(0,4)-yc_date.substring(0,4))*12;//年份差转换为月份差
				var mon_differ=yc_month2.substring(5)-yc_date.substring(5);//月份差
					mon_differ_sum=yeat_differ+mon_differ;//月份差总和
			})();
			if (mon_differ_sum>12) {//判断预测月份差不超过12个月
				comm.alert_tip('预测的月份不能超过12个月！');
				return false;
			}
			if (comm.isEmpty($('#begin_date').val()) || comm.isEmpty($('#end_date').val())) {
				comm.alert_tip('请选择分析时段！');
				return false;
			}
			if (comm.isEmpty(yc_date)) {
				comm.alert_tip('请选择预测的月份！');
				return false;
			}
			if (comm.isEmpty(yc_month2)) {
				comm.alert_tip('请选择预测的月份！');
				return false;
			}
			if (moment(end_date).isAfter(moment(yc_date))) {
				comm.alert_tip('预测时间应在分析时段之后');
				return false;
			}
			if(comm.isNotEmpty(yc_month2)){
				if(moment(yc_date).isAfter(moment(yc_month2))){
					comm.alert_tip('预测月份请选择正确！');
					return false;
				}
			}
			var ycMonthArr = [yc_date,yc_month2];
			self.ycMonth = ycMonthArr;
			var params = {};
			params.beginDate = begin_date;
			params.endDate = end_date;
			params.yc_month = ycMonthArr;
			params.platformID =  platforms.split('=')[0] ;
			params.type = forecastType;

			comm.requestJson('/report/stationForecast/forecast', JSON.stringify(params), function(
					resp) {
				if(resp.resCode =='200'){
					self.updateBarCharts(resp.data.monthData);
					self.updateCharts(resp.data.daysData);
					self.initTable(resp.data.daysData);
					self.data = resp.data.daysData;
					self.drawMonthStation(resp.data.stationsDataList);
				}else{
					comm.alert_tip('预测失败：'+resp.msg);
				}
			});
		},
		
		//查询月份站台数据
		drawMonthStation : function(stationsData){
			self.map.clearMap();
			if(stationsData != null && stationsData != '' && stationsData.length>0){
				for(var i=0; i<stationsData.length; i++){
					var flowValue = stationsData[i].flowValue[0].TOTAL_PERSON_COUNT;
					if(flowValue < 0){
						flowValue = 0 - flowValue;
					}
					
					var levelData = self.getFlowDegree(flowValue);
					var size = levelData[1];
					var color = levelData[0];
	                var circle = new AMap.Circle({
	                    radius: size,
	                    center: [stationsData[i].longitude, stationsData[i].latitude],
	                    strokeColor: color,
	                    strokeOpacity: 1,
	                    strokeWeight: 3,
	                    fillColor: color,
	                    fillOpacity: 0.8,
	                    map: self.map
	                });

	                var template = "<div class='passengerFlowInfo'><h2 class='passengerFlowInfo_title'>站台名:" + stationsData[i].name + "</h2><p class='passengerFlowInfo_p'>客流:" + flowValue + "</p></div>";
	                circle.content = template;
	                circle.on("click", self.passengerFlowClick);
				}
			}
		},
		
		//站台下拉框
		initSelect : function() {
			comm.initPlatSelect('#yztselect',true);
			/*comm.request('/report/template/listPlatform', null, function(resp) {
				var defaultOption = '<option value=" ">全部</option>';
				comm.initSelectOptionForObj('yztselect', resp.data, 'name',
						'id',defaultOption);
			});*/
		},

		//初始化Map
		initMap : function(data) {
			/*$("#ztklfxCharts").width($('#index_padd').width());*/
			 self.infoWindow = new AMap.InfoWindow({
                isCustom: false,
                // 使用自定义窗体
                offset: new AMap.Pixel(0, -15) // -113, -140
            });
			self.map = new AMap.Map('ztklfxCharts', {
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
			
			var title = self.selectSationName+'('+self.ycMonth+' 月份)客流走势';
			
			self.myChart.setOption({
				title : {
					text : title
				},
				xAxis : {
					data : xAxisData
				},
				
				grid: { // 控制图的大小，调整下面这些值就可以，
			             x: 0,
			             x2: 0,
			             y2: 0,// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
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
			/*$("#ztklycCharts").width($('#index_padd').width());*/
			self.myChart = echarts.init(document
					.getElementById('ztklycCharts'));
			
			//折线图每个点，绑定点击事件
			self.myChart.on('click', function (params) {
                if(params.componentType=="series"){
                	var name = params.name;
                	self.forecastStation(name);
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
					top:'90%',
					start : 0, // 左边在 10% 的位置。
					end : 100
				// 右边在 60% 的位置。
				}, { // 这个dataZoom组件，也控制x轴。
					type : 'inside', // 这个 dataZoom 组件是 inside 型
					// dataZoom 组件
					top:'90%',
					start : 0, // 左边在 10% 的位置。
					end : 100
				// 右边在 60% 的位置。
				} ],
				grid : {
					left : '0%',
					right : '0%',
					top : '20%',
//					bottom : '30%',
					getHeight:300,
//					getWidth:500,
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
					data : [],
					axisLabel:{
                          interval:'auto'

						}
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
					if('所有'==self.selectSationName){
						TOTAL_PERSON_COUNT = 128205;
					}else{
						TOTAL_PERSON_COUNT = 1200;
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
					.getElementById('ztklycBarCharts'));
			
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
					data : [],
				    "axisLabel":{  
		                 interval: 0  
		            } 
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
					title:'站台',
					data : "DAYS",
					render: function(data, type, row, meta) {
			            var platforms = self.selectSationName;
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
		forecastStation : function(day) {
			day = day.substring(8);
			var platforms = $.trim($('#yztselect').val());
			var begin_date = moment($('#begin_date').val()).startOf('month').format('YYYY-MM-DD');
			var end_date = moment($('#end_date').val()).endOf('month').format('YYYY-MM-DD');
			var params = {};
			params.day = day;
			params.platformID =  platforms ;
			params.beginDate = begin_date;
			params.endDate = end_date;

			comm.requestJson('/report/stationForecast/forecastOneDayData', JSON.stringify(params), function(
					resp) {
				self.map.clearMap();
				var data = resp.data;
				var stretchColor = '#00b7ff';
        		if(comm.isNotEmpty(self.folwDegree)){
        			stretchColor = self.folwDegree[0].CIRCLECOLOR;
            	}
	            for (var i = 0; i < data.length; i++) {
	            	var total_person_count = data[i].TOTAL_PERSON_COUNT;
	            	var ret = self.getFlowDegree(total_person_count);
	            	strokeColor = ret[0];
	            	var circlesize = ret[1];
	                var circle = new AMap.Circle({
	                    radius: circlesize,
	                    center: [data[i].LONGITUDE, data[i].LATITUDE],
	                    strokeColor: strokeColor,
	                    strokeOpacity: 1,
	                    strokeWeight: 3,
	                    fillColor: strokeColor,
	                    fillOpacity: 0.8,
	                    map: self.map
	                });

	                var template = "<div class='passengerFlowInfo'><h2 class='passengerFlowInfo_title'>站台名:" + data[i].NAME + "</h2><p class='passengerFlowInfo_p'>上车人数:" + data[i].ONBUS_PERSON_COUNT + "</p><p class='passengerFlowInfo_p'>下车人数:" + data[i].OFFBUS_PERSON_COUNT + "</p></div>";
	                circle.content = template;
	                circle.on("click", self.passengerFlowClick);
	            }
			});
		},
	    passengerFlowClick: function(e) {
            var content = e.target.content; //地图上所标点的坐标    
            self.infoWindow.setContent(content);
            self.infoWindow.open(self.map, e.target.getCenter());
        },
		//根据客流获取客流等级颜色值
        getFlowDegree : function(value){
        	if(value != null){
        		var ret = [];
        		for(var i=0; i<self.folwDegree.length; i++){
        			var max = self.folwDegree[i].maxdata;
        			var min = self.folwDegree[i].mindata;
        			if(value >= min && value < max){
        				ret.push(self.folwDegree[i].circlecolor);
        				ret.push(self.folwDegree[i].circlesize);
        			}
        		}
        		return ret;
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
            params.dataType = '005';
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
			titleName = ["站台","上车人数","下车人数"];
			for(var i = 0;i < self.data.length;i++){
				self.data[i].P_NAME =  self.selectSationName;
				self.data[i].DATE =  self.data[i].DAYS;
			}
			$("#img").val(img);
		    $("#tableData").val(JSON.stringify(self.data));
		    $("#fileName").val("公交站台客流预测");
		    $("#titleColumn").val(JSON.stringify(titleColumn));
		    $("#titleName").val(JSON.stringify(titleName));
		    $("#titleSize").val(JSON.stringify(titleSize));
		    $("#export_form").submit();
		},

	};
	return self;
});