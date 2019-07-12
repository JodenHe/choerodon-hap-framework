package io.choerodon.hap.modeling.mapper;

import io.choerodon.hap.modeling.dto.Metadata;
import io.choerodon.hap.modeling.dto.MetadataColumn;
import io.choerodon.hap.modeling.dto.MetadataTable;
import io.choerodon.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Creator: ChangpingShi0213@gmail.com
 * Date:  16:05 2018/11/17
 * Description:
 */
public interface MetadataMapper extends Mapper<Metadata> {
    void createTable(@Param("table") MetadataTable table);
    void renameTable(@Param("table") MetadataTable table, @Param("newName")String newName);
    void removeTable(@Param("table") MetadataTable table);
    void createColumn(@Param("column") MetadataColumn column);
    void renameColumn(@Param("column") MetadataColumn column, @Param("newName") String newName);
    void removeColumn(@Param("column") MetadataColumn column);
    void createMultiLanguageTable(@Param("table") MetadataTable table);
}