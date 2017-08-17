define(
		[ 'text!assessment/tpl/city/assess_total_tab.html' ],
		function(tpl) {
			var self = {
				show : function(indexType) {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					//$('#panel-title-assess-total').text(title);
					$("#assess_total_tab li").each(function(i,v){
						var id = $(this).attr('id');
						$(this).click(function(){
							$("#assess_total_tab li").removeClass('active');
							$(this).addClass('active');
							//按天统计
							if(id=='assess_total_days'){
								require(['assessment/src/city/assess_total_days'], function (assess_total_days) {
									assess_total_days.show(indexType);
								});
							}
							//按周统计
							if(id=='assess_total_weeks'){
								require(['assessment/src/city/assess_total_weeks'], function (assess_total_weeks) {
									assess_total_weeks.show(indexType);
								});
							}
							//按月统计
							if(id=='assess_total_months'){
								require(['assessment/src/city/assess_total_months'], function (assess_total_months) {
									assess_total_months.show(indexType);
								});
							}
							//按年统计
							if(id=='assess_total_years'){
								 require(['assessment/src/city/assess_total_years'], function (assess_total_year) {
									 assess_total_year.show(indexType);
			                	 });
							}
						});
					});
					$('#assess_total_days').click();
				}
			};
			return self;
		});