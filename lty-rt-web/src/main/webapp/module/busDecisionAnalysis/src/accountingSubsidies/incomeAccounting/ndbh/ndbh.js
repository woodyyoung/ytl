define(
		[ 'text!busDecisionAnalysis/tpl/accountingSubsidies/incomeAccounting/ndbh/ndbh.html' ],
		function(tpl) {
			var self = {
				mytable:null,
				myLChart:null,
				myPChart:null,
				show : function(title) {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					$("#yztselect").select2({});
					//初始化下拉框
					self.initSelection();
					//初始化折线图
					//self.ndbhEcharts_fold();
					//初始化饼图
					//self.ndbhEcharts_pie();
					//初始化表格
					//self.initTable();
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
		            $('#btn_confirm').click(function() {
		            	self.getData();
                        $("#ndbhEcharts_pie canvas").css("backgroundImge","none");
		            });
		            
		            
		            //下拉框选择事件
					$('#costOfEntry_style').change(function(){
						self.getData();
					});
					
					self.initChart();
					self.initTable();
		            
		            
				},
				getData:function(){
					
					var startTime = $('#begin_date').val();
	                var endTime = $('#end_date').val();
	                var costType = $.trim($('#costOfEntry_style option:selected').val());
	                if(comm.isEmpty(startTime)||comm.isEmpty(endTime)){
	                	comm.alert_tip("请选择时间年份");
	                	return false;
	                }
					var params = {};
					params.startTime = startTime;
					params.endTime = endTime;
					params.costType = costType;
	                comm.requestJson('/report/industryCost/getIndustryData', JSON.stringify(params), function(resp){
	                	if(resp.tableData){
	                		self.initTable(resp.tableData);
	                	}
	                	if(resp.chatsData){
	                		self.ndbhEcharts_fold(resp.chatsData);
	                	}
	                	if(resp.pieData){
	                		self.ndbhEcharts_pie(resp.pieData);
	                	}
	            		
	            		
	            	},function(resp){
	            		comm.alert_tip("查询失败...");
	            	});
				},
				initChart: function(){
					self.myLChart = echarts.init(document.getElementById('ndbhEcharts_fold'));
					self.myPChart = echarts.init(document.getElementById('ndbhEcharts_pie'));
					self.myLChart.setOption({
						title : {
							text : '全市公交行业成本构成年度对比'
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
							start :10, // 左边在 10% 的位置。
							end : 90,
							top:'87%',
						// 右边在 60% 的位置。
						}, { // 这个dataZoom组件，也控制x轴。
							type : 'inside', // 这个 dataZoom 组件是 inside 型
							// dataZoom 组件
							start : 10, // 左边在 10% 的位置。
							end : 90,
							top:'87%',
						// 右边在 60% 的位置。
						} ],
						grid : {
							left : '0%',
							right : '10%',
							top: '23%',
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
							text : '  全市公交行业成本构成年度比例'
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
					self.initChart();
					if(!data||data.length<1){
						return false;
					}
					self.myLChart.on('click', function (params) {
		                if(params.componentType=="series"){
		                	var xName = params.seriesName;
		                	var Name = params.name;
		                    var params = {};
			                var costType = $.trim($('#costOfEntry_style option:selected').val());
							var params = {};
							params.occurtime = Name;
							params.costType = costType;
			                comm.requestJson('/report/industryCost/getIndustryPieData', JSON.stringify(params), function(resp){
			                	if(resp.pieData){
			                		self.ndbhEcharts_pie(resp.pieData);
			                	}
			            	},function(resp){
			            		comm.alert_tip("查询失败...");
			            	});
		                
		                }
		            });
					 var legendData = ['','','',];
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
			            var ss = {};
			            if (json != null && json != undefined) {
			                for (var i = 0; i < json.length; i++) {
			                    var item = json[i];
			                    var name = '';
			                    name = item.TYPE_NAME;
			                    var klData = ss['' + name + ''];
			                    if (klData == null) {
			                        klData = [];
			                    }
			                    klData.push(item.COST);
			                    ss['' + name + ''] = klData;
			                    if (!comm.contains(xAxisData, item.OCCURTIME)) {
			                        xAxisData.push(item.OCCURTIME);
			                    }
			                    if (!comm.contains(legendData, item.TYPE_NAME)) {
			                        legendData.push(item.TYPE_NAME);
			                    }
			                }
			            }
			            var str ="";
			            legendData.push(str);
			            for (var item in ss) {
			                var it = new Item();
			                it.name = item;
			                it.data = ss[item];
			                Series.push(it);
			            }
			             
			            
			            var chartTile = '全市公交行业成本构成对比';
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
							titles.push(data[i].TYPE_NAME);
							var it = new Item();
			                it.name = data[i].TYPE_NAME;
			                it.value = data[i].COST;
			                Series.push(it);
						}
					}
					option = {
					    title : {
					        text: '全市公交行业成本构成比例',
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
					            center: ['60%', '60%'],
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
				//初始化表达
				initTable : function(data) {
					self.mytable = $('#table_list_template').DataTable({
						destroy: true,
						data: data,
						order: [[ 1, "OCCURTIME" ]],
						language:dataTable_cn,
						columns : [
						/* {
		                  data: null,
		                  defaultContent: '',
		                  className: 'select-checkbox',
		                  orderable: false
		                },*/
		                {
							title:'发生年份',
							data : 'OCCURTIME'
						},
						{
							title:'费用类型',
							data : 'TYPE_NAME',
							render: function(data, type, row, meta) {
					            if(!data){
					            	data="";
					            }
					            return data;
					       }
						}, 
						{
							title:'金额',
							data : 'COST'
						}
						
						],
		                select: {
		                    style:    'os',
		                    selector: 'td:first-child'
		                }
	
					});
				},
				initSelection:function(){
	                var params = {};
	                comm.requestJson('/report/industryCostType/list', JSON.stringify(params), function(resp){
		                var defaultOption = '<option value=" ">全部</option>';
	                    comm.initSelectOptionForObj('costOfEntry_style', resp, 'TYPE_NAME', 'ID',defaultOption);
	            	},function(resp){
	            		comm.alert_tip("查询失败...");
	            	});
				},
			};
			return self;
		});
