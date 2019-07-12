package io.choerodon.hap.modeling.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import io.choerodon.hap.core.util.IDGenerator;
import io.choerodon.base.annotation.Dataset;
import io.choerodon.dataset.service.IDatasetService;
import io.choerodon.hap.modeling.ModelingException;
import io.choerodon.hap.modeling.dto.Metadata;
import io.choerodon.hap.modeling.dto.MetadataChange;
import io.choerodon.hap.modeling.dto.MetadataItem;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.choerodon.web.core.impl.RequestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Dataset("Metadata")
public class ModelingService extends BaseServiceImpl<Metadata> implements IDatasetService<Metadata> {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelingItemService modelingItemService;
    @Autowired
    private ModelingTableService modelingTableService;
    @Autowired
    private ModelingColumnService metadataColumnService;
    @Autowired
    private ModelingPageService modelingPageService;
    @Autowired
    private ModelingRelationService modelingRelationService;


    @Override
    public List<?> queries(Map<String, Object> body, int page, int pageSize, String sortname, boolean isDesc) {
        PageHelper.startPage(page, pageSize);
        Metadata metadata = new Metadata();
        metadata.setName((String) body.get("name"));
        metadata.setStatus((String) body.get("status"));
        return select(metadata, page, pageSize);
    }

    @Override
    public List<Metadata> mutations(List<Metadata> objs) {
        return null;
    }

    public void apply(List<String> ids, boolean commit) {
        try {
            Metadata metadata = new Metadata();
            for (String id : ids) {
                metadata.setId(id);
                metadata = selectByPrimaryKey(metadata);
                if (Metadata.STATUS_COMMITTED.equals(metadata.getStatus())) {
                    continue;
                }
                MetadataItem item = new MetadataItem();
                item.setId(metadata.getChangeId());
                item = modelingItemService.selectByPrimaryKey(item);
                if (item == null) {
                    continue;
                }
                if (commit) {
                    applyTableChanges(item.getData());
                    metadata.setDataId(metadata.getChangeId());
                    metadata.setLockedBy(null);
                    metadata.setChangeId(null);
                    metadata.setStatus(Metadata.STATUS_COMMITTED);
                    updateByPrimaryKey(metadata);
                } else {
                    rollbackTableChanges(item.getData());
                    modelingItemService.deleteByPrimaryKey(item);
                    metadata.setLockedBy(null);
                    metadata.setChangeId(null);
                    metadata.setStatus(Metadata.STATUS_COMMITTED);
                    if (metadata.getDataId() == null) {
                        deleteByPrimaryKey(metadata);
                    } else {
                        updateByPrimaryKey(metadata);
                    }
                }
            }
        } catch (IOException | SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private void rollbackTableChanges(String data) throws IOException, SQLException {
        List<MetadataChange> changes = objectMapper.readValue(data,
                objectMapper.getTypeFactory().constructCollectionType(List.class, MetadataChange.class));
        Collections.reverse(changes);
        for (MetadataChange change : changes) {
            switch (change.getType()) {
                case CREATE_TABLE:
                    modelingTableService.rollbackCreateTable(change.getTable());
                    break;
                case DELETE_TABLE:
                    modelingTableService.rollbackRemoveTable(change.getTable());
                    break;
                case CREATE_COLUMN:
                    metadataColumnService.rollbackCreateColumn(change.getColumn());
                    break;
                case DELETE_COLUMN:
                    metadataColumnService.rollbackRemoveColumn(change.getColumn());
                    break;
                case CREATE_RELATION:
                    modelingRelationService.rollbackCreateRelation(change.getRelation());
                    break;
                case DELETE_RELATION:
                    modelingRelationService.rollbackRemoveRelation(change.getRelation());
                    break;
                case CREATE_PAGE:
                    modelingPageService.rollbackCreate(change.getPage());
                    break;
                case DELETE_PAGE:
                    modelingPageService.rollbackRemove(change.getPage());
                    break;
                case UPDATE_PAGE:
                    modelingPageService.rollbackUpdate(change.getPage());
                    break;
            }
        }
    }

    private void applyTableChanges(String data) throws IOException, SQLException {
        List<MetadataChange> changes = objectMapper.readValue(data,
                objectMapper.getTypeFactory().constructCollectionType(List.class, MetadataChange.class));
        for (MetadataChange change : changes) {
            switch (change.getType()) {
                case DELETE_TABLE:
                    modelingTableService.applyRemoveTable(change.getTable());
                    break;
                case DELETE_COLUMN:
                    metadataColumnService.applyRemoveColumn(change.getColumn());
                    break;
                case CREATE_COLUMN:
                    metadataColumnService.applyCreateColumn(change.getColumn());
                    break;
                case CREATE_TABLE:
                    modelingTableService.applyCreateTable(change.getTable());
                    break;
                case DELETE_RELATION:
                    modelingRelationService.applyRemoveRelation(change.getRelation());
                    break;
                case CREATE_RELATION:
                    modelingRelationService.applyCreateRelation(change.getRelation());
                    break;
                case DELETE_PAGE:
                    modelingPageService.applyRemove(change.getPage());
                    break;
                case CREATE_PAGE:
                    modelingPageService.applyCreate(change.getPage());
                    break;
                case UPDATE_PAGE:
                    modelingPageService.applyUpdate(change.getPage());
                    break;
            }
        }
    }

    public Metadata checkLock(MetadataChange change) {
        Metadata metadata = new Metadata();
        switch (change.getType()) {
            case CREATE_RELATION:
            case DELETE_RELATION:
                metadata.setDataType(Metadata.DATA_TYPE_RELATION);
                metadata.setName(change.getRelation().getUniqueName());
                break;
            case DELETE_TABLE:
            case CREATE_COLUMN:
            case CREATE_TABLE:
            case DELETE_COLUMN:
                metadata.setDataType(Metadata.DATA_TYPE_TABLE);
                metadata.setName(change.getTableName());
                break;
            case UPDATE_PAGE:
            case CREATE_PAGE:
            case DELETE_PAGE:
                metadata.setDataType(Metadata.DATA_TYPE_PAGE);
                metadata.setName(change.getPage().getUrl());
                break;
        }
        List<Metadata> result = select(metadata, 1, 10);
        metadata = result.isEmpty() ? null : result.get(0);
        if (metadata != null && metadata.getLockedBy() != null &&
                !metadata.getLockedBy().equals(RequestHelper.getCurrentRequest().getUserName())) {
            throw new ModelingException("lock check exception", change.getTableName(), metadata.getLockedBy());
        }
        return metadata;
    }

    public void importHistory(List<MetadataChange> changes) throws SQLException {
        for (MetadataChange change : changes) {
            switch (change.getType()) {
                case DELETE_COLUMN:
                    metadataColumnService.removeColumn(change.getColumn());
                    break;
                case CREATE_TABLE:
                    modelingTableService.createTable(change.getTable());
                    break;
                case CREATE_COLUMN:
                    metadataColumnService.createColumn(change.getColumn());
                    break;
                case DELETE_TABLE:
                    modelingTableService.removeTable(change.getTable());
                    break;
                case CREATE_RELATION:
                    modelingRelationService.createRelation(change.getRelation());
                    break;
                case DELETE_RELATION:
                    modelingRelationService.removeRelation(change.getRelation());
                    break;
                case DELETE_PAGE:
                    modelingPageService.remove(change.getPage());
                    break;
                case CREATE_PAGE:
                    modelingPageService.create(change.getPage());
                    break;
                case UPDATE_PAGE:
                    modelingPageService.update(change.getPage(), change.getData());
                    break;
            }
        }
    }

    public List<MetadataChange> export(String[] ids) throws IOException {
        List<MetadataChange> result = new LinkedList<>();
        for (String id : ids) {
            Metadata metadata = new Metadata();
            metadata.setId(id);
            metadata = selectByPrimaryKey(metadata);
            if (metadata == null) {
                continue;
            }
            if (Metadata.STATUS_COMMITTED.equals(metadata.getStatus())) {
                continue;
            }
            MetadataItem metadataItem = new MetadataItem();
            if (metadata.getChangeId() == null) {
                continue;
            }
            metadataItem.setId(metadata.getChangeId());
            metadataItem = modelingItemService.selectByPrimaryKey(metadataItem);
            if (metadataItem == null) {
                continue;
            }
            metadata.setChange(metadataItem);
            List<MetadataChange> changes = objectMapper.readValue(metadataItem.getData(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, MetadataChange.class));
            result.addAll(changes);
        }
        return result;
    }

    /**
     * 添加数据表修改记录.
     *
     * @param change   元数据更改
     * @param metadata 元数据
     * @return 当删除对象的时候如果前面有添加对象会进行合并返回True，其他情况返回False
     * @throws IOException
     */
    public boolean addChange(MetadataChange change, Metadata metadata) throws IOException {
        boolean deleteMerged = false;
        MetadataItem metadataItem = null;
        if (metadata != null) {
            if (Metadata.STATUS_CHECKOUT.equals(metadata.getStatus())) {
                metadataItem = new MetadataItem();
                metadataItem.setId(metadata.getChangeId());
                metadataItem = modelingItemService.selectByPrimaryKey(metadataItem);
            }
        } else {
            metadata = new Metadata();
            switch (change.getType()) {
                case CREATE_RELATION:
                case DELETE_RELATION:
                    metadata.setDataType(Metadata.DATA_TYPE_RELATION);
                    metadata.setName(change.getRelation().getUniqueName());
                    break;
                case DELETE_TABLE:
                case CREATE_COLUMN:
                case CREATE_TABLE:
                case DELETE_COLUMN:
                    metadata.setDataType(Metadata.DATA_TYPE_TABLE);
                    metadata.setName(change.getTableName());
                    break;
                case UPDATE_PAGE:
                case CREATE_PAGE:
                case DELETE_PAGE:
                    metadata.setDataType(Metadata.DATA_TYPE_PAGE);
                    metadata.setName(change.getPage().getUrl());
                    break;
            }
            metadata.setStatus(Metadata.STATUS_CHECKOUT);
        }
        if (metadataItem == null) {
            metadataItem = new MetadataItem();
            metadataItem.setData(objectMapper.writeValueAsString(Collections.singleton(change)));
            metadataItem.setDataVersion(1L);
        } else {
            List<MetadataChange> changes = objectMapper.readValue(metadataItem.getData(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, MetadataChange.class));
            Iterator<MetadataChange> changeIterator;
            switch (change.getType()) {
                case DELETE_COLUMN:
                    changeIterator = changes.iterator();
                    while (changeIterator.hasNext()) {
                        MetadataChange current = changeIterator.next();
                        if (current.getType().equals(MetadataChange.Type.CREATE_COLUMN) &&
                                current.getColumn().getColumnName().equals(change.getColumn().getColumnName())) {
                            changeIterator.remove();
                            deleteMerged = true;
                            break;
                        }
                    }
                    break;
                case DELETE_TABLE:
                    changeIterator = changes.iterator();
                    while (changeIterator.hasNext()) {
                        MetadataChange current = changeIterator.next();
                        if (current.getType().equals(MetadataChange.Type.CREATE_TABLE)) {
                            changeIterator.remove();
                            deleteMerged = true;
                            break;
                        }
                    }
                    break;
                case DELETE_RELATION:
                    changeIterator = changes.iterator();
                    while (changeIterator.hasNext()) {
                        MetadataChange current = changeIterator.next();
                        if (current.getType().equals(MetadataChange.Type.CREATE_RELATION)) {
                            changeIterator.remove();
                            deleteMerged = true;
                            break;
                        }
                    }
                    break;
                case DELETE_PAGE:
                    changeIterator = changes.iterator();
                    while (changeIterator.hasNext()) {
                        MetadataChange current = changeIterator.next();
                        if (current.getType().equals(MetadataChange.Type.CREATE_PAGE) || current.getType().equals(MetadataChange.Type.UPDATE_PAGE)) {
                            changeIterator.remove();
                            deleteMerged = true;
                            break;
                        }
                    }
                    break;
                case UPDATE_PAGE:
                    //只保留一条更新记录
                    changeIterator = changes.iterator();
                    while (changeIterator.hasNext()) {
                        MetadataChange current = changeIterator.next();
                        if (current.getType().equals(MetadataChange.Type.UPDATE_PAGE)) {
                            changeIterator.remove();
                            break;
                        }
                    }
                    break;
            }
            if (!deleteMerged) {
                changes.add(change);
            }
            metadataItem.setData(objectMapper.writeValueAsString(changes));
            metadataItem.setDataVersion(metadataItem.getDataVersion() + 1);
        }
        String changeId = metadataItem.getId();
        if (changeId == null) {
            changeId = IDGenerator.getInstance().generate();
        }
        if (metadata.getId() == null) {
            metadata.setId(IDGenerator.getInstance().generate());
            metadata.setDataId(null);
            metadata.setChangeId(changeId);
            metadata.setLockedBy(RequestHelper.getCurrentRequest().getUserName());
            insertSelective(metadata);
        } else {
            metadata.setChangeId(changeId);
            metadata.setStatus(Metadata.STATUS_CHECKOUT);
            metadata.setLockedBy(RequestHelper.getCurrentRequest().getUserName());
            updateByPrimaryKeySelective(metadata);
        }
        if (metadataItem.getId() == null) {
            metadataItem.setId(changeId);
            metadataItem.setMetadataId(metadata.getId());
            modelingItemService.insertSelective(metadataItem);
        } else {
            modelingItemService.updateByPrimaryKey(metadataItem);
        }
        return deleteMerged;
    }

}
