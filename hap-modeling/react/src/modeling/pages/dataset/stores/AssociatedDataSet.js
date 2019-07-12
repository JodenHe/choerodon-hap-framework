export default {
  autoCreate: true,
  fields: [
    { name: 'field', type: 'string', label: '请选择' },
    { name: 'operation', type: 'string', label: '操作符' },
    { name: 'type', type: 'string', label: '类型' },
    { name: 'value', type: 'string', label: '文本值' },
    { name: 'valueTable', type: 'string' },
    { name: 'table', type: 'string' },
  ],
};
