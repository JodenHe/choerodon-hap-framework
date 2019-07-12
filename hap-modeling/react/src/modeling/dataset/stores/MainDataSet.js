export default {
  primaryKey: 'resourceId',
  name: 'MetadataPage',
  pageSize: 10,
  autoQuery: true,
  fields: [
    { name: 'resourceId', type: 'number', label: 'ID' },
    { name: 'url', type: 'string', label: '代码', required: true },
    { name: 'name', type: 'string', label: '名称', required: true },
    { name: 'description', type: 'intl', label: '描述', required: true },
    { name: 'lockedBy', type: 'string', label: '被锁定' },
  ],
  queryFields: [
    { name: 'url', type: 'string', label: '代码' },
    { name: 'name', type: 'string', label: '名称' },
  ],
};
