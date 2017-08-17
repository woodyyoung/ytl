define(['text!psgFlowAnalysis/tpl/GIS/passengerFlowLine.html'],
function(tpl) {
    //Do setup work here
    var self = {
        mapObj: null,
        infoWindow: null,
        folwDegree:null,
        show: function() {
            $('.index_padd').html('');
            $('.index_padd').html(tpl);
            $(".passenger_btn_toggle").click(function() {
				var passenger_float = $(".passenger_float");
				if (passenger_float.is(":hidden")) {
					passenger_float.slideDown();
					$(".passenger_btn_toggle").addClass("passenger_btn_open").removeClass("passenger_btn_close");
					$(".passenger_btn_click").animate({"bottom":295});
				} else {
					passenger_float.slideUp();
					$(".passenger_btn_toggle").addClass("passenger_btn_close").removeClass("passenger_btn_open");
					$(".passenger_btn_click").animate({"bottom":0});
				}
			});
            $("#xlklfx_tab li").each(function(i, v) {
                var id = $(this).attr('id');
                $(this).click(function() {
                    $("#xlklfx_tab li").removeClass('active');
                    $(this).addClass('active');
                    //按小时统计
                    if (id == 'xlklfx_hours') {
                        self.dataType = "H";
                        require(['psgFlowAnalysis/src/GIS/dateType/xlklfx_hours'],
                        function(xlklfx_hours) {
                        	if(self.folwDegree==null){
                        		self.initPlanDiv(xlklfx_hours);
                        	}else{
                        		xlklfx_hours.show(self.folwDegree);
                        	}
                        });
                    }
                    //按天统计
                    if (id == 'xlklfx_days') {
                        require(['psgFlowAnalysis/src/GIS/dateType/xlklfx_days'],
                        function(xlklfx_days) {
                        	if(self.folwDegree==null){
                        		self.initPlanDiv(xlklfx_days);
                        	}else{
                        		xlklfx_days.show(self.folwDegree);
                        	}
                        });
                    }
                    //按周统计
                    if (id == 'xlklfx_weeks') {
                        require(['psgFlowAnalysis/src/GIS/dateType/xlklfx_weeks'],
                        function(xlklfx_weeks) {
                        	if(self.folwDegree==null){
                        		self.initPlanDiv(xlklfx_weeks);
                        	}else{
                        		xlklfx_weeks.show(self.folwDegree);
                        	}
                        });
                    }
                    //按月统计
                    if (id == 'xlklfx_months') {
                        require(['psgFlowAnalysis/src/GIS/dateType/xlklfx_months'],
                        function(xlklfx_months) {
                        	if(self.folwDegree==null){
                        		self.initPlanDiv(xlklfx_months);
                        	}else{
                        		xlklfx_months.show(self.folwDegree);
                        	}
                        });
                    }
                });
            });
            
			
            //初始化下拉框数据
            comm.requestJson('/report/lineAnalysis/listAllLine', null,
            function(resp) {
                comm.initSelectOptionForObj('test', resp.data, 'name', 'id');
            });

            $('#xlklfx_hours').click();
            self.initPlanDiv();
            
        },
        initPlanDiv:function(tab){
        	var params = {};
            params.dataType = '002';
            params.isDisable = '1';
        	comm.requestJson('/report/passengerFlowLevel/planAndLevelList', JSON.stringify(params),
        	            function(resp) {
        					self.folwDegree = resp;
        					comm.initSelectOptionForObj('planName', resp, 'planName', 'id');
        					comm.initLevelDiv(resp[0].levelList);
        					if(comm.isNotEmpty(tab)){
        						tab.show(self.folwDegree);
        					}
        	            });
        	
        }
    };
    return self;
});