define(
		[ 'text!psgFlowAnalysis/tpl/dmklcx/dmklcx_months.html' ],
		function(tpl) {
			var 	chartData;
			var self = {
				dateRange1 : [],
				dateRange2 : [],
				legendData : null,
				xAxisData : null,
				myChart:null,
				data:null,
				show : function() {
					$('#dmklcx_form').html(tpl);

					// 初始化下拉控件
					self.initArea();
					// 初始化日历控件
					$("#begin_date").datetimepicker({
						format : 'yyyy-mm',
						language : "zh-CN",
						minView : 'year',
						startView : 'year'
					});
					$("#end_date").datetimepicker({
						format : 'yyyy-mm',
						language : "zh-CN",
						minView : 'year',
						startView : 'year'
					});
					$("#begin_time").datetimepicker({
						format : "hh:00:00",
						language : "zh-CN",
						startView : "day",
						minView : 'day'
					});
					$("#end_time").datetimepicker({
						format : "hh:59:59",
						language : "zh-CN",
						startView : "day",
						minView : 'day'
					});

				

					// 确定按钮
					$('#btn_hours_confirm').click(
							function() {
								var holidayType = $('input[name="inlineRadioOptions"]:checked').val();
								var areaCode = $.trim($('#yztselect').val());
								 
								//查询日期校验							            
					            var flag = comm.validQueryDate($(
												'#begin_date').val(), $(
												'#end_date').val());
								if (!flag) {
									return false;
								}
								
								var begin_date = moment($('#begin_date').val())
										.startOf('month').format('YYYY-MM-DD');
								var end_date = moment($('#end_date').val())
										.endOf('month').format('YYYY-MM-DD');


								var params = {};
								params.beginDate = begin_date;
								params.endDate = end_date;
								params.areaCode = areaCode;
								params.holidayFlag = holidayType;
								
								var begin_time = $.trim($('#begin_time').val());
								var end_time = $.trim($('#end_time').val());
								if(begin_time!="" && begin_time!='Invalid date')	
									params.beginTime=begin_time;
								if(end_time!="" && end_time!='Invalid date')	
									params.endTime=end_time;

								comm.request('/report/stretchAnalysis/xlfkfxForMonths',params,function(resp){
//									 self.updateChart(resp.data.stretchData);
//									 self.initTable(resp.data.stationData);
//									 self.data=resp.data.stationData;//
									self.initTable(resp.data);
									 self.data=resp.data.stationData;
									 chartData=resp.data.stretchData;
									 self.initOptionChart(resp.data.stretchData,'line');
									 self.initTable(resp.data.stationData);
								});
					});
					
					//图表导出
					 self.data="";
					 $('#dmklfx_export').unbind();
						$('#dmklfx_export').click(function(){
							self.exportData();
							$("#img").val("");
						    $("#tableData").val("");
						    $("#fileName").val("");
						    $("#titleColumn").val("");
						    $("#titleName").val("");
						    $("#titleSize").val("");
					  });

					// 初始化表格
					self.initTable(null);
					self.initChart();
				},
				exportData:function(){
					var titleColumn = ["STRETCH_NAME","AREA_NAME","P_NAME","MM","ONBUS_PERSON_COUNT","OFFBUS_PERSON_COUNT"];
					var titleName = ["路段","小区","站台","时间","上车人数","下车人数"];
					var titleSize = [20, 20, 40, 20, 20, 20];
					var img = self.myChart.getDataURL("png");
					var json = self.data;
					
					$("#img").val(img);
				    $("#tableData").val(JSON.stringify(json));
				    $("#fileName").val("断面客流查询-按月");
				    $("#titleColumn").val(JSON.stringify(titleColumn));
				    $("#titleName").val(JSON.stringify(titleName));
				    $("#titleSize").val(JSON.stringify(titleSize));
				    $("#export_form").submit();
				},
				initArea : function() {
					var params = {};
					comm.request('/report/stretchAnalysis/listAllStretch',params,function(resp){
						comm.initSelectOptionForObj('yztselect',resp.data,'linename','lineid');
					});
				},
				initChart: function(){
					$("#ztklfxCharts").width($('#index_padd').width());
					self.myChart = echarts.init(document.getElementById('ztklfxCharts'));

					// 显示标题，图例和空的坐标轴
					self.myChart.setOption({
						title : {
							text : '断面客流量'
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
						toolbox: {
					        show: true,
					        x:'1200', 
					        feature: {
//					        	magicType: {type: ['line', 'bar','stack', 'tiled']},
//					            restore: {},
//					            saveAsImage: {}
//					        }
					        	myLine: {
					                show: true,
					                title: '动态类型切换-折线图',
					                icon: 'image://images/line.png?v='+Math.random()+'',
					                onclick: function (){
					                	self.initOptionChart(chartData,"line");
					                }
					            },
					          
					            myBar: {
					                show: true,
					                title: '动态类型切换-柱形图',
					                icon: 'image://images/bar.png?v='+Math.random()+'',
					                onclick: function (){
					                	self.initOptionChart(chartData,"bar");
					                }
					            },
						        myPie: {
					                show: true,
					                title: '动态类型切换-饼状图',
					                icon: 'image://images/pie.png?v='+Math.random()+'',
					                onclick: function (){
					                	self.initPieChart();
					                }
					            },
//					            restore: {},
					            saveAsImage: {}
					       
				        }
						},
						xAxis : {
							type : 'category',
							boundaryGap : false,
							data : [],
							axisLabel : {
				                formatter: '{value}'
				            }
						},
						yAxis : {
							type : 'value',
							axisLabel : {
				                formatter: '{value} 人次'
				            }
						},
						series : []
					});
				},
				//饼状图初始化
				initPieChart:function(){
					$("#ztklfxCharts").width($('#index_padd').width());
					self.myChart = echarts.init(document.getElementById('ztklfxCharts'));
					var dataPie=chartData;
					var pieArrList=[],legendData = [],totleEnd=0, totleStart=0;
					var begin_date = moment($('#begin_date').val()).format('YYYY-MM');
					var end_date = moment($('#end_date').val()).format('YYYY-MM');
					var begin_date2 = moment($('#begin_date2').val()).format('YYYY-MM');
					var end_date2 = moment($('#end_date2').val()).format('YYYY-MM');
					var legendData =['分析日期客流','对比日期客流'];
					var station = $('#yztselect').select2("data")[0].text
					var chartTile =station;
					for(var i=0;i<dataPie.length;i++){
							if(begin_date<=dataPie[i].MM&&end_date>=dataPie[i].MM){
								totleStart+=parseInt(dataPie[i].TOTAL_PERSON_COUNT);
							}
							else if(begin_date2<=dataPie[i].MM&&end_date2>=dataPie[i].MM){
								totleEnd+=parseInt(dataPie[i].TOTAL_PERSON_COUNT);
							}
					}
					//三种值得设置
					if(begin_date&&end_date ){
						var pieValueHours=[{value:totleStart,name:"分析日期客流"}];
						 pieArrList=pieValueHours; 
					}
					
					self.myChart.setOption({
						title : {
							text :chartTile
						},
						  tooltip : {
							    trigger: 'item',
						        formatter: "{a} <br/>{b} : {c} ({d}%)"
						    },
						    legend: {
						        x : 'center',
						       data:legendData,
						    },
						toolbox: {
							name:legendData,
					        show: true,
					        x:'1200', 
					        feature: {
						        myLine: {
					                show: true,
					                title: '动态类型切换-折线图',
					                stack:true,
					                icon: 'image://images/line.png?v='+Math.random()+'',
					                onclick: function (){
					                	self.initOptionChart(chartData,"line");
					                }
					            },
					            myBar: {
					                show: true,
					                title: '动态类型切换-柱形图',
					                icon: 'image://images/bar.png?v='+Math.random()+'',
					                onclick: function (){
					                	self.initOptionChart(chartData,"bar");
					                }
					            },
						        myPie: {
					                show: true,
					                title: '动态类型切换-饼状图',
					                icon: 'image://images/pie.png?v='+Math.random()+'',
					                onclick: function (){
					                	self.initPieChart();
					                }
					            },
//					            restore: {},
					            saveAsImage: {}
					        }
						},
					    series : [
					        {
					            type: 'pie',
					            radius : '55%',
					            center: ['50%', '50%'],
					            data:pieArrList,
					            itemStyle: {
					                emphasis: {
					                    shadowBlur: 10,
					                    shadowOffsetX: 0,
					                    shadowColor: 'rgba(0, 0, 0, 0.5)'
					                }
					            }
					        }
					    ]
					})
				},
				//bar 和line 的方法
				initOptionChart:function(data,type){
						var dataTotile=data;//传递数据 
						console.log(dataTotile);
						if(!dataTotile||dataTotile.length<1){
							self.initChart();
							return false;
						}
						self.initChart();
						var xAxisData = [];//横坐标数组
						var dataStart=[],dataEnd=[];
						var station = $('#yztselect').select2("data")[0].text;
						var begin_date = moment($('#begin_date').val()).format('YYYY-MM');
						var end_date = moment($('#end_date').val()).format('YYYY-MM');
						var begin_date2 = moment($('#begin_date2').val()).format('YYYY-MM');
						var end_date2 = moment($('#end_date2').val()).format('YYYY-MM');
						var chartTile = station;//chart 标题
						for(var i=0;i<dataTotile.length;i++){
							if(begin_date<=dataTotile[i].MM&&end_date>=dataTotile[i].MM){
								dataStart.push(dataTotile[i].TOTAL_PERSON_COUNT);
							}
							else if(begin_date2<=dataTotile[i].MM&&end_date2>=dataTotile[i].MM){
								dataEnd.push(dataTotile[i].TOTAL_PERSON_COUNT);
							}
							  xAxisData.push(dataTotile[i].MM+"月");//横坐标赋值
				         	}
						//三种值得存在 series设置
					      if(begin_date&&end_date){
								var legendData =['分析日期客流'];
								var series=[ {name : '分析日期客流',type : type,data : dataStart,barWidth : 20,}];
							}
					      
						  var series
							self.myChart.setOption({
								title : {
									text : chartTile
								},
								legend : {
									data : legendData
								},
								xAxis : {
									data : xAxisData
								},
								series :series
							});
					},
//				updateChart: function(data){
//					if(!data||data.length<1){
//						self.initChart();
//						return false;
//					}
//					self.legendData = [];
//					self.xAxisData = [];
//
//					// series中的每一项为一个item,所有的属性均可以在此处定义
//					var json = data;// 后台返回的json
//					var Series = []; // 准备存放图表数据
//
//					for ( var i = 0; i < json.length; i++) {
//						var item = json[i];
//						if(!comm.contains(self.xAxisData,item.MM)){
//							self.xAxisData.push(item.MM);
//						}
//						Series.push(item.BUS_PERSON_COUNT);
//					}
//
//					var area = $('#yztselect').select2("data")[0].text;
//					var chartTile = area;
//
//					self.myChart.setOption({
//						title : {
//							text : chartTile
//						},
//						legend : {
//							data : self.legendData
//						},
//						xAxis : {
//							data : self.xAxisData
//						},
//						series :  {
//							name : '',
//							type : 'bar',
//							barWidth:20,
//							// itemStyle: {normal: {areaStyle: {type:
//							// 'default',opacity:isArea}}},
//							// label: {normal: {show: isShowAllData,position:
//							// 'top'}},
//							// markLine: {data: [{type: 'average', name:
//							// '平均值'}]},
//							data : Series
//						}
//					});
//
//				},
				initTable : function(data) {
					//$('#table_list_template').html('');
					$('#table_list_template').DataTable({
						//pageLength : 5,
						language : dataTable_cn,
						destroy : true,
						data : data,
						columns : [ {
							title:'路段',
							data : 'STRETCH_NAME'
						}, {
							title:'小区',
							data : 'AREA_NAME'
						}, {
							title:'站台',
							data : "P_NAME"
						}, {
							//title:'时间',
							data : "MM"
						}, {
							//title:'上车人数',
							data : "ONBUS_PERSON_COUNT"
						}, {
							//title:'下车人数',
							data : "OFFBUS_PERSON_COUNT"
						} ]

					});
				}
			};
			return self;

		});