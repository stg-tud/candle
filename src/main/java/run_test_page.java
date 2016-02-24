import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.webapp.WebAppContext;

import questionnaire.services.TestService;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastian Proksch - initial API and implementation
 */

public class run_test_page {

    private static Server server;

    public static void main(String[] args) throws Exception {

        server = new Server(8080);

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
        webAppContext.setResourceBase("src/main/webapp");
        webAppContext.setParentLoaderPriority(true);

        webAppContext.addEventListener(new GuiceServletContextListener() {
            @Override
            protected Injector getInjector() {
                return Guice.createInjector(new JerseyServletModule() {
                    @Override
                    protected void configureServlets() {
                        bind(TestService.class);
                        bind(DefaultServlet.class).asEagerSingleton();
                        bind(GuiceContainer.class).asEagerSingleton();

                        serve("/*").with(DefaultServlet.class);

                        Map<String, String> params = new HashMap<String, String>();
                        params.put(ServletContainer.JSP_TEMPLATES_BASE_PATH, "WEB-INF/jsp");
                        filterRegex("/((?!css|js).)*").through(GuiceContainer.class, params);
                    }
                });
            }
        });
        webAppContext.addFilter(GuiceFilter.class, "/*", null);

        server.setHandler(webAppContext);
        server.start();
        server.join();
    }
}
