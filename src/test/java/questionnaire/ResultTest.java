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
package questionnaire;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import questionnaire.Result.State;

public class ResultTest {

    private static final String SOME_STRING = "a";

    @Test
    public void equalsAndHashCode() {
        Result<Void> a = new Result<Void>();
        Result<Void> b = new Result<Void>();
        assertEquals(a, b);
        assertTrue(a.hashCode() == b.hashCode());
    }

    @Test
    public void ok() {
        Result<Void> actual = Result.ok();
        Result<Void> expected = new Result<Void>();
        expected.status = State.OK;
        expected.message = null;
        expected.data = null;
        assertEquals(expected, actual);
    }

    @Test
    public void ok_2() {
        Result<String> actual = Result.ok(SOME_STRING);
        Result<String> expected = new Result<String>();
        expected.status = State.OK;
        expected.message = null;
        expected.data = SOME_STRING;
        assertEquals(expected, actual);
    }

    @Test
    public void fail() {
        Result<String> actual = Result.fail(SOME_STRING);
        Result<String> expected = new Result<String>();
        expected.status = State.FAIL;
        expected.message = SOME_STRING;
        expected.data = null;
        assertEquals(expected, actual);
    }

    @Test
    public void fail_2() {
        Result<String> actual = Result.fail(mockThrowable());
        Result<String> expected = new Result<String>();
        expected.status = State.FAIL;
        expected.message = getFailString();
        expected.data = null;
        assertEquals(expected, actual);
    }

    @Test
    public void argumentIsNull() {
        Object o = null;
        Result<Void> actual = Result.fail("", o);
        assertEquals("", actual.message);
    }

    @Test
    public void argumentIsEmptyArray() {
        Object[] o = new Object[0];
        Result<Void> actual = Result.fail("", o);
        assertEquals("", actual.message);
    }

    @Test
    public void stringIsFormatted() {
        Result<Void> actual = Result.fail("a %s", "b");
        assertEquals("a b", actual.message);
    }

    private String getFailString() {
        return "class java.lang.RuntimeException: \"MESSAGE\"<br />\nCLASS.METHOD1(Unknown Source)<br />\nCLASS.METHOD2(Unknown Source)<br />\n";
    }

    private Throwable mockThrowable() {
        StackTraceElement[] es = new StackTraceElement[2];
        es[0] = mockStacktraceElement(1);
        es[1] = mockStacktraceElement(2);

        Throwable t = new RuntimeException("MESSAGE");
        t.setStackTrace(es);

        return t;
    }

    private StackTraceElement mockStacktraceElement(int id) {
        return new StackTraceElement("CLASS", "METHOD" + id, null, 0);
    }

    @Test
    public void enumCoverage() {
        State[] actuals = Result.State.values();
        State[] expecteds = new State[] { State.OK, State.FAIL };
        assertArrayEquals(expecteds, actuals);

        State actual = Result.State.valueOf("OK");
        State expected = State.OK;
        assertEquals(expected, actual);
    }
}