

window.comm = {
	userInfo:null,//缓存用户信息
	initSelectOptionForArray:function(selectId,data){
		var str ='';
		for(var i=0;i<data.length;i++){
			str+='<option value='+data[i]+'>'+data[i]+'</option>';
		}
		
		$('#'+selectId).html(str);
		$('#'+selectId).select2({placeholder: "请选择"});
	},
	initLevelDiv:function(data){
		$(".linePassengerFlowSign_ul").empty();
			for (var i = 0; i < data.length; i++) {
				var li = "<li><span style='background:"+data[i].circlecolor+"'></span>"+data[i].mindata+" - "+ data[i].maxdata + "</li>";
				$(".linePassengerFlowSign_ul").append(li);
			}
		comm.hideOrShowLevel();
    },
	//隐藏按钮绑定
	hideOrShowLevel:function(){
		var A_W = $(".linePassengerFlow_sign").width();
		$(".passenger_rightBtn_click").css("right",A_W);
		$(".passenger_rightBtn_toggle").click(function() {
			var linePassengerFlow_sign = $(".linePassengerFlow_sign");
			if (linePassengerFlow_sign.is(":hidden")) {
				linePassengerFlow_sign.show();
				$(".passenger_rightBtn_toggle").addClass("passenger_btn_open").removeClass("passenger_btn_close");
				$(".passenger_rightBtn_click").css("right",A_W);
			} else {
				linePassengerFlow_sign.hide();
				$(".passenger_rightBtn_toggle").addClass("passenger_btn_close").removeClass("passenger_btn_open");
				$(".passenger_rightBtn_click").css("right",0);
			}
		});
	},
	
	/**
	 * 初始化下拉菜单
	 * selectId 下拉菜单控件ID
	 * data 下拉菜单内容数据 json格式 [{"id":"7d7422284b694237a87df0f04ffa7b9e","name":"332路"},{"id":"7d7422284serer37a8ee7df0f04ffa7b9e","name":"225路"}]
	 * filedName option Text对应数据的属性名称
	 * fliedValue  option Value对应数据的属性名称
	 * defaultOption 默认添加的option 格式如：<option value="">全部</option>
	 */
	initSelectOptionForObj:function(selectId,data,filedName,fliedValue,defaultOption,selectOption){
		var str ='';
		if(defaultOption){
			str += defaultOption;
		}
		for(var i=0;i<data.length;i++){
			if(comm.isNotEmpty(selectOption)&&selectOption==data[i][filedName]){
				str+='<option selected="selected" value='+data[i][fliedValue]+'>'+data[i][filedName]+'</option>';
			}else{
				str+='<option value='+data[i][fliedValue]+'>'+data[i][filedName]+'</option>';
			}
		
		}
		
		$('#'+selectId).html(str);
		$('#'+selectId).select2({placeholder: "请选择"});
	},

	/**
	 * 初始化小区下拉菜单
	 * 
	 * @param selectID
	 *            下拉控件ID
	 * @param data
	 *            下拉树菜单数据源
	 */
	initAreaSelect : function(selectID, data) {
		for ( var i = 0; i < data.length; i++) {
			var obj = data[i];
			obj.id = obj.codeid;
		}
		comm.initLevelSelect(selectID, data, 'parentcodeid', 'codeid',
				'codename', 'levels');
	},
	
	/**
	 * 站台下拉框初始化
	 * @param selectID
	 */
	initPlatSelect : function(selectID,allFlag) {
		//请求站台数据
		var selectParams = {};
        comm.requestJson('/report/myPassengerFlow/selectStretchAndPlat', JSON.stringify(selectParams),
        function(resp) {
        	var data = resp;
        	var treeData = [];
        	if(allFlag){
	    		var allNode ={};
	    		allNode.id=' ',
	    		allNode.name = '所有';
	    		allNode.pid = -1;
	    		allNode.level = 0;
	    		treeData.push(allNode);
        	}
    		
    		for ( var i = 0; i < data.length; i++) {
    			var obj = data[i];
    			obj.text = obj.name;
    			comm.bulidPaltTreeData(obj,treeData);
    		}
    		
    		var sourcedata=treeData.map(function(item){
    			   item.text=item['name'];   
    			   return item;
    		});
    		
    		$(selectID).select2({
    		   // allowClear: true,
    		    //width: '400px',
    		    data: treeData, 
    			templateResult:function(item) { 
    		       var $result = $('<span style="padding-left:' + (20 * item['level']) + 'px;" disabled>' + item['name'] + '</span>');
    		       return $result;
    		    }
    		});
    		
    	
    		$(selectID).on("select2:open",function(e){
    			$("input[type=search]").val('');
    			//$("input[type=search]").trigger('click');
    			$("input[type=search]").trigger('keyup');
    			
    			$("input[type=search]").unbind();
        		$("input[type=search]").keyup(function(e){
        			var keyword = $.trim($(this).val());
        			comm.searchStation(keyword);
        		});
    		});
    		
            
        });
	},
	
	searchStation:function(keyword){
		var selectParams = {};
		selectParams.keywords = keyword;
        comm.requestJson('/report/myPassengerFlow/selectStretchAndPlat', JSON.stringify(selectParams),
        function(resp) {
        	var data = resp;
        	var treeData = [];
        	
    		for ( var i = 0; i < data.length; i++) {
    			var obj = data[i];
    			obj.text = obj.name;
    			comm.bulidPaltTreeData(obj,treeData);
    		}
    		var str = '';
    		
    		for(var i = 0;i<treeData.length;i++){
    			var obj = treeData[i];
    			if(obj.level==0){
    				str+='<li class="select2-results__option"  role="treeitem"  onclick=""  aria-disabled="true"><span style="padding-left:0px;" disabled="">'+obj.name+'</span></li>';
    			}
    			if(obj.level==1){
    				str+='<li class="select2-results__option liselected" treeid="'+obj.id+'" role="treeitem"   aaria-selected="true"><span style="padding-left:20px;" disabled="">'+obj.name+'</span></li>';
    			}
    			if(obj.level==2){
    				str+='<li class="select2-results__option" role="treeitem"   aria-disabled="true"><span style="padding-left:40px;" disabled="">'+obj.name+'</span></li>';
    			}
    		}
    		$('.select2-results__options').html(str);
    		var selectID = $('.select2-results__options').attr('id');
    		selectID = '#'+selectID.split('-')[1];
    		comm.closeLoading();
    		$('.liselected').unbind();
    		$('.liselected').click(function(e){
    			var id = $(this).attr('treeid');
    			$(selectID).val(id).select2(); 
    		});
    		$('.liselected').mouseover(function(e){
    			var id = $(this).css({"color":"white","background": "#5897fb" });
    		});
    		$('.liselected').mouseout(function(e){
    			var id = $(this).css({"color":"","background": "" });
    		});
        });
	},
	
	
	bulidPaltTreeData:function(obj,treeData){
		treeData.push(comm.buildNode(obj));
		if(obj.list!=null&&obj.list.length>0){
			for ( var i = 0; i < obj.list.length; i++) {
				var o = obj.list[i];
				o.text = o.name;
				comm.bulidPaltTreeData(o,treeData);
			}
		}
	},
	
	buildNode:function(obj){
		var node = {};
		node.name = obj.name;
		node.id = obj.id;
		node.pId = obj.pid;
		if(obj.level==null){
			node.level = 2;
			node.disabled=true;
		}else if(obj.level==0){
			node.level = 0;
			node.disabled=true;
		}
		else{
			node.level = parseInt(obj.level);
		}
		return node;
	},
	
	/**
	 * 下拉树菜单
	 * 
	 * @param selectID
	 *            下拉控件ID
	 * @param data
	 *            下拉树菜单数据源
	 * @param parentMenuAttrName
	 *            父级菜单ID 对应的属性名
	 * @param menuIdAttrName
	 *            菜单ID 对应的属性名
	 * @param menuTextAttrName
	 *            菜单文本值对应的属性名
	 * @param menuLevelAttrName
	 *            菜单级别对应的属性名
	 */
	initLevelSelect:function(selectID,data,parentMenuAttrName,menuIdAttrName,menuTextAttrName,menuLevelAttrName){
		var sourcedata=data.map(function(item){
			   item.text=item[menuTextAttrName];   
			   return item;
		});
		var newdata=[];
		comm.createFirstLevel(data,newdata,parentMenuAttrName,menuIdAttrName,menuTextAttrName,menuLevelAttrName);
		$(selectID).select2({
		   // allowClear: true,
		    //width: '400px',
		    data: newdata, 
			templateResult:function(item) { 
		       var $result = $('<span style="padding-left:' + (20 * item[menuLevelAttrName]) + 'px;">' + item[menuTextAttrName] + '</span>');
		        return $result;
		    }
		});
	},
	
	createFirstLevel:function(data,newdata,parentMenuAttrName,menuIdAttrName,menuTextAttrName,menuLevelAttrName){
        for(var i = 0; i<data.length; i++){						
			var firstMenuCodeID = data[i][menuIdAttrName];
			
			var level = data[i][menuLevelAttrName];
			if(level==0){		
				 newdata.push(data[i]);
				 comm.createSecondLevel(data,firstMenuCodeID,newdata,menuIdAttrName,parentMenuAttrName);
			}                     			
		}
	},
	
	createSecondLevel:function(data,firstMenuCodeID,newdata,menuIdAttrName,parentMenuAttrName){
	         
		for(var j = 0; j<data.length; j++){
			var codeid = data[j][menuIdAttrName];
			var secondparentcodeid = data[j][parentMenuAttrName];
			if(secondparentcodeid == firstMenuCodeID){
				newdata.push(data[j]);
				comm.createThirdLevel(data,codeid,parentMenuAttrName,newdata);
				
			}
		}
	},
	createThirdLevel:function(data,secondparentcodeid,parentMenuAttrName,newdata){
			for(var j = 0; j<data.length; j++){			
				var thirdparentcodeid = data[j][parentMenuAttrName];
				if(thirdparentcodeid == secondparentcodeid){
				   newdata.push(data[j]);
				}
			}
	},
	
	validParms:function(params){
		for(var k in params) {
			if(comm.isEmpty(params[k])){
				params[k] = null;
			}
		}
	},
	
	request : function(url, params, sucess, error) {
		comm.validParms(params);
		$.ajax({
			beforeSend : function() {
				comm.showLoading();
			},
			complete : function() {
				comm.closeLoading();
			},
			type : "POST",
			dataType : 'json',
			// contentType: "application/json",
			url : url,
			data : params,
			success : sucess,
			error : error
		});
	},
	requestDefault : function(url, params, sucess, error) {
		comm.validParms(params);
		$.ajax({
			beforeSend : function() {
				comm.showLoading();
			},
			complete : function() {
				comm.closeLoading();
			},
			type : "POST",
			url : url,
			data : params,
			success : sucess,
			error : error
		});
	},
	requestJson : function(url, params, sucess, error) {
		$.ajax({
			beforeSend : function() {
				comm.showLoading();
			},
			complete : function() {
				comm.closeLoading();
			},
			type : "POST",
			contentType : "application/json",
			url : url,
			data : params,
			success : sucess,
			error : error
		});
	},
	requestJsonByGet : function(url, sucess, error) {
		$.ajax({
			beforeSend : function() {
				comm.showLoading();
			},
			complete : function() {
				comm.closeLoading();
			},
			type : "get",
			contentType : "application/json",
			url : url,
			success : sucess,
			error : error
		});
	},
	
	contains:function(arr, obj) {  
	    var i = arr.length;  
	    while (i--) {  
	        if (arr[i] === obj) {  
	            return true;  
	        }  
	    }  
	    return false;  
	},
	/**
	  * 去掉首尾空格
	  */
	 trim : function (val){
		 return val.replace(/(^\s*)|(\s*$)/g,"");
	 },
	
	/**
	 * 判断是空
	 */
	isEmpty:function(obj){
		if(typeof(obj) == 'string'){
			obj = comm.trim(obj);
		}
		if(obj==null || obj==undefined || obj=='undefined' ||obj==''||obj=='null'||obj=='Invalid date'){
			return true;
		}
		return false;
	},
	/**
	 * 判断非空
	 */
	isNotEmpty:function(obj){
		if(typeof(obj) == 'string'){
			obj = comm.trim(obj);
		}
		if(obj==null || obj==undefined || obj=='undefined' ||obj==''||obj=='null'||obj=='Invalid date'){
			return false;
		}
		return true;
	},
	
	/**
	 * 弹出提示框
	 * content:提示内容
	 * width: 提示框的宽度  %值 。不写为默认值
	 */
	alert_tip : function (content, width) {
		$("#alert_tip").find('.modal-body').html(content);
		if(comm.isNotEmpty(width)&&'number'==typeof(width)){
			$("#alert_tip").find('.modal-dialog').css("width",width+'%');
		}else{
			$("#alert_tip").find('.modal-dialog').css("width",'');
		}
		$("#alert_tip").modal({
	        keyboard: true
	    });
	},
	/**
	 * 警告或错误提示框
	 * content:警告内容
	 * width: 提示框的宽度  %值 。不写为默认值
	 */
	alert_warn : function (content, width) {
		$("#alert_warn").find('.modal-body').html(content);
		if(comm.isNotEmpty(width)&&'number'==typeof(width)){
			$("#alert_warn").find('.modal-dialog').css("width",width+'%');
		}else{
			$("#alert_warn").find('.modal-dialog').css("width",'');
		}
		$("#alert_warn").modal({
	        keyboard: true
	    });
	},
	
	
	/**
	 * 
	 * 验证查询日期
	 * @param beginDate1
	 * @param endate1
	 * @param beginDate2
	 * @param endDate2
	 */
	validQueryDate: function (beginDate1,endDate1,beginDate2 ,endDate2,flag){
		var tip1 = "日期范围";
		var tip2 = "对比日期范围";
		if(flag==true){
			tip1 = '分析时段';
			tip2 = "对比时段";
		}
		//如果日期范围都为空
		if(comm.isEmpty(beginDate1)&&comm.isEmpty(endDate1)&&comm.isEmpty(beginDate2)&&comm.isEmpty(endDate2)){
			comm.alert_tip('请选择'+tip1+'!');
			return false;
		}
		//日期范围1 有一个为空
		if((comm.isEmpty(beginDate1)&&comm.isNotEmpty(endDate1))||(comm.isNotEmpty(beginDate1)&&comm.isEmpty(endDate1))){
			comm.alert_tip(''+tip1+'请选择完整！');
			return false;
		}
		//对比日期范围 有一个为空
		if((comm.isEmpty(beginDate2)&&comm.isNotEmpty(endDate2))||(comm.isNotEmpty(beginDate2)&&comm.isEmpty(endDate2))){
			comm.alert_tip(''+tip2+'请选择完整！');
			return false;
		}
		
		//如果日期范围1都不为空 
		if(comm.isNotEmpty(beginDate1)&&comm.isNotEmpty(endDate1)&&beginDate1>endDate1){
			comm.alert_tip(''+tip1+'请选择正确！');
			return false;
		}
		
		//如果对比日期范围都不为空 
		if(comm.isNotEmpty(beginDate2)&&comm.isNotEmpty(endDate2)&&beginDate2>endDate2){
			comm.alert_tip(''+tip2+'请选择正确！');
			return false;
		}
		
		
		//如果日期范围和对比日期范围都不为空 
		if(comm.isEmpty(beginDate1)&&comm.isEmpty(endDate1)&&comm.isEmpty(beginDate2)&&comm.isEmpty(endDate2)){
			comm.alert_tip('请选择'+tip1+'!');
			return false;
		}
		
		
		//交叉范围校验
		
		if(comm.isNotEmpty(beginDate1)&&comm.isNotEmpty(endDate1)&&comm.isNotEmpty(beginDate2)&&comm.isNotEmpty(endDate2)){
			var jiaochatip = ''+tip1+'和'+tip2+'不能交叉';
			if(beginDate1>=beginDate2 && beginDate1<=endDate2){
				comm.alert_tip(jiaochatip);
				return false;
			}
			if(endDate1>=beginDate2 && endDate1<=endDate2){
				comm.alert_tip(jiaochatip);
				return false;
			}
			if(beginDate2>=beginDate1 && beginDate2<=endDate1){
				comm.alert_tip(jiaochatip);
				return false;
			}
			if(endDate2>=beginDate1 && endDate2<=endDate1){
				comm.alert_tip(jiaochatip);
				return false;
			}
		}
		
		return true;
	},
	
	/**
	 * 
	 * @param beginDate 日期
	 * @param endDate  对比日期
	 * @returns {Boolean}
	 */
	validQueryDate2: function (beginDate,endDate){
		//如果日期范围都为空
		if(comm.isEmpty(beginDate)&&comm.isEmpty(endDate)){
			comm.alert_tip('请选择日期!');
			return false;
		}
		//如果日期范围都为空
		if(beginDate == endDate){
			comm.alert_tip('分析日期跟对比不能为同一天!');
			return false;
		}
		return true;
	},

	/**
	 * 确认提示框
	 * content:确认内容
	 * successFun :点击确定按钮回调函数
	 * cancleFun 点击取消回调函数
	 * width: 提示框的宽度  %值 。不写为默认值
	 */
	alert_confirm : function (content,successFun,cancleFun, width) {
		$("#alert_confirm").find('.modal-body').html(content);
		if(comm.isNotEmpty(width)&&'number'==typeof(width)){
			$("#alert_confirm").find('.modal-dialog').css("width",width+'%');
		}else{
			$("#alert_confirm").find('.modal-dialog').css("width",'');
		}
		$("#alert_confirm").modal({
			keyboard: true
		});
		//确定按钮 删除之前绑定的事件
		$("#alert_confirm").find('.btn-primary').off();
		if(successFun&&'function' == typeof(successFun)){
			//重新绑定确定事件
			$("#alert_confirm").find('.btn-primary').click(function(){
				successFun();
				$("#alert_confirm").modal('hide');
			});
		}
		//取消按钮 删除之前绑定的事件
		$("#alert_confirm").find('.btn-default').off();
		if(cancleFun&&'function' == typeof(cancleFun)){
			//重新绑定取消事件
			$("#alert_confirm").find('.btn-default').click(function(){
				cancleFun();
				$("#alert_confirm").modal('hide');
			});
		}
		
	},
	
	/**
	 * 显示加载层遮罩
	 */
	showLoading : function () {  
        $("#showLoadIngDiv").css("height",$(document).height());     
        $("#showLoadIngDiv").css("width",$(document).width());     
        $("#showLoadIngDiv").show();     
    } ,
	/**
	 * 隐藏遮罩
	 */
	closeLoading : function () {
		try{
			 $("#showLoadIngDiv").hide(); 
		}catch(e){
		}
	},

}

