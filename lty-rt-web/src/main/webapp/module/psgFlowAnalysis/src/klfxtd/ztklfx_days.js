define(
		[ 'text!psgFlowAnalysis/tpl/klfxtd/ztklfx_days.html' ],
		function(tpl) {
			var 	chartData;
			var self = {
				dateRange1:[],
				dateRange2:[],
				legendData:null,
				xAxisData:null,
				myChart:null,
				data:null,
				isHaveCompareDate:false,
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
					$("#begin_date2").datetimepicker({
						format : 'yyyy-mm-dd',
						language : "zh-CN",
						minView : 'month'
					});
					$("#end_date2").datetimepicker({
						format : 'yyyy-mm-dd',
						language : "zh-CN",
						minView : 'month'
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
					
					
					$("[name='inlineRadioOptions1']").click(function(){
						 var width = $(".col-sm-3").width();
						 var inlineRadioOptions = $('input[name="inlineRadioOptions1"]:checked ').val();
						 if(inlineRadioOptions != '' && inlineRadioOptions == '0'){
							 $("#targetselectDiv").hide();
						 }else if(inlineRadioOptions != '' && inlineRadioOptions == '1'){
							 $(".xlkl_col > span.select2-container").css("width",width);
							 $("#targetselectDiv").show();
							
						 }
					 });
					
					self.data = "";
					$('#table_chart_export').unbind();
					$('#table_chart_export').click(function(){
						self.exportData();
						$("#img").val("");
					    $("#tableData").val("");
					    $("#fileName").val("");
					    $("#titleColumn").val("");
					    $("#titleName").val("");
					    $("#titleSize").val("");
					});
					
					 //确定按钮
					 $('#btn_hours_confirm').click(function(){
						 var psgType = $('input[name="inlineRadioOptions1"]:checked').val();
						 var holidayType = $('input[name="inlineRadioOptions"]:checked').val();
						 var yplatforms = $.trim($('#yztselect option:selected').val());
			             var tplatforms = $.trim($('#mbztselect option:selected').val());
			             
			             //查询日期校验							            
			             var flag = comm.validQueryDate($(
										'#begin_date').val(), $(
										'#end_date').val(), $(
										'#begin_date2 ').val(), $(
										'#end_date2').val());
						 if (!flag) {
							return false;
						 }
			             
			             var begin_date = moment($('#begin_date').val()).format('YYYY-MM-DD');
			             var end_date = moment($('#end_date').val()).format('YYYY-MM-DD');
			             var begin_date2 = moment($('#begin_date2 ').val()).format('YYYY-MM-DD');
			             var end_date2 = moment($('#end_date2').val()).format('YYYY-MM-DD');
			             
						 if(psgType != '' && psgType == '1'){
							 if(yplatforms == '' || yplatforms == null || tplatforms == ''|| tplatforms == null){
								 comm.alert_tip("请选择源站台和目标站台");
								 return false;
							 }
							 if(comm.isNotEmpty(yplatforms)&&comm.isNotEmpty(tplatforms)&&yplatforms.split('=')[0]==tplatforms.split('=')[0]){
								 comm.alert_tip("源站台和目标站台不能为同一站台!");
								 return false;
							 }
						 }
						 self.getDateRange();
						 var params = {};
						 params.psgType = psgType;
						 params.holidayFlag = holidayType;
						 params.beginDate = begin_date;
						 params.endDate = end_date;
						 params.beginDate2 = begin_date2;
						 params.endDate2 = end_date2;
						 
						 var begin_time = $.trim($('#begin_time').val());
						 var end_time = $.trim($('#end_time').val());
						 if(begin_time!="" && begin_time!='Invalid date')	
							 params.beginTime=begin_time;
						 if(end_time!="" && end_time!='Invalid date')	
							 params.endTime=end_time;
						 
						 if(comm.isNotEmpty(begin_date2)&&comm.isNotEmpty(end_date2)){
							 self.isHaveCompareDate = true;
						 }else{
							 self.isHaveCompareDate = false;
						 }
						 
						 params.queryPlatforms = [yplatforms];
						 params.queryTPlatform = tplatforms;
						 
						 comm.request('/report/template/ztfkfxFordays',params,function(resp){
							 //self.updateChart(resp.data);
							 chartData=resp.data;
							 self.initOptionChart(resp.data,'line');
							 if(psgType == '0'){
								 $("#div_list_template_od").hide();
								 $("#div_list_template").show();
								 self.initTable(resp.data);
							 }else if(psgType == '1'){
								 $("#div_list_template").hide();
								 $("#div_list_template_od").show();
								 self.initTableOD(resp.data);
							 }
							 self.data = resp.data;
						 });
					 });
					 
					 //
					 $('#yztselect').change(function(){
						 var selectData = $("#yztselect").select2("data");
						 if(selectData.length>1){
							 $('#singleStation').hide();
							 $('#end_date2').val('');
							 $('#begin_date2').val('');
						 }else{
							 $('#singleStation').show();
						 }
					 });
					 
					 $("#targetselectDiv").hide();
					 $("#div_list_template").show();
					 $("#div_list_template_od").hide();
					 //初始化表格
					 self.initTable(null);
					 self.initChart();
//					 self.judgetime(($('#begin_date').val()),($('#end_date').val()),($('#begin_date2').val()),($('#end_date2').val()));
				},
//				judgetime:function (begin1Time,end1Time ,begin2Time,end2Time) {
//					alert(111);
//					alert($('#begin_date').val());
//					if(begin1Time>begin2Time && begin1Time<end2Time )
//					{alert('请选择不交叉时间段');}
//					if(end1Time >begin2Time && end1Time <end2Time )
//					{alert('请选择不交叉时间段');}
					
					
					
//					if(($('#begin_date').val()) >($('#begin_date2').val()) && ($('#begin_date').val())<($('#end_date2').val()) )
//					{alert('请选择不交叉时间段');}
//					if(($('#end_date').val()) >($('#begin_date2').val()) && ($('#end_date').val()) <($('#end_date2').val()) )
//					{alert('请选择不交叉时间段');}
//					
//				},
				initSelect : function() {
					comm.initPlatSelect('#yztselect');
			        comm.initPlatSelect('#mbztselect');
					/*comm.request('/report/template/listPlatform',null,function(resp){
						comm.initSelectOptionForObj('yztselect',resp.data,'name','id');
						comm.initSelectOptionForObj('mbztselect',resp.data,'name','id');
					});*/
				},
				getDateRange : function() {
					self.dateRange1 = [];
					self.dateRange2 =[];
					var begin_date1 = moment($('#begin_date').val());
					var end_date1 = moment($('#end_date').val()).add(1, 'd');
					var begin_date2 = moment($('#begin_date2').val());
					var end_date2 = moment($('#end_date2').val()).add(1, 'd');
					while(begin_date1.isBefore(end_date1)){
						self.dateRange1.push(begin_date1.format('YYYY-MM-DD'));
						begin_date1 = begin_date1.add(1, 'd');
					}
					while(begin_date2.isBefore(end_date2)){
						self.dateRange2.push(begin_date2.format('YYYY-MM-DD'));
						begin_date2 = begin_date2.add(1, 'd');
					}
				},
				getBelongRange : function(day) {
					if(comm.contains(self.dateRange1,day)){
						return self.legendData[0];
					}
					return self.legendData[1];
				},
				exportData : function(){
					var psgType = $('input[name="inlineRadioOptions1"]:checked').val();
					var titleColumn = [];
					var titleName = [];
					var titleSize = [20, 20, 20, 20];
					var img = self.myChart.getDataURL("png");
					if(psgType == '0'){
						titleColumn = ["P_NAME","DD","ONBUS_PERSON_COUNT","OFFBUS_PERSON_COUNT"];
						titleName = ["站台","时间","上车人数","下车人数"];
					}else if(psgType == '1'){
						titleColumn = ["SOURCE_PLATFORM_NAME","TAGET_PLATFORM_NAME","DD","ONBUS_PERSON_COUNT"];
						titleName = ["源站台","目标站台","时间","站台间客流量"];
					}
					
					$("#img").val(img);
				    $("#tableData").val(JSON.stringify(self.data));
				    $("#fileName").val("站台客流统计分析-按日");
				    $("#titleColumn").val(JSON.stringify(titleColumn));
				    $("#titleName").val(JSON.stringify(titleName));
				    $("#titleSize").val(JSON.stringify(titleSize));
				    $("#export_form").submit();
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
						//新增X轴单位 日 2017年3月15日 
						xAxis : {
							type : 'category',
							boundaryGap: false,
							axisLabel : {
				                formatter: '{value} 日'
				            },
							data : []
						},
						//薪资Y轴坐标 人次 2017年3月15日 
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
				    var begin_date2 = $('#begin_date2').val();
				    var end_date2 = $('#end_date2').val();
					var legendData =['分析日期客流','对比日期客流'];
					var station = $('#yztselect').select2("data")[0].text
					var chartTile = station;
					for(var i=0;i<dataPie.length;i++){
						if(begin_date<=dataPie[i].DD&&end_date>=dataPie[i].DD){
							totleEnd+=parseInt(dataPie[i].TOTAL_PERSON_COUNT);
						}
						else if(begin_date2<=dataPie[i].DD&&end_date2>=dataPie[i].DD){
							totleStart+=parseInt(dataPie[i].TOTAL_PERSON_COUNT);
						}
				}
					//三种值得设置
					if(begin_date&&end_date&&!begin_date2||!end_date2 ){
						var pieValueHours=[{value:totleStart,name:"分析日期客流"}];
						 pieArrList=pieValueHours; 
					}
					else if(begin_date2&&end_date2&&!begin_date||!end_date){
						var pieValueHours=[{value:totleEnd,name:"对比日期客流"}];
						 pieArrList=pieValueHours;
					}
					else if(begin_date&&end_date&&begin_date2&&end_date2){
						   var pieValueDay=[{value:totleStart,name:"分析日期客流"},{value:totleEnd,name:"对比日期客流"}];
						      pieArrList=pieValueDay;
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
					    var begin_date = $('#begin_date').val();
					    var end_date = $('#end_date').val();
					    var begin_date2 = $('#begin_date2').val();
					    var end_date2 = $('#end_date2').val();
						var chartTile = station;//chart 标题
						for(var i=0;i<dataTotile.length;i++){
							if(begin_date<=dataTotile[i].DD&&end_date>=dataTotile[i].DD){
								
								dataStart.push(dataTotile[i].TOTAL_PERSON_COUNT);
							}
							else if(begin_date2<=dataTotile[i].DD&&end_date2>=dataTotile[i].DD){
								dataEnd.push(dataTotile[i].TOTAL_PERSON_COUNT);
							}
							  if(begin_date&&end_date&&begin_date2&&end_date2){
								  xAxisData.push("第"+(i+1));//横坐标赋值
						    	}
							else{
								 xAxisData.push(dataTotile[i].DD);//横坐标赋值
							  }
				         	}
						//三种值得存在 series设置
					      if(begin_date&&end_date&&!begin_date2||!end_date2){
								var legendData =['分析日期客流'];
								var series=[ {name : '分析日期客流',type : type,data : dataStart}];
							}
					      else if(begin_date2&&end_date2&&!begin_date||!end_date){
					    		var legendData =['对比日期客流'];
								var series=[ {name : '对比日期客流',type : type,data : dataEnd}];
					      }
					      else if(begin_date&&end_date&&begin_date2&&end_date2){
					    		var legendData =['分析日期客流','对比日期客流'];
					    		var series=[ {name : '分析日期客流',type : type,data : dataStart}, {name : '对比日期客流',type : type,data : dataEnd}];
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
				
				initTable : function(data) {
					//$('#table_list_template').html('');
					$('#table_list_template').DataTable({
					
						//每页显示三条数据
						//pageLength : 5,
						language:dataTable_cn,
						destroy: true,
						data: data,
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
							data : "DD"
						},{
							//title:'上车人数',
							data : "ONBUS_PERSON_COUNT"
						},{
							//title:'下车人数',
							data : "OFFBUS_PERSON_COUNT"
						}]

					});
				},
				initTableOD : function(data) {
					//$('#table_list_template').html('');
					$('#table_list_template_od').DataTable({
						select: true,
						//每页显示三条数据
						//pageLength : 5,
						destroy: true,
						data: data,
						language:dataTable_cn,
						columns : [{
							title:'源站台',
							data : 'SOURCE_PLATFORM_NAME'
						},{
							title:'目标站台',
							data : "TAGET_PLATFORM_NAME"
						},{
							title:'时间',
							data : "DD"
						},{
							title:'站台间客流量',
							data : "ONBUS_PERSON_COUNT"
						}]

					});
				},
				valid:function(){
					
				}
			};
			return self;
		});