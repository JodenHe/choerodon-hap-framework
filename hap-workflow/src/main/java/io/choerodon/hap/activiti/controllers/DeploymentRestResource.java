package io.choerodon.hap.activiti.controllers;

import io.choerodon.hap.activiti.components.ActivitiMultiLanguageManager;
import io.choerodon.base.annotation.Permission;
import io.choerodon.base.enums.ResourceType;
import io.choerodon.web.core.impl.RequestHelper;
import io.swagger.annotations.ApiParam;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.rest.service.api.RestResponseFactory;
import org.activiti.rest.service.api.repository.DeploymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DeploymentRestResource {

    @Autowired
    protected RestResponseFactory restResponseFactory;

    @Autowired
    protected RepositoryService repositoryService;

    @Permission(type = ResourceType.SITE)
    @GetMapping(value = "/repository/deployments/{deploymentId}", produces = "application/json")
    public DeploymentResponse getDeployment(@ApiParam(name = "deploymentId", value = "The id of the deployment to get.") @PathVariable String deploymentId, HttpServletRequest request) {
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();

        if (deployment == null) {
            throw new ActivitiObjectNotFoundException("Could not find a deployment with id '" + deploymentId + "'.", Deployment.class);
        }
        DeploymentResponse deploymentResponse = restResponseFactory.createDeploymentResponse(deployment);
        //add by qiang.zeng start
        deploymentResponse.setName(ActivitiMultiLanguageManager.getMultLanguageInfoByCode(deploymentResponse.getName(), RequestHelper.createServiceRequest(request)));
        return deploymentResponse;
    }
}
