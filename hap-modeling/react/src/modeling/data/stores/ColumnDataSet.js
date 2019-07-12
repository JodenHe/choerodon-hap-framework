export default {
  primaryKey: 'id',
  name: 'MetadataColumn',
  pageSize: 10,
  fields: [
    { name: 'tableName', type: 'string' },
    { name: 'columnName', type: 'string', label: '列名', required: true },
    { name: 'displayType', type: 'string', label: '类型', required: true, lookupCode: 'MODELING.COLUMN_TYPE' },
    { name: 'multiLanguage', type: 'boolean', label: '多语言' },
    { name: 'description', type: 'string', label: '描述' },
    { name: 'nullable', type: 'boolean', label: '可以为空' },
  ],
  queryFields: [
    { name: 'columnName', type: 'string', label: '列名' },
  ],
};
