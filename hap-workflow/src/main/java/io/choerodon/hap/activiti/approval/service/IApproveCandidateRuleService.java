package io.choerodon.hap.activiti.approval.service;

import io.choerodon.hap.activiti.approval.dto.ApproveCandidateRule;
import io.choerodon.hap.core.ProxySelf;
import io.choerodon.web.core.IRequest;
import io.choerodon.mybatis.service.IBaseService;

import java.util.List;

public interface IApproveCandidateRuleService
        extends IBaseService<ApproveCandidateRule>, ProxySelf<IApproveCandidateRuleService> {

    List<ApproveCandidateRule> selectAll(IRequest request, ApproveCandidateRule rule);
}