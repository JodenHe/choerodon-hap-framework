export default {
  autoQuery: true,
  paging: false,
  selection: false,
  fields: [
    { name: 'processInstanceId', type: 'string', label: '流程ID' },
    { name: 'id', type: 'string', label: 'id' },
    { name: 'startTime', type: 'string', label: '审批时间' },
    { name: 'name', type: 'string', label: '审批环节' },
    { name: 'assignee', type: 'string' },
    { name: 'assigneeName', type: 'string', label: '审批人' },
    { name: 'action', type: 'string', label: '审批动作', lookupCode: 'WLF.ACTION' },
    { name: 'comment', type: 'string', label: '审批意见' },
  ],

};
