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

import java.util.ArrayList;
import java.util.List;

class Foreach implements SqlNode {
    final String text;
    final String collection;
    final String item;
    final String open;
    final String index;
    final String close;
    final String separator;
    final List<SqlNode> list = new ArrayList<>();

    @Override
    public void addSqlNode(SqlNode node) {
        list.add(node);
    }

    public Foreach(String text, String collection, String item, String index, String open, String close, String separator) {
        this.text = text;
        this.collection = collection;
        this.item = item;
        this.open = open;
        this.close = close;
        this.index = index;
        this.separator = separator;
    }

    @Override
    public AckContext apply(ParamContext context) {
        ForeachContext collectionContext;
        if (this.collection != null && !this.collection.isEmpty()) {
            if ("list".equals(this.collection) || "array".equals(this.collection)) {
                collectionContext = new ForeachContext(context);
            } else {
                Object value = context.get(this.collection);
                if (value != null) {
                    collectionContext = new ForeachContext(context, value);
                } else {
                    collectionContext = new ForeachContext(context);
                }
            }
        } else {
            collectionContext = new ForeachContext(context);
        }

        StringBuilder sqlBuilder = new StringBuilder();
        List<Object> params = new ArrayList<>();
        if (collectionContext.getArray() != null) {
            for (int i = 0; i < collectionContext.getArray().length; i++) {
                if (this.index != null && !this.index.isEmpty()) {
                    collectionContext.setIndex(this.index, i);
                }
                if (this.item != null && !this.item.isEmpty()) {
                    collectionContext.setItem(this.item, collectionContext.getArray()[i]);
                }
                AckContext ackContext = new AckContext(text, collectionContext);
                for (SqlNode node : list) {
                    ackContext.append(node.apply(collectionContext));
                }
                if (ackContext.getSqlbuilder() != null && ackContext.getSqlbuilder().length() > 0) {
                    if (this.separator != null && !this.separator.isEmpty() && sqlBuilder.length() > 0 && i > 0) {
                        sqlBuilder.append(separator);
                    }
                    sqlBuilder.append(ackContext.getSqlbuilder());
                    if (ackContext.getParams() != null) {
                        params.addAll(ackContext.getParams());
                    }
                }
            }
        } else {
            int i = 0;
            for (String key : collectionContext.getMap().keySet()) {
                Object value = collectionContext.get(key);
                if (this.index != null && !this.index.isEmpty()) {
                    collectionContext.setIndex(this.index, i);
                }
                if (this.item != null && !this.item.isEmpty()) {
                    collectionContext.setItem(this.item, value);
                }
                AckContext ackContext = new AckContext(text, collectionContext);
                for (SqlNode node : list) {
                    ackContext.append(node.apply(collectionContext));
                }
                if (ackContext.getSqlbuilder() != null && ackContext.getSqlbuilder().length() > 0) {
                    if (this.separator != null && !this.separator.isEmpty() && sqlBuilder.length() > 0 && i > 0) {
                        sqlBuilder.append(separator);
                    }
                    sqlBuilder.append(ackContext.getSqlbuilder());
                    if (ackContext.getParams() != null) {
                        params.addAll(ackContext.getParams());
                    }
                }
                i++;
            }
        }
        AckContext result = new AckContext(sqlBuilder, params);
        if (open != null && !open.isEmpty()) {
            result.getSqlbuilder().insert(0, open);
        }
        if (close != null && !close.isEmpty()) {
            result.getSqlbuilder().append(close);
        }
        return result;
    }
}