/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

/**
 * Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * date 2013-1-10
 * verion 1.0
 */
public class OR extends Where {

	public OR(String expression, Object value) {
		super(expression, value);
	}

	public OR(String expression, Object... value) {
		super(expression, value);
	}

}
