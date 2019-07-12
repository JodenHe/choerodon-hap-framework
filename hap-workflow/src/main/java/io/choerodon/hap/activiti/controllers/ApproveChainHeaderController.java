package io.choerodon.hap.activiti.controllers;

import io.choerodon.base.annotation.Permission;
import io.choerodon.base.enums.ResourceType;
import io.choerodon.hap.activiti.dto.ApproveChainHeader;
import io.choerodon.hap.activiti.service.IApproveChainHeaderService;
import io.choerodon.web.controller.BaseController;
import io.choerodon.web.core.IRequest;
import io.choerodon.web.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping(value = {"/wfl", "/api/wfl"})
public class ApproveChainHeaderController extends BaseController {

    @Autowired
    private IApproveChainHeaderService service;

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/approve/chain/header/query")
    public ResponseData query(@RequestBody(required = false) ApproveChainHeader dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        return new ResponseData(service.select(dto, page, pageSize));
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/approve/chain/header/submit")
    public ResponseData update(HttpServletRequest request, @RequestBody List<ApproveChainHeader> dto) {
        return new ResponseData(service.batchUpdate(dto));
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/approve/chain/submit")
    public ResponseData submitHeadLine(HttpServletRequest request, @RequestBody List<ApproveChainHeader> dto, final BindingResult result) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData rd = new ResponseData(false);
            rd.setMessage(getErrorMessage(result, request));
            return rd;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.updateHeadLine(dto));
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/approve/chain/header/remove")
    public ResponseData delete(HttpServletRequest request, @RequestBody List<ApproveChainHeader> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}