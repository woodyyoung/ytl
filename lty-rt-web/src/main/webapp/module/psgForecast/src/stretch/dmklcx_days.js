define([ 'text!psgForecast/tpl/stretch/dmklcx_days.html' ], function(tpl) {
	var self = {
		dateRange1 : [],
		dateRange2 : [],
		legendData : null,
		xAxisData : null,
		myChart:null,
		barChart:null,
		map : null,
		infoWindow :null,
		stretchName:'所有',
		stretchId:'-1',
		beginDate:null,
		endDate:null,
		yc_month:null,
		folwDegrees:null,
        folwDegree:null,
		data:null,
		show : function() {
			$('#dmklfx_form').html(tpl);

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
			self.initStretch();

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
			
			//对路段名称进行赋值
			$('#dmselect').change(function() {
				self.stretchName = $("#dmselect").select2("data")[0].text;
			});
		},
		
		//初始化断面
		initStretch : function() {
			var params = {};
			comm.request('/report/stretchAnalysis/listAllStretch',params,function(resp){
				var all = new Object();
				all.linename = "全部";
				all.lineid = "-1";
				resp.data.unshift(all);
				comm.initSelectOptionForObj('dmselect',resp.data,'linename','lineid');
			});
		},
		
		
		//预测
		forecast : function() {
			//数据校验
			var stretchId = $.trim($('#dmselect').val());
			
			var yc_month = $('#yc_date').val();
			var yc_month2 = $('#yc_date2').val();
			var forecastType =  $('input[name=forecastType]:checked').val();
			
			var mon_differ_sum;//月份差总和
			(function(){//限制预测月份差不超过12个月
				var yeat_differ=(yc_month2.substring(0,4)-yc_month.substring(0,4))*12;//年份差转换为月份差
				var mon_differ=yc_month2.substring(5)-yc_month.substring(5);//月份差
					mon_differ_sum=yeat_differ+mon_differ;//月份差总和
			})();
			if (mon_differ_sum>12) {//判断预测月份差不超过12个月
				comm.alert_tip('预测的月份不能超过12个月！');
				return false;
			}
			if (comm.isEmpty($('#yc_date').val()) || comm.isEmpty($('#yc_date2').val())) {
				comm.alert_tip('请选择分析时段！');
				return false;
			}
			if (comm.isEmpty(yc_month)) {
				comm.alert_tip('请选择预测的月份！');
				return false;
			}
			
			if (comm.isEmpty(yc_month2)) {
				comm.alert_tip('请选择预测的月份！');
				return false;
			}
			 if (($('#yc_date').val()) >= ($('#yc_date2').val())) {
	              alert('请选择正确日期范围 ');
					return false;
				}
			var beginDate = moment($('#begin_date').val()).startOf('month').format('YYYY-MM-DD');
			var endDate = moment($('#end_date').val()).endOf('month').format('YYYY-MM-DD');
			
			if (moment(endDate).isAfter(moment(yc_month))) {
				comm.alert_tip('预测时间应在分析时段之后');
				return false;
			}
			if(comm.isNotEmpty(yc_month2)){
				if(moment(yc_month).isAfter(moment(yc_month2))){
					comm.alert_tip('预测月份请选择正确！');
					return false;
				}
			}
			var ycMonthArr = [yc_month,yc_month2];
			self.yc_month = ycMonthArr;
			self.beginDate = beginDate;
			self.endDate = endDate;
			self.stretchId = stretchId;
			var params = {};
			params.beginDate = beginDate;
			params.endDate = endDate;
			params.yc_month = ycMonthArr;
			params.stretchId =  stretchId;
			params.type = forecastType;

			self.map.clearMap();
			
			comm.requestJson('/report/stretchForecast/forecast', JSON.stringify(params), function(
					resp) {
				if(resp.resCode =='200'){
					self.updateBarCharts(resp.data.monthData);
					self.updateCharts(resp.data.daysData);
					self.initTable(resp.data.daysData);
					self.data = resp.data.daysData;
					self.drawMonthStretch(resp.data.stretchsData);
				}else{
					comm.alert_tip('预测失败：'+resp.msg);
				}
			});
		},
		
		drawMonthStretch : function(stretchsData){
			if(stretchsData != null && stretchsData != '' && stretchsData.length>0){
				for(var i=0; i<stretchsData.length; i++){
					var localData = stretchsData[i].LAN.split(',');
					var flow = 0;
					if(stretchsData[i].flowValue!=null&&stretchsData[i].flowValue.length>0){
						flow = stretchsData[i].flowValue[0].TOTAL_PERSON_COUNT;
					}
					var color = self.getFlowDegree(flow);
					
					var extData = {
		                    content: "<div class='linePassFlow_num'>" + "<p>路段断面:" + stretchsData[i].LINENAME + "</p><p>客流量:" + flow + "</div>"
		            };
					
					for(var j = 0; j<localData.length-1; j++){			            	  
						var pathData=[];
						var point1 = localData[j].split('-');
						var point2 = localData[j+1].split('-');
						pathData.push([point1[1], point1[0]], [point2[1], point2[0]]);
		    			self.drawStretch(pathData, color, extData);
					};
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
			$("#dmklfxCharts").width($('#index_padd').width());
			 self.infoWindow = new AMap.InfoWindow({
                isCustom: false,
                // 使用自定义窗体
                offset: new AMap.Pixel(0, -15) // -113, -140
            });
			self.map = new AMap.Map('dmklfxCharts', {
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
			
			var title = self.stretchName+'('+self.yc_month+' 月份)客流走势';
			
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
			$("#dmklycCharts").width($('#index_padd').width());
			self.myChart = echarts.init(document
					.getElementById('dmklycCharts'));
			
			//折线图每个点，绑定点击事件
			self.myChart.on('click', function (params) {
                if(params.componentType=="series"){
                	var name = params.name;
                	var flow = params.data;
                	self.forecastStretch(name, flow);
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
			if(comm.isEmpty(data)){
				self.initBarCharts();
				return false;
			}
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
					if(self.stretchName =='所有'){
						TOTAL_PERSON_COUNT = 10500;
					}else{
						TOTAL_PERSON_COUNT = 1600;
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
					.getElementById('dmklycBarCharts'));
			
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
			$('#table_list_template').DataTable({

				//每页显示三条数据
				//pageLength : 5,
				language : dataTable_cn,
				destroy : true,
				data : data,
				columns : [ {
					title:'路段断面',
					data : "DAYS",
					render: function(data, type, row, meta) {
			            var platforms = self.stretchName;
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
        
        drawStretch : function(data, color, extData){
        	var busPolyline = new AMap.Polyline({
	            map: self.map,
	            path: data,
	            strokeColor: color,
	            strokeOpacity: 0.8,
	            strokeWeight: 6,
	            extData : extData
		    });
        	busPolyline.on("mouseover", self.polygonMouse);
            busPolyline.on("mouseout", self.polygonMouse_close);
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
		
		forecastStretch : function(day, flow) {
			var item = day.substring(8);
			var params = {};
			params.items = self.getFxdays(item);
			//params.item = item;
			params.stretchId =  self.stretchId ;
			params.beginDate = self.beginDate;
			params.endDate = self.endDate;
			//params.yc_month = self.yc_month;
			

			comm.requestJson('/report/stretchForecast/forecastOneDayData', JSON.stringify(params), function(
					resp) {
				
				self.map.clearMap();
				var data = resp.data;
				if(self.stretchId == '-1'){
					for(var i=0; i<data.length; i++){
						var localData = data[i].LAN.split(',');
						var flowData = data[i].flowData;
						var color = self.getFlowDegree(flowData);
						var stretchName = data[i].LINENAME;
						
						var extData = {
			                    content: "<div class='linePassFlow_num'>" + "<p>路段断面:" + stretchName + "</p>" + "<p>时间:" + day + "</p>" + "<p>客流量:" + flowData + "</div>"
			            };
						
						for(var j = 0; j<localData.length-1; j++){			            	  
							var pathData=[];
							var point1 = localData[j].split('-');
							var point2 = localData[j+1].split('-');
							pathData.push([point1[1], point1[0]], [point2[1], point2[0]]);
			    			self.drawStretch(pathData, color, extData);
						};
					}
				}else{
					var localData = data[0].LAN.split(',');
					var color = self.getFlowDegree(flow);
					
					var extData = {
		                    content: "<div class='linePassFlow_num'>" + "<p>路段断面:" + self.stretchName + "</p>" + "<p>时间:" + day + "</p>" + "<p>客流量:" + flow + "</div>"
		            };
					
					for(var j = 0; j<localData.length-1; j++){			            	  
						var pathData=[];
						var point1 = localData[j].split('-');
						var point2 = localData[j+1].split('-');
						pathData.push([point1[1], point1[0]], [point2[1], point2[0]]);
		    			self.drawStretch(pathData, color, extData);
					};
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
            params.dataType = '007';
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
				self.data[i].P_NAME =  self.stretchName;
				self.data[i].DATE =  self.data[i].DAYS;
			}
			$("#img").val(img);
		    $("#tableData").val(JSON.stringify(self.data));
		    $("#fileName").val("路段断面客流预测");
		    $("#titleColumn").val(JSON.stringify(titleColumn));
		    $("#titleName").val(JSON.stringify(titleName));
		    $("#titleSize").val(JSON.stringify(titleSize));
		    $("#export_form").submit();
		},
	};
	return self;
});