package io.choerodon.hap.activiti.approval.dto;


import io.choerodon.base.annotation.Children;
import io.choerodon.mybatis.common.query.Comparison;
import io.choerodon.mybatis.common.query.Where;
import io.choerodon.mybatis.entity.BaseDTO;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * @author xiangyu.qi@hand-china.com
 */
@Table(name = "wfl_business_rule_header")
public class BusinessRuleHeader extends BaseDTO {
    public static final String FIELD_CODE = "code";
    public static final String FIELD_DESCRIPTION = "description";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OrderBy("DESC")
    private Long businessRuleId;

    @NotEmpty
    @Length(max = 50)
    @Where
    private String code;

    @NotEmpty
    @Length(max = 255)
    @Where(comparison = Comparison.LIKE)
    private String description;

    private String wflType;

    private String enableFlag;

    @Where(comparison = Comparison.GREATER_THAN_OR_EQUALTO)
    private Date startActiveDate;

    @Where(comparison = Comparison.LESS_THAN_OR_EQUALTO)
    private Date endActiveDate;

    @Children
    @Transient
    private List<BusinessRuleLine> lines;

    public void setBusinessRuleId(Long businessRuleId) {
        this.businessRuleId = businessRuleId;
    }

    public Long getBusinessRuleId() {
        return businessRuleId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setWflType(String wflType) {
        this.wflType = wflType;
    }

    public String getWflType() {
        return wflType;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setStartActiveDate(Date startActiveDate) {
        this.startActiveDate = startActiveDate;
    }

    public Date getStartActiveDate() {
        return startActiveDate;
    }

    public void setEndActiveDate(Date endActiveDate) {
        this.endActiveDate = endActiveDate;
    }

    public Date getEndActiveDate() {
        return endActiveDate;
    }

    public List<BusinessRuleLine> getLines() {
        return lines;
    }

    public void setLines(List<BusinessRuleLine> lines) {
        this.lines = lines;
    }
}
