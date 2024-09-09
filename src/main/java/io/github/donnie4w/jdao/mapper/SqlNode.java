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

interface SqlNode {
    AckContext apply(ParamContext context);

    void addSqlNode(SqlNode node);
}

class CrudNode implements SqlNode {
    final String content;
    final List<SqlNode> list = new ArrayList<SqlNode>();

    public CrudNode(String content) {
        this.content = content;
    }

    @Override
    public void addSqlNode(SqlNode node) {
        this.list.add(node);
    }

    @Override
    public AckContext apply(ParamContext context) {
        AckContext sb = new AckContext(content, context);
        for (SqlNode sqlNode : list) {
            sb.append(sqlNode.apply(context));
        }
        return sb;
    }
}
