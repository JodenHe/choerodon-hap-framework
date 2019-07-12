import React, { Component } from 'react';
import { get } from 'mobx';
import { inject, observer } from 'mobx-react';
import uuid from 'uuid/v4';
import { Button, Dropdown, Icon, Menu } from 'choerodon-ui/pro';
import { pxToRem } from 'choerodon-ui/pro/lib/util/UnitConvertor';
import { PREFIX } from '../../constants';
import DropZone from '../../dnd/DropZone';
import BaseDesigner from '..';
import defaultProps from '../table-command/defaultProps';

@inject('dndStore', 'componentsDS')
@observer
export default class CommandGroup extends BaseDesigner {
  static defaultProps = {
    prefixCls: `${PREFIX}-table-btn-group`,
  };

  handleButtonMenuClick = ({ key }) => {
    const { record, componentsDS } = this.props;
    const id = uuid();
    const parentId = record.get('id');
    const { props, ...otherProps } = defaultProps();
    if (key === 'custom') {
      props.icon = 'mode_edit';
    }
    componentsDS.create({
      type: 'TableCommand', id, parentId, method: key, iconOnly: true, props, ...otherProps,
    }, componentsDS.length);
  };

  getItems() {
    const { record: { children } } = this.props;
    return children.filter(child => child.get('type') === 'TableCommand');
  }

  renderButtonMenus() {
    const { Item } = Menu;
    return (
      <Menu mode="vertical" onClick={this.handleButtonMenuClick}>
        <Item key="row_delete"><Icon type="delete" />删除按钮</Item>
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
    const droppable = data && type && type === 'TableCommand' && children.indexOf(data) !== -1;
    return (
      <div className={prefixCls}>
        <Dropdown overlay={this.renderButtonMenus()} trigger={['hover', 'click']}>
          <Button className={`${prefixCls}-new`} icon="add" color="blue"><span>添加按钮</span><Icon type="more_vert" /></Button>
        </Dropdown>
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
      </div>
    );
  }
}
