package io.choerodon.hap.activiti.controllers;

import io.choerodon.hap.activiti.dto.ApproveChainHeader;
import io.choerodon.hap.activiti.dto.ApproveChainLine;
import io.choerodon.hap.activiti.service.IApproveChainLineService;
import io.choerodon.base.annotation.Permission;
import io.choerodon.base.enums.ResourceType;
import io.choerodon.web.controller.BaseController;
import io.choerodon.web.core.IRequest;
import io.choerodon.web.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping(value = {"/wfl", "/api/wfl"})
public class ApproveChainLineController extends BaseController {

    @Autowired
    private IApproveChainLineService service;

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/approve/chain/line/query")
    public ResponseData query(ApproveChainLine dto, HttpServletRequest request) {
        return new ResponseData(service.select(dto, 1, 0));
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = {"/approve-chain-line/query", "/approve/chain/line/queryByHeader"})
    public ResponseData queryByHeader(ApproveChainHeader dto, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(
                service.selectByNodeId(requestContext, dto.getProcessKey(), dto.getUsertaskId()));
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/approve/chain/line/submit")
    public ResponseData update(HttpServletRequest request, @RequestBody List<ApproveChainLine> dto) {
        return new ResponseData(service.batchUpdate(dto));
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/approve/chain/line/remove")
    public ResponseData delete(HttpServletRequest request, @RequestBody List<ApproveChainLine> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }
}