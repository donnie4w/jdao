/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */

package io.github.donnie4w.jdao.base;

/**
 * @File:jdao: com.jdao.base :JdaoException.java
 * @Date:2017年10月23日
 * @Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * @Author: dong
 * @Desc:
 */
public class JRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JRuntimeException(String message) {
		super(message);
	}

	public JRuntimeException(Throwable cause) {
		super(cause);
	}

	public JRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
