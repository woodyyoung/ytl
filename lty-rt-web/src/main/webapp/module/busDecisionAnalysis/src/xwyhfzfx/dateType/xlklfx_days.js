define(['text!busDecisionAnalysis/tpl/xwyhfzfx/dateType/xlklfx_days.html'],
function(tpl) {
    var self = {
        mapObj: null,
        infoWindow: null,
        legendData: null,
        xAxisData: null,
        markerArr: {},
        maxData: 0,
        folwDegrees:null,
        folwDegree:null,
        show: function(data) {
            $('#xlklfx_form').html(tpl);
            self.folwDegrees = data;
            self.folwDegree = self.folwDegrees[0].levelList;
            $('#planName').val($('#planName option:first-child').val()).trigger('change');
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
            $('#btn_day_confirm').click(function() {
                self.initData();
            });
            
            //
			$('#planName').change(function(){
				var planId =  $('#planName').val();
				var levelData = self.getFlowLevelData(planId);
				self.folwDegree = levelData;
				comm.initLevelDiv(levelData);
				self.initData();
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
            
            self.initData();

        },
        initData: function() {
            var params = {};
            var dayType = $('input[name="optionsRadios1"]:checked').val();
            var lineId = $.trim($('#test option:selected').val());
            var startTime = $('#startTime').val();
            var endTime = $('#endTime').val();
            /*if (($('#startTime').val()) >= ($('#endTime').val())) {
				comm.alert_tip('请选择正确日期范围 ');
				return false;
			}*/
            params = {
                "dataType": "D",
                "lineId": lineId,
                "holidayFlag": dayType,
                "startTime": startTime,
                "endTime": endTime
            }
            comm.requestJson('/report/lineAnalysis/getAllLinePsgFlowDataList', JSON.stringify(params),
            function(resp) {
            	try {
            		var linePsgData = resp.linePsgData;
            		self.initLineEChats(linePsgData);
            		var allStationsAndLinePath = resp.allStationsAndLinePath;
            		var allPlatForms = resp.allPlats;
            		self.mapObj.clearMap();
            		self.maxData = 0;
            		if(lineId != '' && lineId != null ){
                        var linePath = allStationsAndLinePath.linePath;
                        self.initLineMap(linePath);
            		}else{
            			for(var i = 0 ;i<allStationsAndLinePath.length ; i++){
            				var linePath = allStationsAndLinePath[i].linePath;
            				self.initLineMap(linePath);
            			}
            		}
            		self.initStationMap(allPlatForms);
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
        initStationMap: function(data) {
            self.markerArr = [];
            for (var i = 0; i < data.length; i++) {
                var marker;
                var icon = null;
                var lines = data[i].lines;
                var linedata = "<div class='linePassFlow_num'>";
                var count = 0;
				count = data[i].totalPersonCount;
                for (var j = 0; j < lines.length; j++) {
					linedata = linedata+"<p>"+lines[j].LINE_NAME+": "+eval((lines[j].TOTAL_PERSON_COUNT/count).toFixed(2)*100)+"%</p>";
				}
                var title = "<p>站台名："+data[i].platformName+"</p>"
                +"<p>客流总量："+count+"</p>"+
                linedata +"</div>";
                marker = new AMap.Marker({
                	content:"<div class='circlemarker'><div class='circle'></div></div>",
                    position: [data[i].longitude, data[i].latitude],
                   // title: title,
                    map: self.mapObj,
                    icon: icon,
                    zIndex: 10
                });
                marker.content = title;
                marker.on('click', self.markerClick); 
                self.markerArr.push(marker);
                self.mapObj.setFitView();
                var zoom = self.mapObj.getZoom();
                if (zoom < 13) {
                    marker.hide();
                };
            };
        },
        initLineMap: function(data) {
        	var lastColor = '#00b7ff';
        	if(comm.isNotEmpty(self.folwDegree)){
        		lastColor = self.folwDegree[0].CIRCLECOLOR;
        	}
        	var lastCount = 0;
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
        },
        busPolylineMouse:function(e){
	        var busPolyline=e.target;
	        var content = busPolyline.getExtData().content;//地图上所标点的坐标    
	        self.infoWindow.setContent(content);
	        self.infoWindow.open(self.mapObj, e.lnglat);      
	    },
	    busPolylineMouse_close:function(e){
	    	self.infoWindow.close();
	    },
        initLineEChats: function(data) {
            var mydChart = echarts.init(document.getElementById('xlklfxCharts'));
            var maxData = "0";
            mydChart.on('click', function (params) {
                if(params.componentType=="series"){
                	var xName = params.name;
                    var params = {};
                    var dayType = $('input[name="optionsRadios1"]:checked').val();
                    var lineId = $.trim($('#test option:selected').val());
                    params = {
                        "dataType": "D",
                        "lineId": lineId,
                        "holidayFlag": dayType,
                        "aloneDay" :  xName
                    };
                    try {
                    	comm.requestJson('/report/lineAnalysis/getAllLinePsgFlowDataList', JSON.stringify(params),
                    			function(resp) {
                    		var allStationsAndLinePath = resp.allStationsAndLinePath;
                    		var allPlatForms = resp.allPlats;
                    		self.mapObj.clearMap();
                    		self.maxData = 0;
                    		if(lineId != '' && lineId != null ){
                                var linePath = allStationsAndLinePath.linePath;
                                self.initLineMap(linePath);
                    		}else{
                    			for(var i = 0 ;i<allStationsAndLinePath.length ; i++){
                    				var linePath = allStationsAndLinePath[i].linePath;
                    				self.initLineMap(linePath);
                    			}
                    		}
                    		self.initStationMap(allPlatForms);
                    	});
					} catch (e) {
					}
                
                }
            });
            self.legendData = [];
            self.xAxisData = [];
            // 显示标题，图例和空的坐标轴
            mydChart.setOption({
                title: {
                    text: ''
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: self.legendData
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
                    data: self.xAxisData
                },
                yAxis: {
                    type: 'value',
					axisLabel : {
		                formatter: '{value} 人次'
		            }
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
                json = data.lineData
            } catch(e) {}
            var Series = []; // 准备存放图表数据
            var ss = {};
            if (json != null && json != undefined) {
                for (var i = 0; i < json.length; i++) {
                    var item = json[i];
                    var name = '';
                    name = "data";
                    
                    var klData = ss['' + name + ''];
                    if (klData == null) {
                        klData = [];
                    }
                    klData.push(item.ONBUS_PERSON_COUNT);
                    self.maxData = eval(self.maxData + item.ONBUS_PERSON_COUNT);
                    ss['' + name + ''] = klData;

                    if (!comm.contains(self.xAxisData, item.DD)) {
                        self.xAxisData.push(item.DD);
                    }
                }
            }

            for (var item in ss) {
                var it = new Item();
                it.name = item;
                it.data = ss[item];
                Series.push(it);
            }
            
            $('.total_p2').text(eval(self.maxData)); //设置线路客流总量
            
            var chartTile = '';
            mydChart.setOption({
                title: {
                    text: chartTile
                },
                legend: {
                    data: self.legendData
                },
                xAxis: {
                    data: self.xAxisData
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