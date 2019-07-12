package io.choerodon.hap.security.permission.service.impl;

import io.choerodon.hap.security.permission.dto.DataPermissionTable;
import io.choerodon.hap.security.permission.dto.DataPermissionTableRule;
import io.choerodon.hap.security.permission.mapper.DataPermissionTableRuleMapper;
import io.choerodon.hap.security.permission.mapper.DatasetMapper;
import io.choerodon.hap.security.permission.service.IDataPermissionTableService;
import io.choerodon.base.annotation.Dataset;
import io.choerodon.dataset.exception.DatasetException;
import io.choerodon.dataset.service.IDatasetService;
import io.choerodon.message.IMessagePublisher;
import io.choerodon.mybatis.entity.BaseDTO;
import io.choerodon.mybatis.service.BaseServiceImpl;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author jialong.zuo@hand-china.com
 */
@Service
@Dataset("PermissionTable")
public class DataPermissionTableServiceImpl extends BaseServiceImpl<DataPermissionTable> implements IDataPermissionTableService, IDatasetService<DataPermissionTable> {

    @Autowired
    DataPermissionTableRuleMapper dataPermissionTableRuleMapper;

    @Autowired
    IMessagePublisher iMessagePublisher;
    @Autowired
    private DatasetMapper datasetMapper;

    @Override
    public void removeTableWithRule(List<DataPermissionTable> dataMaskTables) {
        batchDelete(dataMaskTables);
        dataMaskTables.forEach(v -> {
            DataPermissionTableRule rule = new DataPermissionTableRule();
            rule.setTableId(v.getTableId());
            dataPermissionTableRuleMapper.delete(rule);
            iMessagePublisher.publish("dataPermission.tableRemove", v);
        });
    }
    public void deletePostFilter(DataPermissionTable table) {
        DataPermissionTableRule rule = new DataPermissionTableRule();
        rule.setTableId(table.getTableId());
        dataPermissionTableRuleMapper.delete(rule);
        iMessagePublisher.publish("dataPermission.tableRemove", table);
    }

    @Override
    public List<?> queries(Map<String, Object> body, int page, int pageSize, String sortname, boolean isDesc) {
        try {
            DataPermissionTable example = new DataPermissionTable();
            BeanUtils.populate(example, body);
            return select(example, page, pageSize);
        }catch (Exception e){
            throw new DatasetException("dataset.error", e);
        }
    }

    @Override
    public List<DataPermissionTable> mutations(List<DataPermissionTable> objs) {
        batchUpdate(objs);
        for (DataPermissionTable table : objs){
            switch (table.get__status()){
                case BaseDTO.STATUS_DELETE:
                    deletePostFilter(table);
                    break;
            }
        }
        return objs;
    }
}