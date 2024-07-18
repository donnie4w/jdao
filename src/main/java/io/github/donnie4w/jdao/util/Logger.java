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

package io.github.donnie4w.jdao.util;


import java.util.logging.Level;

public class Logger {
    final static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Logger.class.getName());

    static boolean isVaild = false;

    static {
        logger.setLevel(Level.ALL);
    }

    public static boolean isVaild() {
        return isVaild;
    }

    public static void setLogger(boolean on) {
        isVaild = on;
    }

    public static void log(Object... args) {
        logger.info(convertToString(args));
    }

    public static void info(Object... args) {
        if (isVaild)
            logger.info(convertToString(args));
    }

    public static void warn(Object... args) {
        if (isVaild)
            logger.warning(convertToString(args));
    }

    public static void severe(Object... args) {
        if (isVaild)
            logger.severe(convertToString(args));
    }

    private static String convertToString(Object... args) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(arg).append(",");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}
