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

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
    private final Logger logger;
    private boolean isLog;

    private Log(boolean on, Class<?> clazz) {
        isLog = on;
        logger = Logger.getLogger(clazz == null ? "" : clazz.getName());
        logger.setLevel(Level.INFO);
    }


    public static Log newInstance() {
        return new Log(true, null);
    }

    public static Log newInstance(Class<?> clazz) {
        return new Log(false, clazz);
    }

    public static Log newInstance(boolean on, Class<?> clazz) {
        return new Log(on, clazz);
    }

    public void logOn(boolean isLog) {
        this.isLog = isLog;
    }

    public void log(String... log) {
        if (isLog) {
            if (log != null && log.length == 1) {
                logger.log(Level.INFO, log[0]);
            } else if (log != null && log.length > 1) {
                StringBuilder sb = new StringBuilder();
                for (String s : log) {
                    sb.append(s);
                }
                logger.log(Level.INFO, sb.toString());
            }
        }
    }
}
