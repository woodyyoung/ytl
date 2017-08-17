define(
		[ 'text!psgForecast/tpl/stretch/dmklcx.html' ],
		function(tpl) {
			var self = {
				show : function() {
					$('.index_padd').html('');
					$('.index_padd').html(tpl);
					require(['psgForecast/src/stretch/dmklcx_days'], function (dmklcx_days) {
						dmklcx_days.show();
					});
				}
			};
			return self;
		});