/*
 * Copyright (c) 2024, donnie <donnie4w@gmail.com> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * github.com/donnie4w/jdao
 */

package io.github.donnie4w.jdao.base;

import io.github.donnie4w.jdao.handle.JdaoException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Date;

public class FieldBean {

    private final String fieldName;
    private final int fieldIndex;
    private final Object fieldValue;

    public FieldBean(String fieldName, int fieldIndex, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldIndex = fieldIndex;
        this.fieldValue = fieldValue;
    }

    public Object value() {
        return fieldValue;
    }

    public String name() {
        return fieldName;
    }

    public int index() {
        return fieldIndex;
    }

    public Date valueDate() {
        if (fieldValue != null) {
            try {
                return Util.asDate(fieldValue);
            } catch (JdaoException e) {
            }
        }
        return null;
    }

    private ZonedDateTime convertToZonedDateTime(Date date) {
        if (date != null) {
            return date.toInstant().atZone(ZoneId.systemDefault());
        }
        return null;
    }

    public LocalDateTime valueLocalDateTime() {
        try {
            return Util.asLocalDateTime(fieldValue);
        } catch (JdaoException e) {
            return null;
        }
    }

    public LocalDate valueLocalDate() {
        try {
            return Util.asLocalDate(fieldValue);
        } catch (JdaoException e) {
            return null;
        }
    }

    public LocalTime valueLocalTime() {
        try {
            return Util.asLocalTime(fieldValue);
        } catch (JdaoException e) {
            return null;
        }
    }

    public String valueString() {
        return Util.asString(fieldValue);
    }

    public long valueLong() {
        return Util.asLong(fieldValue);
    }

    public int valueInt() {
        return Util.asInt(fieldValue);
    }

    public char valueChar() {
        return Util.asChar(fieldValue);
    }

    public boolean valueBoolean() {
        return Util.asBoolean(fieldValue);
    }

    public short valueShort() {
        return Util.asShort(fieldValue);
    }

    public byte valueByte() {
        return Util.asByte(fieldValue);
    }

    public byte[] valueBytes() {
        return Util.asBytes(fieldValue);
    }

    public float valueFloat() {
        return Util.asFloat(fieldValue);
    }

    public double valueDouble() {
        return Util.asDouble(fieldValue);
    }

    public BigInteger valueBigInteger() {
        return Util.asBigInteger(fieldValue);
    }

    public BigDecimal valueBigDecimal() {
        return Util.asBigDecimal(fieldValue);
    }


    @Override
    public String toString() {
        return "FieldBean{" +
                "fieldName='" + fieldName + '\'' +
                ", fieldIndex=" + fieldIndex +
                ", fieldValue=" + fieldValue +
                '}';
    }
}
