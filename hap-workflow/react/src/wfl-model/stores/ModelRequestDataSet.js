import { $l } from '@choerodon/boot';

export default {
  autoCreate: true,
  fields: [
    { name: 'name', type: 'string', label: $l('hap.name'), required: true },
    { name: 'key', type: 'string', label: $l('activiti.uniqueidentification'), required: true },
    { name: 'category', type: 'string', label: $l('activiti.category'), required: true, defaultValue: 'demo' },
    { name: 'version', type: 'number', label: 'version', defaultValue: 1 },
    { name: 'metaInfo', type: 'string', label: 'metaInfo' },
    { name: 'description', type: 'string', label: $l('hap.description') },
  ],
};
