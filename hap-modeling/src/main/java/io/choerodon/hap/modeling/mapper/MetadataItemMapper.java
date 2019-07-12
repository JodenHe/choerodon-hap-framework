package io.choerodon.hap.modeling.mapper;

import io.choerodon.hap.modeling.dto.MetadataItem;
import io.choerodon.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Creator: ChangpingShi0213@gmail.com
 * Date:  16:04 2018/11/17
 * Description:
 */
public interface MetadataItemMapper extends Mapper<MetadataItem> {
    List<Map> selectByMetadataIdOrderByUpdateDate(@Param("metadataId") String metadataId);
}