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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import questionnaire.data.Event;
import questionnaire.data.Event.EventType;
import questionnaire.data.PlayerStatus;
import questionnaire.data.ServerStatus;
import questionnaire.data.Study;
import questionnaire.data.Task;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class IOHelperTest {

    private static final long ONE_HOUR_OFFSET = 1000 * 60 * 60;;

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private IOHelper sut;

    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        mapper = ObjectMapperSetup.getObjectMapper();
        IOHelperFixture.setup(tmp.getRoot().getAbsolutePath());
        sut = new IOHelper(tmp.getRoot(), mapper);
    }

    @Test(expected = RuntimeException.class)
    public void errorIfRootIsMissing() {
        tmp.delete();
        sut = new IOHelper(tmp.getRoot(), mapper);
    }

    @Test(expected = RuntimeException.class)
    public void errorIfDataIsMissing() {
        String rootName = tmp.getRoot().getAbsolutePath();
        delete(rootName + "/code/");
        delete(rootName + "/study/");
        delete(rootName + "/tasks/");
        delete(rootName + "/events/");
        delete(rootName + "/player/");
        sut = new IOHelper(tmp.getRoot(), mapper);
    }

    private void delete(String folderName) {
        File folder = new File(folderName);
        for (File child : folder.listFiles()) {
            child.delete();
        }
        folder.delete();
    }

    @Test
    public void folderStructureIsCreated() {
        Set<String> actualData = Sets.newHashSet(tmp.getRoot().list());
        Set<String> expectedData = Sets.newHashSet("code", "events", "player", "study", "tasks");
        assertEquals(expectedData, actualData);
    }

    @Test
    public void readPlayers_empty() {
        Map<String, PlayerStatus> actuals = sut.readPlayers();
        Map<String, PlayerStatus> expecteds = Maps.newHashMap();
        assertEquals(expecteds, actuals);
    }

    @Test
    public void readPlayers_afterCreate() {
        PlayerStatus p = new PlayerStatus("a", new LinkedList<String>());
        sut.write(p);

        Map<String, PlayerStatus> actual = sut.readPlayers();
        Map<String, PlayerStatus> expected = Maps.newHashMap();
        expected.put("a", p);

        assertEquals(expected, actual);
    }

    @Test
    public void createPlayer() {
        String name = "a";
        PlayerStatus p = createPlayer(name);
        assertFalse(sut.exists(name));
        sut.write(p);
        assertTrue(sut.exists(name));

        name = "b";
        p = createPlayer(name);
        assertFalse(sut.exists(name));
        sut.write(p);
        assertTrue(sut.exists(name));
    }

    public PlayerStatus createPlayer(String name) {
        List<String> tasks = Lists.newArrayList("a1", "b2");
        return new PlayerStatus(name, tasks);
    }

    @Test
    public void readPlayer() {
        PlayerStatus expected = createPlayer("a");
        sut.write(expected);
        PlayerStatus actual = sut.readPlayer(expected.getName());
        assertEquals(expected, actual);
    }

    @Test
    public void orderForStudyStaysTheSame() {
        Study expected = sut.readStudy("REScala");
        for (int i = 0; i < 1000; i++) {
            Study actual = sut.readStudy("REScala");
            assertEquals(expected, actual);
        }
    }

    @Test
    public void readStudy() {
        Study expected = IOHelperFixture.createStudy();
        Study actual = sut.readStudy(expected.getName());
        assertEquals(expected, actual);
    }

    @Test
    public void readTask() {
        List<String> tasks = IOHelperFixture.createStudy().getIntroTasks();
        assertFalse(tasks.isEmpty());
        for (String taskId : tasks) {
            Task actual = sut.readTask(taskId);
            Task expected = IOHelperFixture.createTask(taskId);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void readTask_codeNonEmpty() throws IOException {
        Task expected = IOHelperFixture.createTask("test");
        assertNotNull(expected.code);

        File f = newFile("/tasks/test.json");
        mapper.writeValue(f, expected);

        Task actual = sut.readTask("test");
        assertEquals(expected, actual);
    }

    @Test
    public void readTask_CodeFileDoesNotExist() {
        Task expected = IOHelperFixture.createTask("i1");
        expected.code = null;

        newFile("/code/i1.scala").delete();
        Task actual = sut.readTask("i1");

        assertEquals(expected, actual);
    }

    @Test
    public void readTaskWithoutSolution() {
        Task actual = sut.readTaskWithoutSolution("i1");

        Task expected = IOHelperFixture.createTask("i1");
        expected.answer = null;

        assertEquals(expected, actual);
    }

    @Test
    public void getLogs_noNullResult() {
        List<Event> actual = sut.readLogs();
        List<Event> expected = Lists.newArrayList();
        assertEquals(expected, actual);
    }

    @Test
    public void log() {
        Event expected = new Event();
        sut.log(expected);
        List<Event> actuals = sut.readLogs();

        assertEquals(1, actuals.size());
        assertEquals(expected, actuals.get(0));
    }

    @Test
    public void log_timeIsSetIfNull() {
        sut.log(new Event());
        List<Event> actuals = sut.readLogs();
        assertEquals(1, actuals.size());
        assertNotNull(actuals.get(0).time);
    }

    @Test
    public void log_timeIsNotOverridden() {
        Event a = createEventWithTimeOffset(ONE_HOUR_OFFSET);
        Event b = createEventWithTimeOffset(ONE_HOUR_OFFSET);
        sut.log(a);

        List<Event> actuals = sut.readLogs();
        List<Event> expecteds = Lists.newArrayList(b);
        assertEquals(expecteds, actuals);
    }

    private static Event createEventWithTimeOffset(long offset) {
        Event event = new Event();
        event.answer = "a";
        event.auth = "au";
        event.taskId = "tid";
        event.type = EventType.PAGE_START;
        event.time = new Date(new Date().getTime() - offset);
        return event;
    }

    @Test
    public void log_filesAreCreated() {
        File expected = new File(tmp.getRoot().getAbsoluteFile() + "/events/event-000000.json");
        assertFalse(expected.exists());
        sut.log(new Event());
        assertTrue(expected.exists());
    }

    @Test
    public void serverState_isNotWrittenByDefault() {
        File f = newFile("/player/serverStatus.json");
        assertFalse(f.exists());
        ServerStatus actual = sut.readServerState();
        assertFalse(f.exists());

        ServerStatus expected = new ServerStatus();
        assertEquals(expected, actual);
    }

    @Test
    public void serverStateCanBeWrittenAndRead() {
        ServerStatus expected = new ServerStatus();
        expected.addOopParticipant();
        expected.addOopParticipant();
        expected.addRpParticipant();

        sut.write(expected);
        assertTrue(newFile("/player/serverStatus.json").exists());
        ServerStatus actual = sut.readServerState();
        assertEquals(expected, actual);
    }

    @Test(expected = RuntimeException.class)
    public void ioError_rootPointsToFile() throws IOException {
        File f = new File(tmp.getRoot().getAbsolutePath() + "/xy");
        f.createNewFile();
        new IOHelper(f, mapper);
    }

    @Test(expected = RuntimeException.class)
    public void ioError_taskDoesNotExist() {
        sut.readTask("nonExisting");
    }

    @Test(expected = RuntimeException.class)
    public void ioError_ioProblemWithCode() {
        File f = new File(tmp.getRoot().getAbsolutePath() + "/code/i1.scala");
        f.delete();
        f.mkdir();
        sut.readTask("i1");
    }

    @Test(expected = RuntimeException.class)
    public void ioError_directoryMissingForWrite() {
        File f = new File(tmp.getRoot().getAbsolutePath() + "/player/");
        f.delete();
        sut.write(new PlayerStatus("", null));
    }

    private File newFile(String path) {
        return new File(tmp.getRoot().getAbsoluteFile() + path);
    }
}