/*
 * Copyright 2013 the original author or authors.
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
 */

package org.gradle.api.internal.changedetection;

import org.gradle.api.Action;
import org.gradle.api.execution.TaskInputChanges;

public abstract class StatefulTaskInputChanges implements TaskInputChanges {
    private boolean outOfDateProcessed;
    private boolean removedProcessed;

    public void outOfDate(final Action<? super InputFileChange> outOfDateAction) {
        if (outOfDateProcessed) {
            throw new IllegalArgumentException("Cannot process outOfDate files multiple times");
        }
        doOutOfDate(outOfDateAction);
        outOfDateProcessed = true;
    }

    protected abstract void doOutOfDate(Action<? super InputFileChange> outOfDateAction);

    public void removed(Action<? super InputFileChange> removedAction) {
        if (!outOfDateProcessed) {
            throw new IllegalStateException("Must first process outOfDate files before processing removed files");
        }
        if (removedProcessed) {
            throw new IllegalArgumentException("Cannot process removed files multiple times");
        }
        doRemoved(removedAction);
        removedProcessed = true;
    }

    protected abstract void doRemoved(Action<? super InputFileChange> removedAction);
}
