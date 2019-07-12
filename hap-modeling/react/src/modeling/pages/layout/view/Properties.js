import React, { Component, createElement } from 'react';
import { inject, observer } from 'mobx-react';
import { PREFIX } from '../constants';
import { getProperties } from '../components/mapping';
import { getDataSetConfigById } from '../utils';

@inject('componentsDS', 'datasetsDS')
@observer
export default class Properties extends Component {
  static defaultProps = {
    prefixCls: `${PREFIX}-properties`,
  };

  renderProperties(current) {
    const type = getProperties(current.get('type'));
    if (type) {
      const dataSet = getDataSetConfigById(
        this.props.datasetsDS,
        current.get('dataSet') || (current.parent && current.parent.get('dataSet')),
      );
      return type(current, dataSet);
    }
  }

  render() {
    const { prefixCls, componentsDS: { current } } = this.props;
    return current ? (
      <td className={prefixCls}>
        <div className={`${prefixCls}-content`}>
          <h2 className={`${prefixCls}-header`}>组件属性</h2>
          <div className={`${prefixCls}-body`}>
            {this.renderProperties(current)}
          </div>
        </div>
      </td>
    ) : null;
  }
}
