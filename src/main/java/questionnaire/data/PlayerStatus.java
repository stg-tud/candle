/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package questionnaire.data;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class PlayerStatus {

    private String name;

    private boolean hasStarted = false;
    private String currentTask = "";
    private List<String> allTaskIds;

    protected PlayerStatus() {
        // for serialization
    }

    public PlayerStatus(String name, List<String> allTaskIds) {
        this.name = name;
        this.allTaskIds = allTaskIds;
    }

    public String getName() {
        return name;
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public String getCurrentTaskId() {
        return currentTask;
    }

    public void toggleNext() {
        if (!hasStarted) {
            hasStarted = true;
            if (allTaskIds.size() > 0) {
                currentTask = allTaskIds.get(0);
            }
        } else {
            int prevIdx = allTaskIds.indexOf(currentTask);
            int newIdx = prevIdx + 1;
            if (newIdx < allTaskIds.size()) {
                currentTask = allTaskIds.get(newIdx);
            }
        }
    }

    public boolean hasNextTask() {
        int curIdx = allTaskIds.indexOf(currentTask);
        return allTaskIds.size() > (curIdx + 1);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}