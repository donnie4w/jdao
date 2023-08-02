/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.type;

import io.github.donnie4w.jdao.base.Fields;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * date : timestamp, date ,time
 */
public class DATE extends Fields {
    public Date value;

    @Override
    protected void _setobject(Object o) {
        if (o instanceof java.util.Date){
            this.value = (Date)o;
        }else if(o instanceof java.time.LocalDateTime ){
            LocalDateTime ldt = LocalDateTime.now();
            this.value = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        }else if (o instanceof  java.time.LocalDate){
            LocalDate ld = LocalDate.now();
            this.value = Date.from(ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        }else if (o instanceof  java.time.LocalTime){
            LocalDate ld = LocalDate.now();
            LocalTime lt = LocalTime.now();
            this.value = Date.from(lt.atDate(ld).atZone(ZoneId.systemDefault()).toInstant());
        }
    }
    @Override
    protected void _setBigDecimal(BigDecimal o) {}

    public void setValue(Date v){
        this.value = v;
        this.isSetValue = true;
        this._value = this.value;
    }

    @Override
    protected Class<?> valueClass() {
        return Date.class;
    }

    private DATE(String name) {
        super(name);
    }
    public static DATE New(String fieldName){
        return new DATE(fieldName);
    }
    public Date getValue(){
        return value;
    }
}
