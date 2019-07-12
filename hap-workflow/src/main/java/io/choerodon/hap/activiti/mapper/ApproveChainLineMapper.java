package io.choerodon.hap.activiti.mapper;

import io.choerodon.hap.activiti.dto.ApproveChainLine;
import io.choerodon.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApproveChainLineMapper extends Mapper<ApproveChainLine> {

    List<ApproveChainLine> selectByHeaderId(Long headerId);

    List<ApproveChainLine> selectByNodeId(@Param("processKey") String processKey, @Param("nodeId") String nodeId);
}