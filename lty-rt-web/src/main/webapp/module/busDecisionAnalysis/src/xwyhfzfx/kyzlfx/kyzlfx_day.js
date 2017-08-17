define([ 'text!busDecisionAnalysis/tpl/xwyhfzfx/kyzlfx/kyzlfx_day.html' ],
	function(tpl) {
		var self = {
			show : function() {
				tab_comm.initPage(tpl);
				tab_comm.clearMapAndChart();
				
				// 点击分析按钮
				$('#btn_day_confirm').click(function(){
					self.analysis();
				});
			},
			
			/**
			 * 客运走廊分析
			 */
			analysis : function() {
				tab_comm.beginDate = $('#begin_date').val();
				tab_comm.endDate = $('#end_date').val();
				tab_comm.areaCode = $('#xqselect').val();
				tab_comm.flowDirection = $('input[name=flowDirection]:checked').val();
				tab_comm.workType = $('input[name=workType]:checked').val();
				
				if(comm.isEmpty(tab_comm.beginDate) || comm.isEmpty(tab_comm.endDate)) {
					comm.alert_tip('请选择时间！');
					return false;
				}
				
				var params = {
					beginDate : tab_comm.beginDate,
					endDate : tab_comm.endDate,
					areaCode : tab_comm.areaCode,
					flowDirection : tab_comm.flowDirection,
					workType : tab_comm.workType
				};
				
				comm.requestJson('/report/psgCorridorAnalysis/queryPsgCorridorByDay', JSON
						.stringify(params), function(resp) {
					tab_comm.map.clearMap();
					tab_comm.polygons = [];
					tab_comm.clearMapAndChart();
					
					if(resp.data == null || resp.data == ''){
						return;
					}
					
					if(tab_comm.flowDirection == 0){
						tab_comm.loadFlowOutTop(resp.data.flowOut, resp.data.areaInfoList);
						tab_comm.loadFlowInTop(resp.data.flowIn, resp.data.areaInfoList);
					}else if(tab_comm.flowDirection == 1){
						//TopOut10图标处理
						tab_comm.loadFlowOutTop(resp.data.flowOut, resp.data.areaInfoList);
					}else if(tab_comm.flowDirection == 2){
						//Topin10图标处理
						tab_comm.loadFlowInTop(resp.data.flowIn, resp.data.areaInfoList);
					}
					
					//客流分析图标处理
					tab_comm.loadFlowAnalysis(resp.data.flowAnalysis);
					
					//处理地图小区和连接线
					tab_comm.loadMapLine(resp.data.adjoinList, resp.data.areaInfoList, resp.data.adjoinAreaFlow, resp.data.flowOut, resp.data.flowIn);
				});
			}
		};
		return self;
	});