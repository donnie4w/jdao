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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil {

    public static Object getFieldValue(Object obj, String fieldName) {
        Object r = null;
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            if (field != null) {
                field.setAccessible(true);
                r = field.get(obj);
            }
            if (r == null) {
                Method getter = obj.getClass().getDeclaredMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                getter.setAccessible(true);
                r = getter.invoke(obj);
            }
        } catch (Exception e) {
        }
        return r;
    }
}
