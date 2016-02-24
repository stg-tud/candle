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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import questionnaire.data.Event.EventType;

public class EventTest {

    private static final int TIMING_THRESHOLD = 100;
    private Event sut;

    @Test
    public void noArgsConstructor() {
        sut = new Event();
    }

    @Test
    public void timeIsSet() {
        long actual = new Event().time.getTime();
        long expected = new Date().getTime();
        assertTrue(expected - actual < TIMING_THRESHOLD);
    }

    @Test
    public void equalsAndHashCode() {
        Event a = new Event();
        Event b = new Event();
        assertEquals(a, b);
        assertTrue(a.hashCode() == b.hashCode());
    }

    @Test
    public void enumCoverage() {
        EventType[] actuals = EventType.values();
        EventType[] expecteds = new EventType[] { EventType.PAGE_START, EventType.CODE_HIDDEN, EventType.SELECTION,
                EventType.SUBMISSION, EventType.TIMEOUT, EventType.RELOAD };
        assertArrayEquals(expecteds, actuals);

        EventType actual = EventType.valueOf("PAGE_START");
        EventType expected = EventType.PAGE_START;
        assertEquals(expected, actual);
    }
}