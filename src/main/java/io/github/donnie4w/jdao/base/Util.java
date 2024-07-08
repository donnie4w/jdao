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
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-13
 * @verion 1.0
 */
public class Util {

    private static int len(String s) {
        if (s != null) {
            return s.length();
        }
        return 0;
    }

    public static Date asDate(Object obj) throws JdaoException {
        if (obj instanceof java.util.Date) {
            return (Date) obj;
        } else if (obj instanceof java.time.LocalDateTime) {
            LocalDateTime ldt = LocalDateTime.now();
            return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        } else if (obj instanceof java.time.LocalDate) {
            LocalDate ld = LocalDate.now();
            return Date.from(ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        } else if (obj instanceof java.time.LocalTime) {
            LocalDate ld = LocalDate.now();
            LocalTime lt = LocalTime.now();
            return Date.from(lt.atDate(ld).atZone(ZoneId.systemDefault()).toInstant());
        } else if (obj instanceof String) {
            return DateConvert.convertToDate((String)obj);
        } else if (obj instanceof byte[]) {
            return DateConvert.convertToDate(new String((byte[]) obj));
        }
        return null;
    }

    public static DateTimeFormatter inferDateFormat(String dateString) {
        DateTimeFormatter[] formatters = new DateTimeFormatter[] {
                DateTimeFormatter.ISO_LOCAL_DATE,                // e.g. 2023-07-08
                DateTimeFormatter.ISO_LOCAL_DATE_TIME,           // e.g. 2023-07-08T10:15:30
                DateTimeFormatter.ISO_OFFSET_DATE_TIME,          // e.g. 2023-07-08T10:15:30+01:00
                DateTimeFormatter.ISO_ZONED_DATE_TIME,           // e.g. 2023-07-08T10:15:30+01:00[Europe/Paris]
                DateTimeFormatter.ISO_INSTANT,                   // e.g. 2023-07-08T10:15:30Z
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSXXX"),  // e.g. 2024-07-08 13:45:08.280951+08:00
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSXXX")  // e.g. 2024-07-08 13:45:08.280951+08:00
        };

        for (DateTimeFormatter formatter : formatters) {
            try {
                if (formatter == DateTimeFormatter.ISO_LOCAL_DATE) {
                    LocalDate.parse(dateString, formatter);
                } else if (formatter == DateTimeFormatter.ISO_LOCAL_DATE_TIME) {
                    LocalDateTime.parse(dateString, formatter);
                } else if (formatter == DateTimeFormatter.ISO_OFFSET_DATE_TIME ||
                        formatter == DateTimeFormatter.ISO_ZONED_DATE_TIME ||
                        formatter == DateTimeFormatter.ISO_INSTANT) {
                    OffsetDateTime.parse(dateString, formatter);
                } else {
                    ZonedDateTime.parse(dateString, formatter);
                }
                return formatter;
            } catch (DateTimeParseException e) {
               e.printStackTrace();
            }
        }

        throw new IllegalArgumentException("Unknown date format: " + dateString);
    }


    public static String replaceMicrosecondsWithS(String timestamp) {
        Pattern pattern = Pattern.compile("\\.(\\d+)");
        Matcher matcher = pattern.matcher(timestamp);
        if (matcher.find()) {
            String microseconds = matcher.group(1);
            String replacement = "." + "S".repeat(microseconds.length());
            return matcher.replaceFirst(replacement);
        } else {
            return timestamp;
        }
    }

    public static String asString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            return new String((byte[]) obj, StandardCharsets.UTF_8);
        }
        return String.valueOf(obj);
    }

    public static byte[] asBytes(Object obj) {
        if (obj instanceof byte[]) {
            return ((byte[]) obj);
        }
        if (obj instanceof ByteBuffer) {
            return ((ByteBuffer) obj).array();
        }
        if (obj instanceof String) {
            return ((String) obj).getBytes(StandardCharsets.UTF_8);
        }
        return null;
    }

    public static boolean asBoolean(Object obj) {
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        return false;
    }

    public static byte asByte(Object obj) {
        if (obj instanceof Byte) {
            return (Byte) obj;
        }
        return 0;
    }

    public static BigInteger asBigInteger(Object obj) {
        if (obj instanceof Integer) {
            return BigDecimal.valueOf((Integer) obj).toBigInteger();
        }
        if (obj instanceof Long) {
            return BigInteger.valueOf((Long) obj);
        }
        if (obj instanceof Short) {
            return BigDecimal.valueOf(((Short) obj).intValue()).toBigInteger();
        }
        if (obj instanceof Float) {
            return BigDecimal.valueOf(((Float) obj).intValue()).toBigInteger();
        }
        if (obj instanceof Byte) {
            return BigDecimal.valueOf(((Byte) obj).byteValue()).toBigInteger();
        }
        if (obj instanceof Character) {
            return BigDecimal.valueOf(((Character) obj).charValue()).toBigInteger();
        }
        if (obj instanceof Double) {
            return BigDecimal.valueOf((Double) obj).toBigInteger();
        }
        if (obj instanceof BigInteger) {
            return ((BigInteger) obj);
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).toBigInteger();
        }

        if (obj instanceof String) {
            try {
                return new BigDecimal((String) obj).toBigInteger();
            } catch (Exception e) {
            }
        }

        return BigInteger.valueOf(0);
    }

    public static BigDecimal asBigDecimal(Object obj) {
        if (obj instanceof Integer) {
            return BigDecimal.valueOf((Integer) obj);
        }
        if (obj instanceof Long) {
            return BigDecimal.valueOf((Long) obj);
        }
        if (obj instanceof Short) {
            return BigDecimal.valueOf(((Short) obj).shortValue());
        }
        if (obj instanceof Float) {
            return BigDecimal.valueOf(((Float) obj).floatValue());
        }
        if (obj instanceof Byte) {
            return BigDecimal.valueOf(((Byte) obj).byteValue());
        }
        if (obj instanceof Character) {
            return BigDecimal.valueOf(((Character) obj).charValue());
        }
        if (obj instanceof Double) {
            return BigDecimal.valueOf((Double) obj);
        }
        if (obj instanceof BigInteger) {
            return BigDecimal.valueOf(((BigInteger) obj).longValue());
        }
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        if (obj instanceof String) {
            try {
                return new BigDecimal((String) obj);
            } catch (Exception e) {
            }
        }

        return BigDecimal.valueOf(0);
    }

    public static int asInt(Object obj) {
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        if (obj instanceof Long) {
            return ((Long) obj).intValue();
        }
        if (obj instanceof Short) {
            return ((Short) obj).intValue();
        }
        if (obj instanceof Float) {
            return ((Float) obj).intValue();
        }
        if (obj instanceof Byte) {
            return ((Byte) obj).intValue();
        }
        if (obj instanceof Character) {
            return (short) ((Character) obj).charValue();
        }
        if (obj instanceof Double) {
            return ((Double) obj).intValue();
        }
        if (obj instanceof BigInteger) {
            return ((BigInteger) obj).intValue();
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).intValue();
        }

        if (obj instanceof String) {
            try {
                return Integer.valueOf((String) obj);
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public static long asLong(Object obj) {
        if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        }
        if (obj instanceof Long) {
            return (Long) obj;
        }
        if (obj instanceof Short) {
            return ((Short) obj).longValue();
        }
        if (obj instanceof Float) {
            return ((Float) obj).longValue();
        }
        if (obj instanceof Double) {
            return ((Double) obj).longValue();
        }
        if (obj instanceof Byte) {
            return ((Byte) obj).longValue();
        }
        if (obj instanceof Character) {
            return (short) ((Character) obj).charValue();
        }
        if (obj instanceof BigInteger) {
            return ((BigInteger) obj).longValue();
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).longValue();
        }
        if (obj instanceof String) {
            try {
                return Long.valueOf((String) obj);
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public static short asShort(Object obj) {
        if (obj instanceof Integer) {
            return ((Integer) obj).shortValue();
        }
        if (obj instanceof Long) {
            return ((Long) obj).shortValue();
        }
        if (obj instanceof Short) {
            return ((Short) obj);
        }
        if (obj instanceof Float) {
            return ((Float) obj).shortValue();
        }
        if (obj instanceof Double) {
            return ((Double) obj).shortValue();
        }
        if (obj instanceof Byte) {
            return ((Byte) obj).shortValue();
        }
        if (obj instanceof Character) {
            return (short) ((Character) obj).charValue();
        }
        if (obj instanceof BigInteger) {
            return ((BigInteger) obj).shortValue();
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).shortValue();
        }
        if (obj instanceof String) {
            try {
                return Short.valueOf((String) obj);
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public static char asChar(Object obj) {
        if (obj instanceof Character) {
            return ((Character) obj).charValue();
        }
        char c = (char) asShort(obj);
        return 0;
    }

    public static double asDouble(Object obj) {
        if (obj instanceof Integer) {
            return ((Integer) obj).doubleValue();
        }
        if (obj instanceof Long) {
            return ((Long) obj).doubleValue();
        }
        if (obj instanceof Short) {
            return ((Short) obj);
        }
        if (obj instanceof Float) {
            return ((Float) obj).doubleValue();
        }
        if (obj instanceof Double) {
            return ((Double) obj);
        }
        if (obj instanceof Byte) {
            return ((Byte) obj).doubleValue();
        }
        if (obj instanceof Character) {
            return (short) ((Character) obj).charValue();
        }
        if (obj instanceof BigInteger) {
            return ((BigInteger) obj).doubleValue();
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).doubleValue();
        }
        if (obj instanceof String) {
            try {
                return Double.valueOf((String) obj);
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public static float asFloat(Object obj) {
        if (obj instanceof Integer) {
            return ((Integer) obj).floatValue();
        }
        if (obj instanceof Long) {
            return ((Long) obj).floatValue();
        }
        if (obj instanceof Short) {
            return ((Short) obj).floatValue();
        }
        if (obj instanceof Float) {
            return ((Float) obj);
        }
        if (obj instanceof Byte) {
            return ((Byte) obj).floatValue();
        }
        if (obj instanceof Character) {
            return (short) ((Character) obj).charValue();
        }
        if (obj instanceof Double) {
            return ((Double) obj).floatValue();
        }
        if (obj instanceof BigInteger) {
            return ((BigInteger) obj).floatValue();
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).floatValue();
        }
        if (obj instanceof String) {
            try {
                return Float.valueOf((String) obj);
            } catch (Exception e) {
            }
        }
        return 0;
    }

    private static  ZonedDateTime convertToZonedDateTime(Date date) {
        if (date != null) {
            return date.toInstant().atZone(ZoneId.systemDefault());
        }
        return null;
    }

    public static  LocalDateTime asLocalDateTime(Object obj) throws JdaoException {
        ZonedDateTime zonedDateTime = convertToZonedDateTime(asDate(obj));
        return zonedDateTime != null ? zonedDateTime.toLocalDateTime() : null;
    }

    public static LocalDate asLocalDate(Object obj) throws JdaoException {
        ZonedDateTime zonedDateTime = convertToZonedDateTime(asDate(obj));
        return zonedDateTime != null ? zonedDateTime.toLocalDate() : null;
    }

    public static LocalTime asLocalTime(Object obj) throws JdaoException {
        ZonedDateTime zonedDateTime = convertToZonedDateTime(asDate(obj));
        return zonedDateTime != null ? zonedDateTime.toLocalTime() : null;
    }

    public static String encodeFieldname(String name) {
        if (Const.isKey(name)) {
            return name + "_";
        }
        return name;
    }

    public static String decodeFieldname(String name) {
        if (name.endsWith("_")) {
            String keyname = name.substring(0, name.length() - 1);
            if (Const.isKey(keyname)) {
                return keyname;
            }
        }
        return name;
    }

}
