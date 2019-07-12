package io.choerodon.hap.activiti.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.choerodon.hap.activiti.components.ActivitiMultiLanguageManager;
import io.choerodon.hap.activiti.components.ApprovalRule;
import io.choerodon.hap.activiti.core.IActivitiConstants;
import io.choerodon.hap.activiti.custom.process.CustomHistoricProcessInstanceQueryRequest;
import io.choerodon.hap.activiti.custom.task.CustomTaskQueryRequest;
import io.choerodon.hap.activiti.dto.ActivitiNode;
import io.choerodon.hap.activiti.dto.HistoricProcessInstanceResponseExt;
import io.choerodon.hap.activiti.dto.ProcessInstanceForecast;
import io.choerodon.hap.activiti.dto.TaskActionRequestExt;
import io.choerodon.hap.activiti.dto.TaskResponseExt;
import io.choerodon.hap.activiti.exception.TaskActionException;
import io.choerodon.hap.activiti.exception.WflSecurityException;
import io.choerodon.hap.activiti.exception.dto.ActiviException;
import io.choerodon.hap.activiti.service.IActivitiService;
import io.choerodon.base.annotation.Permission;
import io.choerodon.base.enums.ResourceType;
import io.choerodon.web.controller.BaseController;
import io.choerodon.web.core.IRequest;
import io.choerodon.web.dto.ResponseData;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.util.io.InputStreamSource;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.rest.common.api.DataResponse;
import org.activiti.rest.service.api.RestResponseFactory;
import org.activiti.rest.service.api.history.HistoricTaskInstanceQueryRequest;
import org.activiti.rest.service.api.repository.ModelRequest;
import org.activiti.rest.service.api.repository.ModelResponse;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shengyang.zhou@hand-china.com
 */
@Controller
@RequestMapping(value = {"/wfl", "/api/wfl"})
public class ActivitiController extends BaseController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RestResponseFactory restResponseFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IActivitiService activitiService;

    @Autowired
    protected ProcessEngineConfigurationImpl processEngineConfiguration;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ApprovalRule approvalRule;


    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 创建流程,使用当前登录用户.
     *
     * @param request     ProcessInstanceCreateRequest
     * @param httpRequest HttpServletRequest
     * @param response    HttpServletResponse
     * @return 创建的流程信息
     */
    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/runtime/process-instances", produces = "application/json")
    public ProcessInstanceResponse createProcessInstance(@RequestBody ProcessInstanceCreateRequest request,
                                                         HttpServletRequest httpRequest, HttpServletResponse response) {
        IRequest iRequest = createRequestContext(httpRequest);
        String employeeCode = httpRequest.getParameter("userId");
        if (StringUtils.isNotEmpty(employeeCode) && "ADMIN".equalsIgnoreCase(iRequest.getEmployeeCode())) {
            // allow ADMIN to simulate other user
            // FOR TEST ONLY
            iRequest.setEmployeeCode(employeeCode);
        }
        return activitiService.startProcess(iRequest, request);
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/definition/user-tasks/{processInstanceId}")
    @ResponseBody
    public List<ActivitiNode> getAvailableUserTask(HttpServletRequest request, @PathVariable String processInstanceId) {
        IRequest iRequest = createRequestContext(request);
        return activitiService.getProcessNodes(iRequest, processInstanceId);
    }


    /**
     * 待办事项,个人（强制根据员工号过滤）.
     *
     * @param request       CustomTaskQueryRequest
     * @param requestParams Map<String, String>
     * @param httpRequest   HttpServletRequest
     * @return 个人待办事项列表
     */
    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/query/tasks", produces = "application/json")
    public DataResponse getMyTasks(@RequestBody CustomTaskQueryRequest request,
                                   @RequestParam Map<String, String> requestParams, HttpServletRequest httpRequest) throws WflSecurityException {
        IRequest iRequest = createRequestContext(httpRequest);
        if (ActivitiMultiLanguageManager.multiLanguageOpen) {
            request.setProcessDefinitionName(ActivitiMultiLanguageManager.getMultiLanguageInfoByDescription(request.getProcessDefinitionNameLike(), iRequest));
            request.setProcessDefinitionNameLike(null);
        }
        if (StringUtils.isEmpty(iRequest.getEmployeeCode())) {
            throw new WflSecurityException(WflSecurityException.USER_NOT_RELATE_EMP);
        }
        if (requestParams.size() != 0) {
            request.setSize(Integer.parseInt(requestParams.get("pageSize")));
            request.setStart((Integer.parseInt(requestParams.get("page")) - 1) * Integer.parseInt(requestParams.get("pageSize")));
            if (requestParams.get("sortname") != null) {
                request.setSort(requestParams.get("sortname"));
                request.setOrder(requestParams.get("sortorder"));
            } else {
                request.setSort("priority");
                request.setOrder("desc");
            }
        }
        request.setCandidateOrAssigned(iRequest.getEmployeeCode());
        return activitiService.queryTaskList(iRequest, request, requestParams);
    }

    /**
     * 待办事项，管理员用(可以任意查询).
     *
     * @param request       CustomTaskQueryRequest
     * @param requestParams Map<String, String>
     * @param httpRequest   HttpServletRequest
     * @return 所有人的待办事项列表
     */
    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/query/tasks/admin", produces = "application/json")
    public DataResponse taskListAdmin(@RequestBody CustomTaskQueryRequest request,
                                      @RequestParam Map<String, String> requestParams, HttpServletRequest httpRequest) {
        IRequest iRequest = createRequestContext(httpRequest);
        if (ActivitiMultiLanguageManager.multiLanguageOpen) {
            request.setProcessDefinitionName(ActivitiMultiLanguageManager.getMultiLanguageInfoByDescription(request.getProcessDefinitionNameLike(), iRequest));
            request.setProcessDefinitionNameLike(null);
        }
        if (requestParams.size() != 0) {
            request.setSize(Integer.parseInt(requestParams.get("pageSize")));
            request.setStart((Integer.parseInt(requestParams.get("page")) - 1) * Integer.parseInt(requestParams.get("pageSize")));
            if (requestParams.get("sortname") != null) {
                request.setSort(requestParams.get("sortname"));
                request.setOrder(requestParams.get("sortorder"));
            } else {
                request.setSort("priority");
                request.setOrder("desc");
            }
        }
        DataResponse dataResponse = activitiService.queryTaskList(iRequest, request, requestParams);
        return dataResponse;
    }

    /**
     * 审批记录.
     *
     * @param queryRequest     HistoricTaskInstanceQueryRequest
     * @param allRequestParams Map<String, String>
     * @param request          HttpServletRequest
     * @return 流程审批历史
     */
    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/query/historic-task-instances")
    public DataResponse queryProcessInstances(HistoricTaskInstanceQueryRequest queryRequest,
                                              @RequestParam Map<String, String> allRequestParams, HttpServletRequest request) {

        IRequest iRequest = createRequestContext(request);
        return activitiService.queryHistoricTaskInstances(iRequest, queryRequest, allRequestParams);
    }

    /**
     * 我参与的流程.
     *
     * @param historicProcessInstanceQueryRequest CustomHistoricProcessInstanceQueryRequest
     * @param requestParams                       Map<String, String>
     * @param httpRequest                         HttpServletRequest
     * @return 我参与的流程列表
     */
    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/query/process-instances/my")
    public DataResponse queryProcessInstances(@RequestBody CustomHistoricProcessInstanceQueryRequest historicProcessInstanceQueryRequest,
                                              @RequestParam Map<String, String> requestParams, HttpServletRequest httpRequest) throws WflSecurityException {
        IRequest iRequest = createRequestContext(httpRequest);
        if (ActivitiMultiLanguageManager.multiLanguageOpen) {
            historicProcessInstanceQueryRequest.setProcessDefinitionNameLike
                    (ActivitiMultiLanguageManager.getMultiLanguageInfoByDescription(historicProcessInstanceQueryRequest.getProcessDefinitionNameLike(), iRequest));
        }
        if (StringUtils.isEmpty(iRequest.getEmployeeCode())) {
            throw new WflSecurityException(WflSecurityException.USER_NOT_RELATE_EMP);
        }
        if (Boolean.valueOf(requestParams.get("involved"))) {
            historicProcessInstanceQueryRequest.setInvolvedUser(iRequest.getEmployeeCode());
        } else if (Boolean.valueOf(requestParams.get("startedBy"))) {
            historicProcessInstanceQueryRequest.setStartedBy(iRequest.getEmployeeCode());
        } else if (Boolean.valueOf(requestParams.get("carbonCopy"))) {
            historicProcessInstanceQueryRequest.setCarbonCopyUser(iRequest.getEmployeeCode());
        } else {
            historicProcessInstanceQueryRequest.setInvolvedUser(iRequest.getEmployeeCode());
        }
        return activitiService.queryProcessInstances(iRequest, historicProcessInstanceQueryRequest, requestParams, true);
    }

    @Permission(type = ResourceType.SITE)
    @GetMapping(value = "/activiti/process_history_carbon.html")
    public ModelAndView carbonCopy(HttpServletRequest httpRequest) {
        ModelAndView mv = new ModelAndView(getViewPath() + "/activiti/process_history");
        mv.addObject("carbonCopy", true);
        return mv;
    }

    @Permission(type = ResourceType.SITE)
    @GetMapping(value = "/activiti/process_history_start.html")
    public ModelAndView startedBy(HttpServletRequest httpRequest) {
        ModelAndView mv = new ModelAndView(getViewPath() + "/activiti/process_history");
        mv.addObject("startedBy", true);
        return mv;
    }


    /**
     * 流程监控查询.
     *
     * @param historicProcessInstanceQueryRequest CustomHistoricProcessInstanceQueryRequest
     * @param requestParams                       Map<String, String>
     * @param httpRequest                         HttpServletRequest
     * @return 所有流程审批记录
     */
    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/query/process-instances/monitor")
    public DataResponse queryAllProcessInstances(@RequestBody CustomHistoricProcessInstanceQueryRequest historicProcessInstanceQueryRequest,
                                                 @RequestParam Map<String, String> requestParams, HttpServletRequest httpRequest) {
        IRequest iRequest = createRequestContext(httpRequest);
        if (ActivitiMultiLanguageManager.multiLanguageOpen) {
            historicProcessInstanceQueryRequest.setProcessDefinitionNameLike
                    (ActivitiMultiLanguageManager.getMultiLanguageInfoByDescription(historicProcessInstanceQueryRequest.getProcessDefinitionNameLike(), iRequest));
        }
        if (requestParams.size() != 0) {
            historicProcessInstanceQueryRequest.setSize(Integer.parseInt(requestParams.get("pageSize")));
            historicProcessInstanceQueryRequest.setStart((Integer.parseInt(requestParams.get("page")) - 1) * Integer.parseInt(requestParams.get("pageSize")));
            if (requestParams.get("sortname") != null) {
                historicProcessInstanceQueryRequest.setSort(requestParams.get("sortname"));
                historicProcessInstanceQueryRequest.setOrder(requestParams.get("sortorder"));
            }
        }
        return activitiService.queryProcessInstances(iRequest, historicProcessInstanceQueryRequest, requestParams, false);
    }

    @Permission(type = ResourceType.SITE)
    @GetMapping(value = "/activiti/admin/task_detail.html")
    public ModelAndView adminTask(HttpServletRequest httpRequest, String taskId) {
        ModelAndView mv = new ModelAndView(getViewPath() + "/activiti/task_detail");
        mv.addObject("taskId", taskId);
        mv.addObject("isAdmin", true);
        return mv;
    }

    /**
     * 完成,转交...任务.
     *
     * @param taskId        人工任务Id
     * @param actionRequest TaskActionRequestExt
     * @param request       HttpServletRequest
     * @param response      HttpServletResponse
     */
    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/runtime/tasks/{taskId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void executeTaskAction(@PathVariable String taskId, @RequestBody TaskActionRequestExt actionRequest,
                                  HttpServletRequest request, HttpServletResponse response) throws TaskActionException {
        IRequest iRequest = createRequestContext(request);
        activitiService.executeTaskAction(iRequest, taskId, actionRequest, false);
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/runtime/admin/tasks/{taskId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void executeTaskActionAdmin(@PathVariable String taskId, @RequestBody TaskActionRequestExt actionRequest,
                                       HttpServletRequest request, HttpServletResponse response) throws TaskActionException {
        IRequest iRequest = createRequestContext(request);
        activitiService.executeTaskAction(iRequest, taskId, actionRequest, true);
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/runtime/admin/tasks/batch-delegate")
    @ResponseStatus(value = HttpStatus.OK)
    public void executeBatchDelegate(@RequestBody List<TaskActionRequestExt> actionRequests,
                                     HttpServletRequest request, HttpServletResponse response) throws TaskActionException {
        IRequest iRequest = createRequestContext(request);
        for (TaskActionRequestExt actionRequest : actionRequests) {
            actionRequest.setComment("");
            activitiService.executeTaskAction(iRequest, actionRequest.getCurrentTaskId(), actionRequest, true);
        }
    }

    /**
     * 新建模型(editor).
     *
     * @param modelRequest ModelRequest
     * @param request      HttpServletRequest
     * @param response     HttpServletResponse
     * @return 新建的模型数据
     */
    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/repository/models", produces = "application/json")
    public ModelResponse createModel(@RequestBody ModelRequest modelRequest, HttpServletRequest request,
                                     HttpServletResponse response) {
        Model model = repositoryService.newModel();
        model.setCategory(modelRequest.getCategory());
        model.setDeploymentId(modelRequest.getDeploymentId());
        model.setKey(modelRequest.getKey());
        model.setMetaInfo(modelRequest.getMetaInfo());
        model.setName(modelRequest.getName());
        model.setVersion(modelRequest.getVersion());
        model.setTenantId(modelRequest.getTenantId());

        repositoryService.saveModel(model);
        response.setStatus(HttpStatus.CREATED.value());

        HashMap<String, Object> content = new HashMap<>();
        content.put("resourceId", model.getId());

        HashMap<String, String> properties = new HashMap<>();
        properties.put("process_id", modelRequest.getKey());
        properties.put("name", modelRequest.getName());
        properties.put("process_namespace", modelRequest.getCategory());
        content.put("properties", properties);

        HashMap<String, String> stencilset = new HashMap<>();
        stencilset.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        content.put("stencilset", stencilset);

        try {
            repositoryService.addModelEditorSource(model.getId(), objectMapper.writeValueAsBytes(content));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }

        return restResponseFactory.createModelResponse(model);

    }

    @Permission(type = ResourceType.SITE)
    @GetMapping("/repository/model/node")
    public ResponseData getUserTaskFromModelSource(HttpServletRequest request, String modelId) {
        IRequest iRequest = createRequestContext(request);
        List<?> list = activitiService.getUserTaskFromModelSource(iRequest, modelId);
        return new ResponseData(list);
    }

    /**
     * 部署流程.
     *
     * @param modelId 流程模型Id
     * @param request HttpServletRequest
     * @return 部署流程信息
     */
    @Permission(type = ResourceType.SITE)
    @GetMapping("/repository/model/{modelId}/deploy")
    @ResponseBody
    public ResponseData modelDeployment(@PathVariable String modelId, HttpServletRequest request) {
        IRequest iRequest = createRequestContext(request);
        Model model = null;
        try {
            model = activitiService.deployModel(modelId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ResponseData rd = new ResponseData(false);
            rd.setMessage(e.getMessage());
            return rd;
        }
        model.setName(ActivitiMultiLanguageManager.getMultLanguageInfoByCode(model.getName(), iRequest));
        return new ResponseData(Collections.singletonList(model));
    }

    @Permission(type = ResourceType.SITE)
    @GetMapping(value = "/repository/model/{modelId}/export", produces = "text/xml")
    public ResponseEntity<byte[]> modelExport(@PathVariable String modelId,
                                              @RequestParam(defaultValue = "") String type) throws IOException {
        Model model = repositoryService.getModel(modelId);
        byte[] modelData = repositoryService.getModelEditorSource(modelId);
        JsonNode jsonNode = objectMapper.readTree(modelData);
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(jsonNode);
        byte[] xmlBytes = new BpmnXMLConverter().convertToXML(bpmnModel, "UTF-8");
        HttpHeaders responseHeaders = new HttpHeaders();
        String id = model.getId();
        if (bpmnModel.getMainProcess() != null) {
            id = bpmnModel.getMainProcess().getId();
        }
        if ("bpmn20".equalsIgnoreCase(type)) {
            responseHeaders.set("Content-Disposition", "attachment;filename=" + id + ".bpmn20.xml");
            responseHeaders.set("Content-Type", "application/octet-stream");
        } else {
            responseHeaders.set("Content-Type", "text/xml;charset=utf8");
        }
        try {
            return new ResponseEntity<>(xmlBytes, responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            throw new ActivitiIllegalArgumentException("Error exporting diagram", e);
        }
    }

    @Permission(type = ResourceType.SITE)
    @GetMapping("/repository/deploy/{deployId}/export")
    public ResponseEntity<byte[]> deployExport(@PathVariable String deployId) throws IOException {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(deployId);
        byte[] xmlBytes = new BpmnXMLConverter().convertToXML(bpmnModel, "UTF-8");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "text/xml;charset=utf8");
        try {
            return new ResponseEntity<>(xmlBytes, responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            throw new ActivitiIllegalArgumentException("Error exporting diagram", e);
        }
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/repository/model/import", produces = "text/html;charset=UTF-8")
    public String importModel(HttpServletRequest request) throws FileUploadException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            return "<script>alert('NOT a Multipart Request')</script>";
        }
        InputStream fileInputStream = null;
        String name = null;
        String key = null;
        String category = null;
        if (request instanceof MultipartHttpServletRequest) {
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            if (file != null) {
                fileInputStream = file.getInputStream();
            }
            name = request.getParameter("name");
            key = request.getParameter("key");
            category = request.getParameter("category");
        } else {
            ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
            List<FileItem> items = upload.parseRequest(request);
            FileItem item = null;
            Map<String, String> parameters = new HashMap<>();
            for (FileItem fi : items) {
                if (!fi.isFormField()) {
                    item = fi;
                } else {
                    parameters.put(fi.getFieldName(), fi.getString("UTF-8"));
                }
            }
            if (item != null && StringUtils.isNotEmpty(item.getName())) {
                fileInputStream = item.getInputStream();
                name = parameters.get("name");
                key = parameters.get("key");
                category = parameters.get("category");
            }
        }
        if (fileInputStream == null) {
            return "<script>alert('File Content is Null')</script>";
        }
        try (InputStream inputStream = fileInputStream) {
            InputStreamSource source = new InputStreamSource(fileInputStream);
            BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(source, false, false, "UTF-8");
            bpmnModel.getMainProcess().setId(StringUtils.defaultIfEmpty(key, bpmnModel.getMainProcess().getId()));
            bpmnModel.getMainProcess().setName(StringUtils.defaultIfEmpty(name, bpmnModel.getMainProcess().getName()));

            Model model = repositoryService.newModel();
            model.setCategory(StringUtils.defaultIfEmpty(category, "default"));
            model.setDeploymentId(null);
            model.setKey(bpmnModel.getMainProcess().getId());
            model.setName(bpmnModel.getMainProcess().getName());

            String metaInfo = model.getMetaInfo();
            if (StringUtils.isEmpty(metaInfo)) {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", model.getName());
                map.put("version", String.valueOf(model.getVersion()));
                metaInfo = objectMapper.writeValueAsString(map);
            }
            model.setMetaInfo(metaInfo);

            repositoryService.saveModel(model);

            ObjectNode content = new BpmnJsonConverter().convertToJson(bpmnModel);
            content = approvalRule.processCustomProperties(content);

            repositoryService.addModelEditorSource(model.getId(), objectMapper.writeValueAsBytes(content));
            restResponseFactory.createModelResponse(model);
            return "<script>window.parent.onImportComplete(true)</script>";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "<script>window.parent.onImportComplete(false,'请上传符合 BPMN 规范的文件')</script>";
        }
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/v2/repository/model/import", produces = "application/json;charset=UTF-8")
    public ResponseData importModel2(HttpServletRequest request) throws FileUploadException, IOException {
        ResponseData responseData = new ResponseData();
        if (!ServletFileUpload.isMultipartContent(request)) {
            responseData.setSuccess(false);
            responseData.setMessage("NOT a Multipart Request");
            return responseData;
        }
        InputStream fileInputStream = null;
        String name = null;
        String key = null;
        String category = null;
        if (request instanceof MultipartHttpServletRequest) {
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            if (file != null) {
                fileInputStream = file.getInputStream();
            }
            name = request.getParameter("name");
            key = request.getParameter("key");
            category = request.getParameter("category");
        } else {
            ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
            List<FileItem> items = upload.parseRequest(request);
            FileItem item = null;
            Map<String, String> parameters = new HashMap<>();
            for (FileItem fi : items) {
                if (!fi.isFormField()) {
                    item = fi;
                } else {
                    parameters.put(fi.getFieldName(), fi.getString("UTF-8"));
                }
            }
            if (item != null && StringUtils.isNotEmpty(item.getName())) {
                fileInputStream = item.getInputStream();
                name = parameters.get("name");
                key = parameters.get("key");
                category = parameters.get("category");
            }
        }
        if (fileInputStream == null) {
            responseData.setSuccess(false);
            responseData.setMessage("File Content is Null");
            return responseData;
        }
        try (InputStream inputStream = fileInputStream) {
            InputStreamSource source = new InputStreamSource(fileInputStream);
            BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(source, false, false, "UTF-8");
            bpmnModel.getMainProcess().setId(StringUtils.defaultIfEmpty(key, bpmnModel.getMainProcess().getId()));
            bpmnModel.getMainProcess().setName(StringUtils.defaultIfEmpty(name, bpmnModel.getMainProcess().getName()));

            Model model = repositoryService.newModel();
            model.setCategory(StringUtils.defaultIfEmpty(category, "default"));
            model.setDeploymentId(null);
            model.setKey(bpmnModel.getMainProcess().getId());
            model.setName(bpmnModel.getMainProcess().getName());

            String metaInfo = model.getMetaInfo();
            if (StringUtils.isEmpty(metaInfo)) {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", model.getName());
                map.put("version", String.valueOf(model.getVersion()));
                metaInfo = objectMapper.writeValueAsString(map);
            }
            model.setMetaInfo(metaInfo);

            repositoryService.saveModel(model);

            ObjectNode content = new BpmnJsonConverter().convertToJson(bpmnModel);
            content = approvalRule.processCustomProperties(content);

            repositoryService.addModelEditorSource(model.getId(), objectMapper.writeValueAsBytes(content));
            restResponseFactory.createModelResponse(model);
            responseData.setSuccess(true);
            return responseData;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseData.setSuccess(false);
            responseData.setMessage("请上传符合 BPMN 规范的文件");
            return responseData;
        }

    }

    @Permission(type = ResourceType.SITE)
    @PostMapping("/runtime/tasks/{taskId}/details")
    @ResponseBody
    public TaskResponseExt taskDetails(HttpServletRequest request, @PathVariable String taskId)
            throws WflSecurityException {

        IRequest iRequest = createRequestContext(request);
        return activitiService.getTaskDetails(iRequest, taskId);
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping("/runtime/admin/tasks/{taskId}/details")
    @ResponseBody
    public TaskResponseExt taskDetailsAdmin(HttpServletRequest request, @PathVariable String taskId)
            throws WflSecurityException {

        IRequest iRequest = createRequestContext(request);
        return activitiService.getTaskDetails(iRequest, taskId, true);
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/runtime/process-instances/{processInstanceId}/forecast")
    public List<ProcessInstanceForecast> getProcessInstanceForecast(HttpServletRequest request, @PathVariable String processInstanceId) {
        IRequest iRequest = createRequestContext(request);
        return activitiService.processInstanceForecast(iRequest, processInstanceId);

    }

    @Permission(type = ResourceType.SITE)
    @GetMapping(value = "/runtime/process-instances/{processInstanceId}/diagram")
    public ResponseEntity<byte[]> getProcessInstanceDiagram(@PathVariable String processInstanceId, HttpServletRequest request, HttpServletResponse response) {
        IRequest iRequest = createRequestContext(request);
        boolean isActivity = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult() != null;

        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).list().iterator().next();

        ProcessDefinition pde = repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());

        if (pde != null && pde.hasGraphicalNotation()) {
            BpmnModel bpmnModel = repositoryService.getBpmnModel(pde.getId());
            ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();

            List<HistoricActivityInstance> finished = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId).finished().list();
            Map<String, Boolean> boundaryUserTask = new HashMap<>();
            Map<String, Date> nodeEndTime = new HashMap<>();
            List<String> ids = finished.stream()
                    .map(e -> {
                        if (StringUtils.isNotEmpty(e.getDeleteReason()) && e.getDeleteReason().contains("boundary")) {
                            boundaryUserTask.put(e.getActivityId(), true);
                        }
                        nodeEndTime.put(e.getActivityId(), e.getEndTime());
                        return e.getActivityId() + IActivitiConstants.HISTORY_SUFFIX;
                    }).collect(Collectors.toList());
            if (isActivity) {
                ids.addAll(runtimeService.getActiveActivityIds(processInstance.getId()));
            }

            List<String> flows = new ArrayList<>();
            Collection<SequenceFlow> flowElement = bpmnModel.getMainProcess().findFlowElementsOfType(SequenceFlow.class);

            Map<String, Boolean> idMap = new HashedMap();

            for (String id : ids) {
                if (id.endsWith(IActivitiConstants.HISTORY_SUFFIX)) {
                    id = id.substring(0, id.indexOf(IActivitiConstants.HISTORY_SUFFIX));
                }
                idMap.put(id, true);
            }
            // Map<单一网关Id, 目标节点最早的结束时间>
            Map<String, Date> exclusiveGatewayEarliestEndNode = new HashMap<>();
            for (SequenceFlow flowElement1 : flowElement) {
                String startId = flowElement1.getSourceRef();
                String endId = flowElement1.getTargetRef();
                Boolean isStartId = idMap.get(startId);
                Boolean isEndId = idMap.get(endId);
                Boolean isSelectFlow = Boolean.TRUE;
                if (flowElement1.getSourceFlowElement() instanceof ExclusiveGateway) {
                    FlowNode node = (FlowNode) flowElement1.getSourceFlowElement();
                    List<SequenceFlow> flowList = node.getOutgoingFlows();
                    if (CollectionUtils.isNotEmpty(flowList) && flowList.size() > 1) {
                        Date currentNodeEndTime = nodeEndTime.get(endId);
                        if (currentNodeEndTime != null) {
                            //当前单一网关是否已经有目标节点最早的结束时间
                            if (exclusiveGatewayEarliestEndNode.containsKey(node.getId())) {
                                Date currentEarliestNodeEndTime = exclusiveGatewayEarliestEndNode.get(node.getId());
                                //比较当前顺序流的目标节点结束时间是否为最早
                                if (!currentNodeEndTime.equals(currentEarliestNodeEndTime)) {
                                    isSelectFlow = Boolean.FALSE;
                                }
                            } else {
                                Date earliestNodeEndTime = nodeEndTime.get(flowList.get(0).getTargetRef());
                                for (int i = 1; i < flowList.size(); i++) {
                                    Date flowNodeEndTime = nodeEndTime.get(flowList.get(i).getTargetRef());
                                    if (flowNodeEndTime != null && (earliestNodeEndTime == null || flowNodeEndTime.before(earliestNodeEndTime))) {
                                        earliestNodeEndTime = flowNodeEndTime;
                                    }
                                }
                                exclusiveGatewayEarliestEndNode.put(node.getId(), earliestNodeEndTime);
                                if (!currentNodeEndTime.equals(earliestNodeEndTime)) {
                                    isSelectFlow = Boolean.FALSE;
                                }
                            }
                        }
                    }
                }

                boolean boundary = boundaryUserTask.get(startId) != null ? boundaryUserTask.get(startId) : false;
                if (BooleanUtils.isTrue(isStartId) && !boundary && BooleanUtils.isTrue(isEndId) && isSelectFlow) {
                    flows.add(flowElement1.getId());
                }
            }

            //存储原节点名称
            Map<String, String> oldFlowNodeName = new HashMap<>();
            Collection<FlowNode> flowNodes = bpmnModel.getMainProcess().findFlowElementsOfType(FlowNode.class);
            //展示流程图时 替换节点多语言名称
            for (FlowNode flowNode : flowNodes) {
                oldFlowNodeName.put(flowNode.getId(), flowNode.getName());
                flowNode.setName(ActivitiMultiLanguageManager.getMultLanguageInfoByCode(flowNode.getName(), iRequest));
            }

            InputStream resource = diagramGenerator.generateDiagram(bpmnModel, "svg", ids,
                    flows, processEngineConfiguration.getActivityFontName(),
                    processEngineConfiguration.getLabelFontName(), processEngineConfiguration.getAnnotationFontName(),
                    processEngineConfiguration.getClassLoader(), 1.0);

            //生成流程图后 还原节点名称
            for (FlowNode flowNode : flowNodes) {
                flowNode.setName(oldFlowNodeName.get(flowNode.getId()));
            }

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Content-Type", "image/svg+xml");
            try {
                return new ResponseEntity<>(IOUtils.toByteArray(resource), responseHeaders, HttpStatus.OK);
            } catch (Exception e) {
                throw new ActivitiIllegalArgumentException("Error exporting diagram", e);
            }

        } else {
            throw new ActivitiIllegalArgumentException(
                    "Process instance with id '" + processInstance.getId() + "' has no graphical notation defined.");
        }
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping("/history/form/details/{procId}")
    @ResponseBody
    public ResponseData getFromVar(HttpServletRequest request, @PathVariable String procId) {
        return new ResponseData(historyService.createHistoricVariableInstanceQuery().processInstanceId(procId).list());
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping("/instance/{processInstanceId}")
    @ResponseBody
    public HistoricProcessInstanceResponseExt instanceDetail(HttpServletRequest request, @PathVariable String processInstanceId)
            throws WflSecurityException {
        IRequest iRequest = createRequestContext(request);
        return activitiService.getInstanceDetail(iRequest, processInstanceId);
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping("/instanceUP/{processInstanceId}")
    @ResponseBody
    public ResponseData instanceDetailUP(HttpServletRequest request, @PathVariable String processInstanceId)
            throws WflSecurityException {
        IRequest iRequest = createRequestContext(request);
        List<HistoricProcessInstanceResponseExt> list = new ArrayList<>();
        list.add(activitiService.getInstanceDetail(iRequest, processInstanceId));
        return new ResponseData(list);
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping("/instance/carbon-copy-read")
    @ResponseBody
    public void carbonCopyRead(HttpServletRequest request, @RequestBody HistoricProcessInstanceResponseExt processInstanceResponseExt)
            throws WflSecurityException {
        IRequest iRequest = createRequestContext(request);
        if ("N".equalsIgnoreCase(processInstanceResponseExt.getReadFlag())) {
            activitiService.processCarbonCopyRead(processInstanceResponseExt.getId(), iRequest.getEmployeeCode());
        }
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping("/runtime/prc/suspend/{procId}")
    public void suspendProc(HttpServletRequest request, @PathVariable String procId) {
        runtimeService.suspendProcessInstanceById(procId);
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping("/runtime/prc/active/{procId}")
    public void activeProc(HttpServletRequest request, @PathVariable String procId) {
        runtimeService.activateProcessInstanceById(procId);
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/runtime/prc/end/{procId}")
    @ResponseBody
    public void endProc(HttpServletRequest request, @PathVariable String procId) {
        activitiService.deleteProcessInstance(procId);
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/runtime/prc/back/{procId}")
    @ResponseBody
    public void backProc(HttpServletRequest request, @PathVariable String procId) {
        IRequest iRequest = createRequestContext(request);
        if (activitiService.isStartRecall(procId, iRequest.getEmployeeCode())) {
            activitiService.startRecall(iRequest, procId, iRequest.getEmployeeCode());
        } else if (activitiService.isTaskRecall(procId, iRequest.getEmployeeCode())) {
            activitiService.taskRecall(iRequest, procId, iRequest.getEmployeeCode());
        }
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/runtime/exception/search")
    @ResponseBody
    public ResponseData searchException(HttpServletRequest request, ActiviException exception, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pagesize) {
        IRequest iRequest = createRequestContext(request);
        List<ActiviException> exceptions = activitiService.queryException(exception, page, pagesize);
        if (ActivitiMultiLanguageManager.multiLanguageOpen && CollectionUtils.isNotEmpty(exceptions)) {
            for (ActiviException exception1 : exceptions) {
                exception1.setProcDefName(ActivitiMultiLanguageManager.getMultLanguageInfoByCode(exception1.getProcDefName(), iRequest));
            }
        }
        return new ResponseData(exceptions);
    }

    @Permission(type = ResourceType.SITE)
    @PostMapping(value = "/runtime/execute/{procId}")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseData executeTaskActionByAdmin(@PathVariable String procId, @RequestBody TaskActionRequestExt actionRequest,
                                                 HttpServletRequest request, HttpServletResponse response) throws TaskActionException {
        actionRequest.setComment("");
        IRequest iRequest = createRequestContext(request);
        activitiService.executeTaskByAdmin(iRequest, procId, actionRequest);
        return new ResponseData();
    }

    @Permission(type = ResourceType.SITE)
    @DeleteMapping(value = "/repository/deployments/{deploymentId}", produces = "application/json")
    public void deleteDeployment(@PathVariable String deploymentId, @RequestParam(value = "cascade", required = false, defaultValue = "false") Boolean cascade, HttpServletResponse response) {

        activitiService.deleteDeployment(deploymentId, cascade);
        response.setStatus(HttpStatus.NO_CONTENT.value());
    }
}
