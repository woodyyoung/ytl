define(
		[],
		function(tpl) {
			var self = {
				legendData:null,
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
					
					//初始化左树
					self.initLeftTree(indexType);
					
					//初始化下拉框
					self.initSelect();
					
					
					 //确定按钮
					 $(queryBtnId).click(function(){
						 self.queryData(dayType);
					 });
					 
					 //导出
					 $('#btn-export').unbind();
					 $('#btn-export').click(function() {
			                self.exportExcel();
			         });
					 
					 //初始化图表
					 self.initChart();
					 self.initZChart();
					 self.initIndexDescription(null);
					 //初始化表格
					 self.initTable([]);
					
				},
				
				exportExcel:function(){
					var visitorsFlowRate_mapW = $(".visitorsFlowRate_map").width();
	                var visitorsFlowRate_mapH = $(".visitorsFlowRate_map").height();
	      		    html2canvas($("#fxpjjs_panel"), {
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
				
				queryData:function(dayType){
					 var inlineRadioOptions = $('input[name="inlineRadioOptions"]:checked ').val();
					 var id = $('#id').val();
					 if(id == null || id == ''){
						 comm.alert_tip("请先选择指标");
						 return false;
					 }
					 
					//查询日期校验							            
		             var flag = comm.validQueryDate($(
									'#begin_date').val(), $(
									'#end_date').val(), $(
									'#begin_date2 ').val(), $(
									'#end_date2').val(),true);
					 if (!flag) {
						return false;
					 }
					 
					 var begin_date = $('#begin_date').val();
					 var end_date = $('#end_date').val();
					 var begin_date2 = $('#begin_date2').val();
					 var end_date2 = $('#end_date2').val();
					 /*//两个都为空和两个都填的情况才能查询
					 if((begin_date == '' && end_date != '') || (begin_date != '' && end_date == '')){
						 comm.alert_tip("请选择分析时段开始时间和结束时间");
						 return false;
					 }
					 if((begin_date2 == '' && end_date2 != '') || (begin_date2 != '' && end_date2 == '')){
						 comm.alert_tip("请选择对比时段开始时间和结束时间");
						 return false;
					 }
					 if(begin_date == '' && end_date == '' && begin_date2 == '' && end_date2 == ''){
						 comm.alert_tip("请选择时间");
						 return false;
					 }
					 if (($('#end_date').val()) >= ($('#begin_date2').val())||($('#begin_date').val()) >= ($('#end_date2').val())) {
							comm.alert_tip('请选择正确日期范围 ');
							return false;
						}*/
					 var lineId = $.trim($('#lineSelect option:selected').val());
					 var areaId = $.trim($('#areaSelect option:selected').val());
					 
					 var params = {};
					 if(id.indexOf("005") == 0){//个体
						 params.arg3 = lineId;
						 params.arg4 = '';
					 }else if(id.indexOf("006") == 0){//区域
						 params.arg3 = '';
						 params.arg4 = areaId;
					 }else{
						 params.arg3 = '';
						 params.arg4 = '';
					 }
					 
					
					 params.beginDate = begin_date;
					 params.endDate = end_date;
					 if(begin_date2!=null&&begin_date2!=''&&begin_date2!='Invalid date'){
						 params.beginDate2 = begin_date2;
						 params.endDate2 = end_date2;
					 }
					 params.arg = id;
					 params.arg1 = dayType;
					 params.arg2 = inlineRadioOptions;
					 
					 comm.request('/report/indexSourceData/getChartData',params,function(resp){
						 if(resp.code == 0){
							 self.unit = resp.data[1].unit;
							 var tableData = resp.data[0];
							 var fxdata = tableData[0];
							 if(fxdata == null||fxdata.length<1){
								 fxdata =null;
							 }
							 var dbdata = tableData[1];
							 if(dbdata ==null|| dbdata.length<1){
								 dbdata =null;
							 }
							 if(fxdata==null&&dbdata==null){
								 self.initTable([]);
								 self.initChart();
								 self.initZChart();
								 self.initIndexDescription(null);
							 }else{
								 //self.initTable(tableData);
								 self.updateChart(tableData);
								 self.updateZChart(resp.data[1]);
								 self.initIndexDescription(resp.data[1]);
							 }
							/* self.initTable(tableData);
							 self.data = tableData;
							 self.initChart(tableData);
							 self.initZChart(resp.data[1]);
							 self.initIndexDescription(resp.data[1]);
							 self.initTable(resp.data);
							 comm.alert_tip(resp);*/
						 }else{
							comm.alert_tip(resp.msg);
						 }
					 });
				},
				
				initZChart : function(){
					self.zChart = echarts.init(document.getElementById('zCharts'));
					self.zChart.setOption({
						title : {
							text :'分项评价分析图'
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
	                        data: []
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
							data : []
						}
					});
				},
				
				updateZChart : function(data){
					//从新初始化
					if(!data||data.length<1){
						self.initZChart();
						return false;
					}
					self.initZChart();
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
				
				initChart: function(){
					self.chart = echarts.init(document.getElementById('dCharts'));
					self.chart.setOption({
						title : {
							text :"评价分析图"
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
							data : ['分析时段','对比时段']
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
					self.initChart();
					self.legendData = [];
					self.xAxisData = [];

				    var begin_date = $('#begin_date').val();
				    var end_date = $('#end_date').val();
				    var begin_date2 = $('#begin_date2').val();
				    var end_date2 = $('#end_date2').val();
					
				 // series中的每一项为一个item,所有的属性均可以在此处定义
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
					var dateRange = [];

					for(var key in json){
						var item = new Item();
						if(key == '1'){
							xAxisLength = json[key].length;
							item.name = '分析时段';
							self.legendData.push('分析时段');
						}else if(key == '2'){
							item.name = '对比时段';
							self.legendData.push('对比时段');
						}
						
						if(json[key].length > xAxisLength){
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
							dateRange.push(json[key][i]);
							item.data.push(json[key][i].actualScore);
						}
						
						Series.push(item);
						
					}
					
					self.initTable(dateRange);
					self.data = dateRange;
					
					self.chart.setOption({
						legend : {
							data : self.legendData
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
						yAxis : [
									{
									    type : 'value',
									    axisLabel : {
							                formatter: '{value}'+self.unit
							            }
									}
								],
						series : Series
					});
					
				},
				
				initIndexDescription : function(data){
					if(data == null){
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
							var indexstr = "<br/>&nbsp;&nbsp;评价：<br/><p style='text-indent:2em'>" + data.charName + "为" + data.curAvg + ",";
							if(data.curAvg - data.targetLevel > 0 ){
								indexstr += "高于目标值" + data.diff;
							}else if(data.curAvg - data.targetLevel < 0 ){
								indexstr += "低于目标值" + data.diff;
							}else{
								indexstr += "与目标值相等";
							}
							indexstr += "," + data.charName + "表现" + data.levelDescription + "。</p>";
							$('#index').html(indexstr);
						}
						
						//建议
						if(data.proposal){
							$('#proposal').html("<br/>&nbsp;&nbsp;建议：&nbsp;&nbsp;&nbsp;&nbsp;<br/><p style='text-indent:2em'>" + data.proposal + "</p>");
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
				
				initTable : function(data) {
					$('#table_list_template').DataTable({
					
						//每页显示三条数据
						//pageLength : 5,
						language:dataTable_cn,
						destroy: true,
						data: data,
						columns : [{
							//title:'指标ID',
							data : 'indexId'
						}, {
							//title:'指标名称',
							data : 'indexName'
						},{
							//title:'统计日期',
							data : "countDate",
							render: function(data, type, row, meta) {
					            return data.substr(0,10);
					        }
						},{
							//title:'统计数量',
							data : "indexNum"
						},{
							//title:'统计总数',
							data : "indexTotalNum"
						},{
							//title:'实际指标值',
							data : "actualScore"
						},{
							//title:'实际指标等级',
							data : "actualLevel"
						},{
							//title:'实际指标值',
							data : "actualLevel",
							render: function(data, type, row, meta) {
								if(row.actualIndexLevel==null){
									 return '';
								}
								else{
									return row.actualIndexLevel.lowerLimit+'~'+row.actualIndexLevel.topLimit+'('+row.levelUnit+')';
								}
					        }
						}]

					});
				},
				initSelect : function(){
					comm.requestJson('/report/lineAnalysis/listAllLine', null,
				            function(resp) {
				                comm.initSelectOptionForObj('lineSelect', resp.data, 'name', 'id');
				            });
					
					var params = {};
					comm.request('/report/areaAnalysis/listAllArea', params,
				            function(resp) {
				                comm.initAreaSelect('#areaSelect',resp.data);
				            });
			            
				},
				exportData : function(img){
					var psgType = $('input[name="inlineRadioOptions1"]:checked').val();
					var titleColumn = ["indexId", "indexName", "countDate", "indexNum", "indexTotalNum", "actualLevel", "actualScore"];
					var titleName = ["指标ID","指标名称","统计日期","统计数量","统计总数","实际指标等级","实际指标值"];
					var titleSize = [20, 20, 20, 20, 20, 20, 20];
					var picLoc = [0, 0, 0, 0, 0, 1, 18, 120];
					
					$("#img").val(img);
				    $("#tableData").val(JSON.stringify(self.data));
				    $("#fileName").val("分项评价计算-按"+self.dayTypeStr);
				    $("#titleColumn").val(JSON.stringify(titleColumn));
				    $("#titleName").val(JSON.stringify(titleName));
				    $("#titleSize").val(JSON.stringify(titleSize));
				    $("#picLocation").val(JSON.stringify(picLoc));
				    $("#export_form").submit();
				},

				initLeftTree : function(indexType) {
					comm.requestJson('/report/index/getIndexTree', indexType,
							function(resp) {
								if(resp.code == 0){
									self.initTree(resp.data);
								}else{
									comm.alert_tip(resp.msg);
								}
								
							},function(resp){
								comm.alert_tip("树加载失败...")
							});
				},
				initTree : function(data) {
					self.myTreeTable = $('#treeview-searchable').treeview({
						levels: 2,
			            showBorder: false,
			            showTags: false,
			            data:data
					});
					
					//左树点击事件
					$("#treeview-searchable").on("nodeSelected",function(event,defaultData){
						var id = defaultData.id;
						if(id == null || id == ''){
							comm.alert_tip("请先选择指标");
						}else{
							if(id.indexOf("005") == 0){//个体
								$("#lineSelectDiv").show();
								$("#areaSelectDiv").hide();
							}else if(id.indexOf("006") == 0){//区域
								$("#lineSelectDiv").hide();
								$("#areaSelectDiv").show();
							}else{
								$("#lineSelectDiv").hide();
								$("#areaSelectDiv").hide();
							}
						}
						$("#id").val(id);
					});
					//默认展开第一级节点 	
					$("#treeview-searchable").treeview('expandNode', [ 0, { levels: 1, silent: true } ]);
					
				}
			};
			return self;
		});