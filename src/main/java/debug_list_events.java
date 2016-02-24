import java.io.File;

import questionnaire.IOHelper;
import questionnaire.ObjectMapperSetup;
import questionnaire.data.Event;
import questionnaire.data.Event.EventType;

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

public class debug_list_events {
    private static IOHelper ioHelper = new IOHelper(new File("data"), ObjectMapperSetup.getObjectMapper());

    public static void main(String[] args) {
        for(Event e : ioHelper.readLogs()) {
            System.out.printf("%s -- %8s : %-12s @ %-18s -- %s\n", e.time, e.auth, e.type, e.taskId, e.answer);
        }
    }
}