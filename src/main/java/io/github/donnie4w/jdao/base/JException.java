/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

/**
 * Date:2017年10月23日
 * Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * Desc:
 */
public class JException extends Exception {

	private static final long serialVersionUID = 1L;

	public JException(String message) {
		super(message);
	}

	public JException(Throwable cause) {
		super(cause);
	}

	public JException(String message, Throwable cause) {
		super(message, cause);
	}

}
