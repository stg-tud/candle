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
package questionnaire.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.sun.jersey.api.view.Viewable;

@Path("/")
public class TestService {

    @GET
    public Viewable index() {
        return new Viewable("/test.jsp");
    }
}