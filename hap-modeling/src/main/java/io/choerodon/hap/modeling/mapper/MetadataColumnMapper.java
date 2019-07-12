package io.choerodon.hap.modeling.mapper;

import io.choerodon.hap.modeling.dto.MetadataColumn;
import io.choerodon.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MetadataColumnMapper extends Mapper<MetadataColumn> {
    List<MetadataColumn> selectColumnByTableName(@Param("tableName") String tableName);
}
