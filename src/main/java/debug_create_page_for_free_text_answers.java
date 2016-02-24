import java.io.File;
import java.util.List;
import java.util.Map;

import questionnaire.IOHelper;
import questionnaire.ObjectMapperSetup;
import questionnaire.data.Event;
import questionnaire.data.Event.EventType;
import questionnaire.data.Task;
import questionnaire.data.Task.AnswerType;

import com.google.common.collect.Lists;
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

public class debug_create_page_for_free_text_answers {

    private static IOHelper ioHelper = new IOHelper(new File("data"), ObjectMapperSetup.getObjectMapper());

    private static Map<String, List<String>> answers = Maps.newLinkedHashMap();

    public static void main(String[] args) {

        for (Event e : ioHelper.readLogs()) {
            if (e.type != EventType.SUBMISSION) {
                continue;
            }
            Task t = ioHelper.readTask(e.taskId);

            if (t.answerType == AnswerType.FREE_TEXT) {
                getListFor(e.taskId).add(e.answer);
            }
        }

        System.out
                .println("<html><head><style>li { margin: 5px 0; padding: 5px 0; border: 1px dotted black; border-width: 1px 0 0 0;} </style></head><body>");

        for (String taskId : answers.keySet()) {
            Task t = ioHelper.readTask(taskId);

            System.out.printf("<h1>%s</h1>\n", t.question);

            System.out.println("<ul>");
            for (String answer : answers.get(taskId)) {
                answer = answer.trim();
                boolean hasNoAnswer = answer.isEmpty() || "x".equals(answer) || "X".equals(answer)
                        || "-".equals(answer);
                if (!hasNoAnswer) {
                    System.out.printf("<li>%s</li>\n", answer.replace("\n", "<br/>"));
                }
            }
            System.out.println("</ul>");
        }

        System.out.println("</html></body>");

    }

    public static List<String> getListFor(String taskId) {
        if (answers.containsKey(taskId)) {
            return answers.get(taskId);
        }
        List<String> l = Lists.newLinkedList();
        answers.put(taskId, l);
        return l;
    }
}