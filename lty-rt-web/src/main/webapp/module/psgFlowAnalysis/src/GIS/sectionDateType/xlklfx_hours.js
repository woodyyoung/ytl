define(['text!psgFlowAnalysis/tpl/GIS/sectionDateType/xlklfx_hours.html'],
function(tpl) {
    var self = {
        mapObj: null,
        infoWindow: null,
        markerArr: {},
        folwDegree:null,
        folwDegrees:null,
        mydChart:null,
        show: function(data) {
            $('#xlklfx_form').html(tpl);
            if(data){
            	self.folwDegrees = data;
            	if(self.folwDegrees){
            		self.folwDegree = self.folwDegrees[0].levelList;
            		comm.initLevelDiv(self.folwDegree);
            	}
            }
            $('#planName ').val($('#planName option:first-child').val()).trigger('change');
            //初始化日历控件
            $("#startTime").datetimepicker({
                format: 'yyyy-mm-dd',
                language: "zh-CN",
                minView: 'month'
            });
            $("#endTime").datetimepicker({
                format: 'yyyy-mm-dd',
                language: "zh-CN",
                minView: 'month'
            });

            //确定按钮
            $('#btn_hour_confirm').click(function() {
                self.initData();
            });
            
            $('#planName').change(function(){
				var planId =  $('#planName').val();
				var levelData = self.getFlowLevelData(planId);
				self.folwDegree = levelData;
				comm.initLevelDiv(levelData);
				//self.initData();
			});

            self.infoWindow = new AMap.InfoWindow({
                isCustom: false,
                // 使用自定义窗体
                offset: new AMap.Pixel(0, -15) // -113, -140
            });

            self.mapObj = new AMap.Map('visitorsFlow_mapZ', {
                resizeEnable: true,
                center: [109.4, 24.33],
                zoom: 13
            });
            self.mapObj.on("zoomend",
            function() {
                var zoom = self.mapObj.getZoom();
                for (var a = 0; a < self.markerArr.length; a++) {
                    if (zoom < 13) {
                        self.markerArr[a].hide();
                    } else {
                        self.markerArr[a].show();
                    }
                };
            });

            AMap.plugin(['AMap.ToolBar', 'AMap.Scale'],
            function() {
                self.mapObj.addControl(new AMap.ToolBar());

                self.mapObj.addControl(new AMap.Scale());

            });

            self.initChart();
        },
        initChart: function(){
			self.mydChart = echarts.init(document.getElementById('xlklfxCharts'));
			self.mydChart.setOption({
				title : {
					text : '断面流量'
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
					axisLabel : {
		                formatter: '{value} 日'
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
        initData: function() {
            var params = {};
            var dayType = $('input[name="optionsRadios1"]:checked').val();
            var stretchCode =  $.trim($('#section option:selected').val());
            var startTime = $('#startTime').val();
            params = {
                "dataType": "0",
                "stretchCode": stretchCode,
                "holidayFlag": dayType,
                "aloneDay": startTime
            }
            comm.requestJson('/report/stretchAnalysis/getAllSectionPsgFlowDataList', JSON.stringify(params),
            function(resp) {
            	try {
            		var linePsgData = resp.stretchPsg.sectionLinePsgData;
            		self.initLineEChats(linePsgData);
            		var stretchMap = resp.stretchPsg.stretchPsgAndLats;
            		self.mapObj.clearMap();
            		var stretchColor = '#00b7ff';
            		if(comm.isNotEmpty(self.folwDegree)){
            			stretchColor = self.folwDegree[0].CIRCLECOLOR;
                	}
            		if(stretchCode != null && stretchCode!= '' && stretchCode != undefined){
                			var stretchData = stretchMap[2];
                			var stretchNum = stretchMap[1];
                			var stretchName = stretchMap[0];
                			stretchColor = self.getFlowDegree(stretchNum)[0];
                			self.initLineMap(stretchData,stretchNum,stretchName,stretchColor);
            		}else{
            			for (var i = 0; i < stretchMap.length; i++) {
            				var stretchData = stretchMap[i][2];
            				var stretchNum = stretchMap[i][1];
            				var stretchName = stretchMap[i][0];
            				stretchColor = self.getFlowDegree(stretchNum)[0];
            				self.initLineMap(stretchData,stretchNum,stretchName,stretchColor);
            			}
            		}
				} catch (e) {
				}
            });
        },
        drawLine: function(lineLpath, strokeColor, extData) {
            var busPolyline = new AMap.Polyline({
                map: self.mapObj,
                path: lineLpath,
                strokeColor: strokeColor,
                strokeOpacity: 0.8,
                strokeWeight: 6,
                extData:extData
            });
            busPolyline.on("mouseover",self.busPolylineMouse);
            busPolyline.on("mouseout",self.busPolylineMouse_close);
            self.mapObj.setFitView();
        },
        initLineMap: function(data,carNum,stretchName,strokeColor) {
        	var lastColor = '';
            for (var j = 0; j < data.length - 1; j++) {
                var lineLpath = [];
                lineLpath.push([data[j].ING, data[j].LAT], [data[j+1].ING, data[j+1].LAT]);
                var extData = '';
                if(strokeColor == '0'){
                	strokeColor = '#00b7ff'
                }
            	extData={
            			content: "<div class='linePassFlow_num'>["+stretchName+"]"+":"+carNum+"</div>"
                }
                self.drawLine(lineLpath, strokeColor, extData);
            };
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

        initLineEChats: function(data) {
            var maxData = "0";
            self.mydChart.on('click', function (params) {
                if(params.componentType=="series"){
                	var xName = params.seriesName;
                	var Name = params.name;
                    var params = {};
                    var dayType = $('input[name="optionsRadios1"]:checked').val();
                    var stretchCode = $.trim($('#section option:selected').val());
                    params = {
                        "dataType": "0",
                        "stretchCode": stretchCode,
                        "holidayFlag": dayType,
                        "aloneHour" : Name,
                        "aloneDay" :  xName
                    };
                    try {
                        comm.requestJson('/report/stretchAnalysis/getAllSectionPsgFlowDataList', JSON.stringify(params),
                    			function(resp) {
                        	self.mapObj.clearMap();
                        	if(resp && resp.stretchPsg && resp.stretchPsg.stretchPsgAndLats){
                        		var stretchMap = resp.stretchPsg.stretchPsgAndLats;
                        		var stretchColor = '#00b7ff';
                        		if(comm.isNotEmpty(self.folwDegree)){
                        			stretchColor = self.folwDegree[0].CIRCLECOLOR;
                        		}
                        		if(stretchCode != null && stretchCode!= '' && stretchCode != undefined){
                        			var stretchData = stretchMap[2];
                        			var stretchNum = stretchMap[1];
                        			var stretchName = stretchMap[0];
                        			stretchColor = self.getFlowDegree(stretchNum)[0];
                        			self.initLineMap(stretchData,stretchNum,stretchName,stretchColor);
                        		}else{
                        			for (var i = 0; i < stretchMap.length; i++) {
                        				var stretchData = stretchMap[i][2];
                        				var stretchNum = stretchMap[i][1];
                        				var stretchName = stretchMap[i][0];
                        				stretchColor = self.getFlowDegree(stretchNum)[0];
                        				self.initLineMap(stretchData,stretchNum,stretchName,stretchColor);
                        			}
                        		}
                        	}
                    		
        				});
					} catch (e) {
					}
                
                }
            });
            
            var legendData = [];
            var xAxisData = [];
            // 显示标题，图例和空的坐标轴
            self.mydChart.setOption({
                title: {
                    text: ''
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: legendData
                },
                dataZoom: [{ // 这个dataZoom组件，默认控制x轴。
                    type: 'slider',
                    // 这个 dataZoom 组件是 slider 型 dataZoom 组件
                    start: 0,
                    // 左边在 10% 的位置。
                    end: 100
                    // 右边在 60% 的位置。
                },
                { // 这个dataZoom组件，也控制x轴。
                    type: 'inside',
                    // 这个 dataZoom 组件是 inside 型 dataZoom 组件
                    start: 0,
                    // 左边在 10% 的位置。
                    end: 100
                    // 右边在 60% 的位置。
                }],
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    feature: {
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: xAxisData
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    name: '客流量',
                    type: 'line',
                    stack: '总量',
                    data: []
                }]
            });

            var Item = function() {
                return {
                    name: '',
                    type: 'line',
                    data: []
                }
            };

            // series中的每一项为一个item,所有的属性均可以在此处定义
            var json = null; // 后台返回的json
            try {
                json = data
            } catch(e) {}
            var Series = []; // 准备存放图表数据
            var ss = {};
            if (json != null && json != undefined) {
                for (var i = 0; i < json.length; i++) {
                    var item = json[i];
                    var name = '';
                    name = item.DD;
                    var klData = ss['' + name + ''];
                    if (klData == null) {
                        klData = [];
                    }
                    klData.push(item.ONBUS_PERSON_COUNT);
                    ss['' + name + ''] = klData;
                    maxData = eval(maxData + item.ONBUS_PERSON_COUNT);
                    if (!comm.contains(xAxisData, item.HH)) {
                        xAxisData.push(item.HH);
                    }
                    if (!comm.contains(legendData, item.DD)) {
                        legendData.push(item.DD);
                    }
                }
            }

            for (var item in ss) {
                var it = new Item();
                it.name = item;
                it.data = ss[item];
                Series.push(it);
            }
             
            $('.total_p2').text(eval(maxData)); //设置线路客流总量
            
            
            var chartTile = '';
            self.mydChart.setOption({
                title: {
                    text: chartTile
                },
                legend: {
                    data: legendData
                },
                xAxis: {
                    data: xAxisData
                },
                series: Series
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
        initSelect: function(data) {
            var defaultOption = '<option value="">全部</option>';
            comm.initSelectOptionForObj('test', data, 'name', 'id', defaultOption);
        },
        markerClick: function(e) {
            //处理Marker的单击事件
            var content = e.target.content; //地图上所标点的坐标    
            self.infoWindow.setContent(content);
            self.infoWindow.open(self.mapObj, e.target.getPosition());
        }

    };
    return self;
});