define(
		[ 'text!districtManagement/tpl/dataEntry/costOfEntry.html','text!districtManagement/tpl/dataEntry/costOfEntry_add.html' ],
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
		            $("#costOfEntry_style").select2({});
		            //添加全市公交行业成本
					$('#btn-insert').click(function(){
						self.addCostOfEntry();
					});
					
					//删除按钮
//		            $('#btn-del').click(function() {
//		                var rowDataId = self.getTableContent();
//		                if (rowDataId.split(",").length < 1 || rowDataId == '') {
//		                    alert("请选择一条记录");
//		                    return false;
//		                }
//		                var params = {};
//		                params.industryCostId = rowDataId;
//		                comm.alert_confirm('危险！确定要删除吗?',function(){
//			                comm.request('/report/industryCost/delete', params,
//			                function(resp) {
//			                	comm.alert_tip('删除成功!');
//			                	self.initSelect();
//			                });
//		                });
//		                self.initSelect();
//		            });
					  $('#btn-del').click(function() {
			                var rowDataId = self.getTableContent();
			                if (rowDataId.split(",").length < 1 || rowDataId == '') {
			                    alert("请选择一条记录");
			                    return false;
			                }
			                var params = {};
			                params.industryCostId = rowDataId;
			                comm.alert_confirm('危险！确定要删除吗?',function(){
				                comm.request('/report/industryCost/delete', params,
				                function(resp) {
				                	comm.alert_tip('删除成功!');
				                });
	                            self.initSelect();
			                });
		
			            });
		            
		            
		          //修改按钮
		            $('#btn-update').click(function() {
		                var rowDataId = self.getTableContent();
		                if (rowDataId.split(",").length > 1 || rowDataId == null || rowDataId == '') {
		                    alert("请选择单行记录");
		                    return false;
		                }
		                var data = self.mytable.rows('.selected').data();
		                self.addCostOfEntry(data);
		            });
		            
		            
		            //查询按钮
		            $('#btn_industryCost_confirm').click(function() {
		                self.initSelect();
		            });
		            self.initSelection();
		            self.initSelect();
		            
				},
				addCostOfEntry:function(data){
					$('#dialogDiv').html(addtpl);
					$("#addCostOfEntryModal").modal({
						keyboard: true
					});
					$("#addCostOfEntryModal").modal('show');
					var params = {};
					comm.requestJson('/report/industryCostType/list', JSON.stringify(params), function(resp){
						comm.initSelectOptionForObj('costTypeNo', resp, 'TYPE_NAME', 'ID');
						$('#costTypeNo').next().css('width','250px');
					},function(resp){
						alert("初始化失败...");
					});
					$("#occurtime").datetimepicker({
		                format: 'yyyy',
		                language: "zh-CN",
		                startView:'decade',
		            	minView: 'decade'

		            });
		            
	                
		            if (data != null) {
		                //初始化表单数据
		            	$("#id").val(data[0].ID);
		            	$("#costTypeNo").val(data[0].COSTTYPENO);
				        $("#cost").val(data[0].COST);
				        $("#occurtime").val(data[0].OCCURTIME);
				        $("#createby").val(data[0].CREATEBY);
				        $("#remark").val(data[0].REMARK);
		            } else{
		            	$("#id").val();
		            	$("#costOfEntry_style").val();
				        $("#cost").val();
				        $("#occurtime").val();
				        $("#createby").val(comm.userInfo.realName);
				        $("#remark").val();
		            }
		            
		            //保存按钮
		            $('#btn_add_stretch').click(function() {
		            	var params = $("#industry_form").serializeJson(); 
		                comm.requestDefault('/report/industryCost/save', params,
		                function(resp) {
		                	if(resp && resp.resCode < 0){
		                		alert(resp.msg);
		                	}else{
		                		$("#addCostOfEntryModal").modal('hide');
		                		self.initSelect();
		                	}
		                },function(resp){
		            		alert("保存失败...");
		            	});
		                 
		            });
		            
				},
				initSelection:function(){
	                var params = {};
	                comm.requestJson('/report/industryCostType/list', JSON.stringify(params), function(resp){
		                var defaultOption = '<option value=" ">全部</option>';
	                    comm.initSelectOptionForObj('costOfEntry_style', resp, 'TYPE_NAME', 'ID',defaultOption);
	            	},function(resp){
	            		alert("查询失败...");
	            	});
				},
				initSelect:function(){
					var startTime = $('#startTime').val();
	                var endTime = $('#endTime').val();
	                var costType = $.trim($('#costOfEntry_style option:selected').val());
	                var params = {};
	                params.startTime = startTime;
	                params.endTime = endTime;
	                params.costType = costType;
	                comm.requestJson('/report/industryCost/list', JSON.stringify(params), function(resp){
	            		self.initTable(resp);
	            	},function(resp){
	            		alert("查询失败...");
	            	});
				},
				initTable:function(data){
					self.mytable = $('#example').DataTable({
						destroy: true,
						data: data,
						order: [[ 1, "costTypes" ]],
						language:dataTable_cn,
						columns : [
						 {
		                  data: null,
		                  defaultContent: '',
		                  className: 'select-checkbox',
		                  orderable: false
		                },
						{
							title:'成本类型',
							defaultContent: '',
							data : 'COST_TYPE_NO'
						}, 
						{
							title:'金额',
							defaultContent: '',
							data : 'COST'
						},
						{
							title:'发生年份',
							defaultContent: '',
							data : 'OCCURTIME'
						},
						{
							title:'录入时间',
							defaultContent: '',
							data : "CREATETIME"
						},
						{
							title:'录入人',
							defaultContent: '',
							data : "CREATEBY"
						},
						{
							title:'备注',
							defaultContent: '',
							data : "REMARK"
						}
						],
		                select: {
		                    style:    'os',
		                    selector: 'td:first-child'
		                }
	
					});
				},
				getTableContent: function() {
		            var nTrs = self.mytable.rows('.selected').data();
		            var retStr = "";
		            if (nTrs != null && nTrs.length > 0) {
		                for (var k = 0; k < nTrs.length; k++) {
		                    retStr += nTrs[k].ID + ",";
		                }
		                retStr = retStr.substring(0, retStr.length - 1);
		            }
		            return retStr;
		        },
			};
			return self;
	});