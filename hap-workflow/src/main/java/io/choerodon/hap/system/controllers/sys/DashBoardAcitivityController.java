package io.choerodon.hap.system.controllers.sys;

import io.choerodon.hap.activiti.service.IActivitiService;
import io.choerodon.base.annotation.Permission;
import io.choerodon.base.enums.ResourceType;
import io.choerodon.web.controller.BaseController;
import io.choerodon.web.core.IRequest;
import org.activiti.rest.common.api.DataResponse;
import org.activiti.rest.service.api.runtime.task.TaskQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * DashBoardController.
 *
 * @author zhizheng.yang@hand-china.com
 */

@Controller
public class DashBoardAcitivityController extends BaseController {

    @Autowired
    private IActivitiService activitiService;

    /**
     * 个人工作流代办仪表盘.
     *
     * @param httpRequest HttpServletRequest
     */
    @Permission(type = ResourceType.SITE)
    @GetMapping(value = "/dashboard/task.html")
    public ModelAndView getQueryResult(HttpServletRequest httpRequest) {
        IRequest iRequest = createRequestContext(httpRequest);
        TaskQueryRequest request = new TaskQueryRequest();
        request.setCandidateOrAssigned(iRequest.getEmployeeCode());
        request.setSort("createTime");
        request.setOrder("asc");
        Map<String, String> map = new HashMap<String, String>();
        map.put("size", "20");
        DataResponse dataResponse = activitiService.queryTaskList(iRequest, request, map);
        ModelAndView view = new ModelAndView(getViewPath() + "/dashboard/task");
        view.addObject("message", dataResponse.getData());
        return view;
    }

}
