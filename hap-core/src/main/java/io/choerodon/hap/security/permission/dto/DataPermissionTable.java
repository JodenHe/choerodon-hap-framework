package io.choerodon.hap.security.permission.dto;

import io.choerodon.mybatis.entity.BaseDTO;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

/**
 * @author jialong.zuo@hand-china.com
 */
@Table(name = "sys_permission_table")
public class DataPermissionTable extends BaseDTO {

    public static final String FIELD_TABLE_ID = "tableId";
    public static final String FIELD_TABLE_NAME = "tableName";
    public static final String FIELD_DESCRIPTION = "description";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tableId;

    //MANGE 表名
    @NotEmpty
    @Length(max = 250)
    private String tableName;

    //描述
    @Length(max = 250)
    private String description;

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
