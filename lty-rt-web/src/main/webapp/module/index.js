define(['module/index-middle'],function (middle) {
    //Do setup work here
		var  self = {
			isSub:false,	
            bindClick: function() {
            	
            	 
            	 //查看我的资料
            	 $("#index_myinfo").click(function() {
            		 self.queryInfo();
 	             });
            	
            	 //修改我的密码
            	 $("#index_updatePWD").click(function() {
            		 self.updatePWD();
 	             });
            	 
            	 //提交修改数据
            	 $("#index_submit_updatePWD").click(function() {
            		 self.subUpdatePWDInfo();
 	             });
            	 
            	 //旧密码输入完成时
            	 $("#index_oldPWD").blur(function(){
            		 
            		 self.isSub=false;
            		 
            		 var oldPWD = $("#index_oldPWD").val();
                 	 //对比新旧密码是否一致
            		 if(oldPWD==null||oldPWD.length==0){
            			 $("#index_oldPWD_error").text("请输入旧密码");
            		 }
            		 else{
            			 self.isSub=true;
            			 $("#index_oldPWD_error").text("");
            		 }
            		 
            	 });
            	 
            	 //新密码输入完成时
            	 $("#index_newPWD").blur(function(){
            		 
            		 self.isSub=false;
            		 
            		 var oldPWD = $("#index_oldPWD").val();
                 	 var newPWD = $("#index_newPWD").val();
                 	 //对比新旧密码是否一致
            		 if(oldPWD==newPWD){
            			 $("#index_newPWD_error").text("新密码与久密码一致,请重新输入");
            		 }else if(newPWD==null||newPWD.length==0){
            			 $("#index_newPWD_error").text("请输入新密码");
            		 }
            		 else{
            			 self.isSub=true;
            			 $("#index_newPWD_error").text("");
            		 }
            		 
            	 });
            	 
            	 //新密码第二次输入完成时
            	 $("#index_newPWD_tow").blur(function(){
            		 self.isSub=false;
            		 
                 	 var newPWD = $("#index_newPWD").val();
                 	 var newPWDTow = $("#index_newPWD_tow").val();
                 	 //对比新旧密码是否一致
            		 if(newPWD!=newPWDTow){
            			 $("#index_newPWD_tow_error").text("两次输入的新密码不一致,请重新输入");
            		 }else if(newPWDTow==null||newPWDTow.length==0){
            			 $("#index_newPWD_tow_error").text("请再次输入新密码");
            		 }
            		 else{
            			 self.isSub=true;
            			 $("#index_newPWD_tow_error").text("");
            		 }
            		 
            	 });
            	 
            	 //菜单绑定事件
            	 $('#sidebar-menu li').each(function(){
            		 $(this).click(function(){
            			 var  dataUrl = $(this).attr('dataUrl');
            			 var menuName =  $.trim($(this).children("a:first-child").text());
            			 /*if(!self.validPemission(menuName)){
            				 comm.alert_tip('没有权限操作!');
            				 return false;
            			 }*/
                		 if(comm.isNotEmpty(dataUrl)){
                			 if(dataUrl.indexOf("?") >= 0 ){
                				 var str = dataUrl.split("?");
                				 require([str[0]], function (data) {
                    				 data.show(str[1].split("=")[1]);
                            	 });
                			 }else{
                				 require([dataUrl], function (data) {
                    				 data.show();
                            	 });
                			 }
                			 
                			 //alert(dataUrl);
                		 }
            		 });
            	 });
            	 
            	 //二级菜单折叠
            	 $('.side-menu > li ').each(function(){
            		 $(this).children("ul").each(function(){
            			 $(this).children("li").each(function(){
            				 $(this).children("a").click(function(){
            					 $(this).parent().parent().find("ul").hide();
            					 $(this).parent().children("ul").show();
            				 });
            			 });
            		 });
            	 });
            	 
            	 
            	 //加载middle部分
            	 middle.show();
            	
            	/* //站台客流分析
                 $('.ztklfx').click(function(){
                	 var tile = $(this).attr('title');
                	 require(['psgFlowAnalysis/src/klfxtd/ztklfx'], function (ztklfx) {
                		 ztklfx.show(tile);
                	 });
                 });
                 //站台客流查询
                 $('.ztklcx').click(function(){
                	 require(['psgFlowAnalysis/src/ztklcx/ztklcx'], function (ztklcx) {
                		 ztklcx.show();
                	 });
                 });
                 //线路客流分析
                 $('.xlklfx').click(function(){
                	 require(['psgFlowAnalysis/src/xlklfx/xlklfx_tab'], function (tab) {
                		 tab.show();
                	 });
                 });
                 //线路客流查询
                 $('.xlklcx').click(function(){
                	 require(['psgFlowAnalysis/src/xlklcx/xlklcx_tab'], function (tab) {
                		 tab.show();
                	 });
                 });
                 //小区客流分析
                 $('.xqklfx').click(function(){
                	 require(['psgFlowAnalysis/src/xqklfx/xqklfx_tab'], function (tab) {
                		 tab.show();
                	 });
                 });
                 //小区客流查询
                 $('.xqklcx').click(function(){
                	 require(['psgFlowAnalysis/src/xqklcx/xqklcx_tab'], function (tab) {
                		 tab.show();
                	 });
                 });
                 //断面客流分析
                 $('.dmklfx').click(function(){
                	 require(['psgFlowAnalysis/src/dmklfx/dmklfx_tab'], function (tab) {
                		 tab.show();
                	 });
                 });
                 //断面客流查询
                 $('.dmklcx').click(function(){
                	 require(['psgFlowAnalysis/src/dmklcx/dmklcx_tab'], function (tab) {
                		 tab.show();
                	 });
                 });
                 
                 $('.qygjmdfx').click(function(){
                	 require(['psgFlowAnalysis/src/GIS/radar'], function (radar) {
                		 radar.show();
                	 });
                 });
                 $('.psgmfs').click(function(){
                	 require(['psgFlowAnalysis/src/GIS/psgfLevel'], function (psgfLevel) {
                		 psgfLevel.show();
                	 });
                 });
                 $('.ztklgis').click(function(){
                	 require(['psgFlowAnalysis/src/GIS/ztklgis'], function (ztklgis) {
                		 ztklgis.show();
                	 });
                 });
                 $('.ztklMapShow').click(function(){
                	 require(['psgFlowAnalysis/src/GIS/psgfMap'], function (psgfMap) {
                		 psgfMap.show();
                	 });
                 });
                 $('.lineKLFX').click(function(){
                	 require(['psgFlowAnalysis/src/GIS/psgfMap'], function (psgfMap) {
                		 psgfMap.show();
                	 });
                 });
                 $('.dm_qyztgl').click(function(){
                	 require(['districtManagement/src/dm_qyztgl'], function (dm_qyztgl) {
                		 dm_qyztgl.show();
                	 });
                 });
                 $('.dm_dygl').click(function(){
                	 require(['districtManagement/src/dm_dygl'], function (dm_dygl) {
                		 dm_dygl.show();
                	 });
                 });
                 $('.dm_ldgl').click(function(){
                	 require(['districtManagement/src/dm_ldgl'], function (dm_ldgl) {
                		 dm_ldgl.show();
                	 });
                 });
                 $('.dm_ldztgl').click(function(){
                	 require(['districtManagement/src/dm_ldztgl'], function (dm_ldztgl) {
                		 dm_ldztgl.show();
                	 });
                 });*/
            },
            updatePWD:function(){
            	
            	$("#index_oldPWD").val('');
            	$("#index_newPWD").val('');
            	$("#index_newPWD_tow").val('');
            	$("#index_oldPWD_error").text('');
            	$("#index_newPWD_error").text('');
            	$("#index_newPWD_tow_error").text('');
            	
            	//弹出修改密码窗体
            	$("#index_updatePWD_modal").modal('show');
            },
            subUpdatePWDInfo:function(){
            	//校验和提交密码数据
            	var oldPWD = $("#index_oldPWD").val();
            	var newPWD = $("#index_newPWD").val();
            	var newPWDTow = $("#index_newPWD_tow").val();
            	
            	//如果为true则提交
            	if(oldPWD.length>0 && newPWD.length>0 &&newPWDTow.length>0 &&self.isSub){
            		
            		var params = {
            				newPWD:newPWD,
            				oldPWD:oldPWD
            		};
            		
            		//提交到后台
            		$.ajax({
    					type : "POST",
    					contentType : "application/json",
    					url : 'report/userMgmt/updateUserPwd',
    					data : JSON.stringify(params),
    					success : function(resp){
    						debugger;
    						if(resp.code == 0){
    		    				window.location.href='login.html';
    	                    }else if(resp.code == -1){
    	                    	$("#index_newPWD_tow_error").text("旧密码输入失败!");
    	                    }
    	                    else{
    	                    	$("#index_newPWD_tow_error").text("密码修改失败!");
    	                    }
    					}
    				});
            		
            	}else if(oldPWD.length==0){
            		 $("#index_oldPWD_error").text("请输入旧密码");
            	}else if(newPWD.length==0){
            		 $("#index_newPWD_error").text("请输入新密码");
            	}else if(newPWDTow.length==0){
            		 $("#index_newPWD_tow_error").text("请再次输入新密码");
            	}
            	
            	
            },
            queryInfo:function(){
            	require(['systemManage/src/user/user_query'], function (user_query) {
            		$.ajax({
    					type : "POST",
    					contentType : "application/json",
    					url : 'report/userMgmt/getSessionUserInfo',
    					success : function(id){
    						if(id != null){
    							user_query.show(id);
    	                    }
    					}
    				});
            		
            		
            	});
            },
            
            validPemission:function(menuName){
            	//return true;
            	if(comm.userInfo == null){
            		return false;
            	}
            	var role = comm.userInfo.role;
            	if(role == null||role.menus==null||role.menus.length<1){
            		return false;
            	}
            	
            	var menus = role.menus;
            	for(var i=0;i<menus.length;i++){
            		var menu = menus[i];
            		if(menuName == menu.menuName){
            			return true;
            		}
            	}
            	return false;
            }
        };
		
		return self;
});