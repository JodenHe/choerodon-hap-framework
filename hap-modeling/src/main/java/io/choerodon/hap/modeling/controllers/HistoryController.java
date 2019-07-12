package io.choerodon.hap.modeling.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.hap.modeling.dto.MetadataChange;
import io.choerodon.hap.modeling.service.impl.ModelingService;
import io.choerodon.base.annotation.Permission;
import io.choerodon.base.enums.ResourceType;
import io.choerodon.web.controller.BaseController;
import io.choerodon.web.dto.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/metadata/history")
public class HistoryController extends BaseController {
    private final static Logger LOGGER = LoggerFactory.getLogger(HistoryController.class);
    @Autowired
    private ModelingService modelingService;
    @Autowired
    private ObjectMapper objectMapper;

    @Permission(type = ResourceType.SITE)
    @PostMapping("/apply")
    public ResponseData apply(@RequestBody List<String> ids, @RequestParam(defaultValue = "true") boolean commit) {
        modelingService.apply(ids, commit);
        return new ResponseData(ids);
    }

    @Permission(type = ResourceType.SITE)
    @GetMapping("/export/history.json")
    public ResponseEntity<List<MetadataChange>> export(@RequestParam String ids) throws IOException {
        List<MetadataChange> changes = modelingService.export(ids.split(","));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=history-%d.json", System.currentTimeMillis()))
                .body(changes);
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping("/import")
    public void importHistory(@RequestParam MultipartFile file) throws IOException, SQLException {
        String json = new String(file.getBytes());
        List changes = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, MetadataChange.class));
        modelingService.importHistory(changes);
    }

}
