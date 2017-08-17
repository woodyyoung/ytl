define(
		[ 'text!psgFlowAnalysis/tpl/xqklfx/xqklfx_tab.html' ],
		function(tpl) {
			var self = {
				show : function(title) {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					$('#panel-title-kLfx').text(title);
					$("#xqklfx_tab li").each(function(i,v){
						var id = $(this).attr('id');
						$(this).click(function(){
							$("#xqklfx_tab li").removeClass('active');
							$(this).addClass('active');
							//按小时统计
							if(id=='xqklfx_hours'){
								 require(['psgFlowAnalysis/src/xqklfx/xqklfx_hours'], function (xqklfx_hours) {
									 xqklfx_hours.show();
			                	 });
							}
							//按天统计
							if(id=='xqklfx_days'){
								require(['psgFlowAnalysis/src/xqklfx/xqklfx_days'], function (xqklfx_days) {
									xqklfx_days.show();
								});
							}
							//按周统计
							if(id=='xqklfx_weeks'){
								require(['psgFlowAnalysis/src/xqklfx/xqklfx_weeks'], function (xqklfx_weeks) {
									xqklfx_weeks.show();
								});
							}
							//按月统计
							if(id=='xqklfx_months'){
								require(['psgFlowAnalysis/src/xqklfx/xqklfx_months'], function (xqklfx_months) {
									xqklfx_months.show();
								});
							}
						});
					});
					
					$('#xqklfx_hours').click();
				}
			};
			return self;
		});