package io.choerodon.hap.activiti.approval.controllers;

import io.choerodon.hap.activiti.approval.dto.ApproveCandidateRule;
import io.choerodon.hap.activiti.approval.service.IApproveCandidateRuleService;
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
@RequestMapping(value = {"/wfl/approve/candidate/rule", "/api/wfl/approve/candidate/rule"})
public class ApproveCandidateRuleController extends BaseController {

    @Autowired
    private IApproveCandidateRuleService service;

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/query")
    public ResponseData query(ApproveCandidateRule dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        return new ResponseData(service.select(dto, page, pageSize));
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/queryAll")
    public ResponseData queryAll(HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        ApproveCandidateRule rule = new ApproveCandidateRule();
        rule.setEnableFlag(BaseConstants.YES);
        return new ResponseData(service.selectAll(requestContext, rule));
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/submit")
    public ResponseData update(HttpServletRequest request, @RequestBody List<ApproveCandidateRule> dto, BindingResult result) {
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
    public ResponseData delete(HttpServletRequest request, @RequestBody List<ApproveCandidateRule> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}