package com.bonc.util;

import java.io.Serializable;

/**
 * 用于mysql数据分页
 * @author zhijie.ma
 * @date 2017年9月24日
 *
 * @param <T>
 */
public class PageBeanMysql<T> implements Serializable{
	/**
	 * limit start,end <br>
	 * 分页起始值
	 */
	private Integer start;
	/**
	 * limit  start,end <br>
	 * 分页结束值
	 */
	private Integer end;
	
	/**
	 * pageNum当前页
	 */
	private Integer pageNum; //当前页
	
	/**
	 * pageSize	//每页显示的数据条数
	 */
	private Integer pageSize = 10;
	
	/**
	 * count 数据总条数(需包含筛选条件，用于页面计算页数)
	 */
	private Integer count;
	
	/**
	 * 存放的数据
	 */
	private T data;
	
	public PageBeanMysql() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param pageNum	当前页号
	 * @param count	数据总条数
	 * @param data	存放的数据
	 */
	public PageBeanMysql(Integer pageNum, Integer count, T data) {
		super();
		this.pageNum = pageNum;
		this.count = count;
		this.data = data;
	}
	
	/**
	 * 获取计算分页起始值
	 * @param pageNum	当前页
	 */
	public PageBeanMysql(Integer pageNum) {
		super();
		this.start = pageSize * (pageNum-1);
		this.end = pageSize;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	
	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 分页起始值
	 * @return
	 */
	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	/**
	 * 分页结束值
	 * @return
	 */
	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "PageBean [pageNum=" + pageNum + ", count=" + count + ", data=" + data + "]";
	}

}
