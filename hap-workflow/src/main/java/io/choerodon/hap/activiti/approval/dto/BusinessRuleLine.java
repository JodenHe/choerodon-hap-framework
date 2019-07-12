package io.choerodon.hap.activiti.approval.dto;


import io.choerodon.mybatis.common.query.Where;
import io.choerodon.mybatis.entity.BaseDTO;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

/**
 * @author xiangyu.qi@hand-china.com
 */
@Table(name = "wfl_business_rule_line")
public class BusinessRuleLine extends BaseDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long businessRuleLineId;

    @Where
    private Long businessRuleId;

    @NotEmpty
    @Length(max = 255)
    private String description;

    @Length(max = 500)
    @NotEmpty
    private String conditions;

    private String enableFlag;

    public void setBusinessRuleLineId(Long businessRuleLineId) {
        this.businessRuleLineId = businessRuleLineId;
    }

    public Long getBusinessRuleLineId() {
        return businessRuleLineId;
    }

    public void setBusinessRuleId(Long businessRuleId) {
        this.businessRuleId = businessRuleId;
    }

    public Long getBusinessRuleId() {
        return businessRuleId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getConditions() {
        return conditions;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

}
