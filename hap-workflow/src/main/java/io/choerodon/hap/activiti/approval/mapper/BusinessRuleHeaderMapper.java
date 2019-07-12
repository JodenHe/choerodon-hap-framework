package io.choerodon.hap.activiti.approval.mapper;

import io.choerodon.hap.activiti.approval.dto.BusinessRuleHeader;
import io.choerodon.mybatis.common.Mapper;

public interface BusinessRuleHeaderMapper extends Mapper<BusinessRuleHeader> {

    BusinessRuleHeader selectByCode(String code);
}