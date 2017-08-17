define(
		[ 'text!psgFlowAnalysis/tpl/xlklcx/xlklcx_hours.html' ],
		function(tpl) {
			var 	chartData;
			var self = {
				myChart:null,
				data:null,
				show : function() {
					$('#xlklcx_form').html(tpl);
					
					//初始化线路下拉控件
					self.initLine();
					//初始化日历控件
					 $("#begin_date").datetimepicker({
						format : 'yyyy-mm-dd',
						language : "zh-CN",
						minView : 'month'
					});
					$("#end_date").datetimepicker({
						format : 'yyyy-mm-dd',
						language : "zh-CN",
						minView : 'month'
					});
					
					 //确定按钮
					 $('#btn_hours_confirm').click(function(){
						 var stationId = $.trim($('#yztselect').val());
						 var lineId = $('#xlselect').val();
						 var begin_date = $('#begin_date').val();
						 var end_date = $('#end_date').val();
						 
						 var params = {};
						 params.beginDate = begin_date;
						 params.endDate = end_date;
						 params.stationId = stationId;
						 params.lineId = lineId;
						 params.inlineRadioOptions = 'option1';
						 if (comm.isEmpty($('#begin_date').val())) {
								comm.alert_tip('请选择查询日期');
								return false;
							}   
						 comm.request('/report/lineAnalysis/xlfkfxForHours',params,function(resp){
//							 self.updateChart(resp.data.lineData);
							 self.initTable(resp.data.stationData);
							 self.data=resp.data.stationData;//
//							 self.initTable(resp.data);
//							 self.data=resp.data;
							 chartData=resp.data.lineData;
							 self.initOptionChart(resp.data.lineData,'line');
//							 self.initTable(resp.data.stationData);
						 });
					 });
					//图表导出
					 self.data="";
					 $('#xlklfx_export').unbind();
						$('#xlklfx_export').click(function(){
							self.exportData();
							$("#img").val("");
						    $("#tableData").val("");
						    $("#fileName").val("");
						    $("#titleColumn").val("");
						    $("#titleName").val("");
						    $("#titleSize").val("");
					  });
					 //
					 $('#xlselect').change(function(){
						var lineId =  $('#xlselect').val();
						self.initStation(lineId);
					 });
					
					 //初始化表格
					 self.initTable(null);
					 self.initChart();
				},
				exportData:function(){
					var titleColumn = ["LINE_NAME","STATION_NAME","HH","ONBUS_PERSON_COUNT","OFFBUS_PERSON_COUNT"];
					var titleName = ["线路","站点","时间","上车人数","下车人数"];
					var titleSize = [20, 40, 20, 20, 20];
					var img = self.myChart.getDataURL("png");
					var json = self.data;
					if(json[0].HH.split(" ").length <= 1){
						for( var i = 0; i < json.length; i++){
							json[i].HH = json[i].DD+" "+json[i].HH;
						}
					}
					$("#img").val(img);
				    $("#tableData").val(JSON.stringify(json));
				    $("#fileName").val("线路客流查询-按小时");
				    $("#titleColumn").val(JSON.stringify(titleColumn));
				    $("#titleName").val(JSON.stringify(titleName));
				    $("#titleSize").val(JSON.stringify(titleSize));
				    $("#export_form").submit();
				},
				initLine : function() {
					var params = {};
					comm.request('/report/lineAnalysis/listAllLine',params,function(resp){
						comm.initSelectOptionForObj('xlselect',resp.data,'name','id');
						self.initStation(resp.data[0].id);
					});
				},
				initStation:function(lineId){
					var params = {lineId:lineId};
					comm.request('/report/lineAnalysis/queryStation',params,function(resp){
						var defaultOption = '<option value=" ">全部</option>';
						comm.initSelectOptionForObj('yztselect',resp.data,'name','id',defaultOption);
					});
				},
				initChart: function(){
					$("#ztklfxCharts").width($('#index_padd').width());
					self.myChart = echarts.init(document.getElementById('ztklfxCharts'));

					// 显示标题，图例和空的坐标轴
					self.myChart.setOption({
						title : {
							text : '线路客流量'
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
//						            restore: {},
						            saveAsImage: {}
					        }
					        
						},
						xAxis : {
							type : 'category',
							data : [],
							axisLabel : {
//				                formatter: '{value} 点'
								formatter:function(value){
									if(value!=undefined){
										return value.slice(0,3)
									}
									}
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
//				updateChart: function(data){
//					if(!data||data.length<1){
//						self.initChart();
//						return false;
//					}
//					
//					
//					var xAxisData = [];
//
//					// series中的每一项为一个item,所有的属性均可以在此处定义
//					var json = data;// 后台返回的json
//					var seriesData = []; // 准备存放图表数据
//
//					for ( var i = 0; i < json.length; i++) {
//						var item = json[i];
//						seriesData.push(item.BUS_PERSON_COUNT);
//						if(!comm.contains(xAxisData,item.HH)){
//							xAxisData.push(item.HH+"点");
//						}
//					}
//
//					var line = $('#xlselect').select2("data")[0].text
//					var station = $('#yztselect').select2("data")[0].text
//					var chartTile = line+'/'+station;
//					
//					self.myChart.setOption({
//						title : {
//							text : chartTile
//						},
//						legend : {
//							data : []
//						},
//						xAxis : {
//							data : xAxisData
//						},
//						series :  {
//							name : '',
//							type : 'bar',
//							barWidth : 20,
//							//itemStyle: {normal: {areaStyle: {type: 'default',opacity:isArea}}},
//							// label: {normal: {show: isShowAllData,position: 'top'}},
//							//markLine: {data: [{type: 'average', name: '平均值'}]},
//							data : seriesData
//						}
//					});
//					
//				},
				//饼状图初始化
				initPieChart:function(){
				$("#ztklfxCharts").width($('#index_padd').width());
				self.myChart = echarts.init(document.getElementById('ztklfxCharts'));
				var dataPie=chartData;
				console.log(dataPie);
				var pieArrList=[],legendData = [],totleEnd=0, totleStart=0;
				var begin_date = $('#begin_date').val();
				var end_date = $('#end_date').val();
				var legendData =['分析日期客流'];
				var line = $('#yztselect').select2("data")[0].text
				var station = $('#yztselect').select2("data")[0].text
				var chartTile = line;
				for(var i=0;i<dataPie.length;i++){
						if(dataPie[i].DD==end_date){
							totleEnd+=parseInt(dataPie[i].TOTAL_PERSON_COUNT);
						}
						else{
							totleStart+=parseInt(dataPie[i].TOTAL_PERSON_COUNT);
						}
				}
				//三种值得设置
				  if(begin_date){
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
//				            restore: {},
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
					if(!dataTotile||dataTotile.length<1){
						self.initChart();
						return false;
					}
					self.initChart();
					var xAxisData = [];//横坐标数组
					var dataStart=[],dataEnd=[];
					var line = $('#yztselect').select2("data")[0].text;
					var station = $('#yztselect').select2("data")[0].text;
					var begin_date = $('#begin_date').val();
					var end_date = $('#end_date').val();
					var chartTile = line;//chart 标题
					var legendData =['分析日期客流','对比日期客流'];
					//三种值得存在 series设置
					if(begin_date){
						var legendData =['分析日期客流'];
						var series= {name : '分析日期客流',type : type,data : dataStart,barWidth :15,};
					}
					for(var i=0;i<dataTotile.length;i++){
						if(dataTotile[i].DD==begin_date){
							dataStart.push(dataTotile[i].TOTAL_PERSON_COUNT);
						}
						else{
							dataEnd.push(dataTotile[i].TOTAL_PERSON_COUNT);
						}
						  xAxisData.push(dataTotile[i].HH+ "点--"+(parseInt(dataTotile[i].HH)+1)+"点");//横坐标赋值
			         	}
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
				initTable : function(data) {
					//$('#table_list_template').html('');
					$('#table_list_template').DataTable({
						select: true,
						//每页显示三条数据
						//pageLength : 5,
						destroy: true,
						data: data,
						language:dataTable_cn,
						columns : [/*{
							//title:'小区',
							data : 'LINE_NAME',
							render: function(data, type, row, meta) {
					            return '';
					        }
						}, */{
							title:'线路',
							data : 'LINE_NAME'
							
						},{
							title:'站点',
							data : "STATION_NAME"
						},{
							//title:'时间',
							data : "HH",
							render: function(data, type, row, meta) {
						            if(row.DD!=null&&row.DD!=''){
						            	return row.DD+" "+data;
						            }
						            return data;
						       }
						},{
							//title:'上车人数',
							data : "ONBUS_PERSON_COUNT"
						},{
							//title:'下车人数',
							data : "OFFBUS_PERSON_COUNT"
						}]

					});
				}
			};
			return self;
		});