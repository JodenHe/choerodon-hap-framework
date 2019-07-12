package io.choerodon.hap.activiti.approval.dto;


import io.choerodon.mybatis.common.query.Comparison;
import io.choerodon.mybatis.common.query.Where;
import io.choerodon.mybatis.entity.BaseDTO;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

/**
 * @author xiangyu.qi@hand-china.com
 */
@Table(name = "wfl_approve_candidate_rule")
public class ApproveCandidateRule extends BaseDTO {
    public static final String FIELD_CODE = "code";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OrderBy("DESC")
    private Long candidateRuleId;

    @NotEmpty
    @Length(max = 50)
    private String code;

    @NotEmpty
    @Length(max = 255)
    @Where(comparison = Comparison.LIKE)
    private String description;

    @NotEmpty
    @Length(max = 100)
    private String expression;

    private String enableFlag;

    public void setCandidateRuleId(Long candidateRuleId) {
        this.candidateRuleId = candidateRuleId;
    }

    public Long getCandidateRuleId() {
        return candidateRuleId;
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

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

}
