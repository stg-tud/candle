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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import questionnaire.data.Task.AnswerType;

public class TaskTest {

    private Task sut;

    @Before
    public void setup() {
        sut = new Task();
    }

    @Test
    public void defaultValues() {
        assertNotNull(sut.taskType);
        assertNull(sut.options);
        assertEquals(false, sut.taskType.isBlind);
        assertEquals(false, sut.taskType.isMeasured);
        assertEquals(0, sut.taskType.codeTime);
        assertEquals(0, sut.taskType.questionTime);
        assertEquals(AnswerType.MULTIPLE_CHOICE, sut.answerType);
    }

    @Test
    public void hasAnswer() {
        sut = new Task();

        assertFalse(sut.hasAnswer());
        sut.answer = "a";
        assertTrue(sut.hasAnswer());
    }

    @Test
    public void hashCodeAndEquals() {
        Task a = new Task();
        Task b = new Task();

        assertEquals(a, b);
        assertTrue(a.hashCode() == b.hashCode());
    }
}