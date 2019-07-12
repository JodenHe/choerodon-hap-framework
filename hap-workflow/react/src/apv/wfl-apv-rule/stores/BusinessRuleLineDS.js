import { $l } from '@choerodon/boot';

export default {
  autoQuery: true,
  primaryKey: 'businessRuleLineId',
  pageSize: 10,
  name: 'BusinessRuleLine',
  fields: [
    { name: 'businessRuleLineId', type: 'number', label: 'LineId' },
    { name: 'businessRuleId', type: 'number', label: 'headerId' },
    { name: 'description', type: 'string', label: $l('businessruleline.description'), required: true },
    { name: 'conditions', type: 'string', label: $l('businessruleline.conditions'), required: true },
    { name: 'enableFlag', type: 'boolean', label: $l('hap.enableflag'), defaultValue: 'Y', trueValue: 'Y', falseValue: 'N', required: true },
  ],
};
