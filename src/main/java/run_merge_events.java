import java.io.File;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import questionnaire.IOHelper;
import questionnaire.ObjectMapperSetup;
import questionnaire.data.Event;

/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastian Proksch - initial API and implementation
 */

public class run_merge_events {
    private static final ObjectMapper mapper = ObjectMapperSetup.getObjectMapper();
    private static IOHelper ioHelper1 = new IOHelper(new File("/Users/seb/versioned_code/empirical-study-platform/data1"), mapper);
    private static IOHelper ioHelper2 = new IOHelper(new File("/Users/seb/versioned_code/empirical-study-platform/data2"), mapper);
    private static IOHelper ioHelperMerged = new IOHelper(new File("/Users/seb/versioned_code/empirical-study-platform/data-merged"), mapper);

    public static void main(String[] args) {

        List<Event> e1 = ioHelper1.readLogs();
        for (Event e : e1) {
            ioHelperMerged.log(e);
        }
        List<Event> e2 = ioHelper2.readLogs();
        for (Event e : e2) {
            ioHelperMerged.log(e);
        }

        List<Event> eM = ioHelperMerged.readLogs();
        System.out.printf("%d + %d = %d?\n", e1.size(), e2.size(), eM.size());
    }
}
