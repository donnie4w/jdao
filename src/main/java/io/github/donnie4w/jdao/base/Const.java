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

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Const {
    public static final Map<Integer, DateTimeFormatter> FORMATTERS = new ConcurrentHashMap<>();

    private final static String ANSIC_PATTERN = "EEE MMM d HH:mm:ss yyyy";
    private final static String UnixDate_PATTERN = "EEE MMM d HH:mm:ss z yyyy";
    private final static String RubyDate_PATTERN = "EEE MMM d HH:mm:ss Z yyyy";
    private final static String RFC822_PATTERN = "d MMM yy HH:mm z";
    private final static String RFC822Z_PATTERN = "d MMM yy HH:mm Z";
    private final static String RFC850_PATTERN = "EEEE, dd-MMM-yy HH:mm:ss z";
    private final static String RFC1123_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";
    private final static String RFC1123Z_PATTERN = "EEE, dd MMM yyyy HH:mm:ss Z";
    private final static String RFC3339_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX";
    private final static String RFC3339Nano_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSXXX";
    private final static String Kitchen_PATTERN = "h:mm a";
    private final static String Stamp_PATTERN = "MMM d HH:mm:ss";
    private final static String StampMilli_PATTERN = "MMM d HH:mm:ss.SSS";
    private final static String StampMicro_PATTERN = "MMM d HH:mm:ss.SSSSSS";
    private final static String StampNano_PATTERN = "MMM d HH:mm:ss.SSSSSSSSS";
    private final static String DateTime_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final static String DateOnly_PATTERN = "yyyy-MM-dd";
    private final static String TimeOnly_PATTERN = "HH:mm:ss";
    static final Map<String, Boolean> keyMap = new HashMap();

    static {
        FORMATTERS.put(ANSIC_PATTERN.length(), DateTimeFormatter.ofPattern(ANSIC_PATTERN));
        FORMATTERS.put(UnixDate_PATTERN.length(), DateTimeFormatter.ofPattern(UnixDate_PATTERN));
        FORMATTERS.put(RubyDate_PATTERN.length(), DateTimeFormatter.ofPattern(RubyDate_PATTERN));
        FORMATTERS.put(RFC822_PATTERN.length(), DateTimeFormatter.ofPattern(RFC822_PATTERN));
        FORMATTERS.put(RFC822Z_PATTERN.length(), DateTimeFormatter.ofPattern(RFC822Z_PATTERN));
        FORMATTERS.put(RFC850_PATTERN.length(), DateTimeFormatter.ofPattern(RFC850_PATTERN));
        FORMATTERS.put(RFC1123_PATTERN.length(), DateTimeFormatter.ofPattern(RFC1123_PATTERN));
        FORMATTERS.put(RFC1123Z_PATTERN.length(), DateTimeFormatter.ofPattern(RFC1123Z_PATTERN));
        FORMATTERS.put(RFC3339_PATTERN.length(), DateTimeFormatter.ofPattern(RFC3339_PATTERN));
        FORMATTERS.put(RFC3339Nano_PATTERN.length(), DateTimeFormatter.ofPattern(RFC3339Nano_PATTERN));
        FORMATTERS.put(Kitchen_PATTERN.length(), DateTimeFormatter.ofPattern(Kitchen_PATTERN));
        FORMATTERS.put(Stamp_PATTERN.length(), DateTimeFormatter.ofPattern(Stamp_PATTERN));
        FORMATTERS.put(StampMilli_PATTERN.length(), DateTimeFormatter.ofPattern(StampMilli_PATTERN));
        FORMATTERS.put(StampMicro_PATTERN.length(), DateTimeFormatter.ofPattern(StampMicro_PATTERN));
        FORMATTERS.put(StampNano_PATTERN.length(), DateTimeFormatter.ofPattern(StampNano_PATTERN));
        FORMATTERS.put(DateTime_PATTERN.length(), DateTimeFormatter.ofPattern(DateTime_PATTERN));
        FORMATTERS.put(DateOnly_PATTERN.length(), DateTimeFormatter.ofPattern(DateOnly_PATTERN));
        FORMATTERS.put(TimeOnly_PATTERN.length(), DateTimeFormatter.ofPattern(TimeOnly_PATTERN));
    }

    public static boolean isKey(String keyName){
        switch (keyName){
            case "public":
            case "protected":
            case "private":
            case "class":
            case "extends":
            case "implements":
            case "abstract":
            case "final":
            case "new":
            case "this":
            case "super":
            case "if":
            case "else":
            case "switch":
            case "case":
            case "default":
            case "for":
            case "while":
            case "do":
            case "break":
            case "continue":
            case "return":
            case "try":
            case "catch":
            case "finally":
            case "throw":
            case "throws":
            case "boolean":
            case "byte":
            case "goto":
            case "const":
            case "char":
            case "short":
            case "int":
            case "long":
            case "float":
            case "double":
            case "void":
            case "null":
            case "true":
            case "false":
            case "static":
            case "transient":
            case "volatile":
            case "strictfp":
            case "assert":
            case "enum":
                return  true;
            default:
                return  false;
        }
    }

}
