define(
		[ 'text!districtManagement/tpl/dataEntry/oneCarCostInput.html','text!districtManagement/tpl/dataEntry/oneCarCostInput_add.html' ],
		function(tpl,addtpl) {
			var self = {
				mytable : null,
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					//渲染表格
					self.initialTab(null);
						
					$('#selectFind').click(function(){
						var param=$('#findPVCost_form').serializeJson();
						param.line_id=$.trim(param.line_id);
						self.initialTab(param);
					})	
					
				
					
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
		            
		            
		            $("#oneCarCostInput_line").select2({});
		            
					$("#oneCarCostInput_firm").change(function(){
						self.initLine('oneCarCostInput_line',this.value);
					});
		            
					self.initPvCostTypes('oneCarCostInput_type',null);
					self.initDepartments('oneCarCostInput_firm','oneCarCostInput_line');
					
		            $("#oneCarCostInput_type").select2({});
		            //添加公交财政补贴基础数据
					$('#btn-insert').click(function(){
						self.oneCarCostInput();
					});
					
					$('#btn-update').click(function(){
						var rows = self.mytable.rows('.selected').data();
						 if(rows.length != 1){
							 comm.alert_tip('请选择一条要修改的类型数据！');
							 return false;
						 }
						 $("#startTime1").datetimepicker({
			                format: 'yyyy',
			                language: "zh-CN",
			                startView:'decade',
			            	minView: 'decade'


			             });
						 if(rows[0].roleName=='系统管理员'){
							 comm.alert_tip('系统管理员不能修改');
							 return false;
						 }

						 $('#dialogDiv').html('');
						 $('#dialogDiv').html(_.template(addtpl, rows[0]));
						 $('#myModalLabelTile').html('修改全市公交行业人车成本录入');
						 $("#addoneCarCostInputModal").modal({
								keyboard: true
						 });
						 $('#addoneCarCostInputModal').modal('show');
						 $('#btn_add_stretch').unbind('click');
						 
						$('#user_id').val(comm.userInfo.id);
						$('#user_name').val(comm.userInfo.userName);
						$('#note').val(rows[0].NOTE);
						$('#money').val(rows[0].MONEY);
						$('#pvcost_id').val(rows[0].PVCOST_ID);
						$('#startTime1').val(rows[0].YEAR);
						
						self.initPvCostTypes('subsidiesType',rows[0].PVCOSTTYPE_NAME);
						//$("#subsidiesType").select2('val',rows[0].PVCOSTTYPE_NAME);
						//$('#subsidiesType').find("<option value=\""+rows[0].PVCOSTTYPE_ID+"\">"+rows[0].PVCOSTTYPE_NAME+"</option>").attr("selected",true);
			            self.initDepartments('sourceOfSubsidies','subsidyObject',rows[0].COMPANY_NAME,rows[0].LINE_NAME);
						//self.initLine('subsidyObject',rows[0].COMPANY_ID,rows[0].LINE_NAME);
			            $("#sourceOfSubsidies").change(function(){
							self.initLine('subsidyObject',this.value,rows[0].LINE_NAME);
						});
			            //$("#sourceOfSubsidies").change();
			            
			            $('#btn_add_stretch').click(function(){
							var params =  $("#pvcost_form").serializeJson();
							if(comm.isEmpty(params.pvcosttype_id)){
								comm.alert_tip("必须选择类型！");
								return false;
							}else if(comm.isEmpty(params.money)){
								comm.alert_tip("必须输入金额！");
								return false;
							}else if(comm.isEmpty(params.company_id)){
								comm.alert_tip("必须选择公司！");
								return false;
							}else if(comm.isEmpty(params.line_id)){
								comm.alert_tip("必须选择线路！");
								return false;
							}else if(comm.isEmpty(params.year)){
								comm.alert_tip("请输入发生的年份！");
								return false;
							}
							$('#addcostOfCarTypeModal').modal('hide');
							$('.modal-backdrop').remove();
							comm.requestDefault('/report/pvcost/updPVCost',params,function(resp){
								if(resp.resCode == 200){
									 comm.alert_tip("操作成功！");
									 self.show();
								 }else{
									 comm.alert_tip(resp.msg);
								 }
							});
						})
						
						
						/*
						 *  $('#addcostOfCarTypeModal').modal('show');
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
						 * 
						 * */
						 
						 //$('#pvcosttype_id').val(rows[0].PVCOSTTYPE_ID);
						// $('#pvcosttype_name').val(rows[0].PVCOSTTYPE_NAME);
						// $('#user_id').val(comm.userInfo.id);
						// $('#user_name').val(comm.userInfo.userName);
						 //$('#note').val(rows[0].NOTE);
					})
					
					
					$('#btn-del').click(function(){
						 var rows = self.mytable.rows('.selected').data();
						 comm.alert_confirm('危险！确定要删除吗?',function(){
							comm.requestDefault('/report/pvcost/delPVCost',{'pvcost_id':rows[0].PVCOST_ID},function(resp){
								 if(resp.resCode == 200){
									 comm.alert_tip("操作成功！");
									 self.show();
								 }else{
									 comm.alert_tip(resp.msg);
								 }
							 });
						 });
					})
				},
				initDepartments : function(selectId,lineSelectId,selectedOption,line_name){
					comm.request('/report/pvcost/getDepartments',null,function(resp){
						var defaultOption = '<option value=" ">全部</option>';
						comm.initSelectOptionForObj(selectId,resp.data,'NAME','ID',defaultOption);
						if(resp.data[0]){
							if(selectedOption){
								self.initLine(lineSelectId,$('#'+selectId).val(),line_name);
							}else{
								self.initLine(lineSelectId,resp.data[0].ID);
							}
							//self.initLine(lineSelectId,resp.data[0].ID);

						}
					});
				},
				initPvCostTypes : function(selectId,selectedOption){
					comm.request('/report/pvcosttype/getPVCostTypes',null,function(resp){
						var defaultOption = '<option value=" ">全部</option>';
						comm.initSelectOptionForObj(selectId,resp.data,'PVCOSTTYPE_NAME','PVCOSTTYPE_ID',defaultOption);
					});
				},
				initLine:function(selectId,department_id,selectedOption){
					comm.request('/report/lineAnalysis/queryLineDataByDepartmentId',{"departmentid":""+department_id},function(resp){
						if(selectedOption)
							comm.initSelectOptionForObj(selectId,resp.data,'NAME','ID',null,selectedOption);
						else{
							var defaultOption = '<option value=" ">全部</option>';
							comm.initSelectOptionForObj(selectId,resp.data,'NAME','ID',defaultOption);
						}
					});
				},
				initialTab:function(param){				
					comm.requestDefault('/report/pvcost/getPVCosts',param, function(resp) {
			            if(resp.resCode == 200){
			            	var entryData =resp.data;
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
									title:'成本类型',
									data : 'PVCOSTTYPE_NAME'
								}, 
								{
									title:'金额',
									data : 'MONEY',
									render: function(data, type, row, meta) {
							            if(row.MONEY!=null&&row.MONEY!=''){
							            	return row.MONEY;
							            }
							            return '';
							       }
								},
								{
									title:'线路',
									data : 'LINE_NAME',
									render: function(data, type, row, meta) {
							            if(row.LINE_NAME!=null&&row.LINE_NAME!=''){
							            	return row.LINE_NAME;
							            }
							            return '';
							       }
								},
								{
									title:'公司',
									data : "COMPANY_NAME",
									render: function(data, type, row, meta) {
							            if(row.COMPANY_NAME!=null&&row.COMPANY_NAME!=''){
							            	return row.COMPANY_NAME;
							            }
							            return '';
							       }
								},
								{
									title:'发生年份',
									data : "YEAR",
									render: function(data, type, row, meta) {
							            if(row.YEAR!=null&&row.YEAR!=''){
							            	return row.YEAR;
							            }
							            return '';
							       }
								},
								{
									title:'录入时间',
									data : "CREATE_TIME"
								},
								{
									title:'录入人',
									data : "USER_NAME"
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
				oneCarCostInput:function(){
					$('#dialogDiv').html(addtpl);
					$("#addoneCarCostInputModal").modal({
						keyboard: true
					});
					$("#addoneCarCostInputModal").modal('show');
					$("#startTime1").datetimepicker({
		                format: 'yyyy',
		                language: "zh-CN",
		                startView:'decade',
		            	minView: 'decade'


		            });
		            $("#endTime1").datetimepicker({
		                format: 'yyyy-mm-dd',
		                language: "zh-CN",
		                minView: 'month'
		            });
		            
		            $('#user_id').val(comm.userInfo.id);
					$('#user_name').val(comm.userInfo.userName);
					self.initPvCostTypes('subsidiesType',null);
		            self.initDepartments('sourceOfSubsidies','subsidyObject');
		            $("#sourceOfSubsidies").change(function(){
						self.initLine('subsidyObject',this.value);
					});
		            $('#btn_add_stretch').click(function(){
						var params =  $("#pvcost_form").serializeJson();
						if(comm.isEmpty(params.pvcosttype_id)){
							comm.alert_tip("必须选择类型！");
							return false;
						}else if(comm.isEmpty(params.money)){
							comm.alert_tip("必须输入金额！");
							return false;
						}else if(comm.isEmpty(params.company_id)){
							comm.alert_tip("必须选择公司！");
							return false;
						}else if(comm.isEmpty(params.line_id)){
							comm.alert_tip("必须选择线路！");
							return false;
						}else if(comm.isEmpty(params.year)){
							comm.alert_tip("请输入发生的年份！");
							return false;
						}
						$('#addcostOfCarTypeModal').modal('hide');
						$('.modal-backdrop').remove();
						comm.requestDefault('/report/pvcost/addPVCost',params,function(resp){
							if(resp.resCode == 200){
								 comm.alert_tip("操作成功！");
								 self.show();
							 }else{
								 comm.alert_tip(resp.msg);
							 }
						});
					})
		            
				},
			};
			return self;
	});