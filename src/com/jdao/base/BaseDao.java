package com.jdao.base;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-13
 * @verion 1.0
 */
public interface BaseDao {

	public int size();

	public boolean hasNext();

	public QueryDao next();

	public void flip();

	public Object fieldType(String field);

	public Object fieldValue(String field);

	public int field2Int(String field);

	public String field2String(String field);

	public BigDecimal field2BigDecimal(String field);

	public Date field2Date(String field, String format) throws ParseException;

	public String field2DateString(String field, String format) throws ParseException;
}
