package com.lty.rt.basicData.treeUtil;

import java.util.List;

public class TreeViewEntity {

	private String id;

	private String text;

	private int level;

	private List<TreeViewEntity> nodes;

	private List<String> tags;

	private String arg;
	
	private String arg1;

	private String pid;

	private State state = new State();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<TreeViewEntity> getNodes() {
		return nodes;
	}

	public void setNodes(List<TreeViewEntity> nodes) {
		this.nodes = nodes;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
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

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

}
