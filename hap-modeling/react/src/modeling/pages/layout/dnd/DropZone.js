import React, { Children, cloneElement, Component, createElement, isValidElement } from 'react';
import { findDOMNode } from 'react-dom';
import { action } from 'mobx';
import { inject, observer } from 'mobx-react';
import { Modal } from 'choerodon-ui/pro';
import { stopPropagation } from 'choerodon-ui/pro/lib/util/EventManager';
import omit from 'lodash/omit';
import noop from 'lodash/noop';
import classNames from 'classnames';
import uuid from 'uuid/v4';
import ComponentsModal from '../view/ComponentsModal';
import { PREFIX } from '../constants';
import { getDefaultProps } from '../components/mapping';
import DropItem from './DropItem';
import { getEditorByField } from '../editors/editorMapping';

@inject('dndStore', 'componentsDS')
@observer
export default class DropZone extends Component {
  static defaultProps = {
    prefixCls: `${PREFIX}-drop-zone`,
  };

  state = {
    dropIndex: -1,
  };

  handleComponentSelect = action((type, { record }) => {
    const dataSet = record.get('datasetId');
    const fields = record.getCascadeRecords('fields');
    const row = this.createElement('Row', {}, record);
    const component = this.createElement(type, { dataSet, parentId: row.get('id'), span: 24 }, record);
    if (fields) {
      const id = component.get('id');
      fields.forEach(field => (
        this.createElement(`${type}Item`, {
          parentId: id,
          props: { name: field.get('name') },
          editor: type === 'Form' ? { type: getEditorByField(field) } : undefined,
        }, record)
      ));
    }
    component.dataSet.current = component;
  });

  handleMouseUp = (e) => {
    if (e.button === 0) {
      stopPropagation(e);
      const { dropIndex } = this.state;
      if (dropIndex !== -1) {
        const { dndStore, dndStore: { dragItem, dragItem: { record: dragData } }, componentsDS, record, onDrop = noop } = this.props;
        if (dragItem.type === 'DataSet') {
          dndStore.setWaiter(new Promise((resolve) => {
            Modal.open({
              title: '选择数据组件',
              style: { width: 300 },
              children: (
                <ComponentsModal dragItem={dragItem} onSelect={this.handleComponentSelect} />
              ),
              onClose: () => {
                this.setDropIndex(-1);
                resolve();
              },
            });
          }));
        } else if (dragItem.type === 'Field') {
          dndStore.setWaiter(Promise.resolve());
          const dataSet = dragData.cascadeParent;
          const props = { parentId: record.get('id'), props: { name: dragData.get('name') } };
          const dsId = dataSet.get('datasetId');
          if (record.get('dataSet') !== dsId) {
            props.dataSet = dsId;
          }
          this.createElement(`${record.get('type')}Item`, props, dataSet);
          this.setDropIndex(-1);
        } else {
          dndStore.setWaiter(Promise.resolve());
          if (onDrop(dragData, dropIndex) !== false) {
            const fromData = this.getRecordByDropIndex(dropIndex);
            if (fromData !== dragData) {
              if (fromData) {
                const fromIndex = componentsDS.indexOf(fromData);
                componentsDS.splice(fromIndex, 0, dragData);
              } else {
                componentsDS.push(dragData);
              }
            }
          }
          this.setDropIndex(-1);
        }
      }
    }
  };

  handleMouseLeave = () => {
    this.setDropIndex(-1);
  };

  handleDropItemEnter = (dropIndex) => {
    this.setDropIndex(dropIndex);
  };

  handleDropItemDown = (dropIndex) => {
    this.setDropIndex(dropIndex);
  };

  setDropIndex(dropIndex) {
    if (dropIndex !== this.state.dropIndex) {
      this.setState({ dropIndex });
    }
  }

  getRecordByDropIndex(dropIndex) {
    const { items } = this.props;
    if (dropIndex !== -1) {
      return items && items[dropIndex];
    }
  }

  createElement(type, props, dataSet) {
    const { componentsDS } = this.props;
    const { dropIndex } = this.state;
    const id = uuid();
    const record = this.getRecordByDropIndex(dropIndex);
    const { props: defaultProps, ...otherDefaultProps } = getDefaultProps(type, dataSet) || {};
    return componentsDS.create({
      type, id, ...props, props: { ...defaultProps, ...props.props }, ...otherDefaultProps,
    }, record ? componentsDS.indexOf(record) : componentsDS.length);
  }

  renderChildren() {
    const {
      items = [], droppable, layout, emptyText, prefixCls, dndStore: { dragItem, width, height },
      itemRenderer = noop, beforeRender = noop,
    } = this.props;
    const { dropIndex } = this.state;
    const result = [];
    const commonProps = {
      droppable: droppable && layout,
      className: `${prefixCls}-item`,
    };
    const childList = items.slice();
    const { record } = dragItem || {};
    if (record) {
      const index = childList.indexOf(record);
      if (index !== -1) {
        childList.splice(index, 1);
        const hiddenProps = {
          ...commonProps,
          key: `drop-item-${record.id}`,
          hidden: true,
        };
        itemRenderer(hiddenProps, dropIndex, record);
        result.push(
          <DropItem {...hiddenProps} />,
        );
      }
    }
    beforeRender();
    if (childList.length) {
      childList.forEach((current, index) => {
        if (dropIndex !== -1 && index >= dropIndex) {
          index += 1;
        }
        const dropItemProps = {
          ...commonProps,
          key: `drop-item-${current.id}`,
          index,
          onMouseEnter: this.handleDropItemEnter,
          onMouseDown: this.handleDropItemDown,
        };
        if (current === record) {
          dropItemProps.hidden = true;
        }
        itemRenderer(dropItemProps, dropIndex, current, index, width, height);
        result.push((
          <DropItem {...dropItemProps} />
        ));
      });
    } else if (emptyText) {
      const dropItemProps = {
        ...commonProps,
        key: 'drop-item-empty',
        index: 0,
        onMouseEnter: this.handleDropItemEnter,
        children: (
          <div className={`${prefixCls}-empty`}>
            {droppable ? '拖入此处' : emptyText}
          </div>
        ),
      };
      result.push(
        <DropItem {...dropItemProps} />,
      );
    }
    return result;
  }

  render() {
    const { prefixCls, droppable, className, layout, component: Cmp = 'div', ...restProps } = this.props;
    const { dropIndex } = this.state;
    const classString = classNames(prefixCls, className, {
      [`${prefixCls}-droppable`]: dropIndex > -1,
      [`${prefixCls}-drop-target`]: droppable,
      [`${prefixCls}-${layout}`]: layout,
    });
    const props = {
      ...omit(
        restProps,
        ['dndStore', 'componentsDS', 'record', 'itemRenderer', 'beforeRender', 'items', 'emptyText'],
      ),
      className: classString,
      onMouseUp: droppable ? this.handleMouseUp : noop,
      onMouseLeave: droppable ? this.handleMouseLeave : undefined,
    };
    return (
      <Cmp {...props}>
        {this.renderChildren()}
      </Cmp>
    );
  }
}
