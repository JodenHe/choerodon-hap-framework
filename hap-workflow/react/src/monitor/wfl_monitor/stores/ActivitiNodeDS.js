export default {
  queryUrl: '/wfl/definition/user-tasks',
  dataKey: null,
  autoQuery: false,
  selection: 'single',
  paging: false,
  fields: [
    { name: 'nodeId', type: 'string', label: '节点Id' },
    { name: 'name', type: 'string', label: '节点名称' },
    { name: 'type', type: 'string' },
  ],
};
