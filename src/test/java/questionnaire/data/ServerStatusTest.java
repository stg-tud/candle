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
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ServerStatusTest {

    private ServerStatus sut;

    @Before
    public void setup() {
        sut = new ServerStatus();
    }

    @Test
    public void defaultValues() {
        assertEquals(0, sut.getNumTotal());
        assertEquals(0, sut.getNumRpParticipants());
        assertEquals(0, sut.getNumOopParticipants());
    }

    @Test
    public void increaseOop() {
        sut.addOopParticipant();
        assertEquals(1, sut.getNumTotal());
        assertEquals(1, sut.getNumOopParticipants());
    }

    @Test
    public void increaseRp() {
        sut.addRpParticipant();
        assertEquals(1, sut.getNumTotal());
        assertEquals(1, sut.getNumRpParticipants());
    }

    @Test
    public void equalsAndHashCode() {
        ServerStatus a = new ServerStatus();
        ServerStatus b = new ServerStatus();
        assertEquals(a, b);
        assertTrue(a.hashCode() == b.hashCode());
    }
}