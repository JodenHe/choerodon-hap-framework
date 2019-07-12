package io.choerodon.hap.activiti.service.impl;

import com.github.pagehelper.Page;
import io.choerodon.hap.activiti.components.ActivitiMultiLanguageManager;
import io.choerodon.hap.activiti.dto.ModelResponseExt;
import io.choerodon.base.annotation.Dataset;
import io.choerodon.dataset.service.IDatasetService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.ModelQueryProperty;
import org.activiti.engine.query.QueryProperty;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.rest.common.api.DataResponse;
import org.activiti.rest.service.api.RestResponseFactory;
import org.activiti.rest.service.api.repository.ModelResponse;
import org.activiti.rest.service.api.repository.ModelsPaginateList;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vista
 * @time 18-12-29 上午10:58
 */
@Service
@Dataset("BaseModel")
public class BaseModelServiceImpl implements IDatasetService<ModelResponse> {

    private static Map<String, QueryProperty> allowedSortProperties = new HashMap<>();

    static {
        allowedSortProperties.put("id", ModelQueryProperty.MODEL_ID);
        allowedSortProperties.put("category", ModelQueryProperty.MODEL_CATEGORY);
        allowedSortProperties.put("createTime", ModelQueryProperty.MODEL_CREATE_TIME);
        allowedSortProperties.put("key", ModelQueryProperty.MODEL_KEY);
        allowedSortProperties.put("lastUpdateTime", ModelQueryProperty.MODEL_LAST_UPDATE_TIME);
        allowedSortProperties.put("name", ModelQueryProperty.MODEL_NAME);
        allowedSortProperties.put("version", ModelQueryProperty.MODEL_VERSION);
        allowedSortProperties.put("tenantId", ModelQueryProperty.MODEL_TENANT_ID);
    }

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    protected RestResponseFactory restResponseFactory;

    @Override
    @SuppressWarnings("unchecked")
    public List<?> queries(Map<String, Object> body, int page, int pageSize, String sortname, boolean isDesc) {

        Map<String, String> allRequestParams = (Map) body;
        if (ActivitiMultiLanguageManager.multiLanguageOpen) {
            if (allRequestParams.containsKey("nameLike")) {
                allRequestParams.put("nameLike", ActivitiMultiLanguageManager.getMultiLanguageInfoByDescription(allRequestParams.get("nameLike"), null));
            }
        }

        allRequestParams.put("start", String.valueOf((page - 1) * pageSize));
        allRequestParams.put("size", String.valueOf(pageSize));

        ModelQuery modelQuery = repositoryService.createModelQuery();


        if (allRequestParams.containsKey("id")) {
            modelQuery.modelId(allRequestParams.get("id"));
        }
        if (allRequestParams.containsKey("category")) {
            modelQuery.modelCategory(allRequestParams.get("category"));
        }
        if (allRequestParams.containsKey("categoryLike")) {
            modelQuery.modelCategoryLike(allRequestParams.get("categoryLike"));
        }
        if (allRequestParams.containsKey("categoryNotEquals")) {
            modelQuery.modelCategoryNotEquals(allRequestParams.get("categoryNotEquals"));
        }
        if (allRequestParams.containsKey("name")) {
            modelQuery.modelName(allRequestParams.get("name"));
        }
        if (allRequestParams.containsKey("nameLike")) {
            modelQuery.modelNameLike(allRequestParams.get("nameLike"));
        }
        if (allRequestParams.containsKey("key")) {
            modelQuery.modelKey(allRequestParams.get("key"));
        }
        if (allRequestParams.containsKey("version")) {
            modelQuery.modelVersion(Integer.valueOf(allRequestParams.get("version")));
        }
        if (allRequestParams.containsKey("latestVersion")) {
            boolean isLatestVersion = Boolean.valueOf(allRequestParams.get("latestVersion"));
            if (isLatestVersion) {
                modelQuery.latestVersion();
            }
        }
        if (allRequestParams.containsKey("deploymentId")) {
            modelQuery.deploymentId(allRequestParams.get("deploymentId"));
        }
        if (allRequestParams.containsKey("deployed")) {
            boolean isDeployed = Boolean.valueOf(allRequestParams.get("deployed"));
            if (isDeployed) {
                modelQuery.deployed();
            } else {
                modelQuery.notDeployed();
            }
        }
        if (allRequestParams.containsKey("tenantId")) {
            modelQuery.modelTenantId(allRequestParams.get("tenantId"));
        }
        if (allRequestParams.containsKey("tenantIdLike")) {
            modelQuery.modelTenantIdLike(allRequestParams.get("tenantIdLike"));
        }
        if (allRequestParams.containsKey("withoutTenantId")) {
            boolean withoutTenantId = Boolean.valueOf(allRequestParams.get("withoutTenantId"));
            if (withoutTenantId) {
                modelQuery.modelWithoutTenantId();
            }
        }
        DataResponse dataResponse = new ModelsPaginateList(restResponseFactory).paginateList(allRequestParams, modelQuery, "id", allowedSortProperties);
        if (ActivitiMultiLanguageManager.multiLanguageOpen && CollectionUtils.isNotEmpty((List) dataResponse.getData())) {
            List<ModelResponseExt> modelResponseExts = (List<ModelResponseExt>) dataResponse.getData();
            for (ModelResponseExt modelResponseExt : modelResponseExts) {
                modelResponseExt.setName(ActivitiMultiLanguageManager.getMultLanguageInfoByCode(modelResponseExt.getName(), null));
            }
        }

        Page list = new Page(dataResponse.getStart(), dataResponse.getSize());
        list.setTotal(dataResponse.getTotal());
        list.setOrderBy(dataResponse.getOrder());

        List<ModelResponse> modelResponseList = (List<ModelResponse>) dataResponse.getData();
        list.addAll(modelResponseList);

        return list;
    }

    @Override
    public List<ModelResponse> mutations(List<ModelResponse> objs) {
        return null;
    }
}
