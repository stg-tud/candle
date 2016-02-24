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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ServerStatus {

    private int numOop = 0;
    private int numRp = 0;

    public int getNumTotal() {
        return numOop + numRp;
    }

    public int getNumRpParticipants() {
        return numRp;
    }

    public int getNumOopParticipants() {
        return numOop;
    }

    public void addRpParticipant() {
        numRp++;
    }

    public void addOopParticipant() {
        numOop++;
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