define(
		[ 'text!busDecisionAnalysis/tpl/accountingSubsidies/financialSubsidies/czgc/czgc.html' ],
		function(tpl) {
			var self = {
				show : function(title) {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					$("#yztselect").select2({});
					//初始化下拉框
					self.initSelection();
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
//                       $("#ndbhEcharts_pie canvas").css("backgroundImge","none");
		            });
		            
		            
		            //下拉框选择事件
					$('#yztselect').change(function(){
						self.getData();
					});
					
					self.initChart();
					//self.initTable();

				},
				populatedropdown:function(){

				},
				getData:function(){
					var startTime = $('#begin_date').val();
	                var endTime = $('#end_date').val();
	                var costType = $('#yztselect').val();
	                if(comm.isEmpty(startTime)||comm.isEmpty(endTime)){
	                	alert("请选择时间年份");
	                	return false;
	                }
					var params = {};
					params.startTime = startTime;
					params.endTime = endTime;
					if(costType.trim()){
						params.financialtype_type_id = costType;
					}
					
	                comm.requestDefault('/report/financial/getCharts', params, function(resp){
	                	
	                	if(resp.data.line){
	                		self.ndbhEcharts_fold(resp.data.line);
	                	}
	                	if(resp.data.pie){
	                		self.ndbhEcharts_pie(resp.data.pie);
	                	}
	            		
	            		
	            	},function(resp){
	            		alert("查询失败...");
	            	});

	            	self.loadTableData();
				},
				loadTableData:function(){
					var startTime = $('#begin_date').val();
	                var endTime = $('#end_date').val();	                
	                var typeid = $('#yztselect').val().trim();
	                
	                var params =  {
	                	startTime:startTime,
	                	endTime:endTime,	                	
	                };
                    if(typeid){
						params.financialtype_type_id = typeid;
					}
	                comm.requestDefault('/report/financial/getFinancials',params, function(resp) {
	            		self.initTable(resp.data.financialData);
	            	},function(resp){
	            		self.initTable([]);
	            	});
				},
				initChart: function(){
					self.myLChart = echarts.init(document.getElementById('ndbhEcharts_fold'));
					self.myPChart = echarts.init(document.getElementById('ndbhEcharts_pie'));
					self.myLChart.setOption({
						title : {
							text : '折线图'
						},
						tooltip : {
							trigger : 'axis'
						},
						legend : {
							data : ['','',]
						},
		          
						dataZoom : [ { // 这个dataZoom组件，默认控制x轴。
							type : 'slider', // 这个 dataZoom 组件是 slider 型
							// dataZoom 组件
							start : 10, // 左边在 10% 的位置。
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
							left : '3%',
							right : '10%',
							bottom : '3%',
							top: '23%',
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
							data : ['','',]
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
					if(!data.DATA||data.DATA.length<1){
						return false;
					}
					self.myLChart.on('click', function (params) {
		                if(params.componentType=="series"){
		                	var xName = params.seriesName;
		                	var Name = params.name;
		                    var params = {};
			                var costType = $.trim($('#yztselect').val());
							var params = {};
							params.startTime = Name;
							params.endTime = Name;
							if(costType.trim()){
								params.financialtype_type_id = costType;
							}
							
			                comm.requestDefault('/report/financial/getCharts', params, function(resp){
			                	if(resp.data.pie){
			                		self.ndbhEcharts_pie(resp.data.pie);
			                	}
			            	},function(resp){
			            		alert("查询失败...");
			            	});
		                
		                }
		            });
					
			            var xAxisData = data.YEARS;
			            var Series=data.DATA;
			            // 显示标题，图例和空的坐标轴
		               var legendData = data.DATA.map(function(d){
		               	  return d.name;
		               });
		               
//			           var item = data.DATA.map(function(d){
//		                  return d.name;
//		               });
		               for(i=0;i<3;i++){
		            	    var item ="";
				            legendData.unshift(item);
	                        console.log(legendData);
		               }
			           
			            var chartTile = '折线图   ';
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
					var titles = data.TYPES;
					var Series = data.DATA ;
					                           
					var option = {
					    title : {
					        text: '全市公交行业补贴构成比例',
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
						order: [[ 1, "YEAR" ]],
						language:dataTable_cn,
						columns : [
						 /*{
		                  data: null,
		                  defaultContent: '',
		                  className: 'select-checkbox',
		                  orderable: false
		                },*/
		                {
							title:'年份',
							data : 'YEAR'
						},
						{
							title:'补贴费用',
							data : 'MONEY'
						}, 
						{
							title:'补贴来源',
							data : 'FINANCIALTYPE_SOURCE_NAME'
						},
						{
							title:'补贴种类',
							data : 'FINANCIALTYPE_TYPE_NAME'
						},
						{
							title:'补贴对象',
							data : 'FINANCIALTYPE_OBJECT_NAME'
						}					
						
						],
		                /*select: {
		                    style:    'os',
		                    selector: 'td:first-child'
		                }*/
	
					});
				},
				initSelection:function(){
	                var params = {
							"financialtype_object":"1"
					};
	                comm.requestDefault('/report/financialtype/getFinancialTypes', params, function(resp){
		                var defaultOption = '<option value=" ">全部</option>';	                    
	                    comm.initSelectOptionForObj('yztselect', resp.data.financialTypeData, 'FINANCIALTYPE_NAME', 'FINANCIALTYPE_ID',defaultOption);
	            	},function(resp){
	            		alert("查询失败...");
	            	});
				},
			};
		return self;
	});
