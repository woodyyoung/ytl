define(
		[ 'text!psgFlowAnalysis/tpl/klfxtd/ztklfx.html' ],
		function(tpl) {
			var self = {
				show : function(title) {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					$('#panel-title-kLfx').text(title);
					$("#ztklfx_tab li").each(function(i,v){
						var id = $(this).attr('id');
						$(this).click(function(){
							$("#ztklfx_tab li").removeClass('active');
							$(this).addClass('active');
							//按小时统计
							if(id=='ztklfx_hours'){
								 require(['psgFlowAnalysis/src/klfxtd/ztklfx_hours'], function (ztklfx_hours) {
									 ztklfx_hours.show();
			                	 });
							}
							//按天统计
							if(id=='ztklfx_days'){
								require(['psgFlowAnalysis/src/klfxtd/ztklfx_days'], function (ztklfx_days) {
									ztklfx_days.show();
								});
							}
							//按周统计
							if(id=='ztklfx_weeks'){
								require(['psgFlowAnalysis/src/klfxtd/ztklfx_weeks'], function (ztklfx_weeks) {
									ztklfx_weeks.show();
								});
							}
							//按月统计
							if(id=='ztklfx_months'){
								require(['psgFlowAnalysis/src/klfxtd/ztklfx_months'], function (ztklfx_months) {
									ztklfx_months.show();
								});
							}
						});
					});
					
					$('#ztklfx_hours').click();
				}
			};
			return self;
		});