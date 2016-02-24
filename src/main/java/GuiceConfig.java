import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;

import questionnaire.IOHelper;
import questionnaire.ObjectMapperSetup;
import questionnaire.services.QuestionService;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class GuiceConfig extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                bindServices();
                setupServer();
            }

            private void bindServices() {
                // bind(IndexService.class);
                bind(QuestionService.class);
            }

            private void setupServer() {
                ObjectMapper mapper = ObjectMapperSetup.getObjectMapper();
                bind(JacksonJsonProvider.class).toInstance(new JacksonJsonProvider(mapper));

                bind(IOHelper.class).toInstance(new IOHelper(new File("data"), mapper));

                Map<String, String> params = new HashMap<String, String>();
                params.put(ServletContainer.JSP_TEMPLATES_BASE_PATH, "WEB-INF/jsp");
                filterRegex("/((?!css|js|images).)*").through(GuiceContainer.class, params);
            }
        });
    }
}