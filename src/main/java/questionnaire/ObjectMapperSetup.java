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

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

public class ObjectMapperSetup {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setVisibility(JsonMethod.ALL, Visibility.NONE);
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);

        mapper.setSerializationInclusion(Inclusion.NON_NULL);

        mapper.enable(Feature.INDENT_OUTPUT);
    }

    public static ObjectMapper getObjectMapper() {
        return mapper;
    }
}