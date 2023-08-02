/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.type;

import io.github.donnie4w.jdao.base.Fields;

import java.math.BigDecimal;

/**
 * int : integer
 */
public class INT extends Fields {
    public  int value;

    @Override
    protected void _setobject(Object o) {
        this.value = (Integer)o;
    }

    protected  void _setBigDecimal(BigDecimal o){
        this.value = o.intValue();
    }
    public void setValue(int v){
        this.value = v;
        this.isSetValue = true;
        this._value = this.value;
    }

    @Override
    protected Class<?> valueClass() {
        return Integer.class;
    }
    private INT(String name) {
        super(name);
    }

    public static INT New(String fieldName){
        return new INT(fieldName);
    }
    public int getValue(){
        return value;
    }
}
