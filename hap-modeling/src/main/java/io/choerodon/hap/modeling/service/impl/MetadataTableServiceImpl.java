package io.choerodon.hap.modeling.service.impl;

import io.choerodon.hap.modeling.service.IMetadataTableService;
import io.choerodon.hap.modeling.dto.MetadataColumn;
import io.choerodon.hap.modeling.dto.MetadataTable;
import io.choerodon.hap.modeling.mapper.MetadataColumnMapper;
import io.choerodon.hap.modeling.mapper.MetadataTableMapper;
import io.choerodon.mybatis.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class MetadataTableServiceImpl extends BaseServiceImpl<MetadataTable> implements IMetadataTableService {
    @Autowired
    private MetadataTableMapper metadataTableMapper;
    @Autowired
    private MetadataColumnMapper metadataColumnMapper;

    @Override
    public MetadataTable queryTable(String name) {
        MetadataTable table = new MetadataTable();
        table.setTableName(name);
        MetadataTable resultTable = metadataTableMapper.selectOne(table);
        if(resultTable == null){
            return null;
        }
        List<MetadataColumn> columns = metadataColumnMapper.selectColumnByTableName(name);
        Set<String> primaryColumns = new TreeSet<>();
        for(MetadataColumn column: columns){
            if(Boolean.TRUE.equals(column.getPrimaryKey())){
                primaryColumns.add(column.getColumnName());
            }
        }
        resultTable.setPrimaryColumns(primaryColumns);
        columns.forEach(MetadataColumn::solveDisplayType);
        resultTable.setColumns(columns);
        return resultTable;
    }

    @Override
    public List<MetadataTable> selectAllTables() {
        return metadataTableMapper.selectAllTables();
    }
}
