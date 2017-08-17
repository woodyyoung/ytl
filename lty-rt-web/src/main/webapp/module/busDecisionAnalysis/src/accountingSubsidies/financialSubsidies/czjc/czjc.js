define([ 'text!busDecisionAnalysis/tpl/accountingSubsidies/financialSubsidies/czjc/czjc.html' ], function(tpl) {
	var self = {
		mytable : null,
		show : function() {
			$('.index_padd').html('');
			$('.index_padd').html(tpl);
			var data = [ {
				"补贴名称" : "车辆购置补贴",
				"补贴额度" : "100",
				"补贴时间" : "7月",
				"补贴对象" : "户外职工",
				"补贴来源" : "地方",
				"备注" : ""
			}, {
				"补贴名称" : "车辆购置补贴",
				"补贴额度" : "100",
				"补贴时间" : "7月",
				"补贴对象" : "户外职工",
				"补贴来源" : "地方",
				"备注" : ""
			},{
				"补贴名称" : "车辆购置补贴",
				"补贴额度" : "100",
				"补贴时间" : "7月",
				"补贴对象" : "户外职工",
				"补贴来源" : "地方",
				"备注" : ""
			}, {
				"补贴名称" : "车辆购置补贴",
				"补贴额度" : "100",
				"补贴时间" : "7月",
				"补贴对象" : "户外职工",
				"补贴来源" : "地方",
				"备注" : ""
			},{
				"补贴名称" : "车辆购置补贴",
				"补贴额度" : "100",
				"补贴时间" : "7月",
				"补贴对象" : "户外职工",
				"补贴来源" : "地方",
				"备注" : ""
			}, {
				"补贴名称" : "车辆购置补贴",
				"补贴额度" : "100",
				"补贴时间" : "7月",
				"补贴对象" : "户外职工",
				"补贴来源" : "地方",
				"备注" : ""
			},{
				"补贴名称" : "车辆购置补贴",
				"补贴额度" : "100",
				"补贴时间" : "7月",
				"补贴对象" : "户外职工",
				"补贴来源" : "地方",
				"备注" : ""
			}, {
				"补贴名称" : "车辆购置补贴",
				"补贴额度" : "100",
				"补贴时间" : "7月",
				"补贴对象" : "户外职工",
				"补贴来源" : "地方",
				"备注" : ""
			}
			];
			self.mytable = $("#example").DataTable({
				data : data,
				language : dataTable_cn,
				destroy : true,
				columns : [ {
					data : null,
					defaultContent : '',
					className : 'select-checkbox',
					orderable : false
				}, {
					data : "补贴名称",
					title : "补贴名称"
				}, {
					data : "补贴额度",
					title : "补贴额度"
				}, {
					data : "补贴时间",
					title : "补贴时间"
				}, {
					data : "补贴对象",
					title : "补贴对象"
				}, {
					data : "补贴来源",
					title : "补贴来源"
				}, {
					data : "备注",
					title : "备注"
				}
				]

			});

		},
	};
	return self;
});