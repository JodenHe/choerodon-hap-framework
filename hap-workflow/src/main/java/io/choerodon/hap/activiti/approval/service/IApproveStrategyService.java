package io.choerodon.hap.activiti.approval.service;

import io.choerodon.hap.activiti.approval.dto.ApproveStrategy;
import io.choerodon.hap.core.ProxySelf;
import io.choerodon.web.core.IRequest;
import io.choerodon.mybatis.service.IBaseService;

import java.util.List;

public interface IApproveStrategyService
        extends IBaseService<ApproveStrategy>, ProxySelf<IApproveStrategyService> {

    List<ApproveStrategy> selectAll(IRequest request, ApproveStrategy strategy);
}