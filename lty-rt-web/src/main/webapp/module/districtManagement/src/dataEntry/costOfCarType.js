define(
		[ 'text!districtManagement/tpl/dataEntry/costOfCarType.html','text!districtManagement/tpl/dataEntry/costOfCarType_add.html' ],
		function(tpl,addtpl) {
			var self = {
				mytable : null,
				show : function() { 
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					//渲染表格
					self.initialTab();
					
		            //添加公交营收录入
					$('#btn-insert').click(function(){
						self.costOfCarType();
					});
					
					
					$('#btn-update').click(function(){
						self.updateCostOfCarType();
					});
					
					$('#btn-del').click(function(){
						 var rows = self.mytable.rows('.selected').data();
						 comm.alert_confirm('危险！确定要删除吗?',function(){
							comm.requestDefault('/report/pvcosttype/delPVCostType',{'pvcosttype_id':rows[0].PVCOSTTYPE_ID},function(resp){
								 if(resp.resCode == 200){
									 comm.alert_tip("操作成功！");
									 self.show();
								 }else{
									 comm.alert_tip(resp.msg);
								 }
							 });
						 });
					});
				},
				initialTab:function(){
					comm.requestJson('/report/pvcosttype/getPVCostTypes', null, function(resp) {
			            if(resp.resCode == 200){
			            	var entryData = resp.data;
								//var entryData = [{"number":"1","name":"千车公里成本","remark":""},{"number":"1","name":"千车公里成本","remark":""},{"number":"1","name":"千车公里成本","remark":""}];
								self.mytable = $('#example').DataTable({
									destroy: true,
									data: entryData,
									order: [[ 1, "NUMBER1" ]],
									language:dataTable_cn,
									columns : [
									 {
					                  data: null,
					                  defaultContent: '',
					                  className: 'select-checkbox',
					                  orderable: false
					                },
									{
										title:'编号',
										data : 'NUMBER1'
									},
									{
										title:'名称',
										data : 'PVCOSTTYPE_NAME'
									},
									{
										title:'备注',
										data : "NOTE",
										render: function(data, type, row, meta) {
								            if(row.NOTE!=null&&row.NOTE!=''){
								            	return row.NOTE;
								            }
								            return '';
								       }
									}
									],
					                select: {
					                    style:    'os',
					                    selector: 'td:first-child'
					                }
								});
								
			            }else{
			            	alert(resp.msg);
			            }
					},function(resp){
						alert("调用失败");
					}); 
				},
				//修改角色信息
				updateCostOfCarType:function(){
					 var rows = self.mytable.rows('.selected').data();
					 if(rows.length != 1){
						 comm.alert_tip('请选择一条要修改的类型数据！');
						 return false;
					 }
					 if(rows[0].roleName=='系统管理员'){
						 comm.alert_tip('系统管理员不能修改');
						 return false;
					 }

					 $('#dialogDiv').html('');
					 $('#dialogDiv').html(_.template(addtpl, rows[0]));
					 $('#myModalLabelTile').html('修改全市公交行业人车成本类型');
					 $("#addcostOfCarTypeModal").modal({
							keyboard: true
					 });
					 $('#addcostOfCarTypeModal').modal('show');
					 $('#btn_add_stretch').unbind('click');
					 $('#pvcosttype_id').val(rows[0].PVCOSTTYPE_ID);
					 $('#pvcosttype_name').val(rows[0].PVCOSTTYPE_NAME);
					 $('#user_id').val(comm.userInfo.id);
					 $('#user_name').val(comm.userInfo.userName);
					 $('#note').val(rows[0].NOTE);
					 //绑定保存事件
					 $('#btn_add_stretch').click(function(){
						var pvcosttype_name=$.trim($('#pvcosttype_name').val());
						var note=$.trim($('#note').val());
						if(comm.isEmpty(pvcosttype_name)){
								comm.alert_tip("名称必须填写！");
								return false;
						 }
						var params =  $("#pvcosttype_form").serializeJson();
						 $('#addcostOfCarTypeModal').modal('hide');
						 $('.modal-backdrop').remove();
						comm.requestDefault('/report/pvcosttype/updPVCostType',params,function(resp){
							 if(resp.resCode == 200){
								 comm.alert_tip("操作成功！");
								 self.show();
							 }else{
								 comm.alert_tip(resp.msg);
							 }
						});
					 });
						
					
				},
				costOfCarType:function(){
					$('#dialogDiv').html(addtpl);
					$("#addcostOfCarTypeModal").modal({
						keyboard: true
					});
					$("#addcostOfCarTypeModal").modal('show');
					
					$('#btn_add_stretch').click(function(){
						var pvcosttype_name=$.trim($('#pvcosttype_name').val());
						var note=$.trim($('#note').val());
						if(comm.isEmpty(pvcosttype_name)){
							comm.alert_tip("名称必须填写！");
							return false;
						}
						$('#user_id').val(comm.userInfo.id);
						$('#user_name').val(comm.userInfo.userName);
						var params =  $("#pvcosttype_form").serializeJson();
						 $('#addcostOfCarTypeModal').modal('hide');
						 $('.modal-backdrop').remove();
						comm.requestDefault('/report/pvcosttype/addPVCostType',params,function(resp){
							 if(resp.resCode == 200){
								 comm.alert_tip("操作成功！");
								 self.show();
							 }else{
								 comm.alert_tip(resp.msg);
							 }
						 });

					});
				},
			};
			return self;
	});