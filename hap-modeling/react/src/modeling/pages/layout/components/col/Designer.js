import React, { Children, isValidElement } from 'react';
import { inject } from 'mobx-react';
import { Col } from 'choerodon-ui/pro';
import BaseDesigner from '../index';

@inject('dndStore', 'datasetsDS')
export default class ColDesigner extends BaseDesigner {
  render() {
    const { record, children } = this.props;
    return (
      <Col span={record.get('span')}>
        {children}
      </Col>
    );
  }
}
