define(
		[ 'text!busDecisionAnalysis/tpl/xwyhfzfx/wireMeshPreAssessment/xwtzypg.html' ],
		function(tpl) {
			var self = {
				show : function(title) {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					$("#yztselect").select2({});
					//初始化左树
					self.initTree();
					//初始化echarts
					self.initCycleChart();
					//初始化日历控件
					 $("#begin_date").datetimepicker({
						format : 'yyyy-mm-dd',
						language : "zh-CN",
						minView : 'month'
					});
				},
				initTree : function(data){
					var data = [
					    {
			            text: '总公司',
			            href: '#parent1',
			            tags: ['8'],
			            nodes: [
			              {
			                text: '一公司',
			                href: '#child1',
			                tags: ['5'],
			                nodes:[
			                	{
			                		text: '1路',
					                href: '',
					                tags: ['2'],
					                nodes:[
					                	{
					                		text: '方案一',
							                href: '',
							                tags: ['0'],
					                	},
					                	{
					                		text: '方案二',
							                href: '',
							                tags: ['0'],
					                	}
					                ]
			                	},
			                	{
			                		text: '2路',
					                href: '',
					                tags: ['0'],
			                	},
			                	{
			                		text: '3路',
					                href: '',
					                tags: ['0'],
			                	}
			                ]
			              },
			              {
			                text: '二公司',
			                href: '#child2',
			                tags: ['0']
			              },
			              {
			                text: '三公司',
			                href: '#child3',
			                tags: ['0']
			              }
			            ]
			          }
			        ];
					self.myTreeTable = $('#treeview-searchable').treeview({
						levels: 1,
			            showBorder: false,
			            showTags: true,
			            data:data
					});
				},
				initCycleChart : function(){
					var myChart = echarts.init(document.getElementById('dCycleCharts'));					
					option = {
					    title: {
					        text: '雷达图'
					    },
					    tooltip: {},
					    legend: {
					        data: ['1路', '调整前']
					    },
					    radar: {
					        // shape: 'circle',
					        indicator: [
					           { name: '每日客运总量评价', max: 6500},
					           { name: '每公里客运密度评价', max: 16000},
					           { name: '线路长度评价', max: 30000},
					           { name: '线路非直线系数评价', max: 38000},
					           { name: '断面客流不均衡系数评价', max: 52000},
					           { name: '方向客流不均衡系数评价', max: 25000},
					           { name: '时间客流不均衡系数评价', max: 6500},
					           { name: '站台负荷度评价', max: 16000},
					           { name: '最高满载率评价', max: 30000},
					           { name: '平均满载率评价', max: 38000},
					           { name: '平均站距', max: 52000},
					           { name: '平均乘车距离', max: 25000}
					        ]
					    },
					    series: [{
					        name: '1路 vs 调整前',
					        type: 'radar',
					        // areaStyle: {normal: {}},
					        data : [
					            {
					                value : [4300, 10000, 28000, 35000, 50000, 19000,4300, 10000, 28000, 35000, 50000, 19000],
					                name : '1路'
					            },
					             {
					                value : [5000, 14000, 28000, 31000, 42000, 21000,4300, 10000, 28000, 35000, 50000, 19000],
					                name : '调整前'
					            }
					        ]
					    }]
					};
					if (option && typeof option === "object") {
					    myChart.setOption(option, true);
					}
				},
			};
			return self;
		});