/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

import java.util.List;

/**
 * Date:2017年10月20日
 * Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * Desc: 分页封装类
 */
public class PageBean<T> {

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
