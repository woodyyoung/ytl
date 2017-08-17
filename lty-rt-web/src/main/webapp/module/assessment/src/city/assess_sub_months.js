define(
		['assessment/src/city/assess_sub_comm' ,  'text!assessment/tpl/city/assess_sub_months.html' ],
		function(subComm,tpl) {
			var self = {
					
				show : function(indexType) {
					$('#assess_sub_form').html(tpl);
					//初始化日历控件
					 $("#begin_date").datetimepicker({
						format : 'yyyy-mm',
						language : "zh-CN",
						autoclose : true,
						minView : 'year',
						startView : 'year'
					});
					$("#end_date").datetimepicker({
						format : 'yyyy-mm',
						language : "zh-CN",
						autoclose : true,
						minView : 'year',
						startView : 'year'
					});
					$("#begin_date2").datetimepicker({
						format : 'yyyy-mm',
						language : "zh-CN",
						autoclose : true,
						minView : 'year',
						startView : 'year'
					});
					$("#end_date2").datetimepicker({
						format : 'yyyy-mm',
						language : "zh-CN",
						autoclose : true,
						minView : 'year',
						startView : 'year'
					});
					
					subComm.show(indexType,'months','#btn_months_confirm');
				}
				
			};
			return self;
		});