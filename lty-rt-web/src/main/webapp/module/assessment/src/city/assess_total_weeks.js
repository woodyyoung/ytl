define(
		[ 'assessment/src/city/assess_total_comm' ,'text!assessment/tpl/city/assess_total_weeks.html' ],
		function(totalComm,tpl) {
			var self = {
				show : function(indexType) {
					$('#assess_total_form').html(tpl);
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
					
					totalComm.show(indexType,'weeks','#btn_weeks_confirm');
				}
			};
			return self;
		});