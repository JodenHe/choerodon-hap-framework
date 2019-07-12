package io.choerodon.hap.activiti.service.impl;

import io.choerodon.hap.activiti.components.ActivitiMultiLanguageManager;
import io.choerodon.hap.activiti.exception.dto.ActiviException;
import io.choerodon.hap.activiti.service.IActivitiService;
import io.choerodon.base.annotation.Dataset;
import io.choerodon.dataset.exception.DatasetException;
import io.choerodon.dataset.service.IDatasetService;
import io.choerodon.web.core.impl.RequestHelper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author qiang.zeng
 * @since 2018/12/28.
 */
@Service
@Dataset("ActiviException")
public class ActiviExceptionServiceImpl implements IDatasetService<ActiviException> {

    @Autowired
    private IActivitiService activitiService;

    @Override
    public List<?> queries(Map<String, Object> body, int page, int pageSize, String sortname, boolean isDesc) {
        try {
            ActiviException exception = new ActiviException();
            BeanUtils.populate(exception, body);
            List<ActiviException> exceptions = activitiService.queryException(exception, page, pageSize);
            if (ActivitiMultiLanguageManager.multiLanguageOpen && CollectionUtils.isNotEmpty(exceptions)) {
                for (ActiviException exception1 : exceptions) {
                    exception1.setProcDefName(ActivitiMultiLanguageManager.getMultLanguageInfoByCode(exception1.getProcDefName(), RequestHelper.getCurrentRequest()));
                }
            }
            return exceptions;
        } catch (Exception e) {
            throw new DatasetException("dataset.error", e);
        }
    }

    @Override
    public List<ActiviException> mutations(List<ActiviException> objs) {
        return null;
    }
}
