import java.io.File;

import questionnaire.IOHelper;
import questionnaire.ObjectMapperSetup;
import questionnaire.data.Event;
import questionnaire.data.Event.EventType;

/**
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Contributors: - Sebastian Proksch
 */

public class debug_count_participant_loss {

    public static final String FIRST_QUESTION = "bench_PERSONAL_1";
    public static final String FIRST_OP_QUESTION = "test_OO_1";
    public static final String FIRST_RP_QUESTION = "test_RP_1";
    public static final String LAST_QUESTION = "exit_10";

    private static IOHelper ioHelper = new IOHelper(new File("data"), ObjectMapperSetup.getObjectMapper());

    public static void main(String[] args) {

        int fq = 0;
        int foq = 0;
        int frq = 0;
        int lq = 0;

        for (Event e : ioHelper.readLogs()) {
            if (e.type != EventType.PAGE_START) {
                continue;
            }

            if (e.taskId.equals(FIRST_QUESTION)) {
                fq++;
            }

            if (e.taskId.equals(FIRST_OP_QUESTION)) {
                foq++;
            }

            if (e.taskId.equals(FIRST_RP_QUESTION)) {
                frq++;
            }

            if (e.taskId.equals(LAST_QUESTION)) {
                lq++;
            }
            // Task t = ioHelper.readTask(e.taskId);
        }

        System.out.println("statistics:");
        System.out.println("===========");
        System.out.println("#students that saw the first intro question: " + fq);
        System.out.println("#students that saw the first OOP question: " + foq);
        System.out.println("#students that saw the first RP question: " + frq);
        System.out.println("#students that saw the last question: " + lq);
    }
}