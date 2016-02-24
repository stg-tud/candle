import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import questionnaire.IOHelper;
import questionnaire.ObjectMapperSetup;
import questionnaire.data.Event;
import questionnaire.data.Event.EventType;
import questionnaire.data.Task;

public class run_student_results {

	private static final String INTERESTED_IN_USER = "x2879190";
	private static final String INTERESTED_IN_TASK = "xbench_FP_7";

	private static final String DATA_PATH = "/Users/seb/versioned_code/empirical-study-platform/data-merged";

	private static final ObjectMapper mapper = ObjectMapperSetup.getObjectMapper();
	private static final IOHelper ioHelper = new IOHelper(new File(DATA_PATH), mapper);

	private static Set<String> taskNames;
	private static Map<String, Task> tasks;
	private static Map<String, String> groupAssignment;
	private static Map<String, Map<String, ResultType>> results;
	private static Map<String, Map<String, String>> freeTextAnswers;
	private static Pattern renamePattern = Pattern.compile("test_(OO|RP)_([0-9]+)");;

	public static void main(String[] args) {

		// DO NOT REALY ON THIS!
		// thsi is untested code that is only used to double check another
		// result

		taskNames = Sets.newTreeSet();
		tasks = Maps.newLinkedHashMap();
		groupAssignment = Maps.newLinkedHashMap();
		results = Maps.newLinkedHashMap();
		freeTextAnswers = Maps.newLinkedHashMap();

		for (Event e : ioHelper.readLogs()) {
			if ("12345678".equals(e.auth) || "2653264".equals(e.auth)) {
				continue;
			}

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

			storeTask(e);
			if (e.type == EventType.SUBMISSION) {

				if (e.auth.equals(INTERESTED_IN_USER)) {
					System.out.printf("%s (%s):\n\tExpect: \"%s\"\n\tActual: \"%s\"\n\t--> %b\n", e.taskId, e.auth, expected(e), e.answer,
							isCorrect(e));
				}

				if (e.taskId.equals(INTERESTED_IN_TASK)) {
					System.out.printf("%s (%s):\n\tExpect: \"%s\"\n\tActual: \"%s\"\n\t--> %b\n", e.auth, e.taskId, expected(e), e.answer,
							isCorrect(e));
				}

				if (isPersonal(e)) {
					store(e, ResultType.FREETEXT);
					storeFreeText(e);
				} else {
					store(e, isCorrect(e) ? ResultType.CORRECT : ResultType.FALSE);
				}

			} else if (e.type == EventType.TIMEOUT) {
				store(e, ResultType.TIMEOUT);
			}
		}

		System.out.println();
		
		System.out.println("## Overview");
		printCsv();

	}

	private static boolean isPersonal(Event e) {
		return e.taskId.startsWith("bench_PERSONAL");
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
		for (String taskId : taskNames) {
			System.out.printf("\t%s", taskId);
		}
		System.out.println();

		for (String auth : getSortedUsers()) {
			System.out.printf("%s\t%s", auth, groupAssignment.get(auth));

			for (String taskId : taskNames) {
				ResultType res = getResult(auth, taskId);
				System.out.printf("\t");
				switch (res) {
				case CORRECT:
					System.out.printf("1");
					break;
				case FALSE:
					System.out.printf("0");
					break;
				case TIMEOUT:
					System.out.printf("Timeout");
					break;
				case NO_ANSWER:
					System.out.printf("n/a");
					break;
				case FREETEXT:
					String freeTextAnswer = freeTextAnswers.get(auth).get(taskId);
					try {
						int num = Integer.parseInt(freeTextAnswer);
						System.out.print(num);
						break;
					} catch (Exception e) {
					}
					System.out.printf("\"%s\"", freeTextAnswer.replace("\"", "\\\""));
					break;
				default:
					break;
				}
			}
			System.out.println();
		}
	}

	private static ResultType getResult(String auth, String taskId) {
		Map<String, ResultType> byUser = results.get(auth);
		ResultType res = byUser.get(taskId);
		if (res == null) {
			return ResultType.NO_ANSWER;
		}
		return res;
	}

	private static Set<String> getSortedUsers() {
		Set<String> auths = Sets.newTreeSet();
		auths.addAll(results.keySet());
		return auths;
	}

	private static void storeTask(Event e) {
		String taskId = getTask(e);
		if (!taskNames.contains(taskId)) {
			taskNames.add(taskId);
			Task task = ioHelper.readTask(e.taskId);
			tasks.put(taskId, task);
		}
	}

	private static boolean isCorrect(Event e) {
		return expected(e).equals(e.answer);
	}

	private static Object expected(Event e) {
		String taskId = getTask(e);
		Task task = tasks.get(taskId);
		String expected = StringEscapeUtils.unescapeHtml4(task.answer);
		return expected;
	}

	private static void store(Event e, ResultType resType) {
		Map<String, ResultType> byUser = results.get(e.auth);
		if (byUser == null) {
			byUser = Maps.newLinkedHashMap();
			results.put(e.auth, byUser);
		}

		String taskId = getTask(e);
		ResultType res = byUser.get(taskId);
		if (res != null) {
			throw new RuntimeException("should be null!! " + e);
		}
		byUser.put(taskId, resType);
	}

	private static void storeFreeText(Event e) {
		Map<String, String> byUser = freeTextAnswers.get(e.auth);
		if (byUser == null) {
			byUser = Maps.newLinkedHashMap();
			freeTextAnswers.put(e.auth, byUser);
		}

		String taskId = getTask(e);
		String res = byUser.get(taskId);
		if (res != null) {
			throw new RuntimeException("should be null!! " + e);
		}
		byUser.put(taskId, e.answer);
	}

	public enum ResultType {
		CORRECT, FALSE, TIMEOUT, NO_ANSWER, FREETEXT
	}
}