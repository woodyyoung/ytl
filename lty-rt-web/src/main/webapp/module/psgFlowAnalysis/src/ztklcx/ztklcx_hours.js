define(
		[ 'text!psgFlowAnalysis/tpl/ztklcx/ztklcx_hours.html' ],
		function(tpl) {
			var 	chartData;
			var self = {
				myChart:null,
				data:null,
				show : function() {
					$('#ztklfx_form').html(tpl);
					
					//初始化下拉控件
					self.initSelect();
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
						 var platforms = $('#yztselect').val();
						 var begin_date = $('#begin_date').val();
						 //var end_date = $('#end_date').val();
						 
						 var params = {};
						 params.beginDate = begin_date;
						 //params.endDate = end_date;
						 params.queryPlatforms = [platforms];
						 if (comm.isEmpty($('#begin_date').val())) {
								comm.alert_tip('请选择查询日期');
								return false;
							}   
						 comm.request('/report/template/ztfkfx',params,function(resp){
							 self.initTable(resp.data);
							 self.data=resp.data;
							 chartData=resp.data;
							 self.initOptionChart(resp.data,'line');
							 self.initTable(resp.data.stationData);
						 });
					 });
					 
					 //图表导出
					 self.data="";
					 $('#ztklfx_export').unbind();
						$('#ztklfx_export').click(function(){
							self.exportData();
							$("#img").val("");
						    $("#tableData").val("");
						    $("#fileName").val("");
						    $("#titleColumn").val("");
						    $("#titleName").val("");
						    $("#titleSize").val("");
					  });
					 
					 //
					 $('#yztselect').change(function(){
						 var selectData = $("#yztselect").select2("data");
						 /*if(selectData.length>1){
							 $('#end_date').hide();
							 $('#end_date').parent().prev().hide();
							 $('#end_date').val('');
						 }else{
							 $('#end_date').show();
							 $('#end_date').parent().prev().show();
						 }*/
					 });
					
					 //初始化表格
					 self.initTable(null);
					 self.initChart();

				},
				initSelect : function() {
					comm.initPlatSelect('#yztselect');
					/*var params = {};
					params.name="xxxx";
					params.id="id1";
					params.gprsid=66;
					
					comm.request('/report/template/listPlatform',params,function(resp){
						comm.initSelectOptionForObj('yztselect',resp.data,'name','id');
					});*/
					//comm.initSelectOption('yztselect',[3452,4323,34564,5675]);
				},
				initChart: function(){
					$("#ztklfxCharts").width($('#index_padd').width());
					self.myChart = echarts.init(document.getElementById('ztklfxCharts'));

					// 显示标题，图例和空的坐标轴
					self.myChart.setOption({
						title : {
							text : '站台客流量'
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
//				                formatter: '{value}'
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
						var series= {name : '分析日期客流',type : type,data : dataStart,barWidth :20,};
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
//				updateChart: function(data){
//					if(!data||data.length<1){
//						self.initChart();
//						return false;
//					}
//					var legendData = [];
//					var selectData = $("#yztselect").select2("data");
//					var selectLength = selectData.length;
//					//if(selectLength>1){
//					$.each($("#yztselect").select2("data"),function(i,v){
//						legendData.push(v.text);
//					});
//					//}
//					
//					var xAxisData = [];
//
//					
//					var Item = function() {
//						return {
//							name : '',
//							type : 'bar',
//							barWidth : 20,
//							//itemStyle: {normal: {areaStyle: {type: 'default',opacity:isArea}}},
//							// label: {normal: {show: isShowAllData,position: 'top'}},
//							//markLine: {data: [{type: 'average', name: '平均值'}]},
//							data : []
//						}
//					};
//					// series中的每一项为一个item,所有的属性均可以在此处定义
//					var json = data;// 后台返回的json
//					var Series = []; // 准备存放图表数据
//					var ss = {};
//
//					for ( var i = 0; i < json.length; i++) {
//						var item = json[i];
//						var name = '';
//						name = item.P_NAME;
//						/*if(selectLength<=1){
//							name = item.HH;
//						}else{
//							name = item.P_NAME;
//						}*/
//						var klData = ss['' + name + ''];
//						if (klData == null) {
//							klData = [];
//						}
//						klData.push(item.BUS_PERSON_COUNT);
//						ss['' + name + ''] = klData;
//						
//						if(!comm.contains(xAxisData,item.HH)){
//							xAxisData.push(item.HH+"点");
//						}
//						/*if(selectLength<=1&&!comm.contains(legendData,item.HH)){
//							legendData.push(item.HH);
//						}*/
//					}
//
//					for ( var item in ss) {
//						var it = new Item();
//						it.name = item;
//						it.data = ss[item];
//						Series.push(it);
//					}
//					
//					var chartTile = '';
//					/*if(selectLength<=1){
//						chartTile = selectData[0].text;
//					}else{
//						chartTile = $('#begin_date').val();
//					}*/
//					chartTile = $('#begin_date').val();
//					self.myChart.setOption({
//						title : {
//							text : chartTile
//						},
//						legend : {
//							data : legendData
//						},
//						xAxis : {
//							data : xAxisData
//						},
//						series : Series
//					});
//					
//				},
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
							data : 'P_NAME',
							render: function(data, type, row, meta) {
					            return '';
					        }
						}, {
							//title:'路段',
							data : 'P_NAME',
							render: function(data, type, row, meta) {
								return '';
					        }
						},*/{
							//title:'站台',
							data : "P_NAME"
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
				},
				exportData:function(){
					var titleColumn = ["P_NAME","HH","ONBUS_PERSON_COUNT","OFFBUS_PERSON_COUNT"];
					var titleName = ["站台","时间","上车人数","下车人数"];
					var titleSize = [20, 20, 20, 20];
					var img = self.myChart.getDataURL("png");
					
					var json = self.data;
					if(json[0].HH.split(" ").length <= 1){
						for( var i = 0; i < json.length; i++){
							json[i].HH = json[i].DD+" "+json[i].HH;
						}
					}
					
					$("#img").val(img);
				    $("#tableData").val(JSON.stringify(json));
				    $("#fileName").val("站台客流查询-按小时");
				    $("#titleColumn").val(JSON.stringify(titleColumn));
				    $("#titleName").val(JSON.stringify(titleName));
				    $("#titleSize").val(JSON.stringify(titleSize));
				    $("#export_form").submit();
				}
			};
			return self;
		});