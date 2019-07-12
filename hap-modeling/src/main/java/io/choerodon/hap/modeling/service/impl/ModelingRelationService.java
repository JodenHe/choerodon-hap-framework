package io.choerodon.hap.modeling.service.impl;

import com.github.pagehelper.PageHelper;
import io.choerodon.hap.core.util.IDGenerator;
import io.choerodon.base.annotation.Dataset;
import io.choerodon.dataset.service.IDatasetService;
import io.choerodon.hap.modeling.dto.Metadata;
import io.choerodon.hap.modeling.dto.MetadataChange;
import io.choerodon.hap.modeling.dto.MetadataRelation;
import io.choerodon.hap.modeling.mapper.MetadataRelationMapper;
import io.choerodon.hap.system.dto.DTOStatus;
import io.choerodon.mybatis.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Dataset("MetadataRelation")
public class ModelingRelationService extends BaseServiceImpl<MetadataRelation> implements IDatasetService<MetadataRelation> {

    @Autowired
    private ModelingService modelingService;

    @Autowired
    private MetadataRelationMapper metadataRelationMapper;

    public List<Map<String, Object>> selectRelationTable(String table) {
        return metadataRelationMapper.selectRelationTable(table);
    }

    @Override
    public List<?> queries(Map<String, Object> body, int page, int pageSize, String sortname, boolean isDesc) {
        PageHelper.startPage(page, pageSize);
        return metadataRelationMapper.selectMetadataRelations();
    }

    @Override
    public List<MetadataRelation> mutations(List<MetadataRelation> objs) {
        for (MetadataRelation relation : objs) {
            switch (relation.get__status()) {
                case DTOStatus.ADD:
                    createRelation(relation);
                    break;
                case DTOStatus.DELETE:
                    removeRelation(relation);
                    break;
            }
        }
        return objs;
    }

    public void applyCreateRelation(MetadataRelation relation) {
        //啥都不用干
    }

    public void applyRemoveRelation(MetadataRelation relation) {
        relation.setObjectVersionNumber(null);
        deleteByPrimaryKey(relation);
    }

    public void rollbackCreateRelation(MetadataRelation relation) {
        relation.setObjectVersionNumber(null);
        deleteByPrimaryKey(relation);
    }

    public void rollbackRemoveRelation(MetadataRelation relation) {
        relation.setObjectVersionNumber(null);
        relation.setEnable(true);
        updateByPrimaryKeySelective(relation);
    }

    private static String solveUniqueName(String masterTable, String relationTable) {
        return masterTable.compareTo(relationTable) > 0 ? masterTable + "-" + relationTable : relationTable + "-" + masterTable;
    }

    public void createRelation(MetadataRelation relation) {
        try {
            MetadataChange change = new MetadataChange();
            change.setType(MetadataChange.Type.CREATE_RELATION);
            change.setRelation(relation);
            Metadata metadata = modelingService.checkLock(change);
            relation.setId(IDGenerator.getInstance().generate());
            relation.setEnable(true);
            relation.setUniqueName(solveUniqueName(relation.getMasterTable(), relation.getRelationTable()));
            insertSelective(relation);
            modelingService.addChange(change, metadata);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void removeRelation(MetadataRelation relation) {
        try {
            MetadataChange change = new MetadataChange();
            change.setType(MetadataChange.Type.DELETE_RELATION);
            change.setRelation(relation);
            Metadata metadata = modelingService.checkLock(change);
            if (modelingService.addChange(change, metadata)) {
                relation.setObjectVersionNumber(null);
                deleteByPrimaryKey(relation);
            } else {
                relation.setEnable(false);
                relation.setObjectVersionNumber(null);
                updateByPrimaryKeySelective(relation);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
