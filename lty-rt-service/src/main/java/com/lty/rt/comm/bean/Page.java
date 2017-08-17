package com.lty.rt.comm.bean;

/**
 * @author Administrator
 * 
 * 
 */
public class Page extends BaseBean {
	private static final long serialVersionUID = 1L;
	private int totalSize;
	private int pageSize;
	private int curPage;
	private String pageContent;

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public String getPageContent() {
		return pageContent;
	}

	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}

}
