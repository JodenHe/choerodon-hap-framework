package io.choerodon.hap.activiti.service;

import io.choerodon.hap.activiti.dto.ApproveChainLine;
import io.choerodon.hap.core.ProxySelf;
import io.choerodon.mybatis.service.IBaseService;
import io.choerodon.web.core.IRequest;

import java.util.List;

public interface IApproveChainLineService extends IBaseService<ApproveChainLine>, ProxySelf<IApproveChainLineService> {
    List<ApproveChainLine> selectByHeaderId(IRequest iRequest, Long headerId);

    List<ApproveChainLine> selectByNodeId(IRequest iRequest, String key, String nodeId);
}