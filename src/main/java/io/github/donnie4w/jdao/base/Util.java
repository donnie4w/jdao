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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-13
 * @verion 1.0
 */
public class Util {


    /**
     * @param format
     * @return Date
     * @throws ParseException
     * @date date
     */
    public static Date string2Date(String date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date ret = sdf.parse(date);
        return ret;
    }

    /**
     * @param date
     * @param format
     * @return String
     * @throws ParseException
     */
    public static String date2String(Date date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String ret = sdf.format(date);
        return ret;
    }

    /**
     * @param date
     * @param format
     * @return Date
     * @throws ParseException
     */
    public static Date dateFormat(Date date, String format) throws ParseException {
        return string2Date(date2String(date, format), format);
    }

    public static Date parseTime(String datastr) {
        DateTimeFormatter formatter = Const.FORMATTERS.get(datastr.length());
        if (formatter == null) {
            return null;
        }
        LocalDateTime localDateTime = LocalDateTime.parse(datastr, formatter);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


//    public static Date toDate(String str, String formatter) {
//        try {
//            LocalDate localDate = LocalDate.parse(str, DateTimeFormatter.ofPattern(formatter));
//            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//        } catch (DateTimeParseException e) {
//            return null;
//        }
//    }

    private static int len(String s) {
        if (s != null) {
            return s.length();
        }
        return 0;
    }

    public static Date asDate(Object obj) {
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
        }
        return null;
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
            return BigDecimal.valueOf(((Short) obj).intValue());
        }
        if (obj instanceof Float) {
            return BigDecimal.valueOf(((Float) obj).intValue());
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
            return ((Double) obj).intValue();
        }
        if (obj instanceof BigInteger) {
            return ((BigInteger) obj).intValue();
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
