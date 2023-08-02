/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.type;

import io.github.donnie4w.jdao.base.Fields;

import java.math.BigDecimal;

/**
 *  float : real
 */
public class FLOAT extends Fields {
    public  float  value;

    @Override
    protected void _setobject(Object o) {
        this.value = (Float)o;
    }

    protected  void _setBigDecimal(BigDecimal o){
        this.value = o.floatValue();
    }
    public void setValue(float v){
        this.value = v;
        this.isSetValue = true;
        this._value = this.value;
    }

    @Override
    protected Class<?> valueClass() {
        return Float.class;
    }
    private FLOAT(String name) {
        super(name);
    }
    public static FLOAT New(String fieldName){
        return new FLOAT(fieldName);
    }
    public float getValue(){
        return value;
    }
}
