package io.choerodon.hap.activiti.listeners;

import org.activiti.bpmn.model.Task;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.delegate.TransactionDependentTaskListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component(value = "taskEndNotificationListener")
public class TaskEndNotificationListener implements TaskListener, TransactionDependentTaskListener {

    private Logger log = LoggerFactory.getLogger(getClass());


    @Autowired
    private TaskCreateNotificationListener taskCreateNotificationListener;


    @Override
    public void notify(String processInstanceId, String executionId, Task task, Map<String, Object> executionVariables, Map<String, Object> customPropertiesMap) {
        ExecutionEntity execution = Context.getCommandContext().getExecutionEntityManager().create();
        try {
            execution.setVariablesLocal(executionVariables);
            taskCreateNotificationListener.sendMessage(execution, task, processInstanceId, executionId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            execution.removeVariablesLocal(executionVariables.keySet());
        }
    }


    @Override
    public void notify(DelegateTask delegateTask) {

    }
}