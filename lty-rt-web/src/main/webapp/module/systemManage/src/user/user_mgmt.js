define(
		[ 'text!systemManage/tpl/user/user_mgmt.html',	  'text!systemManage/tpl/user/user_update_pwd.html' ],
		function(tpl,updatePwdTpl) {
			var self = {
				userTab : null,
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(_.template(tpl,null));
					console.log(comm.userInfo.role.roleId);
					if(comm.userInfo.role.roleId != null && comm.userInfo.role.roleId ==1 )
						$('.user_btn').append("<button type=\"button\" class=\"btn btn-danger\" id =\"btn-update-pwd\">修改密码</button>");
						$('.user_btn').append("<button type=\"button\" class=\"btn btn-primary\" id =\"btn-pass-reset\">重置密码</button>");
					self.loadTableData();
					
					//添加按钮action
					$('#btn-insert').click(function(){
						 require(['systemManage/src/user/user_add'], function (user_add) {
							 user_add.show('');
	                	 });
					});
					
					//修改按钮action
					$('#btn-update').click(function(){
						 var rows = self.userTab.rows('.selected').data();
						 
						 if(rows.length == 0){
							 comm.alert_tip('请选择修改的用户！');
							 return false;
						 }
						 
						 if(rows.length > 1){
							 comm.alert_tip('请选择一个修改的用户！');
							 return false;
						 }
						 
						 var id = rows[0].id;
						 require(['systemManage/src/user/user_add'], function (user_add) {
							 user_add.show(id);
	                	 });
					});
					
					//删除按钮action
					$('#btn-del').click(function(){
						 var rows = self.userTab.rows('.selected').data();
						 
						 if(rows.length == 0){
							 comm.alert_tip('请选择删除的用户！');
							 return false;
						 }
						 
						 if(rows.length > 1){
							 comm.alert_tip('请选择一个删除的用户！');
							 return false;
						 }
						 
						 var idStr = '';
						 for(var i=0; i<rows.length; i++){
							 idStr += rows[i].id + ','
							 if(rows[i].userName=='admin'){
								 comm.alert_tip('admin的用户不能删除！');
								 return false;
							 }
						 }
						 
						 var params = {
							id : idStr.substring(0, idStr.length-1)	 
						 };
						 comm.alert_confirm('确定要删除此用户吗?',function(){
							 comm.requestJson('/report/userMgmt/delUser', JSON.stringify(params), function(resp){
								if(resp.code == 0){
									 comm.alert_tip('删除成功!');
									self.loadTableData();
								}else{
									console.error(resp.msg);
								}
							 },function(resp){
								 console.error("删除失败...");
							 });
						 });
					});
					
					//重置密码action
					$('#btn-pass-reset').click(function(){
						 var rows = self.userTab.rows('.selected').data();
						 
						 if(rows.length != 1){
							 comm.alert_tip('请选择一个重置的用户！');
							 return false;
						 }
						 
						 var idStr = '';
						 for(var i=0; i<rows.length; i++){
							 idStr += rows[i].id + ','
						 }
						 
						 var params = {
							id : idStr.substring(0, idStr.length-1)	 
						 };
						 comm.alert_confirm('确定要重置此用户密码吗?',function(){
							 comm.requestJson('/report/userMgmt/userPassReset', JSON.stringify(params), function(resp){
								if(resp.code == 0){
									comm.alert_tip('密码重置成功！请记住重置后的密码为:888888');
									self.loadTableData();
								}else{
									console.error(resp.msg);
								}
							 },function(resp){
								 console.error("重置失败...");
							 });
						 });
					});
					
					//设置角色
					$('#btn-grant-role').click(function(){
						 self.grantRole();
					});
					
					//修改密码
					$('#btn-update-pwd').click(function(){
						 self.updatePwd();
					});
					
				},
				//设置角色
				grantRole:function(){
					 var rows = self.userTab.rows('.selected').data();
					 if(rows.length != 1){
						 comm.alert_tip('请选择一个用户！');
						 return false;
					 }
					 require(['systemManage/src/user/user_grant_role'], function (user_grant_role) {
						 user_grant_role.show(rows[0]);
                	 });
					 
				},
				
				//修改密码
				updatePwd:function(){
					var rows = self.userTab.rows('.selected').data();
					if(rows.length != 1){
						comm.alert_tip('请选择一个用户！');
						return false;
					}
					if(rows[0].roleId == 1){
						comm.alert_tip('只能修改非系统管理员的密码!');
						return false;
					}
					$('#dialogDiv').html('');
					$('#dialogDiv').html(_.template(updatePwdTpl, {}));
					$("#user_updatePWD_modal").modal({
						keyboard: true
					});
					$('#user_updatePWD_modal').modal('show');
					//绑定保存事件
					$('#update_submit_updatePWD').unbind('click');
					$('#update_submit_updatePWD').click(function(){
						 var newPWD = $.trim($("#update_newPWD	").val());
	                 	 var oldPWD = $.trim($("#update_newPWD_tow").val());
	                 	 if(comm.isEmpty(newPWD)){
	                 		comm.alert_tip('请输入密码！');
	                 		return false;
	                 	 }
	                 	 if(comm.isEmpty(newPWD)){
	                 		comm.alert_tip('请再次输入密码！');
	                 		return false;
	                 	 }
	                 	 //对比新旧密码是否一致
	            		 if(oldPWD!=newPWD){
	            			 comm.alert_tip('两次输入的密码不相同！');
	            			 return false;
	            		 }
	            		 if(newPWD.length<6){
	                 		comm.alert_tip('密码长度应该在6~15位之间！');
	                 		return false;
	                 	 }
	            		 var params={};
	            		 params.userId=rows[0].id;
	            		 params.pwd = newPWD;
	            		 $('#user_updatePWD_modal').modal('hide');
						 $('.modal-backdrop').remove();
	            		 //保存
	            		 comm.requestJson('/report/userMgmt/updatePwd', JSON.stringify(params), function(resp) {
	 		                if (resp.data == 0) {
	 		                	comm.alert_tip('修改成功!');
	 		                }else{
	 		                	console.error("修改失败");
	 		                }
	 		             });
	            		 
	            		 
					});
					
				},
				
				//加载表格数据
				loadTableData : function () {
					comm.requestJson('/report/userMgmt/queryAllUser', null, function(resp){
						if(resp.code == 0){
							self.initTable(resp.data);
						}else{
							console.error(resp.msg);
						}
					},function(resp){
						console.error("加载失败...");
					});
				},
				
				//渲染表格
				initTable : function (data) {
					self.userTab = $('#user-tab').DataTable({
						destroy: true,
						data: data,
						order: [[ 1, "userName" ]],
						language:dataTable_cn,
						columns : [
						 {
		                  data: null,
		                  defaultContent: '',
		                  className: 'select-checkbox',
		                  orderable: false
		                },
						{
							title:'用户名',
							data : 'userName'
						}, 
						{
							title:'电话号码',
							data : 'phoneNumber'
						},
						{
							title:'邮箱',
							data : 'email'
						},
						{
							title:'地址',
							data : "address"
						},
						{
							title:'性别',
							data : "sex"
						},
						{
							title:'真实姓名',
							data : "realName"
						},
						{
							title:'角色',
							data : "role",
							render: function(data, type, row, meta) {
					            if(row.role!=null){
					            	return row.role.roleName;
					            }
					            return data;
					       }
						},
						{
							title:'创建时间',
							data : "createDate"
						}
						],
		                select: {
		                    style:    'os',
		                    selector: 'td:first-child'
		                }
	
					});
				}
				
			};
			return self;
		});