define(
		[ 'text!districtManagement/tpl/dataEntry/fiscalSubsidyManagement.html','text!districtManagement/tpl/dataEntry/fiscalSubsidyManagement_add.html' ],
		function(tpl,addtpl) {
			var self = {
				mytable : null,
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					//渲染表格
					
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
		            $("#subsidyObject").select2({});
		            $("#sourceOfSubsidies").select2({});
		            $("#subsidiesType").select2({});
		            //添加公交财政补贴基础数据
					$('#btn-insert').click(function(){
						self.fiscalSubsidyManagement();
					});
					
					//删除按钮
		            $('#btn-del').click(function() {
		                var rows = self.mytable.rows('.selected').data();
						if(rows.length == 0){
							 comm.alert_tip('请选择删除项！');
							 return false;
						}
						var row=rows[0];
						var params = {},
							financialtypeid = row.FINANCIAL_ID;
							params = {
								"financial_id":financialtypeid
							}
							comm.alert_confirm('危险！确定要删除吗?',function(){
								comm.requestDefault('/report/financial/delFinancial',params,function(resp){
									if(resp){
										comm.alert_tip('删除成功!');
										self.initSelect();
									}
								});
							});
		            });
		            
		            
		          //修改按钮
		            $('#btn-update').click(function() {
		                var rowDataId = self.mytable.rows('.selected').data();
		                if (rowDataId.length != 1 ) {
		                    alert("请选择单行记录");
		                    return false;
		                }
		                var data =rowDataId[0];
		                self.fiscalSubsidyManagement(data);
		            });
		            $('#dialogDiv').html(addtpl);
					// $("#addfiscalSubsidyManagementModal").modal({
					// 	keyboard: true
					// });
		            
		             $('#searchfiscalsubsidy').click(function() {
		                self.initSelect();
		                return false;
		            });
		             //保存按钮
		            $('#btn_add_stretch').click(function() {
		            	self.savefiscalSubsidy();
		                 
		            });
		            self.initSelection();
		            self.initSelect();
				},
				initTable:function(resp){
					var entryData = resp.data.financialData;
					self.mytable = $('#example').DataTable({
						destroy: true,
						data: entryData,
						order: [[ 1, "FINANCIALTYPE_TYPE_NAME" ]],
						language:dataTable_cn,
						columns : [
						{
		                  data: null,
		                  defaultContent: '',
		                  className: 'select-checkbox',
		                  orderable: false
		                },
						{
							title:'补贴类型',
							data : 'FINANCIALTYPE_TYPE_NAME'
						}, 
						{
							title:'补贴额度',
							data : 'MONEY'
						},
						{
							title:'补贴时间',
							data : 'YEAR'
						},
						{
							title:'补贴对象',
							data : "FINANCIALTYPE_OBJECT_NAME"
						},
						{
							title:'补贴来源',
							data : "FINANCIALTYPE_SOURCE_NAME"
						},
						{
							title:'录入时间',
							data : "CREATE_TIME"
						},
						{
							title:'录入人',
							data : "FINANCIALTYPE_TYPE_USER_NAME"
						},
						{
							title:'备注',
							data : "NOTE",
							render: function(data, type, row, meta) {
					            if(comm.isNotEmpty(data)){
					            	return data;
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
				},
				popultepopupdropdown: function(){
					var finanacialtypes=[{id:"1",name:"公交财政补贴种类"},{id:"2",name:"公交财政补贴对象"},{id:"3",name:"公交财政补贴来源"}];
					for(var i=0;i<finanacialtypes.length;i++){
						$("#subsidiesType").append('<option value="'+finanacialtypes[i].id+'">'+finanacialtypes[i].name+'</option>');				    
					}

				},
				initSelection:function(){
					var params = {
							"financialtype_object":"1"
						}
 					comm.requestDefault('/report/financialtype/getFinancialTypes',params,function(resp){
 						var defaultOption = '<option value=" ">全部</option>';
	                    comm.initSelectOptionForObj('subsidiesType', resp.data.financialTypeData, 'FINANCIALTYPE_NAME', 'FINANCIALTYPE_ID',defaultOption);

	                    comm.initSelectOptionForObj('subsidiesType1', resp.data.financialTypeData, 'FINANCIALTYPE_NAME', 'FINANCIALTYPE_ID');
	                    

 					},function(resp){
	            		alert("查询失败...");
	            	});

	            	params = {
							"financialtype_object":"2"
						}
 					comm.requestDefault('/report/financialtype/getFinancialTypes',params,function(resp){
 						var defaultOption = '<option value=" ">全部</option>';
	                    comm.initSelectOptionForObj('subsidyObject', resp.data.financialTypeData, 'FINANCIALTYPE_NAME', 'FINANCIALTYPE_ID',defaultOption);
	                     comm.initSelectOptionForObj('subsidyObject1', resp.data.financialTypeData, 'FINANCIALTYPE_NAME', 'FINANCIALTYPE_ID');
 					},function(resp){
	            		alert("查询失败...");
	            	});

	            	params = {
							"financialtype_object":"3"
						}
 					comm.requestDefault('/report/financialtype/getFinancialTypes',params,function(resp){
 						var defaultOption = '<option value=" ">全部</option>';
	                    comm.initSelectOptionForObj('sourceOfSubsidies', resp.data.financialTypeData, 'FINANCIALTYPE_NAME', 'FINANCIALTYPE_ID',defaultOption);
	                      comm.initSelectOptionForObj('sourceOfSubsidies1', resp.data.financialTypeData, 'FINANCIALTYPE_NAME', 'FINANCIALTYPE_ID');
 					},function(resp){
	            		alert("查询失败...");
	            	});
				},
				initSelect:function(){
					var startTime = $('#startTime').val();
	                var endTime = $('#endTime').val();
	                // var typeid = $('#subsidiesType ').val();
	                // var objectid = $('#subsidyObject').val();
	                // var sourceid = $('#sourceOfSubsidies').val();
	                var typeid= $.trim($('#subsidiesType option:selected').val());
	                var objectid= $.trim($('#subsidyObject option:selected').val());
	                var sourceid= $.trim($('#sourceOfSubsidies option:selected').val());
	                var params =  {
	                	startTime:startTime,
	                	endTime:endTime,
	                	financialtype_object_id:objectid,
	                	financialtype_source_id:sourceid, 
	                	financialtype_type_id:typeid
	                };

	                comm.requestDefault('/report/financial/getFinancials',params, function(resp) {
	            		self.initTable(resp);
	            	},function(resp){
	            		alert("查询失败...");
	            	});
				},
				fiscalSubsidyManagement:function(data){

					$("#addfiscalSubsidyManagementModal").modal('show');
					$("#startTime1").datetimepicker({
		                format: 'yyyy',
		                language: "zh-CN",
		                startView:'decade',
		            	minView: 'decade'
		            });
					var params = {};		            
	                
		            if (data != null) {
						self.editmode="edit";
		                //初始化表单数据
		                $('#financial_id').val(data.FINANCIAL_ID);
		            	$("#subsidiesType1").val(data.FINANCIALTYPE_TYPE_ID);
				        $("#cost").val(data.MONEY);
				        $("#startTime1").val(data.YEAR);
				        $("#subsidyObject1").val(data.FINANCIALTYPE_OBJECT_ID);
				        $("#sourceOfSubsidies1").val(data.FINANCIALTYPE_SOURCE_ID);
				        $("#createby").val(data.FINANCIALTYPE_TYPE_USER_NAME);
				        $("#remark").val(data.NOTE);
		            } else{
						self.editmode="add";
		            	$("#subsidiesType1").val();
		            	$("#cost").val();
				        $("#startTime1").val();
				        $("#subsidyObject1").val();
				        $("#sourceOfSubsidies1").val();
				        $("#createby").val(comm.userInfo.realName);
				        $("#remark").val();
		            }		          

				},

				savefiscalSubsidy:function(){
					/*var params = $("#subsidiesform").serializeJson(); */
					    var subsidiesType1 = $("#subsidiesType1").val(),
			            	cost = $("#cost").val(),
					        startTime1 = $("#startTime1").val(),
					        subsidyObject1 = $("#subsidyObject1").val(),
					        sourceOfSubsidies1 = $("#sourceOfSubsidies1").val(),
					        /*createby = $("#createby").val(),*/
					        remark = $("#remark").val(),
					        financial_id  =  $('#financial_id').val();
							params = {
								"financialtype_type_id":subsidiesType1,
								"money":cost,
								"year":startTime1,
								"financialtype_object_id":subsidyObject1,
								"financialtype_source_id":sourceOfSubsidies1,
								user_name:comm.userInfo.userName,
								user_id:comm.userInfo.id,
								"note":remark
							}

						var saveurl="";
						if(self.editmode=="edit"){
							saveurl="updFinancial";
							params.financial_id = financial_id;
						}else if(self.editmode=="add"){
							saveurl="addFinancial";
						}
		                comm.requestDefault('/report/financial/'+saveurl, params,function(resp) {
		                	if(resp){
		                		comm.alert_tip('操作成功！');
		                		$("#addfiscalSubsidyManagementModal").modal('hide');
		                		self.initSelect();
		                		/*alert(resp.msg);*/
		                	}/*else{
		                		$("#addfiscalSubsidyManagementModal").modal('hide');
		                		self.initSelect();
		                	}*/
		                },function(resp){
		            		alert("操作失败...");
		            	});
                },

			};
			return self;
	});