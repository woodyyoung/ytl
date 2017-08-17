define(
		[ 'text!districtManagement/tpl/dataEntry/includedInTheCamp.html','text!districtManagement/tpl/dataEntry/includedInTheCamp_add.html' ],
		function(tpl,addtpl) {
			var self = {
				mytable : null,
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					//渲染表格
					/*self.loadTableData();*/
					$("#startTime").datetimepicker({
		                format: 'yyyy',
		                language: "zh-CN",
		                startView:'decade',
		            	minView: 'decade'
		            });
		            $("#endTime").datetimepicker({
		                format: 'yyyy',
		                language: "zh-CN",
		                startView:'decade',
		            	minView: 'decade'
		            });
		           
		           //添加公交营收录入
					$('#btn-insert').click(function(){
						self.includedInTheCamp();
					});
					//修改角色按钮
					$('#btn-update').click(function(){
						self.updateincludedInTheCamp();
					});
					
					//查询按钮
		            $('#btn_hours_confirm').click(function() {
		            	self.loadTableData();
		            });
		            
		            $('#btn_hours_confirm').click();
					
					//删除
					$('#btn-del').click(function(){
						 var rows = self.mytable.rows('.selected').data();
						 
						 if(rows.length == 0){
							 comm.alert_tip('请选择删除的数据！');
							 return false;
						 }
						 
						 var idStr = '';
						 for(var i=0; i<rows.length; i++){
							 idStr += rows[i].id + ','
						 }
						 
						 var params = {
							id : idStr.substring(0, idStr.length-1)	 
						 };
						 comm.alert_confirm('危险！确定要删除吗?',function(){
							 comm.requestJson('/report/busrevenue/delById', JSON.stringify(params), function(resp){
								if(resp.code == 0){
									comm.alert_tip('删除成功!');
									self.loadTableData();
								}else{
									console.error(resp.msg);
								}
							 },function(resp){
								 console.error("删除失败...");
							 });
						 });
					})
				},
				
				
				//加载表格数据
				loadTableData : function () {
					var startTime = $('#startTime').val();
	                var endTime = $('#endTime').val();
	                /*if(comm.isEmpty(startTime)||comm.isEmpty(endTime)){
	                	alert("请选择时间年份");
	                	return false;
	                }*/
	                var params = {};
					params.startTime = startTime;
					params.endTime = endTime;
					comm.requestJson('/report/busrevenue/busrevenumlist', JSON.stringify(params), function(resp){
						if(resp.code == 0){
							self.initialTab(resp.data);
						}else{
							comm.alert_tip(resp.msg);
						}
					},function(resp){
						comm.alert_tip("加载失败...");
						alert(data);
					});
				},
				/*getData:function(){
					
					
	                comm.requestJson('/report/busrevenue/querData', JSON.stringify(params), function(resp){
	                	if(resp.code == 0){
	                		self.initialTab(resp.data);
						}
	            	},function(resp){
	            		alert("查询失败...");
	            	});
				},*/
				//渲染表格
				initialTab:function(data){
					self.mytable = $('#example').DataTable({
						destroy: true,
						data: data,
						order: [[ 1, "subsidiesType" ]],
						language:dataTable_cn,
						columns : [
						 {
		                  data: null,
		                  defaultContent: '',
		                  className: 'select-checkbox',
		                  orderable: false
		                },
						{
							title:'营收',
							data : 'revenue'
						}, 
						{
							title:'发生年月',
							data : 'yearStr'
						},
						/*{
							title:'录入时间',
							data : 'entrytime'
						},*/
						{
							title:'录入人',
							data : "inputman"
						},
						{
							title:'备注',
							data : "remarks"
						}
						],
		                select: {
		                    style:    'os',
		                    selector: 'td:first-child'
		                }
	
					});
				},
				//修改
				updateincludedInTheCamp:function(){
					 var rows = self.mytable.rows('.selected').data();
					 if(rows.length != 1){
						 comm.alert_tip('请选择一条要修改的数据！');
						 return false;
					 }
					 $('#dialogDiv').html('');
					 $('#dialogDiv').html(_.template(addtpl, rows[0]));
					 $('#myModalLabelTile').html('修改公交营收录入');
					 $("#yearStr").attr('readonly','readonly');
					 $("#addincludedInTheCampModal").modal({
							keyboard: true
					 });
					 $('#addincludedInTheCampModal').modal('show');
					 $('#btn_add_stretch').unbind('click');
					 //绑定保存事件
					 $('#btn_add_stretch').click(function(){
						self.save();
					 });
						
					
				},
				includedInTheCamp:function(){
					$('#dialogDiv').html('');
					$('#dialogDiv').html(_.template(addtpl, {}));
					$("#yearStr").datetimepicker({
			            	format: 'yyyy',
			            	language: "zh-CN",
			            	startView:'decade',
			            	minView: 'decade'
			           });
					$("#addincludedInTheCampModal").modal({
						keyboard: true
					});
					$("#addincludedInTheCampModal").modal('show');
					//绑定保存事件
					$('#btn_add_stretch').unbind('click');
					$('#btn_add_stretch').click(function(){
						self.save();
					});
				},
				//保存
				save:function(){
					 var revenue = $.trim($('#revenue').val());
					 $('#revenue').val(revenue);
					 if(comm.isEmpty(revenue)){
						comm.alert_tip("营收字段必须填写！");
						return false;
					 }
					 var params = $("#Revenue_form").serializeJson();
					 $('#addincludedInTheCampModal').modal('hide');
					 $('.modal-backdrop').remove();
					 comm.requestDefault('/report/busrevenue/save',params,function(resp){
						 if(resp.resCode == 200){
							 comm.alert_tip("操作成功！");
							 self.show();
						 }else{
							 comm.alert_tip(resp.msg);
						 }
					 });
				},
			};
			return self;
	});