import React, { Component, createElement } from 'react';
import { inject } from 'mobx-react';
import { action } from 'mobx';
import { Icon } from 'choerodon-ui/pro';
import autobind from 'choerodon-ui/pro/lib/decorator/autobind';
import noop from 'lodash/noop';
import { getDataSetConfigById, recursiveRemove } from '../utils';
import { getDesigner } from './mapping';
import { PREFIX } from '../constants';

export default class BaseDesigner extends Component {
  wrapComponent(component) {
    return component;
  }

  @autobind
  handleRemove() {
    const { record, onRemove = noop } = this.props;
    recursiveRemove(record);
    onRemove(record);
  }

  renderRemoveButton() {
    return (
      <Icon
        key="delete"
        className={`${PREFIX}-close-btn`}
        type="cancel"
        onClick={this.handleRemove}
      />
    );
  }

  renderComponent(record) {
    const type = getDesigner(record.get('type'));
    if (type) {
      const dataSet = getDataSetConfigById(this.props.datasetsDS, record.get('dataSet'));
      return this.wrapComponent(createElement(type, {
        key: `designer-${record.id}`,
        dataSet,
        record,
      }), record);
    }
  }

  renderComponents(records) {
    return records && records.map(record => this.renderComponent(record));
  }
}
