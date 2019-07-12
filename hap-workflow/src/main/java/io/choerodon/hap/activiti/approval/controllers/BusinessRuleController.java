package io.choerodon.hap.activiti.approval.controllers;

import io.choerodon.hap.activiti.approval.dto.BusinessRuleHeader;
import io.choerodon.hap.activiti.approval.dto.BusinessRuleLine;
import io.choerodon.hap.activiti.approval.service.IBusinessRuleHeaderService;
import io.choerodon.hap.activiti.approval.service.IBusinessRuleLineService;
import io.choerodon.base.annotation.Permission;
import io.choerodon.base.enums.ResourceType;
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

/**
 * @author xiangyu.qi@hand-china.com
 */
@RestController
@RequestMapping(value = {"/wfl/business/rule", "/api/wfl/business/rule"})
public class BusinessRuleController extends BaseController {

    @Autowired
    private IBusinessRuleHeaderService service;

    @Autowired
    private IBusinessRuleLineService lineService;

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/header/query")
    public ResponseData query(BusinessRuleHeader dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        return new ResponseData(service.select(dto, page, pageSize));
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/header/queryAll")
    public ResponseData queryAll(HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        BusinessRuleHeader dto = new BusinessRuleHeader();
        dto.setEnableFlag("Y");
        return new ResponseData(service.selectAll(requestContext, dto));
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/header/submit")
    public ResponseData update(HttpServletRequest request, @RequestBody List<BusinessRuleHeader> dto, BindingResult result) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData rd = new ResponseData(false);
            rd.setMessage(getErrorMessage(result, request));
            return rd;
        }
        return new ResponseData(service.batchUpdate(dto));
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/header/remove")
    public ResponseData delete(HttpServletRequest request, @RequestBody List<BusinessRuleHeader> dto) {
        IRequest requestCtx = createRequestContext(request);
        service.batchDelete(requestCtx, dto);
        return new ResponseData();
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/line/query")
    public ResponseData queryLine(BusinessRuleLine dto, HttpServletRequest request) {
        return new ResponseData(lineService.selectOptions(dto, null));
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/line/remove")
    public ResponseData deleteLine(HttpServletRequest request, @RequestBody List<BusinessRuleLine> dto) {
        lineService.batchDelete(dto);
        return new ResponseData();
    }
}