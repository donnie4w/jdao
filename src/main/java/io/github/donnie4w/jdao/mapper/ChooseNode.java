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

import io.github.donnie4w.jdao.util.ExpressionEvaluator;
import io.github.donnie4w.jdao.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ChooseNode implements SqlNode {
    String content;
    String test;

    List<SqlNode> list = new ArrayList<SqlNode>();

    public ChooseNode(String content, String test) {
        if (Utils.stringValid(content)) {
            this.content = content;
        }
        if (Utils.stringValid(test)) {
            this.test = test;
        }
    }

    @Override
    public void addSqlNode(SqlNode node) {
        this.list.add(node);
    }

    @Override
    public AckContext apply(ParamContext context) {
        if (Utils.stringValid(test)) {
            if (!ExpressionEvaluator.evaluateExpression(test, context)) {
                return null;
            }
        }
        AckContext ac = new AckContext(content, context);
        for (SqlNode sqlNode : list) {
            if (sqlNode != null) {
                AckContext a = sqlNode.apply(context);
                if (a != null) {
                    ac.append(a);
                    break;
                }
            }
        }
        return ac;
    }
}

class WhenNode implements SqlNode {
    String content;
    String test;

    List<SqlNode> list = new ArrayList<SqlNode>();

    public WhenNode(String content, String test) {
        if (Utils.stringValid(content)) {
            this.content = content;
        }
        if (Utils.stringValid(test)) {
            this.test = test;
        }
    }

    @Override
    public void addSqlNode(SqlNode node) {
        this.list.add(node);
    }

    @Override
    public AckContext apply(ParamContext context) {
        if (Utils.stringValid(test)) {
            if (!ExpressionEvaluator.evaluateExpression(test, context)) {
                return null;
            }
        }
        AckContext ac = new AckContext(content, context);
        for (SqlNode sqlNode : list) {
            if (sqlNode != null) {
                AckContext a = sqlNode.apply(context);
                if (a != null) {
                    ac.append(a);
                }
            }
        }
        return ac;
    }
}

class OtherWise implements SqlNode {
    String content;
    String test;

    List<SqlNode> list = new ArrayList<SqlNode>();

    public OtherWise(String content, String test) {
        if (Utils.stringValid(content)) {
            this.content = content;
        }
        if (Utils.stringValid(test)) {
            this.test = test;
        }
    }

    @Override
    public void addSqlNode(SqlNode node) {
        this.list.add(node);
    }

    @Override
    public AckContext apply(ParamContext context) {
        if (Utils.stringValid(test)) {
            if (!ExpressionEvaluator.evaluateExpression(test, context)) {
                return null;
            }
        }
        AckContext ac = new AckContext(content, context);
        for (SqlNode sqlNode : list) {
            if (sqlNode != null) {
                AckContext a = sqlNode.apply(context);
                if (a != null) {
                    ac.append(a);
                }
            }
        }
        return ac;
    }
}


