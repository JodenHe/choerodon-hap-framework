import React, { Component } from 'react';
import { PREFIX } from '../../constants';
import CommandGroup from './CommandGroup';

export default class Commander extends Component {
  static defaultProps = {
    prefixCls: `${PREFIX}-table-commander`,
  };

  render() {
    const { prefixCls, dataSet, record } = this.props;
    return (
      <div className={prefixCls}>
        <label>行操作区域</label>
        <CommandGroup dataSet={dataSet} record={record} />
      </div>
    );
  }
}
