package io.choerodon.hap.modeling.mapper;

import io.choerodon.hap.modeling.dto.MetadataTable;
import io.choerodon.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;

import java.util.List;

public interface MetadataTableMapper extends Mapper<MetadataTable> {
    /**
     * 根据tableName查询MetadataTable，模糊查询
     * @param tableName 模糊查询的name， 为空查询所有
     * @return 查询到的MetadataTable
     */
    List<MetadataTable> selectMetadataTables(@Nullable @Param("tableName") String tableName);
    List<MetadataTable> selectAllTables();
}
