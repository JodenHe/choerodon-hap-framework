import { $l } from '@choerodon/boot';

export default {
  autoQuery: true,
  primaryKey: 'candidateRuleId',
  pageSize: 20,
  name: 'ApproveCandidateRule',
  fields: [
    { name: 'candidateRuleId', type: 'number' },
    { name: 'code', type: 'string', label: $l('approvecandidaterule.code'), required: true, unique: true },
    { name: 'description', type: 'string', label: $l('approvecandidaterule.description'), required: true },
    { name: 'expression', type: 'string', label: $l('approvecandidaterule.expression'), required: true },
    { name: 'enableFlag', type: 'boolean', label: $l('hap.enableflag'), defaultValue: 'Y', trueValue: 'Y', falseValue: 'N', required: true },
  ],
  queryFields: [
    { name: 'code', type: 'string', label: $l('approvecandidaterule.code') },
    { name: 'description', type: 'string', label: $l('approvecandidaterule.description') },
  ],
};
