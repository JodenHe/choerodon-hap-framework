package io.choerodon.hap.modeling.controllers;

import io.choerodon.hap.modeling.service.IPageDefineService;
import io.choerodon.base.annotation.Permission;
import io.choerodon.base.enums.ResourceType;
import io.choerodon.web.controller.BaseController;
import io.choerodon.web.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class PageDefineController extends BaseController {
    @Autowired
    private IPageDefineService pageDefineService;

    @Permission(type = ResourceType.SITE)
    @GetMapping("/PageDefine/{name}")
    public String query(@PathVariable String name) {
        return pageDefineService.query(name);
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping("/PageDefine/{name}")
    public ResponseData update(@PathVariable String name, @RequestBody String data) {
        pageDefineService.update(name, data);
        return new ResponseData();
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping("/PageLock/{name}")
    public ResponseData lock(@PathVariable String name) {
        pageDefineService.update(name, pageDefineService.query(name));
        return new ResponseData();
    }
}