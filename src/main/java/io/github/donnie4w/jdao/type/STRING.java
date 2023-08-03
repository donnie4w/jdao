/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.type;

import io.github.donnie4w.jdao.base.Fields;

import java.math.BigDecimal;

/**
 *  String ： char,varchar
 */
public final class STRING extends Fields{
    public  String value;
    private STRING(String name) {
        super(name);
    }
    @Override
    protected void _setobject(Object o) {
        this.value = (String)o;
    }

    @Override
    protected void _setBigDecimal(BigDecimal o) {}

    public void setValue(String v){
        this.value = v;
        this.isSetValue = true;
        this._value = v;
    }

    @Override
    protected Class<?> valueClass() {
        return String.class;
    }

    public static STRING New(String fieldName){
        return new STRING(fieldName);
    }

    public String getValue(){
        return value;
    }
}
