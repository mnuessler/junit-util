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

import static org.apache.commons.lang3.StringUtils.defaultString;

import java.util.Collections;
import java.util.List;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;

public class FailureLogger extends TestWatcher {
    private Callback<String> loggingCallback;

    public FailureLogger(Callback<String> loggingCallback) {
        this.loggingCallback = loggingCallback;
    }

    @Override
    protected void failed(Throwable failure, Description description) {
        List<Throwable> failures = (failure instanceof MultipleFailureException) ? ((MultipleFailureException) failure)
                .getFailures() : Collections.singletonList(failure);

        for (Throwable t : failures) {
            StackTraceElement testClassElement = findStrackTraceForClass(t.getStackTrace(), description.getClassName());
            if (testClassElement != null) {
                log(formatMessage(description, testClassElement, t));
            }
        }
    }

    private StackTraceElement findStrackTraceForClass(StackTraceElement[] elements, String testClassName) {
        for (StackTraceElement element : elements) {
            if (element.getClassName().contains(testClassName)) {
                return element;
            }
        }
        return null;
    }

    private String formatMessage(Description description, StackTraceElement element, Throwable t) {
        return String.format("%s in %s#%s, line %d: %s", t.getClass().getCanonicalName(), description.getTestClass(),
                description.getMethodName(), element.getLineNumber(), defaultString(t.getMessage(), "<no message>"));
    }

    private void log(String msg) {
        loggingCallback.call(msg);
    }

    public interface Callback<T> {
        void call(T value);
    }

}
