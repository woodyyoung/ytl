define(
		[ 'text!psgFlowAnalysis/tpl/xlklfx/xlklfx_hours.html' ],
		function(tpl) {
		var 	chartData;
			var self = {
				myChart:null,
				data:null,
				show : function() {
					$('#xlklfx_form').html(tpl);
					
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
					
					$("[name='inlineRadioOptions1']").click(function(){
						 var width = $(".col-sm-3").width();
						 var inlineRadioOptions = $('input[name="inlineRadioOptions1"]:checked ').val();
						 if(inlineRadioOptions != '' && inlineRadioOptions == 'option1'){
							 $("#targetselectDiv").hide();
						 }else if(inlineRadioOptions != '' && inlineRadioOptions == 'option2'){
							 $(".xlkl_col > span.select2-container").css("width",width);
							 $("#targetselectDiv").show();
							
						 }
					 });
					
					self.data = "";
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
					
					 //确定按钮
					 $('#btn_hours_confirm').click(function(){
						 var inlineRadioOptions = $('input[name="inlineRadioOptions1"]:checked ').val();
						 var stationId = $.trim($('#yztselect').val());
						 var targetStationId = $.trim($('#targetselect').val());
						 if(inlineRadioOptions != '' && inlineRadioOptions == 'option2'){
							 if(stationId == '' || stationId == null || targetStationId == ''|| targetStationId == null){
								 comm.alert_tip("请选择源站台和目标站台!");
								 return false;
							 }
							 if(comm.isNotEmpty(stationId)&&comm.isNotEmpty(targetStationId)&&stationId==targetStationId){
								 comm.alert_tip("源站台和目标站台不能为同一站台!");
								 return false;
							 }
						 }
						 
						 var lineId = $('#xlselect').val();
						 var begin_date = $('#begin_date').val();
						 var end_date = $('#end_date').val();
						 
						 //查询日期校验
						 var flag = comm.validQueryDate2( $('#begin_date').val(),$('#end_date').val());
						 if(!flag){
							 return false;
						 }
						 
						 var params = {};
						 params.beginDate = begin_date;
						 params.endDate = end_date;
						 params.stationId = stationId;
						 params.lineId = lineId;
						 params.inlineRadioOptions = inlineRadioOptions;
						 params.targetStationId = targetStationId;
						 comm.request('/report/lineAnalysis/xlfkfxForHours',params,function(resp){
							 if(inlineRadioOptions == 'option1'){
								 $("#div_list_template_od").hide();
								 $("#div_list_template").show();
								 chartData=resp.data.lineData;
								 self.initOptionChart(resp.data.lineData,'line');
								 self.initTable(resp.data.stationData);
							 }else if(inlineRadioOptions == 'option2'){
								 $("#div_list_template").hide();
								 $("#div_list_template_od").show();
								 chartData=resp.data.lineData;
								 self.initOptionChart(resp.data.lineData,'line');
								 self.initTableOD(resp.data.stationData);
							 }
							 self.data = resp.data.stationData;
						 });
					 });
					 
					 //
					 $('#xlselect').change(function(){
						var lineId =  $('#xlselect').val();
						self.initStation(lineId);
					 });
					
					 //初始化表格
					 $("#targetselectDiv").hide();
					 $("#div_list_template").show();
					 $("#div_list_template_od").hide();
					 self.initTable(null);
					 self.initChart();

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
						comm.initSelectOptionForObj('targetselect',resp.data,'name','id',defaultOption);
					});
				},
				exportData : function(){
					var psgType = $('input[name="inlineRadioOptions1"]:checked').val();
					var titleColumn = [];
					var titleName = [];
					var titleSize = [20, 20, 20, 20, 20];
					var img = self.myChart.getDataURL("png");
					if(psgType == 'option1'){
						titleColumn = ["LINE_NAME","STATION_NAME","HH","ONBUS_PERSON_COUNT","OFFBUS_PERSON_COUNT"];
						titleName = ["线路","站点","时间","上车人数","下车人数"];
					}else if(psgType == 'option2'){
						titleColumn = ["LINE_NAME","STATION_NAME","TAGET_STATION_NAME","HH","ONBUS_PERSON_COUNT"];
						titleName = ["线路","源站台","目标站台","时间","站台间客流量"];
					}
					
					var json = self.data;
					if(json[0].HH.split(" ").length <= 1){
						for( var i = 0; i < json.length; i++){
							json[i].HH = json[i].DD+" "+json[i].HH;
						}
					}
					
					$("#img").val(img);
				    $("#tableData").val(JSON.stringify(json));
				    $("#fileName").val("线路客流统计分析-按小时");
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
							start : 0, // 左边在 10% 的位置。
							end : 100
						}, { 
							type : 'inside', // 这个 dataZoom 组件是 inside 型
							start : 0, // 左边在 10% 的位置。
							end : 100
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
						xAxis : {
							type : 'category',
							boundaryGap: false,
							data : []
						},
						yAxis : {
							type : 'value',
							axisLabel : {
				                formatter: '{value} 人次'
				            }
						},
						series : [ ]
					});
				},
			//饼状图初始化
			initPieChart:function(){
				$("#ztklfxCharts").width($('#index_padd').width());
				self.myChart = echarts.init(document.getElementById('ztklfxCharts'));
				var dataPie=chartData;
				var pieArrList=[],legendData = [],totleEnd=0, totleStart=0;
				var begin_date = $('#begin_date').val();
				var end_date = $('#end_date').val();
				var legendData =['分析日期客流','对比日期客流'];
				var line = $('#xlselect').select2("data")[0].text
				var station = $('#yztselect').select2("data")[0].text
				var chartTile = line+'/'+station;
				for(var i=0;i<dataPie.length;i++){
						if(dataPie[i].DD==end_date){
							totleEnd+=parseInt(dataPie[i].TOTAL_PERSON_COUNT);
						}
						else{
							totleStart+=parseInt(dataPie[i].TOTAL_PERSON_COUNT);
						}
				}
				//三种值得设置
				if(end_date&&begin_date ){
				      var pieValueDay=[{value:totleStart,name:"分析日期客流"},{value:totleEnd,name:"对比日期客流"}];
				      pieArrList=pieValueDay;
				}
				else if(begin_date&&!end_date){
					var pieValueHours=[{value:totleStart,name:"分析日期客流"}];
					 pieArrList=pieValueHours;
				}
				else if(end_date&&!begin_date){
					var pieValueHours=[{value:totleEnd,name:"对比日期客流"}];
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
					var line = $('#xlselect').select2("data")[0].text;
					var station = $('#yztselect').select2("data")[0].text;
					var begin_date = $('#begin_date').val();
					var end_date = $('#end_date').val();
					var chartTile = line+'/'+station;//chart 标题
					for(var i=0;i<dataTotile.length;i++){
						if(dataTotile[i].DD==begin_date){
							dataStart.push(dataTotile[i].TOTAL_PERSON_COUNT);
						}
						else{
							dataEnd.push(dataTotile[i].TOTAL_PERSON_COUNT);
						}
						  xAxisData.push(dataTotile[i].HH+"点");//横坐标赋值
			         	}
					//三种值得存在 series设置
						if(begin_date&&!end_date){
							var legendData =['分析日期客流'];
							var series= {name : '分析日期客流',type : type,data : dataStart};
						}
						else if(end_date&&!begin_date){
							var legendData =['对比日期客流'];
							var series= {name : '对比日期客流',type : type,data : dataEnd};
						}
						else if(begin_date&&end_date){
							var legendData =['分析日期客流','对比日期客流'];
							var series=[ {name : '分析日期客流',type : type,data : dataStart}, {name : '对比日期客流',type : type,data : dataEnd}];
						}
					  var series
						self.myChart.setOption({
							title : {
								text : chartTile
							},
							  tooltip : {
								  formatter: function (params) {		    	
				                      	var num=0;
				                        for (var i = 0, l = params.length; i < l; i++) {
				                            var num=parseInt(params[i].name.substring(0,params[i].name.length-1));
				                               num+=1
				                               if(num>=24){
				                            	   var str=num+"点";
				                               }
				                               else if(num<10){
				                            	   var str="0"+num+"点";  
				                               }
				                               else{
				                            	   var str=num+"点";
				                               }
				                               if(params.length==1){
				                            	   return [
									                          params[i].name+"~" +str+ '<br/>',
									                          params[i].seriesName+" : "+ (params[i].value?params[i].value:"0")+ '<br/>',
									                        ].join('');
									     	      }
				                                else{
					                        	   return [
								                          params[i].name+"~" +str+ '<br/>',
								                          params[i].seriesName+" : "+ (params[i].value?params[i].value:"0")+ '<br/>',
								                          params[i+1].seriesName+" : "+ (params[i+1].value?params[i+1].value:"0")+ '<br/>',
								                        ].join('');
				                                  }
							    				}
				                        }
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
							title:'线路',
							data : 'LINE_NAME'
						}, {
							title:'源站台',
							data : 'STATION_NAME'
						},{
							title:'目标站台',
							data : "TAGET_STATION_NAME"
						},{
							title:'时间',
							data : "HH",
							render: function(data, type, row, meta) {
						            if(row.DD!=null&&row.DD!=''){
						            	return row.DD+" "+data;
						            }
						            return data;
						       }
						},{
							title:'站台间客流量',
							data : "ONBUS_PERSON_COUNT"
						}]

					});
				}
			};
			return self;
		});