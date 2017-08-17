define([ 'text!districtManagement/tpl/dir_right.html' ],
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
				
				$("#btn-insert").click(function () {
					require(['districtManagement/src/area_add'], function (area_add) {
						area_add.show('', codeId);
               	 	});
				});
				
				$("#btn-update").click(function () {
					require(['districtManagement/src/area_add'], function (area_add) {
						area_add.show(areaId, codeId);
               	 	});
				});
				
				$("#btn-del").click(function () {
					if(comm.isEmpty(areaId)){
						return;
					}
					comm.alert_confirm('确定要删除此区域吗?',function(){
						comm.requestJson('/report/districtManagement/delById', areaId, function(resp){
							if(resp.code == 0){
								mgmt.show();
								//area_comm.initTableData();
								//alert("删除成功");
							}else{
								comm.alert_tip(resp.msg);
							}
						},function(resp){
							comm.alert_tip("删除失败...")
						});
						
						area_comm.returnMgmtPage();
					
					});
				});
			}
		};
		return self;
	});