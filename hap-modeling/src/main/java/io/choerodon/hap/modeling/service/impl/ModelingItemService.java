package io.choerodon.hap.modeling.service.impl;

import com.github.pagehelper.PageHelper;
import io.choerodon.base.annotation.Dataset;
import io.choerodon.dataset.service.IDatasetService;
import io.choerodon.hap.modeling.dto.MetadataItem;
import io.choerodon.hap.modeling.mapper.MetadataItemMapper;
import io.choerodon.mybatis.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Dataset("MetadataItem")
public class ModelingItemService extends BaseServiceImpl<MetadataItem> implements IDatasetService<MetadataItem> {
    @Autowired
    private MetadataItemMapper metadataItemMapper;

    @Override
    public List<?> queries(Map<String, Object> body, int page, int pageSize, String sortname, boolean isDesc) {
        PageHelper.startPage(page, pageSize);
        return metadataItemMapper.selectByMetadataIdOrderByUpdateDate((String) body.get("metadataId"));
    }

    @Override
    public List<MetadataItem> mutations(List<MetadataItem> objs) {
        return null;
    }
}
