define(
		[ ],
		function() {
			var self = {
				myTreeTable : null,
				mapObj:null,
				stretchMgt:null,
				mytable:null,
				show : function(stretchMgt) {
					self.stretchMgt = stretchMgt;
					//清空map
					self.stretchMgt.mapObj.clearMap();
					//加载关联站台
					self.initTableData(stretchMgt.stretchCode);

					//删除关联按钮
					$('#btn-del-platform').unbind('click');
					$('#btn-del-platform').click(function(){
						self.deleteGlPaltforms();
					});
					//加载所有站台
					self.loadPaltform();
					//加载路段
					self.loadStretchMap();
					
				},
				//加载路段
				loadStretchMap:function(){
					comm.requestJson('/report/stretchMap/selectStretchMapByStretchId', self.stretchMgt.stretchId,function(resp) {
			            if(resp.code == 0){
			            	var data = resp.data;
			            	var areapath = [];
			            	for(var j = 0; j<data.length-1; j++){			            	  
					             areapath.push([data[j].ing, data[j].lat],[data[j+1].ing, data[j+1].lat] );
							};    
			            	self.drawLine(areapath,'#4fe516','');
			            }else{
			            	comm.alert_tip(resp.msg);
			            }
					},function(resp){
						comm.alert_tip("调用失败");
					});
				},
				//绘制路段
				drawLine: function(lineLpath, strokeColor, extData) {
					self.stretchMgt.polyline = new AMap.Polyline({
		                map: self.stretchMgt.mapObj,
		                path: lineLpath,
		                strokeColor: strokeColor,
		                strokeOpacity: 0.9,
		                strokeWeight: 6,
		                extData:extData
		            });
		            self.stretchMgt.mapObj.setFitView();
		            //self.polylineArray.push(polyline);
		        },
				//加载站台
				loadPaltform:function(){
					comm.requestJson('/report/stretchPlatformManagement/findNotRefrencePlatfom', self.stretchMgt.stretchCode, function(resp) {
			            self.drawPaltform('#000000',resp.data);
					},function(resp){
						comm.alert_tip("调用失败");
					}); 
				},
				//绘制站台
				drawPaltform:function(color,data){
				 	for (var i = 0; i < data.length; i++) {
		                var circle = new AMap.Circle({
		                    radius: 20,
		                    center: [data[i].LONGITUDE, data[i].LATITUDE],
		                    strokeColor: color,
		                    strokeWeight: 1,
		                    fillColor: color,
		                    fillOpacity: 0.8,
		                    zIndex:98,
		                    map: self.stretchMgt.mapObj
		                });

		                var template = "<div class='passengerFlowInfo'><h2 class='passengerFlowInfo_title'>" + data[i].NAME + "</h2></div>";
		                circle.content = template;
		                circle.on("click", self.passengerFlowClick);
		                circle.on("mouseover", self.passengerFlowClick);
		                circle.on("mouseout", self.polygonMouse_close);
		            }
				},
				passengerFlowClick: function(e) {
		            var content = e.target.content; // 地图上所标点的坐标
		            self.stretchMgt.infoWindow.setContent(content);
		            self.stretchMgt.infoWindow.open(self.stretchMgt.mapObj, e.target.getCenter());
				},
				polygonMouse_close: function(e) {
					self.stretchMgt.infoWindow.close();
				},
				initTableData : function(data) {
					comm.requestJson('/report/stretchPlatformManagement/selectStretchPlat',
							data, 
							function(resp) {
								if(resp.code == 0){
									 self.initTable(resp.data); 
									 self.drawPaltform("#0b0bf1", resp.data);
								}else{
									comm.alert_tip(resp.msg);
								}
					        },function(reap){
					        	comm.alert_tip("站台加载失败...")
					        });
				},
				initTable : function(data) {
					self.mytable = $('#gl_platform_table').DataTable({
						// 每页显示三条数据
						// pageLength : 5,
						destroy : true,
						data : data,
						order : [ [ 1, "desc" ] ],
						language : dataTable_cn,
						columns : [ {
							data : null,
							defaultContent : '',
							className : 'select-checkbox',
							orderable : false
						}, /*{
							title : '编号',
							data : 'numbered'
						},*/ {
							title : '站台名称',
							data : 'NAME'
						}, {
							title : '经度',
							data : "LONGITUDE"
						}, {
							title : '维度',
							data : "LATITUDE"
						}/*, {
							title : '备注',
							data : "remark"
						}*/],
						select : {
							style : 'os',
							selector : 'td:first-child'
						}

					});
				},
				deleteGlPaltforms:function(){
					if(comm.isEmpty(self.stretchMgt.stretchCode)){
						comm.alert_tip("请先选择路段");
						return false;
					}
					var rowDataId = self.getTableContent();
					if(rowDataId.split(",").length < 1 || rowDataId == ''){
						comm.alert_tip("请选择一条记录");
						return false;
					}
					var params = {};
					params.ldid = self.stretchMgt.stretchCode;
					params.platFormId = rowDataId;
					comm.requestJson('/report/stretchPlatformManagement/delByIds', JSON.stringify(params), function(resp){
						if(resp.code == 0){
							comm.alert_tip('删除成功!');
							self.show(self.stretchMgt);
							
						}else{
							comm.alert_tip(resp.msg);
						}
					},function(resp){
						comm.alert_tip("删除失败...")
					});
				},
				getTableContent : function() {  
					var nTrs = self.mytable.rows('.selected').data();
					var retStr = "";
				       
					if(nTrs != null && nTrs.length > 0){
						for(var k = 0; k < nTrs.length; k++){
							retStr += nTrs[k].PLATFORMID +",";
						}
						retStr = retStr.substring(0,retStr.length-1);
				       }
					return retStr;
				}
		}
			
		return self;
		});