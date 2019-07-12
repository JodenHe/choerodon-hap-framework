package io.choerodon.hap.modeling.service.impl;

import com.github.pagehelper.PageHelper;
import io.choerodon.base.annotation.Dataset;
import io.choerodon.dataset.exception.DatasetException;
import io.choerodon.dataset.service.IDatasetService;
import io.choerodon.hap.function.dto.Resource;
import io.choerodon.hap.function.mapper.ResourceMapper;
import io.choerodon.hap.modeling.dto.Metadata;
import io.choerodon.hap.modeling.dto.MetadataChange;
import io.choerodon.hap.system.dto.DTOStatus;
import io.choerodon.mybatis.service.BaseServiceImpl;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Dataset("MetadataPage")
public class ModelingPageService extends BaseServiceImpl<Resource> implements IDatasetService<Resource> {

    @Autowired
    private ModelingService modelingService;

    @Autowired
    private PageDefineServiceImpl pageDefineService;

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public List<?> queries(Map<String, Object> body, int page, int pageSize, String sortname, boolean isDesc) {
        try {
            Resource resource = new Resource();
            BeanUtils.populate(resource, body);
            resource.setSortname(sortname);
            resource.setSortorder(isDesc ? "desc" : "asc");
            PageHelper.startPage(page, pageSize);
            return resourceMapper.selectPageByOptions(resource);
        } catch (Exception e) {
            throw new DatasetException("dataset.error", e);
        }
    }

    @Override
    public List<Resource> mutations(List<Resource> objs) {
        for (Resource page : objs) {
            switch (page.get__status()) {
                case DTOStatus.ADD:
                    create(page);
                    break;
                case DTOStatus.DELETE:
                    remove(page);
                    break;
                case DTOStatus.UPDATE:
                    updateByPrimaryKey(page);
                    break;
            }
        }
        return objs;
    }

    public void applyUpdate(Resource page) {
        //啥都不用干
    }

    public void rollbackUpdate(Resource page) {
        //啥都不用干
    }

    public void applyCreate(Resource page) {
        //啥都不用干
    }

    public void applyRemove(Resource page) {
        page.setObjectVersionNumber(null);
        deleteByPrimaryKey(page);
    }

    public void rollbackCreate(Resource page) {
        page.setObjectVersionNumber(null);
        deleteByPrimaryKey(page);
    }

    public void rollbackRemove(Resource page) {
        //页面建模功能暂用loginRequire来代替enable
        page.setLoginRequire("Y");
        page.setObjectVersionNumber(null);
        updateByPrimaryKeySelective(page);
    }

    public void update(Resource page, String data) {
        try {
            try {
                pageDefineService.processPage(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            MetadataChange change = new MetadataChange();
            change.setType(MetadataChange.Type.UPDATE_PAGE);
            change.setPage(page);
            change.setData(data);
            Metadata metadata = modelingService.checkLock(change);
            modelingService.addChange(change, metadata);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void create(Resource page) {
        try {
            MetadataChange change = new MetadataChange();
            change.setType(MetadataChange.Type.CREATE_PAGE);
            change.setPage(page);
            Metadata metadata = modelingService.checkLock(change);
            page.setLoginRequire("Y");
            page.setType(Metadata.DATA_TYPE_PAGE);
            insertSelective(page);
            modelingService.addChange(change, metadata);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void remove(Resource page) {
        try {
            MetadataChange change = new MetadataChange();
            change.setType(MetadataChange.Type.DELETE_PAGE);
            change.setPage(page);
            Metadata metadata = modelingService.checkLock(change);
            if (modelingService.addChange(change, metadata)) {
                page.setObjectVersionNumber(null);
                deleteByPrimaryKey(page);
            } else {
                page.setLoginRequire("N");
                page.setObjectVersionNumber(null);
                updateByPrimaryKeySelective(page);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
