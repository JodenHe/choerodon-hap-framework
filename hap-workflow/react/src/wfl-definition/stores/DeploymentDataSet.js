export default {
  fields: [
    { name: 'id', type: 'string', label: 'ID' },
    { name: 'name', type: 'string', label: '名称' },
    { name: 'deploymentTime', type: 'moment', label: '发布时间' },
    { name: 'category', type: 'string', label: '标识' },
    { name: 'url', type: 'string', label: 'URL' },
    { name: 'tenantId', type: 'string', label: 'tenantId' },
  ],
};
