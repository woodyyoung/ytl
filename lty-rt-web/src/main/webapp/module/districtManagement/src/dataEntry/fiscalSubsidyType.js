define(
		[ 'text!districtManagement/tpl/dataEntry/fiscalSubsidyType.html','text!districtManagement/tpl/dataEntry/fiscalSubsidyType_add.html' ],
		function(tpl,addtpl) {
			var self = {
				mytable : null,
				menuId:null,
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					//渲染表格
					self.initialTab();
					//渲染左树
					self.initTree();
		            //添加财政补贴类型
					$('#btn-insert').click(function(){
						self.fiscalSubsidyType();
					});
					$("#fiscalSubsidyType_name").select2({});
					$('#dialogDiv').html(addtpl);
					$("#btn_add_stretch").click(function(){
						self.saveData();
					});
					$("#btn-update").click(function(){
						self.popupEdit();
					});
					$("#btn-delete").click(function(){
						self.deleteData();
					});
					self.initialTabData();
				},
				initialTabData:function(){
					var params = {
							"financialtype_object":"1"
						}
 					comm.requestDefault('/report/financialtype/getFinancialTypes',params,function(resp){
 						self.initialTab(resp);
 					});
				},
				initialTab:function(data){
					/*var entryData = [{"number":"编号","name":"名称","remark":"备注"},{"number":"编号","name":"名称","remark":"备注"},{"number":"编号","name":"名称","remark":"备注"},{"number":"编号","name":"名称","remark":"备注"}];*/
					var rows=[];
					if(data && data.data){
						rows =data.data.financialTypeData;
					}
					self.mytable = $('#example').DataTable({
						destroy: true,
						data: rows,
						order: [[ 1, "number" ]],
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
							data : 'NUMBER_SEQ'
						}, 
						{
							title:'名称',
							data : 'FINANCIALTYPE_NAME'
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
				initTree : function() {
					self.defaultData = [
			          {
			            text: '类型管理',
			            href: '#parent1',
			            tags: ['3'],
			            nodes: [
			              {
			                text: '公交财政补贴种类',
			                href: '#child1',
			                financialtype:"1",
			                state:{
			                 expanded: true,
			                 selected: true
			                }
			              },
			              {
			                text: '公交财政补贴对象',
			                href: '#child2',
			                financialtype:"2"
			              },
			              {
			                text: '公交财政补贴来源',
			                href: '#child3',
			                financialtype:"3"
			              }
			            ]
			          }
			        ];
					self.myTreeTable = $('#treeview-searchable').treeview({
						levels: 2,
			            showBorder: false,
			            showTags: true,
			            data:self.defaultData
					});

					self.menuId = '1';
					self.selectedFinancialType="1";
					//左树点击事件
					$("#treeview-searchable").on("nodeSelected",function(event,selecteddata){

						self.menuId = selecteddata.financialtype;

						self.ajaxRequestTree(selecteddata);
					});

					$("#treeview-searchable").on("nodeUnselected",function(event,selecteddata){

						self.menuId = '';

					});

					 $("#addfiscalSubsidyTypeModal").modal({
						keyboard: true
					});

				},
				ajaxRequestTree:function(defaultData){
					self.selectedFinancialType=defaultData.financialtype;
					var params = {};
						params = {
							"financialtype_object":defaultData.financialtype
						}
						comm.requestDefault('/report/financialtype/getFinancialTypes',params,function(resp){
							if(resp){
								self.initialTab(resp);
							}
						});
				},
				fiscalSubsidyType:function(){					
					if(comm.isEmpty(self.menuId)){
						comm.alert_tip("请选中一个类型再添加!");
						return false;
					}
					$('#myModalLabelTile').html('添加公交财政补贴类型');
					$("#addfiscalSubsidyTypeModal").modal('show');
					var typenode=self.defaultData[0].nodes.find(function(n){
						return n.financialtype==self.selectedFinancialType;
					});
					if(typenode){
						$("#number").val(typenode.text);
					}
					 $("#name").val("");
					 $("#note").val("");
					self.editmode="add";
				},
				saveData:function(){
					var number = self.selectedFinancialType,
						name = $("#name").val(),
						note = $("#note").val(),
						params = {};
						params = {
							"financialtype_object":number,
							"financialtype_name":name,
							"note":note,
							user_id:comm.userInfo.id,
							user_name:comm.userInfo.userName
						}
						if(params.financialtype_name==''||params.financialtype_name==null){
							comm.alert_tip('名称不能为空！');
							return false;
						}
						var actionurl="";
						if(self.editmode=="add"){
							actionurl="addFinancialType";
						}else if(self.editmode="edit"){
                            actionurl="updFinancialType";
                            params.financialtype_id=$("#financialtypeid").val();
						}
						comm.requestDefault('/report/financialtype/'+actionurl, params,function(resp){
							if(resp){
								comm.alert_tip('操作成功！');
								self.ajaxRequestTree({financialtype:number});
								/*self.initialTab(resp);*/
							}
						});
						$("#addfiscalSubsidyTypeModal").modal('hide');
				},
				popupEdit:function(row){
                   var rows = self.mytable.rows('.selected').data();
					if(rows.length == 0){
						 comm.alert_tip('请选择修改项！');
						 return false;
					}
					if(rows.length > 1){
						 comm.alert_tip('请选择一个修改项！');
						 return false;
					}
					var row=rows[0];
					$('#myModalLabelTile').html('修改公交财政补贴类型');
					$("#addfiscalSubsidyTypeModal").modal('show');
					self.editmode="edit";
					var typenode=self.defaultData[0].nodes.find(function(n){
						return n.financialtype==self.selectedFinancialType;
					});
					if(typenode){
						$("#number").val(typenode.text);
					}
					 $("#name").val(row.FINANCIALTYPE_NAME);
					 $("#note").val(row.NOTE);
					 $("#financialtypeid").val(row.FINANCIALTYPE_ID);
				},
				deleteData:function(){
					var rows = self.mytable.rows('.selected').data();
					if(rows.length == 0){
						 comm.alert_tip('请选择删除项！');
						 return false;
					}
					var row=rows[0];
					var number = self.selectedFinancialType;
					var params = {},
						financialtypeid = row.FINANCIALTYPE_ID;
						params = {
							"financialtype_id":financialtypeid
						}
						comm.alert_confirm('危险！确定要删除吗?',function(){
							comm.requestDefault('/report/financialtype/delFinancialType',params,function(resp){
								if(resp){
									comm.alert_tip('删除成功!');
									self.ajaxRequestTree({financialtype:number});
								}
							});
						});
				},
				
			};
			return self;
	});