/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.type;

import io.github.donnie4w.jdao.base.Fields;

import java.math.BigDecimal;


/**
 *  short ： smallint
 */
public class SHORT extends Fields {
    public  short value;

    @Override
    protected void _setobject(Object o) {
        this.value = ((Integer)o).shortValue();
    }

    protected  void _setBigDecimal(BigDecimal o){
        this.value = o.shortValue();
    }
    public void setValue(short v){
        this.value = v;
        this.isSetValue = true;
        this._value = this.value;
    }

    @Override
    protected Class<?> valueClass() {
        return Short.class;
    }

    private SHORT(String name) {
        super(name);
    }
    public static SHORT New(String fieldName){
        return new SHORT(fieldName);
    }
    public short getValue(){
        return value;
    }
}
