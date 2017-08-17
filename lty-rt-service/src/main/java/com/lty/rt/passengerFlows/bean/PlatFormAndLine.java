package com.lty.rt.passengerFlows.bean;

import java.io.Serializable;
import java.util.List;

import com.lty.rt.basicData.bean.Line;

public class PlatFormAndLine  implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String level;
	private String pid;
	private String arg;
	private String arg1;
	private List<Line> list;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getArg() {
		return arg;
	}

	public void setArg(String arg) {
		this.arg = arg;
	}

	public String getArg1() {
		return arg1;
	}

	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}

	public List<Line> getList() {
		return list;
	}

	public void setList(List<Line> list) {
		this.list = list;
	}

}
