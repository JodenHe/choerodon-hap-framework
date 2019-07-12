import React, { isValidElement } from 'react';
import defaultTo from 'lodash/defaultTo';
import { Select } from 'choerodon-ui/pro';
import { getEditor } from './editorMapping';

const mapToRecord = ({ record }, key) => ({ record, name: `editor.${key}` });
const mapToField = ({ field }, key) => ({ record: field, name: key });

const properties = {
  required: ['Switch', '必输', mapToField],
  readOnly: ['Switch', '只读', mapToField],
  placeholder: ['TextField', '占位符', (props, key) => {
    const { record, field } = props;
    const { parent } = record;
    const placeholder = parent && parent.get('labelLayout') === 'placeholder'
      ? record.get('editor.label') || field.get('label') || field.get('name')
      : undefined;
    return { ...mapToRecord(props, key), placeholder };
  }],
  defaultValue: [null, '默认值', mapToField],
  defaultChecked: ['Switch', '默认选中', ({ field }) => {
    const checkedValue = defaultTo(field.get('trueValue'), true);

    function handleChange(value) {
      field.set('defaultValue', value ? checkedValue : undefined);
    }

    return { checked: field.get('defaultValue') === checkedValue, onChange: handleChange };
  }],
  trueValue: ['TextField', '选中值', (props, key) => {
    const { field } = props;

    function handleValueChange(value) {
      if (field.get('defaultValue')) {
        field.set('defaultValue', defaultTo(value, true));
      }
    }

    return { onChange: handleValueChange, ...mapToField(props, key) };
  }],
  falseValue: ['TextField', '未选中值', mapToField],
  maxLength: ['NumberField', '最大长度', mapToField, { step: 1, min: 1 }],
  minLength: ['NumberField', '最小长度', mapToField, { step: 1, min: 0 }],
  pattern: ['TextField', '正则校验', mapToField],
  max: [null, '最大值', mapToField],
  min: [null, '最小值', mapToField],
  step: ['NumberField', '步距', mapToField],
  currency: ['TextField', '货币代码', mapToField],
  format: ['TextField', '日期格式', mapToField],
  multiple: ['Switch', '是否为多值', mapToField],
  lovCode: ['Lov', 'Lov代码', mapToField],
  lookupCode: ['Lov', '值列表代码', mapToField],
  lookupUrl: ['TextField', '值列表URL', mapToField],
  value: ['TextField', '选中值', (props, key) => {
    const { record, field } = props;

    function handleValueChange(value) {
      if (field.get('defaultValue')) {
        field.set('defaultValue', defaultTo(value, true));
      }
    }

    return { ...mapToRecord(props, key), onChange: handleValueChange };
  }],
  clearButton: ['Switch', '清除按钮', mapToRecord],
  reveal: ['Switch', '是否可揭示', mapToRecord],
  cols: ['NumberField', '列数', mapToRecord, { step: 1, min: 1 }],
  rows: ['NumberField', '行数', mapToRecord, { step: 1, min: 1 }],
  resize: ['Select', '可否调整大小', (props, key) => {
    const { Option } = Select;
    return {
      ...mapToRecord(props, key),
      children: [
        <Option key="none" value="none">禁用</Option>,
        <Option key="both" value="both">可用</Option>,
        <Option key="vertical" value="vertical">纵向可用</Option>,
        <Option key="horizontal" value="horizontal">横向可用</Option>,
      ],
    };
  }],
  vertical: ['Switch', '是否垂直布局', mapToRecord],
};

export default function getProperties(keys, recordAndField) {
  return keys.map((key) => {
    if (typeof key === 'function') {
      return key(recordAndField);
    } else {
      const [cmp, label, dynamicProps, props] = properties[key];
      const { type } = recordAndField;
      const Cmp = getEditor(cmp || (type === 'IntlField' ? 'TextField' : type)).editor;
      const otherProps = dynamicProps(recordAndField, key);
      return <Cmp key={key} label={label} {...otherProps} {...props} />;
    }
  });
}
