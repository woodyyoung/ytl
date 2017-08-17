define(
	[ 'text!districtManagement/tpl/area_config.html' ],
	function(tpl) {
		var self = {
			availablePlat : null,
			myplatTable : null,
			show : function(areaId) {
				$('#area_config').html(tpl);
				area_comm.platformData = null;
				area_comm.oppath = null;
				area_comm.initMap();
				
				if(comm.isNotEmpty(areaId)){
					//画小区
					area_comm.drawArea(areaId);
					//画所有的站台
					area_comm.drawAllStation(areaId);
					
				}else{
					area_comm.initTable();
					//画所有的站台
					area_comm.drawAllStation(areaId);
				}
				
				//清空
			    $('#cleardraw').click(function(){
				    area_comm.mouseTool.close(true);
				    if(comm.isNotEmpty(area_comm.polygon)){
				    	area_comm.polygon.setMap(null);
				    }
				    area_comm.oppath = null;
			    });
			    
			    $('#add-platform').click(function (){
			    	if(comm.isEmpty(self.availablePlat)){
			    		self.initPlatAjax();
			    	}else{
			    		self.initPlat(self.availablePlat);
			    	}
			    	$(this).attr('data-target','#myModal');
			    });
			    
			    //确认添加站台
				$('#determineAdd').click(function(){
					self.addPlatform();
				});
				
				//删除站台
				$("#del-platform").click(function (){
					self.delPlatform();
				});
			},
			
			delPlatform : function (){
				var selectPlatform = self.getAddTableContent(area_comm.mytable);
				if(selectPlatform.length == 0){
					comm.alert_tip("请选择一条记录");
					return false;
				}
				
				//删除表格中站台
				for(var i=0; i<selectPlatform.length; i++){
					area_comm.platformData.splice($.inArray(selectPlatform[i], area_comm.platformData), 1);
				}
				area_comm.initTable(area_comm.platformData);
				
				//添加到可用站台
				if(comm.isEmpty(self.availablePlat)){
					comm.requestJson('/report/areaStationManagement/queryAvailablePlatform', null,
						function(resp) {
					        if(resp.code == 0){
					        	self.availablePlat = resp.data;
					        }
						});
				}
				$.merge(self.availablePlat, selectPlatform);
				
				//地图画站台
				area_comm.initStationMap(selectPlatform, 'circle');
			},
			
			addPlatform : function (){
				//获取选中的站台id
				var selectPlatform = self.getAddTableContent(self.myplatTable);
				if(selectPlatform.length == 0){
					comm.alert_tip("请选择一条记录");
					return false;
				}
				
				//删除可用站台
				for(var i=0; i<selectPlatform.length; i++){
					self.availablePlat.splice($.inArray(selectPlatform[i], self.availablePlat),1);
				}
				
				//重新加载表格
				if(comm.isEmpty(area_comm.platformData)){
					area_comm.platformData = [];
				}
				$.merge(area_comm.platformData, selectPlatform);
				area_comm.initTable(area_comm.platformData);
				
				//地图画站台
				area_comm.initStationMap(selectPlatform, 'circle0');
				
				//关闭modal
				$("#closeModal").click();
			},
			
			initPlatAjax : function(){
				comm.requestJson('/report/areaStationManagement/queryAvailablePlatform', null,
					function(resp) {
				        if(resp.code == 0){
				        	self.availablePlat = resp.data;
				        	self.initPlat(resp.data);
				        }else{
				        	//alert(resp.msg);
				        }
					},function(resp){
						//alert("区域树加载失败...")
					});
			
			},
			
			initPlat : function(data){
				self.myplatTable = $('#addPlatForm').DataTable({
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
			
			getAddTableContent : function(table) {  
				var nTrs = table.rows('.selected').data();
				var selectPlatform = [];
			       
				if(nTrs != null && nTrs.length > 0){
					for(var k = 0; k < nTrs.length; k++){
						selectPlatform.push(nTrs[k]);
					}
			       }
				return selectPlatform;
			}
		};
		return self;
});