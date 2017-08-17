package com.lty.rt.passengerFlows.bean;

import java.io.Serializable;
import java.util.List;

public class StretchPlatFormLine  implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String pid;
	private String level;
	private String arg;
	private List<PlatFormAndLine> list;

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

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getArg() {
		return arg;
	}

	public void setArg(String arg) {
		this.arg = arg;
	}

	public List<PlatFormAndLine> getList() {
		return list;
	}

	public void setList(List<PlatFormAndLine> list) {
		this.list = list;
	}

}
