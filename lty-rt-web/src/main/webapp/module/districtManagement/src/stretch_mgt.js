define(
		[ 'text!districtManagement/tpl/stretch_mgt.html', 'text!districtManagement/tpl/stretch_add.html' ],
		function(tpl,addTpl) {
			var self = {
				myTreeTable : null,
				mapObj:null,
				infoWindow :null,
				myplatTable:null,
				stretchId:null,//路段ID UUID
				stretchCode:null,//路段编号  004 
				oppath:null,
				mouseTool:null,
				polyline:null,
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					//初始化左树
					self.initLeftTree();
					//初始化地图
					self.initMap();
					
					//添加站台按钮
					$('#btn-add-platform').click(function(){
						if(comm.isEmpty(self.stretchId)){
							comm.alert_tip("请先选择路段！");
							return false;
						}
						self.showAddPlatformDialog();
					});
					//确认添加站台
					$('#determineAdd').click(function(){
						self.saveAddPlatform();
					});
					//添加路段按钮
					$('#btn-insert').click(function(){
						self.addStretch();
					});
					//修改路段按钮
					$('#btn-update').click(function(){
						self.updateStretch();
					});
					
					//删除路段按钮
					$('#btn-del').click(function(){
						 self.deleteStretch();
					});
					
				},
				//添加路段
				addStretch:function(){
					$('#dialogDiv').html('');
					$('#dialogDiv').html(_.template(addTpl, {}));
					$("#addStretchModal").modal({
						keyboard: true
					});
					$('#addStretchModal').modal('show');
					//绑定保存事件
					$('#btn_add_stretch').unbind('click');
					$('#btn_add_stretch').click(function(){
						self.saveStretch();
					});
				},
				//修改路段信息
				updateStretch:function(){
					if(comm.isEmpty(self.stretchId)){
						comm.alert_tip("请选择修改的路段");
						return false;
					}
					comm.requestJson('/report/stretchManagement/getStretchByPrimaryKey',self.stretchId,function(resp){
						 if(resp.code == 0){
							 $('#dialogDiv').html('');
							 $('#dialogDiv').html(_.template(addTpl, resp.data));
							 $('#myModalLabelTile').html('修改路段');
							 $("#addStretchModal").modal({
									keyboard: true
							 });
							 $('#addStretchModal').modal('show');
							 $('#btn_add_stretch').unbind('click');
							 //绑定保存事件
							 $('#btn_add_stretch').click(function(){
								self.saveStretch();
							 });
						
						 }else{
							 comm.alert_tip(resp.msg);
						 }
					
					 });
				},
				//删除路段
				deleteStretch:function(){
					if(comm.isEmpty(self.stretchId)){
						comm.alert_tip("请选择删除的路段");
						return false;
					}
					comm.alert_confirm('确定要删除此路段吗?',function(){
						comm.requestJson('/report/stretchManagement/delById', self.stretchId, function(resp){
							if(resp.code == 0){
								comm.alert_tip("删除成功");
								self.show();
							}else{
								comm.alert_tip(resp.msg);
							}
						},function(resp){
							comm.alert_tip("删除失败...")
						});
					});
				},
				//保存路段基本信息
				saveStretch:function(){
					var linename = $.trim($('#add_linename').val());
					$('#add_linename').val(linename);
					if(comm.isEmpty(linename)){
						comm.alert_tip("路段名称必须填写！");
						return false;
					}
					var linelength = $.trim($('#add_linelength').val());
					$('#add_linelength').val(linelength);
					if(comm.isEmpty(linelength)){
						$('#add_linelength').val('0');
					}
					if(isNaN(linelength)){
						comm.alert_tip("长度为需为数字");
						return false;
					}
					 var params =  $("#ldgl_form").serializeJson();
					 $('#addStretchModal').modal('hide');
					 $('.modal-backdrop').remove();
					 comm.requestDefault('/report/stretchManagement/insertStretch',params,function(resp){
						 if(resp.code == 0){
							 comm.alert_tip("操作成功！");
							 self.show();
						 }else{
							 comm.alert_tip(resp.msg);
						 }
					 });
				},
				
				initLeftTree : function() {
					comm.requestJson('/report/stretchManagement/getDistrictTree', null,
							function(resp) {
						        if(resp.code == 0){
						        	self.initTree(resp.data);
						        }else{
						        	comm.alert_tip(resp.msg);
						        }
							},function(resp){
								comm.alert_tip("区域树加载失败...")
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
						self.stretchId = defaultData.arg;
						self.stretchCode = defaultData.id;
						require(['districtManagement/src/stretch_info'], function (info) {
							info.show(self);
	                	});
					});
					 
					//左树取消点击事件
					$("#treeview-searchable").on("nodeUnselected",function(event,defaultData){
						self.stretchId = '';
						self.stretchCode = '';
					});
					
					//默认展开第一个节点 	
					$('li[data-nodeid=0]').click();
				},
				
			
				initMap : function(){
					
					//地图
					self.mapObj = new AMap.Map('visitorsFlowRate_map',{
						 resizeEnable:true,
					     center:[109.4, 24.33 ],
					     mapStyle: "light",
					     zoom:12
					 });
					
					self.infoWindow = new AMap.InfoWindow({
		                isCustom: false,
		                // 使用自定义窗体
		                offset: new AMap.Pixel(0, -15) // -113, -140
		            });
					
					 var polygonOptions = {
							 map: self.mapObj,
							 strokeColor: '#f00',
							 strokeWeight: 2
					 };
					 
					 //打开画笔
					 self.mouseTool = new AMap.MouseTool(self.mapObj); //在地图中添加MouseTool插件
					 $('#opendraw').click(function(){
						 if(null!=self.polyline){
							 self.polyline.setOptions({
					                strokeOpacity: 0.35
					         });
						 }
						 self.mouseTool.polyline(); //用鼠标工具画矩形		
						 AMap.event.addListener(self.mouseTool,'draw',self.getDrawPath);
					 });
					 
					 //清空
					 $('#cleardraw').click(function(){
						 var overlays = self.mapObj.getAllOverlays('polyline');
						 
						 self.mapObj.remove(overlays);
						 self.polyline.setMap(null);
					 });
					 
					 //撤销至上一步
					$('#resetdraw').click(function(){
						self.removeLastLine();
					 });
					 
					//保存画笔地域
					 $('#drawsubmit').click(function(){
						 if(comm.isEmpty(self.stretchId)){
							 comm.alert_tip('请先选择路段！');
							 return false;
						 }
						 if(comm.isEmpty(self.oppath)){
							 comm.alert_tip('请先绘制路段！');
							 return false;
						 }
						 var params = {};
						 params.stretchId = self.stretchId;
						 params.oppath = self.oppath;
						 comm.requestJson('/report/stretchMap/insertStretchMap', JSON.stringify(params),
							function(resp) {
					            if(resp.code == 0){
					            	comm.alert_tip("保存成功");
					            	require(['districtManagement/src/stretch_info'], function (info) {
										info.show(self);
				                	});
					            }else{
					            	comm.alert_tip(resp.msg);
					            }
							},function(resp){
								comm.alert_tip("保存失败");
						});
					 });
					 
					 
					
					
				},
				//撤销
				removeLastLine : function(){
					var overlays=self.mapObj.getAllOverlays('polyline');
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
				getDrawPath:function(e){
					 //获取路径信息
					 self.oppath = e.obj.getPath();
				},
				showAddPlatformDialog:function(){
					comm.requestJson('/report/stretchPlatformManagement/getPlatFormList', null,
						function(resp) {
							if(resp.code == 0){
								self.initAddPlatformTable(resp.data);
							}else{
								comm.alert_tip(resp.msg);
							}
						},function(resp){
							comm.alert_tip("区域树加载失败...")
						});
				},
				
				initAddPlatformTable : function(data){
					self.myplatTable = $('#addPlatForm').DataTable({
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
						},/* {
							title : '编号',
							data : 'PLATFORMID'
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
					
					$("#myModal").modal({
						keyboard: true
					});
					$('#myModal').modal('show');
				},
				//保存关联站台
				saveAddPlatform:function(){
					if(comm.isEmpty(self.stretchCode)){
						comm.alert_tip("请先选中路段再添加站台！");
						return false;
					}
					//获取选中的站台id
					var rowDataId = self.getAddTableContent();
					if(rowDataId.split(",").length < 1 || rowDataId == ''){
						comm.alert_tip("请选择一条记录");
						return false;
					}
					
					var params = {};
					params.stretchid = self.stretchId+","+self.stretchCode;
					params.platFormId = rowDataId;
					comm.requestJson('/report/stretchPlatformManagement/insertFromPlat', JSON.stringify(params), function(resp){
						if(resp.code == 0){
							comm.alert_tip("添加站台成功");
							$("#myModal").modal('hide');
				        	require(['districtManagement/src/stretch_info'], function (info) {
								info.show(self);
		                	});
						}else{
							comm.alert_tip(resp.msg);
						}
					},function(resp){
						comm.alert_tip("添加站台失败...");
						return false
					});
				},
				getAddTableContent : function() {  
					var nTrs = self.myplatTable.rows('.selected').data();
					var retStr = "";
					if(nTrs != null && nTrs.length > 0){
						for(var k = 0; k < nTrs.length; k++){
							retStr += nTrs[k].PLATFORMID +",";
						}
						retStr = retStr.substring(0,retStr.length-1);
				       }
					return retStr;
				},
				
			
		}
			
		return self;
		});