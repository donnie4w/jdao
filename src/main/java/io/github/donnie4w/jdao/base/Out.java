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

import java.sql.Types;

/**
 * Output parameters to the stored procedure
 */
public class Out extends Params {

    private final int type;

    /**
     * Constructs a new Out instance.
     *
     * @param t the type of the value
     */
    public Out(Type t) {
        this.type = types2java(t);
    }

    public static int types2java(Type type) {
        switch (type) {
            case TINYINT:
                return Types.TINYINT;
            case INTEGER:
                return Types.INTEGER;
            case SMALLINT:
                return Types.SMALLINT;
            case DECIMAL:
                return Types.DECIMAL;
            case NUMERIC:
                return Types.NUMERIC;
            case DOUBLE:
                return Types.DOUBLE;
            case FLOAT:
                return Types.FLOAT;
            case REAL:
                return Types.REAL;
            case BIGINT:
                return Types.BIGINT;
            case TIMESTAMP:
                return Types.TIMESTAMP;
            case DATE:
                return Types.DATE;
            case TIME:
                return Types.TIME;
            case LONGVARBINARY:
                return Types.LONGVARBINARY;
            case BINARY:
                return Types.BINARY;
            case VARBINARY:
                return Types.VARBINARY;
            case BIT:
                return Types.BIT;
            default:
                return Types.VARCHAR;
        }
    }

    @Override
    public int getTypes() {
        return this.type;
    }

    @Override
    public Object getValue() {
        return null;
    }

}
