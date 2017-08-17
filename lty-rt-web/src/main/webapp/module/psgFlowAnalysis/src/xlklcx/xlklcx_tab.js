define(
		[ 'text!psgFlowAnalysis/tpl/xlklcx/xlklcx_tab.html' ],
		function(tpl) {
			var self = {
				show : function(title) {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					$('#panel-title-kLfx').text(title);
					$("#xlklcx_tab li").each(function(i,v){
						var id = $(this).attr('id');
						$(this).click(function(){
							$("#xlklcx_tab li").removeClass('active');
							$(this).addClass('active');
							//按小时统计
							if(id=='xlklcx_hours'){
								 require(['psgFlowAnalysis/src/xlklcx/xlklcx_hours'], function (xlklcx_hours) {
									 xlklcx_hours.show();
			                	 });
							}
							//按天统计
							if(id=='xlklcx_days'){
								require(['psgFlowAnalysis/src/xlklcx/xlklcx_days'], function (xlklcx_days) {
									xlklcx_days.show();
								});
							}
							//按周统计
							if(id=='xlklcx_weeks'){
								require(['psgFlowAnalysis/src/xlklcx/xlklcx_weeks'], function (xlklcx_weeks) {
									xlklcx_weeks.show();
								});
							}
							//按月统计
							if(id=='xlklcx_months'){
								require(['psgFlowAnalysis/src/xlklcx/xlklcx_months'], function (xlklcx_months) {
									xlklcx_months.show();
								});
							}
						});
					});
					
					$('#xlklcx_hours').click();
				}
			};
			return self;
		});