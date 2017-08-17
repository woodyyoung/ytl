define(
	[ 'text!districtManagement/tpl/area_add.html' ],
	function(tpl) {
		var self = {
			type : 0,
			areaInfo : null,
			mode : null,
			areaId : null,
			codeId : null,
			show : function(areaId, codeId) {
				if(comm.isEmpty(codeId)){
					return;
				}
				
				$('.index_padd').html(tpl);
				self.areaId = areaId;
				self.codeId = codeId;
				
				if(comm.isEmpty(areaId)){
					//新增模式
					self.type = 1;
					self.mode = 'add';
					
					area_comm.platformData = null;
					area_comm.oppath = null;
				}else{
					self.mode = 'modify';
					//修改模式
					//初始化小区基本信息
					comm.requestJson('/report/districtManagement/getPjmkAreaByPrimaryKey', areaId, function(resp){
						if(resp.code == 0){
							self.areaInfo = resp.data;
							$("#codename").val(self.areaInfo.codename);
							var type = self.areaInfo.propertiesid;
							$("#properties option[value='"+type+"']").attr("selected",true);
							$("#area").val(self.areaInfo.area);
							$("#remark").html(self.areaInfo.remark);
							
							if(type == 2){
								require(['districtManagement/src/area_config'], function (config) {
									config.show(areaId);
			                	});
							}
							self.type = type;
						}else{
							alert(resp.msg);
						}
					},function(resp){
						alert("加载失败...");
					});
				}
				
				$("#propertiesid").change(function (){
					var type = $("#propertiesid").val();
					if(type == 1){
						$('#area_config').html("");
					}else if(type == 2){
						require(['districtManagement/src/area_config'], function (config) {
							config.show(areaId);
	                	});
					}
					self.type = type;
				});
				
				//点击返回
				$("#dygl_back").click(function (){
					area_comm.returnMgmtPage();
				});
				
				$("#dygl_save").click(function (){
					self.saveArea();
				});
			},
			
			//保存小区
			saveArea : function (){
				var codename = $("#codename").val();
				var area = $("#area").val();
				var properties = $("#properties").find("option:selected").text();
				var propertiesid = $("#properties").find("option:selected").val();
				var remark = $("#remark").val();
				
				if(comm.isEmpty(codename)){
					comm.alert_tip("请输入区域名称!");
					return false;
				}
				
				if(comm.isEmpty(area)){
					comm.alert_tip("请输入区域面积!");
					return false;
				}
				
				//基本信息处理
				if(self.mode == 'add'){
					self.areaInfo = {};
				}
				
				self.areaInfo.codename = codename;
				self.areaInfo.area = area;
				self.areaInfo.properties = properties;
				self.areaInfo.propertiesid = propertiesid;
				self.areaInfo.remark = remark;
				self.areaInfo.codeid = self.codeId;
				var areaData = {areaInfo : self.areaInfo};
				
				//一般小区
				if(self.type == 2){
					//对选择的站台保存处理
					if(comm.isNotEmpty(area_comm.platformData) && area_comm.platformData.length > 0){
						var selectPlatInfo = [];
						for(var i=0; i<area_comm.platformData.length; i++){
							var platInfo = {
								areaId : self.areaId,
								platFormId : area_comm.platformData[i].id,
								areaCodeId : self.codeId
							};
							selectPlatInfo.push(platInfo);
						}
						areaData.selectPlatInfo = selectPlatInfo;
					}else{
						areaData.selectPlatInfo = null;
					}
					
					//对地图小区进行处理
					if(area_comm.oppath != null){
						var areaMaps = [];
						for(var i=0; i<area_comm.oppath.length; i++){
							var areaMap = {
								areaId : self.areaId,
								lat : area_comm.oppath[i].lat,
								ing : area_comm.oppath[i].lng,
								orderby : i
							};
							areaMaps.push(areaMap);
						}
						areaData.areaMaps = areaMaps;
					}else{
						if(area_comm.initAreaData == null){
							comm.alert_tip("请在地图上绘制区域");
							return false;
						}
					}
				}
				
				//保存小区数据
				comm.requestJson('/report/districtManagement/saveAreaInfo', JSON.stringify(areaData), function(resp){
					if(resp.code == 0){
						area_comm.platformData = null;
						area_comm.oppath = null;
						area_comm.returnMgmtPage();
					}else{
						alert(resp.msg);
					}
				},function(resp){
					alert("保存失败...");
				});
			}
		};
		return self;
});