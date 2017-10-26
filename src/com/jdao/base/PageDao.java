package com.jdao.base;

import java.util.List;

/**
 * @File:jdao: com.jdao.base :PageDao.java
 * @Date:2017年10月20日
 * @Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * @Author: dong
 * @Desc: 分页封装类
 */
public class PageDao<T> {

	private int totalcount;

	private T t;

	private List<T> list;

	public int getTotalcount() {
		return totalcount;
	}

	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
}
