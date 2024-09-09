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

package io.github.donnie4w.jdao;

import io.github.donnie4w.jdao.base.DateConvert;
import io.github.donnie4w.jdao.handle.JdaoException;
import org.junit.Test;
import java.util.Date;

public class DateConvertTest {
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
            Date date = DateConvert.convertToDate(dateStr);
            System.out.println("Converted date: " + date);
        }
    }
}
