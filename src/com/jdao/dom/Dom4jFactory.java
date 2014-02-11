package com.jdao.dom;
/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2012-12-12
 * @verion 1.0
 */
public class Dom4jFactory {
	public static Dom4jHandler getDom4jHandler() {
		return new Dom4jHandlerImpl();
	}
}
