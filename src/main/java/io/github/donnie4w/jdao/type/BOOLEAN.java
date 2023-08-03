/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.type;


import io.github.donnie4w.jdao.base.Fields;

import java.math.BigDecimal;

/**
 * boolean : bit
 */
public final class BOOLEAN extends Fields {
    public  boolean value;

    @Override
    protected void _setobject(Object o) {
        this.value = (Boolean)o;
    }

    @Override
    protected void _setBigDecimal(BigDecimal o) {}

    public void setValue(boolean v){
        this.value = v;
        this.isSetValue = true;
        this._value = this.value;
    }

    @Override
    protected Class<?> valueClass() {
        return Boolean.class;
    }

    private BOOLEAN(String name) {
        super(name);
    }
    public static BOOLEAN New(String fieldName){
        return new BOOLEAN(fieldName);
    }
    public boolean getValue(){
        return value;
    }
}
