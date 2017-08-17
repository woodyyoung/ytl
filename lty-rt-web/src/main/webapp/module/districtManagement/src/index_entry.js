define(
		[ 'text!districtManagement/tpl/index_entry.html',
		  'text!districtManagement/tpl/index_level.html', 
		  'text!districtManagement/tpl/index_add.html',],
		function(tpl,indexLevel,addTpl) {
			var self = {
				mytable : null,
				myTreeTable : null,
				mapObj:null,
				oppath:null,
				mouseTool:null,
				indexId:null,
				indexName:null,
				indexType:null,
				indexUnit:null,
				hasNodes:false,
				
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					
					 //初始化日历控件
		            $("#startTime").datetimepicker({
		                format: 'yyyy-mm-dd',
		                language: "zh-CN",
		                minView: 'month'
		            });
		            $("#startTime").val(moment().startOf('year').format('YYYY-MM-DD'));
		            $("#endTime").val(moment().format('YYYY-MM-DD'));
		            $("#endTime").datetimepicker({
		                format: 'yyyy-mm-dd',
		                language: "zh-CN",
		                minView: 'month'
		            });

		            
		            var lineDiv = $("#lineDiv");
					var qyDiv = $("#qyDiv");
					lineDiv.hide();
					qyDiv.hide();
					//初始化左树
					self.initLeftTree();
					
					//新增按钮
					 $('#btn-insert').click(function(){
						self.addIndexData();
					 });
					 
					 //修改
					 $('#btn-update').click(function(){
						 self.updateIndexData();
					 });
					 
					//查询
					 $('#btn-query').click(function(){
						 var startTime = $('#startTime').val();
						 var endTime = $('#endTime').val();
						 if(startTime == null || startTime == ''){
							 alert("请选择开始时间再点击查询。");
							 return false;
						 }
						 if(endTime == null || endTime == ''){
							 alert("请选择结束时间再点击查询。");
							 return false;
						 }
						 /*if(self.indexType == '005'){
							$('#lineSearchDiv').show();
							$('#qySearchDiv').hide();
						}else if(self.indexType == '006'){
							$('#lineSearchDiv').hide();
							$('#qySearchDiv').show();
						}else{
							$('#lineSearchDiv').hide();
							$('#qySearchDiv').hide();
							$('#lineSearchSelect').val("");
							$('#qySearchSelect').val("");
						}*/
						 var params = {};
						 params.beginDate = startTime;
						 params.endDate = endTime;
						 self.initTableBefore(params);
					 });
					 
					 //删除
					 $('#btn-del').click(function(){
						 var rowDataId = self.getTableContentId();
						 if(rowDataId.split("_").length < 1 || rowDataId == ''){
							 comm.alert_tip("请选择一条记录");
							 return false;
						 }	
						comm.requestJson('/report/indexSourceData/delByIndexIdAndDate', rowDataId,  function(resp){
							if(resp.code == 0){
								self.initTableBefore(null);
								comm.alert_tip("删除成功");
							}else{
								comm.alert_tip(resp.msg);
							}
						},function(resp){
							comm.alert_tip("删除失败...");
							return false
						});
					 });
					 
					 $('#determineAdd').click(function(){
							var planIndexId = $.trim($("#planIndexId").val());
							var planIndexDate = $("#planIndexDate").val();
							var planMaxData = $("#planMaxData").val();
							var planTotalData = $("#planTotalData").val();
							if(comm.isEmpty(planMaxData)){
								alert("请先填写统计数量！");
								return false;
							}
							if(comm.isEmpty(planTotalData)){
								alert("请先填写统计总数！");
								return false;
							}
							
							var params = {};
							params.indexId = planIndexId;
							params.countDate = planIndexDate;
							params.indexNum = planMaxData;
							params.indexTotalNum = planTotalData;
							comm.requestJson('/report/indexSourceData/update', JSON.stringify(params),  function(resp){
								if(resp.code == 0){
									alert("修改成功");
									$("#batchModal").modal("hide");
									var startTime = $('#startTimeTable').val();
									var endTime = $('#endTimeTable').val();
									var paramsAfter = {};
									paramsAfter.beginDate = startTime;
									paramsAfter.endDate = endTime;
									self.initTableBefore(paramsAfter);
								}else{
									alert(resp.msg);
								}
							},function(resp){
								alert("修改失败...");
								return false
							});
							
						});
					 
					 	//初始化下拉框数据
			            comm.requestJson('/report/lineAnalysis/listAllLine', null,
			            function(resp) {
			                comm.initSelectOptionForObj('lineSearchSelect', resp.data, 'name', 'id');
			            });
			            
			            var params = {};
			            comm.request('/report/areaAnalysis/listAllArea', params,
			            function(resp) {
			                comm.initAreaSelect('#qySearchSelect',resp.data);
			            });
					

					
				},
				
				updateIndexData:function(){
					if(comm.isEmpty(self.indexId)){
						 comm.alert_tip("请选择一个指标项");
						 return false;
					}
					 var rows = self.mytable.rows('.selected').data();
					 if(rows.length != 1){
						 comm.alert_tip('请选择一条要修改的数据！');
						 return false;
					 }
					$('#dialogDiv').html('');
					$('#dialogDiv').html(_.template(addTpl, {}));
					$('#add_index_name').val(self.indexName);
					$('#add_index_unit').text(self.indexUnit);
					$('#add_index_val').val(rows[0].ACTUAL_SCORE);
					$('#input_remark').val(rows[0].REMARK);
					if(self.indexType == '005'){
						$('#lineDiv').show();
						$('#qyDiv').hide();
						$('#lineSelect').html('<option>'+rows[0].LINENAME+'</option>');
			          
					}else if(self.indexType == '006'){
						$('#lineDiv').hide();
						$('#qyDiv').show();
						$('#qySelect').html('<option>'+rows[0].AREANAME+'</option>');
					}else{
						$('#lineDiv').hide();
						$('#qyDiv').hide();
						$('#lineSelect').val("");
						$('#qySelect').val("");
					}
					$("#startTime1").val(rows[0].COUNT_DATE);
					$("#startTime1").attr('readOnly','readOnly');
					$("#startTime1").parent().prev().text('日期');
					$("#endTime1").parent().parent().hide();
					
					$("#addIndexDataModal").modal({
						keyboard: true
					});
					$('#addIndexDataModal').modal('show');
					//绑定保存事件
					$('#btn_add_IndexData').unbind('click');
					$('#btn_add_IndexData').click(function(){
						self.saveUpdateIndexData(rows[0].ID);
					});
					
					
				},
				saveUpdateIndexData:function(id){
					 var indexValue = $('#add_index_val').val();
					 var remark = $('#input_remark').val();
					 var params = {};
					 params.id = id;
					 params.actualScore = indexValue;
					 params.inputPerson  = comm.userInfo.realName;
					 params.remark  = remark;
					 if( comm.isEmpty(indexValue)){
						 comm.alert_tip("指标值不能为空");
						 return false;
					 }
					 $('#addIndexDataModal').modal('hide');
					 $('.modal-backdrop').remove();
					 comm.requestJson('/report/indexSourceData/updateIndexData',JSON.stringify(params),function(resp){
						 if(resp.code == 0){
							 comm.alert_tip('操作成功！');
							 $('#btn-query').click();
						 }else{
							 comm.alert_tip('操作失败！'+resp.msg);
						 }
					 });
				},
				
				
				addIndexData:function(){
					if(comm.isEmpty(self.indexId)){
						 comm.alert_tip("请选择一个指标项");
						 return false;
					}
					if(self.hasNodes){
						 comm.alert_tip("不能对顶级指标进行数据录入！");
						 return false;
					}
					$('#dialogDiv').html('');
					$('#dialogDiv').html(_.template(addTpl, {}));
					$('#add_index_name').val(self.indexName);
					$('#add_index_unit').text(self.indexUnit);
					if(self.indexType == '005'){
						$('#lineDiv').show();
						$('#qyDiv').hide();
						//初始化下拉框数据
			            comm.requestJson('/report/lineAnalysis/listAllLine', null,
			            function(resp) {
			                comm.initSelectOptionForObj('lineSelect', resp.data, 'name', 'id');
			            });
			            
			          
					}else if(self.indexType == '006'){
						$('#lineDiv').hide();
						$('#qyDiv').show();
						var params = {};
			            comm.request('/report/areaAnalysis/listAllArea', params,
			            function(resp) {
			                comm.initAreaSelect('#qySelect',resp.data);
			            	$('#qySelect').next().css('width','250px');
			            });
					}else{
						$('#lineDiv').hide();
						$('#qyDiv').hide();
						$('#lineSelect').val("");
						$('#qySelect').val("");
					}
					$("#startTime1").datetimepicker({
		                format: 'yyyy-mm-dd',
		                language: "zh-CN",
		                minView: 'month'
		            });
		            $("#endTime1").datetimepicker({
		                format: 'yyyy-mm-dd',
		                language: "zh-CN",
		                minView: 'month'
		            });
					
					$("#addIndexDataModal").modal({
						keyboard: true
					});
					$('#addIndexDataModal').modal('show');
					//绑定保存事件
					$('#btn_add_IndexData').unbind('click');
					$('#btn_add_IndexData').click(function(){
						self.saveIndexData();
					});
					
					
				},
				
				saveIndexData:function(){
					 var lineId = $.trim($('#lineSelect option:selected').val());
					 var yareaId = $.trim($('#qySelect option:selected').val());
					 var indexValue = $('#add_index_val').val();
					 var rangeValue = $('#add_index_val_range').val();
					 var startTime = $('#startTime1').val();
					 var endTime = $('#endTime1').val();
					 var remark = $('#input_remark').val();
					 var params = {};
					 params.indexId = self.indexId;
					 if(comm.isNotEmpty(lineId)){
						 params.lineId = lineId;
					 }
					 if(comm.isNotEmpty(yareaId)){
						 params.areaId = yareaId;
					 }
					 params.actualScore = indexValue;
					 params.startTime = startTime;
					 params.endTime = endTime;
					 params.inputPerson  = comm.userInfo.userName;
					 params.remark  = remark;
					 params.rangeValue  = rangeValue;
					 if(comm.isEmpty(startTime) || comm.isEmpty(endTime)){
						 comm.alert_tip("日期范围请选择开始时间和结束时间");
						 return false;
					 }
					 if(moment(startTime).isAfter(moment(endTime))){
						 comm.alert_tip("开始日期不能大于结束日期");
						 return false;
					 }
					 if( comm.isEmpty(indexValue)){
						 comm.alert_tip("指标值不能为空");
						 return false;
					 }
					 $('#addIndexDataModal').modal('hide');
					 $('.modal-backdrop').remove();
					 comm.requestJson('/report/indexSourceData/save',JSON.stringify(params),function(resp){
						 if(resp.code == 0){
							 comm.alert_tip('操作成功！');
							 $('#btn-query').click();
						 }else{
							 comm.alert_tip('操作失败！'+resp.msg);
						 }
					 });
				},
				
				initLeftTree : function(indexType) {
					comm.requestJson('/report/index/getIndexTree', null,
							function(resp) {
								if(resp.code == 0){
									self.initTree(resp.data);
								}else{
									alert(resp.msg);
								}
								
							},function(resp){
								alert("树加载失败...")
							});
				},
				initTree : function(data) {
					self.myTreeTable = $('#treeview-searchable').treeview({
						levels: 1,
			            showBorder: false,
			            showTags: true,
			            data:data
					});
					
					//左树点击事件
					$("#treeview-searchable").on("nodeSelected",function(event,defaultData){
						self.indexId = defaultData.id;
						self.indexName = defaultData.text;
						self.indexType =  defaultData.id.substr(0,3);
						if(defaultData.nodes&&defaultData.nodes.length>0){
							self.hasNodes = true;
						}else{
							self.hasNodes = false;
						}
						var params = {id : defaultData.id };
						/*$("#id").val(defaultData.id);
						if(defaultData.levels == 1){
							$('#description').text(""); 
							$('#levelUnit').text(""); 
							return;
						}
						var lineDiv = $("#lineDiv");
						var qyDiv = $("#qyDiv");
						var indexType = defaultData.id.substr(0,3);
						if(indexType == '005'){
							lineDiv.show();
							qyDiv.hide();
							$('#yztselect').val("");
						}else if(indexType == '006'){
							lineDiv.hide();
							qyDiv.show();
							$('#test').val("");
						}else{
							lineDiv.hide();
							qyDiv.hide();
							$('#test').val("");
							$('#yztselect').val("");
						}*/
						
						comm.requestJson('/report/indexSourceData/getDescription',
								JSON.stringify(params), 
								function(resp) {
									if(resp.code == 0){
										if(resp.data){
											if(resp.data.descriptions){
												$('#description').text(resp.data.descriptions); 
											}else{
												$('#description').text('指标说明没有维护，请维护指标说明!'); 
											}
											if(resp.data.levelUnit){
												$('#levelUnit').text("("+resp.data.levelUnit+") "); 
												self.indexUnit = resp.data.levelUnit;
											}
											/*if(resp.data.indexLevelList){
												$('#index_level').html(_.template(indexLevel, resp.data));
											}*/
										}
									}else{
										alert(resp.msg);
									}
						        },function(reap){
						        	alert("指标说明加载失败...")
						        });
						//self.initTableBefore(null);
						$('#btn-query').click();
						$('#startTimeTable').val("");
						$('#endTimeTable').val("");
					});
					
					//左树取消点击事件
					$("#treeview-searchable").on("nodeUnselected",function(event,defaultData){
						self.indexId = '';
						self.indexName = '';
						self.indexType =  '';
						self.indexUnit ='';
					});
					 
				},
				initTableBefore : function(data){
					var params = {};
					if(data != null){
						params = data;
					}
					params.indexId = self.indexId;
					comm.requestJson('/report/indexSourceData/searchIndexData',
					JSON.stringify(params), 
					function(resp) {
						if(resp.code == 0){
							self.initTable(resp.data, self.indexId);
						}else{
							comm.alert_tip(resp.msg);
						}
			        },function(reap){
			        	comm.alert_tip("指标历史记录加载失败...")
			        });
				},
				initTable : function(data, indexid) {
					var lineVisible = false;
					if(self.indexType == '005'){
						lineVisible = true;
					}
					var areaVisible = false;
					if(self.indexType == '006'){
						areaVisible = true;
					}
					self.mytable = $('#entry_example').DataTable({
					
						//每页显示三条数据
						//pageLength : 5,
						language:dataTable_cn,
						destroy: true,
						data: data,
						columns : [
						{
							data: null,
		                    defaultContent: '',
		                    className: 'select-checkbox',
		                    orderable: false
						},
						/*{
							title:'指标ID',
							data : 'INDEXID'
						},*/ {
							title:'指标值',
							data : 'ACTUAL_SCORE'
						},{
							title:'日期',
							data : "COUNT_DATE"
						},{
							title:'录入时间',
							data : "CREATETIME",
							render: function(data, type, row, meta) {
					            return data.substr(0,10);
					        }
						},{
							title:'线路',
							data : "LINENAME",
							visible:lineVisible,
							render: function(data, type, row, meta) {
								 if(comm.isNotEmpty(data)){
						        	   return data;
						           }
						           return '';
					        }
						},{
							title:'区域',
							data : "AREANAME",
							visible:areaVisible,
							render: function(data, type, row, meta) {
								 if(comm.isNotEmpty(data)){
						        	   return data;
						           }
						           return '';
					        }
						},{
							title:'录入人',
							data : "INPUT_PERSON",
							render: function(data, type, row, meta) {
								   if(comm.isNotEmpty(data)){
						        	   return data;
						           }
						           return '';
					        }
						},{
							title:'备注',
							data : "REMARK",
							render: function(data, type, row, meta) {
					           if(comm.isNotEmpty(data)){
					        	   return data;
					           }
					           return '';
					        }
						}/*,{
							title:'统计数量',
							data : "indexNum"
						},{
							title:'统计总数',
							data : "indexTotalNum"
						},{
							title:'实际指标等级',
							data : "actualLevel"
						},{
							title:'实际得分',
							data : "actualScore"
						}*/],
						select: {
		                      style:    'os',
		                      selector: 'td:first-child'
		                      }

					});
				},
				getTableContentId : function() {
					var nTrs = self.mytable.rows('.selected').data();
					var retStr = "";
					if (nTrs != null && nTrs.length > 0) {
						for (var k = 0; k < nTrs.length; k++) {
							retStr += nTrs[k].ID + ",";
						}
						retStr = retStr.substring(0, retStr.length - 1);
					}
					return retStr;
				}
		}
			
		return self;
		});