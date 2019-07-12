package io.choerodon.hap.modeling.controllers;

import io.choerodon.hap.modeling.dto.MetadataColumn;
import io.choerodon.hap.modeling.dto.MetadataTable;
import io.choerodon.hap.modeling.mapper.MetadataColumnMapper;
import io.choerodon.base.annotation.Permission;
import io.choerodon.base.enums.ResourceType;
import io.choerodon.fnd.util.dto.Code;
import io.choerodon.fnd.util.dto.Lov;
import io.choerodon.fnd.util.service.ICodeService;
import io.choerodon.fnd.util.service.ICodeValueService;
import io.choerodon.fnd.util.service.ILovItemDataSet;
import io.choerodon.fnd.util.service.ILovService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiang.zeng
 * @since 2019/1/13.
 */
@RestController
@RequestMapping("/modeling")
public class ModelingController {

    @Autowired
    private ILovService lovService;
    private ILovItemDataSet lovItemDataSet;
    private ICodeService codeService;
    private ICodeValueService codeValueService;
    @Autowired
    private MetadataColumnMapper metadataColumnMapper;

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/dataset/lovitem/queries")
    public List<Lov> queryLovItemByLovCodes(HttpServletRequest request, @RequestBody List<Lov> lovList) {
        List<Lov> response = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lovList)) {
            for (Lov lov : lovList) {
                lov = lovService.selectByCode(lov.getCode());
                if (lov != null) {
                    lov.setLovItems(lovItemDataSet.selectByLovCode(lov.getCode()));
                    response.add(lov);
                }
            }
        }
        return response;
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/dataset/codevalue/queries")
    public List<Code> queryCodeValueByCodes(HttpServletRequest request, @RequestBody List<Code> codeList) {
        List<Code> response = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(codeList)) {
            for (Code code : codeList) {
                code = codeService.getByCodeName(code.getCode());
                if (code != null) {
                    code.setCodeValues(codeValueService.selectCodeValueByCode(code));
                    response.add(code);
                }
            }
        }
        return response;
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/dataset/metadatacolumn/queries")
    public List<MetadataTable> queryMetadataColumnByTableKey(HttpServletRequest request, @RequestBody List<MetadataTable> tableNames) {
        if (CollectionUtils.isNotEmpty(tableNames)) {
            for (MetadataTable metadataTable : tableNames) {
                List<MetadataColumn> columns = metadataColumnMapper.selectColumnByTableName(metadataTable.getTableName());
                columns.forEach(MetadataColumn::solveDisplayType);
                metadataTable.setColumns(columns);
            }
        }
        return tableNames;
    }
}
