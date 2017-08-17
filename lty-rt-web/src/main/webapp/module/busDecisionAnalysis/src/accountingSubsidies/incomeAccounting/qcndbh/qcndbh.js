define(
		[ 'text!busDecisionAnalysis/tpl/accountingSubsidies/incomeAccounting/qcndbh/qcndbh.html' ],
		function(tpl) {
			var arr = [];
			Array.prototype.range = function ( start,end ){
			    var _self = this;
			    var length = end - start +1;
			    var step = start - 1;
			    return Array.apply(null,{length:length}).map(function (v,i){step++;return step;});
			}
			var self = {
				mytable:null,
				show : function(title) {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					$("#line_id").select2({});
					$("#pvcosttype_id").select2({});
					$('.qcndbh_company').hide();
					//初始化折线图
					self.initPvCostTypes('pvcosttype_id');
					
					self.initDepartments('company_id');
					self.initLine('line_id');
					
					//初始化表格
					self.defaultTable(null);
					
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
					
					self.ndbhEcharts_fold();
					
					 /************************************统计页面************************************/
		            $('#btn_hours_confirm').click(function(){
						var params =  $("#pvcost_select_form").serializeJson();
						params.pvcosttype_id =$.trim($('#pvcosttype_id').val());
						var lineRadio1 = $("#lineRadio1").is(':checked');
						if(lineRadio1){
							if(comm.isEmpty(params.line_id)){
								comm.alert_tip("请选择线路！");
								return false;
							}
							params.company_id=null;
							params.line_id=$.trim(params.line_id);
						}else{
							if(comm.isEmpty(params.company_id)){
								comm.alert_tip("请选择公司！");
								return false;
							}
							params.line_id=null;
							params.company_id=$.trim(params.company_id);
						}
						if(comm.isEmpty(params.startTime)){
							comm.alert_tip("请输入起始时间！");
							return false;
						}
						if(comm.isEmpty(params.endTime)){
							comm.alert_tip("请输入结束时间！");
							return false;
						}
						
						//初始化表格
						self.initTable(params);
						
						//查询图表数据
						comm.request('/report/pvcost/queryPVCostsByCondition',params,function(resp){
							self.ndbhEcharts_fold(resp.data.YEARS,resp.data.DATA);
						});
						
		            })
		            
		            
		            $('#pvcosttype_id').change(function(){
		            	$('#btn_hours_confirm').click();
		            })
		            
		            
		            $("input[name='qcndbhRadio']").click(function(){
						var lineRadio1 = $("#lineRadio1").is(':checked');
						if(lineRadio1){
							$(".qcndbh_company").hide();
							$(".qcndbh_line").show();
							self.defaultTable(null);
						}else{
							$(".qcndbh_line").hide();
							$(".qcndbh_company").show();
							self.defaultTable(null);
						}
					});
		            
		            /************************************统计页面************************************/
				},
				initPvCostTypes : function(selectId){
					comm.request('/report/pvcosttype/getPVCostTypes',null,function(resp){
						var defaultOption = '<option value=" ">全部</option>';
						comm.initSelectOptionForObj(selectId,resp.data,'PVCOSTTYPE_NAME','PVCOSTTYPE_ID',defaultOption);
					});
				},
				initDepartments : function(selectId){
					comm.request('/report/pvcost/getDepartments',null,function(resp){
						comm.initSelectOptionForObj(selectId,resp.data,'NAME','ID');
					});
				},
				initLine:function(selectId){
					comm.request('/report/lineAnalysis/queryLineDataByDepartmentId',{"departmentid":null},function(resp){
						var defaultOption = '<option value=" ">全部</option>';
						comm.initSelectOptionForObj(selectId,resp.data,'NAME','ID',defaultOption);
					});
				},
				ndbhEcharts_fold : function(xAxisData,seriesData){
					if(comm.isEmpty(xAxisData)){
						xAxisData=[];
					}
					if(comm.isEmpty(seriesData)){
						seriesData=[];
					}
					
					var myChart = echarts.init(document.getElementById('ndbhEcharts_fold'));
					option = {
					    title: {
					        text: '折线图'
					    },
					    tooltip: {
					        trigger: 'axis'
					    },
					    legend: {
					        data:['轮胎消耗费用1']
					    },
					    grid: {
					        left: '3%',
					        right: '4%',
					        bottom: '3%',
					        containLabel: true
					    },
					    toolbox: {
					        feature: {
					            saveAsImage: {}
					        }
					    },
					    xAxis: [{
					        type: 'category',
					        boundaryGap: false,
					        //data: ['2010','2011','2012','2013','2014','2015','2016']
					        data: xAxisData,
					    }],
					    yAxis: {
					        type: 'value'
					    },
					    series: seriesData
					};
					if (option && typeof option === "object") {
					    myChart.setOption(option, true);
					}
				},
				defaultTable : function(data) {
					var lineVisible = false;
					var companyVisible = false;
					var qcndbhRadio = $('input[name=qcndbhRadio]:checked')
							.val();
					if (qcndbhRadio == '0') {
						lineVisible = true;
					}
					if (qcndbhRadio == '1') {
						companyVisible = true;
					}
					self.mytable = $('#table_list_template').DataTable({
						// 每页显示三条数据
						// pageLength : 5,
						language : dataTable_cn,
						destroy : true,
						data : data,
						columns : [ {
							title : '年份',
							data : "YEAR"
						}, {
							title : '线路',
							visible : lineVisible,
							data : "LINE_NAME"
						}, {
							title : '公司',
							visible : companyVisible,
							data : "COMPANY_NAME"
						}, {
							title : '费用类型',
							data : "PVCOSTTYPE_NAME"
						}, {
							title : '费用',
							data : "MONEY"
						} ],
						select : {
							style : 'os',
							selector : 'td:first-child'
						}

					});
				},
				
				// 初始化表达
				initTable : function(param) {
					comm.requestDefault('/report/pvcost/getPVCosts',param, function(resp) {
			            if(resp.resCode == 200){
			            	var data =resp.data;
			            	self.defaultTable(data);
			            }else{
			            	comm.alert_tip(resp.msg);
			            }
					},function(resp){
						comm.alert_tip("调用失败");
					}); 
				}
				
			};
			return self;
		});
