package questionnaire;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;

import questionnaire.data.Event;
import questionnaire.data.PlayerStatus;
import questionnaire.data.ServerStatus;
import questionnaire.data.Study;
import questionnaire.data.Task;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class IOHelper {

    private static String CODE_FOLDER = "/code/";
    private static String EVENTS_FOLDER = "/events/";
    private static String PLAYER_FOLDER = "/player/";
    private static String STUDY_FOLDER = "/study/";
    private static String TASKS_FOLDER = "/tasks/";

    private final File root;
    private final ObjectMapper mapper;

    @Inject
    public IOHelper(File root, ObjectMapper mapper) {
        this.root = root;
        this.mapper = mapper;
        assertFolders();
    }

    private void assertFolders() {
        assertFolder(root.toString());
        assertFolder(root + CODE_FOLDER);
        assertFolder(root + STUDY_FOLDER);
        assertFolder(root + TASKS_FOLDER);

        new File(root + "/" + EVENTS_FOLDER).mkdirs();
        new File(root + "/" + PLAYER_FOLDER).mkdirs();
    }

    private void assertFolder(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException(String.format("unexpected: folder '%s' does not exist", path));
        }
        String[] files = file.list();
        if (files == null || files.length == 0) {
            throw new RuntimeException(String.format("unexpected: folder '%s' is empty", path));
        }
    }

    public synchronized Map<String, PlayerStatus> readPlayers() {
        Map<String, PlayerStatus> players = Maps.newHashMap();

        for (File pFile : new File(root + PLAYER_FOLDER).listFiles()) {
            PlayerStatus p = read(pFile, PlayerStatus.class);
            players.put(p.getName(), p);
        }

        return players;
    }

    public synchronized boolean exists(String name) {
        return getPlayerFile(name).exists();
    }

    private File getPlayerFile(String name) {
        File pFile = new File(root + PLAYER_FOLDER + name + ".json");
        return pFile;
    }

    public synchronized PlayerStatus readPlayer(String name) {
        File pFile = getPlayerFile(name);
        return read(pFile, PlayerStatus.class);
    }

    public synchronized void write(PlayerStatus p) {
        write(getPlayerFile(p.getName()), p);
    }

    public synchronized Study readStudy(String studyName) {
        File fileName = new File(root + STUDY_FOLDER + studyName + ".json");
        Study study = read(fileName, Study.class);
        return study;
    }

    public synchronized Task readTaskWithoutSolution(String taskId) {
        Task t = readTask(taskId);
        t.answer = null;
        return t;
    }

    public synchronized Task readTask(String taskId) {
        File taskFile = new File(root + TASKS_FOLDER + taskId + ".json");
        File codeFile = new File(root + CODE_FOLDER + taskId + ".scala");

        Task task = read(taskFile, Task.class);

        task.answer = escape(task.answer);
        task.options = escape(task.options);

        if (task.code == null && codeFile.exists()) {
            String rawCode = read(codeFile);
            task.code = escape(rawCode);
        }

        return task;
    }
    
    public synchronized Task readTaskNoEscape(String taskId) {
        File taskFile = new File(root + TASKS_FOLDER + taskId + ".json");
        File codeFile = new File(root + CODE_FOLDER + taskId + ".scala");

        Task task = read(taskFile, Task.class);

        if (task.code == null && codeFile.exists()) {
            task.code = read(codeFile);
        }

        return task;
    }

    public synchronized void log(Event event) {

        if (event.time == null) {
            event.time = new Date();
        }

        File eventsDir = new File(root + EVENTS_FOLDER);
        int numChilds = eventsDir.list().length;
        File nextFile = new File(eventsDir, String.format("event-%06d.json", numChilds));

        write(nextFile, event);
    }

    public synchronized List<Event> readLogs() {
        List<Event> events = new ArrayList<Event>();
        File[] files = new File(root + EVENTS_FOLDER).listFiles();
        Arrays.sort(files);
        for (File file : files) {
            boolean isMetaFile = ".DS_Store".equals(file.getName());
            if (!isMetaFile) {
                events.add(read(file, Event.class));
            }
        }
        return events;
    }

    private <T> T read(File file, Class<T> clazz) {
        try {
            return mapper.readValue(file, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String read(File file) {
        try {
            return FileUtils.readFileToString(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> boolean write(File file, T data) {
        try {
            mapper.writeValue(file, data);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String escape(String s) {
        return escapeHtml4(s);
    }

    private static List<String> escape(List<String> ss) {
        List<String> escaped = new ArrayList<String>();
        for (String s : ss) {
            escaped.add(escape(s));
        }
        return escaped;
    }

    public synchronized ServerStatus readServerState() {
        File file = new File(root + PLAYER_FOLDER + "serverStatus.json");
        if (file.exists()) {
            return read(file, ServerStatus.class);
        } else {
            return new ServerStatus();
        }
    }

    public synchronized void write(ServerStatus state) {
        write(new File(root + PLAYER_FOLDER + "serverStatus.json"), state);
    }
    
	public synchronized List<PlayerStatus> readPlayersList() {
		List<PlayerStatus> players = new ArrayList<PlayerStatus>();
		for (File pFile : new File(root + PLAYER_FOLDER).listFiles()) {
			PlayerStatus p = read(pFile, PlayerStatus.class);
			players.add(p);
		}
		return players;
	}
	
	
    public synchronized List<Task> readTasks() {
        List<Task> tasks = new ArrayList<Task>();
        File[] files = new File(root + TASKS_FOLDER).listFiles();
        Arrays.sort(files);
        for (File file : files) {
        	tasks.add(read(file, Task.class));
        }
        return tasks;
    }

}







