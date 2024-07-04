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
 * @File:jdao: com.jdao.base :JdaoException.java
 * @Date:2017年10月23日
 * @Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * @Author: dong
 * @Desc:
 */
public class JdaoRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JdaoRuntimeException(String message) {
        super(message);
    }

    public JdaoRuntimeException(Throwable cause) {
        super(cause);
    }

    public JdaoRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
