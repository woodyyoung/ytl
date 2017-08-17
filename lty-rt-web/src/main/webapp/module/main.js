requirejs.config({
	urlArgs: "bust=" +  (new Date()).getTime(),
    //By default load any module IDs from js/lib
    baseUrl: 'js',
    //except, if the module ID starts with "app",
    //load it from the js/app directory. paths
    //config is relative to the baseUrl, and
    //never includes a ".js" extension since
    //the paths config could be for a directory.
    paths: {
    	underscore: 'underscore-min',
    	jquery: 'jquery.min',
        app: '../module/app',
        module: '../module',
        charts: '../module/charts',
        psgFlowAnalysis: '../module/psgFlowAnalysis',
        basicDataConfig: '../module/basicDataConfig',
        psgForecast: '../module/psgForecast',
        template: '../module/template',
        systemManage: '../module/systemManage',
        busAppraise: '../module/busAppraise',
        busDecisionAnalysis: '../module/busDecisionAnalysis',
        districtManagement: '../module/districtManagement',
        assessment: '../module/assessment'
    },
    shim: {                   
    	//引入没有使用requirejs模块写法的类库。backbone依赖underscore
        'underscore': {
            exports: '_'
        },
        'jquery': {
            exports: '$'
        }
    }
});

define(['underscore'], function(underscore) {
	// Start the main app logic.
	requirejs(['module/index'],
			function   (index) {
		index.bindClick();
	});
});
