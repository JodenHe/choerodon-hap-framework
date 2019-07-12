export default {
  selection: false,
  paging: false,
  fields: [
    { name: 'taskId', type: 'number' },
    { name: 'taskName', type: 'string', label: '当前节点' },
    { name: 'assigneeName', type: 'string', label: '当前审批人' },
    { name: 'assigneeCode', type: 'string' },
    { name: 'delegateUserCode', type: 'string', label: '转交员工' },
    { name: 'delegateUserName', type: 'string', label: '转交员工姓名' },
  ],
};
