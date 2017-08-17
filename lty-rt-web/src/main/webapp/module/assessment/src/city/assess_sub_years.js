define(
		['assessment/src/city/assess_sub_comm' ,  'text!assessment/tpl/city/assess_sub_years.html' ],
		function(subComm,tpl) {
			var self = {
				show : function(indexType) {
					$('#assess_sub_form').html(tpl);
					
					//初始化日历控件
					 $("#begin_date").datetimepicker({
						format : 'yyyy',
						language : "zh-CN",
						autoclose : true,
						minView : 4,
						startView : 4
					});
					$("#end_date").datetimepicker({
						format : 'yyyy',
						language : "zh-CN",
						autoclose : true,
						minView : 4,
						startView : 4
					});
					$("#begin_date2").datetimepicker({
						format : 'yyyy',
						language : "zh-CN",
						autoclose : true,
						minView : 4,
						startView : 4
					});
					$("#end_date2").datetimepicker({
						format : 'yyyy',
						language : "zh-CN",
						autoclose : true,
						minView : 4,
						startView : 4
					});
					
					subComm.show(indexType,'years','#btn_years_confirm');
				}
			};
			return self;
		});