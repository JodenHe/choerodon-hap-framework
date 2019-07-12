package io.choerodon.hap.modeling.mapper;

import io.choerodon.hap.modeling.dto.MetadataRelation;
import io.choerodon.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MetadataRelationMapper extends Mapper<MetadataRelation> {
    List<MetadataRelation> selectMetadataRelations();
    List<Map<String, Object>> selectRelationTable(@Param("table") String table);
}
