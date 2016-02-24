package questionnaire.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import questionnaire.ObjectMapperSetup;
import questionnaire.data.Task.TaskType;

import com.google.common.collect.Lists;

public class PlayerStatusTest {

    private static final String DEFAULT_NAME = "a";

    private List<String> tasks;
    private PlayerStatus uut;

    @Before
    public void setup() {
        tasks = Lists.newLinkedList();
        uut = new PlayerStatus(DEFAULT_NAME, tasks);
    }

    @Test
    public void nameCanBeRead() {
        String actual = uut.getName();
        String expected = DEFAULT_NAME;
        assertEquals(expected, actual);
    }

    @Test
    public void startFlagIsCorrectlySet() {
        assertFalse(uut.hasStarted());
        uut.toggleNext();
        assertTrue(uut.hasStarted());
    }

    @Test
    public void byDefaultNoNextTask() {
        assertFalse(uut.hasNextTask());
    }

    @Test
    public void nextTasks() {
        tasks.add("t1");
        assertTrue(uut.hasNextTask());
        uut.toggleNext();
        assertFalse(uut.hasNextTask());
    }

    @Test
    public void getCurrent() {
        tasks.add("t1");
        tasks.add("t2");

        uut.toggleNext();
        String actual1 = uut.getCurrentTaskId();
        assertEquals("t1", actual1);

        uut.toggleNext();
        String actual2 = uut.getCurrentTaskId();
        assertEquals("t2", actual2);
    }

    @Test
    public void doubleToggle() {
        uut.toggleNext();
    }

    @Test
    public void doubleToggle2() {
        tasks.add("t1");
        tasks.add("t2");
        uut.toggleNext();
        uut.toggleNext();
        uut.toggleNext();
    }

    @Test
    public void taskTypeSerialization() throws Exception {
        // set to non default values
        TaskType tt = new Task.TaskType();
        tt.isBlind = !tt.isBlind;
        tt.isMeasured = !tt.isMeasured;
        tt.codeTime++;
        tt.questionTime++;

        ObjectMapper mapper = ObjectMapperSetup.getObjectMapper();
        String json = mapper.writeValueAsString(tt);

        TaskType actual = mapper.readValue(json, Task.TaskType.class);
        assertEquals(tt, actual);
    }

    @Test
    public void equalsAndHashCode() {
        PlayerStatus a = new PlayerStatus();
        PlayerStatus b = new PlayerStatus();
        assertEquals(a, b);
        assertTrue(a.hashCode() == b.hashCode());
    }
}