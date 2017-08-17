define([ 'text!systemManage/tpl/user/user_query.html' ], function(tpl) {
	var self = {
		id : '',
		show : function(id) {

			//记录ID
			self.id = id;

			//加载form表单
			self.loadFormTab();

		},
		
		//加载表单数据
		loadFormTab : function() {
			if (comm.isEmpty(self.id)) {
				$('.index_padd').html(_.template(tpl, null));
			} else {
				var params = {
					id : self.id
				};
				comm.requestJson('/report/userMgmt/queryUserById', JSON
						.stringify(params), function(resp) {
					$('.index_padd').html(_.template(tpl, resp.data));
				});
			}
		}
	};
	return self;
});
