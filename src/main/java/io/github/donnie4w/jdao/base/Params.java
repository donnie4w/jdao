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

/**
 * Parameter object of a stored procedure
 */
public abstract class Params {

    /**
     * Creates a new In instance.
     *
     * @param o the value to be included in the IN clause
     * @return a new In instance
     */
    public static In IN(Object o) {
        return new In(o);
    }

    /**
     * Creates a new Out instance.
     *
     * @param t the type of the value
     * @return a new Out instance
     */
    public static Out OUT(Type t) {
        return new Out(t);
    }

    /**
     * Creates a new InOut instance.
     *
     * @param o the value to be used in the query
     * @param t the type of the value
     * @return a new InOut instance
     */
    public static InOut INOUT(Object o, Type t) {
        return new InOut(o, t);
    }

    /**
     * Gets the value of the parameter.
     *
     * @return the value of the parameter
     */
    public abstract Object getValue();

    /**
     * Gets the types of the parameter.
     *
     * @return the types of the parameter
     */
    public abstract int getTypes();

}