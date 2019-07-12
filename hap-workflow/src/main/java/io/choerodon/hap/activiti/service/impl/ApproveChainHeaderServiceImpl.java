package io.choerodon.hap.activiti.service.impl;

import io.choerodon.hap.activiti.dto.ApproveChainHeader;
import io.choerodon.hap.activiti.mapper.ApproveChainHeaderMapper;
import io.choerodon.hap.activiti.mapper.ApproveChainLineMapper;
import io.choerodon.hap.activiti.service.IApproveChainHeaderService;
import io.choerodon.hap.core.util.CommonUtils;
import io.choerodon.mybatis.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ApproveChainHeaderServiceImpl extends BaseServiceImpl<ApproveChainHeader>
        implements IApproveChainHeaderService {

    @Autowired
    private ApproveChainHeaderMapper headerMapper;

    @Autowired
    private ApproveChainLineMapper lineMapper;

    @Override
    public ApproveChainHeader selectByUserTask(String processDefinitionId, String userTaskId) {
        return headerMapper.selectByUserTask(processDefinitionId, userTaskId);
    }

    @Override
    public List<ApproveChainHeader> updateHeadLine(List<ApproveChainHeader> dto) {

        for (ApproveChainHeader header : dto) {
            if (header.getApproveChainId() == null) {
                insertSelective(header);
            }
            CommonUtils.foreach(header.getLines(), (line) -> {
                line.setApproveChainId(header.getApproveChainId());
                if (line.getApproveChainLineId() == null) {
                    lineMapper.insertSelective(line);
                } else {
                    int count = lineMapper.updateByPrimaryKeySelective(line);
                    checkOvn(count, line);
                }
            });
        }

        return dto;
    }
}