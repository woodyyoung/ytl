package com.lty.rt.basicData.vo;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 列表页面传递查询参数和分页数据
 * 
 * @author huangzhanhu
 * @version 创建时间：2014年6月12日 上午10:01:51
 */
public class BaseConditionVO {
	public final static int PAGE_SHOW_COUNT = 20;// 每页显示数据
	private int pageNum = 1;// 当前页数
	private int pageSize = 0;// 总页数
	private int totalCount = 0;// 总记录数
	private String orderField;
	private String orderDirection;
	private String arg = "";
	private String arg1 = "";
	private String arg2 = "";
	private String arg3 = "";
	private String arg4 = "";
	private String arg5 = "";
	private String status;
	private String type;
	private String startDate = "";
	private String endDate = "";
	private String q = "";// 自动检索所传数据
	private String ids = "";// id集合
	private String code = "";// 编码
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateSearch; // 按日期自动检索
	private int ifSession = 0;// 默认为0先从session取值，1为从页面取传递的值。

	private Map<String, Object> map = new HashMap();

	public int getIfSession() {
		return ifSession;
	}

	public void setIfSession(int ifSession) {
		this.ifSession = ifSession;
	}

	public String getArg4() {
		return arg4.trim();
	}

	public void setArg4(String arg4) {
		this.arg4 = arg4;
	}

	public String getArg5() {
		return arg5.trim();
	}

	public void setArg5(String arg5) {
		this.arg5 = arg5;
	}

	public String getType() {
		if (type != null) {
			return type.trim();
		} else {
			return type;
		}
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		if ("".equals(status)) {
			return null;
		} else if (status != null) {
			return status.trim();
		}
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartDate() {
		return startDate.trim();
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate.trim();
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize > 0 ? pageSize : PAGE_SHOW_COUNT;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

	public String getOrderDirection() {
		return "desc".equals(orderDirection) ? "desc" : "asc";
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getArg() {
		return arg.trim();
	}

	public void setArg(String arg) {
		this.arg = arg;
	}

	public String getArg1() {
		return arg1.trim();
	}

	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}

	public String getArg2() {
		return arg2.trim();
	}

	public void setArg2(String arg2) {
		this.arg2 = arg2;
	}

	public String getArg3() {
		return arg3.trim();
	}

	public void setArg3(String arg3) {
		this.arg3 = arg3;
	}

	public String getQ() {
		return q.trim();
	}

	public void setQ(String q) {
		this.q = q;
	}

	public Date getDateSearch() {
		return dateSearch;
	}

	public void setDateSearch(Date dateSearch) {
		this.dateSearch = dateSearch;
	}

	public String getOrder(String sortString) {
		String result = sortString;
		if (this.orderField != null) {
			result = this.orderField;
			if (this.orderDirection != null) {
				result = result + "." + this.orderDirection;
			}
			result = result + "," + sortString;
		}
		return result;
	}

	public String getIds() {
		return ids;
	}

	public String getIdsResult() {
		if (ids != null) {
			if (ids.length() > 0) {
				ids = ids.replaceAll("undefined", "");
				while (ids.indexOf(",,") > -1) {
					ids = ids.replaceAll(",,", ",");
				}
				if (ids.indexOf(",") == 0) {
					ids = ids.substring(1);
				}

				if (ids.lastIndexOf(",") == ids.length() - 1) {
					ids = ids.substring(0, ids.length() - 1);
				}
			}
		}
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getArgVlaue(String controlName) {
		// 实例化一个当前对象
		Class c = this.getClass();
		String result = "";
		if (existsField(c, controlName)) {
			try {
				Field field = c.getDeclaredField(controlName);

				field.setAccessible(true);
				try {
					result = (String) field.get(this);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

	public static boolean existsField(Class clz, String fieldName) {
		try {
			return clz.getDeclaredField(fieldName) != null;
		} catch (Exception e) {
			// e.printStackTrace();
		}
		if (clz != Object.class) {
			return existsField(clz.getSuperclass(), fieldName);
		}
		return false;
	}

	public Map<String, Object> beanToMap() {
		Map<String, Object> params = new HashMap<String, Object>(0);
		try {
			PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
			PropertyDescriptor[] descriptors = propertyUtilsBean
					.getPropertyDescriptors(this);
			for (int i = 0; i < descriptors.length; i++) {
				String name = descriptors[i].getName();
				if (!StringUtils.equals(name, "class")) {
					params.put(name,
							propertyUtilsBean.getNestedProperty(this, name));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}

}
