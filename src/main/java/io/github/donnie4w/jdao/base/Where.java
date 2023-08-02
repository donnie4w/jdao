/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0
 */
public class Where {
	private String expression;
	private Object value;

	public Where(OR or) {
		this.expression = or.getExpression();
		this.value = or.getValue();
	}

	public Where(String expression, Object value) {
		this.expression = expression;
		this.value = value;
	}

	public Where(String expression, Object... value) {
		this.expression = expression;
		if (value != null && value.length > 0)
			this.value = value;
	}

	public OR OR(Where... wheres) {
		if (wheres != null && wheres.length == 1) {
			expression = expression + wheres[0].getExpression().replaceFirst(" and ", " or ");
			value = value != null ? new Array(value, wheres[0].getValue()) : null;
		} else {
			StringBuilder sb = new StringBuilder();
			for (Where w : wheres) {
				sb.append(w.getExpression());
				value = value != null ? new Array(value, w.getValue()) : null;
			}
			sb.append(") ");
			expression = expression + sb.toString().replaceFirst(" and ", " or (");
		}

		return new OR(expression, value);
	}

	public Where AND(OR or) {
		expression = expression + or.getExpression().replaceFirst(" and ", " and (") + ")";
		value = value != null ? new Array(value, or.getValue()) : null;
		return this;
	}

	public String getExpression() {
		return expression;
	}

	public Object getValue() {
		return value;
	}

	public Where setExpression(String expression) {
		this.expression = expression;
		return this;
	}

	public Where setValue(Object value) {
		this.value = value;
		return this;
	}
}
