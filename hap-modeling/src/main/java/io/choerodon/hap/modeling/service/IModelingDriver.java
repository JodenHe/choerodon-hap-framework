package io.choerodon.hap.modeling.service;


import io.choerodon.hap.modeling.dto.MetadataColumn;
import io.choerodon.hap.modeling.dto.MetadataTable;

import java.sql.SQLException;

public interface IModelingDriver {
    void createTable(MetadataTable table) throws SQLException;
    void removeTable(MetadataTable table) throws SQLException;
    void renameTable(MetadataTable table, String newName) throws SQLException;
    void createColumn(MetadataColumn column) throws SQLException;
    void removeColumn(MetadataColumn column) throws SQLException;
    void renameColumn(MetadataColumn column, String newName) throws SQLException;
    void createMultiLanguageTable(MetadataTable table) throws SQLException;
}
