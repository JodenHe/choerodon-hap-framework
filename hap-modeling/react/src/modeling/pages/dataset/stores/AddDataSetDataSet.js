export default {
  fields: [
    { name: 'name', type: 'string', label: '名称', required: true },
    { name: 'tableName', type: 'string', label: '主表名', required: true, lookupUrl: '/metadata/selectTable', textField: 'tableName', valueField: 'tableName' },
  ],
};
