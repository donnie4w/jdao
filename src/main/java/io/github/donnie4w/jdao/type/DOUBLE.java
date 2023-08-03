/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.type;

import io.github.donnie4w.jdao.base.Fields;

import java.math.BigDecimal;

/**
 *  double : DOUBLE
 */
public final class DOUBLE extends Fields {
    public  double  value;

    @Override
    protected void _setobject(Object o) {
        this.value = (Double)o;
    }
    protected  void _setBigDecimal(BigDecimal o){
        this.value = o.doubleValue();
    }
    public void setValue(double v){
        this.value = v;
        this.isSetValue = true;
        this._value = this.value;
    }

    @Override
    protected Class<?> valueClass() {
        return Double.class;
    }
    private DOUBLE(String name) {
        super(name);
    }
    public static DOUBLE New(String fieldName){
        return new DOUBLE(fieldName);
    }
    public double getValue(){
        return value;
    }
}
