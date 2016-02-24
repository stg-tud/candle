import java.io.File;
import java.util.List;

import questionnaire.IOHelper;
import questionnaire.ObjectMapperSetup;
import questionnaire.data.Study;
import questionnaire.data.Task;

/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastian Proksch - initial API and implementation
 */

public class debug_read_all_files {
    private static IOHelper ioHelper = new IOHelper(new File("data"), ObjectMapperSetup.getObjectMapper());

    public static void main(String[] args) {
        Study study = ioHelper.readStudy("REScala");
        readAll(study.getIntroTasks());
        readAll(study.getOopTasks());
        readAll(study.getRpTasks());
    }

    private static void readAll(List<String> tasks) {
        for (String taskId : tasks) {
            try {
                Task t = ioHelper.readTask(taskId);

                boolean hasAnswer = t.options.contains(t.answer);
                if (!hasAnswer) {
                    System.out.printf("## answer (" + t.answer + ") not contained in options of task " + t.id + "\n");
                }
            } catch (Exception e) {
                System.out.println("######### error on reading " + taskId + ":");
                e.printStackTrace();
            }
        }
    }
}