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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import questionnaire.data.PlayerStatus;
import questionnaire.data.ServerStatus;
import questionnaire.data.Study;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class CreationHelper {

    // private static final String STUDY_NAME = "REScala";
    private static final String STUDY_NAME = "REScala2";

    private final IOHelper ioHelper;

    @Inject
    public CreationHelper(IOHelper ioHelper) {
        this.ioHelper = ioHelper;
    }

    public synchronized boolean isValid(String authRaw) {
        String auth = authRaw.trim();
        if (auth.isEmpty()) {
            return false;
        }
        if (!auth.matches("\\d+")) {
            return false;
        }
        return true;
    }

    public synchronized PlayerStatus createPlayer(String auth) {
        if (!isValid(auth)) {
            throw new RuntimeException("authentication is invalid");
        }
        if (ioHelper.exists(auth)) {
            throw new RuntimeException("authentication already exists");
        }

        Study study = ioHelper.readStudy(STUDY_NAME);
        ServerStatus state = ioHelper.readServerState();

        List<String> tasks = createTasks(study, state);
        PlayerStatus playerStatus = new PlayerStatus(auth, tasks);

        ioHelper.write(playerStatus);
        ioHelper.write(state);
        return playerStatus;
    }

    private static List<String> createTasks(Study study, ServerStatus state) {
        if (state.getNumRpParticipants() < state.getNumOopParticipants()) {
            state.addRpParticipant();
            return mix(study.getIntroTasks(), study.getRpTasks(), study.getExitTasks());
        } else {
            state.addOopParticipant();
            return mix(study.getIntroTasks(), study.getOopTasks(), study.getExitTasks());
        }
    }

    private static List<String> mix(List<String> intro, List<String> tasks, List<String> outro) {
        List<String> res = Lists.newLinkedList();
        res.addAll(intro);
        res.addAll(shuffle(tasks));
        res.addAll(outro);
        return res;
    }

    private static <T> List<T> shuffle(List<T> list) {
        List<T> copy = new ArrayList<T>(list);
        Collections.shuffle(copy);
        return copy;
    }

}