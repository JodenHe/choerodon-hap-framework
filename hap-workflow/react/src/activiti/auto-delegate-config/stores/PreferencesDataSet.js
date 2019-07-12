const PREFERENCES_MAP = {
  deliver_start_date: 'deliverStartDate',
  deliver_end_date: 'deliverEndDate',
  auto_deliver: 'autoDelegate',
};

function dynamicFieldProperties({ dataSet, record, name }) {
  if (record.get('preferences') === PREFERENCES_MAP.deliver_end_date || record.get('preferences') === PREFERENCES_MAP.deliver_start_date) {
    return { type: 'dateTime', format: 'YYYY-MM-DD HH:mm:ss' };
  } else if (record.get('preferences') === PREFERENCES_MAP.auto_deliver) {
    return {
      type: 'string',
      lovCode: 'LOV_ACT_EMPLOYEE',
    };
  }
}

export default {
  queryUrl: '/sys/preferences/queryPreferences',
  autoQuery: true,
  paging: false,

  fields: [
    { name: 'userId', type: 'number' },
    { name: 'preferences', type: 'string' },
    { name: 'preferencesId', type: 'number' },
    { name: 'preferencesValue', dynamicProps: dynamicFieldProperties },
    { name: '_token', type: 'string' },
    { name: 'objectVersionNumber', type: 'number' },
  ],
  events: {
    load: ({ dataSet }) => {
      const dataList = dataSet.data;
      if (dataList && dataList.findIndex(r => r.get('preferences') === PREFERENCES_MAP.deliver_start_date) === -1) {
        dataSet.create({ preferences: PREFERENCES_MAP.deliver_start_date, preferencesValue: undefined });
      }
      if (dataList && dataList.findIndex(r => r.get('preferences') === PREFERENCES_MAP.deliver_end_date) === -1) {
        dataSet.create({ preferences: PREFERENCES_MAP.deliver_end_date, preferencesValue: undefined });
      }
      if (dataList && dataList.findIndex(r => r.get('preferences') === PREFERENCES_MAP.auto_deliver) === -1) {
        dataSet.create({ preferences: PREFERENCES_MAP.auto_deliver, preferencesValue: undefined });
      }
    },
  },
};
