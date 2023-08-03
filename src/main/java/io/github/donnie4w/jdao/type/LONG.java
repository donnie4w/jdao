/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.type;

import io.github.donnie4w.jdao.base.Fields;

import java.math.BigDecimal;

/**
 *  long
 */
public final class LONG extends Fields {
    public long value;

    private LONG(String name) {
        super(name);
    }
    @Override
    protected void _setobject(Object o) {
        this.value = ((Long)o);
    }

    protected  void _setBigDecimal(BigDecimal o){
        this.value = ((BigDecimal)o).longValue();
    }
    public void setValue(long v){
        this.value = v;
        this.isSetValue = true;
        this._value = this.value;
    }

    @Override
    protected Class<?> valueClass() {
        return Long.class;
    }

    public static LONG New(String fieldName){
        return new LONG(fieldName);
    }

    public long getValue(){
        return value;
    }
}
