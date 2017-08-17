define(
		[ 'text!busDecisionAnalysis/tpl/xwyhfzfx/auxiliaryLineNetwork/fzxw.html' ],
		function(tpl) {
			var self = {
				isLineSelected:true,
				lineEditor:null,
				lineEditorArray:null,
				contextMenu:null,
				polyline:null,
				polylineArray:null,
				treeView: null,
				lineId: null,
				timeFlowChart: null,
				stationFlowChart: null,
				selectDay: null,
				mapObj: null,
				markerArr: null,
				infoWindow: null,
				stations: null,
				stationTab: null,
				paltfromTab: null,
				platform: null,
				isEdit: false,
				schemeId: null,
				lineStations:null,
				mapStations: [],
				platformIds: '',
				allPlatForm:null,
				deleteLine:null,
				folwDegrees:null,
		        folwDegree:null,
				show : function(title) {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					
					//初始化左树
					self.loadLineTree();
					
					//初始化日历控件
					$("#queryDate").datetimepicker({
						format : 'yyyy-mm-dd',
						language : "zh-CN",
						minView : 'month'
					}); 
					$("#queryDate").val(moment().subtract(1, 'days').format('YYYY-MM-DD'));
					 
					//隐藏按钮绑定
					var passenger_float_H = $(".passenger_float").height();
					$(".passenger_btn_click").css({"bottom":passenger_float_H});
					$(".passenger_btn_toggle").click(function() {						
						var passenger_float = $(".passenger_float");
						if (passenger_float.is(":hidden")) {
							passenger_float.slideDown();
							$(".passenger_btn_toggle").addClass("passenger_btn_open").removeClass("passenger_btn_close");
							$(".passenger_btn_click").animate({"bottom":passenger_float_H});
						} else {
							passenger_float.slideUp();
							$(".passenger_btn_toggle").addClass("passenger_btn_close").removeClass("passenger_btn_open");
							$(".passenger_btn_click").animate({"bottom":0});
						}
					});

					self.initMap();
					self.initTimeFlowChart();
					self.initStationFlowChart();
					//self.line();
					self.removeLastLine();
					//self.delectLast();
					//self.lineReset();
					self.saveScheme();
					self.initLevelDiv();
					
					//确定按钮
					$('#queryFlowData').click(function() {
						self.queryFlowData();
					});
					
					//
					$('#planName').change(function(){
						var planId =  $('#planName').val();
						var levelData = self.getFlowLevelData(planId);
						self.folwDegree = levelData;
						comm.initLevelDiv(levelData);
						self.queryFlowData();
					});

					
					//新增方案
					$("#addScheme").click(function(){
						if(!self.isLineSelected){
							comm.alert_tip('请选择线路！');
							return false;
						}
						if(self.lineId == null){
							comm.alert_tip('请选择线路！');
							return false;
						}
						$('#inlineCheckbox3').attr('checked',false);
						self.isEdit = true;
						$("#schemeBtnGroup").show();
						$(".fzxwMap_tool").show();
						self.addLineScheme();
					});
					
					//添加站点
					$("#addStation").click(function(){
						if(!comm.isEmpty(self.lineId)){
							comm.requestJson('/report/auxiliaryLine/queryAllStation', null, function(resp) {
								self.platform = resp.data.stations;
								self.loadPaltfromTab(self.platform);
				            });
						}
					});
					
					//删除方案
					$("#delScheme").click(function (){
						comm.alert_confirm('确定要删除此方案吗?',function(){
							if(comm.isEmpty(self.schemeId)){
								comm.alert_tip('请选择方案！');
								return false;
							}else{
								var params = {
					                "schemeId": self.schemeId
						        };
								comm.requestJson('/report/auxiliaryLine/delLineScheme', JSON.stringify(params), function(resp) {
									self.loadLineTree();
									self.mapObj.clearMap();
									self.initTimeFlowChart();
									self.initStationFlowChart();
					            });
							}
						});
					});
					
					//删除站点
					$("#deleteStation").click(function(){
						if(!comm.isEmpty(self.lineId)){
							var params = {
				                "lineId": self.lineId
					        };
							
							self.loadStationTab(self.mapStations);
							/*comm.requestJson('/report/auxiliaryLine/queryStationData', JSON.stringify(params), function(resp) {
								var data = resp.data;
								self.stations = data.stations;
								
				            });*/
						}
					});
					
					$("#addStationData").click(function (){
						self.addPaltfrom();
					});
					
					$("#delStationData").click(function (){
						self.delStation();
					});
					
					$("#schemeBtnGroup").hide();
					$(".fzxwMap_tool").hide();
					$("#validateName").hide();
					
					
					//显示全部站台 
					$('#inlineCheckbox3').change(function(){
						var checkFlag = $('#inlineCheckbox3').is(':checked');
						if(checkFlag){
							self.loadAllPlatformData();
						}else{
							self.hidePlatForm();
						}
					});
					
				},
				
		        // 加载客流等级
		        initLevelDiv:function(){
		        	var params = {};
		            params.dataType = '012';
		            params.isDisable = '1';
		        	comm.requestJson('/report/passengerFlowLevel/planAndLevelList', JSON.stringify(params),
        	            function(resp) {
        					//self.folwDegrees = resp;
        					comm.initSelectOptionForObj('planName', resp, 'planName', 'id');
        					comm.initLevelDiv(resp[0].levelList);
        					self.folwDegree = resp[0].levelList;
        	            });
		        },
		        
		        //根据客流获取客流等级颜色值
		        getFlowDegree : function(value){
		        	if(comm.isNotEmpty(value)){
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
				//加载所有站台数据
				loadAllPlatformData :function(){
					var queryDate = $('#queryDate').val();
					if(comm.isEmpty(queryDate)){
						comm.alert_tip('请选择日期！');
						return false;
					}
					var params = {
			                "lineId": self.lineId,
			                "date": queryDate
				    };
					comm.requestJson('/report/auxiliaryLine/queryAllPlatformForAuxiliaryLine', JSON.stringify(params), function(resp) {
			            if(resp.resCode == 200){
			            	self.allPlatForm = resp.data.platforms;
			            	self.drawPlatform(resp.data.platforms);
			            }else{
			            	alert(resp.msg);
			            }
					},function(resp){
						alert("调用失败");
					}); 
				},
				
				//初始化站台表格
				loadPaltfromTab : function(data) {
					self.paltfromTab = $('#paltfromTab').DataTable({
						destroy: true,
						data: data,
						order: [[ 1, "desc" ]],
						language:dataTable_cn,
						columns : [
						 {
	                      data: null,
	                      defaultContent: '',
	                      className: 'select-checkbox',
	                      orderable: false
	                    },
						{
							title:'站点名称',
							data : 'name'
						},
						{
							title:'经度',
							data : 'longitude'
						},
						{
							title:'纬度',
							data : 'latitude'
						}
						],
		                select: {
		                    style:    'os',
		                    selector: 'td:first-child'
		                }
					});
				},
				
				addPaltfrom : function() {  
					var selectedDatas = self.paltfromTab.rows('.selected').data();
					var tempStation = [];
					if(selectedDatas != null && selectedDatas.length > 0){
						for(var k = 0; k < selectedDatas.length; k++){
							if(self.platformIds.indexOf(selectedDatas[k].id) < 0){
								var mapStation = {
										platformId : selectedDatas[k].id,
										longitude : selectedDatas[k].longitude, 
										latitude : selectedDatas[k].latitude,
										name : selectedDatas[k].name
								};
								self.platformIds += selectedDatas[k].id + ",";
								tempStation.push(mapStation);
								self.mapStations.push(mapStation);
							}
						}
						
						if(tempStation.length > 0){
							$("#platformCloseBtn").click();
							self.drawStation(tempStation);
						}else{
							$("#platformCloseBtn").click();
						}
				    }
				},
				
				//初始化站点表格
				loadStationTab : function(data) {
					self.stationTab = $('#stationTab').DataTable({
						destroy: true,
						data: data,
						order: [[ 1, "desc" ]],
						language:dataTable_cn,
						columns : [
						 {
	                      data: null,
	                      defaultContent: '',
	                      className: 'select-checkbox',
	                      orderable: false
	                    },
						{
							title:'站点名称',
							data : 'name'
						},
						{
							title:'经度',
							data : 'longitude'
						},
						{
							title:'纬度',
							data : 'latitude'
						}
						],
		                select: {
		                    style:    'os',
		                    selector: 'td:first-child'
		                }
					});
				},
				
				delStation : function() {  
					var selectedDatas = self.stationTab.rows('.selected').data();
					var index = [];
					if(selectedDatas != null && selectedDatas.length > 0){
						for(var k = 0; k < selectedDatas.length; k++){
							for(var i=0; i<self.mapStations.length; i++){
								if(self.mapStations[i].platformId == selectedDatas[k].platformId){
									index.push(i);
								}
							}
						}
						
						if(index.length > 0){
							for(var i=0; i<index.length; i++){
							/*	
								for(var j=0;j<self.lineStations.length;j++){
									var obj = self.lineStations[j];
									if(self.mapStations[i].platformId == obj.id){
										obj.circle.setMap(null);
									}
								}*/
								self.mapStations.splice(index[i], 1);
							}
							
							$("#stationCloseBtn").click();
							//self.mapObj.clearMap();
							//self.drawStation(self.mapStations);
						}
				    }
				},
				
				//新增方案
				addLineScheme : function() {
					
						$('#queryFlowData').click();
					
						/*var params = {
			                "lineId": self.lineId
				        };
						
						comm.requestJson('/report/auxiliaryLine/queryStationData', JSON.stringify(params), function(resp) {
							var data = resp.data;
							self.stations = data.stations;
							for(var i=0; i<self.stations.length; i++){
								
								var mapStation = {
									platformId : self.stations[i].id,
									longitude : self.stations[i].longitude, 
									latitude : self.stations[i].latitude,
									name : self.stations[i].name
								};
								self.platformIds += self.stations[i].id + ",";
								self.mapStations.push(mapStation);
							}
							self.mapObj.clearMap();
							self.drawStation(self.stations);
							self.loadLine(data.lpath);
			            });*/
				},
				
				
				//加载线路
				loadLine : function(data){
					var points = data.split("|");
					var lineLpath = [];
					for(var i=0; i<points.length-1; i++){
						var point1 = points[i].split(",");
						var point2 = points[i+1].split(",");
						if(comm.isNotEmpty(point1[1])&&comm.isNotEmpty(point1[0])&&comm.isNotEmpty(point2[0])&&comm.isNotEmpty(point2[1])){
			                lineLpath.push([point1[1], point1[0]], [point2[1], point2[0]]);
						}
					}
				    var color = "#881111";
				    self.polylineArray = [];
		            self.drawLine(lineLpath, color, "");
		            self.bindEditLine();
				},
				
				//查询线路客流数据
				queryFlowData : function () {
					var queryDate = $('#queryDate').val();
					if(comm.isEmpty(queryDate)){
						comm.alert_tip('请选择日期！');
						return false;
					}
					
					if(comm.isNotEmpty(self.schemeId)){
						self.querySchemeFlowData();
						return false;
					}
					if(comm.isEmpty(self.lineId)){
						comm.alert_tip('请选择线路！');
						return false;
					}
					
					
					self.selectDay = queryDate;
					
					var params = {
		                "dataType": "H",
		                "lineId": self.lineId,
		                "holidayFlag": "",
		                "queryDate": queryDate
			        };
					
					comm.requestJson('/report/auxiliaryLine/getTimeFlow', JSON.stringify(params), function(resp) {
	            		var lineData = resp.data.lineData;
	            		self.loadTimeFlowChart(lineData);
	            		self.loadStationFlowChart(queryDate, 1);
		            });
					
					params = {
			                "lineId": self.lineId,
			                "date": queryDate
				    };
					comm.requestJson('/report/auxiliaryLine/queryLineIndexData', JSON.stringify(params), function(resp) {
						self.fillLineIndexData(resp.data);
		            });
					
					self.drawLineByPsgFlow();
					
				},
				
				/**
				 * 用客流等级画线路
				 */
				drawLineByPsgFlow: function() {
				    var params = {};
		            var lineId =  self.lineId;
		            var startTime =  $('#queryDate').val();
		            params = {
		                "dataType": "H",
		                "lineId": lineId,
		                "holidayFlag": '',
		                "aloneDay": startTime,
		                "startTime": startTime
		            }
		            comm.requestJson('/report/lineAnalysis/getLinePsgFlowDataList', JSON.stringify(params),
		            function(resp) {
		            	try {
		            		if(resp.stationsAndLinePath.linePath){
		            			var linePath = resp.stationsAndLinePath.linePath;
		            			self.initLineMap(linePath);
		            		}
		            		if(resp.stationsAndLinePath.stationData){
		            			var stationMap = resp.stationsAndLinePath.stationData;
		            			self.initStationMap(stationMap);
		            		}
						} catch (e) {
							
						}
		            });
			   },
			   
			  initStationMap: function(data) {
		           /* self.markerArr = [];
		            for (var i = 0; i < data.length; i++) {
		                var marker;
		                var icon = null;
		                var div = null;
		                div = document.createElement('div');
		                div.className = 'circle';
		                div.innerHTML = i;
		                marker = new AMap.Marker({
		                    content: div,
		                    position: [data[i][1], data[i][0]],
		                    title: data[i][3]+":" + i,
		                    map: self.mapObj,
		                    icon: icon,
		                    zIndex: 10
		                });
		                marker.content = data[i][2];
		                marker.on('mouseover', self.markerClick); 
		                self.markerArr.push(marker);
		                self.mapObj.setFitView();
		                var zoom = self.mapObj.getZoom();
		                if (zoom < 13) {
		                    marker.hide();
		                };
		            };*/
		            
		            self.lineStations = data;
			    	for (var i = 0; i < data.length; i++) {
	            	    var color = '#0b0bf1';
	            	   
		                var circle = new AMap.Circle({
		                    radius: 20,
		                    center: [data[i][1],  data[i][0]],
		                    strokeColor: color,
		                    strokeWeight: 1,
		                    fillColor: color,
		                    fillOpacity: 0.5,
		                    zIndex:98,
		                    map: self.mapObj
		                });

		                var template = "<div class='passengerFlowInfo'><h2 class='passengerFlowInfo_title'>" + data[i][3] +":"+data[i][2]+"</h2></div>";
		                circle.content = template;
		                circle.on("click", self.passengerFlowClick);
		                circle.on("mouseover", self.passengerFlowClick);
		                circle.on("mouseout", self.busPolylineMouse_close);
		                self.lineStations[i].circle = circle;
		            }
		            
		       },
		       
			  
			   
			   initLineMap: function(data) {
		        	self.mapObj.clearMap();
		        	var lastColor = '#00b7ff';
		        	if(comm.isNotEmpty(self.folwDegree)){
		        		lastColor = self.folwDegree[0].CIRCLECOLOR;
		        	}
		        	var lastCount = 0;
		        	self.polylineArray = [];
		            for (var j = 0; j < data.length - 1; j++) {
		                var lineLpath = [];
		                lineLpath.push([data[j][1], data[j][0]], [data[j + 1][1], data[j + 1][0]]);
		                var carNum = data[j][2];
		                var stroke = data[j][3];
		                var strokeColor = '';
		                var extData = {content:''};
		                if(stroke == '0'){
		                	strokeColor = lastColor;
		                }else{
		                	strokeColor = self.getFlowDegree(carNum)[0];
		                	lastCount = carNum;
		                }
		            	extData={
		            			content: "<div class='linePassFlow_num'>客流量:"+lastCount+"</div>"
		            	}
		                lastColor = strokeColor;
		                self.drawLine(lineLpath, strokeColor, extData);
		            };
		            
		            self.bindEditLine();
		        },
				
				//查询方案客流数据
				querySchemeFlowData : function () {
					
					var queryDate = $('#queryDate').val();
					if(comm.isEmpty(queryDate)){
						comm.alert_tip('请选择日期！');
						return false;
					}
					
					self.selectDay = queryDate;
					
					var params = {
							"psgType": "1",
							"schemeId": self.schemeId,
							 "lineId": self.lineId,
							"date": queryDate
					};
					
					//更新线路基本信息
					comm.requestJson('/report/auxiliaryLine/queryLineStationData', JSON.stringify(params), function(resp) {
						var data = resp.data;
						self.fillLineBasicInfo(data);
		            });
					
					//更新线路指标信息
					comm.requestJson('/report/auxiliaryLine/queryLineIndexData', JSON.stringify(params), function(resp) {
						self.fillLineIndexData(resp.data);
		            });
					
					comm.requestJson('/report/auxiliaryLine/querySchemePsgFlowData', JSON.stringify(params), function(resp) {
						var schemePsgFlowData = resp.data.schemePsgFlowData;
						//self.loadTimeFlowChart(schemePsgFlowData);
						//self.updateStationFlowChart(resp.data.stationPsgFlowData);
						self.fillSchemeIndexData(resp.data);
					});
					
					
					
				},
				
				//填充线路基本信息
				fillLineBasicInfo:function(data){
					$("#busNum").html(data.busNum);
					$("#lineLength").html(data.lineLength);
					$("#direction").html(data.direction==0?'上行':'下行');
					$("#stationSpan").html(data.stationSpan);
					$("#stationNum").html(data.stationNum);
					$("#firstTime").html(data.startAndEndPlanRunime.STARTRUNTIME);
					$("#lastTime").html(data.startAndEndPlanRunime.ENDRUNTIME);
				},
				
				//填充方案指标数据
				fillSchemeIndexData: function(data){
					var max_fillrate = data.fillRates==null?'':data.fillRates.MAX_FILLRATE +"%";
					var avg_fillrate = data.fillRates==null?'':data.fillRates.AVG_FILLRATE +"%";
					var totalPsg = data.totalPsg==null?'':data.totalPsg.TOTAL_PERSON_COUNT;
					var stationNum = data.stations.length;
					var stationSpan = parseFloat($('#stationSpan').text()) + parseFloat($('#stationSpan').text()) * 0.1;
					
					$('#scheme_max_fillrate').text(max_fillrate);
					$('#scheme_avg_fillrate').text(avg_fillrate);
					$('#scheme_total_psg').text(totalPsg);
					$('#scheme_stationNum').text(stationNum);
					$('#scheme_stationSpan').text(stationSpan);
					
				},
				
				//清空方案指标数据
				cleanSchemeIndexData: function(){
					$('#scheme_max_fillrate').text('');
					$('#scheme_avg_fillrate').text('');
					$('#scheme_total_psg').text('');
					$('#scheme_stationNum').text('');
					$('#scheme_stationSpan').text('');
				},
				
				
				//填充线路指标数据
				fillLineIndexData: function(data){
					var max_fillrate = data.fillRates==null?'':data.fillRates.MAX_FILLRATE +"%";
					var avg_fillrate = data.fillRates==null?'':data.fillRates.AVG_FILLRATE +"%";
					var totalPsg = data.totalPsg==null?'':data.totalPsg.BUS_PERSON_COUNT;
					$('#max_fillrate').text(max_fillrate);
					$('#avg_fillrate').text(avg_fillrate);
					$('#total_psg').text(totalPsg);
				},
				
				//清空方案指标数据
				cleanLineIndexData: function(){
					$('#max_fillrate').text('');
					$('#avg_fillrate').text('');
					$('#total_psg').text('');
					$("#busNum").html('');
					$("#lineLength").html('');
					$("#direction").html('');
					$("#stationSpan").html('');
					$("#stationNum").html('');
				},
				

				//加载时间客流
				loadTimeFlowChart: function(data) {
		            var json = data;// 后台返回的json
					var seriesData = []; // 准备存放图表数据
					var xAxisData = [];
					
					for ( var i = 0; i < json.length; i++) {
						var item = json[i];
						var time = item.HH;
						var TOTAL_PERSON_COUNT = item.TOTAL_PERSON_COUNT;
						xAxisData.push(time);
						seriesData.push(TOTAL_PERSON_COUNT);
					}
					
					self.timeFlowChart.setOption({
						title : {
							text : self.selectDay+'客流'
						},
						xAxis : {
							data : xAxisData
						},
						series : [ {
							name : '',
							type : 'line',
							data : seriesData
						}]
					});
		        },
				
				//加载线路树
				loadLineTree : function() {
					comm.request('/report/auxiliaryLine/loadLineTree', null, function(resp){
						var data = resp.data.lineTree;
						self.treeView = $('#treeview-searchable').treeview({
							collapsed:true,
							levels: 1,
				            showBorder: false,
				            showTags: true,
				            data: data
						});
						
						self.bindSelectEvent();
						$('#treeview-searchable').treeview('expandAll', { levels: 1, silent: true });
					});
				},
				
				//线路树绑定选择事件
				bindSelectEvent : function () {
					//左树点击事件
					$("#treeview-searchable").on("nodeSelected", function(event, data){
						var arg = data.arg;
						self.mapObj.clearMap();
						$('.fzxw_radio').show();
						$("#stationFlowChart").show();
						$("#timeFlowChart").show();	
						$(".passenger_btn_click").show();
						$(".linePassengerFlow_sign").show();
						self.initTimeFlowChart();
						self.initStationFlowChart();
						
						if(arg == 'line'){
							self.lineId = data.id;
							self.schemeId = null;
							self.isLineSelected = true,
							self.clickLineNodeAction(self.lineId);
						}else if(arg == 'scheme'){
							self.isLineSelected = false,
							self.schemeId = data.id;
							self.lineId = null;
							self.clickSchemeNodeAction(self.schemeId);
						}else{
							self.lineId = null;
							self.schemeId = null;
						}
						
						self.mapStations = [];
						self.platformIds = '';
						$("#schemeBtnGroup").hide();
						$(".fzxwMap_tool").hide();
					});
					
					$("#treeview-searchable").on("nodeUnselected", function(event, data){
						var arg = data.arg;
						self.mapObj.clearMap();
						self.initTimeFlowChart();
						self.initStationFlowChart();
						
						if(arg == 'line'){
							//self.lineId = data.id;
							self.lineId = null;
						}else if(arg == 'scheme'){
							//self.schemeId = data.id;
							self.schemeId = null;
						}else{
							self.lineId = null;
							self.schemeId = null;
						}
						
						self.mapStations = [];
						self.platformIds = '';
						$("#schemeBtnGroup").hide();
						$(".fzxwMap_tool").hide();
					});
					
				},
				
				//点击节点动作
				clickLineNodeAction : function(lineId) {
					//清空线路信息
					self.cleanLineIndexData();
					//清空方案指标数据
					self.cleanSchemeIndexData();
					
					var params = {
		                "lineId": lineId
			        };
					
					comm.requestJson('/report/auxiliaryLine/queryLineStationData', JSON.stringify(params), function(resp) {
						self.mapStations = [];
						var data = resp.data;
						self.fillLineBasicInfo(data);
						
						/*var lpath = data.lpath;
						self.loadLine(lpath);
						
						var stations = data.stations;
						self.drawStation(stations);*/
						
						self.stations = data.stations;
						for(var i=0; i<self.stations.length; i++){
							
							var mapStation = {
								platformId : self.stations[i].id,
								longitude : self.stations[i].longitude, 
								latitude : self.stations[i].latitude,
								name : self.stations[i].name
							};
							self.platformIds += self.stations[i].id + ",";
							self.mapStations.push(mapStation);
						}
						
						$('#queryFlowData').click();
		            });
				},
				
				//点击方案动作
				clickSchemeNodeAction : function (data) {
					$("#stationFlowChart").hide();
					$("#timeFlowChart").hide();	
					$(".passenger_btn_click").hide();
					$(".linePassengerFlow_sign").hide();
					$('.fzxw_radio').hide();
					
					var params = {
						id : data
					};
					comm.requestJson('/report/auxiliaryLine/querySchemeData', JSON.stringify(params), function(resp){
						var schemeId = resp.data.schemeId;
						var schemeName = resp.data.schemeName;
						var lineId = resp.data.lineId;
						self.lineId = lineId;
						var mapData = resp.data.mapData;
						var stations = resp.data.stations;
						self.mapObj.clearMap();
						self.loadSchemeLine(mapData);
						self.drawSchemeStation(stations);
						self.cleanSchemeIndexData();
						
						$('#queryFlowData').click();
						
					});
				},
				
				//加载方案线路
				loadSchemeLine:function(data){
					var points = data.split("|");
					var lineLpath = [];
					for(var i=0; i<points.length-1; i++){
						var point1 = points[i].split(",");
						var point2 = points[i+1].split(",");
						if(comm.isNotEmpty(point1[1])&&comm.isNotEmpty(point1[0])&&comm.isNotEmpty(point2[0])&&comm.isNotEmpty(point2[1])){
			                lineLpath.push([point1[1], point1[0]], [point2[1], point2[0]]);
						}
					}
				    var color = "#881111";
		            var polyline = new AMap.Polyline({
		                map: self.mapObj,
		                path: lineLpath,
		                strokeColor: color,
		                strokeOpacity: 1,
		                strokeWeight: 6,
		                extData:''
		            });
		            self.mapObj.setFitView();
				},
				
				
			     drawSchemeStation: function(data) {
			    	for (var i = 0; i < data.length; i++) {
	            	    var color = '#0b0bf1';
		                var circle = new AMap.Circle({
		                    radius: 20,
		                    center: [data[i].longitude, data[i].latitude],
		                    strokeColor: color,
		                    strokeWeight: 1,
		                    fillColor: color,
		                    fillOpacity: 0.5,
		                    zIndex:98,
		                    map: self.mapObj
		                });

		                var template = "<div class='passengerFlowInfo'><h2 class='passengerFlowInfo_title'>" + data[i].name + "</h2></div>";
		                circle.content = template;
		                circle.on("click", self.passengerFlowClick);
		                circle.on("mouseover", self.passengerFlowClick);
		                circle.on("mouseout", self.busPolylineMouse_close);
		            }
		         },
				
				
				
				//初始化Map
				initMap : function() {
					self.mapObj = new AMap.Map('ztklfxCharts', {
						resizeEnable : true,
						center : [ 109.4, 24.33 ],
						zoom : 13
					});
					
					self.infoWindow = new AMap.InfoWindow({
		                isCustom: false,
		                // 使用自定义窗体
		                offset: new AMap.Pixel(0, -15) // -113, -140
		            });
					
					
					self.contextMenu = new AMap.ContextMenu(); 
					self.contextMenu.addItem("删除此段", function(e) {
						self.deleteLine.setPath(null);
						self.deleteLine.setMap(null);
						
					    }, 0);
				},
				
				
				//编辑折线
				bindEditLine:function(){
					self.lineEditorArray = [];
					for(var i = 0;i<self.polylineArray.length;i++){
						var lineEditor  = new AMap.PolyEditor(self.mapObj, self.polylineArray[i]);
						self.lineEditorArray.push(lineEditor);
					}
					
					AMap.event.addDomListener(document.getElementById('startEditLine'), 'click', function(){
						for(var i = 0;i<self.lineEditorArray.length;i++){
							self.lineEditorArray[i].open();
						}
						
				    }, false);
				    AMap.event.addDomListener(document.getElementById('endEditLine'), 'click', function(){
				    	for(var i = 0;i<self.lineEditorArray.length;i++){
							self.lineEditorArray[i].close();
						}
				    });
				},

				
				//撤销
				removeLastLine : function(){
					var overlays=self.mapObj.getAllOverlays();
					if(overlays.length>0){
						var a=overlays[overlays.length-1];
						var b=a.getPath();
						b.pop();
						a.setPath(b);
						if(b.length == 0){
							self.mapObj.remove(overlays[overlays.length-1]);
						}
					}
				},


				//保存方案
				saveScheme : function(){
					$("#saveScheme").click(function(){
						var schemeName = $("#schemeName").val();
						if(comm.isEmpty(schemeName)){
							$("#validateName").show();
							return false;
						}
						var mapData = "";
						for(var i= 0;i<self.polylineArray.length;i++){
							var linePath = self.polylineArray[i].getPath();
							for(var j=0;j<linePath.length;j++){
								mapData += linePath[j].lat + "," + linePath[j].lng+ "|";
								//mapData += linePath[j][0] + "," + linePath[j][1] + "|";
							}
							
						}
						
					/*	var overlays = self.mapObj.getAllOverlays();
						var linePath = null;
						for(var i=0;i<overlays.length;i++){
							var a=overlays[i];
							if(a.CLASS_NAME=="AMap.Polyline"){
							   linePath = overlays[i].getPath();
							   break;
						    }
						}
						var mapData = "";
						if(linePath != null){
							for(var i=0; i<linePath.length; i++){
								linePath[i].lat;
								linePath[i].lng;
								mapData += linePath[i].lat + "," + linePath[i].lng + "|";
							}
							
							mapData = mapData.substring(0, mapData.length-1);
						}
						*/
						for(var i=0;i<self.mapStations.length;i++){
							var data = self.mapStations[i];
							data.circle = null;
						}
						
						var params = {
							lineId : self.lineId,
							schemeName : schemeName,
							mapData : mapData,
							mapStations : self.mapStations
						};
						
						comm.requestJson('/report/auxiliaryLine/saveLineScheme', JSON.stringify(params), function(resp) {
							$("#closeScheme").click();
							$("#schemeBtnGroup").show();
							$(".fzxwMap_tool").show();
							self.loadLineTree();
							self.mapObj.clearMap();
							self.initTimeFlowChart();
							self.initStationFlowChart();
						});
					});
				},

				//初始化折线图
				initTimeFlowChart: function(){
					self.timeFlowChart = echarts.init(document
							.getElementById('timeFlowChart'));
					self.timeFlowChart.setOption({
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
							top:'80%',
							start : 0, // 左边在 10% 的位置。
							end : 100
						// 右边在 60% 的位置。
						}, { // 这个dataZoom组件，也控制x轴。
							type : 'inside', // 这个 dataZoom 组件是 inside 型
							// dataZoom 组件
							start : 0, // 左边在 10% 的位置。
							top:'80%',
							end : 100
						// 右边在 60% 的位置。
						} ],
						grid : {
							left : '3%',
							right : '4%',
							bottom : '10%',
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
					
					/*self.timeFlowChart.on('click', function (params) {
		                if(params.componentType=="series"){
		                	var time = params.name;
		                	self.loadStationFlowChart(time,0);
		                }
		            });*/
				},
				
				loadStationFlowChart : function(time,dataType) {
					var params = {
                        "dataType": dataType,
                        "lineId": self.lineId,
                        "holidayFlag": '',
                        "aloneHour" : time,
                        "aloneDay" :  self.selectDay
	                };
					
					comm.requestJson('/report/auxiliaryLine/getStationLineFlow', JSON.stringify(params), function(resp) {
						var json = resp.data.stationData;// 后台返回的json
						self.updateStationFlowChart(json);
					});
				},
				
				updateStationFlowChart:function(json){
					var seriesData = []; // 准备存放图表数据
					var xAxisData = [];
					
					for ( var i = 0; i < json.length; i++) {
						var item = json[i];
						var stationName = item.NAME;
						var TOTAL_PERSON_COUNT = item.TOTAL_PERSON_COUNT;
						xAxisData.push(stationName);
						seriesData.push(TOTAL_PERSON_COUNT);
					}
					
					self.stationFlowChart.setOption({
		                title: {
		                    text: '站点客流'
		                },
		                legend: {
		                    data: ['站点客流']
		                },
		                xAxis: {
		                    data: xAxisData
		                },
		                series: [{
		                    name: '客流',
		                    type: 'bar',
							barWidth:10,
		                    data: seriesData
		                }]
		            });
				},
								
				//初始化直方图
				initStationFlowChart: function(){
					self.stationFlowChart = echarts.init(document.getElementById('stationFlowChart'));
					
					self.stationFlowChart.setOption({
						title : {
							text : '站台客流直方图',
							y:'bottom',
							x: 'right' ,
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
							top:'40%',
							start : 10, // 左边在 10% 的位置。
							end : 90
						// 右边在 60% 的位置。
						}, { // 这个dataZoom组件，也控制x轴。
							type : 'inside', // 这个 dataZoom 组件是 inside 型
							// dataZoom 组件
							start : 0, // 左边在 10% 的位置。
							end : 100
						// 右边在 60% 的位置。
						} ],
						grid : {
							y:10,
							y2:140,
							left : '0%',
							right : '13%',
							
							bottom : '40%',
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
							axisLabel: {
		                        interval: 0,
		                        margin:6,
//		                        dataMin : 10, 
//		                        dataMax : 10, 
		                        rotate: 50,
		                        textStyle:{
		                           fontSize:5
		                       }
		                    },
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
				
				/**
				 * 汇
				 */
				drawLine: function(lineLpath, strokeColor, extData) {
		            var polyline = new AMap.Polyline({
		                map: self.mapObj,
		                path: lineLpath,
		                strokeColor: strokeColor,
		                strokeOpacity: 1,
		                strokeWeight: 6,
		                extData:extData
		            });
		            polyline.on("mouseover", self.busPolylineMouse);
		            polyline.on("mouseout", self.busPolylineMouse_close);
		            polyline.on("rightclick", self.ploylineRightClick);
		            polyline.on("dblclick", self.ploylineRightClick);
		            self.mapObj.setFitView();
		            self.polylineArray.push(polyline);
		        },
		        
		        ploylineRightClick:function(e){
		        	var busPolyline=e.target;
		        	self.deleteLine = busPolyline;
		        	self.contextMenu.open(self.mapObj, e.lnglat);
		        	
		        	/*self.contextMenu.open(self.mapObj, e.lnglat);
		               contextMenuPositon = e.lnglat;
		        	alert('aaaa');*/
		        },
		        
		        busPolylineMouse:function(e){
		        	try {
		        		var busPolyline=e.target;
		        		var content = busPolyline.getExtData().content;//地图上所标点的坐标    
		        		self.infoWindow.setContent(content);
		        		self.infoWindow.open(self.mapObj, e.lnglat);      
					} catch (e) {
					}
			    },
			    
			    busPolylineMouse_close:function(e){
			    	self.infoWindow.close();
			    },
			    
			    passengerFlowClick: function(e) {
		            var content = e.target.content; //地图上所标点的坐标    
		            self.infoWindow.setContent(content);
		            self.infoWindow.open(self.mapObj, e.target.getCenter());
				 },
				 
				/*drawAllStation : function (data) {
					var data = self.allPlatForm;
	            	var allPlatforms = [];
	            	if(self.lineStations==null||self.lineStations.length<1){
	            		self.drawPlatform(data);
	            		return false;
	            	}
	            	for(var i=0;i<data.length;i++){
	            		var platFormName = data[i].name;
	            		var flag = true;
	            		for(var j=0;j<self.lineStations.length;j++){
	            			var stationName = self.lineStations[j].name;
	            			if(undefined == stationName ){
	            				stationName = self.lineStations[j][3];
	            			}
	            			if(platFormName == stationName ){
	            				flag = false;
	            				break;
	            			}
	            		}
	            		if(flag){
	            			allPlatforms.push(data[i]);
	            		}
	            	}
	            	
	            	self.allPlatForm = allPlatforms;
	            	self.drawPlatform(allPlatforms);
				},
				*/
				drawPlatform:function(data){
					for (var i = 0; i < data.length; i++) {
	            	    var color = '#000000';
	            	   
		                var circle = new AMap.Circle({
		                    radius: 20,
		                    center: [data[i].LONGITUDE, data[i].LATITUDE],
		                    strokeColor: color,
		                    strokeWeight: 1,
		                    fillColor: color,
		                    fillOpacity: 0.5,
		                    zIndex:98,
		                    map: self.mapObj
		                });
		              
		                
		                
		                var template = "<div class='passengerFlowInfo'><h2 class='passengerFlowInfo_title'>" + data[i].NAME + " :"+data[i].TOTAL_PERSON_COUNT+"</h2>";
		                template+="<p class='passengerFlowInfo_p'>上车人数:" + data[i].ONBUS_PERSON_COUNT + "</p><p class='passengerFlowInfo_p'>下车人数:" + data[i].OFFBUS_PERSON_COUNT + "</p>";
		                
		                var totalCount = data[i].TOTAL_PERSON_COUNT;
		                var LINE_TOTAL_PERSON_COUNT = data[i].LINE_TOTAL_PERSON_COUNT;
		                if(comm.isNotEmpty(LINE_TOTAL_PERSON_COUNT)){
			                var linepsgArray = LINE_TOTAL_PERSON_COUNT.split(",");
			                
			                for(var j = 0;j<linepsgArray.length;j++){
			                	var linepsg = linepsgArray[j].split(':');
			                	var rate = 0;
			                	if(comm.isNotEmpty(linepsg[1])&&linepsg[1]!='0'){
			                		rate = parseInt( ((parseInt(linepsg[1])/parseInt(totalCount)).toFixed(2) *100)+'') ;
			                	}
			                	
			                	template+="<p class='passengerFlowInfo_p'>" + linepsg[0]+ " 承载率:"+rate+"%</p>";
			                	
			                }
		                }
		                
		                template+='</div>';
		                circle.content = template;
		                circle.on("click", self.passengerFlowClick);
		                circle.on("mouseover", self.passengerFlowClick);
		                circle.on("mouseout", self.busPolylineMouse_close);
		                
		                data[i].circle = circle;
		            }
				},
			    
			    drawStation: function(data) {
			    	self.lineStations = data;
			    	for (var i = 0; i < data.length; i++) {
	            	    var color = '#0b0bf1';
	            	   
		                var circle = new AMap.Circle({
		                    radius: 20,
		                    center: [data[i].longitude, data[i].latitude],
		                    strokeColor: color,
		                    strokeWeight: 1,
		                    fillColor: color,
		                    fillOpacity: 0.5,
		                    zIndex:98,
		                    map: self.mapObj
		                });

		                var template = "<div class='passengerFlowInfo'><h2 class='passengerFlowInfo_title'>" + data[i].name + "</h2></div>";
		                circle.content = template;
		                circle.on("click", self.passengerFlowClick);
		                circle.on("mouseover", self.passengerFlowClick);
		                circle.on("mouseout", self.busPolylineMouse_close);
		                
		                self.lineStations[i].circle = circle;
		            }
		        },
		        
		        hidePlatForm:function(){
		        	var data = self.allPlatForm;
		        	for (var i = 0; i < data.length; i++) {
		        		data[i].circle.setMap(null);
		        	}
		        }
		        
		       /* drawStation: function(data) {
		            self.markerArr = [];
		            for (var i = 0; i < data.length; i++) {
		                var marker;
		                var icon = null;
		                var div = null;
		                div = document.createElement('div');
		                div.className = 'circle';
		                
		                marker = new AMap.Marker({
		                    content: div,
		                    position: [data[i].longitude, data[i].latitude],
		                    title: data[i].name,
		                    map: self.mapObj,
		                    icon: icon,
		                    zIndex: 10
		                });
		                marker.content = data[i].name;
		                marker.on('mouseover', self.markerClick); 
		                self.markerArr.push(marker);
		                self.mapObj.setFitView();
		                var zoom = self.mapObj.getZoom();
		                if (zoom < 13) {
		                    marker.hide();
		                };
		            };
		        }*/
			};
			return self;
		});