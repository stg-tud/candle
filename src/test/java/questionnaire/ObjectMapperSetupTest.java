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

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import questionnaire.data.Task;

public class ObjectMapperSetupTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = ObjectMapperSetup.getObjectMapper();
    }
    
    @Test
    public void nonEmptyValidation() throws IOException {
        Task expected = new Task();
        expected.taskType.codeTime = 5;
        expected.id = "a";
        expected.options = null;
        
        String actualStr = mapper.writeValueAsString(expected);
        String expectedStr = "{\n"
                + "  \"taskType\" : {\n"
                + "    \"isOpen\" : false,\n"
                + "    \"isBlind\" : false,\n"
                + "    \"isMeasured\" : false,\n"
                + "    \"isGraded\" : false,\n"
                + "    \"codeTime\" : 5,\n"
                + "    \"questionTime\" : 0\n"
                + "  },\n"
                + "  \"answerType\" : \"MULTIPLE_CHOICE\",\n"
                + "  \"id\" : \"a\"\n"
                + "}";
        assertEquals(expectedStr, actualStr);
        
        Task actual = mapper.readValue(actualStr, Task.class);
        assertEquals(expected, actual);
    }
    
    @Test
    public void coverage() {
        new ObjectMapperSetup();
    }
}