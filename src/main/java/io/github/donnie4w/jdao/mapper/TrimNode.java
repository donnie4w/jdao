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

public class TrimNode implements SqlNode {
    private final String prefix;
    private final String suffix;
    private final String[] prefixeOverrides;
    private final String[] suffixOverrides;

    List<SqlNode> list = new ArrayList<>();

    @Override
    public void addSqlNode(SqlNode node) {
        this.list.add(node);
    }

    public TrimNode(String prefix, String suffix, String prefixeOverrides, String suffixOverrides) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.prefixeOverrides = prefixeOverrides != null ? prefixeOverrides.split("\\|") : null;
        this.suffixOverrides = suffixOverrides != null ? suffixOverrides.split("\\|") : null;
    }

    @Override
    public AckContext apply(ParamContext context) {
        AckContext ac = new AckContext(null, context);
        for (SqlNode sqlNode : list) {
            if (sqlNode != null) {
                AckContext a = sqlNode.apply(context);
                if (a != null) {
                    ac.append(a);
                }
            }
        }

        StringBuilder sqlbuilder = ac.getSqlbuilder();
        if (sqlbuilder == null) {
            return ac;
        }

        while (Character.isWhitespace(sqlbuilder.charAt(0))) {
            sqlbuilder.deleteCharAt(0);
        }

        if (prefixeOverrides != null) {
            for (String toRemove : prefixeOverrides) {
                if (sqlbuilder.indexOf(toRemove) == 0) {
                    sqlbuilder.delete(0, toRemove.length());
                    break;
                }
            }
        }
        if (suffixOverrides != null) {
            for (String toRemove : suffixOverrides) {
                int startIdx = sqlbuilder.length() - toRemove.length();
                if (startIdx >= 0 && sqlbuilder.lastIndexOf(toRemove) == startIdx) {
                    sqlbuilder.delete(startIdx, sqlbuilder.length());
                    break;
                }
            }
        }
        if (prefix != null) {
            sqlbuilder.insert(0, prefix);
        }
        if (suffix != null) {
            sqlbuilder.append(suffix);
        }
        return ac;
    }

}
