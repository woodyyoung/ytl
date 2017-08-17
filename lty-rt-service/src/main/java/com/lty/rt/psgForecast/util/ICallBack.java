package com.lty.rt.psgForecast.util;

import java.util.List;
import java.util.Map;

public interface ICallBack {
	
	List<Map<String, Object>>  call(Map<String, Object> params);

}
