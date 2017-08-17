define(
		[ 'text!districtManagement/tpl/dataEntry/holidayManage.html'],
		function(tpl) {
			var self = {
					mytable : null,
					allDate:{},
					show : function (){
						$('.index_padd').html('');
						$('.index_padd').html(tpl);
						
						var $dateBox = $(".qiandao_list"),
			            myDate = new Date(),
			            month=['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
			            M_option='',
			            Y_option='';

			        //获取当前年份
			        for(var i=2007;i<2100;i++){
			            if(i==myDate.getFullYear()){
			                Y_option+="<option value="+i+'年'+" selected>"+i+"年</sption>";
			            }else{
			                Y_option+="<option value="+i+'年'+">"+i+"年</sption>";
			            }
			        }
			        $('#year').append(Y_option);
			        var year_val=myDate.getFullYear();

			        //获取当前月份
			        for(var i=0;i<month.length;i++){
			            if(month[i]==(parseInt(myDate.getMonth() + 1)+"月")){
			                M_option+="<option value="+month[i]+" selected>"+month[i]+"</sption>";
			            }else{
			                M_option+="<option value="+month[i]+">"+month[i]+"</sption>";
			            }
			        }
			        $('#month').append(M_option);

			        var month_val=parseInt(myDate.getMonth() + 1);
			        //前一月
			        $('.prev').click(function(){
			            month_val--;
			            if(month_val<1){
			                month_val=12;
			            }
			            $('.numday').empty();//清空显示框
			            $('#month').val(month_val+'月');//月份显示
			            $('.months').html(month_val+'月');//显示框月份标题
			            year_val=$('#year').val().substring(0,4);//获取年份value值
			            self.calendar($dateBox,year_val,parseInt(month_val-1));//当前日历生成
			            
			        });
			        //后一月
			        $('.next').click(function(){
			            month_val++;
			            if(month_val>12){
			                month_val=1;
			            }
			            $('.numday').empty();//清空显示框
			            $('#month').val(month_val+'月');//月份显示
			            $('.months').html(month_val+'月');//显示框月份标题
			            year_val=$('#year').val().substring(0,4);//获取年份value值
			            self.calendar($dateBox,year_val,parseInt(month_val-1));//当前日历生成
			          
			        });
			        
			        $('.day').click(function(){
			        	$('.numday').empty();
			        	month_val=parseInt(myDate.getMonth() + 1);
			        	self.calendar($dateBox,myDate.getFullYear(),parseInt(myDate.getMonth()));//当前日历生成
			            $('#td'+myDate.getDate()).addClass('bg');
			            $('.numday').html('<span id="dat'+myDate.getDate()+'">'+myDate.getDate()+'号</span>');
			            $('#year').val(myDate.getFullYear()+'年');//年份显示
			            $('.years').html(myDate.getFullYear()+'年');//显示框年份标题
			            $('#month').val(parseInt(myDate.getMonth() + 1)+'月');//月份显示
			            $('.months').html(parseInt(myDate.getMonth() + 1)+'月');//显示框月份标题
			            

			        });
			        self.calendar($dateBox,myDate.getFullYear(),parseInt(myDate.getMonth()));
			        
			        
			      //右侧标题日期刷新
			        $('.years').html($('#year').val());
			        $('.months').html($('#month').val());
			        //当年份变化时右侧显示框及标题、日历刷新
			        $('#year').change(function(){
			            $('.numday').empty();
			            $('.years').html($(this).val());
			            year_val=parseInt($('#year').val());
			            month_val=parseInt($('#month').val());
			            self.calendar($dateBox,year_val,parseInt(month_val-1));
			        });
			      //当月份变化时右侧显示框及标题、日历刷新
			        $('#month').change(function(){
			            $('.numday').empty();
			            $('.months').html($(this).val());
			            year_val=parseInt($('#year').val());
			            month_val=parseInt($('#month').val());
			            self.calendar($dateBox,year_val,parseInt(month_val-1));
			            
			        });
			        
			        
			        
			        /*日期选择*/
			        Date.prototype.format = function (fmt) {
			            var o = {
			                "M+": this.getMonth() + 1, //月份
			                "d+": this.getDate(), //日
			                "h+": this.getHours(), //小时
			                "m+": this.getMinutes(), //分
			                "s+": this.getSeconds(), //秒
			                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
			                "S": this.getMilliseconds() //毫秒
			            };
			            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
			            for (var k in o)
			                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
			            return fmt;
			        };
			        var date=myDate.format("yyyy-MM-dd");
			        /*var date_st=date.substring(0,5)+'01-01';*/

			        $("#date_begin").datetimepicker({
			        	format : 'yyyy-mm-dd',
						language : "zh-CN",
						minView : 'month'
			        }).val('');
			        $("#date_end").datetimepicker({
			        	format : 'yyyy-mm-dd',
						language : "zh-CN",
						minView : 'month'
			        }).val('');
			        
			        
			        
			        //发送修改数据
			        $('#sendCal').click(function(){
			        	
			        	var key=String($('#status').val()),
			        		values=[];
			        	var nnss=$('.numday').children();
			        	console.log(11);
			        	if(parseInt($('.months').html())<10){
			        		var yyyyMM=parseInt($('.years').html())+'-0'+parseInt($('.months').html());
			        	}else{
			        		var yyyyMM=parseInt($('.years').html())+'-'+parseInt($('.months').html());
			        	}
			        	
			        	console.log(yyyyMM);
			        	for(var i=0;i<nnss.length;i++){
			        		if(parseInt(nnss[i].getAttribute('id').substring(3))<10){
			        			values[i]=yyyyMM+'-0'+nnss[i].getAttribute('id').substring(3);
			        		}else{
			        			values[i]=yyyyMM+'-'+nnss[i].getAttribute('id').substring(3);
			        		};
			        	}
			        	console.log(values);
			        	$('.numday').empty();
			            year_val=parseInt($('#year').val());
			            month_val=parseInt($('#month').val());
			        	self.sendCalenderData(key,values,$dateBox,year_val,month_val-1);
			        	
			        });
			       
			        
			      //查询按钮
		            $('#btn_hours_confirm').click(function() {
		            	self.loadTableData();
		            });
		            
		            //批量更新按钮
		            $('#btn_refresh').click(function() {
		            	self.refresh();
		            });
		            
		            self.loadTableData();//加载表格
		            
					},
					//制作日历
					calendar:function ($dateBox,_year,_month){
						$dateBox.children().remove();//清空原有月份

			            var monthFirst = new Date(_year, _month, 1).getDay(),
			                _html='',
			                arrholiday=[0,6,7,13,14,20,21,27,28,34,35,41],//定义周末数组
			                totalDay = new Date(_year, _month + 1, 0).getDate(); //获取当前月的天数


			            for (var i = 0; i < 42; i++) {
			                _html += '<li><div class="qiandao-icon"></div></li>';
			            }
			            $dateBox.html(_html); //生成日历网格

			            var $dateLi = $dateBox.find("li div");
			            
			            //生成当月的日历
			            for (var i = 0; i < totalDay; i++) {
			                $dateLi.eq(i + monthFirst).html(i + 1);
			                $dateLi.eq(i + monthFirst).attr('id','td'+(i + 1));
			            }
			            
			            //通过后台加载日历
			            if((_month + 1)<10){
			            	var mouth=_year+'0'+(_month + 1);
				            self.loadData(mouth);//加载日历
			            }else{
			            	var mouth=_year+String((_month + 1));
				            self.loadData(mouth);//加载日历
			            }
			            
			           
			           for(var i=0;i<arrholiday.length;i++){
			                $dateLi.eq(arrholiday[i]).addClass('red');
			            }

			            //点击日历选中/取消选中，且将日期放入/移除右侧div
			            $dateLi.click(function(){
			            	var numd;
//			                if($(this).html()!=''){
			            	if(!$(this).hasClass("bg")){
                                $(this).addClass('bg');
			                        //添加选中日期
			                        self.addDate($(this).html());
			                    }else{
			                        $(this).removeClass('bg');
			                        $('#dat'+$(this).html()).remove();
			                    }
//			                }
			            });
					},
					//批量刷新
					refresh: function () {
						comm.requestJsonByGet('/report/jjr/flush', function(resp){
							console.log(resp);
							if(resp.resCode == 200){
								comm.alert_tip(resp.msg);
							}
						},function(resp){
							comm.alert_tip("更新失败...");
							alert(data);
						});
					},
					//加载日历数据
					loadData : function (mouth) {
						comm.requestJsonByGet('/report/jjr/initMouth?mouth='+mouth, function(resp){
							//console.log(resp);
							if(resp.resCode == 200){
								self.initialCalendar(resp.data);
							}else{
								comm.alert_tip(resp.msg);
							}
						},function(resp){
							comm.alert_tip("加载失败...");
							alert(data);
						});
					},
					
					//渲染日历
					initialCalendar:function(data){
						self.allDate=data;
						$.each(data[0],function(val){//非工作日
							$('#td'+parseInt(data[0][val].substring(8))).addClass('red')
						});
						$.each(data[1],function(val){//工作日
							$('#td'+parseInt(data[1][val].substring(8))).css({
								'color':'#000'
							})
						});
						$.each(data[-1],function(val){//节假日
							$('#td'+parseInt(data[-1][val].substring(8))).css({
								'font-weight':'900',
								'color':'blue'
							})
						});
					},
					//显示框添加选中日期
					addDate:function(tid){
                        if($('.numday').children().length<1){
	                           var num=$('<span id="dat'+tid+'">'+tid+'号</span>');
	                        }else{
	                           var num=$('<span id="dat'+tid+'">,'+tid+'号</span>');
	                        }
	                        $('.numday').append(num);
						
					},
					//发送修改日历数据
					sendCalenderData : function (key,val,$dateBox,year_val,month_val) {
						
		                var params = {};
						params[key]=val;
						params.username=comm.userInfo.userName;
						comm.requestJson('/report/jjr/change', JSON.stringify(params), function(resp){
							console.log(resp);
							if(resp.resCode == 200){
								comm.alert_tip("设置成功。。。");
					            self.calendar($dateBox,year_val,month_val);
					            self.loadTableData();//加载表格
							}
						},function(resp){
							comm.alert_tip("发送失败...");
							alert(data);
						});
					},
					//加载表格数据
					loadTableData : function () {
						var startTime = $('#date_begin').val();
		                var endTime = $('#date_end').val();
		                
		                var params = {};
						params.begin = startTime;
						params.end = endTime;
						comm.requestJson('/report/jjr/allJjr', JSON.stringify(params), function(resp){
							if(resp.resCode == 200){
								self.initialTab(resp.data);
							}else{
								comm.alert_tip(resp.msg);
							}
						},function(resp){
							comm.alert_tip("加载失败...");
							alert(data);
						});
					},
					
					//渲染表格
					initialTab:function(data){
						self.mytable = $('#example').DataTable({
							destroy: true,
							searching: true,
							data: data,
							language:dataTable_cn,
							columns : [
							{
				                title: '年月',
				                data : 'FMONTH'
				            },
							{
								title:'日期',
								data : 'FDAY2'
							}, 
							{
								title:'类型',
								data : 'FISWORK'
							},
							{
								title:'录入时间',
								data : 'FCREATETIME'
							},
							{
								title:'录入人',
								data : "FUSERNAME"
							},
							{
								title:'备注',
								data :null,
								defaultContent: '',
								orderable: false
							}
							],
			                select: {
			                    style:    'os',
			                    selector: 'td:first-child'
			                }
		
						});
					}
			}
			return self;
		});
			    