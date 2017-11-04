package com.jdao.base;

/**
 * @File:jdao: com.jdao.base :JdaoException.java
 * @Date:2017年10月23日
 * @Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * @Author: dong
 * @Desc:
 */
public class JdaoRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JdaoRuntimeException(String message) {
		super(message);
	}

	public JdaoRuntimeException(Throwable cause) {
		super(cause);
	}

	public JdaoRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
