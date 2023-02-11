package microservices.examples.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class LoggerConfiguration implements WebMvcConfigurer {

    @Autowired
    private TraceLogger logger;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logger);
    }
}