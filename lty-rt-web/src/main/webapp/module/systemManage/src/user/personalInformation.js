define([ 'text!systemManage/tpl/user/personalInformation.html' ],
		function(tpl) {
			var self = {
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);

					$(".select2_multiple").select2({
						maximumSelectionLength : 5,
						placeholder : "最大可选择5个",
						allowClear : true
					});
				}
			};
			return self;
		});