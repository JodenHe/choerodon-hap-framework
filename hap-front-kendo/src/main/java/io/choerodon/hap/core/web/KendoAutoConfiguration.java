package io.choerodon.hap.core.web;

import io.choerodon.hap.core.web.view.ViewTagFactory;
import io.choerodon.freemarker.ChoerodonFreemarkerAutoConfiguration;
import io.choerodon.freemarker.DefaultFreeMarkerView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
public class KendoAutoConfiguration {

    @Bean(value = "screenTagFactory")
    public ViewTagFactory viewTagFactory() {
        ViewTagFactory viewTagFactory = new ViewTagFactory();
        viewTagFactory.setBasePackage("io.choerodon.hap.core.web.view.ui");
        return viewTagFactory;
    }

    @Bean("screenView")
    public FreeMarkerViewResolver screenView() {
        FreeMarkerViewResolver screenView = new FreeMarkerViewResolver();
        screenView.setViewClass(DefaultFreeMarkerView.class);
        screenView.setSuffix(".view");
        screenView.setOrder(2);
        ChoerodonFreemarkerAutoConfiguration.setCommonFreeMarkerViewResolverConfig(screenView);
        return screenView;
    }
}
