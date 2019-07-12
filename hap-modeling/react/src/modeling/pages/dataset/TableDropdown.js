import React, { Component } from 'react';
import { Menu, Spin } from 'choerodon-ui/pro';
import { observer } from 'mobx-react';
import DataSourceStore from './stores';

@observer
export default class TableDropdown extends Component {
  render() {
    if (DataSourceStore.relationTablesLoading) {
      return (
        <div style={{ boxShadow: '0 0.02rem 0.08rem rgba(0, 0, 0, .12)', padding: '3px 20px' }}><Spin /></div>
      );
    }
    if (!DataSourceStore.relationTables.length) {
      return (
        <Menu>
          <Menu.Item key="no-table">
            暂无关联表
          </Menu.Item>
        </Menu>
      );
    }
    return (
      <Menu onClick={this.props.handleAdd}>
        {
          DataSourceStore.relationTables.map(rt => (
            <Menu.Item key={rt.tableName}>
              <span>{rt.tableName}</span>
            </Menu.Item>
          ))
        }
      </Menu>
    );
  }
}
