import React, { Component } from 'react';
import { get } from 'mobx';
import { inject, observer } from 'mobx-react';
import uuid from 'uuid/v4';
import { Button, Dropdown, Icon, Menu } from 'choerodon-ui/pro';
import { pxToRem } from 'choerodon-ui/pro/lib/util/UnitConvertor';
import { PREFIX } from '../../constants';
import { isTree } from '../../utils';
import DropZone from '../../dnd/DropZone';
import BaseDesigner from '..';
import defaultProps from '../table-button/defaultProps';

@inject('dndStore', 'componentsDS')
@observer
export default class ButtonGroup extends BaseDesigner {
  static defaultProps = {
    prefixCls: `${PREFIX}-table-btn-group`,
  };

  handleButtonMenuClick = ({ key }) => {
    const { record, componentsDS } = this.props;
    const id = uuid();
    const parentId = record.get('id');
    const { props, ...otherProps } = defaultProps();
    if (key === 'custom') {
      props.children = '自定义按钮';
    }
    componentsDS.create({
      type: 'TableButton', id, parentId, method: key, props, ...otherProps,
    }, componentsDS.length);
  };

  getItems() {
    const { record: { children } } = this.props;
    return children.filter(child => child.get('type') === 'TableButton');
  }

  renderButtonMenus() {
    const { dataSet } = this.props;
    const { SubMenu, Item, ItemGroup } = Menu;
    const treeMenus = isTree(dataSet) && (
      <ItemGroup title="树形按钮">
        <Item key="expandAll"><Icon type="add_box" />展开</Item>
        <Item key="collapseAll"><Icon type="short_text" />合并</Item>
      </ItemGroup>
    );
    return (
      <Menu mode="vertical" onClick={this.handleButtonMenuClick}>
        <SubMenu title={<span><Icon type="widgets" /><span>内置按钮</span></span>}>
          <Item key="add"><Icon type="playlist_add" />新增</Item>
          <Item key="delete"><Icon type="delete" />删除</Item>
          <Item key="save"><Icon type="save" />保存</Item>
          <Item key="reset"><Icon type="undo" />重置</Item>
          <Item key="query"><Icon type="search" />查询</Item>
          <Item key="export"><Icon type="export" />导出</Item>
          {treeMenus}
        </SubMenu>
        <Item key="custom"><Icon type="filter_vintage" />自定义按钮</Item>
      </Menu>
    );
  }

  renderDragItem = (dropItemProps, dropIndex, child, index, width) => {
    dropItemProps.children = this.renderComponent(child);
    if (dropIndex !== -1 && index >= dropIndex) {
      dropItemProps.style = { transform: `translateX(${pxToRem(width || 100)})` };
    }
  };

  render() {
    const { prefixCls, record, dndStore: { dragItem } } = this.props;
    const children = this.getItems();
    const data = dragItem && get(dragItem, 'record');
    const type = dragItem && get(dragItem, 'type');
    const droppable = data && type && type === 'TableButton' && children.indexOf(data) !== -1;
    return (
      <div className={prefixCls}>
        {
          !!children.length && (
            <DropZone
              className={`${prefixCls}-buttons`}
              items={children}
              record={record}
              droppable={droppable}
              layout="horizontal"
              itemRenderer={this.renderDragItem}
            />
          )
        }
        <Dropdown overlay={this.renderButtonMenus()} trigger={['hover', 'click']}>
          <Button className={`${prefixCls}-new`} icon="add" color="blue"><span>添加按钮</span><Icon type="more_vert" /></Button>
        </Dropdown>
      </div>
    );
  }
}
