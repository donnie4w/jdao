package com.jdao.base;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0
 */
public class Sort implements Field {
	public String fieldName;

	public Sort(String field) {
		this.fieldName = field;
	}

	public String getFieldName() {
		return fieldName;
	}
}
