define([ 'text!systemManage/tpl/dept/department.html' ], function(tpl) {
	var self = {
		show : function() {
			$('.index_padd').html('');
			$('.index_padd').html(tpl);

			$(".select2_group").select2({});
			$(".select2_multiple").select2({
				maximumSelectionLength : 5,
				placeholder : "最大可选择5个",
				allowClear : true
			});
			self.showDept();
		},
		showDept : function() {
			var defaultData = [ {
				text : '总公司',
				href : '#parent1',
				tags : [ '4' ],
				nodes : [ {
					text : '领导',
					href : '#child1',
					tags : [ '0' ],
				}, {
					text : '综合办',
					href : '#child2',
					tags : [ '0' ]
				}, {
					text : '企划部',
					href : '#child3',
					tags : [ '0' ]
				}, {
					text : '人力资源部',
					href : '#child4',
					tags : [ '0' ],
				}, {
					text : '财务管理部',
					href : '#child5',
					tags : [ '0' ]
				}, {
					text : '运营管理部',
					href : '#child6',
					tags : [ '0' ]
				} ]
			}, {
				text : '一公司',
				href : '#parent2',
				tags : [ '0' ]
			}, {
				text : '二公司',
				href : '#parent3',
				tags : [ '0' ]
			}, {
				text : '三公司',
				href : '#parent4',
				tags : [ '0' ]
			}, {
				text : '四公司',
				href : '#parent5',
				tags : [ '0' ]
			}, {
				text : '三产单位',
				href : '#parent6',
				tags : [ '0' ]
			}, {
				text : '修理厂',
				href : '#parent7',
				tags : [ '0' ]
			} ];

			var $searchableTree = $('#treeview-searchable').treeview({
				levels : 1,
				showBorder : false,
				showTags : false,
				data : defaultData,
			});

			var findCheckableNodess = function() {
				return $searchableTree.treeview('search', [
						$('#input-search').val(), {
							ignoreCase : false,
							exactMatch : false
						} ]);
			};

			var checkableNodes = findCheckableNodess();
			$('#input-search').on('keyup', function(e) {
				checkableNodes = findCheckableNodess();
			});

		}
	};
	return self;
});