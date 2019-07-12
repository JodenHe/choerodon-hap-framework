export default {
  fields: [
    { name: 'userId', type: 'string', defaultValue: 'Jessen' },
    { name: 'businessKey', type: 'string', defaultValue: 104, required: true },
    { name: 'processDefinitionId', type: 'string', textField: 'name', valueField: 'id', required: true },
    { name: 'variables', type: 'object' },
  ],
};
