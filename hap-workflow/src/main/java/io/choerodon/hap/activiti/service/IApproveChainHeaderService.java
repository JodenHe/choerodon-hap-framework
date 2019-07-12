package io.choerodon.hap.activiti.service;

import io.choerodon.hap.activiti.dto.ApproveChainHeader;
import io.choerodon.hap.core.ProxySelf;
import io.choerodon.mybatis.service.IBaseService;

import java.util.List;

public interface IApproveChainHeaderService extends IBaseService<ApproveChainHeader>, ProxySelf<IApproveChainHeaderService> {

    ApproveChainHeader selectByUserTask(String ProcessDefinitionId, String userTaskId);

    List<ApproveChainHeader> updateHeadLine(List<ApproveChainHeader> dto);
}