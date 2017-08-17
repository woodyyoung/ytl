define(
		[ 'text!systemManage/tpl/user/user_add.html' ],
		function(tpl) {
			var self = {
				id : '',
				show : function(id) {
					$('.index_padd').html('');
					
					//记录ID
					self.id = id;
					
					//加载form表单
					self.loadFormTab();
					
					//保存按钮
					$('#user_save').click(function(){
						self.saveUser();
					});
					 
					//返回按钮
					$('#user_back').click(function(){
						self.backMain();
					});
				},
				
				//返回主界面
				backMain : function () {
					require(['systemManage/src/user/user_mgmt'], function (user_mgmt) {
						user_mgmt.show();
                	});
				},
				
				//保存用户
				saveUser : function () {
					var params = $("#user_form").serializeJson();
					
					if(comm.isEmpty(params.userName)){
						comm.alert_tip('用户名不能为空！');
						return false;
					}
					
					if(comm.isEmpty(params.realName)){
						comm.alert_tip('姓名不能为空！');
						return false;
					}
					
					comm.requestJson('/report/userMgmt/saveUser', JSON.stringify(params), function(resp) {
		                if (resp.data == 0) {
		                	comm.alert_tip('新增用户成功! 请记住初始化密码为888888');
		                	self.backMain();
		                }else{
		                	console.error("保存失败");
		                }
		            });
				},
				
				//加载表单数据
				loadFormTab : function () {
					if(comm.isEmpty(self.id)){
						$('.index_padd').html(_.template(tpl, null));
					}else{
						var params = {
							id : self.id
						};
						comm.requestJson('/report/userMgmt/queryUserById', JSON.stringify(params), function(resp) {
		                    $('.index_padd').html(_.template(tpl, resp.data));
		                    
		                    $("title").val("修改用户");
		                    
		                    //保存按钮
							$('#user_save').click(function(){
								self.saveUser();
							});
							 
							//返回按钮
							$('#user_back').click(function(){
								self.backMain();
							});
		                });
					}
				}
			};
			return self;
		});
		