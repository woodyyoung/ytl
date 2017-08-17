define(
		[ 'text!assessment/tpl/city/assess_sub_tab.html' ],
		function(tpl) {
			var self = {
				show : function(indexType) {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					//$('#panel-title-assess-sub').text(title);
					$("#assess_sub_tab li").each(function(i,v){
						var id = $(this).attr('id');
						$(this).click(function(){
							$("#assess_sub_tab li").removeClass('active');
							$(this).addClass('active');
							//按天统计
							if(id=='assess_sub_days'){
								require(['assessment/src/city/assess_sub_days'], function (assess_sub_days) {
									assess_sub_days.show(indexType);
								});
							}
							//按周统计
							if(id=='assess_sub_weeks'){
								require(['assessment/src/city/assess_sub_weeks'], function (assess_sub_weeks) {
									assess_sub_weeks.show(indexType);
								});
							}
							//按月统计
							if(id=='assess_sub_months'){
								require(['assessment/src/city/assess_sub_months'], function (assess_sub_months) {
									assess_sub_months.show(indexType);
								});
							}
							//按年统计
							if(id=='assess_sub_years'){
								 require(['assessment/src/city/assess_sub_years'], function (assess_sub_year) {
									 assess_sub_year.show(indexType);
			                	 });
							}
						});
					});
					$('#assess_sub_days').click();
				}
			};
			return self;
		});