define(
		[ 'text!psgFlowAnalysis/tpl/dmklcx/dmklcx_tab.html' ],
		function(tpl) {
			var self = {
				show : function(title) {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					$('#panel-title-kLfx').text(title);
					$("#dmklcx_tab li").each(function(i,v){
						var id = $(this).attr('id');
						$(this).click(function(){
							$("#dmklcx_tab li").removeClass('active');
							$(this).addClass('active');
							//按小时统计
							if(id=='dmklcx_hours'){
								 require(['psgFlowAnalysis/src/dmklcx/dmklcx_hours'], function (dmklcx_hours) {
									 dmklcx_hours.show();
			                	 });
							}
							//按天统计
							if(id=='dmklcx_days'){
								require(['psgFlowAnalysis/src/dmklcx/dmklcx_days'], function (dmklcx_days) {
									dmklcx_days.show();
								});
							}
							//按周统计
							if(id=='dmklcx_weeks'){
								require(['psgFlowAnalysis/src/dmklcx/dmklcx_weeks'], function (dmklcx_weeks) {
									dmklcx_weeks.show();
								});
							}
							//按月统计
							if(id=='dmklcx_months'){
								require(['psgFlowAnalysis/src/dmklcx/dmklcx_months'], function (dmklcx_months) {
									dmklcx_months.show();
								});
							}
						});
					});
					
					$('#dmklcx_hours').click();
				}
			};
			return self;
		});