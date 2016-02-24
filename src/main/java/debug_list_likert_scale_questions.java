import java.io.File;
import java.util.Map;

import questionnaire.IOHelper;
import questionnaire.ObjectMapperSetup;
import questionnaire.data.Event;
import questionnaire.data.Event.EventType;
import questionnaire.data.Task;
import questionnaire.data.Task.AnswerType;

import com.google.common.collect.Maps;

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

public class debug_list_likert_scale_questions {

    private static IOHelper ioHelper = new IOHelper(new File("data"), ObjectMapperSetup.getObjectMapper());

    private static Map<String, Map<LikertValue, Integer>> answers = Maps.newLinkedHashMap();

    public static void main(String[] args) {

        for (Event e : ioHelper.readLogs()) {
            if (e.type != EventType.SUBMISSION) {
                continue;
            }

            if (e.answer.contains("gree")) {
                Task t = ioHelper.readTask(e.taskId);
                if (t.answerType == AnswerType.MULTIPLE_CHOICE) {
                    count(e.taskId, toLikert(e.answer));
                }
            }
        }

        for (String taskId : answers.keySet()) {
            Task t = ioHelper.readTask(taskId);

            System.out.println(t.question);

            
            Map<LikertValue, Integer> lvs = answers.get(taskId);

            for (LikertValue lv : LikertValue.values()) {
                int num = 0;
                if (lvs.containsKey(lv)) {
                    num = lvs.get(lv);
                }
                System.out.printf("%2s: %2d (%.1f%%)\n", lv, num, (100 *num/89f));
            }
            
            System.out.println();
        }

    }

    private static void count(String taskId, LikertValue answer) {
        Map<LikertValue, Integer> vals = getMapFor(taskId);

        if (vals.containsKey(answer)) {
            int num = vals.get(answer);
            vals.put(answer, num + 1);
        } else {
            vals.put(answer, 1);
        }

    }

    private static LikertValue toLikert(String answer) {
        if ("Strongly agree".equals(answer))
            return LikertValue.SA;
        if ("Agree".equals(answer))
            return LikertValue.A;
        if ("Neither agree nor disagree".equals(answer))
            return LikertValue.N;
        if ("Disagree".equals(answer))
            return LikertValue.D;
        if ("Strongly disagree".equals(answer))
            return LikertValue.SD;

        throw new RuntimeException();
    }

    public static Map<LikertValue, Integer> getMapFor(String taskId) {
        if (answers.containsKey(taskId)) {
            return answers.get(taskId);
        }
        Map<LikertValue, Integer> map = Maps.newLinkedHashMap();
        answers.put(taskId, map);
        return map;
    }

    private enum LikertValue {
        SA, A, N, D, SD
    }
}