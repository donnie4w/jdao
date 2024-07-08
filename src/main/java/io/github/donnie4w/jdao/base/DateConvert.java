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
import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class DateConvert {

    private static final TreeMap<Integer, List<DateTimeFormatter>> formattersByLength = new TreeMap<>();

    static {
        List<DateTimeFormatter> formattersForLength10 = Collections.singletonList(DateTimeFormatter.ISO_LOCAL_DATE);
        formattersByLength.put(10, formattersForLength10);

        List<DateTimeFormatter> formattersForLength19 = Arrays.asList(
                DateTimeFormatter.ISO_LOCAL_DATE_TIME,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        );
        formattersByLength.put(19, formattersForLength19);

        List<DateTimeFormatter> formattersForLength23 = Arrays.asList(
                DateTimeFormatter.ISO_LOCAL_DATE_TIME,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        );
        formattersByLength.put(23, formattersForLength23);

        List<DateTimeFormatter> formattersForLength24 = Arrays.asList(
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSX"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSZ")
        );
        formattersByLength.put(24, formattersForLength24);

        List<DateTimeFormatter> formattersForLength29 = Arrays.asList(
                DateTimeFormatter.ISO_OFFSET_DATE_TIME,
                DateTimeFormatter.ISO_ZONED_DATE_TIME,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSXXX")
        );
        formattersByLength.put(29, formattersForLength29);
    }

    public static Date convertToDate(String isoDateString) throws JdaoException {
        Integer length = isoDateString.length();
        List<DateTimeFormatter> formatters = formattersByLength.floorEntry(length).getValue();

        if (formatters == null) {
            throw new JdaoException("Unsupported date format length: " + length);
        }
        System.out.println("formatters length: " + length);
        for (DateTimeFormatter formatter : formatters) {
            try {
                Object parsed = parseWithFormatter(formatter, isoDateString);
                if (parsed instanceof LocalDate) {
                    LocalDate localDate = (LocalDate) parsed;
                    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                } else if (parsed instanceof LocalDateTime) {
                    LocalDateTime localDateTime = (LocalDateTime) parsed;
                    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                } else if (parsed instanceof OffsetDateTime) {
                    OffsetDateTime offsetDateTime = (OffsetDateTime) parsed;
                    return Date.from(offsetDateTime.toInstant());
                } else if (parsed instanceof ZonedDateTime) {
                    ZonedDateTime zonedDateTime = (ZonedDateTime) parsed;
                    return Date.from(zonedDateTime.toInstant());
                }
            } catch (DateTimeParseException ignored) {
                // continue to the next format
            }
        }
        throw new IllegalArgumentException("Unsupported date format: " + isoDateString);
    }

    private static Object parseWithFormatter(DateTimeFormatter formatter, String isoDateString) {
        if (formatter.equals(DateTimeFormatter.ISO_LOCAL_DATE)) {
            return LocalDate.parse(isoDateString, formatter);
        } else if (formatter.equals(DateTimeFormatter.ISO_LOCAL_DATE_TIME)) {
            return LocalDateTime.parse(isoDateString, formatter);
        } else if (formatter.equals(DateTimeFormatter.ISO_OFFSET_DATE_TIME)) {
            return OffsetDateTime.parse(isoDateString, formatter);
        } else if (formatter.equals(DateTimeFormatter.ISO_ZONED_DATE_TIME)) {
            return ZonedDateTime.parse(isoDateString, formatter);
        } else {
            return LocalDateTime.parse(isoDateString, formatter);
        }
    }

    @Test
    public void testConvert() {
        String[] testDates = {
                "2023-07-08",
                "2023-07-08T15:20:31",
                "2023-07-08T15:20:32",
                "2023-07-08T15:20:33.12",
                "2023-07-08T15:20:34.123",
                "2023-07-08T15:20:35.123+02:00",
                "2023-07-08T15:20:36.123Z",
                "2023-07-08 15:20:37.123456",
                "2023-07-08T15:20:38.123456",
                "2023-07-08T15:20:39.123456+02:00",
        };

        for (String dateStr : testDates) {
            try {
                Date date = convertToDate(dateStr);
                System.out.println("Converted date: " + date);
            } catch (JdaoException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
