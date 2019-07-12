import { $l } from '@choerodon/boot';

export default {
  queryUrl: '/wfl/vacation/query',
  autoQuery: true,
  paging: false,
  selection: false,
  fields: [
    { name: 'id', type: 'number' },
    { name: 'userCode', type: 'string' },
    { name: 'startDate', type: 'date', label: $l('demovacation.startdate'), required: true },
    { name: 'endTime_', type: 'date' },
    { name: 'needDays', type: 'number', label: $l('demovacation.needdays'), required: true },
    { name: 'leaveReason', type: 'string', label: $l('demovacation.leavereason') },
  ],
};
