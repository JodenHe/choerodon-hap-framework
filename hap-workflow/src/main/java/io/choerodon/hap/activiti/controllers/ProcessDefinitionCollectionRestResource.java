package io.choerodon.hap.activiti.controllers;

import io.choerodon.hap.activiti.components.ActivitiMultiLanguageManager;
import io.choerodon.base.annotation.Permission;
import io.choerodon.base.enums.ResourceType;
import io.choerodon.web.core.IRequest;
import io.choerodon.web.core.impl.RequestHelper;
import io.swagger.annotations.ApiParam;
import org.activiti.rest.common.api.DataResponse;
import org.activiti.rest.service.api.repository.ProcessDefinitionCollectionResource;
import org.activiti.rest.service.api.repository.ProcessDefinitionResponse;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author qiang.zeng
 * @since 2018/9/13.
 */
@RestController
@RequestMapping(value = {"/wfl", "/api/wfl"})
public class ProcessDefinitionCollectionRestResource extends ProcessDefinitionCollectionResource {

    @Override
    @Permission(type = ResourceType.SITE)
    @SuppressWarnings("unchecked")
    @GetMapping(value = "/repository/process-definitions", produces = "application/json")
    public DataResponse getProcessDefinitions(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams, HttpServletRequest request) {
        IRequest iRequest = RequestHelper.createServiceRequest(request);
        if (ActivitiMultiLanguageManager.multiLanguageOpen) {
            if (allRequestParams.containsKey("nameLike")) {
                allRequestParams.put("nameLike", ActivitiMultiLanguageManager.getMultiLanguageInfoByDescription(allRequestParams.get("nameLike"), iRequest));
            }
        }
        DataResponse dataResponse = super.getProcessDefinitions(allRequestParams, request);
        if (ActivitiMultiLanguageManager.multiLanguageOpen && CollectionUtils.isNotEmpty((List) dataResponse.getData())) {
            List<ProcessDefinitionResponse> processDefinitionResponses = (List<ProcessDefinitionResponse>) dataResponse.getData();
            for (ProcessDefinitionResponse processDefinitionResponse : processDefinitionResponses) {
                processDefinitionResponse.setName(ActivitiMultiLanguageManager.getMultLanguageInfoByCode(processDefinitionResponse.getName(), iRequest));
            }
        }
        return dataResponse;
    }

}
