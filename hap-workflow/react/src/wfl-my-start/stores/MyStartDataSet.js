export default {
  autoQuery: true,
  pageSize: 20,
  name: 'MyStart',
  selection: false,
  fields: [
    { name: 'processName', type: 'string', label: '流程名称' },
    { name: 'id', type: 'string', label: '流程ID' },
    { name: 'status', type: 'string', label: '流程状态', lookupCode: 'WLF.STATUS' },
    { name: 'description', type: 'string', label: '流程描述' },
    { name: 'taskName', type: 'string', label: '当前节点' },
    { name: 'startUserId', type: 'string', label: '申请人ID' },
    { name: 'startUserName', type: 'string', label: '申请人' },
    { name: 'currentApprover', type: 'string', label: '当前处理人' },
    { name: 'startTime', type: 'dateTime', label: '创建时间' },
    { name: 'endTime', type: 'dateTime', label: '结束时间' },
    { name: 'startedBy', type: 'string', label: '' },
    { name: 'finished', type: 'boolean', label: '' },
    { name: 'suspended', type: 'boolean', label: '' },
  ],
  queryFields: [
    { name: 'id', type: 'string', label: '流程ID' },
    { name: 'processName', type: 'string', label: '流程名称' },
    { name: 'status', type: 'string', label: '流程状态', lookupCode: 'WLF.STATUS' },
    { name: 'startedAfter', type: 'dateTime', label: '创建时间从' },
    { name: 'startedBefore', type: 'dateTime', label: '创建时间至' },

  ],
};
