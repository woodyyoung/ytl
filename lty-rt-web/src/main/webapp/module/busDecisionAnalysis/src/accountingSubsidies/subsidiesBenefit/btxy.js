define(
		[ 'text!busDecisionAnalysis/tpl/accountingSubsidies/subsidiesBenefit/btxy.html' ],
		function(tpl) {
			var self = {
				show : function(title) {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					//初始化饼图
					/*self.ndbhEcharts_pie();*/
					//初始化日历控件
					$("#begin_date").datetimepicker({
						format : 'yyyy',
						language : "zh-CN",
						startView:'decade',
		            	minView: 'decade'

					});
					$("#end_date").datetimepicker({
						format : 'yyyy',
						language : "zh-CN",
						startView:'decade',
		            	minView: 'decade'
					});
					
					//查询按钮
		            $('#btn_hours_confirm').click(function() {
		            	self.getData();
		            });
		            
		            self.initChart();
		            
				},
				initChart: function(){
					self.myLChart = echarts.init(document.getElementById('ndbhEcharts_fold'));
					self.myPChart = echarts.init(document.getElementById('ndbhEcharts_pie'));
					self.myLChart.setOption({
						title : {
							text : '财政投入和营收'
						},
						tooltip : {
							trigger : 'axis'
						},
						legend : {
							data : []
						},
						dataZoom : [ { // 这个dataZoom组件，默认控制x轴。
							type : 'slider', // 这个 dataZoom 组件是 slider 型
							// dataZoom 组件
							start : 0, // 左边在 10% 的位置。
							end : 100
						// 右边在 60% 的位置。
						}, { // 这个dataZoom组件，也控制x轴。
							type : 'inside', // 这个 dataZoom 组件是 inside 型
							// dataZoom 组件
							start : 0, // 左边在 10% 的位置。
							end : 100
						// 右边在 60% 的位置。
						} ],
						grid : {
							left : '3%',
							right : '4%',
							bottom : '3%',
							containLabel : true
						},
						toolbox : {
							feature : {
								saveAsImage : {}
							}
						},
						xAxis : {
							type : 'category',
							boundaryGap : false,
							data : [],
							axisLabel : {
				                formatter: '{value} 年'
				            }
						},
						yAxis : {
							type : 'value',
							axisLabel : {
				                formatter: '{value} 元/年'
				            }
						},
						series : []
					
				});
					self.myPChart.setOption({
						title : {
							text : '公交财政补贴构成比例'
						},
						legend : {
							data : []
						},
						tooltip : {
						    trigger: 'item',
						    formatter: "{a} <br/>{b} : {c} ({d}%)"
						},
						series : [
									{
									    type:'pie'
									}
						          ]
					
				});
				},
				
				ndbhEcharts_fold : function(data){
					if(!data||data.length<1){
						self.initChart();
						return false;
					}
					self.myLChart.on('click', function (params) {
		                if(params.componentType=="series"){
		                	var xName = params.seriesName;
		                	var Name = params.name;
							var params = {};
							params.occurtime = Name;
			                comm.requestJson('/report/busrevenue/getIndustryPieData', JSON.stringify(params), function(resp){
			                	if(resp.pieData){
			                		self.ndbhEcharts_pie(resp.pieData);
			                	}
			            	},function(resp){
			            		alert("查询失败...");
			            	});
		                
		                }
		            });
					    var legendData = ['营收','投入'];
			            var xAxisData = [];
			            // 显示标题，图例和空的坐标轴

			            var Item = function() {
			                return {
			                    name: '',
			                    type: 'line',
			                    data: []
			                }
			            };

			            // series中的每一项为一个item,所有的属性均可以在此处定义
			            var json = null; // 后台返回的json
			            try {
			                json = data;
			            } catch(e) {}
			            var Series = []; // 准备存放图表数据
			            var ser = [];
			            
			            var ss = {};
			            if (json != null && json != undefined) {
			                for (var i = 0; i < json.length; i++) {
			                    var item = json[i];
			                    var name = '营收';
			                    var klData = ss['' + name + ''];
			                    if (klData == null) {
			                        klData = [];
			                    }
			                    klData.push(item.REVENUE);
			                    ss['' + name + ''] = klData;
			                    if (!comm.contains(xAxisData, item.YEAR)) {
			                        xAxisData.push(item.YEAR);
			                    }
			                }
			            }
			            if (json != null && json != undefined) {
			                for (var j = 0; j < json.length; j++) {
			                    var item = json[j];
			                    var name = '投入';
			                    var klData = ss['' + name + ''];
			                    if (klData == null) {
			                        klData = [];
			                    }
			                    klData.push(item.MONEY);
			                    ss['' + name + ''] = klData;
			                    if (!comm.contains(xAxisData, item.YEAR)) {
			                        xAxisData.push(item.YEAR);
			                    }
			                }
			            }

			            for (var item in ss) {
			                var it = new Item();
			                it.name = item;
			                it.data = ss[item];
			                Series.push(it);
			                ser.push(it); 
			            }
			             
			            
			            var chartTile = '财政投入和营收';
			            self.myLChart.setOption({
			                title: {
			                    text: chartTile
			                },
			                legend: {
			                    data: legendData
			                },
			                xAxis: {
			                    data: xAxisData
			                },
			                series: Series
			            });
				},
				getData:function(){
					var startTime = $('#begin_date').val();
	                var endTime = $('#end_date').val();
	                if(comm.isEmpty(startTime)||comm.isEmpty(endTime)){
	                	alert("请选择时间年份");
	                	return false;
	                }
					var params = {};
					params.startTime = startTime;
					params.endTime = endTime;
	                comm.requestJson('/report/busrevenue/getIndustryData', JSON.stringify(params), function(resp){
	                	if(resp.chatsData){
	                		self.ndbhEcharts_fold(resp.chatsData);
	                	}
	                	if(resp.pieData){
	                		self.ndbhEcharts_pie(resp.pieData);
	                	}
	            		
	            		
	            	},function(resp){
	            		alert("查询失败...");
	            	});
				},
				ndbhEcharts_pie : function(data){
					var titles = [];
					var Series = []; // 准备存放图表数据
					  var Item = function() {
			                return {
			                    name: '',
			                    value: ''
			                }
			            };

					if(data && data.length > 0){
						for (var i = 0; i < data.length; i++) {
							titles.push(data[i].FINANCIALTYPE_NAME); 
							var it = new Item();
			                it.name = data[i].FINANCIALTYPE_NAME;
			                it.value = data[i].MONEY;
			                Series.push(it);
						}
					}
					option = {
					    title : {
					        text: '公共财政补贴构成比例',
					        subtext: '',
					        x:'center'
					    },
					    tooltip : {
					        trigger: 'item',
					        formatter: "{a} <br/>{b} : {c} ({d}%)"
					    },
					    legend: {
					        orient: 'vertical',
					        left: 'left',
					        data: titles
					    },
					    series : [
					        {
					            name: '访问来源',
					            type: 'pie',
					            radius : '55%',
					            center: ['50%', '60%'],
					            data:Series,
					            itemStyle: {
					                emphasis: {
					                    shadowBlur: 10,
					                    shadowOffsetX: 0,
					                    shadowColor: 'rgba(0, 0, 0, 0.5)'
					                }
					            }
					        }
					    ]
					};
					if (option && typeof option === "object") {
						self.myPChart.setOption(option, true);
					}
				},
			};
			return self;
		});
