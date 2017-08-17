define(
		[ 'text!assessment/tpl/city/index.html' ],
		function(tpl) {
			var self = {
				mytable : null,
				myTreeTable : null,
				mapObj:null,
				oppath:null,
				mouseTool:null,
				show : function(indexType) {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					
					
					//初始化列表数据
					self.initTableData(indexType);
					
					//删除按钮
					$('#btn-del').click(function(){
						var rowDataId = self.getTableContent();
						if(rowDataId.split(",").length != 1 || rowDataId == ''){
							comm.alert_tip("请选择一条记录");
							return false;
						}
						comm.alert_confirm('危险!确定要删除此指标吗?',function(){
							comm.requestJson('/report/index/delByIds', rowDataId, function(resp){
								if(resp.code == 0){
									self.initTableData(indexType);
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
						 require(['assessment/src/city/index_add'], function (index_add) {
							 index_add.show('', indexType);
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
						 
						 require(['assessment/src/city/index_add'], function (index_add) {
							 index_add.show(rowDataId, indexType);
	                	 });
		                	 
					 });
					 
				},
				
				
				initTable : function(data) {
					
					for(var i=0;i<data.length;i++){
						var obj = data[i];
						var parentIndeName= self.getIndexNameById(data, obj.parentid);
						obj.parentIndeName = parentIndeName;
					}
					
					//删除parentid为-1的数据
					var newData = [];
					for(var i=0;i<data.length;i++){
						var obj = data[i];
						/*if(obj.parentid!=-1){*/
							newData.push(obj);
						/*}*/
					}
					
					self.mytable = $('#example').DataTable({
						//每页显示三条数据
						//pageLength : 5,
						destroy: true,
						data: newData,
						displayLength: 25,
						order: [[ 2, "desc" ]],
						language:dataTable_cn,
						columns : [
						 {
	                      data: null,
	                      defaultContent: '',
	                      className: 'select-checkbox',
	                      orderable: false
	                    },
	                    {
							title:'指标名称',
							data : 'name'
						},
						{
							title:'父级指标',
							data : 'parentIndeName'
						}, 
						{
							title:'指标ID',
							data : 'id'
						},
						{
							title:'目标等级',
							data : 'targetlevel'
						},
						{
							title:'权重%',
							data : 'weight'
						},
						{
							title:'综合权重%',
							data : 'syntheticWeight'
						},
						{
							title:'是否启用',
							data : 'isAble',
							render: function(data, type, row, meta) {
					            if(row.isAble!=null&&row.isAble==0){
					            	return '启用';
					            }
					            return '禁用';
					       }
						}
						],
		                  select: {
		                      style:    'os',
		                      selector: 'td:first-child'
		                      },
					
		                "drawCallback": function(settings) {
				            var api = this.api();
				            var rows = api.rows({
				                page: 'current'
				            }).nodes();
				            var last = null;
				            
				            api.column(2, {
				                page: 'current'
				            }).data().each(function(group, i) {
				                if (last !== group) {
				                    $(rows).eq(i).before('<tr class="group"><td colspan="7">' + group + '</td></tr>');
				                    last = group;
				                }
				            });
				        }
					

					});
				},
				getIndexNameById:function(data,indexId){
					for(var i=0;i<data.length;i++){
						var obj = data[i];
						if(obj.id ==indexId){
							return obj.name;
						}
					}
					return '';
				},
				initTableData : function(indexType) {
					comm.requestJson('/report/index/getLists', indexType, function(resp){
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
				}/*,
				
				getPKTableContent : function() {  
					var nTrs = self.mytable.rows('.selected').data();
					var retStr = "";
				       
					if(nTrs != null && nTrs.length > 0){
						for(var k = 0; k < nTrs.length; k++){
							retStr += nTrs[k].pkid +",";
						}
						retStr = retStr.substring(0,retStr.length-1);
				       }
					return retStr;
				}*/
				
		}
			
		return self;
		});