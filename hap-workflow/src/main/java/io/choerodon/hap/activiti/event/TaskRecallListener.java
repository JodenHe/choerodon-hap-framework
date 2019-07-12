package io.choerodon.hap.activiti.event;

import io.choerodon.hap.activiti.event.dto.TaskRecallInfo;
import io.choerodon.web.core.IRequest;

/**
 * @author xiangyu.qi@hand-china.com on 2017/11/7.
 */
public interface TaskRecallListener {

    /**
     * 撤回监听器监听的流程定义key（processDefinitionKey）
     *
     * @return 支持返回多个key, 用分号分隔，支持返回*，匹配所有流程
     */
    String processDefinitionKey();

    /**
     * 在撤回流程前，做一些业务操作.
     *
     * @param taskRecallInfo 撤回信息
     */
    void doRecall(IRequest request, TaskRecallInfo taskRecallInfo);
}
