package io.choerodon.hap.activiti.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import io.choerodon.hap.activiti.components.ActivitiMultiLanguageManager;
import io.choerodon.hap.activiti.custom.process.CustomHistoricProcessInstanceQueryRequest;
import io.choerodon.hap.activiti.dto.HistoricProcessInstanceResponseExt;
import io.choerodon.hap.activiti.exception.WflSecurityException;
import io.choerodon.hap.activiti.service.IActivitiService;
import io.choerodon.hap.activiti.service.MyStartService;
import io.choerodon.base.annotation.Dataset;
import io.choerodon.dataset.exception.DatasetException;
import io.choerodon.dataset.service.IDatasetService;
import io.choerodon.web.core.IRequest;
import io.choerodon.web.core.impl.RequestHelper;
import org.activiti.rest.common.api.DataResponse;
import org.activiti.rest.service.api.history.HistoricProcessInstanceQueryRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我发起的流程.
 *
 * @author jiameng.cao
 * @since 2018/12/27
 */
@Service
@Dataset("MyStart")
public class MyStartServiceImpl implements IDatasetService<HistoricProcessInstanceQueryRequest>, MyStartService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IActivitiService iActivitiService;


    @Override
    @SuppressWarnings("unchecked")
    public List<?> queries(Map<String, Object> body, int page, int pageSize, String sortname, boolean isDesc) {
        try {
            IRequest iRequest = RequestHelper.getCurrentRequest();
            Map<String, String> requestParams = new HashMap<>();
            CustomHistoricProcessInstanceQueryRequest historicProcessInstanceQueryRequest = objectMapper.readValue(objectMapper.writeValueAsString(body), CustomHistoricProcessInstanceQueryRequest.class);
            historicProcessInstanceQueryRequest.setProcessInstanceId((String) body.get("id"));
            historicProcessInstanceQueryRequest.setProcessDefinitionNameLike((String) body.get("processName"));
            if (ActivitiMultiLanguageManager.multiLanguageOpen) {
                historicProcessInstanceQueryRequest.setProcessDefinitionNameLike
                        (ActivitiMultiLanguageManager.getMultiLanguageInfoByDescription(historicProcessInstanceQueryRequest.getProcessDefinitionNameLike(), iRequest));
            }
            if (StringUtils.isEmpty(iRequest.getEmployeeCode())) {
                throw new WflSecurityException(WflSecurityException.USER_NOT_RELATE_EMP);
            }
            if (sortname == null) {
                historicProcessInstanceQueryRequest.setSort("startTime");
                historicProcessInstanceQueryRequest.setOrder("desc");
            } else {
                historicProcessInstanceQueryRequest.setSort(sortname);
                historicProcessInstanceQueryRequest.setOrder(isDesc ? "desc" : "asc");
            }
            historicProcessInstanceQueryRequest.setStart((page - 1) * pageSize);
            historicProcessInstanceQueryRequest.setSize(pageSize);
            historicProcessInstanceQueryRequest.setStartedBy(iRequest.getEmployeeCode());
            requestParams.put("startedBy", "true");
            DataResponse dataResponse = iActivitiService.queryProcessInstances(iRequest, historicProcessInstanceQueryRequest, requestParams, true);
            Page list = new Page(dataResponse.getStart(), dataResponse.getSize());
            list.setTotal(dataResponse.getTotal());
            list.setOrderBy(dataResponse.getOrder());
            list.addAll((List<HistoricProcessInstanceResponseExt>) dataResponse.getData());
            return list;
        } catch (Exception e) {
            throw new DatasetException("dataset:error", e);
        }
    }

    @Override
    public List<HistoricProcessInstanceQueryRequest> mutations(List<HistoricProcessInstanceQueryRequest> objs) {
        return null;
    }
}
