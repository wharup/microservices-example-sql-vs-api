package microservices.examples.system;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver() {

            @Override	
            public Pageable resolveArgument(MethodParameter methodParameter,
                    @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                    @Nullable WebDataBinderFactory binderFactory) {
                setMaxPageSize(Integer.MAX_VALUE);
                return super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
            }
        };

        resolvers.add(resolver);
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}