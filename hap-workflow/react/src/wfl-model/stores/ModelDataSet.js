import { $l } from '@choerodon/boot';

export default {
  name: 'BaseModel',
  autoQuery: true,
  selection: false,
  pageSize: 20,
  fields: [
    { name: 'name', type: 'string', label: $l('activiti.name') },
    { name: 'key', type: 'string', label: $l('activiti.code') },
    { name: 'id', type: 'number', label: 'ID' },
    { name: 'category', type: 'string', label: $l('activiti.category') },
    { name: 'version', type: 'number', label: '' },
    { name: 'metaInfo', type: 'string', label: '' },
    { name: 'tenantId', type: 'string', label: '' },
    { name: 'url', type: 'string', label: 'URL' },
    { name: 'sourceUrl', type: 'string', label: '' },
    { name: 'sourceExtraUrl', type: 'string', label: '' },
    { name: 'deploymentId', type: 'string', label: '' },
    { name: 'deploymentUrl', type: 'string', label: '' },
    { name: 'createTime', type: 'dateTime', label: $l('activiti.createtime') },
    { name: 'lastUpdateTime', type: 'dateTime', label: $l('activiti.lastupdatetime') },
  ],
  queryFields: [
    { name: 'name', type: 'string', label: $l('activiti.name') },
  ],
};
