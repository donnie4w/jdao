package com.jdao.base;

/**
 * @File:jdao: com.jdao.base :And.java
 * @Date:2017年12月6日
 * @Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * @Author: dong
 * @Desc:
 */
public class And {
	public static Where[] and(Where... wheres) {
		String f = wheres[0].getExpression();
		f = f.replaceFirst(" and ", " and (");
		if (wheres.length == 1) {
			wheres[0].setExpression(f + ")");
		} else {
			wheres[wheres.length - 1].setExpression(wheres[wheres.length - 1].getExpression() + ")");
		}
		return wheres;
	}

	public static Where and(Where where) {
		String f = where.getExpression();
		f = f.replaceFirst(" and ", " and (");
		return where.setExpression(f + ")");
	}
}
