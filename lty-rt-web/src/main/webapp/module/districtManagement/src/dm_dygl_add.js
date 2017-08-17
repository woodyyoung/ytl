define(
		[ 'text!districtManagement/tpl/dm_dygl_add.html' ],
		function(tpl) {
			var self = {
				mytable : null,
				show : function(dataId) {
					$('.index_padd').html('');
					
					if(dataId != null){//edit
						if(dataId.indexOf("add") > -1){//add非顶级区域
							//初始化表单数据
							self.initTableData(dataId.substring(0,dataId.length -3), "add");
						}else if(dataId.indexOf("edit") > -1){
							//初始化表单数据
							self.initTableData(dataId.substring(0,dataId.length -4), "edit");
						}
					}else{//add顶级菜单
						self.initTableData(null, null);
					};
					
					//保存按钮
					 $('#dygl_save').click(function(){
						 self.save();
					 });
					 
					//返回按钮
					 $('#dygl_back').click(function(){
						 require(['districtManagement/src/dm_dygl'], function (psgfLevel) {
	                		 psgfLevel.show();
	                	 });
					 });
					 $('.demo2').colorpicker({
					        format: 'hex'
					 });
					
				},
				initTableData : function(areaCodeId, flag) {
					 var params = {};
					 params.codeid = areaCodeId;
					 if(areaCodeId != null && areaCodeId != ''){
						 if(flag != null && flag == 'add'){
							 $('.index_padd').html(_.template(tpl, params));
						 }else if(flag != null && flag == 'edit'){
							 comm.requestJson('/report/districtManagement/getPjmkAreaByPrimaryKey',areaCodeId,function(resp){
								 if(resp.code == 0){
									 $('.index_padd').html(_.template(tpl, resp.data));
									 
									//保存按钮
									 $('#dygl_save').click(function(){
										 self.save();
									 });
									 
									//返回按钮
									 $('#dygl_back').click(function(){
										 require(['districtManagement/src/dm_dygl'], function (psgfLevel) {
					                		 psgfLevel.show();
					                	 });
									 });
									 
									 $('.demo2').colorpicker({
									        format: 'hex'
									 });
									
								 }else{
									 alert(resp.msg);
								 }
							
								 
							 });
						 }
					 }else{
						 var param = {};
						 param.codeid = areaCodeId;
						 $('.index_padd').html(_.template(tpl, param));
					 }
				},
				save:function(){
					 var params =  $("#dygl_form").serializeJson();
					 params.properties = $("#propertiesid option:selected").text();
					 comm.requestDefault('/report/districtManagement/insertPjmkArea',params,function(resp){
						 if(resp.code == 0){
							 require(['districtManagement/src/dm_dygl'], function (psgfLevel) {
		                		 psgfLevel.show();
		                	 });
						}else{
							alert(resp.msg);
						}
					 });
				}
		
		};
			
		return self;
		
			
		});