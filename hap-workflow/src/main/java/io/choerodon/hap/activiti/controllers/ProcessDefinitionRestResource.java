package io.choerodon.hap.activiti.controllers;

import io.choerodon.hap.activiti.components.ActivitiMultiLanguageManager;
import io.choerodon.base.annotation.Permission;
import io.choerodon.base.enums.ResourceType;
import io.choerodon.web.core.IRequest;
import io.choerodon.web.core.impl.RequestHelper;
import io.swagger.annotations.ApiParam;
import org.activiti.rest.service.api.repository.ProcessDefinitionResource;
import org.activiti.rest.service.api.repository.ProcessDefinitionResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qiang.zeng
 * @since 2018/9/13.
 */
@RestController
@RequestMapping(value = {"/wfl", "/api/wfl"})
public class ProcessDefinitionRestResource extends ProcessDefinitionResource {

    @Permission(type = ResourceType.SITE)
    @GetMapping(value = "/repository/process-definitions/{processDefinitionId}", produces = "application/json")
    public ProcessDefinitionResponse getProcessDefinition(@ApiParam(name = "processDefinitionId", value = "The id of the process definition to get.") @PathVariable String processDefinitionId, HttpServletRequest request) {
        ProcessDefinitionResponse processDefinitionResponse = super.getProcessDefinition(processDefinitionId, request);
        IRequest iRequest = RequestHelper.createServiceRequest(request);
        processDefinitionResponse.setName(ActivitiMultiLanguageManager.getMultLanguageInfoByCode(processDefinitionResponse.getName(), iRequest));
        return processDefinitionResponse;
    }
}
