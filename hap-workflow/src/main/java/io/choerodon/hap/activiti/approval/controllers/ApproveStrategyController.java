package io.choerodon.hap.activiti.approval.controllers;

import io.choerodon.hap.activiti.approval.dto.ApproveStrategy;
import io.choerodon.hap.activiti.approval.service.IApproveStrategyService;
import io.choerodon.base.annotation.Permission;
import io.choerodon.base.enums.ResourceType;
import io.choerodon.base.util.BaseConstants;
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
@RequestMapping(value = {"/wfl/approve/strategy", "/api/wfl/approve/strategy"})
public class ApproveStrategyController extends BaseController {

    @Autowired
    private IApproveStrategyService service;

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/query")
    public ResponseData query(ApproveStrategy dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        return new ResponseData(service.select(dto, page, pageSize));
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/queryAll")
    public ResponseData queryAll(HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ApproveStrategy dto = new ApproveStrategy();
        dto.setEnableFlag(BaseConstants.YES);
        return new ResponseData(service.selectAll(requestContext, dto));
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/submit")
    public ResponseData update(HttpServletRequest request, @RequestBody List<ApproveStrategy> dto, BindingResult result) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData rd = new ResponseData(false);
            rd.setMessage(getErrorMessage(result, request));
            return rd;
        }
        return new ResponseData(service.batchUpdate(dto));
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/remove")
    public ResponseData delete(HttpServletRequest request, @RequestBody List<ApproveStrategy> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}