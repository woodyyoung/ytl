define(
		[ 'text!districtManagement/tpl/area_mgmt.html' ],
		function(tpl) {
			var self = {
				show : function (){
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					
					self.initLeftTree();
				},
				
				initLeftTree : function() {
					comm.requestJson('/report/districtManagement/getDistrictTree', null,
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
					
					if(data != null && data.length > 0){
						var areaId = data[0].arg;
						var codeId = data[0].id;
						data[0].state = {
							expanded: true,
							selected: true
						};
						require(['districtManagement/src/dir_right'], function (dir) {
							dir.show(areaId, codeId);
	                	});
					}
					
					self.myTreeTable = $('#treeview-searchable').treeview({
						levels: 1,
			            showBorder: false,
			            showTags: true,
			            data:data
					});
					
					//左树点击事件
					$("#treeview-searchable").on("nodeSelected",function(event,defaultData){
						var codeId = defaultData.id;
						var areaId = defaultData.arg;
						var type = defaultData.arg1;
						
						if(type == 1){
							require(['districtManagement/src/dir_right'], function (dir) {
								dir.show(areaId, codeId,self);
		                	});
						} else if(type == 2) {
							require(['districtManagement/src/area_right'], function (area) {
								area.show(areaId, codeId,self);
		                	});
						}
					});
				}	
			}
			return self;
		});
			    