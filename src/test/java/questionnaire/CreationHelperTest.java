package questionnaire;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import questionnaire.data.PlayerStatus;
import questionnaire.data.ServerStatus;
import questionnaire.data.Study;

public class CreationHelperTest {

    private CreationHelper uut;
    private IOHelper ioHelper;
    private ServerStatus serverStatus;
    private Study study;

    @Before
    public void setup() {

        study = new Study();
        study.setName("aStudy");
        study.addIntroTask("i1");
        study.addOopTask("o1");
        study.addRpTask("r1");
        study.addExitTask("x1");

        ioHelper = mock(IOHelper.class);
        serverStatus = mock(ServerStatus.class);
        when(ioHelper.readStudy(anyString())).thenReturn(study);
        when(ioHelper.exists(eq("1"))).thenReturn(false).thenReturn(true);
        when(ioHelper.readServerState()).thenReturn(serverStatus);

        when(serverStatus.getNumOopParticipants()).thenReturn(1);
        when(serverStatus.getNumRpParticipants()).thenReturn(4);

        uut = new CreationHelper(ioHelper);
    }

    @Test
    public void createPlayer() {
        uut.createPlayer("1");
        verify(ioHelper).write(any(PlayerStatus.class));
        verify(serverStatus).addOopParticipant();
    }

    @Test
    public void rpPlayerIsCreated() {
        when(serverStatus.getNumOopParticipants()).thenReturn(1);
        when(serverStatus.getNumRpParticipants()).thenReturn(0);

        PlayerStatus actual = uut.createPlayer("1");
        PlayerStatus expected = new PlayerStatus("1", Lists.newArrayList("i1", "r1", "x1"));
        assertEquals(expected, actual);

        verify(ioHelper).readServerState();
        verify(serverStatus).addRpParticipant();
        verify(ioHelper).write(eq(actual));
        verify(ioHelper).write(serverStatus);
    }

    @Test
    public void oopPlayerIsCreated() {
        when(serverStatus.getNumOopParticipants()).thenReturn(0);
        when(serverStatus.getNumRpParticipants()).thenReturn(1);

        PlayerStatus actual = uut.createPlayer("1");
        PlayerStatus expected = new PlayerStatus("1", Lists.newArrayList("i1", "o1", "x1"));
        assertEquals(expected, actual);

        verify(ioHelper).readServerState();
        verify(serverStatus).addOopParticipant();
        verify(ioHelper).write(eq(actual));
        verify(ioHelper).write(serverStatus);
    }

    @Test(expected = RuntimeException.class)
    public void createPlayer_invalid1() {
        uut.createPlayer("");
    }

    @Test(expected = RuntimeException.class)
    public void createPlayer_invalid2() {
        uut.createPlayer("a");
    }

    @Test(expected = RuntimeException.class)
    public void createPlayer_exists() {
        uut.createPlayer("1");
        uut.createPlayer("1");
    }
}