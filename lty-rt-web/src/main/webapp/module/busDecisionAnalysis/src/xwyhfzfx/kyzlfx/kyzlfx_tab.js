define([ 'text!busDecisionAnalysis/tpl/xwyhfzfx/kyzlfx/kyzlfx_tab.html' ],
	function(tpl) {
		var self = {
			folwDegree:null,
			show : function() {
				$('.index_padd').html('');
				$('.index_padd').html(tpl);
				
				tab_comm.initTab();
				
				$("#psg_tab li").each(function(i,v){
					var id = $(this).attr('id');
					
					$(this).click(function(){
						
						$("#psg_tab li").removeClass('active');
						$(this).addClass('active');
						
						// 按小时分析
						if(id=='psg_hours'){
							 require(['busDecisionAnalysis/src/xwyhfzfx/kyzlfx/kyzlfx_hour'], function (psg_hours) {
								 psg_hours.show();
		                	 });
						}
						
						// 按天分析
						if(id=='psg_days'){
							require(['busDecisionAnalysis/src/xwyhfzfx/kyzlfx/kyzlfx_day'], function (psg_days) {
								psg_days.show();
							});
						}
						
						// 按周分析
						if(id=='psg_weeks'){
							require(['busDecisionAnalysis/src/xwyhfzfx/kyzlfx/kyzlfx_week'], function (psg_weeks) {
								psg_weeks.show();
							});
						}
						
						// 按月分析
						if(id=='psg_months'){
							require(['busDecisionAnalysis/src/xwyhfzfx/kyzlfx/kyzlfx_month'], function (psg_months) {
								psg_months.show();
							});
						}
					});
				});
				
				$('#psg_hours').click();
			}
		};
		return self;
	});