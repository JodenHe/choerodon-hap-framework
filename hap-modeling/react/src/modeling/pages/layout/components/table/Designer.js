import React, { cloneElement } from 'react';
import { action, get } from 'mobx';
import { inject, observer } from 'mobx-react';
import autobind from 'choerodon-ui/pro/lib/_util/autobind';
import { pxToRem } from 'choerodon-ui/pro/lib/_util/UnitConvertor';
import BaseDesigner from '..';
import { PREFIX } from '../../constants';
import Ruler from '../../view/Ruler';
import DropZone from '../../dnd/DropZone';
import DragZone from '../../dnd/DragZone';
import Bar from './Bar';
import Commander from './Commander';

@inject('dndStore', 'datasetsDS')
@observer
export default class TableDesigner extends BaseDesigner {
  static defaultProps = {
    prefixCls: `${PREFIX}-table`,
  };

  renderDragItem = (dropItemProps, dropIndex, child, index, width) => {
    dropItemProps.children = this.renderComponent(child);
    if (dropIndex !== -1 && index >= dropIndex) {
      dropItemProps.style = { transform: `translateX(${pxToRem(width || 150)})` };
    }
  };

  @autobind
  handleRemove() {
    const { record: { parent } } = this.props;
    super.handleRemove();
    if (parent && !parent.children) {
      parent.dataSet.remove(parent);
    }
  }

  wrapComponent(component, record) {
    const { dataSet } = this.props;
    return cloneElement(component, { dataSet });
  }

  getItems() {
    const { record: { children } } = this.props;
    return children.filter(child => child.get('type') === 'TableItem');
  }

  render() {
    const { record, dataSet, dndStore: { dragItem }, prefixCls } = this.props;
    const { parent } = record;
    const children = this.getItems();
    const data = dragItem && get(dragItem, 'record');
    const type = dragItem && get(dragItem, 'type');
    const droppable = data && type && (
      type === 'Field' ? dataSet.getCascadeRecords('fields').indexOf(data) !== -1
        && (!children || children.every(child => child.get('props.name') !== data.get('name')))
        : type === 'TableItem' ? children.indexOf(data) !== -1 : false
    );
    return (
      <div className={prefixCls}>
        <DragZone
          type="Component"
          record={record}
          draggable={!parent || !parent.parent || parent.parent.children.length > 1}
          buttons={this.renderRemoveButton()}
        >
          <div className={`${prefixCls}-header`}>
            {record.get('props.header') || dataSet.get('datasetName')}
          </div>
        </DragZone>
        <Bar dataSet={dataSet} record={record} />
        <div className={`${prefixCls}-columns-wrapper`}>
          <div className={`${prefixCls}-columns`}>
            <Ruler records={children} />
            <DropZone
              items={children}
              record={record}
              droppable={droppable}
              layout="horizontal"
              emptyText="请拖入左侧数据源字段"
              itemRenderer={this.renderDragItem}
            />
          </div>
        </div>
        <Commander dataSet={dataSet} record={record} />
      </div>
    );
  }
}
