import React from 'react';
import { toJS } from 'mobx';
import { inject, observer } from 'mobx-react';
import isString from 'lodash/isString';
import omit from 'lodash/omit';
import defaultTo from 'lodash/defaultTo';
import { CheckBox, Radio, SelectBox, Stores } from 'choerodon-ui/pro';
import { getLookupUrl } from 'choerodon-ui/pro/lib/data-set/utils';
import BaseDesigner from '../index';
import DragZone from '../../dnd/DragZone';
import { PREFIX } from '../../constants';
import { getEditorByRecordAndField } from '../../editors/editorMapping';
import { getQueryFieldConfigByName } from '../../utils';

const { LookupCodeStore, LovCodeStore } = Stores;

function getLookupKey(field) {
  const type = field.get('type');
  const lovCode = field.get('lovCode');
  const lookupUrl = field.get('lookupUrl') || getConfig('lookupUrl');
  const lookupCode = field.get('lookupCode');
  if (typeof lookupUrl === 'function' && lookupCode) {
    return lookupUrl(lookupCode);
  } else if (isString(lookupUrl)) {
    return lookupUrl;
  }
  if (lovCode && type !== 'object') {
    return LovCodeStore.getQueryUrl(lovCode);
  }
}

@inject('dndStore', 'datasetsDS')
@observer
export default class TableQueryFieldDesigner extends BaseDesigner {
  static defaultProps = {
    prefixCls: `${PREFIX}-table-query-field`,
  };

  render() {
    const { record, dataSet, prefixCls } = this.props;
    const name = record.get('props.name');
    const field = getQueryFieldConfigByName(dataSet, name);
    const label = record.get('props.label') || field.get('label');
    const Field = getEditorByRecordAndField({ record, field }).editor;
    const props = omit(record.get('editor'), ['type']);
    props.placeholder = label || name;
    const defaultValue = field.get('defaultValue');
    if (Field === Radio) {
      props.checked = defaultValue && defaultValue === defaultTo(props.value, true);
    } else if (Field === CheckBox) {
      props.checked = defaultValue && defaultValue === defaultTo(field.get('trueValue'), true);
    } else if (Field === SelectBox) {
      const lookupKey = getLookupKey(field);
      if (lookupKey) {
        const lookupData = LookupCodeStore.get(lookupKey);
        if (lookupData) {
          const { Option } = SelectBox;
          const textField = field.get('textField') || 'meaning';
          const valueField = field.get('valueField') || 'value';
          props.children = lookupData.map(data => <Option value={data[valueField]}>{data[textField]}</Option>);
        } else {
          LookupCodeStore.fetchLookupData(lookupKey);
        }
      }
    } else {
      props.value = defaultValue;
    }
    return (
      <DragZone
        type={record.get('type')}
        record={record}
        className={prefixCls}
      >
        <div className={`${prefixCls}-wrapper`}>
          <Field className={prefixCls} {...props} style={{ width: 150 }} tabIndex={-1} />
        </div>
      </DragZone>
    );
  }
}
