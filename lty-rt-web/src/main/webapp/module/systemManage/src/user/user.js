define([ 'text!systemManage/tpl/user/user.html' ], function(tpl) {
	var self = {
		mytable : null,
		show : function() {
			$('.index_padd').html('');
			$('.index_padd').html(tpl);

			$(".select2_multiple").select2({
				maximumSelectionLength : 5,
				placeholder : "最大可选择5个",
				allowClear : true
			});

			var data = [ {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			}, {
				"用户名" : "lxd",
				"姓名" : "李晓东",
				"员工编号" : "1",
				"所属部门" : "物资供应部",
				"电话" : "12345678909",
				"邮箱" : "123@.com",
				"顺序" : "1",
				"初始密码" : "123456"
			} ];
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
					data : "用户名",
					title : "用户名"
				}, {
					data : "姓名",
					title : "姓名"
				}, {
					data : "员工编号",
					title : "员工编号"
				}, {
					data : "所属部门",
					title : "所属部门"
				}, {
					data : "电话",
					title : "电话"
				}, {
					data : "邮箱",
					title : "邮箱"
				}, {
					data : "顺序",
					title : "顺序"
				}, {
					data : "初始密码",
					title : "初始密码"
				} ]

			});

		}
	};
	return self;
});