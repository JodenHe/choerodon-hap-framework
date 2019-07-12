package io.choerodon.hap.activiti.mapper;

import io.choerodon.hap.activiti.dto.HiIdentitylink;
import io.choerodon.mybatis.common.Mapper;

import java.util.Map;

public interface HiIdentitylinkMapper extends Mapper<HiIdentitylink> {

    int updateReadFlag(HiIdentitylink hiIdentitylink);

    int insertCarbonCopy(Map<String, String> params);
}