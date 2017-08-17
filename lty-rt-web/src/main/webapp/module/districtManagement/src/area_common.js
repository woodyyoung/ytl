var area_comm = {
		mapObj : null,
		mouseTool : null,
		oppath : null,
		polygon : null,
		platformData : null,
		mytable : null,
		initAreaData : null,
		infoWindow :null,
		initMap : function(){
			
			area_comm.infoWindow = new AMap.InfoWindow({
                isCustom: false,
                // 使用自定义窗体
                offset: new AMap.Pixel(0, -15) // -113, -140
            });
			
			//地图
			area_comm.mapObj = new AMap.Map('visitorsFlowRate_map',{
				 resizeEnable:true,
			     center:[109.4, 24.33 ],
			     zoom:12
			 });
			 
			 var polygonOption = {
					 map: area_comm.mapObj,
					 strokeColor: '#f00',
					 strokeWeight: 2,
					 fillColor: "#3366FF"
			 };
			 
			 //打开画笔
			 area_comm.mouseTool = new AMap.MouseTool(area_comm.mapObj); //在地图中添加MouseTool插件
			 $('#opendraw').click(function(){
				 if(area_comm.polygon != null && area_comm.polygon.getMap() != null){
					 comm.alert_tip('请先清除原有的区域轨迹！');
					 return false;
				 }
				 
				 if(area_comm.oppath != null){
					 comm.alert_tip('请先清除原有的区域轨迹！');
					 return false; 
				 }
				 
				 area_comm.mouseTool.polygon(polygonOption); //用鼠标工具画矩形		
				 AMap.event.addListener( area_comm.mouseTool,'draw',cb);
			 });
			 
			 function cb(e){
				 //获取路径信息
				 area_comm.oppath = e.obj.getPath();
			 }
		},
		
		//画区域
		initAreaMap : function(data){
			for(var j = 0; j<data.length-1; j++){			            	  
	             var areapath=[];
	             areapath.push([data[j].ing, data[j].lat],[data[j+1].ing, data[j+1].lat] );
    			 area_comm.drawLine(areapath);
			};         
		 },
		 
		 drawLine:function(areapath){
			 var busPolyline = new AMap.Polyline({
		            map: area_comm.mapObj,
		            path: areapath,
		            strokeColor:  "#34b000",
		            strokeOpacity: 0.8,
		            strokeWeight: 6
		          
			 });
			 area_comm.mapObj.setFitView();
		 },
		 
		/* //画点    clazz:样式
		 initStationMap : function(data, clazz){
	    	   for(var i = 0; i<data.length; i++){
                var marker;
                var icon=null;
                var div=null;
                div = document.createElement('div');
                div.className = clazz;
                div.innerHTML = i;
                marker = new AMap.Marker({
                  content:div,
                  position: [data[i].longitude, data[i].latitude],
                  title: data[i].name,
                  map: area_comm.mapObj,
                  icon:icon,
                  zIndex: 99           
                });
                area_comm.mapObj.setFitView();
              };
		 },*/
		 
		 //画点    clazz:样式
		 initStationMap : function(data, clazz){
			  var stretchColor = '#00b7ff';
	    	  /* for(var i = 0; i<data.length; i++){
                var marker;
                var icon=null;
                var div=null;
                div = document.createElement('div');
                div.className = clazz;
                div.innerHTML = i;
                marker = new AMap.Marker({
                  content:div,
                  position: [data[i].longitude, data[i].latitude],
                  title: data[i].name,
                  map: area_comm.mapObj,
                  icon:icon,
                  zIndex: 99           
                });
                area_comm.mapObj.setFitView();
              };*/
              for (var i = 0; i < data.length; i++) {
            	    var color = '#0b0bf1';
            	    if(comm.isNotEmpty(data[i].areaCode)){
            	    	color = '#000000';
            	    }else{
            	    	color = '#0b0bf1';
            	    }
	                var circle = new AMap.Circle({
	                    radius: 20,
	                    center: [data[i].longitude, data[i].latitude],
	                    strokeColor: color,
	                    strokeWeight: 1,
	                    fillColor: color,
	                    fillOpacity: 0.5,
	                    zIndex:98,
	                    map: area_comm.mapObj
	                });

	                var template = "<div class='passengerFlowInfo'><h2 class='passengerFlowInfo_title'>" + data[i].name + "</h2></div>";
	                circle.content = template;
	                circle.on("click", area_comm.passengerFlowClick);
	                circle.on("mouseover", area_comm.passengerFlowClick);
	                circle.on("mouseout", area_comm.polygonMouse_close);
	            }
              
		 },
		 
		 passengerFlowClick: function(e) {
            var content = e.target.content; //地图上所标点的坐标    
            area_comm.infoWindow.setContent(content);
            area_comm.infoWindow.open(area_comm.mapObj, e.target.getCenter());
		 },
		 polygonMouse_close: function(e) {
        	area_comm.infoWindow.close();
		 },
		 
		 initTable : function(data) {
			area_comm.mytable = $('#platform-tab').DataTable({
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
					title:'站台名称',
					data : 'name'
				}, 
				{
					title:'创建时间',
					data : 'createTime'
				},
				{
					title:'经度',
					data : "longitude"
				},
				{
					title:'纬度',
					data : "latitude"
				}
				],
                  select: {
                      style:    'os',
                      selector: 'td:first-child'
                      }

			});
		},
		
		returnMgmtPage : function () {
			require(['districtManagement/src/area_mgmt'], function (area_mgmt) {
				area_mgmt.show();
        	});
		},
		
		drawSelectedStation : function (data) {
			area_comm.initStationMap(data, 'circle0');
		},
		
		drawAllStation : function (areaId) {
			comm.requestJson('/report/auxiliaryLine/queryAllPlatform', null, function(resp) {
	            if(resp.resCode == 200){
	            	area_comm.initStationMap(resp.data.platforms, 'circle');
	            	
	            	if(comm.isNotEmpty(areaId)){
	            		area_comm.loadTabAndSelectStation(areaId);
	            	}
	            }else{
	            	alert(resp.msg);
	            }
			},function(resp){
				alert("调用失败");
			}); 
		},
		
		drawArea : function (areaId) {
		   comm.requestJson('/report/areaMap/selectAreaMapByAreaId', areaId, function(resp) {
	            if(resp.code == 0){
	            	var position = resp.data;
	            	var areapath=[];
	            	for(var j = 0; j<position.length-1; j++){			            	  
	   	             	areapath.push([position[j].ing, position[j].lat]);
	            	}
	            	
	            	if(areapath.length > 0){
	            		area_comm.drawAreaMap(areapath);
	            	}
	            }else{
	            	alert(resp.msg);
	            }
			},function(resp){
				alert("调用失败");
			}); 
		},
		
		drawAreaMap : function (data) {
			area_comm.polygon = new AMap.Polygon({
                path: data,
                // 设置多边形边界路径
                strokeColor: "#f00",
                // 线透明度
                strokeWeight: 3,
                // 线宽
                fillColor: "#3366FF",
                // 填充色
                fillOpacity: 0.35,
                // 填充透明度
                map: area_comm.mapObj
            });
			area_comm.mapObj.setFitView();
			area_comm.initAreaData = data;
		},
		
		loadTabAndSelectStation : function (areaId){
			comm.requestJson('/report/areaStationManagement/queryAreaPlatform', areaId, function(resp) {
		        if(resp.code == 0){
		        	area_comm.platformData = resp.data;
		        	area_comm.initTable(resp.data);
		        	//画附近的站台
					area_comm.drawSelectedStation(resp.data);
		        }else{
		        	alert(resp.msg);
		        }
	        },function(reap){
	        	alert("站台加载失败...");
	        });
		}
};