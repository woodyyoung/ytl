//公交都市评价等级设置
define(
		[ 'text!busAppraise/tpl/levelSeting/levelSeting.html' ],
		function(tpl) {
			//Do setup work here
			var self = {
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
				}
			};
			return self;
		});