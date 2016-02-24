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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class StudyTest {

    private Study sut;

    @Before
    public void setup() {
        sut = new Study();
    }

    @Test
    public void defaultValues() {
        assertNull(sut.getName());
        assertEquals(Lists.newLinkedList(), sut.getIntroTasks());
        assertEquals(Lists.newLinkedList(), sut.getOopTasks());
        assertEquals(Lists.newLinkedList(), sut.getRpTasks());
        assertEquals(Lists.newLinkedList(), sut.getExitTasks());
    }

    @Test
    public void nameCanBeSet() {
        sut.setName("a");
        assertEquals("a", sut.getName());
    }

    @Test
    public void introTasksCanBeAdded() {
        sut.addIntroTask("a");
        List<String> expected = Lists.newArrayList("a");
        assertEquals(expected, sut.getIntroTasks());
    }

    @Test
    public void introTaskCanBeAdded_2() {
        sut.addIntroTask("a");
        sut.addIntroTask("b");
        List<String> expected = Lists.newArrayList("a", "b");
        assertEquals(expected, sut.getIntroTasks());
    }

    @Test
    public void exitTasksCanBeAdded() {
        sut.addExitTask("a");
        List<String> expected = Lists.newArrayList("a");
        assertEquals(expected, sut.getExitTasks());
    }

    @Test
    public void exitTaskCanBeAdded_2() {
        sut.addExitTask("a");
        sut.addExitTask("b");
        List<String> expected = Lists.newArrayList("a", "b");
        assertEquals(expected, sut.getExitTasks());
    }

    @Test
    public void oopTaskCanBeAdded_2() {
        sut.addOopTask("a");
        sut.addOopTask("b");
        List<String> expected = Lists.newArrayList("a", "b");
        assertEquals(expected, sut.getOopTasks());
    }

    @Test
    public void rpTaskCanBeAdded_2() {
        sut.addRpTask("a");
        sut.addRpTask("b");
        List<String> expected = Lists.newArrayList("a", "b");
        assertEquals(expected, sut.getRpTasks());
    }

    @Test
    public void equalsAndHashCode() {
        Study a = new Study();
        Study b = new Study();
        assertEquals(a, b);
        assertTrue(a.hashCode() == b.hashCode());
    }
}