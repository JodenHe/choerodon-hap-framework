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
    { name: 'lockedBy', type: 'string', label: '被锁定', help: '当页面被锁定后，只能由锁定人修改！' },
  ],
  queryFields: [
    { name: 'name', type: 'string', label: '名称' },
  ],
};
