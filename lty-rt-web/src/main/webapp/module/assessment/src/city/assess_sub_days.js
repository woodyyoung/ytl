define(
		['assessment/src/city/assess_sub_comm' , 'text!assessment/tpl/city/assess_sub_days.html' ],
		function(subComm,tpl) {
			var self = {
				show : function(indexType) {
					$('#assess_sub_form').html(tpl);
					
					//初始化日历控件
					 $("#begin_date").datetimepicker({
						format : 'yyyy-mm-dd',
						language : "zh-CN",
						autoclose : true,
						minView : 'month'
					});
					$("#end_date").datetimepicker({
						format : 'yyyy-mm-dd',
						language : "zh-CN",
						autoclose : true,
						minView : 'month'
					});
					$("#begin_date2").datetimepicker({
						format : 'yyyy-mm-dd',
						language : "zh-CN",
						autoclose : true,
						minView : 'month'
					});
					$("#end_date2").datetimepicker({
						format : 'yyyy-mm-dd',
						language : "zh-CN",
						autoclose : true,
						minView : 'month'
					});
					
					subComm.show(indexType,'days','#btn_days_confirm');
				}
				
				
			};
			return self;
		});