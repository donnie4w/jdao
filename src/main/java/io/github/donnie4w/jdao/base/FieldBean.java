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

/**
 * Class representing a bean that holds a single field of a database result set.
 */
public class FieldBean {

    private final String fieldName;
    private final int fieldIndex;
    private final Object fieldValue;

    /**
     * Constructs a new FieldBean instance.
     *
     * @param fieldName the name of the field
     * @param fieldIndex the index of the field
     * @param fieldValue the value of the field
     */
    public FieldBean(String fieldName, int fieldIndex, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldIndex = fieldIndex;
        this.fieldValue = fieldValue;
    }

    /**
     * Returns the value of the field.
     *
     * @return the field value
     */
    public Object value() {
        return fieldValue;
    }

    /**
     * Returns the name of the field.
     *
     * @return the field name
     */
    public String name() {
        return fieldName;
    }

    /**
     * Returns the index of the field.
     *
     * @return the field index
     */
    public int index() {
        return fieldIndex;
    }

    /**
     * Returns the value of the field as a {@code Date}.
     *
     * @return the field value as a {@code Date}
     */
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

    /**
     * Returns the value of the field as a {@code LocalDateTime}.
     *
     * @return the field value as a {@code LocalDateTime}
     */
    public LocalDateTime valueLocalDateTime() {
        try {
            return Util.asLocalDateTime(fieldValue);
        } catch (JdaoException e) {
            return null;
        }
    }

    /**
     * Returns the value of the field as a {@code LocalDate}.
     *
     * @return the field value as a {@code LocalDate}
     */
    public LocalDate valueLocalDate() {
        try {
            return Util.asLocalDate(fieldValue);
        } catch (JdaoException e) {
            return null;
        }
    }

    /**
     * Returns the value of the field as a {@code LocalTime}.
     *
     * @return the field value as a {@code LocalTime}
     */
    public LocalTime valueLocalTime() {
        try {
            return Util.asLocalTime(fieldValue);
        } catch (JdaoException e) {
            return null;
        }
    }

    /**
     * Returns the value of the field as a string.
     *
     * @return the field value as a string
     */
    public String valueString() {
        return Util.asString(fieldValue);
    }

    /**
     * Returns the value of the field as a long.
     *
     * @return the field value as a long
     */
    public long valueLong() {
        return Util.asLong(fieldValue);
    }

    /**
     * Returns the value of the field as an integer.
     *
     * @return the field value as an integer
     */
    public int valueInt() {
        return Util.asInt(fieldValue);
    }

    /**
     * Returns the value of the field as a character.
     *
     * @return the field value as a character
     */
    public char valueChar() {
        return Util.asChar(fieldValue);
    }

    /**
     * Returns the value of the field as a boolean.
     *
     * @return the field value as a boolean
     */
    public boolean valueBoolean() {
        return Util.asBoolean(fieldValue);
    }

    /**
     * Returns the value of the field as a short.
     *
     * @return the field value as a short
     */
    public short valueShort() {
        return Util.asShort(fieldValue);
    }

    /**
     * Returns the value of the field as a byte.
     *
     * @return the field value as a byte
     */
    public byte valueByte() {
        return Util.asByte(fieldValue);
    }

    /**
     * Returns the value of the field as a byte array.
     *
     * @return the field value as a byte array
     */
    public byte[] valueBytes() {
        return Util.asBytes(fieldValue);
    }

    /**
     * Returns the value of the field as a float.
     *
     * @return the field value as a float
     */
    public float valueFloat() {
        return Util.asFloat(fieldValue);
    }


    /**
     * Returns the value of the field as a double.
     *
     * @return the field value as a double
     */
    public double valueDouble() {
        return Util.asDouble(fieldValue);
    }


    /**
     * Returns the value of the field as a {@code BigInteger}.
     *
     * @return the field value as a {@code BigInteger}
     */
    public BigInteger valueBigInteger() {
        return Util.asBigInteger(fieldValue);
    }

    /**
     * Returns the value of the field as a {@code BigDecimal}.
     *
     * @return the field value as a {@code BigDecimal}
     */
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
