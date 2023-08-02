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
public class Func implements Field {
	private static final long serialVersionUID = 1L;
	public String fieldName;

	public Func(String field) {
		this.fieldName = field;
	}

	public String getFieldName() {
		return fieldName;
	}

	/**
	 * = 等于
	 */
	public Where EQ(Object value) {
		return new Where(fieldName + "=?", value);
	}

	/**
	 * > 大于
	 */
	public Where GT(Object value) {
		return new Where(fieldName + ">?", value);
	}

	/**
	 * >= 大于等于
	 */
	public Where GE(Object value) {
		return new Where(fieldName + ">=?", value);
	}

	/**
	 * <= 小于等于
	 */
	public Where LE(Object value) {
		return new Where(fieldName + "<=?", value);
	}

	/**
	 * < 小于
	 */
	public Where LT(Object value) {
		return new Where(fieldName + "<?", value);
	}

	/**
	 * <> 不等于
	 */
	public Where NEQ(Object value) {
		return new Where(fieldName + "<>?", value);
	}

	/**
	 * like %value%
	 */
	public Where LIKE(Object value) {
		return new Where(fieldName + " like %?%", value);
	}

	/**
	 * like value%
	 */
	public Where lLIKE(Object value) {
		return new Where(fieldName + " like %?", value);
	}

	/**
	 * like %value
	 */
	public Where rLIKE(Object value) {
		return new Where(fieldName + " like ?%", value);
	}

	/**
	 * between ? and ?
	 */
	public Where BETWEEN(Object from, Object to) {
		return new Where(fieldName + " between ? and ? ", new Array(from, to));
	}

	/**
	 * fieldName as alias name
	 */
	public Func AS(String alias) {
		this.fieldName = fieldName + " as " + alias;
		return this;
	}

}
