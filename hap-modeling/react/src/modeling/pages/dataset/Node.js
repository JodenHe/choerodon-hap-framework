import React, { Component } from 'react';
import uuid from 'uuid/v4';
import classNames from 'classnames';
import { observer } from 'mobx-react';
import { Dropdown, Icon, Menu, Spin } from 'choerodon-ui/pro';
import DataSourceStore from './stores';
import TableDropdown from './TableDropdown';

@observer
export default class TableNode extends Component {
  handleOnClick = () => {
    const { onClick, node } = this.props;
    if (onClick) {
      onClick(node);
    }
  };

  handleClickAdd = (hidden) => {
    if (!hidden) {
      const { node } = this.props;
      DataSourceStore.loadRelationTables(node.get('tableKey'));
    }
  };

  handleAdd = ({ key }) => {
    const { tableDS, node } = this.props;
    const checkIsExist = tableDS.find(r => r.get('tableKey') === key && r.get('parentKey') === node.get('tableKey'));
    const addTable = DataSourceStore.relationTables.find(talbe => talbe.tableName === key);
    if (checkIsExist || !addTable) {
      return;
    }
    const id = uuid();
    const currentRecordId = tableDS.current.get('id');
    const obj = {
      tableKey: key,
      tableName: key,
      parentKey: node.get('tableKey'),
      masterColumnName: addTable.masterColumnName,
      relationColumnName: addTable.relationColumnName,
      id,
    };
    tableDS.create(obj);
    const index = tableDS.findIndex(r => r.get('id') === currentRecordId);
    if (index !== -1) {
      tableDS.locate(index);
    }
  };

  handleDelete = (e) => {
    e.stopPropagation();
    const { tableDS, node, parentNode } = this.props;
    const record = tableDS.find(r => r.get('tableKey') === node.get('tableKey'));
    const parentIndex = tableDS.findIndex(r => r.get('tableKey') === parentNode.get('tableKey'));
    if (parentIndex !== -1 && record) {
      tableDS.locate(parentIndex);
      tableDS.remove(record);
    }
  };

  renderDropdown() {
    return <TableDropdown handleAdd={this.handleAdd} />;
  }

  render() {
    const { node, isMain, isActive } = this.props;
    const className = classNames('tree-node-inner', {
      'tree-node-inner-active': isActive,
    });
    const mainIcon = isMain ? (
      <div className="primary">
        <div className="radius">
          主
        </div>
      </div>
    ) : null;

    return (
      <div
        role="none"
        className={className}
        onClick={this.handleOnClick}
      >
        {mainIcon}
        <div style={{ overflow: 'hidden' }}>
          <div className="title" style={{ lineHeight: '36px' }}>{node.get('tableName')}</div>
          {/* <div className="sub-title">{node.get('tableKey')}</div> */}
        </div>
        <div className="graph-tools">
          <Dropdown
            onHiddenChange={this.handleClickAdd}
            overlay={this.renderDropdown()}
            trigger={['click']}
          >
            <Icon type="add_circle" />
          </Dropdown>
          {!isMain && (!node.children || !node.children.length) && <Icon type="remove_circle" onClick={this.handleDelete} />}
        </div>
        {!isMain && node.get('join') !== 'left' && <i className="inner-join" />}
        {!isMain && node.get('join') === 'left' && <i className="left-join" />}
      </div>
    );
  }
}
