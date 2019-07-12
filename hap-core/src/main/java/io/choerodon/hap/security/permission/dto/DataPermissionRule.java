package io.choerodon.hap.security.permission.dto;

import io.choerodon.mybatis.common.query.Where;
import io.choerodon.mybatis.entity.BaseDTO;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

/**
 * @author jialong.zuo@hand-china.com
 */
@Table(name = "sys_permission_rule")
public class DataPermissionRule extends BaseDTO {

    public static final String FIELD_RULE_ID = "ruleId";
    public static final String FIELD_RULE_CODE = "ruleCode";
    public static final String FIELD_RULE_NAME = "ruleName";
    public static final String FIELD_PERMISSION_FIELD = "permissionField";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ruleId;

    //屏蔽规则code
    @NotEmpty
    @Length(max = 250)
    private String ruleCode;

    //屏蔽规则NAME
    @NotEmpty
    @Length(max = 250)
    @Where
    private String ruleName;

    //安全性字段
    @NotEmpty
    @Length(max = 100)
    private String permissionField;

    @Transient
    private Long tableId;

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setPermissionField(String permissionField) {
        this.permissionField = permissionField;
    }

    public String getPermissionField() {
        return permissionField;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }
}
