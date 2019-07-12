package io.choerodon.hap.activiti.exception.mapper;

import io.choerodon.hap.activiti.exception.dto.ActiviException;
import io.choerodon.mybatis.common.Mapper;

import java.util.List;

public interface ActiviExceptionMapper extends Mapper<ActiviException> {

    List<ActiviException> selectAllException(ActiviException exception);

}