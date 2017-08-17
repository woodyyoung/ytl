define(
		[ 'text!psgFlowAnalysis/tpl/xlklfx/xlklfx_tab.html' ],
		function(tpl) {
			var self = {
				show : function(title) {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					$('#panel-title-kLfx').text(title);
					$("#xlklfx_tab li").each(function(i,v){
						var id = $(this).attr('id');
						$(this).click(function(){
							$("#xlklfx_tab li").removeClass('active');
							$(this).addClass('active');
							//按小时统计
							if(id=='xlklfx_hours'){
								 require(['psgFlowAnalysis/src/xlklfx/xlklfx_hours'], function (xlklfx_hours) {
									 xlklfx_hours.show();
			                	 });
							}
							//按天统计
							if(id=='xlklfx_days'){
								require(['psgFlowAnalysis/src/xlklfx/xlklfx_days'], function (xlklfx_days) {
									xlklfx_days.show();
								});
							}
							//按周统计
							if(id=='xlklfx_weeks'){
								require(['psgFlowAnalysis/src/xlklfx/xlklfx_weeks'], function (xlklfx_weeks) {
									xlklfx_weeks.show();
								});
							}
							//按月统计
							if(id=='xlklfx_months'){
								require(['psgFlowAnalysis/src/xlklfx/xlklfx_months'], function (xlklfx_months) {
									xlklfx_months.show();
								});
							}
						});
					});
					
					$('#xlklfx_hours').click();
				}
			};
			return self;
		});