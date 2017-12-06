package com.jdao.base;

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
}
