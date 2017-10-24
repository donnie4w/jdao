package com.jdao.base;

/**
 * @File:jdao: com.jdao.base :JdaoException.java
 * @Date:2017年10月23日
 * @Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * @Author: dong
 * @Desc:
 */
public class JdaoException extends Exception {

	private static final long serialVersionUID = 1L;

	public JdaoException(String message) {
		super(message);
	}

	public JdaoException(Throwable cause) {
		super(cause);
	}

	public JdaoException(String message, Throwable cause) {
		super(message, cause);
	}

}
