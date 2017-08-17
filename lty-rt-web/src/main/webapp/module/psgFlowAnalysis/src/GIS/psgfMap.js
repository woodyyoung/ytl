define(['text!psgFlowAnalysis/tpl/GIS/passengerFlows.html'],
function(tpl) {
    //Do setup work here
    var self = {
        mapObj: null,
        infoWindow: null,
        folwDegrees:null,
        folwDegree:null,
        data:null,
        show: function() {
            $('.index_padd').html('');
            $('.index_padd').html(tpl);

            $("#startTime").datetimepicker({
                format: 'yyyy-mm-dd',
                language: "zh-CN",
                minView: 'month'
                //minView: 'hour'
            });
            $("#endTime").datetimepicker({
                format: 'yyyy-mm-dd',
                language: "zh-CN",
                minView: 'month'
                //minView: 'hour'

            });
            
            //确定按钮
            $('#btn-export').click(function() {
                var $targetElem = $(".passenger_position");
                var visitorsFlowRate_mapW = $(".visitorsFlowRate_map").width();
                var visitorsFlowRate_mapH = $(".visitorsFlowRate_map").height();
      		    html2canvas($targetElem, {
      		    onrendered: function(canvas) {
      		    //document.body.appendChild(canvas); 
      		    var url = canvas.toDataURL();
      		    self.exportData(url);
      		    },
      		    width: visitorsFlowRate_mapW,
      		    height: visitorsFlowRate_mapH
      		    });
            });
            
            
			
            self.infoWindow = new AMap.InfoWindow({
                isCustom: false,
                // 使用自定义窗体
                offset: new AMap.Pixel(0, -15) // -113, -140
            });

            self.mapObj = new AMap.Map('visitorsFlowRate_map', {
                resizeEnable: true,
                center: [109.4, 24.33],
                zoom: 13
            });

            AMap.plugin(['AMap.ToolBar', 'AMap.Scale', 'AMap.OverView'],
            function() {
                self.mapObj.addControl(new AMap.ToolBar());

                self.mapObj.addControl(new AMap.Scale());

                self.mapObj.addControl(new AMap.OverView({
                    isOpen: true
                }));
            });

            //确定按钮
            $('#btn_cx').click(function() {
                self.initTableData();
            });
            
            //
			$('#planName').change(function(){
				var planId =  $('#planName').val();
				var levelData = self.getFlowLevelData(planId);
				self.folwDegree = levelData;
				comm.initLevelDiv(levelData);
				self.initTableData();
			});
          
            //初始化站台下拉框
            comm.initPlatSelect('#p_station',true);
                
            // 初始化列表数据
            self.initLevelDiv();
            self.initTableData();

        },
        initTableData: function() {
            var stationId = $.trim($('#p_station option:selected').val());
            stationId = stationId.split('=')[0];
            var startTime = $('#startTime').val();
            var endTime = $('#endTime').val();
            var dayType = $('input[name="optionsRadios1"]:checked').val();
            //var params = {};
            if (($('#startTime').val()) >($('#endTime').val())) {
                alert('请选择正确日期范围 ');
  				return false;
  			}
            if (($('#startTime').val()) >($('#endTime').val())) {
                alert('请选择正确日期范围 ');
  				return false;
  			}
            var params = {
                "stationId": stationId,
                "holidayFlag": dayType,
                "startTime": startTime,
                "endTime": endTime
            }
            comm.requestJson('/report/myPassengerFlow/getPassengerData', JSON.stringify(params),
            function(resp) {
                self.initTable(resp);
                self.data = resp;
                self.initMap(resp);
            });
        },
        getImgUrl:function(){
        	var url = '';
        	var $targetElem = $(".passenger_position");
            var visitorsFlowRate_mapW = $(".visitorsFlowRate_map").width();
            var visitorsFlowRate_mapH = $(".visitorsFlowRate_map").height();
  		    html2canvas($targetElem, {
  		    onrendered: function(canvas) {
  		    url = canvas.toDataURL();
  		    },
  		    width: visitorsFlowRate_mapW,
  		    height: visitorsFlowRate_mapH
  		    });
  		    return url;
        },
        exportData : function(img){
			var titleColumn = [];
			var titleName = [];
			var titleSize = [40, 20, 20];
			var startTime = $('#startTime').val();
	        var endTime = $('#endTime').val();
	        var time = startTime + " to " + endTime;
			titleColumn = ["P_NAME","ONBUS_PERSON_COUNT","OFFBUS_PERSON_COUNT"];
			titleName = ["站台","上车人数","下车人数"];
			$("#img").val(img);
		    $("#tableData").val(JSON.stringify(self.data));
		    $("#fileName").val("站台客流展示");
		    $("#titleColumn").val(JSON.stringify(titleColumn));
		    $("#titleName").val(JSON.stringify(titleName));
		    $("#titleSize").val(JSON.stringify(titleSize));
		    $("#export_form").submit();
		},
        initTable: function(data) {
            var startTime = $('#startTime').val();
            var endTime = $('#endTime').val();
            var mytable = $("#example").DataTable({
                data: data,
                destroy: true,
                order: [[1, "desc"]],
                language: dataTable_cn,
                columns: [{
                    defaultContent: '',
                    data: "P_NAME"
                },
                {
                    data: null,
                    defaultContent: '',
                    render: function(data, type, row, meta) {
                        return startTime + " to " + endTime;
                    }
                },
                {
                    defaultContent: '',
                    data: "ONBUS_PERSON_COUNT"
                },
                {
                    defaultContent: '',
                    data: "OFFBUS_PERSON_COUNT"
                }],
                select: {
                    style: 'os',
                    selector: 'td:first-child'
                }
            });
        },
        initMap: function(data) {
        	self.mapObj.clearMap();
            for (var i = 0; i < data.length; i++) {
            	var levelInfo  = self.getFlowDegree(data[i].TOTAL_PERSON_COUNT);
                var circle = new AMap.Circle({
                    radius: levelInfo[1],
                    center: [data[i].LONGITUDE, data[i].LATITUDE],
                    strokeColor: levelInfo[0],
                    strokeWeight: 3,
                    fillColor: levelInfo[0],
                    fillOpacity: 0.8,
                    map: self.mapObj
                });

                var template = "<div class='passengerFlowInfo'><h2 class='passengerFlowInfo_title'>站台名:" + data[i].P_NAME + "</h2><p class='passengerFlowInfo_p'>上车人数:" + data[i].ONBUS_PERSON_COUNT + "</p><p class='passengerFlowInfo_p'>下车人数:" + data[i].OFFBUS_PERSON_COUNT + "</p></div>";
                circle.content = template;
                circle.on("click", self.passengerFlowClick);
            }
        },
        initSelect: function(data) {
            var defaultOption = '<option value=" ">全部</option>';
            comm.initSelectOptionForObj('p_station', data, 'name', 'id', defaultOption);
        },
        passengerFlowClick: function(e) {
            var content = e.target.content; //地图上所标点的坐标    
            self.infoWindow.setContent(content);
            self.infoWindow.open(self.mapObj, e.target.getCenter());
        },
        //根据客流获取客流等级颜色值
        getFlowDegree : function(value){
        	if(comm.isNotEmpty(value)){
        		var ret = [];
        		for(var i=0; i<self.folwDegree.length; i++){
        			var max = self.folwDegree[i].maxdata;
        			var min = self.folwDegree[i].mindata;
        			if(value >= min && value < max){
        				ret.push(self.folwDegree[i].circlecolor);
        				ret.push(self.folwDegree[i].circlesize);
        			}
        		}
        		return ret;
        	}
        },
      //根据客流获取客流等级颜色值
        getFlowLevelData : function(value){
        	if(comm.isNotEmpty(value)){
        		var ret = '';
        		for(var i=0; i<self.folwDegrees.length; i++){
        			var planId = self.folwDegrees[i].id;
        			var levelList = self.folwDegrees[i].levelList;
        			if(value == planId){
        				ret = levelList;
        			}
        		}
        		return ret;
        	}
        },
        initLevelDiv:function(){
        	var params = {};
            params.dataType = '001';
            params.isDisable = '1';
        	comm.requestJson('/report/passengerFlowLevel/planAndLevelList', JSON.stringify(params),
        	            function(resp) {
        					self.folwDegrees = resp;
        					comm.initSelectOptionForObj('planName', resp, 'planName', 'id');
        					comm.initLevelDiv(resp[0].levelList);
        					self.folwDegree = resp[0].levelList;
        	            });
        }

    };
    return self;
});