export default {
  primaryKey: 'id',
  name: 'MetadataRelation',
  pageSize: 10,
  autoQuery: true,
  fields: [
    { name: 'id', type: 'string', label: '主键ID' },
    { name: 'relationType', type: 'string', label: '关系类型', required: true, lookupCode: 'MODELING.RELATION_TYPE' },
    { name: 'masterTable', type: 'string', label: '主表名', required: true, lookupUrl: '/metadata/selectTable', textField: 'tableName', valueField: 'tableName' },
    { name: 'masterColumn', type: 'string', label: '主表字段', required: true, textField: 'columnName', valueField: 'columnName' },
    { name: 'relationTable', type: 'string', label: '关联表名', required: true, lookupUrl: '/metadata/selectTable', textField: 'tableName', valueField: 'tableName' },
    { name: 'relationColumn', type: 'string', label: '关联字段', required: true, textField: 'columnName', valueField: 'columnName' },
    { name: 'uniqueName', type: 'string', label: '名称' },
    { name: 'lockedBy', type: 'string', label: '被锁定' },
  ],
  queryFields: [
    { name: 'masterTable', type: 'string', label: '主表名' },
  ],
};
