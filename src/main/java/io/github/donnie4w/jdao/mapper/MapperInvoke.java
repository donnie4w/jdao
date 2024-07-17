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

package io.github.donnie4w.jdao.mapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class MapperInvoke implements InvocationHandler {

    private final JdaoMapper jdaoMapper;

    public MapperInvoke(JdaoMapper jdaoMapper) {
        this.jdaoMapper = jdaoMapper;
    }

    /**
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the {@code Method} instance corresponding to
     *               the interface method invoked on the proxy instance.  The declaring
     *               class of the {@code Method} object will be the interface that
     *               the method was declared in, which may be a superinterface of the
     *               proxy interface that the proxy class inherits the method through.
     * @param args   an array of objects containing the values of the
     *               arguments passed in the method invocation on the proxy instance,
     *               or {@code null} if interface method takes no arguments.
     *               Arguments of primitive types are wrapped in instances of the
     *               appropriate primitive wrapper class, such as
     *               {@code java.lang.Integer} or {@code java.lang.Boolean}.
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String mapperId = method.getDeclaringClass().getName() + "." + method.getName();
        ParamBean pb = MapperParser.getParamBean(mapperId);
        if (args != null && args.length ==1) {
            switch (pb.getSqlType()) {
                case SELECT:
                    if (List.class.isAssignableFrom(method.getReturnType())) {
                        return jdaoMapper.selectList(mapperId, args[0]);
                    } else if (method.getReturnType().isArray()) {
                        return jdaoMapper.selectArray(mapperId, args[0]);
                    } else {
                        return jdaoMapper.selectOne(mapperId, args[0]);
                    }
                case INSERT:
                    return jdaoMapper.insert(mapperId, args[0]);
                case UPDATE:
                    return jdaoMapper.update(mapperId, args[0]);
                case DELETE:
                    return jdaoMapper.delete(mapperId, args[0]);
                default:
                    return null;
            }
        }else{
            switch (pb.getSqlType()) {
                case SELECT:
                    if (List.class.isAssignableFrom(method.getReturnType())) {
                        return jdaoMapper.selectList(mapperId, args);
                    } else if (method.getReturnType().isArray()) {
                        return jdaoMapper.selectArray(mapperId, args);
                    } else {
                        return jdaoMapper.selectOne(mapperId, args);
                    }
                case INSERT:
                    return jdaoMapper.insert(mapperId, args);
                case UPDATE:
                    return jdaoMapper.update(mapperId, args);
                case DELETE:
                    return jdaoMapper.delete(mapperId, args);
                default:
                    return null;
            }
        }
    }
}
