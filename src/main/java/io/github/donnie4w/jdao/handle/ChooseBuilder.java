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

import io.github.donnie4w.jdao.util.RPNCalculator;

public class ChooseBuilder {
    private final SqlBuilder parentBuilder;
    private final Object context;
    private boolean conditionMet = false;

    public ChooseBuilder(SqlBuilder parentBuilder, Object context) {
        this.parentBuilder = parentBuilder;
        this.context = context;
    }

    public ChooseBuilder when(String expression, String sql, Object... params) {
        if (!conditionMet && RPNCalculator.evaluate(expression, context)) {
            parentBuilder.append(sql).addParameters(params);
            conditionMet = true;
        }
        return this;
    }

    public ChooseBuilder otherwise(String sql, Object... params) {
        if (!conditionMet) {
            parentBuilder.append(sql).addParameters(params);
        }
        return this;
    }
}
