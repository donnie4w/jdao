/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.type;

import io.github.donnie4w.jdao.base.Fields;

import java.math.BigDecimal;

/**
 * byte[] :  binary, varbinary , longvarbinary
 */
public class BINARY extends Fields {
    public byte[] value;

    @Override
    protected void _setobject(Object o) {
        this.value = ((byte[])o);
    }

    @Override
    protected void _setBigDecimal(BigDecimal o) {}

    public void setValue(byte[] v){
        this.value = v;
        this.isSetValue = true;
        this._value = this.value;
    }

    @Override
    protected Class<?> valueClass() {
        return byte[].class;
    }

    private BINARY(String name) {
        super(name);
    }
    public static BINARY New(String fieldName){
        return new BINARY(fieldName);
    }
    public byte[] getValue(){
        return value;
    }
}
