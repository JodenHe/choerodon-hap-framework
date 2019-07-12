package io.choerodon.hap.modeling.controllers;

import io.choerodon.hap.modeling.dto.MetadataColumn;
import io.choerodon.hap.modeling.dto.MetadataTable;
import io.choerodon.hap.modeling.mapper.MetadataTableMapper;
import io.choerodon.hap.modeling.service.IMetadataTableService;
import io.choerodon.hap.modeling.service.impl.ModelingRelationService;
import io.choerodon.base.annotation.Permission;
import io.choerodon.base.enums.ResourceType;
import io.choerodon.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/metadata")
public class MetadataController extends BaseController {
    @Autowired
    private MetadataTableMapper metadataTableMapper;
    @Autowired
    private IMetadataTableService metadataTableService;
    @Autowired
    private ModelingRelationService modelingRelationService;

    @Permission(type = ResourceType.SITE)
    @PostMapping("/selectTable")
    public List<MetadataTable> selectTable() {
        return metadataTableMapper.selectAllTables();
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping("/selectColumn")
    public List<MetadataColumn> selectColumn(@RequestParam("table") String table) {
        return metadataTableService.queryTable(table).getColumns();
    }

    @Permission(type = ResourceType.SITE)
    @GetMapping("/selectRelationTable")
    public List<Map<String, Object>> selectRelationTable(@RequestParam("table") String table) {
        return modelingRelationService.selectRelationTable(table);
    }
}
