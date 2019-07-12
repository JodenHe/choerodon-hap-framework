import React, { Component } from 'react';
import noop from 'lodash/noop';
import { Row } from 'choerodon-ui/pro';
import { PREFIX } from '../constants';
import RulerPointer from './RulerPointer';

export default class Ruler extends Component {
  static defaultProps = {
    prefixCls: `${PREFIX}-ruler`,
  };

  handlePointerChange = (value, index) => {
    const { values, onChange = noop } = this.props;
    values[index] = value;
    onChange(values);
  };

  renderPoints() {
    const { records, values, minWidth, maxWidth, columns } = this.props;
    if (records) {
      return records.map(record => (
        <RulerPointer key={record.id} record={record} />
      ));
    } else if (values) {
      return values.map((value, index) => (
        <RulerPointer
          span={24 / columns}
          key={`rule-point-${index}`}
          value={value}
          index={index}
          onChange={this.handlePointerChange}
          minWidth={minWidth}
          maxWidth={maxWidth}
        />
      ));
    }
  }

  render() {
    const { prefixCls, columns } = this.props;
    const Cmp = columns ? Row : 'div';
    return (
      <Cmp className={prefixCls}>
        {this.renderPoints()}
      </Cmp>
    );
  }
}
