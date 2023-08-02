/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-2-2
 * @verion 1.0.1 入参
 */
public class In extends Params {
	private Object o;

	public In(Object o) {
		this.o = o;
	}

	public Object getValue() {
		return o;
	}

	@Override
	public int getTypes() {
		return 0;
	}
}
