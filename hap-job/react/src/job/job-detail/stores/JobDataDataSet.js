import { $l } from '@choerodon/boot';

function dynamicFieldProperties({ dataSet, record, name }) {
  if (name === 'job_internal_email_template') {
    return {
      textFiled: 'templateCode',
      lovCode: 'MESSAGE_TEMPLATE',
    };
  }
}

export default {
  autoQuery: false,
  fields: [
    { name: 'name', type: 'string', label: $l('job.attributename') },
    { name: 'value', type: 'string', label: $l('job.attributevalue'), dynamicProps: dynamicFieldProperties },
    { name: 'template', type: 'object', label: $l('job.infodetail.jobinfo'), textField: 'templateCode', lovCode: 'MESSAGE_TEMPLATE' },
  ],
  events: {
    update: ({ dataSet, record, name, value, oldValue }) => {
      if (name === 'template') {
        record.set('value', value.templateCode);
      }
    },
  },
};
