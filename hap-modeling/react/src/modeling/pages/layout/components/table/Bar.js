import React, { Component } from 'react';
import { PREFIX } from '../../constants';
import ButtonGroup from './ButtonGroup';
import QueryBar from './QueryBar';

export default class Bar extends Component {
  static defaultProps = {
    prefixCls: `${PREFIX}-table-bar`,
  };

  render() {
    const { prefixCls, dataSet, record } = this.props;
    return (
      <div className={prefixCls}>
        <ButtonGroup dataSet={dataSet} record={record} />
        <QueryBar dataSet={dataSet} record={record} />
      </div>
    );
  }
}
