define(
		[ 'text!systemManage/tpl/role/role.html' ,
		  'text!systemManage/tpl/role/role_add.html',
		  'text!systemManage/tpl/role/grant_menu.html'],
		function(tpl,addTpl,grantMenuTpl) {
			var self = {
				userTab : null,
				myTreeTable:null,
				nodeCheckedSilent:false,
				nodeUncheckedSilent :false,
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					
					self.loadTableData();
					
					//添加角色按钮
					$('#btn-insert').click(function(){
						self.addRole();
					});
					//修改角色按钮
					$('#btn-update').click(function(){
						self.updateRole();
					});
					
					//角色授权菜单管理
					$('#btn-grant-menu').click(function(){
						 //self.deleteRole();
						self.grantMenu();
					});
					
				},
				
				//角色授权功能菜单
				grantMenu:function(){
					var rows = self.userTab.rows('.selected').data();
					if(rows.length != 1){
						comm.alert_tip('请选择一条要授权的角色！');
						return false;
					}
					self.initLeftTree(rows[0]);
					$('#dialogDiv').html('');
					$('#dialogDiv').html(_.template(grantMenuTpl, {}));
					$("#grantMenuModal").modal({
						keyboard: true
					});
					$('#grantMenuModal').modal('show');
					//绑定保存事件
					$('#btn_add_Role').unbind('click');
					$('#btn_add_Role').click(function(){
						self.saveGrantMenu(rows[0]);
					});
					
				},
				//保存角色跟菜单绑定关系
				saveGrantMenu:function(role){
					var nodes = $('#treeview-searchable').treeview('getChecked', 0);
					var menuIds = [];
					for(var i = 0 ;i<nodes.length;i++){
						var node = nodes[i];
						menuIds.push(parseInt(node.id));
					}
					var param = {};
					param.roleId = role.roleId;
					param.menuIds = menuIds;
					 $('#grantMenuModal').modal('hide');
					 $('.modal-backdrop').remove();
					comm.requestJson('/report/role/grantMenu', JSON.stringify(param), function(resp){
						if(resp.resCode == 200){
							comm.alert_tip("授权成功");
							self.show();
						}else{
							comm.alert_tip(resp.msg);
						}
					},function(resp){
						comm.alert_tip("授权失败...")
					});
					
				},
				//添加角色
				addRole:function(){
					$('#dialogDiv').html('');
					$('#dialogDiv').html(_.template(addTpl, {}));
					$("#addRoleModal").modal({
						keyboard: true
					});
					$('#addRoleModal').modal('show');
					//绑定保存事件
					$('#btn_add_Role').unbind('click');
					$('#btn_add_Role').click(function(){
						self.saveRole();
					});
				},
				//修改角色信息
				updateRole:function(){
					 var rows = self.userTab.rows('.selected').data();
					 if(rows.length != 1){
						 comm.alert_tip('请选择一条要修改的角色！');
						 return false;
					 }
					 if(rows[0].roleName=='系统管理员'){
						 comm.alert_tip('系统管理员不能修改');
						 return false;
					 }

					 $('#dialogDiv').html('');
					 $('#dialogDiv').html(_.template(addTpl, rows[0]));
					 $('#addRoleLabelTile').html('修改角色');
					 $("#addRoleModal").modal({
							keyboard: true
					 });
					 $('#addRoleModal').modal('show');
					 $('#btn_add_Role').unbind('click');
					 //绑定保存事件
					 $('#btn_add_Role').click(function(){
						self.saveRole();
					 });
						
					
				},
				//删除角色
				deleteRole:function(){
					if(comm.isEmpty(self.RoleId)){
						comm.alert_tip("请选择删除的角色");
						return false;
					}
					comm.alert_confirm('确定要删除此角色吗?',function(){
						comm.requestJson('/report/role/del', self.RoleId, function(resp){
							if(resp.resCode == 200){
								comm.alert_tip("删除成功");
								self.show();
							}else{
								comm.alert_tip(resp.msg);
							}
						},function(resp){
							comm.alert_tip("删除失败...")
						});
					});
				},
				//保存角色基本信息
				saveRole:function(){
					 var RoleName = $.trim($('#add_RoleName').val());
					 $('#add_RoleName').val(RoleName);
					 if(comm.isEmpty(RoleName)){
						comm.alert_tip("角色名称必须填写！");
						return false;
					 }
					 var RoleUrl = $.trim($('#add_rights').val());
					 $('#add_rights').val(RoleUrl);
					 if(comm.isEmpty(RoleUrl)){
						comm.alert_tip("角色描述必须填写！");
						return false;
					 }
					 var params =  $("#Role_form").serializeJson();
					 $('#addRoleModal').modal('hide');
					 $('.modal-backdrop').remove();
					 comm.requestDefault('/report/role/save',params,function(resp){
						 if(resp.resCode == 200){
							 comm.alert_tip("操作成功！");
							 self.show();
						 }else{
							 comm.alert_tip(resp.msg);
						 }
					 });
				},
				//加载表格数据
				loadTableData : function () {
					comm.requestJson('/report/role/listAllRole', null, function(resp){
						if(resp.resCode == 200){
							self.initTable(resp.data);
						}else{
							comm.alert_tip(resp.msg);
						}
					},function(resp){
						comm.alert_tip("加载失败...");
					});
				},
				
				//渲染表格
				initTable : function (data) {
					self.userTab = $('#role-tab').DataTable({
						destroy: true,
						data: data,
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
							data : 'roleId'
						},
						{
							title:'角色名',
							data : 'roleName'
						}, 
						{
							title:'角色描述',
							data : 'rights'
						}
						],
		                select: {
		                    style:    'os',
		                    selector: 'td:first-child'
		                }
	
					});
				},
				//加载菜单树
				initLeftTree : function(role) {
					comm.requestJson('/report/menu/getMenuTree', null,
						function(resp) {
							if(resp.code == 0){
								self.initTree(role,resp.data);
							}else{
								comm.alert_tip(resp.msg);
							}
							
						},function(resp){
							comm.alert_tip("菜单树加载失败...")
						});
				},
				setNodeState:function(node,role){
					var menus = role.menus;
					for(var j=0;j<menus.length;j++){
						var menu = menus[j];
						if(node.id == menu.menuId ){
							node.state.checked = true;
						}
						if(node.nodes!=null&&node.nodes.length>0){  
					        for(var i in node.nodes){  
					            self.setNodeState(node.nodes[i],role);  
					        }  
					    }  
					}
				},
				initTree : function(role,data) {
					var menus = role.menus;
					if(menus!=null&&menus.length>1){
						for(var i=0;i<data.length;i++){
							var node = data[i];
							self.setNodeState(node,role);
						}
					}
					self.myTreeTable = $('#treeview-searchable').treeview({
						showCheckbox:true,  
						levels: 2,
			            showBorder: false,
			            showTags: true,
			            data:data,
			            onNodeChecked:self.nodeChecked,  
			            onNodeUnchecked:self.nodeUnchecked  
					});
					
					//左树点击事件
					$("#treeview-searchable").on("nodeSelected",function(event,defaultData){
						
					});
					//左树取消点击事件
					$("#treeview-searchable").on("nodeUnselected",function(event,defaultData){
					});
				},
				
				//节点选中
				nodeChecked: function(event, node){  
				    if(self.nodeCheckedSilent){  
				        return;  
				    }  
				    self.nodeCheckedSilent = true;  
				    self.checkAllParent(node);  
				    self.checkAllSon(node);  
				    self.nodeCheckedSilent = false;  
				},
				//取消选择节点
				nodeUnchecked: function(event, node){  
				    if(self.nodeUncheckedSilent)  
				        return;  
				    self.nodeUncheckedSilent = true;  
				    self.uncheckAllParent(node);  
				    self.uncheckAllSon(node);  
				    self.nodeUncheckedSilent = false;  
				},
				//选中全部父节点  
				checkAllParent:function(node){  
				    $('#treeview-searchable').treeview('checkNode',node.nodeId,{silent:true});  
				    var parentNode = $('#treeview-searchable').treeview('getParent',node.nodeId);  
				    if(!("id" in parentNode)){  
				        return;  
				    }else{  
				        self.checkAllParent(parentNode);  
				    }  
				} ,
				//取消全部父节点  
				uncheckAllParent:function(node){  
				    $('#treeview-searchable').treeview('uncheckNode',node.nodeId,{silent:true});  
				    var siblings = $('#treeview-searchable').treeview('getSiblings', node.nodeId);  
				    var parentNode = $('#treeview-searchable').treeview('getParent',node.nodeId);  
				    if(!("id" in parentNode)) {  
				        return;  
				    }  
				    var isAllUnchecked = true;  //是否全部没选中  
				    for(var i in siblings){  
				        if(siblings[i].state.checked){  
				            isAllUnchecked=false;  
				            break;  
				        }  
				    }  
				    if(isAllUnchecked){  
				        self.uncheckAllParent(parentNode);  
				    }  
				  
				},
				  
				//级联选中所有子节点  
				checkAllSon:function(node){  
				    $('#treeview-searchable').treeview('checkNode',node.nodeId,{silent:true});  
				    if(node.nodes!=null&&node.nodes.length>0){  
				        for(var i in node.nodes){  
				            self.checkAllSon(node.nodes[i]);  
				        }  
				    }  
				},
				//级联取消所有子节点  
			    uncheckAllSon:function(node){  
				    $('#treeview-searchable').treeview('uncheckNode',node.nodeId,{silent:true});  
				    if(node.nodes!=null&&node.nodes.length>0){  
				        for(var i in node.nodes){  
				           self.uncheckAllSon(node.nodes[i]);  
				        }  
				    }  
				}  
				
			};
			return self;
		});