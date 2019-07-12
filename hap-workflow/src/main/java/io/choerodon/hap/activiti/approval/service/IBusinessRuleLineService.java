package io.choerodon.hap.activiti.approval.service;

import io.choerodon.hap.activiti.approval.dto.BusinessRuleLine;
import io.choerodon.hap.core.ProxySelf;
import io.choerodon.mybatis.service.IBaseService;

public interface IBusinessRuleLineService
        extends IBaseService<BusinessRuleLine>, ProxySelf<IBusinessRuleLineService> {

}