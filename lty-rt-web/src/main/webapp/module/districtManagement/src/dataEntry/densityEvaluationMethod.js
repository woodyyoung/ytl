define(
		[ 'text!districtManagement/tpl/dataEntry/densityEvaluationMethod.html','text!districtManagement/tpl/dataEntry/densityEvaluationMethod_add.html' ],
		function(tpl,addtpl) {
			var self = {
				mytable : null,
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					//渲染表格
					self.initialTab();
					//渲染左树
					self.initTree();
		            //添加财政补贴类型
					$('#btn-insert').click(function(){
						self.fiscalSubsidyType();
					});
					$("#startTime").datetimepicker({
		                format: 'yyyy-mm-dd',
		                language: "zh-CN",
		                minView: 'month'
		            });
		            $("#endTime").datetimepicker({
		                format: 'yyyy-mm-dd',
		                language: "zh-CN",
		                minView: 'month'
		            });
					$("#fiscalSubsidyType_name").select2({});
				},
				initialTab:function(){
					var entryData = [{"targetedValue":"10","date":"2015-07-01","entryTime":"2015-10-11","enterOne":"admin","remark":"备注"},{"targetedValue":"10","date":"2015-07-01","entryTime":"2015-10-11","enterOne":"admin","remark":"备注"},{"targetedValue":"10","date":"2015-07-01","entryTime":"2015-10-11","enterOne":"admin","remark":"备注"}];
					self.mytable = $('#example').DataTable({
						destroy: true,
						data: entryData,
						order: [[ 1, "number" ]],
						language:dataTable_cn,
						columns : [
						 {
		                  data: null,
		                  defaultContent: '',
		                  className: 'select-checkbox',
		                  orderable: false
		                },
						{
							title:'指标值',
							data : 'targetedValue'
						}, 
						{
							title:'日期',
							data : 'date'
						},
						{
							title:'录入时间',
							data : "entryTime"
						},
						{
							title:'录入人',
							data : 'enterOne'
						},
						{
							title:'备注',
							data : "remark"
						}
						],
		                select: {
		                    style:    'os',
		                    selector: 'td:first-child'
		                }
	
					});
				},
				initTree : function() {
					var defaultData = [
			          {
			            text: '总体线网评价',
			            href: '#parent1',
			            tags: ['1'],
			            nodes: [
			              {
			                text: '线网密度评价',
			                href: '#child1',
			                tags: ['0']
			              }
			            ]
			          },
			          {
			          	text: '个体线网评价',
			            href: '#parent1',
			            tags: ['0']
			          },
			          {
			          	text: '区域线网评价',
			            href: '#parent1',
			            tags: ['0']
			          }
			        ];
					self.myTreeTable = $('#treeview-searchable').treeview({
						levels: 2,
			            showBorder: false,
			            showTags: true,
			            data:defaultData
					});
				},
				fiscalSubsidyType:function(){
					$('#dialogDiv').html(addtpl);
					$("#addDensityEvaluationMethodModal").modal({
						keyboard: true
					});
					$("#addDensityEvaluationMethodModal").modal('show');
					$("#startTime1").datetimepicker({
		                format: 'yyyy-mm-dd',
		                language: "zh-CN",
		                minView: 'month'
		            });
		            $("#endTime1").datetimepicker({
		                format: 'yyyy-mm-dd',
		                language: "zh-CN",
		                minView: 'month'
		            });
				},
			};
			return self;
	});