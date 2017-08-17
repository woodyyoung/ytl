define(
		[ 'text!districtManagement/tpl/dataEntry/index_entry.html' ],
		function(tpl) {
			var self = {
				mytable : null,
				myTreeTable : null,
				mapObj:null,
				oppath:null,
				mouseTool:null,
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					
					 //初始化日历控件
		            $("#startTime").datetimepicker({
		                format: 'yyyy-mm-dd',
		                language: "zh-CN",
		                minView: 'month'
		            });
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
						 var indexId = $('#id').val();
						 var lineId = $.trim($('#test option:selected').val());
						 var yareaId = $.trim($('#yztselect option:selected').val());
						 var indexValue = $('#indexValue').val();
						 var startTime = $('#startTime').val();
						 var endTime = $('#endTime').val();
						 var params = {};
						 params.indexId = indexId;
						 params.lineId = lineId;
						 params.areaId = yareaId;
						 params.actualScore = indexValue;
						 params.startTime = startTime;
						 params.endTime = endTime;
						 if( comm.isEmpty(indexId)){
							 alert("请选择一个指标项");
							 return false;
						 }
						 if( comm.isEmpty(indexValue)){
							 alert("指标值不能为空");
							 return false;
						 }
						 if( comm.isEmpty(startTime) || comm.isEmpty(endTime)){
							 alert("日期范围请选择开始时间和结束时间");
							 return false;
						 }
						 comm.requestJson('/report/indexSourceData/save',JSON.stringify(params),function(resp){
							 if(resp.code == 0){
								 $('#indexValue').val("");
								 $('#startTime').val("");
								 $('#endTime').val("");
							 }else{
								alert(resp.msg);
							 }
						 });
						 
					 });
					 
					//初始化下拉框数据
		            comm.requestJson('/report/lineAnalysis/listAllLine', null,
		            function(resp) {
		                comm.initSelectOptionForObj('test', resp.data, 'name', 'id');
		            });
		            
		            var params = {};
		            comm.request('/report/areaAnalysis/listAllArea', params,
		            function(resp) {
		                comm.initAreaSelect('#yztselect',resp.data);
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
						var params = {id : defaultData.id };
						$("#id").val(defaultData.id);
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
						}
						
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
											}
										}
									}else{
										alert(resp.msg);
									}
						        },function(reap){
						        	alert("区域加载失败...")
						        });
					});
					 
				}
		}
			
		return self;
		});