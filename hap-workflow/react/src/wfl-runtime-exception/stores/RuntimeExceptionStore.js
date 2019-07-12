export default {
  autoQuery: true,
  name: 'ActiviException',
  pageSize: 20,
  fields: [
    { name: 'procId', label: '实例 ID' },
    { name: 'procDefName', label: '实例名称' },
    { name: 'messStr', label: '异常信息' },
    { name: 'duedate', label: '产生时间' },
  ],
  queryFields: [
    { name: 'procId', type: 'string', label: '实例 ID' },
  ],
};
