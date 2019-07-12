package io.choerodon.hap;

import io.choerodon.hap.activiti.custom.CustomSpringProcessEngineConfiguration;
import io.choerodon.hap.activiti.custom.CustomUserTaskParseHandler;
import io.choerodon.hap.activiti.custom.ForecastActivityCmd;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.bpmn.parser.factory.ActivityBehaviorFactory;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.db.DbSqlSessionFactory;
import org.activiti.engine.impl.persistence.entity.data.GroupDataManager;
import org.activiti.engine.impl.persistence.entity.data.UserDataManager;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.rest.common.application.DefaultContentTypeResolver;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;

@Configuration
@Import(WorkflowMvcConfigurer.class)
public class WorkflowAutoConfiguration {
    @Value("${db.type}")
    private String databaseType;
    @Value("${activiti.mailServerHost}")
    private String mailServerHost;
    @Value("${activiti.mailServerPort}")
    private Integer mailServerPort;
    @Value("${activiti.mailServerUsername}")
    private String mailServerDefaultFrom;
    @Value("${activiti.mailServerUsername}")
    private String mailServerUsername;
    @Value("${activiti.mailServerPassword}")
    private String mailServerPassword;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private ActivityBehaviorFactory customBehaviorFactory;

    @Autowired
    @Lazy
    private GroupDataManager customGroupDataManager;

    @Autowired
    @Lazy
    private UserDataManager customUserDataManager;

    @Autowired
    private Map<Object, Object> activitiBeanProvider;

    @Autowired
    private ProcessDiagramGenerator processDiagramGenerator;

    @Autowired
    private DbSqlSessionFactory customDbSqlSessionFactory;

    @Autowired
    @Lazy
    private CustomUserTaskParseHandler customUserTaskParseHandler;

    @Bean(name = "processEngineConfiguration")
    public CustomSpringProcessEngineConfiguration processEngineConfiguration() {
        CustomSpringProcessEngineConfiguration configuration = new CustomSpringProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setDatabaseType(databaseType);
        configuration.setTransactionManager(transactionManager);
        configuration.setDatabaseSchemaUpdate("true");
        configuration.setActivityFontName("simsun");
        configuration.setLabelFontName("simsun");
        configuration.setAnnotationFontName("simsun");
        configuration.setMailServerHost(mailServerHost);
        configuration.setMailServerPort(mailServerPort);
        configuration.setMailServerDefaultFrom(mailServerDefaultFrom);
        configuration.setMailServerUsername(mailServerUsername);
        configuration.setMailServerPassword(mailServerPassword);
        configuration.setAsyncExecutorActivate(true);
        configuration.setDbIdentityUsed(false);
        configuration.setActivityBehaviorFactory(customBehaviorFactory);
        configuration.setGroupDataManager(customGroupDataManager);
        configuration.setUserDataManager(customUserDataManager);
        configuration.setBeans(activitiBeanProvider);
        configuration.setProcessDiagramGenerator(processDiagramGenerator);
        configuration.setCustomDefaultBpmnParseHandlers(Collections.singletonList(customUserTaskParseHandler));
        configuration.setDbSqlSessionFactory(customDbSqlSessionFactory);
        return configuration;
    }

    @Bean(name = "processEngine")
    public ProcessEngineFactoryBean processEngine(ProcessEngineConfigurationImpl processEngineConfiguration) {
        ProcessEngineFactoryBean bean = new ProcessEngineFactoryBean();
        bean.setProcessEngineConfiguration(processEngineConfiguration);
        return bean;
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @Bean
    public IdentityService identityService(ProcessEngine processEngine) {
        return processEngine.getIdentityService();
    }

    @Bean
    public FormService formService(ProcessEngine processEngine) {
        return processEngine.getFormService();

    }

    @Bean
    public DefaultContentTypeResolver defaultContentTypeResolver() {
        return new DefaultContentTypeResolver();
    }

    @Bean
    public ForecastActivityCmd forecastActivityCmd() {
        return new ForecastActivityCmd();
    }

}