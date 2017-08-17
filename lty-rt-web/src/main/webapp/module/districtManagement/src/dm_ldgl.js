define(
		[ 'text!districtManagement/tpl/dm_ldgl.html' ],
		function(tpl) {
			var self = {
				mytable : null,
				myTreeTable : null,
				mapObj:null,
				oppath:null,
				mouseTool:null,
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					
					//初始化左树
					self.initLeftTree();
					
					//初始化列表数据
					self.initTableData();
					
					//初始化地图
					self.initMap();
					
					//删除按钮
					$('#btn-del').click(function(){
						var rowDataId = self.getTableContent();
						if(rowDataId.split(",").length < 1 || rowDataId == ''){
							alert("请选择一条记录");
							return false;
						}
						comm.requestJson('/report/stretchManagement/delById', rowDataId, function(resp){
							if(resp.code == 0){
								self.initLeftTree();
								self.initTableData();
								alert("删除成功");
							}else{
								alert(resp.msg);
							}
						},function(resp){
							alert("删除失败...")
						});
					 });
					
					//新增按钮
					 $('#btn-insert').click(function(){
						 var ldglId = $("#ldglId").val();
						 require(['districtManagement/src/dm_ldgl_add'], function (dm_ldgl_add) {
							 dm_ldgl_add.show(ldglId+"add");
	                	 });
					 });
					
					//修改按钮
					 $('#btn-update').click(function(){
						 var rowDataId = self.getTableContent();
						 if(rowDataId.split(",").length > 1){
							 alert("请选择单行记录");
							 return false;
						 }
						 if(rowDataId.split(",").length < 1 || rowDataId == ''){
							 alert("请选择一条记录");
							 return false;
						 }
						 require(['districtManagement/src/dm_ldgl_add'], function (dm_ldgl_add) {
	                		 dm_ldgl_add.show(rowDataId+"edit");
	                	 });
		                	 
						//psfLevel_add.show(rowDataId);
					 });
					 
					//保存画笔地域
					 $('#drawsubmit').click(function(){
						 var rowDataId = self.getTableContent();
						 if(rowDataId.split(",").length < 1 || rowDataId == ''){
							 alert("请先选择一个区域再绘制地图");
							 return false;
						 }
						 if(rowDataId != '' && rowDataId.split(",").length > 1 ){
							 alert("不能选择多个区域绘制地图，请选择一个区域");
							 return false;
						 }
						 var params = {};
						 params.stretchId = rowDataId;
						 params.oppath = self.oppath;
						 comm.requestJson('/report/stretchMap/insertStretchMap', JSON.stringify(params),
									function(resp) {
							            if(resp.code == 0){
							            	alert("保存成功");
							            }else{
							            	alert(resp.msg);
							            }
									},function(resp){
										alert("保存失败");
									});
					 });
					 
					 //清空
					 $('#cleardraw').click(function(){
						 self.mouseTool.close(true);
					 });
					 
					 
					 /*$('#test').click(function(){
						 var rowDataId = self.getTableContent();
						 if(rowDataId != '' && rowDataId.split(",").length > 1 ){
							 alert("请选择一个路段");
							 return false;
						 }
							
						 comm.requestJson('/report/stretchMap/selectStretchMapByStretchId', rowDataId,
									function(resp) {
							            if(resp.code == 0){
							            	self.initAreaMap(resp.data);
							            	//self.initStationMap(resp.data);

							            	console.log(resp)
							            	alert("调用成功");
							            }else{
							            	alert(resp.msg);
							            }
									},function(resp){
										alert("调用失败");
									});
					 });*/
					 
				},
				initTableData : function(data) {
					var params = {arg : data};
					comm.requestJson('/report/stretchManagement/getStretchList', JSON.stringify(params), function(resp){
						if(resp.code == 0){
							self.initTable(resp.data);
						}else{
							alert(resp.msg);
						}
						
					},function(resp){
						alert("加载失败...")
					});
				},
				initTable : function(data) {
					self.mytable = $('#example').DataTable({
						//每页显示三条数据
						//pageLength : 5,
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
							title:'路段名称',
							data : 'linename'
						}, 
						{
							title:'编码',
							data : 'lineid'
						},
						{
							title:'父级编码',
							data : "parentlineid"
						},
						{
							title:'长度',
							data : "linelength"
						},
						{
							title:'轨迹',
							data : "track"
						},
						{
							title:'备注',
							data : "remark"
						}
						],
		                  select: {
		                      style:    'os',
		                      selector: 'td:first-child'
		                      }

					});
					$(".select-checkbox").click(function(){
						$(this).parent().toggleClass('selected');
						var rowDataId = self.getTableContent();
						if(rowDataId != ''){
							self.getMapData(rowDataId);
						}
					})
				},
				
				initLeftTree : function() {
					comm.requestJson('/report/stretchManagement/getDistrictTree', null,
							function(resp) {
						        if(resp.code == 0){
						        	self.initTree(resp.data);
						        }else{
						        	alert(resp.msg);
						        }
							},function(resp){
								alert("区域树加载失败...")
							});
				},
				
				initTree : function(data) {
					self.myTreeTable = $('#treeview-searchable').treeview({
						levels: 1,
			            showBorder: false,
			            showTags: true,
			            data:data
					});
					
					//左树点击事件
					$("#treeview-searchable").on("nodeSelected",function(event,defaultData){
						$("#ldglId").val(defaultData.id);
						var params = {parentLineId : defaultData.id };
						comm.requestJson('/report/stretchManagement/getStretchList',
								JSON.stringify(params), 
								function(resp) {
									if(resp.code == 0){
										 self.initTable(resp.data); 
									}else{
										alert(resp.msg);
									}
							       
						        },function(reap){
						        	alert("区域加载失败...")
						        });
					});
				},
				
				getTableContent : function() {  
					var nTrs = self.mytable.rows('.selected').data();
					var retStr = "";
				       
					if(nTrs != null && nTrs.length > 0){
						for(var k = 0; k < nTrs.length; k++){
							retStr += nTrs[k].id +",";
						}
						retStr = retStr.substring(0,retStr.length-1);
				       }
					return retStr;
				},
				initMap : function(){
					//地图
					self.mapObj = new AMap.Map('visitorsFlowRate_map',{
						 resizeEnable:true,
						 center:[109.4, 24.33 ],
					     zoom:12
					 });
					 
					 var polygonOptions = {
							 map: self.mapObj,
							 strokeColor: '#f00',
							 strokeWeight: 2
					 };
					 
					//打开画笔
					 self.mouseTool = new AMap.MouseTool(self.mapObj); //在地图中添加MouseTool插件
					 $('#opendraw').click(function(){
						 self.mouseTool.polyline(polygonOptions); //用鼠标工具画矩形		
						 AMap.event.addListener( self.mouseTool,'draw',cb);
					 });
					 
					 function cb(e){
						 //获取路径信息
						 self.oppath = e.obj.getPath();
					 }
				},
				
				//画线
				initAreaMap:function(data){
					self.mapObj.clearMap();
					for(var j = 0; j<data.length-1; j++){			            	  
			             var areapath=[];
			             areapath.push([data[j].ing, data[j].lat],[data[j+1].ing, data[j+1].lat] );
		    			 self.drawLine(areapath);
					};         
				 },
				 drawLine:function(areapath){
					 var busPolyline = new AMap.Polyline({
				            map: self.mapObj,
				            path: areapath,
				            strokeColor:  "#34b000",
				            strokeOpacity: 0.8,
				            strokeWeight: 6
				          
					 });
					 self.mapObj.setFitView();
				 },
				 //画点
				 initStationMap:function(data){
			    	   for(var i = 0; i<data.length; i++){
		                var marker;
		                var icon=null;
		                var div=null;
		                div = document.createElement('div');
		                div.className = 'circle';
		                /*div.innerHTML = i;*/
		                marker = new AMap.Marker({
		                  content:div,
		                  position: [data[i].ing, data[i].lat ],
		                  title: "data"+i,
		                  map: self.mapObj,
		                  icon:icon,
		                  zIndex: 10            
		                });  
		                self.mapObj.setFitView();
		              };
				 },
				 
				 getMapData : function(data){
					 comm.requestJson('/report/stretchMap/selectStretchMapByStretchId', data,
								function(resp) {
						            if(resp.code == 0){
						            	self.initAreaMap(resp.data);
						            	//self.initStationMap(resp.data);
						            }else{
						            	alert(resp.msg);
						            }
								},function(resp){
									alert("调用失败");
								});
				 }
		
		};
			
		return self;
		
			
		});