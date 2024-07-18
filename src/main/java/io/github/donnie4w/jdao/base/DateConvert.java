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
import java.util.Date;

public class DateConvert {


    public static Date convertToDate(String dateStr) throws JdaoException {
        if (dateStr == null || dateStr.length() == 0){
            return null;
        }

        try {
            StringBuilder pattern = new StringBuilder();
            int count = 1;
            boolean isdecimals = true;
            boolean isLocalDate = false;
            boolean isLocalDateTime = false;
            boolean isOffsetDateTime = false;
            boolean isZonedDateTime = false;
            StringBuilder zoneSB = null;
            ZoneId zoneId = null;
            while (count <= dateStr.length()) {
                char c = dateStr.charAt(count - 1);
                switch (count) {
                    case 4:
                        pattern.append("yyyy");
                        break;
                    case 5:
                        pattern.append(c);
                        break;
                    case 7:
                        pattern.append("MM");
                        break;
                    case 8:
                        pattern.append(c);
                        break;
                    case 10:
                        pattern.append("dd");
                        isLocalDate = true;
                        break;
                    case 11:
                        if (c == 'T') {
                            pattern.append("'T'");
                        } else {
                            pattern.append(c);
                        }
                        break;
                    case 13:
                        pattern.append("HH");
                        break;
                    case 14:
                        pattern.append(c);
                        break;
                    case 16:
                        pattern.append("mm");
                        break;
                    case 17:
                        pattern.append(c);
                        break;
                    case 19:
                        pattern.append("ss");
                        isLocalDateTime = true;
                        break;
                    case 20:
                        pattern.append(c);
                        break;
                    default:
                        if (count > 20) {
                            if (Character.isDigit(c) && isdecimals) {
                                pattern.append("S");
                            } else if (c == '+' || c == '-') {
                                isOffsetDateTime = true;
                                isdecimals = false;
                                pattern.append("XX");
                            } else if (c == ':') {
                                pattern.append("X");
                            } else if (c == 'Z') {
                                isdecimals = false;
                                isZonedDateTime = true;
                                pattern.append("X");
                            } else if (c == '[') {
                                isdecimals = false;
                                zoneSB = new StringBuilder();
                            } else if (zoneSB != null && zoneId == null && c != ']') {
                                zoneSB.append(c);
                            } else if (c == ']' && zoneSB != null && zoneSB.length() > 0) {
                                zoneId = ZoneId.of(zoneSB.toString());
                            }
                        }
                }
                count++;
            }

            if (zoneId != null) {
                dateStr = dateStr.replace("[" + zoneSB.toString() + "]", "");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern.toString());
                if (isZonedDateTime) {
                    ZonedDateTime zdt = ZonedDateTime.parse(dateStr, formatter);
                    zdt = zdt.withZoneSameInstant(zoneId);
                    return Date.from(Instant.from(zdt));
                } else if (isOffsetDateTime) {
                    OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateStr, formatter);
                    return Date.from(offsetDateTime.atZoneSameInstant(zoneId).toInstant());
                } else if (isLocalDateTime) {
                    LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
                    return Date.from(Instant.from(localDateTime.atZone(zoneId)));
                } else if (isLocalDate) {
                    LocalDate localDate = LocalDate.parse(dateStr, formatter);
                    return Date.from(localDate.atStartOfDay(zoneId).toInstant());
                }
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern.toString());
            if (isZonedDateTime) {
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateStr, formatter);
                return Date.from(zonedDateTime.toInstant());
            } else if (isOffsetDateTime) {
                OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateStr, formatter);
                return Date.from(offsetDateTime.toInstant());
            } else if (isLocalDateTime) {
                LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
                return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            } else if (isLocalDate) {
                LocalDate localDate = LocalDate.parse(dateStr, formatter);
                return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
        } catch (Exception e) {
           throw new JdaoException(e);
        }
        return null;
    }

    @Test
    public void testConvert() throws JdaoException {
        String[] testDates = {
                "2023",
                "2023-07-08",
                "2023-07-08T15:20:31",
                "2023-07-08T15:20:32",
                "2023-07-08T15:20:33.12",
                "2023-07-08T15:20:34.123",
                "2023-07-08T15:20:35.123+02:00",
                "2023-07-08T15:20:36.123Z",
                "2023-07-08 15:20:37.123456",
                "2023-07-08T15:20:38.123456[Europe/Paris]",
                "2023-07-08T15:20:39.123456+02:00",
                "2024-07-09 01:19:40.4622478+08:00"
        };

        for (String dateStr : testDates) {
            Date date = convertToDate(dateStr);
            System.out.println("Converted date: " + date);
        }
    }
}
