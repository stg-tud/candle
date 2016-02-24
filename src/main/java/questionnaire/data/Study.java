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

import com.google.common.collect.Lists;

public class Study {

    private String name;
    private List<String> intro = Lists.newLinkedList();
    private List<String> exit = Lists.newLinkedList();
    private List<String> oop = Lists.newLinkedList();
    private List<String> rp = Lists.newLinkedList();

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addIntroTask(String taskId) {
        intro.add(taskId);
    }

    public List<String> getIntroTasks() {
        return intro;
    }

    public void addOopTask(String taskId) {
        oop.add(taskId);
    }

    public List<String> getOopTasks() {
        return oop;
    }

    public void addRpTask(String taskId) {
        rp.add(taskId);
    }

    public List<String> getRpTasks() {
        return rp;
    }

    public void addExitTask(String task) {
        exit.add(task);
    }

    public List<String> getExitTasks() {
        return exit;
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