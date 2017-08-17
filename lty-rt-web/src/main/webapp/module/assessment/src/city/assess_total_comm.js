define(
		[ ],
		function() {
			var self = {
				legendData1:null,
				legendData2:null,
				xAxisData:null,
				unit:'',
				data:null,
				zChart:null,
				chart:null,
				dayType:null,
				dayTypeStr:null,
				show : function(indexType,dayType,queryBtnId) {
					self.dayType = dayType;
					if(dayType=='days'){
						self.dayTypeStr='日';
						$('#id').val("");
					}else if(dayType=='weeks'){
						self.dayTypeStr='周';
						$('#id').val("");
					}else if(dayType=='months'){
						self.dayTypeStr='月';
						$('#id').val("");
					}else if(dayType=='years'){
						self.dayTypeStr='年';
						$('#id').val("");
					}else{
						self.dayTypeStr='日';
						$('#id').val("");
					}
					
					//初始化下拉框
					self.initArea(indexType);
					
					 //确定按钮
					 $(queryBtnId).click(function(){
						 //查询验证
						 if(!self.validQueryCondition()){
							 return false;
						 }
						 //构建查询条件
						 var params = self.buildQueryParams(dayType);
						 //查询数据
						 self.queryData(params);
						 
					 });
					 
					 //导出
					 $('#btn-export').unbind();
					 $('#btn-export').click(function() {
			                self.exportExcel();
			         });
					 //初始化图表
					 self.initChart();
					 self.initCycleChart(null);
					 self.initIndexDescription(null);
					 //初始化表格
					 self.initTable([]);
					 //初始化
					 self.initZChart();
				},
				
				exportExcel:function(){
				 var visitorsFlowRate_mapW = $(".visitorsFlowRate_map").width();
	                var visitorsFlowRate_mapH = $(".visitorsFlowRate_map").height();
	      		    html2canvas($(".ztpjfx_panel"), {
	      		    	onrendered: 
	      		    		function(canvas) {
	      		    			var url = canvas.toDataURL();
	      		    			self.exportData(url);
	      		    			//以下代码为下载此图片功能
	      		    			//var triggerDownload = $("<a>").attr("href", url).attr("download","map.png");
	      		    			//triggerDownload[0].click();
	      		    			//triggerDownload.remove();
	      		    		},
	      		    	width: visitorsFlowRate_mapW,
	      		    	height: visitorsFlowRate_mapH
	      		    });
				},
				
				validQueryCondition:function(){
					 /*var begin_date = $('#begin_date').val();
					 var end_date = $('#end_date').val();
					 var begin_date2 = $('#begin_date2').val();
					 var end_date2 = $('#end_date2').val();*/
					 
					 //查询日期校验							            
		             var flag = comm.validQueryDate($(
									'#begin_date').val(), $(
									'#end_date').val(), $(
									'#begin_date2 ').val(), $(
									'#end_date2').val(),true);
					 if (!flag) {
						return false;
					 }
					 
					/* //两个都为空和两个都填的情况才能查询
					 if((begin_date == '' && end_date != '') || (begin_date != '' && end_date == '')){
						 comm.alert_tip("请选择分析时段开始时间和结束时间");
						 return false;
					 }
					 if((begin_date2 == '' && end_date2 != '') || (begin_date2 != '' && end_date2 == '')){
						 comm.alert_tip("请选择对比时段开始时间和结束时间");
						 return false;
					 }
					 if(begin_date == '' && end_date == '' && begin_date2 == '' && end_date2 == ''){
						 comm.alert_tip("请选择分析时段");
						 return false;
					 }*/
					 return true;
				},
				
				buildQueryParams:function(type){
					 var inlineRadioOptions = $('input[name="inlineRadioOptions"]:checked ').val();
					 var id = $.trim($('#topLevel').val());
					 var begin_date = $('#begin_date').val();
					 var end_date = $('#end_date').val();
					 var begin_date2 = $('#begin_date2').val();
					 var end_date2 = $('#end_date2').val();
					 var params = {};
					 params.beginDate = begin_date;
					 params.endDate = end_date;
					 if(begin_date2!=null&&begin_date2!=''&&begin_date2!='Invalid date'){
						 params.beginDate2 = begin_date2;
						 params.endDate2 = end_date2;
					 }
					 params.arg = id;
					 params.arg1 = type;
					 params.arg2 = inlineRadioOptions;
					 return params;
				},
				
				queryData:function(params){
					 comm.request('/report/index/getDetailChartData',params,function(resp){
						 if(resp.code == 0){
							 var json = resp.data;
							 var result_data = [];
							 var flag = '';
							 for(var key in json){
								 for(var i = 0; i < json[key].length; i++ ){
									 if(key == '1'){
										 json[key][i].arg = '分析时段';
									 }else if(key == '2'){
										 json[key][i].arg = '对比时段';
									 }
									 result_data.push(json[key][i]);
								 }
							 }
							 self.initTable(result_data);
							 self.data = result_data;
							 if(result_data==null||result_data.length<1){
								 self.initCycleChart(null);
							 }else{
								 self.initCycleChart(json);
							 }
							 //加载
							 self.loadTotalChartData(params,result_data);
						 }else{
							comm.alert_tip(resp.msg);
						 }
					 });
				 },
				 
				loadTotalChartData:function(params,tableData){
					 comm.request('/report/index/getTotalChartData',params,function(resp){
						 if(resp.code == 0){
							 self.unit = resp.data[1].unit;
							 if(tableData==null||tableData.length<1){
								 self.initChart();
								 self.initZChart();
								 self.initIndexDescription(null);
							 }else{
								 self.updateChart(resp.data[0]);
								 self.updateZChart(resp.data[1]);
								 self.initIndexDescription(resp.data[1],true,tableData);
							 }
						 }else{
							comm.alert_tip(resp.msg);
						 }
					 });
				 },
				 
			
				initZChart : function(){
					self.zChart = echarts.init(document.getElementById('zCharts'));
				    // series中的每一项为一个item,所有的属性均可以在此处定义
				    var Series = []; // 准备存放图表数据
				    var xAxis = [];
				    
				    self.zChart.setOption({
						title : {
							text :'评价分析图'
						},
						tooltip : {
					        trigger: 'axis',
					        axisPointer : {            // 坐标轴指示器，坐标轴触发有效，鼠标指示的提示
					            type : 'line'        // 默认为直线，可选为：'line' | 'shadow'
					        }
					    },
					    toolbox: {
					        feature: {
					            saveAsImage: {show: true}      //下载图表
					        }
					    },
					    grid: {
					        left: '3%',
					        right: '4%',
					        bottom: '15%',
					        containLabel: true
					    },
						legend : {
							data : 'ddd'
						},
						xAxis : {
	                        data: xAxis
	                    },
						yAxis : [
									{
									    type : 'value',
									    axisLabel : {
							                formatter: '{value}指标等级'
							            }
									}
								],
						series : {
							name : '值',
							type: 'bar',
							barWidth : 20,
							data : Series
						}
					});
				},
				updateZChart : function(data){
					var begin_date = $('#begin_date').val();
				    var end_date = $('#end_date').val();
				    var begin_date2 = $('#begin_date2').val();
				    var end_date2 = $('#end_date2').val();
				    
				    // series中的每一项为一个item,所有的属性均可以在此处定义
				    var Series = []; // 准备存放图表数据
				    
				    var xAxis = [];
				    //var xAxisData = [];
				    if(begin_date2 == '' || end_date2 == ''){
				    	xAxis = ['分析时间段', '目标'];
				    	xAxisData = [data.curAvg, data.targetLevel];
				    	for(var i = 0; i < 2; i++){
				    		if(i == 0){
				    			Series[i] = data.curAvg;
				    		}else{
				    			Series[i] = data.targetLevel;
				    		}
				    	}
				    }else{
				    	xAxis = ['对比时间段','分析时间段', '目标'];
				    	//xAxisData = [data.compareAvg, data.curAvg, data.targetLevel];
				    	for(var i = 0; i < 3; i++){
				    		if(i == 0){
				    			Series[i] = data.compareAvg;
				    		}else if(i == 1){
				    			Series[i] = data.curAvg;
				    		}else{
				    			Series[i] = data.targetLevel;
				    		}
				    	}
				    }
					self.zChart.setOption({
						title : {
							text :data.charName
						},
						xAxis : {
	                        data: xAxis
	                    },
						series : {
							name : '值',
							type: 'bar',
							barWidth : 20,
							data : Series
						}
					});
				},
				
				getBadIndexData:function(tableData){
					if(tableData==null||tableData.length<1){
						return '';
					}
					var str ='';
					for(var i=0;i<tableData.length;i++){
						var obj = tableData[i];
						if(obj.description=='差'||comm.isEmpty(obj.description)){
							str +='<p style=\'text-indent:2em;color:red\'>';
							str +=obj.name+'('+obj.arg+')>> 目标值：'+obj.targetLevel+' 实际值：'+obj.actualLevel +' 与目标值的差异率为：'+parseInt((((obj.targetLevel-obj.actualLevel)/obj.targetLevel) *100))+'%' ;
							str +='</p>';
						}
					}
					
					if(comm.isNotEmpty(str)){
						str = '<br/><p style=\'text-indent:2em\'>其中未达标的指标值如下：</p>'+str;
					}
					return str;
				},
				
				getBadIndex:function(tableData){
					if(tableData==null||tableData.length<1){
						return '';
					}
					var str ='';
					str +='<p style=\'text-indent:2em;color:red\'>';
					for(var i=0;i<tableData.length;i++){
						var obj = tableData[i];
						if(obj.description=='差'||comm.isEmpty(obj.description)){
							str +=obj.name+"、";
						}
					}
					str +='</p>';
					
					return str;
				},
				
				initIndexDescription : function(data,flag,tableData){
					if(data == null||!flag||tableData==null||tableData.length<1){
						$('#indexDescription').html("<br/&nbsp;&nbsp;指标说明：");
						$('#index').html("<br/>&nbsp;&nbsp;评价：");
						$('#proposal').html("<br/>&nbsp;&nbsp;建议：");
						$('#descCharts').html("<br/>&nbsp;&nbsp;说明：");
					}else{
						//指标说明
						if(data.descriptions == null){
							$('#indexDescription').html("<br/>&nbsp;&nbsp;指标说明：<br/><p style='text-indent:2em'>无，请先维护指标说明！</p>");
						}else{
							$('#indexDescription').html("<br/>&nbsp;&nbsp;指标说明：<br/><p style='text-indent:2em'>"+data.descriptions + "</p>");
						}
						
						//评价
						if(data.curAvg == undefined){
							$('#index').html("<br/>&nbsp;&nbsp;评价：<br/><p style='text-indent:2em'>无，请先维护对应的指标等级！</p>");
						}else{
							var indexstr = "<br/>&nbsp;&nbsp;评价：<br/><p style='text-indent:2em'>" + data.charName + "指标值为" + data.curAvg + ",";
							if(data.curAvg - data.targetLevel > 0 ){
								indexstr += "高于目标值" + data.diff;
							}else if(data.curAvg - data.targetLevel < 0 ){
								indexstr += "低于目标值" + data.diff;
							}else{
								indexstr += "与目标值相等";
							}
							indexstr += "。</p>";
							
							var badIndexData = self.getBadIndexData(tableData);
							if(comm.isNotEmpty(badIndexData)){
								indexstr += badIndexData;
							}
							$('#index').html(indexstr);
						}
						
						//建议
						if(data.proposal){
							var proposal = self.getBadIndex(tableData);
							if(comm.isNotEmpty(proposal)){
								$('#proposal').html("<br/>&nbsp;&nbsp;建议：&nbsp;&nbsp;&nbsp;&nbsp;<br/><p style='text-indent:2em'>" + data.proposal + " 其中有待提高的指标为："+proposal+"</p>");
							}else{
								$('#proposal').html("<br/>&nbsp;&nbsp;建议：&nbsp;&nbsp;&nbsp;&nbsp;<br/><p style='text-indent:2em'>" + data.proposal + "</p>");
							}
						}else{
							$('#proposal').html("<br/>&nbsp;&nbsp;建议：&nbsp;&nbsp;&nbsp;&nbsp;<br/><p style='text-indent:2em'>无，请先维护指标建议！</p>");
						}
						
						//对柱状图的说明
						var descCharts = "<br/>说明：" ;
						if(data.curAvg != undefined){
							descCharts += "<br/>&nbsp;&nbsp;当前指标：" + data.curAvg;
						}
						if(data.compareDiff != undefined){
							descCharts += "<br/>&nbsp;&nbsp;对比差异率：" + data.compareDiff + "%";
						}
						if(data.targetDiff != undefined){
							descCharts += "<br/>&nbsp;&nbsp;目标差异率：" + data.targetDiff + "%" ;
						}
						$('#descCharts').html("<p>"+descCharts+"</p>");
					}
					
				},
				exportData : function(img){
					var psgType = $('input[name="inlineRadioOptions1"]:checked').val();
					var titleColumn = ["arg", "name", "weight", "actualLevel", "targetLevel", "description"];
					var titleName = ["标识","指标名称","权重","指标值","目标值","说明"];
					var titleSize = [20, 20, 20, 20, 20, 20];
					var picLoc = [0, 0, 0, 0, 0, 1, 18, 145];
					
					$("#img").val(img);
				    $("#tableData").val(JSON.stringify(self.data));
				    var indexName =$('#topLevel').select2("data")[0].text;
				    $("#fileName").val(indexName+'-按'+(self.dayTypeStr)+'');
				    $("#titleColumn").val(JSON.stringify(titleColumn));
				    $("#titleName").val(JSON.stringify(titleName));
				    $("#titleSize").val(JSON.stringify(titleSize));
				    $("#picLocation").val(JSON.stringify(picLoc));
				    $("#export_form").submit();
				},
				
				initChart: function(){
					self.chart = echarts.init(document.getElementById('dCharts'));
					self.chart.setOption({
						title : {
							text :"总体评价分析图"
						},
						tooltip : {
					        trigger: 'axis',
					        axisPointer : {            // 坐标轴指示器，坐标轴触发有效，鼠标指示的提示
					            type : 'line'        // 默认为直线，可选为：'line' | 'shadow'
					        }
					    },
					    toolbox: {
					        feature: {
					            saveAsImage: {show: true}      //下载图表
					        }
					    },
					    grid: {
					        left: '3%',
					        right: '4%',
					        bottom: '15%',
					        containLabel: true
					    },
						legend : {
							data : []
						},
						xAxis : [
							        {
							            type : 'category',
							            data : [],
							            boundaryGap : false,
								        axisLabel:{ 
					                    	 interval : 0,
					                    	 rotate:90
					                     }
							        }
							    ],
						yAxis : [
									{
									    type : 'value',
									    axisLabel : {
							                formatter: '{value}'+self.unit
							            }
									}
								],
						series : []
					});
					
				},
				
				updateChart: function(data){
					//从新初始化
					if(!data||data.length<1){
						self.initChart();
						return false;
					}
					self.initChart();
					self.legendData1 = [];
					self.xAxisData = [];
					var Item = function() {
						return {
							name : '',
							type : 'line',
							data : []
						}
					};
					var json = data;// 后台返回的json
					var Series = []; // 准备存放图表数据
					var xAxisLength = 0;

					for(var key in json){
						var begin_date = $('#begin_date').val();
					    var end_date = $('#end_date').val();
					    var begin_date2 = $('#begin_date2').val();
					    var end_date2 = $('#end_date2').val();
					    
						var item = new Item();
						if(key == '1'){
							xAxisLength = json[key].length;
							item.name = '分析时段';
							self.legendData1.push('分析时段');
						}else if(key == '2'){
							item.name = '对比时段';
							self.legendData1.push('对比时段');
						}
						
						if(json[key].length >= xAxisLength){
							self.xAxisData = [];
						}
						
						for(var i = 0; i < json[key].length; i++ ){
							if(json[key].length >= xAxisLength){
								if(begin_date != '' && end_date != '' && begin_date2 != '' && end_date2 != ''){
									self.xAxisData.push('第'+(i+1)+self.dayTypeStr);
								}else if(begin_date != '' && end_date != '' || begin_date2 != '' && end_date2 != ''){
									self.xAxisData.push(json[key][i].countDate.split(" ")[0] + self.dayTypeStr);
								}
							}
							item.data.push(json[key][i].actualLevel);
						}
						
						Series.push(item);
						
					}
					self.chart.setOption({
						title : {
							text :"总体评价分析图"
						},
						legend : {
							data : self.legendData1
						},
						xAxis : [
							        {
							            type : 'category',
							            data : self.xAxisData,
							            boundaryGap : false,
								        axisLabel:{ 
					                    	 interval : 0,
					                    	 rotate:90
					                     }
							        }
							    ],
						series : Series
					});
					
				},
				
				initCycleChart : function(data){
					if(data==null||data.length<1){
						$('#dCycleCharts').html('');
						return false;
					}
					self.legendData2 = [];
					var myChart = echarts.init(document.getElementById('dCycleCharts'));
					
					var Item = function() {
						return {
							name : '',
							value : []
						}
					};
					
					var Indic = function(){
						return {
							name : '',
							max : 5
						}
					};
					
					var json = data;// 后台返回的json
					var Series = []; // 准备存放图表数据
					var xAxisLength = 0;
					var radar = [];//雷达图的圆
					
					if(json != null){
						var jsonLength = Object.keys(json).length;
						if(jsonLength == 1){
							for(var key in json){
								var item1 = new Item();
								var item2 = new Item();
								
								item1.name = '实际指标值';
								item2.name = '目标值';
								self.legendData2.push('实际指标值');
								self.legendData2.push('目标值');
								
								for(var i = 0; i < json[key].length; i++ ){
									var indic = new Indic();
									indic.name = json[key][i].name;
									radar.push(indic);
									
									item1.value.push(json[key][i].actualLevel);
									item2.value.push(json[key][i].targetLevel);
								}
								Series.push(item1);
								Series.push(item2);
							}
						}else if(jsonLength == 2){
							for(var key in json){
								var item = new Item();
								
								if(key == '1'){
									xAxisLength = json[key].length;
									item.name = '分析时段';
									self.legendData2.push('分析时段');
								}else if(key == '2'){
									item.name = '对比时段';
									self.legendData2.push('对比时段');
								}
								
								if(json[key].length >= xAxisLength){
									self.xAxisData = [];
								}
								
								for(var i = 0; i < json[key].length; i++ ){
									var indic = new Indic();
									if(json[key].length >= xAxisLength){
										self.xAxisData.push('第'+(i+1)+self.dayTypeStr);
									}
									item.value.push(json[key][i].actualLevel);
									
									if(key == '1'){
										indic.name = json[key][i].name;
										radar.push(indic);
									}
								}
								Series.push(item);
							}
						}
					}
					
					
					// 显示标题，图例和空的坐标轴
					myChart.setOption({
						title : {
							text : '总体评价雷达图'
						},
						tooltip: {
						},
						textStyle: {
					        color: 'black'
					    },
					    grid: {
					        left: '5%',
					        right: '4%',
					        bottom: '15%',
					        containLabel: true
					    },
						legend: {
					        data: self.legendData2
					    },
					    radar: {
					        // shape: 'circle',
					        indicator: radar
					    },
					    series: [{
					        name: '总体分析',
					        type: 'radar',
					        // areaStyle: {normal: {}},
					        data : Series
					    }]
					});
				},
				
				initTable : function(data) {
					$('#table_list_template').DataTable({
						select: true,
						//每页显示三条数据
						pageLength : 30,
						destroy: true,
						data: data,
						language:dataTable_cn,
						createdRow: function( row, data, dataIndex ) {
							if(data.description=='差'||comm.isEmpty(data.description)){
								row.style.backgroundColor="red";
								row.style.color="white";
							}
						},
						columns : [{
							//title:'标识',
							data : 'arg'
						},{
							//title:'指标名称',
							data : 'name'
						}, {
							//title:'权重',
							data : 'weight'
						},{
							title:'指标值',
							data : "actualScore"
						},{
							title:'指标等级',
							data : "actualLevel"
						},{
							title:'等级范围',
							data : "actualLevel",
							render: function(data, type, row, meta) {
								if(row.actualIndexLevel==null){
									 return '';
								}
								else{
									return row.actualIndexLevel.lowerLimit+'~'+row.actualIndexLevel.topLimit+'('+row.levelUnit+')';
								}
					        }
						},{
							title:'目标等级',
							data : "targetLevel"
						},{
							title:'目标等级范围',
							data : "actualLevel",
							render: function(data, type, row, meta) {
								if(row.targetIndexLevel==null){
									 return '';
								}
								else{
									return row.targetIndexLevel.lowerLimit+'~'+row.targetIndexLevel.topLimit+'('+row.levelUnit+')';
								}
					        }
						},{
							//title:'说明',
							data : "description"
						}]

					});
				},
				
				initArea : function(indexType) {
					comm.requestJson('/report/index/getAllTopLevels',indexType,function(resp){
						if(resp.code == 0){
							comm.initSelectOptionForObj('topLevel',resp.data,'name','id');
						}else{
							comm.alert_tip(resp.msg);
						}
					});
				}
			};
			return self;
		});