export default {
  autoQuery: false,
  pageSize: 10,
  queryUrl: '/wfl/instanceUP/2501',
  dataKeys: null,
  paging: false,
  fields: [
    { name: 'processName', type: 'string', label: '流程名称' },
    { name: 'startTime', type: 'string', label: '申请时间' },
    { name: 'id', type: 'string', label: '流程ID' },
    { name: 'startUserName', type: 'string', label: '申请人姓名' },
    { name: 'startUserId', type: 'string', label: '申请人' },
    { name: 'description', type: 'string', label: '描述' },
  ],
};
