define(['text!module/index-middle.html'],function (tpl) {
    //Do setup work here
		var  self = {
			mapObj: null,
	        infoWindow: null,
	        myChart:null,
	        myChart1:null,
	        myChart2:null,
	        myChart3:null,
	        folwDegree:null,
            show: function() {
            	 //加载middle部分
        	$('.index_padd').html('');
			$('.index_padd').html(tpl);
				
			 self.infoWindow = new AMap.InfoWindow({
	                isCustom: false,
	                // 使用自定义窗体
	                offset: new AMap.Pixel(0, -15) // -113, -140
	            });

	            self.mapObj = new AMap.Map('qyfbmap', {
	                resizeEnable: true,
	                center: [109.4, 24.33],
	                zoom: 13
	            });

	            AMap.plugin(['AMap.ToolBar', 'AMap.Scale'],
	    	            function() {
	    	                self.mapObj.addControl(new AMap.ToolBar());

	    	                self.mapObj.addControl(new AMap.Scale());

	    	            });
	            
	            
	            
				self.initChart();
				self.initLevelDiv();
				//初始化客流基本统计信息
				self.initData();
            	
            },
            initData:function(){
                comm.requestJson('/report/index/getBasicPsgFlowData', null,
                function(resp) {
                	try {
                		if(resp.data.totalPsgToday){
                			var todayTotalPas = resp.data.totalPsgToday;//查询当日客流总量
                			$('#todayTotalPas').text(todayTotalPas.TOTAL_PERSON_COUNT);
                		}
                		if(resp.data.avgLinePsgToday){
                			var avgLinePsgToday = resp.data.avgLinePsgToday;//查询当日客流总量
                			$('#todayAVGCrowdedUtilised').text(avgLinePsgToday.AVGLINEPSGTODAY);
                		}
                		if(resp.data.maxLinePsg){
                			var maxLinePsg = resp.data.maxLinePsg;//查询当日客流总量
                			$('#todayMaxCrowdedLine').text(maxLinePsg.LINE_NAME);
                			$('#todayMaxCrowdedUtilised').text(maxLinePsg.UTILISED);
                		}
                		if(resp.data.maxStationAndCommunityPsg){
                			var maxStationAndCommunityPsg = resp.data.maxStationAndCommunityPsg;// 查询客流最大站台与客流最大的小区
                			self.initBasinData(maxStationAndCommunityPsg);
                		}
                		var topPlatformPsg = resp.data.topPlatformPsg;// 查询Top10客流最大的站台
        				self.showZtklTop10(topPlatformPsg);//站台客流TOP10图
                		var topCommunityPsg = resp.data.topCommunityPsg;// 查询Top10客流最大小区
        				self.showAreaTop10(topCommunityPsg);//区域客流TOP10
                		var volatilityPsgFlow = resp.data.volatilityPsgFlow;// 查询24小时客流波动
                		self.show24hourskl(volatilityPsgFlow);
                		var topCrowdedPsg = resp.data.topCrowdedPsg;// 查询Top10满载率
                		self.showMzlTop10(topCrowdedPsg);
                		var staionsPsgFlow = resp.data.staionsPsgFlow;// 查询地图客流
                		//区域分布
        				self.showAreaklfb(staionsPsgFlow);
					} catch (e) {
					}
                });
            },
            initBasinData:function(data){
            	if(data.MAXPLATFORM){
            		$('.todayMaxPlatForm').text(data.MAXPLATFORM+"("+data.MAXPLATFORMCOUNT+")"); //MAXARE
            	}
            	if(data.MAXARE){
            		$('.todayMaxCommunity').text(data.MAXARE+"("+data.MAXAREACOUNT+")"); //MAXPLATFORM
            	}
            },
            initChart: function(){
            	self.myChart = echarts.init(document.getElementById('ztkltop10'));
            	self.myChart1 = echarts.init(document.getElementById('areakltop10'));
            	self.myChart2 = echarts.init(document.getElementById('mzltop10'));
            	self.myChart3 = echarts.init(document.getElementById('canvas_dahs'));
				var option = {
						title : {
							text : '站台客流TOP10'
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
							bottom : '13%',
							containLabel : true
						},
						toolbox : {
							feature : {
								saveAsImage : {}
							}
						},
						xAxis : {
							type : 'category',
							axisLabel: {
		                        interval: 0,
		                        dataMin : 10, 
		                        dataMax : 10, 
		                        rotate: 45
		                    },
							data : []
						},
						yAxis : {
							type : 'value'
						},
						series : []
					
				};
				
				// 显示标题，图例和空的坐标轴
				self.myChart.setOption(option);
				self.myChart1.setOption(option);
				// 显示标题，图例和空的坐标轴
				self.myChart2.setOption(option);
				self.myChart3.setOption({
					title : {
						text : ''
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
						data : []
					},
					yAxis : {
						type : 'value'
					},
					series : []
				});
			},
			fillEmptyData:function(data){
				var legendData = [];
				for (var i = 0; i < data.length; i++) {
					 var item = data[i];
					 var name = item.DATATYPE;
					 if (!comm.contains(legendData, name)) {
                         legendData.push(name);
                     }
				}
				if(!comm.contains(legendData, '上周同期')){
					var obj = {TOTAL_PERSON_COUNT: 0, HH: "", DATATYPE: "C"}
					data.push(obj);
				}
				
			},
            showMzlTop10:function(data){
            	//self.fillEmptyData(data);
            	
                var legendData = [];
                var xAxisData = [];
                // 显示标题，图例和空的坐标轴
                var Item = function() {
                    return {
                        name: '',
                        type: 'bar',
                        barWidth : 10,
                        data: []
                    }
                };

                // series中的每一项为一个item,所有的属性均可以在此处定义
                var json = data; // 后台返回的json
                var Series = []; // 准备存放图表数据
                var ss = {};
                if (json != null && json != undefined) {
                    for (var i = 0; i < json.length; i++) {
                        var item = json[i];
                        var name = '';
                        name = item.DATATYPE;
                        if('A' == name){
                        	name = "当天";
                        }else if('B' == name){
                        	name = '前一天';
                        }else if('C' == name){
                        	name = '上周同期';
                        }
                        var klData = ss['' + name + ''];
                        if (klData == null) {
                            klData = [];
                        }
                        klData.push(item.UTILISED);
                        ss['' + name + ''] = klData;

                        if (!comm.contains(xAxisData, item.LINE_NAME)) {
                            xAxisData.push(item.LINE_NAME);
                        }
                        if (!comm.contains(legendData, name)) {
                            legendData.push(name);
                        }
                    }
                }
               

                for (var item in ss) {
                    var it = new Item();
                    it.name = item;
                    it.data = ss[item];
                    Series.push(it);
                }
                
                
                if(!comm.contains(legendData, '上周同期')){
                	  legendData.push('上周同期');
                	  var it = new Item();
                      it.name = '上周同期';
                      it.data = [];
                      Series.push(it);
                }
                
                var chartTile = '';
                self.myChart2.setOption({
                    title: {
                        text: chartTile
                    },
                    legend: {
                        data: legendData
                    },
                    xAxis: {
                        data: xAxisData
                    },
                    //新增Y轴单位 人次 2017年3月15日 何杰
                    yAxis : {
						type : 'value',
						axisLabel : {
			                formatter: '{value} 人次'
			            }
					},
                    series: Series
                });

            },
            showZtklTop10:function(data){
                var legendData = [];
                var xAxisData = [];
                // 显示标题，图例和空的坐标轴
                var Item = function() {
                    return {
                        name: '',
                        type: 'bar',
                        barWidth : 10,
                        data: []
                    }
                };

                // series中的每一项为一个item,所有的属性均可以在此处定义
                var json = data; // 后台返回的json
                var Series = []; // 准备存放图表数据
                var ss = {};
                if (json != null && json != undefined) {
                    for (var i = 0; i < json.length; i++) {
                        var item = json[i];
                        var name = '';
                        name = item.DATATYPE;
                        if('A' == name){
                        	name = "当天";
                        }else if('B' == name){
                        	name = '前一天';
                        }else if('C' == name){
                        	name = '上周同期';
                        }
                        var klData = ss['' + name + ''];
                        if (klData == null) {
                            klData = [];
                        }
                        klData.push(item.TOTAL_PERSON_COUNT);
                        ss['' + name + ''] = klData;

                        if (!comm.contains(xAxisData, item.PLATFORM_NAME)) {
                            xAxisData.push(item.PLATFORM_NAME);
                        }
                        if (!comm.contains(legendData, name)) {
                            legendData.push(name);
                        }
                    }
                }

                for (var item in ss) {
                    var it = new Item();
                    it.name = item;
                    it.data = ss[item];
                    Series.push(it);
                }

                
                if(!comm.contains(legendData, '上周同期')){
              	  legendData.push('上周同期');
              	  var it = new Item();
                    it.name = '上周同期';
                    it.data = [];
                    Series.push(it);
              }
                
                
                var chartTile = '';
                self.myChart.setOption({
                    title: {
                        text: chartTile
                    },
                    legend: {
                        data: legendData
                    },
                    xAxis: {
                        data: xAxisData
                    },
                    //新增Y轴单位 人次 2017年3月15日 何杰
                    yAxis : {
						type : 'value',
						axisLabel : {
			                formatter: '{value} 人次'
			            }
					},
                    series: Series
                });

            },
            showAreaTop10:function(data){
                var legendData = [];
                var xAxisData = [];
                // 显示标题，图例和空的坐标轴
                var Item = function() {
                    return {
                        name: '',
                        type: 'bar',
                        barWidth : 10,
                        data: []
                    }
                };

                // series中的每一项为一个item,所有的属性均可以在此处定义
                var json = data; // 后台返回的json
                var Series = []; // 准备存放图表数据
                var ss = {};
                if (json != null && json != undefined) {
                    for (var i = 0; i < json.length; i++) {
                        var item = json[i];
                        var name = '';
                        name = item.DATATYPE;
                        if('A' == name){
                        	name = "当天";
                        }else if('B' == name){
                        	name = '前一天';
                        }else if('C' == name){
                        	name = '上周同期';
                        }
                        var klData = ss['' + name + ''];
                        if (klData == null) {
                            klData = [];
                        }
                        klData.push(item.TOTAL_PERSON_COUNT);
                        ss['' + name + ''] = klData;

                        if (!comm.contains(xAxisData, item.AREA_NAME)) {
                            xAxisData.push(item.AREA_NAME);
                        }
                        if (!comm.contains(legendData, name)) {
                            legendData.push(name);
                        }
                    }
                }

                for (var item in ss) {
                    var it = new Item();
                    it.name = item;
                    it.data = ss[item];
                    Series.push(it);
                }

                
                if(!comm.contains(legendData, '上周同期')){
              	  legendData.push('上周同期');
              	  var it = new Item();
                    it.name = '上周同期';
                    it.data = [];
                    Series.push(it);
              }
                
                var chartTile = '';
                self.myChart1.setOption({
                    title: {
                        text: chartTile
                    },
                    legend: {
                        data: legendData
                    },
                    xAxis: {
                        data: xAxisData
                    },
                    //新增Y轴单位 人次 2017年3月15日 何杰
                    yAxis : {
						type : 'value',
						axisLabel : {
			                formatter: '{value} 人次'
			            }
					},
                    series: Series
                });



            },
            show24hourskl:function(data){
                var legendData = [];
                var xAxisData = [];
                // 显示标题，图例和空的坐标轴

                var Item = function() {
                    return {
                        name: '',
                        type: 'line',
                        data: []
                    }
                };

                // series中的每一项为一个item,所有的属性均可以在此处定义
                var json = data; // 后台返回的json
                var Series = []; // 准备存放图表数据
                var ss = {};
                if (json != null && json != undefined) {
                    for (var i = 0; i < json.length; i++) {
                        var item = json[i];
                        var name = '';
                        name = item.DATATYPE;
                        if('A' == name){
                        	name = "当天";
                        }else if('B' == name){
                        	name = '前一天';
                        }else if('C' == name){
                        	name = '上周同期';
                        }
                        var klData = ss['' + name + ''];
                        if (klData == null) {
                            klData = [];
                        }
                        klData.push(item.TOTAL_PERSON_COUNT);
                        ss['' + name + ''] = klData;
                        if (!comm.contains(xAxisData, item.HH)) {
                            xAxisData.push(item.HH);
                        }
                        if (!comm.contains(legendData, name)) {
                            legendData.push(name);
                        }
                    }
                }

                for (var item in ss) {
                    var it = new Item();
                    it.name = item;
                    it.data = ss[item];
                    Series.push(it);
                }
                 
                if(!comm.contains(legendData, '上周同期')){
              	  legendData.push('上周同期');
              	  var it = new Item();
                    it.name = '上周同期';
                    it.data = [];
                    Series.push(it);
              }
                
                var chartTile = '';
                self.myChart3.setOption({
                    title: {
                        text: chartTile
                    },
                    legend: {
                        data: legendData
                    },
                    //新增X轴单位 点 2017年3月15日 何杰
                    xAxis: {
                        data: xAxisData,
                        axisLabel : {
			                formatter: '{value} 点'
			            }
                    },
                    //新增Y轴单位 人次 2017年3月15日 何杰
                    yAxis : {
						type : 'value',
						axisLabel : {
			                formatter: '{value} 人次'
			            }
					},
                    series: Series
                });
                
                
                self.myChart3.on('legendselectchanged', function (params) {
				    // 获取点击图例的选中状态
				    var option ={
			    		legend : {
			    			selected : params.selected
						}
				    }
				    self.myChart.setOption(option);
					self.myChart1.setOption(option);
				    self.myChart2.setOption(option);
				    //myChart.setOption(option);
				});

                
            },
            showAreaklfb:function(data){
            	self.mapObj.clearMap();
            	var stretchColor = '#00b7ff';
            	var strokeSize = '10';
        		if(comm.isNotEmpty(self.folwDegree)){
        			stretchColor = self.folwDegree[0].CIRCLECOLOR;
        			strokeSize = self.folwDegree[0].CIRCLESIZE;
            	}
                for (var i = 0; i < data.length; i++) {
                	var total_person_count = data[i].TOTAL_PERSON_COUNT;
                	var stroke = self.getFlowDegree(total_person_count);
                	strokeColor = stroke[0];
                	strokeSize = stroke[1];
                    var circle = new AMap.Circle({
                        radius: strokeSize,
                        center: [data[i].LONGITUDE, data[i].LATITUDE],
                        strokeColor: '#006600',
                        strokeWeight: 1,
                        fillColor: strokeColor,
                        fillOpacity: 0.5,
                        map: self.mapObj
                    });

                    var template = "<div class='passengerFlowInfo'><h2 class='passengerFlowInfo_title'>站台名:" + data[i].STATION_NAME + "</h2><p class='passengerFlowInfo_p'>上车人数:" + data[i].TOTAL_PERSON_COUNT + "</p></div>";
                    circle.content = template;
                    circle.on("click", self.passengerFlowClick);
                }
            },
            passengerFlowClick: function(e) {
                var content = e.target.content; //地图上所标点的坐标    
                self.infoWindow.setContent(content);
                self.infoWindow.open(self.mapObj, e.target.getCenter());
            },
          //根据客流获取客流等级颜色值
            getFlowDegree : function(value){
            	if(comm.isNotEmpty(value)){
            		var ret = [];
            		for(var i=0; i<self.folwDegree.length; i++){
            			var max = self.folwDegree[i].MAXDATA;
            			var min = self.folwDegree[i].MINDATA;
            			if(value >= min && value < max){
            				ret.push(self.folwDegree[i].CIRCLECOLOR);
            				ret.push(self.folwDegree[i].CIRCLESIZE);
            			}
            		}
            		return ret;
            	}
            },
            initLevelDiv:function(){
            	var params = {};
                params.dataType = '010';
                params.isDisable = '1';
            	comm.requestJson('/report/passengerFlowLevel/list', JSON.stringify(params),
            	            function(resp) {
            					self.folwDegree = resp;
            	            });
            	
            }
        };
		
		return self;
});