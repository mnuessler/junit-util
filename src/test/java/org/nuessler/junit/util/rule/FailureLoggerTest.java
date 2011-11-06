/**
 * Copyright 2011 Matthias Nuessler <m.nuessler@web.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nuessler.junit.util.rule;

import static org.hamcrest.core.IsEqual.equalTo;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.nuessler.junit.util.rule.FailureLogger.Callback;

public class FailureLoggerTest {

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Rule
    public FailureLogger logger = new FailureLogger(new Callback<String>() {
        @Override
        public void call(String logMsg) {
            System.out.println(logMsg);
        }
    });

    @Test
    public void test() {
        collector.checkThat(1, equalTo(2));
        collector.addError(new Exception());
    }

    @Test
    public void test2() {
        assertThat(1, equalTo(2));
    }

    private <T> void assertThat(T value, Matcher<T> matcher) {
        collector.checkThat(value, matcher);
    }

}
