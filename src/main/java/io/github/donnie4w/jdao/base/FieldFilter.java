/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

import java.io.Serializable;

@Deprecated
/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-4-10
 * @verion 1.0.5
 */
public interface FieldFilter extends Serializable{
	public Object process(Fields field, String name, Object value);
}
