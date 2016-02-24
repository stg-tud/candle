package questionnaire;

import static questionnaire.ObjectMapperSetup.getObjectMapper;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.collect.Lists;

import questionnaire.data.Study;
import questionnaire.data.Task;

public class IOHelperFixture {

    private static ObjectMapper mapper = getObjectMapper();

    public static void setup(String rootName) throws JsonGenerationException, JsonMappingException, IOException {

        new File(rootName + "/code/").mkdirs();
        new File(rootName + "/study/").mkdirs();
        new File(rootName + "/tasks/").mkdirs();

        mapper.writeValue(new File(rootName + "/study/REScala.json"), createStudy());

        createTaskFiles(rootName, "i1");
        createTaskFiles(rootName, "i2");
        createTaskFiles(rootName, "i3");
        createTaskFiles(rootName, "i4");
        createTaskFiles(rootName, "i5");
        createTaskFiles(rootName, "o1");
        createTaskFiles(rootName, "r1");
    }

    private static void createTaskFiles(String rootName, String id) throws IOException {
        Task task = createTask(id);

        FileUtils.writeStringToFile(new File(rootName + "/code/" + id + ".scala"), task.code);

        task.code = null;
        mapper.writeValue(new File(rootName + "/tasks/" + id + ".json"), task);
    }

    public static Study createStudy() {
        Study study = new Study();
        study.setName("REScala");
        study.addIntroTask("i1");
        study.addIntroTask("i2");
        study.addIntroTask("i3");
        study.addIntroTask("i4");
        study.addIntroTask("i5");
        study.addOopTask("o1");
        study.addIntroTask("r1");
        return study;
    }

    public static Task createTask(String id) {
        Task task = new Task();
        task.id = id;
        task.title = "title:" + id;
        task.description = "desc:" + id;
        task.code = "code:" + id;
        task.question = "question:" + id;
        task.options = Lists.newLinkedList();
        task.options.add(id + "-o1");
        task.options.add(id + "-o2");
        task.options.add(id + "-o3");
        task.answer = id + "-o2";
        return task;
    }
}
