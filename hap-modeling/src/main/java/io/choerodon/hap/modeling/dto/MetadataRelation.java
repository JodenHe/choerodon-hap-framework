package io.choerodon.hap.modeling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.entity.BaseDTO;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "metadata_relation")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetadataRelation extends BaseDTO {
    public static final String RELATION_TYPE_ONE_TO_MANY = "ONE_TO_MANY";
    public static final String RELATION_TYPE_ONE_TO_ONE = "ONE_TO_ONE";
    @Id
    private String id;
    private String masterTable;
    private String masterColumn;
    private String relationTable;
    private String relationColumn;
    private String uniqueName;
    private String relationType;
    private Boolean enable;

    @Transient
    private String lockedBy;

    public String getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getMasterTable() {
        return masterTable;
    }

    public void setMasterTable(String masterTable) {
        this.masterTable = masterTable;
    }

    public String getMasterColumn() {
        return masterColumn;
    }

    public void setMasterColumn(String masterColumn) {
        this.masterColumn = masterColumn;
    }

    public String getRelationTable() {
        return relationTable;
    }

    public void setRelationTable(String relationTable) {
        this.relationTable = relationTable;
    }

    public String getRelationColumn() {
        return relationColumn;
    }

    public void setRelationColumn(String relationColumn) {
        this.relationColumn = relationColumn;
    }


}
