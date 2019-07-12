package io.choerodon.hap.activiti.demo.controllers;

import io.choerodon.hap.activiti.demo.dto.DemoVacation;
import io.choerodon.hap.activiti.demo.service.IDemoVacationService;
import io.choerodon.base.annotation.Permission;
import io.choerodon.base.enums.ResourceType;
import io.choerodon.web.controller.BaseController;
import io.choerodon.web.core.IRequest;
import io.choerodon.web.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class DemoVacationController extends BaseController {

    @Autowired
    private IDemoVacationService vacationService;

    /*
    * 请假流程demo.
    * */
    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/wfl/runtime/process-instances/vacation", produces = "application/json")
    @ResponseBody
    public ResponseData createVacationProcessInstance(@RequestBody DemoVacation demoVacation,
                                                      HttpServletRequest httpRequest, HttpServletResponse response) {
        IRequest iRequest = createRequestContext(httpRequest);
        vacationService.createVacationInstance(iRequest, demoVacation);
        return new ResponseData();
    }

    /*
    请假流程通过businessKey获取流程表单.
    * */
    @Permission(type = ResourceType.SITE)
    @GetMapping("/wfl/history/form/details/vacation/{businessKey}")
    @ResponseBody
    public DemoVacation getProcessFrom(HttpServletRequest request, @PathVariable String businessKey) {
        DemoVacation demoVacation = new DemoVacation();
        demoVacation.setId(Long.parseLong(businessKey));
        return vacationService.selectByPrimaryKey(demoVacation);
    }

    /*
    * 获取当前用户历史请假记录.
    * */
    @Permission(type = ResourceType.SITE)
    @ResponseBody
    @PostMapping("/wfl/vacation/query")
    public ResponseData getVacationHistory(HttpServletRequest request, HttpServletResponse response, DemoVacation demoVacation, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                           @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pagesize) {
        IRequest iRequest = createRequestContext(request);
        List<DemoVacation> lists = vacationService.selectVacationHistory(iRequest);
        return new ResponseData(lists);
    }

    /*
   * 获取当前用户历史请假记录.
   * */
    @Permission(type = ResourceType.SITE)
    @ResponseBody
    @PostMapping("/wfl/vacation/save")
    public ResponseData saveVacationHistory(HttpServletRequest request, HttpServletResponse response, @RequestBody DemoVacation demoVacation, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pagesize) {
        vacationService.updateByPrimaryKeySelective(demoVacation);
        return new ResponseData();
    }
}