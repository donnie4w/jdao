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

import io.github.donnie4w.jdao.util.Utils;

import java.util.ArrayList;
import java.util.List;

class WhereNode implements SqlNode {
    final String content;
    final List<SqlNode> nodes = new ArrayList();

    public WhereNode(String content) {
        this.content = content;
    }

    @Override
    public void addSqlNode(SqlNode node) {
        this.nodes.add(node);
    }

    @Override
    public AckContext apply(ParamContext context) {
        StringBuilder contentdb = new StringBuilder();
        if (Utils.stringValid(content)) {
            contentdb.append(content);
        }

        AckContext ackContext = new AckContext(contentdb.toString(), context);

        for (SqlNode sqlNode : nodes) {
            AckContext a = sqlNode.apply(context);
            if (a != null) {
                ackContext.append(a);
            }
        }
        StringBuilder sqlbuilder = ackContext.getSqlbuilder();
        if (sqlbuilder == null || sqlbuilder.length() == 0) {
            return null;
        }

        while (Character.isWhitespace(sqlbuilder.charAt(0))) {
            sqlbuilder.deleteCharAt(0);
        }

        if (sqlbuilder.length() > 4) {
            if (sqlbuilder.substring(0, 4).toLowerCase().startsWith("and ")) {
                sqlbuilder.delete(0, 4);
            } else if (sqlbuilder.substring(0, 3).toLowerCase().startsWith("or ")) {
                sqlbuilder.delete(0, 3);
            }
        }
        boolean hasPrefix = false;
        if (sqlbuilder.length() > 6) {
            if (sqlbuilder.substring(0, 6).toLowerCase().startsWith("where ")) {
                hasPrefix = true;
            }
        }

        if (!hasPrefix) {
            sqlbuilder.insert(0, "WHERE ");
        }

        return ackContext;
    }

}
