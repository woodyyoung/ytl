define(
		[ 'text!psgFlowAnalysis/tpl/dmklfx/dmklfx_tab.html' ],
		function(tpl) {
			var self = {
				show : function(title) {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					$('#panel-title-kLfx').text(title);
					$("#dmklfx_tab li").each(function(i,v){
						var id = $(this).attr('id');
						$(this).click(function(){
							$("#dmklfx_tab li").removeClass('active');
							$(this).addClass('active');
							//按小时统计
							if(id=='dmklfx_hours'){
								 require(['psgFlowAnalysis/src/dmklfx/dmklfx_hours'], function (dmklfx_hours) {
									 dmklfx_hours.show();
			                	 });
							}
							//按天统计
							if(id=='dmklfx_days'){
								require(['psgFlowAnalysis/src/dmklfx/dmklfx_days'], function (dmklfx_days) {
									dmklfx_days.show();
								});
							}
							//按周统计
							if(id=='dmklfx_weeks'){
								require(['psgFlowAnalysis/src/dmklfx/dmklfx_weeks'], function (dmklfx_weeks) {
									dmklfx_weeks.show();
								});
							}
							//按月统计
							if(id=='dmklfx_months'){
								require(['psgFlowAnalysis/src/dmklfx/dmklfx_months'], function (dmklfx_months) {
									dmklfx_months.show();
								});
							}
						});
					});
					
					$('#dmklfx_hours').click();
				}
			};
			return self;
		});