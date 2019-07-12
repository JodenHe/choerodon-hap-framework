package io.choerodon.hap.modeling.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.choerodon.mybatis.entity.BaseDTO;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Creator: ChangpingShi0213@gmail.com
 * Date:  15:59 2018/11/17
 * Description:METADATA_ITEM对象
 */
@Table(name = "metadata_item")
public class MetadataItem extends BaseDTO {
    @Id
    private String id;
    private String metadataId;
    private String data;
    private Long dataVersion;

    @Override
    @JsonProperty
    @JsonIgnore(value = false)
    public Date getLastUpdateDate() {
        return super.getLastUpdateDate();
    }

    @Override
    @JsonProperty
    @JsonIgnore(value = false)
    public void setLastUpdateDate(Date lastUpdateDate) {
        super.setLastUpdateDate(lastUpdateDate);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(String metadataId) {
        this.metadataId = metadataId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Long dataVersion) {
        this.dataVersion = dataVersion;
    }
}
