/*
 * Copyright 2016-2025 chronicle.software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package run.chronicle.account.util;

/**
 * This interface provides a method to handle JVM errors.
 * Implementations of this interface will define how these errors are handled.
 */
public interface ErrorListener {

    /**
     * This method handles JVM errors.
     * It takes a String message which provides details about the error.
     *
     * @param msg a String providing details about the JVM error.
     */
    void jvmError(String msg);
}
