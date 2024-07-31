
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

import io.github.donnie4w.jdao.base.Table;
import io.github.donnie4w.jdao.handle.JdaoException;

public class MapperTable extends Table {
    protected MapperTable(String tablename) {
        super(tablename, MapperTable.class);
    }

    @Override
    public void toJdao() {
    }

    @Override
    public MapperTable copy(Object h) {
        return this;
    }

    @Override
    public void scan(String fieldname, Object obj) throws JdaoException {
    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }

    @Override
    public MapperTable decode(byte[] bs) throws JdaoException {
        return this;
    }
}
