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

import io.github.donnie4w.jdao.handle.JdaoException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Serializer {
    public static byte[] encode(Map<String, Object> data) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(bos)) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Object value = entry.getValue();
                if (value == null) {
                    continue;
                }
                SeriaType type = SeriaType.fromClass(value.getClass());
                if (type == null) {
                    continue;
                }
                dos.writeByte(entry.getKey().getBytes().length);
                dos.write(entry.getKey().getBytes());
                dos.writeByte(type.getCode());

                switch (type) {
                    case BOOLEAN:
                        dos.writeByte(((Boolean) value) ? 1 : 0);
                        break;
                    case STRING:
                        dos.writeInt(((String) value).length());
                        dos.writeUTF((String) value);
                        break;
                    case DOUBLE:
                        dos.writeDouble((Double) value);
                        break;
                    case FLOAT:
                        dos.writeFloat((Float) value);
                        break;
                    case LONG:
                        dos.writeLong((Long) value);
                        break;
                    case INTEGER:
                        dos.writeInt((Integer) value);
                        break;
                    case SHORT:
                        dos.writeShort((Short) value);
                        break;
                    case BYTE:
                        dos.writeByte((Byte) value);
                        break;
                    case BYTE_ARRAY:
                        dos.writeInt(((byte[]) value).length);
                        dos.write((byte[]) value);
                        break;
                    case DATE:
                        dos.writeLong(((java.util.Date) value).getTime());
                        break;
                    case CHAR:
                        dos.writeChar((Character) value);
                        break;
                    case BIGDECIMAL:
                        BigDecimal decimal = (BigDecimal) value;
                        BigInteger unscaledValue = decimal.unscaledValue();
                        byte[] unscaledBytes = unscaledValue.toByteArray();
                        dos.writeByte(unscaledBytes.length);
                        dos.write(unscaledBytes);
                        dos.writeByte(decimal.scale());
                        break;
                    case BIGINTEGER:
                        BigInteger bigInt = (BigInteger) value;
                        byte[] bigIntBytes = bigInt.toByteArray();
                        dos.writeByte(bigIntBytes.length);
                        dos.write(bigIntBytes);
                        break;
                    default:
                        Logger.warn("Unsupported type: ", type);
                }
            }
            return bos.toByteArray();
        } catch (Exception e) {
            Logger.severe("[Serialize Encode]", e);
        }
        return null;
    }

    public static Map<String, Object> decode(byte[] data) throws JdaoException {

        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             DataInputStream dis = new DataInputStream(bis)) {
            Map<String, Object> result = new HashMap<>();

            while (dis.available() > 0) {
                int keyLen = dis.readByte();
                byte[] bs = new byte[keyLen];
                dis.read(bs);
                String key = new String(bs);

                byte valueType = dis.readByte();
                switch (valueType) {
                    case 1:
                        boolean b = dis.readByte() == 1;
                        result.put(key, b);
                        break;
                    case 2:
                        int strLen = dis.readInt();
                        String valueStr = dis.readUTF();
                        result.put(key, valueStr);
                        break;
                    case 3:
                        double valueDbl = dis.readDouble();
                        result.put(key, valueDbl);
                        break;
                    case 4:
                        float valueFlt = dis.readFloat();
                        result.put(key, valueFlt);
                        break;
                    case 5:
                        long valueLng = dis.readLong();
                        result.put(key, valueLng);
                        break;
                    case 6:
                        int valueInt = dis.readInt();
                        result.put(key, valueInt);
                        break;
                    case 7:
                        short valueShrt = dis.readShort();
                        result.put(key, valueShrt);
                        break;
                    case 8:
                        byte valueByt = dis.readByte();
                        result.put(key, valueByt);
                        break;
                    case 9:
                        char valueChar = dis.readChar();
                        result.put(key, valueChar);
                        break;
                    case 10:
                        int byteArrLen = dis.readInt();
                        byte[] valueByteArr = new byte[byteArrLen];
                        dis.readFully(valueByteArr);
                        result.put(key, valueByteArr);
                        break;
                    case 11:
                        long timestamp = dis.readLong();
                        result.put(key, new java.util.Date(timestamp));
                        break;
                    case 12:
                        byte bigIntLen = dis.readByte();
                        byte[] bigIntBytes = new byte[bigIntLen];
                        dis.read(bigIntBytes);
                        BigInteger bigInt = new BigInteger(bigIntBytes);
                        result.put(key, bigInt);
                        break;
                    case 13:
                        byte[] unscaledBytes = new byte[dis.readByte()];
                        dis.read(unscaledBytes);
                        int scale = dis.readByte();
                        BigInteger unscaledValue = new BigInteger(unscaledBytes);
                        BigDecimal decimal = new BigDecimal(unscaledValue, scale);
                        result.put(key, decimal);
                        break;
                    default:
                        Logger.warn("[Serialize Decode]Unsupported type: ", valueType);
                }
            }
            return result;
        } catch (Exception e) {
            throw new JdaoException(e);
        }
    }


}

