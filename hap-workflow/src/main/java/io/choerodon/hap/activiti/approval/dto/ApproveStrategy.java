package io.choerodon.hap.activiti.approval.dto;


import io.choerodon.mybatis.common.query.Comparison;
import io.choerodon.mybatis.common.query.Where;
import io.choerodon.mybatis.entity.BaseDTO;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

/**
 * @author xiangyu.qi@hand-china.com
 */
@Table(name = "wfl_approve_strategy")
public class ApproveStrategy extends BaseDTO {
    public static final String FIELD_CODE = "code";
    public static final String FIELD_DESCRIPTION = "description";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OrderBy("DESC")
    private Long approveStrategyId;

    @NotEmpty
    @Length(max = 50)
    @Where
    private String code;

    @NotEmpty
    @Length(max = 255)
    @Where(comparison = Comparison.LIKE)
    private String description;

    @Column(name = "conditions")
    @Length(max = 500)
    @NotEmpty
    private String condition;

    private String enableFlag;

    public void setApproveStrategyId(Long approveStrategyId) {
        this.approveStrategyId = approveStrategyId;
    }

    public Long getApproveStrategyId() {
        return approveStrategyId;
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

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

}
