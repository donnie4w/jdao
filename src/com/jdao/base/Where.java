package com.jdao.base;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0
 */
public class Where {
	private String expression;
	private Object value;

	public Where(String expression, Object value) {
		this.expression = expression;
		this.value = value;
	}

	public Where(String expression, Object... value) {
		this.expression = expression;
		this.value = value;
	}

	public OR OR(Where... wheres) {
		if (wheres != null && wheres.length == 1) {
			expression = expression + wheres[0].getExpression().replaceFirst(" and ", " or ");
			value = new Array(value, wheres[0].getValue());
		} else {
			StringBuilder sb = new StringBuilder();
			for (Where w : wheres) {
				sb.append(w.getExpression());
				value = new Array(value, w.getValue());
			}
			sb.append(") ");
			expression = expression + sb.toString().replaceFirst(" and ", " or (");
		}

		return new OR(expression, value);
	}

	public Where AND(OR or) {
		expression = expression + or.getExpression().replaceFirst(" and ", " and (") + ")";
		value = new Array(value, or.getValue());
		return this;
	}

	public String getExpression() {
		return expression;
	}

	public Object getValue() {
		return value;
	}
}
