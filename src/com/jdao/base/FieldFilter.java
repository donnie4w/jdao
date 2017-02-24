package com.jdao.base;

import java.io.Serializable;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-4-10
 * @verion 1.0.5
 */
public interface FieldFilter extends Serializable{
	public Object process(Fields field, String name, Object value);
}
