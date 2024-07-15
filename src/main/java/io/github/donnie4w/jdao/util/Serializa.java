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

import io.github.donnie4w.jdao.base.Table;
import io.github.donnie4w.jdao.handle.JdaoException;

import java.io.*;

public class Serializa {

    public static byte[] encode(Table object) throws JdaoException {
        try (ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(byteArrayOut)) {
            out.writeObject(object);
            return byteArrayOut.toByteArray();
        } catch (IOException e) {
            throw new JdaoException(e);
        }
    }

    public static <T extends Table> T decode(byte[] bytes, Class<T> clazz) throws JdaoException {
        try (ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(bytes);
             ObjectInputStream in = new ObjectInputStream(byteArrayIn)) {
            Object object = in.readObject();
            clazz.cast(object);
            T table = (T) object;
            table.toJdao();
            return table;
        } catch (IOException | ClassNotFoundException e) {
            throw new JdaoException(e);
        }
    }
}
