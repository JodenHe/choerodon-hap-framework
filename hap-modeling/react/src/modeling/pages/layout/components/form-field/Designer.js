import React from 'react';
import { toJS } from 'mobx';
import { inject, observer } from 'mobx-react';
import classNames from 'classnames';
import isString from 'lodash/isString';
import omit from 'lodash/omit';
import defaultTo from 'lodash/defaultTo';
import { CheckBox, Dropdown, Icon, Menu, Radio, SelectBox, Stores } from 'choerodon-ui/pro';
import { normalizeLabelWidth } from 'choerodon-ui/pro/lib/form/utils';
import { getConfig } from 'choerodon-ui/pro/lib/configure';
import BaseDesigner from '..';
import { getFieldConfigByName } from '../../utils';
import DragZone from '../../dnd/DragZone';
import { PREFIX } from '../../constants';
import { getEditorByRecordAndField } from '../../editors/editorMapping';

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
export default class FormFieldDesigner extends BaseDesigner {
  static defaultProps = {
    prefixCls: `${PREFIX}-form-field`,
  };

  state = {
    menuHidden: true,
  };

  handleMenuHiddenChange = (menuHidden) => {
    this.setState({ menuHidden });
  };

  handleMenuClick = ({ key }) => {
    const { record } = this.props;
    if (key === 'cancel') {
      record.set('props.colSpan', 1);
      record.set('props.rowSpan', 1);
    } else {
      const span = record.get(key) || 1;
      record.set(key, span + 1);
    }
  };

  renderMenu() {
    const { record, record: { parent }, colCount } = this.props;
    const columns = parent.get('columns');
    const colSpan = record.get('colSpan') || 1;
    const rowSpan = record.get('rowSpan') || 1;
    return (
      <Menu onClick={this.handleMenuClick}>
        <Menu.Item key="props.colSpan" disabled={colCount + colSpan === columns}>
          <Icon type="border_vertical" /> 向右合并
        </Menu.Item>
        {/* <Menu.Item key="props.rowSpan"> */}
        {/* <Icon type="border_horizontal" /> 向下合并 */}
        {/* </Menu.Item> */}
        <Menu.Item key="cancel" disabled={colSpan === 1 && rowSpan === 1}>
          <Icon type="block" /> 取消合并
        </Menu.Item>
      </Menu>
    );
  }

  getButtons() {
    const { prefixCls } = this.props;
    return [
      <Dropdown
        overlay={this.renderMenu()}
        trigger={['hover', 'click']}
        key="menu"
        hidden={this.state.menuHidden}
        onHiddenChange={this.handleMenuHiddenChange}
      >
        <span className={`${prefixCls}-menu`}>
          <Icon type="menu" />
        </span>
      </Dropdown>,
      this.renderRemoveButton(),
    ];
  }

  render() {
    const { record, dataSet, prefixCls, colCount } = this.props;
    const { parent } = record;
    if (!parent) {
      return null;
    }
    const labelLayout = parent.get('props.labelLayout');
    const labelAlign = parent.get('props.labelAlign');
    const columns = parent.get('props.columns');
    const labelWidth = normalizeLabelWidth(parent.get('props.labelWidth'), columns);
    const name = record.get('props.name');
    const field = getFieldConfigByName(dataSet, name);
    const label = record.get('props.label') || field.get('label');
    const width = labelWidth[colCount];
    const classString = classNames(`${prefixCls}-label`, {
      [`${prefixCls}-label-${labelAlign}`]: labelAlign,
      [`${prefixCls}-required`]: field.get('required'),
    });
    const labelNode = (
      <label className={labelLayout === 'vertical' ? classString : undefined}>
        {label || <span className={`${prefixCls}-empty-label`}>{name}</span>}
      </label>
    );
    const Field = getEditorByRecordAndField({ record, field }).editor;
    const props = omit(record.get('editor'), ['type']);
    if (!props.placeholder && labelLayout === 'placeholder') {
      props.placeholder = label || name;
    }
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
        buttons={this.getButtons()}
        hover={!this.state.menuHidden}
      >
        <table>
          <tbody>
            <tr>
              {labelLayout === 'horizontal' && <td className={classString} style={{ width }}>{labelNode}</td>}
              <td>
                {
                  labelLayout === 'vertical' && labelNode
                }
                <div className={`${prefixCls}-wrapper`}>
                  <Field className={prefixCls} {...props} />
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </DragZone>
    );
  }
}
