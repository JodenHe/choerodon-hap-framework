package io.choerodon.hap.activiti.service.impl;

import io.choerodon.hap.activiti.dto.ApproveChainHeader;
import io.choerodon.hap.activiti.dto.ApproveChainLine;
import io.choerodon.hap.activiti.mapper.ApproveChainLineMapper;
import io.choerodon.hap.activiti.service.IApproveChainLineService;
import io.choerodon.base.annotation.Dataset;
import io.choerodon.dataset.exception.DatasetException;
import io.choerodon.dataset.service.IDatasetService;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.choerodon.web.core.IRequest;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.data.GroupDataManager;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Service
@Dataset("ApproveChainLine")
@Transactional
public class ApproveChainLineServiceImpl extends BaseServiceImpl<ApproveChainLine> implements IApproveChainLineService, IDatasetService<ApproveChainLine> {

    @Autowired
    private ApproveChainLineMapper lineMapper;

    @Autowired
    private GroupDataManager groupDataManager;

    @Override
    public List<ApproveChainLine> selectByHeaderId(IRequest iRequest, Long headerId) {
        List<ApproveChainLine> list = lineMapper.selectByHeaderId(headerId);
        for (ApproveChainLine line : list) {
            if (StringUtils.isEmpty(line.getAssignee()) && StringUtils.isNotEmpty(line.getAssignGroup())) {
                Group group = groupDataManager.findById(line.getAssignGroup());
                if (group != null) {
                    line.setAssignGroupName(group.getName());
                }
            }
        }
        return list;
    }

    @Override
    public List<ApproveChainLine> selectByNodeId(IRequest iRequest, String key, String nodeId) {
        List<ApproveChainLine> list = lineMapper.selectByNodeId(key, nodeId);
        for (ApproveChainLine line : list) {
            if (StringUtils.isEmpty(line.getAssignee()) && StringUtils.isNotEmpty(line.getAssignGroup())) {
                Group group = groupDataManager.findById(line.getAssignGroup());
                if (group != null) {
                    line.setAssignGroupName(group.getName());
                }
            }
        }
        return list;
    }

    @Override
    public List<?> queries(Map<String, Object> body, int page, int pageSize, String sortname, boolean isDesc) {
        ApproveChainHeader dto = new ApproveChainHeader();
        try {
            BeanUtils.populate(dto, body);
            return self().selectByNodeId(null, dto.getProcessKey(), dto.getUsertaskId());

        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new DatasetException("dataset.error", e);
        }
    }

    @Override
    public List<ApproveChainLine> mutations(List<ApproveChainLine> objs) {
        self().batchUpdate(objs);
        return objs;
    }
}