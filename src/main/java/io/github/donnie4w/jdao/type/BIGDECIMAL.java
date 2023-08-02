/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.type;

import io.github.donnie4w.jdao.base.Fields;

import java.math.BigDecimal;

/**
 *  BigDecimal <==> decimal,numeric
 */
public class BIGDECIMAL extends Fields {
    public BigDecimal value;
    @Override
    protected void _setobject(Object o) {
        this.value = (BigDecimal) o;
    }

    @Override
    protected void _setBigDecimal(BigDecimal o) {}

    public void setValue(BigDecimal v) {
        this.value = v;
        this.isSetValue = true;
        this._value = this.value;
    }

    @Override
    protected Class<?> valueClass() {
        return BigDecimal.class;
    }
    private BIGDECIMAL(String name) {
        super(name);
    }
    public static BIGDECIMAL New(String fieldName) {
        return new BIGDECIMAL(fieldName);
    }

    public BigDecimal getValue() {
        return value;
    }
}
