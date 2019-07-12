export default {
  primaryKey: 'codeId',
  autoQuery: true,
  pageSize: 5,
  name: 'Code',
  selection: 'single',
  fields: [
    { name: 'codeId', type: 'number', label: '代码ID', required: true },
    { name: 'code', type: 'string', label: '代码' },
    { name: 'description', type: 'intl', label: '描述', required: true },
    { name: 'enabledFlag', type: 'boolean', label: '是否启用', defaultValue: 'Y', trueValue: 'Y', falseValue: 'N' },
    { name: 'parent', type: 'object', textField: 'description', label: '父级快码', lovCode: 'LOV_CODE_ID' },
    { name: 'parentCodeDescription', bind: 'parent.description', type: 'string', label: '描述' },
    { name: 'parentCodeId', bind: 'parent.codeId', type: 'string', label: '父级快码' },
  ],
  queryFields: [
    { name: 'code', type: 'string', label: '代码' },
    { name: 'description', type: 'string', label: '描述' },
  ],
};
