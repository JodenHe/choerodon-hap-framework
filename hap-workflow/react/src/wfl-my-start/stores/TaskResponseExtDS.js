export default {
  autoQuery: true,
  pageSize: 10,
  dataKey: null,
  fields: [
    { name: 'formKey', type: 'string' },
    { name: 'currentApprover', type: 'string', label: '当前处理人' },
    { name: 'description', type: 'string', label: '流程描述' },
    { name: 'processName', type: 'string', label: '流程' },
    { name: 'id', type: 'string', label: '流程ID' },
    { name: 'startUserId', type: 'string', label: '申请人' },
    { name: 'startUserName', type: 'string', label: '申请人姓名' },
    { name: 'startTime', type: 'dateTime', label: '申请时间' },
    { name: 'businessKey', type: 'string' },

  ],
};
