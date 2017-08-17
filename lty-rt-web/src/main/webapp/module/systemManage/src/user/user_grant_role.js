define(
		[ 'text!systemManage/tpl/user/user_grant_role.html' ],
		function(tpl) {
			var self = {
				userTab:null,
				show : function(user) {
					$('.index_padd').html('');
					
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					
					self.loadTableData();
					
					//保存按钮
					$('#btn-grant-role').click(function(){
						self.saveUser(user);
					});
					 
					//返回按钮
					$('#user_back').click(function(){
						self.backMain();
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
				
				//返回主界面
				backMain : function () {
					require(['systemManage/src/user/user_mgmt'], function (user_mgmt) {
						user_mgmt.show();
                	});
				},
				
				//保存用户
				saveUser : function (user) {
					 var rows = self.userTab.rows('.selected').data();
					 if(rows.length != 1){
						 comm.alert_tip('请选择一个角色！');
						 return false;
					 }
					 var param = {};
					 param.id = user.id;
					 param.sex = user.sex;
					 param.state = user.state;
					 param.roleId =rows[0].roleId;
					 //user.roleId =  rows[0].roleId;
					 comm.requestJson('/report/userMgmt/saveUser', JSON.stringify(param), function(resp) {
		                if (resp.data == 0) {
		                	comm.alert_tip('设置角色成功!');
		                	self.backMain();
		                }else{
		                	comm.alert_tip('设置角色失败!');
		                }
		             });
				}
			};
			return self;
		});
		