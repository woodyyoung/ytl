define(
		[ 'text!psgFlowAnalysis/tpl/xqklcx/xqklcx_tab.html' ],
		function(tpl) {
			var self = {
				show : function(title) {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					$('#panel-title-kLfx').text(title);
					$("#xqklcx_tab li").each(function(i,v){
						var id = $(this).attr('id');
						$(this).click(function(){
							$("#xqklcx_tab li").removeClass('active');
							$(this).addClass('active');
							//按小时统计
							if(id=='xqklcx_hours'){
								 require(['psgFlowAnalysis/src/xqklcx/xqklcx_hours'], function (xqklcx_hours) {
									 xqklcx_hours.show();
			                	 });
							}
							//按天统计
							if(id=='xqklcx_days'){
								require(['psgFlowAnalysis/src/xqklcx/xqklcx_days'], function (xqklcx_days) {
									xqklcx_days.show();
								});
							}
							//按周统计
							if(id=='xqklcx_weeks'){
								require(['psgFlowAnalysis/src/xqklcx/xqklcx_weeks'], function (xqklcx_weeks) {
									xqklcx_weeks.show();
								});
							}
							//按月统计
							if(id=='xqklcx_months'){
								require(['psgFlowAnalysis/src/xqklcx/xqklcx_months'], function (xqklcx_months) {
									xqklcx_months.show();
								});
							}
						});
					});
					
					$('#xqklcx_hours').click();
				}
			};
			return self;
		});