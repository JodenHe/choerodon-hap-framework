export default {
  pageSize: 10,
  dataKey: null,
  fields: [
    { name: 'processInstanceId', type: 'object', label: '流程ID' },
    { name: 'id', type: 'string', label: 'taskID' },
    { name: 'processName', type: 'string', label: '流程名称' },
    { name: 'description', type: 'string', label: '流程描述' },
    { name: 'name', type: 'string', label: '审批环节' },
    { name: 'assignee', type: 'string', label: '当前处理人' },
    { name: 'assigneeName', type: 'string', label: '当前处理人姓名' },
    { name: 'createTime', type: 'string', label: '申请时间' },
    { name: 'priority', type: 'number', label: '优先级' },
    { name: 'delegationState', type: 'string' },
    { name: 'dueTime', type: 'number' },
    { name: 'processInstance', type: 'object' },
    { name: 'activityId', type: 'string', bind: 'processInstance.activityId' },
    { name: 'processDefinitionName', type: 'string', bind: 'processInstance.processDefinitionName', label: '流程名称' },
    { name: 'businessKey', type: 'string', bind: 'processInstance.businessKey' },
    { name: 'startUserId', type: 'string', label: '申请人', bind: 'processInstance.startUserId' },
    { name: 'startUserName', type: 'string', label: '申请人姓名', bind: 'processInstance.startUserName' },
    { name: 'formKey', type: 'string' },
    { name: 'formData', type: 'object' },
    { name: 'deploymentId', type: 'object', bind: 'formData.deploymentId' },
    // { name: 'formProperties', type: 'object', bind: 'formData.formProperties' },
    { name: 'taskId', type: 'object', bind: 'formData.taskId' },
    { name: 'taskUrl', type: 'object', bind: 'formData.taskUrl' },
  ],
};
