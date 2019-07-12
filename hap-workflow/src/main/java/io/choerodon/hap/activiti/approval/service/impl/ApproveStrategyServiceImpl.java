package io.choerodon.hap.activiti.approval.service.impl;

import io.choerodon.hap.activiti.approval.dto.ApproveStrategy;
import io.choerodon.hap.activiti.approval.mapper.ApproveStrategyMapper;
import io.choerodon.hap.activiti.approval.service.IApproveStrategyService;
import io.choerodon.hap.system.dto.DTOStatus;
import io.choerodon.web.core.IRequest;
import io.choerodon.base.annotation.Dataset;
import io.choerodon.dataset.exception.DatasetException;
import io.choerodon.dataset.service.IDatasetService;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.choerodon.mybatis.common.query.Comparison;
import io.choerodon.mybatis.common.query.WhereField;
import io.choerodon.mybatis.entity.Criteria;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Dataset("ApproveStrategy")
public class ApproveStrategyServiceImpl extends BaseServiceImpl<ApproveStrategy> implements IApproveStrategyService, IDatasetService<ApproveStrategy> {

    @Autowired
    private ApproveStrategyMapper mapper;

    @Override
    public List<ApproveStrategy> selectAll(IRequest request, ApproveStrategy strategy) {
        return mapper.select(strategy);
    }

    @Override
    public List<?> queries(Map<String, Object> body, int page, int pageSize, String sortname, boolean isDesc) {
        try {
            ApproveStrategy approveStrategy = new ApproveStrategy();
            BeanUtils.populate(approveStrategy, body);
            approveStrategy.setSortname(sortname);
            approveStrategy.setSortorder(isDesc ? "desc" : "asc");
            Criteria criteria = new Criteria(approveStrategy);
            criteria.where(ApproveStrategy.FIELD_CODE, new WhereField(ApproveStrategy.FIELD_DESCRIPTION, Comparison.LIKE));
            return super.selectOptions(approveStrategy, criteria, page, pageSize);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new DatasetException("dataset.error.ApproveStrategy", e);
        }
    }

    @Override
    public List<ApproveStrategy> mutations(List<ApproveStrategy> objs) {
        for (ApproveStrategy approveStrategy : objs) {
            switch (approveStrategy.get__status()) {
                case DTOStatus.ADD:
                    super.insertSelective(approveStrategy);
                    break;
                case DTOStatus.UPDATE:
                    super.updateByPrimaryKey(approveStrategy);
                    break;
                case DTOStatus.DELETE:
                    super.deleteByPrimaryKey(approveStrategy);
                    break;
                default:
                    break;
            }
        }
        return objs;
    }

}