/*define("common",[],function() {
	var self = {
		*//**
		 * 初始化下拉狂列表
		 * data like [1,2,3,4,5....]
		 *//*
		initSelectOption : function(selectId,data) {
			var str ='<option>请选择</option>';
			for(var i=0;i<data.length;i++){
				str+='<option value='+data[i]+'>data[i]</option>';
			}
			$('#'+selectId).html(str);
			$('#'+selectId).select2();
		},
		test1:function(){
			alert(111);
		}
	};
	return self;
});*/

window.dataTable_cn = {
	    "sProcessing": "处理中...",
	    "sLengthMenu": "显示_MENU_项结果",
	    "sZeroRecords": "没有匹配结果",
	    "sInfo": "显示第_START_至_END_项结果，共_TOTAL_项",
	    "sInfoEmpty": "显示第0至0项结果，共0项",
	    "sInfoFiltered": "(由_MAX_项结果过滤)",
	    "sInfoPostFix": "",
	    "sSearch": "搜索:",
	    "sUrl": "",
	    "sEmptyTable": "表中数据为空",
	    "sLoadingRecords": "载入中...",
	    "sInfoThousands": ",",
	    "oPaginate": {
	        "sFirst": "首页",
	        "sPrevious": "上页",
	        "sNext": "下页",
	        "sLast": "末页"
	    },
	    "oAria": {
	        "sSortAscending": ":以升序排列此列",
	        "sSortDescending": ":以降序排列此列"
	    }
}