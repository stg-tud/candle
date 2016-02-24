import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import questionnaire.IOHelper;
import questionnaire.ObjectMapperSetup;
import questionnaire.data.Event;
import questionnaire.data.Event.EventType;

public class run_student_durations {

	private static final String DATA_PATH = "/Users/seb/versioned_code/empirical-study-platform/data-merged";
	private static final Date TIMEOUT_DATE = new Date();

	private static final ObjectMapper mapper = ObjectMapperSetup.getObjectMapper();
	private static final IOHelper ioHelper = new IOHelper(new File(DATA_PATH), mapper);

	private static Set<String> tasks;
	private static Map<String, String> groupAssignment;

	private static Map<String, Map<String, Date>> starts;
	private static Map<String, Map<String, Date>> ends;
	private static Pattern renamePattern = Pattern.compile("test_(OO|RP)_([0-9]+)");;

	public static void main(String[] args) {

		// DO NOT REALY ON THIS!
		// thsi is untested code that is only used to double check another
		// result

		tasks = Sets.newTreeSet();
		groupAssignment = Maps.newLinkedHashMap();
		starts = Maps.newLinkedHashMap();
		ends = Maps.newLinkedHashMap();

		int i = 0;
		for (Event e : ioHelper.readLogs()) {
			if ("12345678".equals(e.auth) || "2653264".equals(e.auth) || "321342134".equals(e.auth)) {
				continue;
			}
			tasks.add(getTask(e));

			if (e.taskId.startsWith("test_")) {
				boolean isOO = e.taskId.contains("_OO_");
				boolean isRP = e.taskId.contains("_RP_");
				if (isOO) {
					groupAssignment.put(e.auth, "OO");
				} else if (isRP) {
					groupAssignment.put(e.auth, "RP");
				} else {
					throw new RuntimeException("error, user has to be RP or OO!");
				}
			}

			System.out.println(i++);

			if (e.type == EventType.PAGE_START) {
				storeDate(starts, e);
			}
			if (e.type == EventType.SUBMISSION || e.type == EventType.TIMEOUT) {
				storeDate(ends, e);
			}
		}

		printCsv();

	}

	private static String getTask(Event e) {
		Matcher m = renamePattern.matcher(e.taskId);

		if (!m.matches()) {
			return e.taskId;
		}

		return "Ex. " + m.group(2);
	}

	private static void printCsv() {
		System.out.printf("\tGROUP");
		for (String taskId : tasks) {
			System.out.printf("\t%s", taskId);
		}
		System.out.println();

		for (String auth : getSortedUsers()) {
			System.out.printf("%s\t%s", auth, groupAssignment.get(auth));

			for (String taskId : tasks) {
				System.out.printf("\t%s", getDuration(auth, taskId));
			}
			System.out.println();
		}
	}

	private static String getDuration(String auth, String taskId) {
		Map<String, Date> startByUser = starts.get(auth);
		if (startByUser == null) {
			return "n/a";
		}
		Date start = startByUser.get(taskId);
		if (start == null) {
			return "n/a";
		}

		Map<String, Date> endByUser = ends.get(auth);
		if (endByUser == null) {
			return "n/a";
		}
		Date end = endByUser.get(taskId);
		if (end == null) {
			return "n/a";
		}

		if (end == TIMEOUT_DATE) {
			return "Timeout";
		}

		long duration = end.getTime() - start.getTime();
		long durationInS = duration / 1000;
		return Long.toString(durationInS);
	}

	private static Set<String> getSortedUsers() {
		Set<String> auths = Sets.newTreeSet();
		auths.addAll(starts.keySet());
		return auths;
	}

	private static void storeDate(Map<String, Map<String, Date>> starts, Event e) {
		Map<String, Date> byUser = starts.get(e.auth);
		if (byUser == null) {
			byUser = Maps.newLinkedHashMap();
			starts.put(e.auth, byUser);
		}

		String taskId = getTask(e);
		if (e.type == EventType.TIMEOUT) {
			byUser.put(taskId, TIMEOUT_DATE);
		} else {
			byUser.put(taskId, e.time);
		}
	}
}