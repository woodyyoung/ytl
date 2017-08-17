define(
		[ 'text!assessment/tpl/city/index_add.html' ],
		function(tpl) {
			var self = {
				mytable : null,
				show : function(dataId, indexType) {
					$('.index_padd').html('');
					
					//初始化表单数据
					self.initTableData(dataId, indexType);
					
					
					//初始化左树
					if(dataId == ''){
						self.initLeftTree(indexType);
					}
					
					//保存按钮
					 $('#index_save').click(function(){
						 self.save(indexType);
					 });
					 
					//返回按钮
					 $('#index_back').click(function(){
						 require(['assessment/src/city/index'], function (psgfIndex) {
	                		 psgfIndex.show(indexType);
	                	 });
					 });
					 /*$('.demo2').colorpicker({
					        format: 'hex'
					 });*/
					
				},
				
				initLeftTree : function(indexType) {
					comm.requestJson('/report/index/getIndexTree', indexType,
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
						levels: 2,
			            showBorder: false,
			            showTags: false,
			            data:data
					});
					
					//左树点击事件
					$("#treeview-searchable").on("nodeSelected",function(event,defaultData){
						$("#parentid").val(defaultData.id);
					});
					 
					//左树取消点击事件
					$("#treeview-searchable").on("nodeUnselected",function(event,defaultData){
						$("#parentid").val('');
					});

				},
				
				initTableData : function(dataId, indexType) {
					if(dataId == null || dataId == ''){//add
						//$('.index_padd').html(_.template(tpl, null));
						comm.requestJson('/report/index/initLevel',null,function(resp){
							 if(resp.code == 0){
								 resp.data.indexType = indexType;
								 $('.index_padd').html(_.template(tpl, resp.data));
								 
								//保存按钮
								 $('#index_save').click(function(){
									 self.save(indexType);
								 });
								 
								//返回按钮
								 $('#index_back').click(function(){
									 require(['assessment/src/city/index'], function (psgfIndex) {
				                		 psgfIndex.show(indexType);
				                	 });
								 });
								 
								 $('.demo2').colorpicker({
								        format: 'hex'
								 });
							 }else{
								comm.alert_tip(resp.msg);
							 }
						 });
					}else{//edit，传的是pkid
						comm.requestJson('/report/index/getIndexById',dataId,function(resp){
							 if(resp.code == 0){
								 $('.index_padd').html(_.template(tpl, resp.data));
									
								//保存按钮
								 $('#index_save').click(function(){
									 self.save(indexType);
								 });
								 
								//返回按钮
								 $('#index_back').click(function(){
									 require(['assessment/src/city/index'], function (psgfindex) {
				                		 psgfindex.show(indexType);
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
				save:function(indexType){
					
					 var params =  $("#index_form").serializeJson();
					 if(comm.isEmpty(params.name)){
						 comm.alert_tip('指标名称不能为空!');
						 return false;
					 }
					 if(comm.isEmpty(params.targetlevel)){
						 comm.alert_tip('目标等级不能为空!');
						 return false;
					 }
					 if(comm.isEmpty(params.weight)){
						 comm.alert_tip('权重不能为空!');
						 return false;
					 }
					 if(comm.isEmpty(params.syntheticWeight)){
						 comm.alert_tip('综合权重不能为空!');
						 return false;
					 }
					 var flag = true;
					 $.each($('input[name$=lowerLimit]'),function(){
						 if(comm.isEmpty($(this).val())){
							 flag = false;
						 }
					 });
					 $.each($('input[name$=topLimit]'),function(){
						 if(comm.isEmpty($(this).val())){
							 flag = false;
						 }
					 });
					 if(!flag){
						 comm.alert_tip('评分等级请设置完整!');
						 return false;
					 }
					 
					 comm.requestDefault('/report/index/insert',params,function(resp){
						 if(resp.code == 0){
							 require(['assessment/src/city/index'], function (psgfIndex) {
		                		 psgfIndex.show(indexType);
		                	 });	
						 }else{
							comm.alert_tip(resp.msg);
						 }
					 });
				}
		
		};
			
		return self;
		
			
		});