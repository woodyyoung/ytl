define(['text!psgFlowAnalysis/tpl/GIS/passengerFlowLevel.html'],
function(tpl) {
    //Do setup work here
    var self = {
        myTable: null,
        myTreeTable : null,
        show: function() {
            $('.index_padd').html('');
            $('.index_padd').html(tpl);
            
            
            //初始化左树数据
            self.initTreeData(null,null);
            
            $('.demo2').colorpicker({
                format: 'hex'
            });
            

            
            //查询按钮
            $('#btn_psgLevel_confirm').click(function() {
                var levelName = $('#levelName').val();
                var params = {};
                params.levelName = levelName;

                comm.request('/report/passengerFlowLevel/list', params,
                function(resp) {
                    self.initTable(resp);
                    //alert(resp);
                });
            });

            //修改按钮
            $('#btn-update').click(function() {
                var rowDataId = self.getTableContent();
                if (rowDataId.split(",").length > 1 || rowDataId == null || rowDataId == '') {
                    alert("请选择单行记录");
                    return false;
                }
                require(['psgFlowAnalysis/src/GIS/psgfLevel_add'],
                function(psgfLevel_add) {
                    psgfLevel_add.show(rowDataId);
                });
                //psfLevel_add.show(rowDataId);
            });
            
            
            //新增按钮
            $('#btn-insert').click(function() {
            	require(['psgFlowAnalysis/src/GIS/psgfLevel_add'],
            			function(psgfLevel_add) {
            		psgfLevel_add.show(null);
            	});
            });
            
            //启用方案
            $('#btn-startOrCloseDelete').click(function() {
            	var dataType = $("#dataType").val();
            	var menuId = $("#menuId").val();
            	var pid = $("#pid").val();
            	if(dataType == null || dataType == ''){
					alert("请先选择方案名！");
					return false;
				}
            	var params = {};
            	params.menuId = menuId;
            	params.dataType = dataType;
            	comm.requestJson('/report/passengerFlowLevel/planClose', JSON.stringify(params), function(resp){
            		alert("方案启用成功");
            		self.initTreeData(pid,dataType);
            	},function(resp){
            		alert("方案启用失败...");
            		return false
            	});
            });
            //删除方案
            $('#btn-planDelete').click(function() {
				var dataType = $("#dataType").val();
				var menuId = $("#menuId").val();
				var pid = $("#pid").val();
				if(dataType == null || dataType == ''){
					alert("请先选择方案名！");
					return false;
				}
				var params = {};
				params.menuId = dataType;
				comm.requestDefault('/report/passengerFlowLevel/planDelete', params, function(resp){
					alert("方案删除成功");
					self.initTreeData(pid,null);
				},function(resp){
					alert("方案添加失败...");
					return false
				});
            });
            
          //确认添加方案
			$('#determineAddPlan').click(function(){
				var planName = $("#planName").val();
				var menuId = $("#menuId").val();
				var dataType = $("#dataType").val();
				var pid = $("#pid").val();
				if(menuId == null || menuId == ''){
					alert("请先填写方案名！");
					return false;
				}
				var params = {};
				params.planName = planName;
				params.menuId = menuId;
				params.dataType = dataType;
				comm.requestDefault('/report/passengerFlowLevel/planInsert', params, function(resp){
					if(resp.code == 0){
						var quid = $("#quid").val();
						self.initTableData(quid);
						alert("方案添加成功");
						$("#myModal").modal("hide"); 
						self.initTreeData(pid,dataType);
					}else{
						alert(resp.msg);
					}
				},function(resp){
					alert("方案添加失败...");
					return false
				});
				
			});
			
			//确认批量确认添加
			$('#determineAdd').click(function(){
				var planName = $.trim($("#planName").val());
				var maxData = $("#maxData").val();
				var startColor = $("#startColor").val();
				var endColor = $("#endColor").val();
				if(comm.isEmpty(planName)){
					alert("请先填写方案名称！");
					return false;
				}
				if(comm.isEmpty(maxData)){
					alert("请先填写最大值！");
					return false;
				}
				if(comm.isEmpty(startColor) || comm.isEmpty(endColor)){
					alert("请先选择颜色区间！");
					return false;
				}
				var colors = self.getBatchColor(startColor,endColor,10);
				var dataType = $("#dataType").val();
				var menuId = $("#menuId").val();
				var pid = $("#pid").val();
				var params = {};
				params.planName = planName;
				params.maxData = maxData;
				params.dataType = dataType;
				params.menuId = menuId;
				params.colors = colors;
				comm.requestJson('/report/passengerFlowLevel/batchInsert', JSON.stringify(params),  function(resp){
					if(resp.code == 0){
						alert("等级批量添加成功");
						$("#batchModal").modal("hide"); 
						self.clearValue();
						self.initTreeData(pid,null);
					}else{
						alert(resp.msg);
					}
				},function(resp){
					alert("方案添加失败...");
					return false
				});
				
			});
            
			
            
            //添加方案
            $('#btn-batchInsert').click(function() {
            	var menuId =  $.trim($("#menuId").val());
				if(menuId == null || menuId == ''){
					alert("请先选中菜单再添加方案！");
					return false;
				}
				$(this).attr('data-target','#batchModal');
            });

            //删除按钮
            $('#btn-delete').click(function() {
                var rowDataId = self.getTableContent();
                if (rowDataId.split(",").length < 1 || rowDataId == '') {
                    alert("请选择一条记录");
                    return false;
                }
                var params = {};
                params.passengerFlowLevelId = rowDataId;
                comm.request('/report/passengerFlowLevel/delete', params,
                function(resp) {
                    require(['psgFlowAnalysis/src/GIS/psgfLevel'],
                    function(psgfLevel) {
                        psgfLevel.show();
                    });
                });
                require(['psgFlowAnalysis/src/GIS/psgfLevel'],
                function(psgfLevel) {
                    psgfLevel.show();
                });
                //psfLevel_add.show(rowDataId);
            });

        },
        initTreeData: function(expandNodeId,selectedId) {
        	//初始化左树数据
        	var data = '';
        	var params = {};
            comm.requestJson('/report/passengerFlowLevel/treeList', JSON.stringify(params), 
                    function(resp) {
                   	 data = resp.data;
                   	 self.initTree(data,expandNodeId,selectedId);
                   },function(reap){
       	        	alert("等级加载失败...")
       	        });
        	
        },
        getTableContent: function() {
            var nTrs = self.myTable.rows('.selected').data();
            var retStr = "";
            if (nTrs != null && nTrs.length > 0) {
                for (var k = 0; k < nTrs.length; k++) {
                    retStr += nTrs[k].ID + ",";
                }
                retStr = retStr.substring(0, retStr.length - 1);
            }
            return retStr;
        },
        expandNode:function(data,expendNodeId){
        	for(var i = 0;i<data.length;i++){
        		var obj = data[i];
        		var nodeId = obj.id;
        		if(nodeId==expendNodeId){
        			obj.state={ expanded: true };
        		}else{
        			obj.state={ expanded: false };
        		}
        	}
        },
        selectNode:function(data,expendNodeId,selectNodeId){
        	for(var i = 0;i<data.length;i++){
        		var obj = data[i];
        		var nodeId = obj.id;
        		if(nodeId==expendNodeId){
        			if(obj.nodes!=null){
        				if(comm.isEmpty(selectNodeId)){
        					obj.nodes[0].state = {expanded: true,selected: true};
        					self.ajaxRequestTree(obj.nodes[0]);
        				}else{
        					for(var j=0;j<obj.nodes.length;j++){
        						var selectId = obj.nodes[j].id;
        						if(selectNodeId==selectId){
        							obj.nodes[j].state = {expanded: true,selected: true};
        							self.ajaxRequestTree(obj.nodes[j]);
        						}
        					}
        				}
        			}else{
        				obj.state = {expanded: true,selected: true};
    					self.ajaxRequestTree(obj);
        			}
        		}
        	}
        },
        initTree : function(data,expendNodeId,selectNodeId) {
        	if(comm.isEmpty(expendNodeId)){
        		data[0].state={ expanded: true };
        		self.expandNode(data,data[0].id);
        		if(data[0].nodes!=null){
        			self.selectNode(data,data[0].id,data[0].nodes[0].id);
        		}else{
        			self.selectNode(data,data[0].id,data[0].id);
        		}
        		
        	}else{
        		self.expandNode(data,expendNodeId);
        		self.selectNode(data,expendNodeId,selectNodeId);
        	}
        	
        	self.myTreeTable = $('#treeview-searchable').treeview({
          		 levels: 1,
          		 showBorder: false,
          		 showTags: true,
          		 data:data
          	 });
        /*	if(comm.isEmpty(expendNodeId)){
        		if(data[0].level == 1){
        			var id = data[0].arg;
        			$("#menuId").val(id);
        		}else{
        			$("#menuId").val("");
        		}
        	}else{
        		
        	}*/
			//self.initTableData(id);
			//左树点击事件
			$("#treeview-searchable").on("nodeSelected",function(event,defaultData){
				self.ajaxRequestTree(defaultData);
			});
			 
		},
		ajaxRequestTree:function(defaultData){
			$("#dataType").val(defaultData.arg);
			$("#pid").val(defaultData.pid);
			if(defaultData.level == 1){
				$("#menuId").val(defaultData.id);
			}else{
				$("#menuId").val("");
			}
			var params = {};
			params = {
	                "dataType": defaultData.arg
	            }
			comm.requestJson('/report/passengerFlowLevel/list',
					JSON.stringify(params), 
					function(resp) {
						if(resp){
							self.initTable(resp);
						}
			        },function(reap){
			        	alert("等级加载失败...")
			        });
		},
		
        initTableData: function(data) {
            var params = {};
            params = {
	                "dataType": data
	            }
            comm.requestJson('/report/passengerFlowLevel/list', JSON.stringify(params), 
            function(resp) {
                self.initTable(resp);
            });
        },
        initTable: function(data) {
            self.myTable = $('#tab_psgLevelList').DataTable({
                //每页显示三条数据
                //pageLength : 5,
                destroy: true,
                data: data,
                language: dataTable_cn,
                columns: [{
                    data: null,
                    defaultContent: '',
                    className: 'select-checkbox',
                    orderable: false
                },
                /*{
                    title: '所属菜单',
                    defaultContent: '',
                    data: 'DATATYPE',
                    render: function(data, type, row, meta) {
                    	switch(data){
                    	case '001':
                    		return "站点客流展示";
                    	case '002':
                    		return "线路客流展示";
                    	case '003':
                    		return "小区客流展示";
                    	case '004':
                    		return "断面客流展示";
                    	case '005':
                    		return "公交站台客流预测";
                    	case '006':
                    		return "交通小区客流预测";
                    	case '007':
                    		return "路段断面客流预测";
                    	case '008':
                    		return "客运走廊分析";
                    	case '009':
                    		return "线网客流统计分析";
                    	default:
                    		return "";
                    	
                    	}
                    }
                },*/
                {
                    title: '等级名称',
                    defaultContent: '',
                    data: 'LEVELNAME'
                },
                {
                    title: '颜色',
                    defaultContent: '',
                    data: 'CIRCLECOLOR',
                    render: function(data, type, row, meta) {
                        return "<div class='colorDiv' style='background:" + data + "'></div>";
                    }
                },
                {
                    title: '大小',
                    defaultContent: '',
                    data: "CIRCLESIZE"
                },
                {
                    title: '范围下限',
                    defaultContent: '',
                    data: "MINDATA"
                },
                {
                    title: '范围上限',
                    defaultContent: '',
                    data: "MAXDATA"
                },
                {
                    title: '备注',
                    defaultContent: '',
                    data: "REMARK"
                },
                {
                    title: '是否启用',
                    defaultContent: '',
                    render: function(data, type, row, meta) {
                        if (row.ISDISABLE == '1') {
                            return "启用";
                        } else {
                            return "禁用";
                        }
                    }
                }],
                select: {
                    style: 'os',
                    selector: 'td:first-child'
                }

            });
        },
        clearValue:function(){
        	$("#planName").val("");
			$("#maxData").val("");
			$("#startColor").val("");
			$("#endColor").val("");
        },
        getBatchColor:function(startColor, endColor, step){
        	/*
        	  // startColor：开始颜色hex
        	  // endColor：结束颜色hex
        	  // step:几个阶级（几步）
        	  */
        	    startRGB = this.colorRgb(startColor); //转换为rgb数组模式
        	    startR = startRGB[0];
        	    startG = startRGB[1];
        	    startB = startRGB[2];

        	    endRGB = self.colorRgb(endColor);
        	    endR = endRGB[0];
        	    endG = endRGB[1];
        	    endB = endRGB[2];

        	    sR = (endR - startR) / step; //总差值
        	    sG = (endG - startG) / step;
        	    sB = (endB - startB) / step;

        	    var colorArr = [];
        	    for (var i = 0; i < step; i++) {
        	        //计算每一步的hex值
        	        var hex = self.colorHex('rgb(' + parseInt((sR * i + startR)) + ',' + parseInt((sG * i + startG)) + ',' + parseInt((sB * i + startB)) + ');');
        	        colorArr.push(hex);
        	    }
        	    return colorArr;

        },
       colorRgb:function(sColor){
   	    var reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
	    var sColor = sColor.toLowerCase();
	    if (sColor && reg.test(sColor)) {
	        if (sColor.length === 4) {
	            var sColorNew = "#";
	            for (var i = 1; i < 4; i += 1) {
	                sColorNew += sColor.slice(i, i + 1).concat(sColor.slice(i, i + 1));
	            }
	            sColor = sColorNew;
	        }
	        //处理六位的颜色值
	        var sColorChange = [];
	        for (var i = 1; i < 7; i += 2) {
	            sColorChange.push(parseInt("0x" + sColor.slice(i, i + 2)));
	        }
	        return sColorChange;
	    } else {
	        return sColor;
	    }
	},
	colorHex:function(rgb){
	    var _this = rgb;
	    var reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
	    if (/^(rgb|RGB)/.test(_this)) {
	        var aColor = _this.replace(/(?:(|)|rgb|RGB)*/g, "").split(",");
	        var strHex = "#";
	        for (var i = 0; i < aColor.length; i++) {
	            var hex = Number(aColor[i]).toString(16);
	            hex = hex < 10 ? 0 + '' + hex: hex; // 保证每个rgb的值为2位
	            if (hex === "0") {
	                hex += hex;
	            }
	            strHex += hex;
	        }
	        if (strHex.length !== 7) {
	            strHex = _this;
	        }
	        return strHex;
	    } else if (reg.test(_this)) {
	        var aNum = _this.replace(/#/, "").split("");
	        if (aNum.length === 6) {
	            return _this;
	        } else if (aNum.length === 3) {
	            var numHex = "#";
	            for (var i = 0; i < aNum.length; i += 1) {
	                numHex += (aNum[i] + aNum[i]);
	            }
	            return numHex;
	        }
	    } else {
	        return _this;
	    }
	}
	

    };
    return self;
});