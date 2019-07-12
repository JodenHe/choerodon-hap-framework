export default {
  primaryKey: 'id',
  name: 'MetadataTable',
  pageSize: 10,
  autoQuery: true,
  fields: [
    { name: 'tableName', type: 'string', label: '表名', required: true },
    { name: 'multiLanguage', type: 'boolean', label: '多语言' },
    { name: 'description', type: 'string', label: '描述' },
    { name: 'lockedBy', type: 'string', label: '被锁定' },
  ],
  queryFields: [
    { name: 'tableName', type: 'string', label: '表名' },
  ],
};
