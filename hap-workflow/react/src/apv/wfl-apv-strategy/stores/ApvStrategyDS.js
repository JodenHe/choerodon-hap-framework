import { $l } from '@choerodon/boot';

export default {
  autoQuery: true,
  primaryKey: 'approveStrategyId',
  pageSize: 20,
  name: 'ApproveStrategy',
  fields: [
    { name: 'approveStrategyId', type: 'number' },
    { name: 'code', type: 'string', label: $l('approvestrategy.code'), required: true, unique: true },
    { name: 'description', type: 'string', label: $l('approvestrategy.description'), required: true },
    { name: 'condition', type: 'string', label: $l('approvestrategy.condition'), required: true },
    { name: 'enableFlag', type: 'boolean', label: $l('hap.enableflag'), defaultValue: 'Y', trueValue: 'Y', falseValue: 'N', required: true },
  ],
  queryFields: [
    { name: 'code', type: 'string', label: $l('approvestrategy.code') },
    { name: 'description', type: 'string', label: $l('approvestrategy.description') },
  ],
};
