package io.choerodon.hap.activiti.approval.service;

import io.choerodon.hap.activiti.approval.dto.BusinessRuleHeader;
import io.choerodon.hap.core.ProxySelf;
import io.choerodon.web.core.IRequest;
import io.choerodon.mybatis.service.IBaseService;

import java.util.List;

public interface IBusinessRuleHeaderService
        extends IBaseService<BusinessRuleHeader>, ProxySelf<IBusinessRuleHeaderService> {

    BusinessRuleHeader createRule(BusinessRuleHeader header);

    BusinessRuleHeader updateRule(BusinessRuleHeader header);

    boolean batchDelete(IRequest request, List<BusinessRuleHeader> headers);

    List<BusinessRuleHeader> selectAll(IRequest request, BusinessRuleHeader header);
}