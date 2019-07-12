package io.choerodon.hap.activiti.approval.service.impl;

import io.choerodon.hap.activiti.approval.dto.ApproveCandidateRule;
import io.choerodon.hap.activiti.approval.mapper.ApproveCandidateRuleMapper;
import io.choerodon.hap.activiti.approval.service.IApproveCandidateRuleService;
import io.choerodon.hap.system.dto.DTOStatus;
import io.choerodon.web.core.IRequest;
import io.choerodon.base.annotation.Dataset;
import io.choerodon.dataset.exception.DatasetException;
import io.choerodon.dataset.service.IDatasetService;
import io.choerodon.mybatis.service.BaseServiceImpl;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Dataset("ApproveCandidateRule")
public class ApproveCandidateRuleServiceImpl extends BaseServiceImpl<ApproveCandidateRule> implements IApproveCandidateRuleService, IDatasetService<ApproveCandidateRule> {

    @Autowired
    private ApproveCandidateRuleMapper mapper;

    @Override
    public List<ApproveCandidateRule> selectAll(IRequest request, ApproveCandidateRule rule) {
        return mapper.select(rule);
    }

    @Override
    public List<?> queries(Map<String, Object> body, int page, int pageSize, String sortname, boolean isDesc) {
        try {
            ApproveCandidateRule candidateRule = new ApproveCandidateRule();
            BeanUtils.populate(candidateRule, body);
            return select(candidateRule, page, pageSize);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new DatasetException("dataset.error.ApproveCandidateRule", e);
        }
    }

    @Override
    public List<ApproveCandidateRule> mutations(List<ApproveCandidateRule> objs) {
        for (ApproveCandidateRule candidateRule : objs) {
            switch (candidateRule.get__status()) {
                case DTOStatus.ADD:
                    super.insertSelective(candidateRule);
                    break;
                case DTOStatus.UPDATE:
                    super.updateByPrimaryKey(candidateRule);
                    break;
                case DTOStatus.DELETE:
                    super.deleteByPrimaryKey(candidateRule);
                    break;
                default:
                    break;
            }
        }
        return objs;
    }

}