import React, { Children, Component, isValidElement } from 'react';
import { inject } from 'mobx-react';
import { Row } from 'choerodon-ui/pro';
import omit from 'lodash/omit';
import BaseDesigner from '../index';
import ColDesigner from '../col/Designer';

@inject('datasetsDS')
export default class RowDesigner extends BaseDesigner {
  wrapComponent(component, record) {
    return (
      <ColDesigner record={record} key={`designer-${record.id}`}>
        {component}
      </ColDesigner>
    );
  }

  render() {
    const { record, ...restProps } = this.props;
    return (
      <Row {...omit(restProps, ['dataSet', 'datasetsDS'])}>
        {this.renderComponents(record.children)}
      </Row>
    );
  }
}
