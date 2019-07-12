package io.choerodon.hap.activiti.approval.service.impl;

import io.choerodon.hap.activiti.approval.dto.BusinessRuleLine;
import io.choerodon.hap.activiti.approval.service.IBusinessRuleLineService;
import io.choerodon.base.annotation.Dataset;
import io.choerodon.dataset.exception.DatasetException;
import io.choerodon.dataset.service.IDatasetService;
import io.choerodon.hap.system.dto.DTOStatus;
import io.choerodon.mybatis.service.BaseServiceImpl;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Dataset("BusinessRuleLine")
public class BusinessRuleLineServiceImpl extends BaseServiceImpl<BusinessRuleLine> implements IBusinessRuleLineService, IDatasetService<BusinessRuleLine> {

    @Override
    public List<?> queries(Map<String, Object> body, int page, int pageSize, String sortname, boolean isDesc) {
        try {
            BusinessRuleLine businessRuleLine = new BusinessRuleLine();
            BeanUtils.populate(businessRuleLine, body);
            return select(businessRuleLine, page, pageSize);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new DatasetException("dataset.error.BusinessRuleLine", e);
        }
    }

    @Override
    public List<BusinessRuleLine> mutations(List<BusinessRuleLine> objs) {
        for (BusinessRuleLine approveStrategy : objs) {
            if (approveStrategy.get__status().equals(DTOStatus.DELETE)) {
                deleteByPrimaryKey(approveStrategy);
            }
        }
        return objs;
    }
}