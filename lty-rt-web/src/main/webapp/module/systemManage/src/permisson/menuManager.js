define([ 'text!systemManage/tpl/permisson/menuManager.html','text!systemManage/tpl/permisson/menu_add.html' ], function(tpl,addTpl) {
	var self = {
		menuId:null,
		menu:null,
		show : function() {
			$('.index_padd').html('');
			$('.index_padd').html(tpl);
			self.menuId = null;
			self.menu = null;
			
			self.initLeftTree();
			
			//添加权限菜单按钮
			$('#btn-insert').click(function(){
				self.addMenu();
			});
			//修改权限菜单按钮
			$('#btn-update').click(function(){
				self.updateMenu();
			});
			
			//删除权限菜单按钮
			$('#btn-del').click(function(){
				 self.deleteMenu();
			});
			
		},
		initLeftTree : function() {
			comm.requestJson('/report/menu/getMenuTree', null,
				function(resp) {
					if(resp.code == 0){
						self.initTree(resp.data);
					}else{
						comm.alert_tip(resp.msg);
					}
					
				},function(resp){
					comm.alert_tip("菜单树加载失败...")
				});
		},
		initTree : function(data) {
			self.myTreeTable = $('#treeview-searchable').treeview({
				levels: 1,
	            showBorder: false,
	            showTags: true,
	            data:data
			});
			
			//左树点击事件
			$("#treeview-searchable").on("nodeSelected",function(event,defaultData){
				self.menu = defaultData;
				var menuId = defaultData.id;
				self.menuId = menuId;
				var menuUrl = defaultData.arg;
				var menuName  = defaultData.text;
				$('#show_menu_name').text(menuName);
				$('#show_menu_url').text(menuUrl);
			});
			//左树取消点击事件
			$("#treeview-searchable").on("nodeUnselected",function(event,defaultData){
				self.menuId = '';
			});
		},
		//添加权限菜单
		addMenu:function(){
			$('#dialogDiv').html('');
			$('#dialogDiv').html(_.template(addTpl, {}));
			$('#add_menuParentId').val(self.menuId);
			$("#addMenuModal").modal({
				keyboard: true
			});
			$('#addMenuModal').modal('show');
			//绑定保存事件
			$('#btn_add_Menu').unbind('click');
			$('#btn_add_Menu').click(function(){
				self.saveMenu();
			});
		},
		//修改权限菜单信息
		updateMenu:function(){
			if(comm.isEmpty(self.menuId)){
				comm.alert_tip("请选择修改的权限菜单");
				return false;
			}

			 $('#dialogDiv').html('');
			 $('#dialogDiv').html(_.template(addTpl, self.menu));
			 $('#myModalLabelTile').html('修改权限菜单');
			 $("#addMenuModal").modal({
					keyboard: true
			 });
			 $('#addMenuModal').modal('show');
			 $('#btn_add_Menu').unbind('click');
			 //绑定保存事件
			 $('#btn_add_Menu').click(function(){
				self.saveMenu();
			 });
				
			
		},
		//删除权限菜单
		deleteMenu:function(){
			if(comm.isEmpty(self.menuId)){
				comm.alert_tip("请选择删除的权限菜单");
				return false;
			}
			//判断菜单是否有子菜单
			if(self.menu.nodes&&self.menu.nodes.length>1){
				comm.alert_tip("请先删除子菜单!");
				return false;
			}
			comm.alert_confirm('危险！删除菜单会导致无权限访问此菜单，您确定要删除此权限菜单吗?',function(){
				comm.requestJson('/report/menu/del', self.menuId, function(resp){
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
		//保存权限菜单基本信息
		saveMenu:function(){
			 var menuName = $.trim($('#add_menuName').val());
			 $('#add_menuName').val(menuName);
			 if(comm.isEmpty(menuName)){
				comm.alert_tip("权限菜单名称必须填写！");
				return false;
			 }
			 var menuUrl = $.trim($('#add_menuUrl').val());
			 $('#add_menuUrl').val(menuUrl);
			 if(comm.isEmpty(menuUrl)){
				comm.alert_tip("权限菜单URL必须填写！");
				return false;
			 }
			 var params =  $("#menu_form").serializeJson();
			 $('#addMenuModal').modal('hide');
			 $('.modal-backdrop').remove();
			 comm.requestDefault('/report/menu/save',params,function(resp){
				 if(resp.resCode == 200){
					 comm.alert_tip("操作成功！");
					 self.show();
				 }else{
					 comm.alert_tip(resp.msg);
				 }
			 });
		}
	};
	return self;
});