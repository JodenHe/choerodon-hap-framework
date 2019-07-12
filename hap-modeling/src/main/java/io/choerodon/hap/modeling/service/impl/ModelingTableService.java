package io.choerodon.hap.modeling.service.impl;

import com.github.pagehelper.PageHelper;
import io.choerodon.hap.core.util.IDGenerator;
import io.choerodon.hap.modeling.dto.Metadata;
import io.choerodon.hap.modeling.dto.MetadataChange;
import io.choerodon.hap.modeling.dto.MetadataColumn;
import io.choerodon.hap.modeling.dto.MetadataTable;
import io.choerodon.hap.modeling.mapper.MetadataColumnMapper;
import io.choerodon.hap.modeling.mapper.MetadataTableMapper;
import io.choerodon.hap.modeling.service.IModelingDriver;
import io.choerodon.hap.system.dto.DTOStatus;
import io.choerodon.base.annotation.Dataset;
import io.choerodon.dataset.exception.DatasetException;
import io.choerodon.dataset.service.IDatasetService;
import io.choerodon.mybatis.entity.Criteria;
import io.choerodon.mybatis.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
@Dataset("MetadataTable")
public class ModelingTableService extends BaseServiceImpl<MetadataTable> implements IDatasetService<MetadataTable> {
    @Autowired
    private IModelingDriver modelingDriver;
    @Autowired
    private ModelingService modelingService;
    @Autowired
    private MetadataTableMapper metadataTableMapper;
    @Autowired
    private MetadataColumnMapper metadataColumnMapper;
    @Autowired
    private ModelingColumnService metadataColumnService;

    private static final Criteria UPDATE_DESCRIPTION_CRITERIA = new Criteria();

    static {
        UPDATE_DESCRIPTION_CRITERIA.update("description");
    }

    @Override
    public List<?> queries(Map<String, Object> body, int page, int pageSize, String sortname, boolean isDesc) {
        PageHelper.startPage(page, pageSize);
        return metadataTableMapper.selectMetadataTables((String) body.get("tableName"));
    }

    @Override
    public List<MetadataTable> mutations(List<MetadataTable> objs) {
        try {
            for (MetadataTable table : objs) {
                switch (table.get__status()) {
                    case DTOStatus.ADD:
                        createTable(table);
                        break;
                    case DTOStatus.DELETE:
                        removeTable(table);
                        break;
                    case DTOStatus.UPDATE:
                        updateByPrimaryKeyOptions(table, UPDATE_DESCRIPTION_CRITERIA);
                        break;
                }
            }
            return objs;
        } catch (Exception e) {
            throw new DatasetException(e.getMessage());
        }
    }

    public void rollbackCreateTable(MetadataTable table) throws SQLException {
        modelingDriver.removeTable(table);
        deleteByPrimaryKey(table);
        MetadataColumn column = new MetadataColumn();
        column.setTableName(table.getTableName());
        metadataColumnMapper.delete(column);
    }

    public void rollbackRemoveTable(MetadataTable table) throws SQLException {
        String tableName = table.getTableName();
        table.setTableName(BaseModelingDriver.TABLE_DELETE_RESTORE_PREFIX + tableName);
        modelingDriver.renameTable(table, tableName);
        table.setTableName(tableName);
        updateByPrimaryKeySelective(table);
    }

    public void applyCreateTable(MetadataTable table) throws SQLException {
        //啥都不用干
    }

    public void applyRemoveTable(MetadataTable table) throws SQLException {
        String tableName = table.getTableName();
        table.setTableName(BaseModelingDriver.TABLE_DELETE_RESTORE_PREFIX + tableName);
        modelingDriver.removeTable(table);
        deleteByPrimaryKey(table);
        MetadataColumn column = new MetadataColumn();
        column.setTableName(tableName);
        metadataColumnMapper.delete(column);
    }

    public void createTable(MetadataTable table) throws SQLException {
        if (Boolean.TRUE.equals(table.getMultiLanguage()) && !table.getTableName().endsWith("_b")) {
            table.setTableName(table.getTableName() + "_b");
        }
        table.setTableName(table.getTableName().toLowerCase());
        try {
            if (!table.getTableName().startsWith("x_")) {
                table.setTableName("x_" + table.getTableName());
            }
            MetadataChange change = new MetadataChange();
            change.setType(MetadataChange.Type.CREATE_TABLE);
            change.setTable(table);
            Metadata metadata = modelingService.checkLock(change);
            modelingDriver.createTable(table);
            table.setId(IDGenerator.getInstance().generate());
            modelingService.addChange(change, metadata);
            insertSelective(table);
            metadataColumnService.insertNewTableColumn(table.getTableName());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void removeTable(MetadataTable table) throws SQLException {
        table.setTableName(table.getTableName().toLowerCase());
        if (!table.getTableName().startsWith("x_")) {
            throw new IllegalArgumentException("modify table must starts with x_");
        }
        try {
            MetadataChange change = new MetadataChange();
            change.setType(MetadataChange.Type.DELETE_TABLE);
            change.setTable(table);
            Metadata metadata = modelingService.checkLock(change);
            if (modelingService.addChange(change, metadata)) {
                modelingDriver.removeTable(table);
                deleteByPrimaryKey(table);
            } else {
                modelingDriver.renameTable(table, BaseModelingDriver.TABLE_DELETE_RESTORE_PREFIX + table.getTableName());
                table.setTableName(BaseModelingDriver.TABLE_DELETE_RESTORE_PREFIX + table.getTableName());
                updateByPrimaryKeySelective(table);
            }

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
