package io.choerodon.hap;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author jiameng.cao
 * @since 2019/2/26
 */
@Configuration
@ComponentScan("org.activiti.rest.service.api")
public class WorkflowMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/editor-app/**", "/diagram-viewer/**")
                .addResourceLocations("classpath:/resources/editor-app/", "classpath:/resources/diagram-viewer/");
    }
}
