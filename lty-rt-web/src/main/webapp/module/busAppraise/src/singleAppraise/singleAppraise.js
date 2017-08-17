define(
		[ 'text!psgFlowAnalysis/tpl/gjxwpj/gtxwpj.html' ],
		function(tpl) {
			//Do setup work here
			var self = {
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);

					self.initSelect();

					var myChart = echarts.init(document
							.getElementById('ztklfxCharts'));

					// 显示标题，图例和空的坐标轴
					myChart.setOption({
						title : {
							text : ''
						},
						tooltip: {
						},
						textStyle: {
					        color: 'black'
					    },
						legend: {
					        data: ['预算分配', '实际开销']
					    },
					    radar: {
					        // shape: 'circle',
					        indicator: [
					           { name: '线网密度', max: 4000},
					           { name: '平均换乘次数', max: 5000},
					           { name: '线网重复次数', max: 5000},
					           { name: '站点覆盖率', max: 12000},
					           { name: '平均线路长度', max: 22000},
					           { name: '平均非直线系数', max: 62000},
					           { name: '平均客流不均衡系数', max: 52000},
					           { name: '平均站点负荷度', max: 38500},
					           { name: '平均满载率', max: 42000},
					           { name: '平均乘车距离时间', max:8000},
					           { name: '平均站距', max: 9000},
					           { name: '公交线网长度', max: 5999}
					        ]
					    },
					    series: [{
					        name: '预算 vs 开销（Budget vs spending）',
					        type: 'radar',
					        // areaStyle: {normal: {}},
					        data : [
					            {
					                value : [3500, 4000,4200, 7500, 16000, 55000,48800,9900,11200,1450,3560,5000],
					                name : '预算分配'
					            },
					             {
					                value : [3700, 4300, 3800, 6100, 14000, 60000,41262,28000,6364,6565,6512,5200],
					                name : '实际开销'
					            }
					        ]
					    }]
					});

					var mydChart = echarts.init(document
							.getElementById('dCharts'));
					var mydOption = {
						    tooltip : {
						        trigger: 'axis',
						        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
						            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
						        }
						    },
						    toolbox: {
						        feature: {
						            saveAsImage: {show: true}
						        }
						    },
						    legend: {
						        data:['目标值','实际值','-目标值-','-折线实际值-']
						    },
						    grid: {
						        left: '3%',
						        right: '4%',
						        bottom: '15%',
						        containLabel: true
						    },
						    xAxis : [
						        {
						            type : 'category',
						            data : ['线网密度','平均换乘次数','线网重复次数','站点覆盖率','平均线路长度','平均非直线系数','平均客流不均衡系数','平均站点负荷度','平均满载率','平均乘车距离时间','平均站距','公交线网长度'],
							        axisLabel:{ 
				                    	 interval : 0,
				                    	 rotate:45
				                     }
						        }
						    ],
						    yAxis : [
						        {
						            type : 'value'
						        }
						    ],
						    series : [
						        {
						            name:'目标值',
						            type:'bar',
						            data:[520,215,456,461,289,414,675,266,486,196,216,406]
						        },
						        {
						            name:'实际值',
						            type:'bar',
						            data:[620,115,256,561,589,514,875,236,546,156,256,456]
						        },
						        {
						            name:'-折线实际值-',
						            type:'line',
						            data:[620,115,256,561,589,514,875,236,546,156,256,456]
						        },
						        {
						            name:'-目标值-',
						            type:'line',
						            data:[520,215,456,461,289,414,675,266,486,196,216,406]
						        }
						    ]
						};
					mydChart.setOption(mydOption);
					
					
					
					/*$.ajax({
						type : "POST",
						url : "/report/myTest/list",
						dataType : 'json',
						data : {},
						success : function(data) {
							 myChart.setOption({
					            xAxis: {
					                data: data.data.categories
					            },
					            series: [{
					                name: '销量',
					                data: data.data.data
					            }]
					        });
						},
						error : function(msg) {
							alert('失败');
						}
					});*/

				},

				initSelect : function() {
					$("#yztselect").select2();
					
				/*	$("#yztselect")
							.select2(
									{
										ajax : {
											url : "/report/template/galleryChart",
											dataType : 'json',
											type : 'POST',
											delay : 250,
											data : function(params) {
												var param = new Object();
												return {};
											},
											processResults : function(resp,
													page) {
												var array = new Array();
												if (resp.data) {
													for ( var i = 0; i < resp.data.data.length; i++) {
														var product = resp.data.data[i];
														var productName = resp.data.categories[i];
														array
																.push({
																	id : product,
																	text : product.productName
																});
													}
												}
												var ret = new Object();
												ret.results = array;
												return ret;
											},
											cache : true
										},
										minimumInputLength : 2,
										language : "zh-CN",
									});*/
				}

			};
			return self;
		});