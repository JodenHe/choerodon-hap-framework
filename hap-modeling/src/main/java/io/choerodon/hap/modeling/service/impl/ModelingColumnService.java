package io.choerodon.hap.modeling.service.impl;

import com.github.pagehelper.PageHelper;
import io.choerodon.hap.core.util.IDGenerator;
import io.choerodon.hap.modeling.dto.Metadata;
import io.choerodon.hap.modeling.dto.MetadataChange;
import io.choerodon.hap.modeling.dto.MetadataColumn;
import io.choerodon.hap.modeling.mapper.MetadataColumnMapper;
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
@Dataset("MetadataColumn")
public class ModelingColumnService extends BaseServiceImpl<MetadataColumn> implements IDatasetService<MetadataColumn> {
    @Autowired
    private IModelingDriver modelingDriver;
    @Autowired
    private ModelingService modelingService;
    @Autowired
    private MetadataColumnMapper metadataColumnMapper;

    private static final Criteria UPDATE_DESCRIPTION_CRITERIA = new Criteria();

    static {
        UPDATE_DESCRIPTION_CRITERIA.update("description");
    }

    @Override
    public List<?> queries(Map<String, Object> body, int page, int pageSize, String sortname, boolean isDesc) {
        String tableName = (String) body.get("tableName");
        PageHelper.startPage(page, pageSize);
        List<MetadataColumn> columns = metadataColumnMapper.selectColumnByTableName(tableName);
        columns.forEach(MetadataColumn::solveDisplayType);
        return columns;
    }

    /**
     * 从DisplayTypeName转换为TypeName和ColumnSize
     *
     * @param column 列
     */
    public static void solveTypeName(MetadataColumn column) {
        String[] type = column.getDisplayType().split("\\(");
        column.setTypeName(type[0]);
        if (type.length == 2) {
            column.setColumnSize(Integer.valueOf(type[1].substring(0, type[1].length() - 1)));
        }
    }

    public void insertNewTableColumn(String tableName) {
        MetadataColumn column = new MetadataColumn();
        column.setTableName(tableName);
        column.setNullable(false);
        column.setPrimaryKey(true);
        column.setMultiLanguage(false);
        column.setColumnSize(32);
        column.setTypeName("CHAR");

        column.setColumnName("UUID");
        column.setDescription("UUID主鍵");
        column.setId(IDGenerator.getInstance().generate());
        insertSelective(column);

        column.setPrimaryKey(false);
        column.setTypeName("BIGINT");
        column.setColumnSize(null);

        column.setColumnName("CREATED_BY");
        column.setDescription("创建人");
        column.setId(IDGenerator.getInstance().generate());
        insertSelective(column);

        column.setColumnName("LAST_UPDATED_BY");
        column.setDescription("更新主体");
        column.setId(IDGenerator.getInstance().generate());
        insertSelective(column);

        column.setTypeName("DATETIME");

        column.setColumnName("CREATION_DATE");
        column.setDescription("创建时间");
        column.setId(IDGenerator.getInstance().generate());
        insertSelective(column);

        column.setColumnName("LAST_UPDATE_DATE");
        column.setDescription("更新时间");
        column.setId(IDGenerator.getInstance().generate());
        insertSelective(column);
    }

    @Override
    public List<MetadataColumn> mutations(List<MetadataColumn> objs) {
        try {
            for (MetadataColumn column : objs) {
                switch (column.get__status()) {
                    case DTOStatus.ADD:
                        createColumn(column);
                        break;
                    case DTOStatus.DELETE:
                        removeColumn(column);
                        break;
                    case DTOStatus.UPDATE:
                        updateByPrimaryKeyOptions(column, UPDATE_DESCRIPTION_CRITERIA);
                        break;
                }
            }
            return objs;
        } catch (Exception e) {
            throw new DatasetException(e.getMessage());
        }
    }

    public void rollbackCreateColumn(MetadataColumn column) throws SQLException {
        modelingDriver.removeColumn(column);
        deleteByPrimaryKey(column);
    }

    public void rollbackRemoveColumn(MetadataColumn column) throws SQLException {
        String columnName = column.getColumnName();
        column.setColumnName(BaseModelingDriver.COLUMN_DELETE_RESTORE_PREFIX + columnName);
        modelingDriver.renameColumn(column, columnName);
        MetadataColumn update = selectByPrimaryKey(column);
        update.setColumnName(columnName);
        updateByPrimaryKeySelective(update);
    }

    public void applyCreateColumn(MetadataColumn column) throws SQLException {
        //啥都不用干
    }

    public void applyRemoveColumn(MetadataColumn column) throws SQLException {
        String columnName = column.getColumnName();
        column.setColumnName(BaseModelingDriver.COLUMN_DELETE_RESTORE_PREFIX + columnName);
        modelingDriver.removeColumn(column);
        deleteByPrimaryKey(column);
    }

    public void createColumn(MetadataColumn column) throws SQLException {
        column.setTableName(column.getTableName().toLowerCase());
        column.setColumnName(column.getColumnName().toUpperCase());
        try {
            if (!column.getColumnName().startsWith("X_")) {
                column.setColumnName("X_" + column.getColumnName());
            }
            MetadataChange change = new MetadataChange();
            change.setType(MetadataChange.Type.CREATE_COLUMN);
            change.setColumn(column);
            Metadata metadata = modelingService.checkLock(change);
            modelingDriver.createColumn(column);
            column.setId(IDGenerator.getInstance().generate());
            modelingService.addChange(change, metadata);
            solveTypeName(column);
            insertSelective(column);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void removeColumn(MetadataColumn column) throws SQLException {
        column.setTableName(column.getTableName().toLowerCase());
        column.setColumnName(column.getColumnName().toUpperCase());
        if (!column.getColumnName().startsWith("X_")) {
            throw new IllegalArgumentException("modify column must starts with x_");
        }
        try {
            MetadataChange change = new MetadataChange();
            change.setType(MetadataChange.Type.DELETE_COLUMN);
            change.setColumn(column);
            Metadata metadata = modelingService.checkLock(change);
            if (modelingService.addChange(change, metadata)) {
                modelingDriver.removeColumn(column);
                deleteByPrimaryKey(column);
            } else {
                modelingDriver.renameColumn(column, BaseModelingDriver.COLUMN_DELETE_RESTORE_PREFIX + column.getColumnName());
                MetadataColumn update = new MetadataColumn();
                update.setId(column.getId());
                update.setColumnName(BaseModelingDriver.COLUMN_DELETE_RESTORE_PREFIX + column.getColumnName());
                updateByPrimaryKeySelective(update);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
