define(
		[ 'text!psgForecast/tpl/line/xlklcx.html' ],
		function(tpl) {
			var self = {
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					require(['psgForecast/src/line/xlklcx_days'], function (xlklcx_days) {
						xlklcx_days.show();
					});
				}
			};
			return self;
		});