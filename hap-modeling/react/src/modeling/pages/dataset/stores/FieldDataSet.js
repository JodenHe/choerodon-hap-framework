const uniqueNameValidator = (value, name, record, dataSet) => {
  const recordsWithName = record.dataSet.filter(r => r.get('name') === value);
  if (recordsWithName.length === 1) {
    return true;
  }
  return '相同name已存在，为了更好地定义LOV类型的字段，请保证name互不相同！';
};

export default {
  paging: false,
  fields: [
    { name: 'name', type: 'string', label: '字段名', required: true, validator: uniqueNameValidator, help: '字段名用于DataSet唯一实别，保证唯一性且主表不可修改' },
    { name: 'columnName', type: 'string', label: '【表名】- 列名', required: true },
    { name: 'type', type: 'string', label: '字段类型', lookupCode: 'MODEL.DATASET.FIELD.TYPE', required: true, defaultValue: 'string' },
    { name: 'label', type: 'string', label: '字段标签' },
    { name: 'required', type: 'boolean', label: '是否必选' },
    { name: 'bind', type: 'string', label: '别名绑定' },

    { name: 'unique', type: 'boolean', label: '是否唯一', defaultValue: false },
    { name: 'uniqueString', type: 'string', label: '唯一索引组名' },

    { name: 'order', type: 'string', label: '排序类型', lookupCode: 'MODEL.DATASET.FIELD.ORDER' },
    { name: 'format', type: 'string', label: '日期格式化' },
    { name: 'requiredMessage', type: 'string', label: '必选校验错误信息' },
    { name: 'readOnly', type: 'boolean', label: '是否只读', defaultValue: false },
    { name: 'pattern', type: 'string', label: '正则校验' },
    { name: 'group', type: 'number', label: '是否分组' },
    { name: 'defaultValue', label: '默认值' },
    { name: 'multiple', type: 'boolean', label: '是否为值数组', defaultValue: false },
    { name: 'maxLength', type: 'number', label: '最大长度' },
    { name: 'minLength', type: 'number', label: '最小长度' },
    { name: 'max', type: 'number', label: '最大值' },
    { name: 'min', type: 'number', label: '最小值' },
    { name: 'step', type: 'number', label: '步距' },
    { name: 'textField', type: 'string', label: '显示的字段名' },
    { name: 'valueField', type: 'string', label: '值字段' },
    // { name: 'trueValue', type: 'boolean', label: 'trueValue' },
    // { name: 'falseValue', type: 'boolean', label: 'falseValue' },
    { name: 'lookupCode', type: 'string', label: '值列表代码', lovCode: 'LOV_CODE' },

    { name: 'operation', type: 'string', label: '后端操作符', lookupCode: 'MODEL.DATASET.OPERATION', defaultValue: 'equals' },
    { name: 'tableKey', type: 'string', label: '' },


    // ---------------lov---------------
    { name: 'lovCode', type: 'string', label: 'LOV配置代码', lovCode: 'LOV_LIST' },
    { name: 'description', type: 'string', label: '描述' },
    { name: 'field', type: 'string', label: '包含属性' },
  ],
};
