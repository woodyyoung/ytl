define([ 'text!districtManagement/tpl/area_right.html' ],
	function(tpl) {
		var self = {
			show : function(areaId, codeId,mgmt) {
				$("#area_display").html(tpl);
				
				//初始化小区基本信息
				comm.requestJson('/report/districtManagement/getPjmkAreaByPrimaryKey', areaId, function(resp){
					if(resp.code == 0){
						var areaInfo = resp.data;
						$("#codeName").html(resp.data.codename);
						$("#properties").html(resp.data.properties);
						$("#area").html(resp.data.area);
						$("#remark").html(resp.data.remark);
					}else{
						alert(resp.msg);
					}
				},function(resp){
					alert("加载失败...");
				});
				
				//加载地图
				area_comm.initMap();
				
				//画小区
				area_comm.drawArea(areaId);
				
				//画所有的站台
				area_comm.drawAllStation(areaId);
				
				$("#btn-update").click(function () {
					require(['districtManagement/src/area_add'], function (area_add) {
						area_add.show(areaId, codeId);
               	 	});
				});
				
				$("#btn-del").click(function () {
					comm.alert_confirm('确定要删除此区域吗?',function(){
						comm.requestJson('/report/districtManagement/delById', areaId, function(resp){
							if(resp.code == 0){
								mgmt.show();
							/*	area_comm.initLeftTree();
								area_comm.initTableData();*/
							}else{
								alert(resp.msg);
							}
						},function(resp){
							alert("删除失败...");
						});
						
						area_comm.returnMgmtPage();
						
					});
					
				});
			}
		};
		return self;
	});