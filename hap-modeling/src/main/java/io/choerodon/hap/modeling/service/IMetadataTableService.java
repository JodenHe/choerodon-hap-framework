package io.choerodon.hap.modeling.service;

import io.choerodon.hap.modeling.dto.MetadataTable;
import io.choerodon.mybatis.service.IBaseService;

import java.util.List;

public interface IMetadataTableService extends IBaseService<MetadataTable> {
    MetadataTable queryTable(String name);
    List<MetadataTable> selectAllTables();
}
