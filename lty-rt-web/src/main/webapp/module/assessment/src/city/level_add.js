define(
		[ 'text!assessment/tpl/city/level_add.html' ],
		function(tpl) {
			var self = {
				mytable : null,
				show : function(dataId) {
					$('.index_padd').html('');
					
					//初始化表单数据
					self.initTableData(dataId);
					
					//保存按钮
					 $('#level_save').click(function(){
						 self.save();
					 });
					 
					//返回按钮
					 $('#level_back').click(function(){
						 require(['assessment/src/city/level'], function (psgfLevel) {
	                		 psgfLevel.show();
	                	 });
					 });
					 /*$('.demo2').colorpicker({
					        format: 'hex'
					 });*/
					
				},
				initTableData : function(dataId) {
					if(dataId == null || dataId == ''){//add
						$('.index_padd').html(_.template(tpl, null));
					}else{//edit
						comm.requestJson('/report/level/getLevelById',dataId,function(resp){
							 if(resp.code == 0){
								 $('.index_padd').html(_.template(tpl, resp.data));
								 
								 $('#add_index_level').attr('readOnly','readOnly');
									
								//保存按钮
								 $('#level_save').click(function(){
									 self.save();
								 });
								 
								//返回按钮
								 $('#level_back').click(function(){
									 require(['assessment/src/city/level'], function (psgfLevel) {
				                		 psgfLevel.show();
				                	 });
								 });
								 $('.demo2').colorpicker({
								        format: 'hex'
								 });
							
							 }else{
								comm.alert_tip(resp.msg);
							 }
						
						 });
					}
				},
				save:function(){
					 var params =  $("#level_form").serializeJson();
					 if(comm.isEmpty(params.levels)){
						 comm.alert_tip('评价等级不能为空!');
						 return false;
					 }
					 if(comm.isEmpty(params.levels)){
						 comm.alert_tip('评价等级不能为空!');
						 return false;
					 }
					 if(comm.isEmpty(params.description)){
						 comm.alert_tip('评价等级描述不能为空!');
						 return false;
					 }
					 
					 comm.requestDefault('/report/level/insertLevel',params,function(resp){
						 if(resp.code == 0){
							 require(['assessment/src/city/level'], function (psgfLevel) {
		                		 psgfLevel.show();
		                	 });	
						 }else{
							comm.alert_tip(resp.msg);

						 }
					 });
				}
		
		};
			
		return self;
		
			
		});