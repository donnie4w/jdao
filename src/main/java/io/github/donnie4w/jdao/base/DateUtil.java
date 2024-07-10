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

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.Arrays;
import java.util.Date;

public class DateUtil {

    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    public static Date asDate(Object obj) throws JdaoException {
        if (obj instanceof Date) {
            return (Date) obj;
        } else if (obj instanceof LocalDateTime) {
            return dateFromLocalDateTime((LocalDateTime) obj);
        } else if (obj instanceof LocalDate) {
            return dateFromLocalDate((LocalDate) obj);
        } else if (obj instanceof LocalTime) {
            return dateFromLocalTime((LocalTime) obj);
        } else if (obj instanceof String) {
            return parseStringDate((String) obj);
        } else if (obj instanceof byte[]) {
            return parseByteArrayDate((byte[]) obj);
        } else if (obj instanceof Long || obj instanceof Integer || obj instanceof BigInteger) {
            return dateFromNumber(obj);
        } else {
            throw new JdaoException("Unsupported type for conversion to Date: " + obj.getClass().getName());
        }
    }

    private static Date dateFromLocalDateTime(LocalDateTime ldt) {
        ZonedDateTime zdt = ldt.atZone(DEFAULT_ZONE_ID);
        return Date.from(zdt.toInstant());
    }

    private static Date dateFromLocalDate(LocalDate ld) {
        ZonedDateTime zdt = ld.atStartOfDay(DEFAULT_ZONE_ID);
        return Date.from(zdt.toInstant());
    }

    private static Date dateFromLocalTime(LocalTime lt) {
        LocalDate today = LocalDate.now();
        ZonedDateTime zdt = lt.atDate(today).atZone(DEFAULT_ZONE_ID);
        return Date.from(zdt.toInstant());
    }

    private static Date parseStringDate(String str) throws JdaoException {
        try {
            Date t = DateConvert.convertToDate(str);
            if (t == null) {
                if ((str).matches("\\d+")) {
                    t = new Date(Long.valueOf(str));
                }
            }
            return t;
        } catch (Exception e) {
            throw new JdaoException("Failed to parse date from string: " + str, e);
        }
    }

    private static Date parseByteArrayDate(byte[] bytes) throws JdaoException {
        try {
            String str = new String(bytes, StandardCharsets.UTF_8);
            return parseStringDate(str);
        } catch (Exception e) {
            throw new JdaoException("Failed to parse date from byte array: " + Arrays.toString(bytes), e);
        }
    }

    private static Date dateFromNumber(Object number) {
        long millis;
        if (number instanceof Integer) {
            millis = ((Integer) number).longValue() * 1000; // Assuming seconds
        } else if (number instanceof Long) {
            millis = (Long) number;
        } else { // BigInteger
            millis = ((BigInteger) number).longValue();
        }
        return new Date(millis);
    }
}