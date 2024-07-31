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
package io.github.donnie4w.jdao.handle;

/**
 * This exception is thrown when there is a class-related error in JDAO.
 */
public class JdaoClassException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new JdaoClassException with the specified detail message.
     *
     * @param message the detail message
     */
    public JdaoClassException(String message) {
        super(message);
    }

    /**
     * Constructs a new JdaoClassException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public JdaoClassException(Throwable cause) {
        super(cause);
    }


    /**
     * Constructs a new JdaoClassException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public JdaoClassException(String message, Throwable cause) {
        super(message, cause);
    }

}
