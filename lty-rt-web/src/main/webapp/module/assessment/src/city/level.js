define(
		[ 'text!assessment/tpl/city/level.html' ],
		function(tpl) {
			var self = {
				mytable : null,
				myTreeTable : null,
				mapObj:null,
				oppath:null,
				mouseTool:null,
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					
					
					//初始化列表数据
					self.initTableData();
					
					//删除按钮
					$('#btn-del').click(function(){
						var rowDataId = self.getTableContent();
						if(rowDataId.split(",").length != 1 || rowDataId == ''){
							comm.alert_tip("请选择一条记录");
							return false;
						}
						comm.alert_confirm('危险!确定要删除此评价等级吗?',function(){
							comm.requestJson('/report/level/delByIds', rowDataId, function(resp){
								if(resp.code == 0){
									self.initTableData();
									comm.alert_tip("删除成功");
								}else{
									comm.alert_tip(resp.msg);
								}
							},function(resp){
								comm.alert_tip("删除失败...")
							});
						});
					 });
					
					//新增按钮
					 $('#btn-insert').click(function(){
						 require(['assessment/src/city/level_add'], function (level_add) {
							 level_add.show('');
	                	 });
					 });
					
					//修改按钮
					 $('#btn-update').click(function(){
						 var rowDataId = self.getTableContent();
						 if(rowDataId.split(",").length > 1){
							 comm.alert_tip("请选择单行记录");
							 return false;
						 }
						 if(rowDataId.split(",").length < 1 || rowDataId == ''){
							 comm.alert_tip("请选择一条记录");
							 return false;
						 }
						 
						 require(['assessment/src/city/level_add'], function (level_add) {
							 level_add.show(rowDataId);
	                	 });
		                	 
					 });
					 
				},
				
				
				initTable : function(data) {
					self.mytable = $('#example').DataTable({
						//每页显示三条数据
						//pageLength : 5,
						destroy: true,
						data: data,
						order: [[ 1, "asc" ]],
						language:dataTable_cn,
						columns : [
						 {
	                      data: null,
	                      defaultContent: '',
	                      className: 'select-checkbox',
	                      orderable: false
	                    },
						{
							title:'评分等级',
							data : 'levels'
						}, 
						{
							title:'描述',
							data : 'description'
						}						
						],
		                  select: {
		                      style:    'os',
		                      selector: 'td:first-child'
		                      }

					});
				},
				
				initTableData : function(data) {
					comm.requestJson('/report/level/findLevels', null, function(resp){
						if(resp.code == 0){
							self.initTable(resp.data);
						}else{
							comm.alert_tip(resp.msg);
						}
					},function(resp){
						comm.alert_tip("加载失败...")
					});
				},
				
				getTableContent : function() {  
					var nTrs = self.mytable.rows('.selected').data();
					var retStr = "";
				       
					if(nTrs != null && nTrs.length > 0){
						for(var k = 0; k < nTrs.length; k++){
							retStr += nTrs[k].id +",";
						}
						retStr = retStr.substring(0,retStr.length-1);
				       }
					return retStr;
				}
				
		}
			
		return self;
		});