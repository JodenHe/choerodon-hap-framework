package io.choerodon.hap.modeling.service.impl;

import io.choerodon.hap.modeling.dto.MetadataColumn;
import io.choerodon.hap.modeling.dto.MetadataTable;
import io.choerodon.hap.modeling.mapper.MetadataMapper;
import io.choerodon.hap.modeling.service.IModelingDriver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class ModelingDriverDelegate implements IModelingDriver, InitializingBean {
    private IModelingDriver delegate;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private MetadataMapper metadataMapper;


    public String getMultiLanguageTableName(String tableName) {
        return tableName.substring(0, tableName.length() - 2) + "_tl";
    }

    @Override
    public void createTable(MetadataTable table) throws SQLException {
        delegate.createTable(table);
        if (Boolean.TRUE.equals(table.getMultiLanguage())) {
            MetadataTable tlTable = new MetadataTable();
            tlTable.setTableName(getMultiLanguageTableName(table.getTableName()));
            delegate.createMultiLanguageTable(tlTable);
        }
    }

    @Override
    public void removeTable(MetadataTable table) throws SQLException {
        delegate.removeTable(table);
        if (Boolean.TRUE.equals(table.getMultiLanguage())) {
            MetadataTable tlTable = new MetadataTable();
            tlTable.setTableName(getMultiLanguageTableName(table.getTableName()));
            delegate.removeTable(tlTable);
        }
    }

    @Override
    public void renameTable(MetadataTable table, String newName) throws SQLException {
        delegate.renameTable(table, newName);
        if (Boolean.TRUE.equals(table.getMultiLanguage())) {
            MetadataTable tlTable = new MetadataTable();
            tlTable.setTableName(getMultiLanguageTableName(table.getTableName()));
            delegate.renameTable(tlTable, getMultiLanguageTableName(newName));
        }
    }

    @Override
    public void createColumn(MetadataColumn column) throws SQLException {
        delegate.createColumn(column);
        if (Boolean.TRUE.equals(column.getMultiLanguage())) {
            MetadataColumn tlColumn = new MetadataColumn();
            tlColumn.setTableName(getMultiLanguageTableName(column.getTableName()));
            tlColumn.setColumnName(column.getColumnName());
            tlColumn.setDisplayType(column.getDisplayType());
            delegate.createColumn(tlColumn);
        }
    }

    @Override
    public void removeColumn(MetadataColumn column) throws SQLException {
        delegate.removeColumn(column);
        if (Boolean.TRUE.equals(column.getMultiLanguage())) {
            MetadataColumn tlColumn = new MetadataColumn();
            tlColumn.setTableName(getMultiLanguageTableName(column.getTableName()));
            tlColumn.setColumnName(column.getColumnName());
            tlColumn.setDisplayType(column.getDisplayType());
            delegate.removeColumn(tlColumn);
        }
    }

    @Override
    public void renameColumn(MetadataColumn column, String newName) throws SQLException {
        delegate.renameColumn(column, newName);
        if (Boolean.TRUE.equals(column.getMultiLanguage())) {
            MetadataColumn tlColumn = new MetadataColumn();
            tlColumn.setTableName(getMultiLanguageTableName(column.getTableName()));
            tlColumn.setColumnName(column.getColumnName());
            tlColumn.setDisplayType(column.getDisplayType());
            delegate.renameColumn(tlColumn, newName);
        }
    }

    @Override
    public void createMultiLanguageTable(MetadataTable table) throws SQLException {
        throw new IllegalArgumentException("不应该调用此方法，此类自动处理多语言表。");
    }

    private static IModelingDriver getDelegate(DataSource source, MetadataMapper mapper) throws SQLException {
        IModelingDriver delegate;
        try (Connection connect = source.getConnection()) {
            String database = connect.getMetaData().getDatabaseProductName();
            switch (database) {
                case "MySQL":
                    delegate = new MysqlModelingDriver(mapper);
                    break;
                default:
                    delegate = new BaseModelingDriver(mapper);
                    break;
            }
        }
        return delegate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        delegate = getDelegate(dataSource, metadataMapper);
    }
}
