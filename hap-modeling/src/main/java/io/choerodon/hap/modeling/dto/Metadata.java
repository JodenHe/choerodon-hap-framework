package io.choerodon.hap.modeling.dto;

import io.choerodon.mybatis.entity.BaseDTO;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Creator: ChangpingShi0213@gmail.com
 * Date:  15:49 2018/11/17
 * Description: metadatad对象
 */
@Table(name = "metadata")
public class Metadata extends BaseDTO {
    public static final String DATA_TYPE_PAGE = "PAGE";
    public static final String DATA_TYPE_TABLE = "TABLE";
    public static final String DATA_TYPE_RELATION = "RELATION";
    public static final String STATUS_CHECKOUT = "CHECKOUT";
    public static final String STATUS_COMMITTED = "COMMITTED";
    @Id
    private String id;
    private String name;
    private String description;
    private String dataType;
    private String status;
    private String lockedBy;
    private String dataId;
    private String changeId;

    @Transient
    private MetadataItem change;

    public MetadataItem getChange() {
        return change;
    }

    public void setChange(MetadataItem change) {
        this.change = change;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getChangeId() {
        return changeId;
    }

    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

}
