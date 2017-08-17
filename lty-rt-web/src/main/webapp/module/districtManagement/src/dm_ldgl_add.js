define(
		[ 'text!districtManagement/tpl/dm_ldgl_add.html' ],
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
					 $('#ldgl_save').click(function(){
						 self.save();
					 });
					 
					//返回按钮
					 $('#ldgl_back').click(function(){
						 require(['districtManagement/src/dm_ldgl'], function (psgfLevel) {
	                		 psgfLevel.show();
	                	 });
					 });
					 $('.demo2').colorpicker({
					        format: 'hex'
					 });
					
				},
				initTableData : function(lineid, flag) {
					 var params = {};
					 params.lineid = lineid;
					 if(lineid != null && lineid != ''){
						 if(flag != null && flag == 'add'){
							 $('.index_padd').html(_.template(tpl, params));
						 }else if(flag != null && flag == 'edit'){
							 comm.requestJson('/report/stretchManagement/getStretchByPrimaryKey',lineid,function(resp){
								 if(resp.code == 0){
									 $('.index_padd').html(_.template(tpl, resp.data));
										
									//保存按钮
									 $('#ldgl_save').click(function(){
										 self.save();
									 });
									 
									//返回按钮
									 $('#ldgl_back').click(function(){
										 require(['districtManagement/src/dm_ldgl'], function (psgfLevel) {
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
						 param.lineid = lineid;
						 $('.index_padd').html(_.template(tpl, param));
					 }
				},
				save:function(){
					 var params =  $("#ldgl_form").serializeJson();
					 comm.requestDefault('/report/stretchManagement/insertStretch',params,function(resp){
						 if(resp.code == 0){
							 require(['districtManagement/src/dm_ldgl'], function (psgfLevel) {
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