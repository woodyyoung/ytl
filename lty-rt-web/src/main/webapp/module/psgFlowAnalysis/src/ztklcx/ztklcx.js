define(
		[ 'text!psgFlowAnalysis/tpl/ztklcx/ztklcx.html' ],
		function(tpl) {
			var self = {
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					$('#panel-title-kLfx').text('站台客流查询');
					$("#ztklcx_tab li").each(function(i,v){
						var id = $(this).attr('id');
						$(this).click(function(){
							$("#ztklcx_tab li").removeClass('active');
							$(this).addClass('active');
							//按小时统计
							if(id=='ztklcx_hours'){
								 require(['psgFlowAnalysis/src/ztklcx/ztklcx_hours'], function (ztklcx_hours) {
									 ztklcx_hours.show();
			                	 });
							}
							//按天统计
							if(id=='ztklcx_days'){
								require(['psgFlowAnalysis/src/ztklcx/ztklcx_days'], function (ztklcx_days) {
									ztklcx_days.show();
								});
							}
							//按周统计
							if(id=='ztklcx_weeks'){
								require(['psgFlowAnalysis/src/ztklcx/ztklcx_weeks'], function (ztklcx_weeks) {
									ztklcx_weeks.show();
								});
							}
							//按月统计
							if(id=='ztklcx_months'){
								require(['psgFlowAnalysis/src/ztklcx/ztklcx_months'], function (ztklcx_months) {
									ztklcx_months.show();
								});
							}
						});
					});
					
					$('#ztklcx_hours').click();
				}
			};
			return self;
		});