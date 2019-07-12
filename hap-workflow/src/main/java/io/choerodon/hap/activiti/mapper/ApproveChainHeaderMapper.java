package io.choerodon.hap.activiti.mapper;

import io.choerodon.hap.activiti.dto.ApproveChainHeader;
import io.choerodon.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

public interface ApproveChainHeaderMapper extends Mapper<ApproveChainHeader> {

    ApproveChainHeader selectByUserTask(@Param("processKey") String processKey,
                                        @Param("usertaskId") String usertaskId);
}