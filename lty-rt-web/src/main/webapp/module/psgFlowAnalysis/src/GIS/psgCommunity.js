
define(['text!psgFlowAnalysis/tpl/GIS/communityPassengerFlow.html'],
function(tpl) {
    // Do setup work here
    var self = {
        mapObj: null,
        infoWindow: null, 
        folwDegrees:null,
        folwDegree:null,
        areaListData:null,
        show: function() {
            $('.index_padd').html('');
            $('.index_padd').html(tpl);

            self.mapObj = new AMap.Map('visitorsFlowRate_map', {
                resizeEnable: true,
                center: [109.4, 24.33],
                mapStyle: "light",
                zoom: 13
            });

            AMap.plugin(['AMap.ToolBar', 'AMap.Scale', 'AMap.OverView'],
            function() {
                self.mapObj.addControl(new AMap.ToolBar());

                self.mapObj.addControl(new AMap.Scale());

                self.mapObj.addControl(new AMap.OverView({
                    isOpen: true
                }));
            });

            self.infoWindow = new AMap.InfoWindow({
                offset: new AMap.Pixel(0, -10)
            });

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
            
            //
			$('#planName').change(function(){
				var planId =  $('#planName').val();
				var levelData = self.getFlowLevelData(planId);
				self.folwDegree = levelData;
				comm.initLevelDiv(levelData);
				self.initData();
			});

            //确定按钮
            $('#btn_cx').click(function() {
                self.initData();
            });
            //self.initData();
            self.initArea();
            self.initLevelDiv();

        },
        initData: function() {
            var params = {};
            var dayType = $('input[name="optionsRadios1"]:checked').val();
            var yareaId = $.trim($('#yztselect option:selected').val());
            var tareaId = $.trim($('#mbztselect option:selected').val());
            //查询日期校验							            
            var flag = comm.validQueryDate($(
							'#startTime').val(), $(
							'#endTime').val());
			 if (!flag) {
				return false;
			 }
			 
            var startTime = $('#startTime').val();
            var endTime = $('#endTime').val();
            //校验
            yareaInfo = self.getAreaInfo(yareaId);
            tareaInfo = self.getAreaInfo(tareaId);
            if(comm.isEmpty(yareaInfo)||yareaInfo.levels!='3'){//一般小区
            	comm.alert_tip('请选择到具体的源区域!');
            	return false;
            }
            if(comm.isEmpty(tareaInfo)||tareaInfo.levels!='3'){//一般小区
            	comm.alert_tip('请选择到具体的目标区域!');
            	return false;
            }
            if(yareaId == tareaId){//一般小区
            	comm.alert_tip('源区域和目标区域不能为同一区域!');
            	return false;
            }
            
            
           
            params = {
                "holidayFlag": dayType,
                "sAreaId": yareaId,
                "tAreaId": tareaId,
                "startTime": startTime,
                "endTime": endTime
            }
            comm.requestJson('/report/areaAnalysis/queryAreaPsgFlow', JSON.stringify(params),
            function(resp) {
                    var flowInto = resp.data.data.SOD_VALUE;
                    var outFlow = resp.data.data.TOD_VALUE;
                    var yzt = resp.data.yzt;
                    var mbzt = resp.data.mbzt;
                    var yzts = [];
                    var yztname = '';
                    var mbzts = [];
                    var mbztname = '';
                    for (var i = 0; i < yzt.length; i++) {
                        var temp = [];
                        temp.push(yzt[i].ING);
                        temp.push(yzt[i].LAT);
                        yztname = yzt[i].CODENAME;
                        yzts.push(temp);

                    }
                    for (var j = 0; j < mbzt.length; j++) {
                        var temp = [];
                        temp.push(mbzt[j].ING);
                        temp.push(mbzt[j].LAT);
                        mbztname = mbzt[j].CODENAME;
                        mbzts.push(temp);
                    }
                    self.mapObj.clearMap();
                    self.ResidentialArea(flowInto, outFlow, yzts, mbzts,yztname,mbztname);

            })
        },

        lineColor: function(lpathAll,carNum,carNum1,start,end){

            for(var j = 0; j < lpathAll.length; j++){
                var lineLpathAll = [];
                lineLpathAll.push(lpathAll[j].start,lpathAll[j].end);
                var strokeColor = self.getFlowDegree(carNum)[0];
                var extData={
                        content: "<div class='linePassFlow_num'>"+"<p>"+start+"到"+end+"</p>"+"<p>"+"流入:"+carNum+"</p>"+"<p>流出:"+carNum1+"</p>"+"</div>"
                }
                self.drawLine(lineLpathAll, strokeColor,extData);
            };
        },
        drawLine: function(lineLpath, strokeColor,extData) {
            var busPolyline = new AMap.Polyline({
                map: self.mapObj,
                showDir: true,
                path: lineLpath,
                strokeColor: strokeColor,
                strokeOpacity: 0.8,
                strokeWeight: 9,
                extData:extData
            });
            busPolyline.on("mouseover",self.busPolylineMouse);
            busPolyline.on("mouseout",self.busPolylineMouse_close);
            self.mapObj.setFitView();
        },
        ResidentialArea: function(flowInto, outFlow, yzts, mbzts,yztname,mbztname) {
            var polygons = [];
            var data = [{
                "ResidentialArea": yzts,
                "stationname": yztname,
                "targetstationname": mbztname,
                "flowInto": flowInto,
                "outFlow": outFlow
            },
            {
                "ResidentialArea": mbzts,
                "stationname": mbztname,
                "targetstationname": "",
                "flowInto": outFlow,
                "outFlow": flowInto
            }];
            for (var i = 0; i < data.length; i++) {
                var extData = {
                    content: "<div class='linePassFlow_num'>" + "<p>小区名:" + data[i].stationname + "</p>" + "<p>流入:" + data[i].flowInto + "</p>" + "<p>流出:" + data[i].outFlow + "</div>"
                }
                var polygonArr = data[i].ResidentialArea;
                var polygon = new AMap.Polygon({
                    path: polygonArr,
                    // 设置多边形边界路径
                    strokeColor: "#FF3366",
                    // 线颜色
                    strokeOpacity: 0.2,
                    // 线透明度
                    strokeWeight: 7,
                    // 线宽
                    fillColor: "#3366FF",
                    // 填充色
                    fillOpacity: 0.35,
                    // 填充透明度
                    map: self.mapObj,
                    extData: extData
                });
                polygons.push({
                    "polygon": polygon,
                    "number": data[i].flowInto + data[i].outFlow,
                    "stationname": data[i].stationname,
                    "targetstationname": data[i].targetstationname
                });
                polygon.on("mouseover", self.polygonMouse);
                polygon.on("mouseout", self.polygonMouse_close);
                self.mapObj.setFitView();
            }

            var lpath = [];
            for (var j = 0; j < polygons.length; j++) {
               var targetstationname = polygons[j].targetstationname;

                var targetpolygon = polygons.find(function(a) {
                    return a.stationname == targetstationname
                });

                if (j < polygons.length - 1) {
                    var line = {
                        "start": polygons[j].polygon.getBounds().getCenter(),
                        "end": targetpolygon.polygon.getBounds().getCenter(),
                        "number": polygons[j].number + polygons[j + 1].number
                    }
                    lpath.push(line);
                }
            }
            self.lineColor(lpath,flowInto,outFlow,yztname,mbztname);
            for(var j = 0; j < lpath.length; j++){                
                    var lpath1 = [];
		            for(var j = 0; j < lpath.length; j++){                
		            var mvLat;
		            var mvLnt;
		            var a = lpath[j].end.M - lpath[j].start.M;
		            var b = lpath[j].end.O - lpath[j].start.O;
		            
		            if (a > 0) {
		                mvLnt = 0.001;
		            }else {
		                mvLnt = - 0.001;
		            }
		            
		            if (b > 0) {
		                mvLat = -0.001;
		            }else {
		                mvLat = 0.001;
		            }
		            var line1 = {
		                 "start": {
		                	 "O":lpath[j].end.O+ mvLat,
		                     "M":lpath[j].end.M+mvLnt,
		                     "lat":lpath[j].end.lat,
		                     "lng":lpath[j].end.lng,
		                },
		                "end": {
		                	"O":lpath[j].start.O+mvLat,
		                    "M":lpath[j].start.M+mvLnt,
		                    "lat":lpath[j].start.lat,
		                    "lng":lpath[j].start.lng,
		                },
		                "number": lpath[j].number
		            }
		            if(!polygons[1].polygon.getBounds().contains(line1.start)){
		            	line1.start = polygons[1].polygon.getBounds().getCenter();
		            }
		            if(!polygons[0].polygon.getBounds().contains(line1.end)){
		            	line1.end = polygons[0].polygon.getBounds().getCenter();
		            }
		            
		
		            lpath1.push(line1);
		        };
            };
//            for(var j = 0; j < lpath.length; j++){                
//                var lpath1 = [];
//	            for(var j = 0; j < lpath.length; j++){                
//	            var mvLat;
//	            var mvLnt;
//	            var a = lpath[j].end.lat - lpath[j].start.lng;
//	            var b = lpath[j].end.lng - lpath[j].start.lat;
//	            
//	            if (a > 0) {
//	                mvLnt = 0.001;
//	            }else {
//	                mvLnt = - 0.001;
//	            }
//	            
//	            if (b > 0) {
//	                mvLat = -0.001;
//	            }else {
//	                mvLat = 0.001;
//	            }
//	            var line1 = {
//	                 "start": {
//
//	                	 /*"J":lpath[j].end.J+ mvLat,
//	                     "N":lpath[j].end.N+mvLnt,*/
//
//	                     "lat":lpath[j].end.lat+mvLat,
//	                     "lng":lpath[j].end.lng+mvLnt,
//	                },
//	                "end": {
//
//	               /* 	"J":lpath[j].start.J+mvLat ,
//	                    "N":lpath[j].start.N+mvLnt,*/
//
//	                    "lat":lpath[j].start.lat+mvLat,
//	                    "lng":lpath[j].start.lng+mvLnt,
//	                },
//	                "number": lpath[j].number
//	            }
//	            var line1 = [  [lpath[j].end.lng+mvLat, lpath[j].end.lat+ mvLnt],
//	                           [lpath[j].start.lng+mvLat, lpath[j].start.lat+ mvLnt]]
//            if(!polygons[1].polygon.getBounds().contains(line1.start)){
//	            	line1.start = polygons[1].polygon.getBounds().getCenter();
//	            }
//	            if(!polygons[0].polygon.getBounds().contains(line1.end)){
//	            	line1.end = polygons[0].polygon.getBounds().getCenter();
//            }
//            
	
//	            lpath1.push(line1);
//	        };
//        };
                self.lineColor(lpath1,outFlow,flowInto,mbztname,yztname);
                     },
        initArea: function() {
            var params = {};
            comm.request('/report/areaAnalysis/listAllArea', params,
            function(resp) {
            	self.areaListData = resp.data;
                comm.initAreaSelect('#yztselect',resp.data);
				comm.initAreaSelect('#mbztselect',resp.data);
            });
        },
        getAreaInfo:function(id){
        	for(var i=0;i<self.areaListData.length;i++){
        		var area = self.areaListData[i];
        		if(area.id==id){
        			return area;
        		}
        	}
        	return '';
        },
        polygonMouse: function(e) {
            var polygon = e.target;
            var content = polygon.getExtData().content; //地图上所标点的坐标    
            self.infoWindow.setContent(content);
            self.infoWindow.open(self.mapObj, e.lnglat);
        },
        polygonMouse_close: function(e) {
            self.infoWindow.close();
        },
      //根据客流获取客流等级颜色值
        getFlowDegree : function(value){
        	if(comm.isNotEmpty(value) || value == 0){
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
            params.dataType = '003';
            params.isDisable = '1';
        	comm.requestJson('/report/passengerFlowLevel/planAndLevelList', JSON.stringify(params),
        	            function(resp) {
        					self.folwDegrees = resp;
        					comm.initSelectOptionForObj('planName', resp, 'planName', 'id');
        					comm.initLevelDiv(resp[0].levelList);
        					self.folwDegree = resp[0].levelList;
        	            });
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

    }
    return self;
});