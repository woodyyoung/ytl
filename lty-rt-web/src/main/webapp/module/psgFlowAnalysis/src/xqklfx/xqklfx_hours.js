define(
		[ 'text!psgFlowAnalysis/tpl/xqklfx/xqklfx_hours.html' ],
		function(tpl) {
			var 	chartData;
			var self = {
				myChart:null,
				data:null,
				show : function() {
					$('#xqklfx_form').html(tpl);
					
					//初始化小区下拉控件
					self.initArea();
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
							 $(".xqkl_col > span.select2-container").css("width",width);
							 $("#targetselectDiv").show();
							
						 }
					 });
					
					 self.data = "";
					 $('#xqklfx_export').unbind();
					 $('#xqklfx_export').click(function(){
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
						 var targetAreaCode = $.trim($('#targetselect').val());
						 var areaCode = $.trim($('#yztselect').val());
						 var begin_date = $('#begin_date').val();
						 var end_date = $('#end_date').val();
						 
						 var params = {};
						 params.beginDate = begin_date;
						 params.endDate = end_date;
						 params.areaCode = areaCode;
						 params.inlineRadioOptions = inlineRadioOptions;
						 params.targetAreaCode = targetAreaCode;
						//查询日期校验
						 var flag = comm.validQueryDate2( $('#begin_date').val(),$('#end_date').val());
						 if(!flag){
							 return false;
						 }
						 comm.request('/report/areaAnalysis/xlfkfxForHours',params,function(resp){
							 if(inlineRadioOptions == 'option1'){
								 $("#div_list_template_od").hide();
								 $("#div_list_template").show();
								// self.updateChart(resp.data.areaData);
								 chartData=resp.data.areaData;
								 self.initOptionChart(resp.data.areaData,'line');
								 self.initTable(resp.data.stationData);
							 }else if(inlineRadioOptions == 'option2'){
								 $("#div_list_template").hide();
								 $("#div_list_template_od").show();
								 chartData=resp.data.areaData;
								 self.initOptionChart(resp.data.areaData,'line');
								 self.initTableOD(resp.data.stationData);
							 }
							 self.data = resp.data.stationData;
						 });
					 });
					 
					 //初始化表格
					 $("#div_list_template").show();
					 $("#div_list_template_od").hide();
					 $("#targetselectDiv").hide();
					 self.initTable(null);
					 self.initChart();
				},
				initArea : function() {
					var params = {};
					comm.request('/report/areaAnalysis/listAllArea',params,function(resp){
						
						comm.initAreaSelect('#yztselect',resp.data);
						comm.initAreaSelect('#targetselect',resp.data);
						
					});
				},
				
				
				
				
				/*selectGrop:function(data){
					for(var i = 0; i<data.length; i++){
						var newcodeid = [];
						var parentCodeid = data[i].parentcodeid;
                     		for(var j = 0; j<data.length; j++){
					            var codeId = data[j].codeid;
					            if(parentCodeid == codeId){
					            	var parentname=data[j].codename
					            }
                     		}
					}
				},*/
				/*selectGrop:function(data){
					for(var i = 0; i<data.length; i++){
						var levels = data[i].levels;
						var codename = data[i].codename;
						if(levels == 0){
							var temp1 = "<span class='selectGrop_sp1'>"+codename+"</span>";
							$("#yztselect").append(temp1);
						}else if(levels == 1){
							var temp2 = "<span class='selectGrop_sp2'>"+codename+"</span>";
							$("#yztselect").append(temp2);
						}else{
							var temp3 = "<span class='selectGrop_sp3'>"+codename+"</span>";
							$("#yztselect").append(temp3);
						}
					}
				},*/
				
				/*createFirstLevel:function(data,newdata){
			        for(var i = 0; i<data.length; i++){						
						var codeid = data[i].codeid;
						var level = data[i].levels;
						if(level==0){		
							 newdata.push(data[i]);
							 self.createSecondLevel(data,codeid,newdata);
							 
						}                     			
					}
				},
				createSecondLevel:function(data,parentcodeid,newdata){
				         
					for(var j = 0; j<data.length; j++){
						var codeid = data[j].codeid;
						var secondparentcodeid = data[j].parentcodeid;
						if(secondparentcodeid == parentcodeid){
							newdata.push(data[j]);
							self.createThirdLevel(data,codeid,newdata);
							
						}
					}
				},
				createThirdLevel:function(data,parentcodeid,newdata){
				        
						for(var j = 0; j<data.length; j++){			
							var thirdparentcodeid = data[j].parentcodeid;
							if(thirdparentcodeid == parentcodeid){
							   newdata.push(data[j]);
							}
						}
				},
*/

				exportData : function(){
					var psgType = $('input[name="inlineRadioOptions1"]:checked').val();
					var titleColumn = [];
					var titleName = [];
					var titleSize = [20, 20, 40, 20, 20, 20];
					var img = self.myChart.getDataURL("png");
					if(psgType == 'option1'){
						titleColumn = ["AREA_NAME","LINE_NO","P_NAME","HH","ONBUS_PERSON_COUNT","OFFBUS_PERSON_COUNT"];
						titleName = ["小区","线路","站台","时间","上车人数","下车人数"];
					}else if(psgType == 'option2'){
						titleColumn = ["AREA_NAME","TAGET_AREA_NAME","HH","ONBUS_PERSON_COUNT"];
						titleName = ["源小区","目标小区","时间","小区间客流量"];
					}
					
					var json = self.data;
					if(json[0].HH.split(" ").length <= 1){
						for( var i = 0; i < json.length; i++){
							json[i].HH = json[i].DD+" "+json[i].HH;
						}
					}
					
					$("#img").val(img);
				    $("#tableData").val(JSON.stringify(json));
				    $("#fileName").val("区域客流统计分析-按小时");
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
							text : '区域客流量'
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
						xAxis : {
							type : 'category',
							boundaryGap: false,
							axisLabel : {
				                formatter: '{value}'
				            },
							data : []
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
					var begin_date = $('#begin_date').val();
					var end_date = $('#end_date').val();
					var legendData =['分析日期客流','对比日期客流'];
					var station = $('#yztselect').select2("data")[0].text
					var chartTile = station;
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
						var chartTile = station;//chart 标题
						for(var i=0;i<dataTotile.length;i++){
							if(dataTotile[i].DD==begin_date){
								dataStart.push(dataTotile[i].TOTAL_PERSON_COUNT);
							}
							else{
								dataEnd.push(dataTotile[i].TOTAL_PERSON_COUNT);
								console.log(dataEnd);
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
					//$('#table_list_template').html('');
					$('#table_list_template').DataTable({
						select: true,
						//每页显示三条数据
						//pageLength : 5,
						destroy: true,
						data: data,
						language:dataTable_cn,
						columns : [{
							//title:'小区',
							data : 'AREA_NAME'
						}, {
							title:'线路',
							data : 'LINE_NO'
							
						},{
							title:'站台',
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
							title:'源小区',
							data : 'AREA_NAME'
						}, {
							title:'目标小区',
							data : 'TAGET_AREA_NAME'
							
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
							title:'小区间客流量',
							data : "ONBUS_PERSON_COUNT"
						}]

					});
				}
			};
			return self;
		});