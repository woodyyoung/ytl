/*
 *  
 * Copyright 
 * 1、表单系列化功能
 */
;(function($){
	/*
	 *  表单序列化成json
	 */
	 $.fn.serializeJson=function(){
		var serializeObj={};
		var array=this.serializeArray();
		var str=this.serialize();
		$(array).each(function(){
		if(serializeObj[this.name]){
		if($.isArray(serializeObj[this.name])){
		serializeObj[this.name].push(this.value);
		}else{
		serializeObj[this.name]=[serializeObj[this.name],this.value];
		}
		}else{
		serializeObj[this.name]=this.value;
		}
		});
		return serializeObj;
	 };
	
	 
})(jQuery);

