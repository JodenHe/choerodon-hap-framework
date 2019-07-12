package io.choerodon.hap.activiti.service.impl;

import com.github.pagehelper.Page;
import io.choerodon.hap.activiti.components.ActivitiMultiLanguageManager;
import io.choerodon.hap.activiti.dto.ProcessDefinitionDto;
import io.choerodon.hap.activiti.service.IActivitiService;
import io.choerodon.base.annotation.Dataset;
import io.choerodon.dataset.exception.DatasetException;
import io.choerodon.dataset.service.IDatasetService;
import io.choerodon.hap.system.dto.DTOStatus;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.ProcessDefinitionQueryProperty;
import org.activiti.engine.query.QueryProperty;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.rest.common.api.DataResponse;
import org.activiti.rest.service.api.RestResponseFactory;
import org.activiti.rest.service.api.repository.ProcessDefinitionResponse;
import org.activiti.rest.service.api.repository.ProcessDefinitionsPaginateList;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vista
 * @since 18-12-26
 */
@Service
@Dataset("ProcessDefinition")
public class ProcessDefinitionServiceImpl implements IDatasetService<ProcessDefinitionDto> {

    private static final Map<String, QueryProperty> properties = new HashMap<String, QueryProperty>();

    static {
        properties.put("id", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_ID);
        properties.put("key", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_KEY);
        properties.put("category", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_CATEGORY);
        properties.put("name", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_NAME);
        properties.put("version", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_VERSION);
        properties.put("deploymentId", ProcessDefinitionQueryProperty.DEPLOYMENT_ID);
        properties.put("tenantId", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_TENANT_ID);
    }

    @Autowired
    private IActivitiService activitiService;

    @Autowired
    protected RestResponseFactory restResponseFactory;

    @Autowired
    protected RepositoryService repositoryService;

    @Override
    @SuppressWarnings("unchecked")
    public List<?> queries(Map<String, Object> body, int page, int pageSize, String sortname, boolean isDesc) {
        Map<String, String> allRequestParams = (Map) body;
        // 默认分页从 0 开始,而 DataSet 默认分页从 1 开始.
        allRequestParams.put("start", String.valueOf((page - 1) * pageSize));
        //工作流测试 下拉框查询流程实例 默认分页大小为9999
        pageSize = (pageSize == 0) ? 9999 : pageSize;
        allRequestParams.put("size", String.valueOf(pageSize));

        if (ActivitiMultiLanguageManager.multiLanguageOpen) {
            if (allRequestParams.containsKey("nameLike")) {
                allRequestParams.put("nameLike", ActivitiMultiLanguageManager.getMultiLanguageInfoByDescription(allRequestParams.get("nameLike"), null));
            }
        }
        DataResponse dataResponse = getProcessDefinitions(allRequestParams, sortname);
        if (ActivitiMultiLanguageManager.multiLanguageOpen && CollectionUtils.isNotEmpty((List) dataResponse.getData())) {
            List<ProcessDefinitionResponse> processDefinitionResponses = (List<ProcessDefinitionResponse>) dataResponse.getData();
            for (ProcessDefinitionResponse processDefinitionResponse : processDefinitionResponses) {
                processDefinitionResponse.setName(ActivitiMultiLanguageManager.getMultLanguageInfoByCode(processDefinitionResponse.getName(), null));
            }
        }
        Page list = new Page(dataResponse.getStart(), dataResponse.getSize());
        list.setTotal(dataResponse.getTotal());
        list.setOrderBy(dataResponse.getOrder());
        List<ProcessDefinitionResponse> processDefinitionDtoList = (List<ProcessDefinitionResponse>) dataResponse.getData();
        list.addAll(processDefinitionDtoList);
        return list;
    }

    @Override
    public List<ProcessDefinitionDto> mutations(List<ProcessDefinitionDto> objs) {

        for (ProcessDefinitionDto item : objs) {
            switch (item.get__status()) {
                case DTOStatus.DELETE:
                    activitiService.deleteDeployment(item.getDeploymentId(), true);
                    break;
                default:
                    throw new DatasetException("错误的操作");
            }
        }
        return objs;
    }


    private DataResponse getProcessDefinitions(Map<String, String> allRequestParams, String sortname) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        // Populate filter-parameters
        if (allRequestParams.containsKey("category")) {
            processDefinitionQuery.processDefinitionCategory(allRequestParams.get("category"));
        }
        if (allRequestParams.containsKey("categoryLike")) {
            processDefinitionQuery.processDefinitionCategoryLike(allRequestParams.get("categoryLike"));
        }
        if (allRequestParams.containsKey("categoryNotEquals")) {
            processDefinitionQuery.processDefinitionCategoryNotEquals(allRequestParams.get("categoryNotEquals"));
        }
        if (allRequestParams.containsKey("key")) {
            processDefinitionQuery.processDefinitionKey(allRequestParams.get("key"));
        }
        if (allRequestParams.containsKey("keyLike")) {
            processDefinitionQuery.processDefinitionKeyLike(allRequestParams.get("keyLike"));
        }
        if (allRequestParams.containsKey("name")) {
            processDefinitionQuery.processDefinitionName(allRequestParams.get("name"));
        }
        if (allRequestParams.containsKey("nameLike")) {
            processDefinitionQuery.processDefinitionNameLike(allRequestParams.get("nameLike"));
        }
        if (allRequestParams.containsKey("resourceName")) {
            processDefinitionQuery.processDefinitionResourceName(allRequestParams.get("resourceName"));
        }
        if (allRequestParams.containsKey("resourceNameLike")) {
            processDefinitionQuery.processDefinitionResourceNameLike(allRequestParams.get("resourceNameLike"));
        }
        if (allRequestParams.containsKey("version")) {
            processDefinitionQuery.processDefinitionVersion(Integer.valueOf(allRequestParams.get("version")));
        }
        if (allRequestParams.containsKey("suspended")) {
            Boolean suspended = Boolean.valueOf(allRequestParams.get("suspended"));
            if (suspended != null) {
                if (suspended) {
                    processDefinitionQuery.suspended();
                } else {
                    processDefinitionQuery.active();
                }
            }
        }
        if (allRequestParams.containsKey("latest")) {
            Boolean latest = Boolean.valueOf(allRequestParams.get("latest"));
            if (latest != null && latest) {
                processDefinitionQuery.latestVersion();
            }
        }
        if (allRequestParams.containsKey("deploymentId")) {
            processDefinitionQuery.deploymentId(allRequestParams.get("deploymentId"));
        }
        if (allRequestParams.containsKey("startableByUser")) {
            processDefinitionQuery.startableByUser(allRequestParams.get("startableByUser"));
        }
        if (allRequestParams.containsKey("tenantId")) {
            processDefinitionQuery.processDefinitionTenantId(allRequestParams.get("tenantId"));
        }
        if (allRequestParams.containsKey("tenantIdLike")) {
            processDefinitionQuery.processDefinitionTenantIdLike(allRequestParams.get("tenantIdLike"));
        }

        return new ProcessDefinitionsPaginateList(restResponseFactory).paginateList(allRequestParams, processDefinitionQuery, sortname, properties);
    }
}
