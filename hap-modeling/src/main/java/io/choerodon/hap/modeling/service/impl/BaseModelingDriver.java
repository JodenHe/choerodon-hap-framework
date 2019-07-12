package io.choerodon.hap.modeling.service.impl;

import io.choerodon.hap.modeling.dto.MetadataColumn;
import io.choerodon.hap.modeling.dto.MetadataTable;
import io.choerodon.hap.modeling.mapper.MetadataMapper;
import io.choerodon.hap.modeling.service.IModelingDriver;

import java.sql.SQLException;

public class BaseModelingDriver implements IModelingDriver {
    public static final String TABLE_DELETE_RESTORE_PREFIX = "__del_";
    public static final String COLUMN_DELETE_RESTORE_PREFIX = "__DEL_";
    protected MetadataMapper mapper;

    public BaseModelingDriver(MetadataMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void createTable(MetadataTable table) throws SQLException {
        mapper.createTable(table);
    }

    @Override
    public void removeTable(MetadataTable table) throws SQLException {
        mapper.removeTable(table);
    }

    @Override
    public void renameTable(MetadataTable table, String newName) throws SQLException {
        mapper.renameTable(table, newName);
    }

    @Override
    public void createColumn(MetadataColumn column) throws SQLException {
        mapper.createColumn(column);
    }

    @Override
    public void removeColumn(MetadataColumn column) throws SQLException {
        mapper.removeColumn(column);
    }

    @Override
    public void renameColumn(MetadataColumn column, String newName) throws SQLException {
        mapper.renameColumn(column, newName);
    }

    @Override
    public void createMultiLanguageTable(MetadataTable table) throws SQLException {
        mapper.createMultiLanguageTable(table);
    }

}
