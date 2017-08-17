define(
		[ 'text!psgForecast/tpl/area/xqklcx.html' ],
		function(tpl) {
			var self = {
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					require(['psgForecast/src/area/xqklcx_days'], function (xqklcx_days) {
						xqklcx_days.show();
					});
				}
			};
			return self;
		});