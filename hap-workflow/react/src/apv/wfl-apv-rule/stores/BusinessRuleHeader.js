import { $l } from '@choerodon/boot';

export default {
  autoQuery: true,
  primaryKey: 'businessRuleId',
  pageSize: 10,
  name: 'BusinessRuleHeader',
  fields: [
    { name: 'businessRuleId', type: 'number' },
    { name: 'code', type: 'string', label: $l('businessruleheader.code'), required: true, unique: true },
    { name: 'description', type: 'string', label: $l('businessruleheader.description'), required: true },
    { name: 'startActiveDate', type: 'date', label: $l('user.startactivedate') },
    { name: 'endActiveDate', type: 'date', label: $l('user.endactivedate') },
    { name: 'enableFlag', type: 'boolean', label: $l('hap.enableflag'), defaultValue: 'Y', trueValue: 'Y', falseValue: 'N', required: true },
  ],
  queryFields: [
    { name: 'code', type: 'string', label: $l('businessruleheader.code') },
    { name: 'description', type: 'string', label: $l('businessruleheader.description') },
  ],
};
