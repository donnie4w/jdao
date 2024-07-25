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
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.Arrays;
import java.util.Date;

public class DateUtil {

    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    public static Date asDate(Object obj) throws JdaoException {
        if (obj instanceof Date) {
            return (Date) obj;
        } else if (obj instanceof OffsetDateTime) {
            return java.sql.Timestamp.from(((OffsetDateTime) obj).toInstant());
        } else if (obj instanceof ZonedDateTime) {
            return java.sql.Timestamp.from(((ZonedDateTime) obj).toInstant());
        } else if (obj instanceof Instant) {
            Instant instant = (Instant) obj;
            if (instant.getNano() > 0) {
                return java.sql.Timestamp.from(instant);
            } else {
                return Date.from(instant);
            }
        } else if (obj instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) obj;
            if (localDateTime.getNano() > 0) {
                return java.sql.Timestamp.from(localDateTime.atZone(DEFAULT_ZONE_ID).toInstant());
            } else {
                return Date.from(localDateTime.atZone(DEFAULT_ZONE_ID).toInstant());
            }
        } else if (obj instanceof LocalTime) {
            LocalTime localTime = (LocalTime) obj;
            LocalDate today = LocalDate.now();
            ZonedDateTime zdt = localTime.atDate(today).atZone(DEFAULT_ZONE_ID);
            if (localTime.getNano() > 0) {
                return java.sql.Timestamp.from(zdt.toInstant());
            }
            return Date.from(zdt.toInstant());
        } else if (obj instanceof LocalDate) {
            LocalDate localDate = (LocalDate) obj;
            return Date.from(localDate.atStartOfDay(DEFAULT_ZONE_ID).toInstant());
        } else if (obj instanceof String) {
            return parseStringDate((String) obj);
        } else if (obj instanceof byte[]) {
            return parseByteArrayDate((byte[]) obj);
        } else if (obj instanceof Long) {
            return timeStampToDate((Long) obj);
        } else if (obj instanceof Integer) {
            return timeStampToDate((Integer) obj);
        } else if (obj instanceof BigInteger) {
            return timeStampToDate(((BigInteger) obj).longValue());
        } else if (obj instanceof BigDecimal) {
            return timeStampToDate(((BigDecimal) obj).longValue());
        } else {
            throw new JdaoException("Unsupported type for conversion to Date: " + obj.getClass().getName());
        }
    }

    private static Date parseStringDate(String str) throws JdaoException {
        try {
            Date t = DateConvert.convertToDate(str);
            if (t == null) {
                if ((str).matches("\\d+")) {
                    return timeStampToDate(Long.valueOf(str));
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

    private static Date timeStampToDate(long timestamp) {
        if (timestamp > 3093527923199000L) {
            return convertNanosecondsToDate(timestamp);
        } else {
            return new Date(timestamp);
        }
    }

    private static Date convertNanosecondsToDate(long timestamp) {
        java.sql.Timestamp ts = new java.sql.Timestamp(timestamp / 1_000_000L);
        ts.setNanos((int) (timestamp % 1_000_000_000L));
        return ts;
    }

}