define(
		[ 'text!districtManagement/tpl/dataEntry/costTypes.html','text!districtManagement/tpl/dataEntry/costTypes_add.html'],
		function(tpl,addTpl) {
			var self = {
				mytable : null,
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					
					//添加全市公交行业成本类型
					$('#btn-insert').click(function(){
						self.addCostTypes();
					});
					
					//删除按钮
		            $('#btn-del').click(function() {
		                var rowDataId = self.getTableContent();
		                if (rowDataId.split(",").length < 1 || rowDataId == '') {
		                    comm.alert_tip("请选择一条记录");
		                    return false;
		                }
		                var params = {};
		                params.industryCostId = rowDataId;
		                comm.alert_confirm('危险！确定要删除吗?',function(){
			                comm.request('/report/industryCostType/delete', params,
			                function(resp) {
			                	if(resp.resCode=='200'){
			                		comm.alert_tip('删除成功');
			                	}else{
			                		self.alert_tip('删除失败');
			                	}
			                	 self.initSelect();
			                });
		                });
		            });
		            
		            
		          //修改按钮
		            $('#btn-update').click(function() {
		                var rowDataId = self.getTableContent();
		                if (rowDataId.split(",").length > 1 || rowDataId == null || rowDataId == '') {
		                    comm.alert_tip("请选择单行记录");
		                    return false;
		                }
		                var data = self.mytable.rows('.selected').data();
		                self.addCostTypes(data);
		            });
		            
		            
		            //查询按钮
		            $('#btn_industryCost_confirm').click(function() {
		                self.initSelect();
		            });
		            
		            self.initSelect();
					
				},
				addCostTypes:function(data){
					$('#dialogDiv').html(addTpl);
					$("#addCostTypesModal").modal({
						keyboard: true
					});
					$("#addCostTypesModal").modal('show');
					
					if (data != null) {
		                //初始化表单数据
						$("#id").val(data[0].ID);
		            	/*$("#typeNo").val(data[0].TYPE_NO);*/
				        $("#typeName").val(data[0].TYPE_NAME);
				        $("#mark").val($.trim(data[0].MARK));
		            } else{
		            	/*$("#typeNo").val();*/
				        $("#typeName").val();
				        $("#mark").val();
				        $("#id").val();
		            }
					
					 //保存按钮
		            $('#btn_add_stretch').click(function() {
		            	var params = $("#industryType_form").serializeJson(); 
		                comm.requestDefault('/report/industryCostType/save', params,
		                function(resp) {
		                	$("#addCostTypesModal").modal('hide');
		                	if(resp.resCode=='200'){
		                		comm.alert_tip('操作成功');
		                	}else{
		                		comm.alert_tip('操作失败');
		                	}
		                	self.initSelect();
		                });
		                 
		            });
					
				},
				initSelect:function(){
	                var params = {};
	                
	                comm.requestJson('/report/industryCostType/list', JSON.stringify(params), function(resp){
	            		self.initTable(resp);
	            	},function(resp){
	            		comm.alert_tip("查询失败...");
	            	});
				},
				initTable:function(data){
					//渲染表格
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
						/*{
							title:'编号',
							data : 'TYPE_NO'
						}, */
						{
							title:'名称',
							data : 'TYPE_NAME'
						},
						{
							title:'备注',
							data : "MARK"
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