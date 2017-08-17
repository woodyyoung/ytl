define([ 'text!districtManagement/tpl/dm_ldztgl.html' ], function(tpl) {
	var self = {
		mytable : null,
		myTreeTable : null,
		myplatTable : null,
		show : function() {
			$('.index_padd').html('');
			$('.index_padd').html(tpl);
			
			//初始化左树
			self.initLeftTree();
			
			// 初始化列表数据
			//self.initTableData();
			
			//添加按钮
			$('#btn-add').click(function(){
				var zhldid = $("#zhldid").val();
				if(zhldid == null || zhldid == ''){
					alert("请先选中路段再添加站台！");
					return false;
				}
				self.initLineAjax();
				$(this).attr('data-target','#myModal');
				//data-target="#myModal"
			});
			
			//确认添加站台
			$('#determineAdd').click(function(){
				var zhldid = $("#zhldid").val();
				if(zhldid == null || zhldid == ''){
					alert("请先选中路段再添加站台！");
					return false;
				}
				
				//获取选中的站台id
				var rowDataId = self.getAddTableContent();
				if(rowDataId.split(",").length < 1 || rowDataId == ''){
					alert("请选择一条记录");
					return false;
				}
				
				var params = {};
				params.stretchid = zhldid;
				params.platFormId = rowDataId;
				comm.requestJson('/report/stretchPlatformManagement/insertFromPlat', JSON.stringify(params), function(resp){
					if(resp.code == 0){
						var ldid = $("#ldid").val();
						self.initTableData(ldid);
						alert("添加站台成功");
						$("body").removeClass("modal-open");
			        	$(".modal-backdrop").remove();
			        	$("#myModal").removeClass("in").css({"display":"none"})
					}else{
						alert(resp.msg);
					}
				},function(resp){
					alert("添加站台失败...");
					return false
				});
				
			});
			
			//删除按钮
			$('#btn-del').click(function(){
				var ldid = $('#ldid').val();
				var rowDataId = self.getTableContent();
				if(rowDataId.split(",").length < 1 || rowDataId == ''){
					alert("请选择一条记录");
					return false;
				}
				var params = {};
				params.ldid = ldid;
				params.platFormId = rowDataId;
				comm.requestJson('/report/stretchPlatformManagement/delByIds', JSON.stringify(params), function(resp){
					if(resp.code == 0){
						self.initTableData(ldid);
						alert("删除成功");
					}else{
						alert(resp.msg);
					}
				},function(resp){
					alert("删除失败...")
				});
			 });

		},

		
		initLeftTree : function() {
			comm.requestJson('/report/stretchManagement/getDistrictTree', null,
					function(resp) {
						if(resp.code == 0){
							self.initTree(resp.data);
						}else{
							alert(resp.msg);
						}
					},function(resp){
						alert("区域树加载失败...")
					});
		},
		initTree : function(data) {
			self.mytable = $('#treeview-searchable').treeview({
				levels: 1,
	            showBorder: false,
	            showTags: true,
	            data:data
			});
			//左树点击事件
			$("#treeview-searchable").on("nodeSelected",function(event,defaultData){
				$("#zhldid").val(defaultData.arg+","+defaultData.id);
				$("#ldid").val(defaultData.arg);
				comm.requestJson('/report/stretchPlatformManagement/selectStretchPlat',
						defaultData.arg, 
						function(resp) {
							if(resp.code == 0){
								self.initTable(resp.data); 
							}else{
								alert(resp.msg);
							}
				        },function(reap){
				        	alert("站台加载失败...")
				        });
			});
			
		},
		initTableData : function(data) {
			comm.requestJson('/report/stretchPlatformManagement/selectStretchPlat',
					data, 
					function(resp) {
						if(resp.code == 0){
							 self.initTable(resp.data); 
						}else{
							alert(resp.msg);
						}
			        },function(reap){
			        	alert("站台加载失败...")
			        });
		},
		initTable : function(data) {
			self.mytable = $('#ldztgl_example').DataTable({
				// 每页显示三条数据
				// pageLength : 5,
				destroy : true,
				data : data,
				order : [ [ 1, "desc" ] ],
				language : dataTable_cn,
				columns : [ {
					data : null,
					defaultContent : '',
					className : 'select-checkbox',
					orderable : false
				}, {
					title : '编号',
					data : 'numbered'
				}, {
					title : '站点名称',
					data : 'name'
				}, {
					title : '描述',
					data : "described"
				}, {
					title : '方向',
					data : "direct"
				}, {
					title : '备注',
					data : "remark"
				}],
				select : {
					style : 'os',
					selector : 'td:first-child'
				}

			});
		},
		initLineAjax : function(){
			comm.requestJson('/report/stretchPlatformManagement/getPlatFormList', null,
					function(resp) {
						if(resp.code == 0){
							self.initPlat(resp.data);
						}else{
							alert(resp.msg);
						}
					},function(resp){
						alert("区域树加载失败...")
					});
		
		},
		initPlat : function(data){
			self.myplatTable = $('#addPlatForm').DataTable({
				// 每页显示三条数据
				// pageLength : 5,
				destroy : true,
				data : data,
				order : [ [ 1, "desc" ] ],
				language : dataTable_cn,
				columns : [ {
					data : null,
					defaultContent : '',
					className : 'select-checkbox',
					orderable : false
				}, {
					title : '编号',
					data : 'numbered'
				}, {
					title : '站点名称',
					data : 'name'
				}, {
					title : '描述',
					data : "described"
				}, {
					title : '方向',
					data : "direct"
				}, {
					title : '备注',
					data : "remark"
				}],
				select : {
					style : 'os',
					selector : 'td:first-child'
				}
			});
		},
		getAddTableContent : function() {  
			var nTrs = self.myplatTable.rows('.selected').data();
			var retStr = "";
		       
			if(nTrs != null && nTrs.length > 0){
				for(var k = 0; k < nTrs.length; k++){
					retStr += nTrs[k].platFormId +",";
				}
				retStr = retStr.substring(0,retStr.length-1);
		       }
			return retStr;
		},
		getTableContent : function() {  
			var nTrs = self.mytable.rows('.selected').data();
			var retStr = "";
		       
			if(nTrs != null && nTrs.length > 0){
				for(var k = 0; k < nTrs.length; k++){
					retStr += nTrs[k].platFormId +",";
				}
				retStr = retStr.substring(0,retStr.length-1);
		       }
			return retStr;
		}

	};
	return self;

});