import React, { cloneElement } from 'react';
import { action, get } from 'mobx';
import { inject, observer } from 'mobx-react';
import { Button } from 'choerodon-ui/pro';
import { $l } from 'choerodon-ui/pro/lib/locale-context';
import BaseDesigner from '../index';
import { PREFIX } from '../../constants';
import DragZone from '../../dnd/DragZone';

@observer
export default class ButtonDesigner extends BaseDesigner {
  static defaultProps = {
    prefixCls: `${PREFIX}-table-button`,
  };

  getCustomProps() {
    const { record } = this.props;
    const icon = record.get('props.icon');
    const children = record.get('props.children');
    const funcType = record.get('props.funcType');
    const color = record.get('props.color');
    const props = {};
    if (icon) {
      props.icon = icon;
    }
    if (funcType) {
      props.funcType = funcType;
    }
    if (color) {
      props.color = color;
    }
    if ((icon || record.get('method') !== 'custom') && record.get('iconOnly')) {
      props.children = undefined;
    } else if (children) {
      props.children = children;
    }
    return props;
  }

  getButtonProps() {
    const { record } = this.props;
    switch (record.get('method')) {
      case 'add':
        return { icon: 'playlist_add', children: $l('Table', 'create_button') };
      case 'save':
        return { icon: 'save', children: $l('Table', 'save_button'), type: 'submit' };
      case 'row_delete':
        return { icon: 'delete', funcType: 'flat' };
      case 'delete':
        return { icon: 'delete', children: $l('Table', 'delete_button') };
      case 'remove':
        return { icon: 'remove_circle', children: $l('Table', 'remove_button') };
      case 'reset':
        return { icon: 'undo', children: $l('Table', 'reset_button'), type: 'reset' };
      case 'query':
        return { icon: 'search', children: $l('Table', 'query_button') };
      case 'export':
        return { icon: 'export', children: $l('Table', 'export_button') };
      case 'expandAll':
        return { icon: 'add_box', children: $l('Table', 'expand_button') };
      case 'collapseAll':
        return { icon: 'short_text', children: $l('Table', 'collapse_button') };
      default:
    }
  }

  render() {
    const { record, prefixCls } = this.props;
    return (
      <DragZone
        type={record.get('type')}
        record={record}
        className={prefixCls}
        buttons={this.renderRemoveButton()}
      >
        <Button style={{ display: 'block' }} tabIndex={-1} {...this.getButtonProps()} {...this.getCustomProps()} />
      </DragZone>
    );
  }
}
