import React, { Children, cloneElement, isValidElement } from 'react';
import { inject, observer } from 'mobx-react';
import { action, get } from 'mobx';
import { Col, Icon, Row } from 'choerodon-ui/pro';
import autobind from 'choerodon-ui/pro/lib/decorator/autobind';
import { normalizeLabelWidth } from 'choerodon-ui/pro/lib/form/utils';
import BaseDesigner from '../index';
import { PREFIX } from '../../constants';
import DragZone from '../../dnd/DragZone';
import DropZone from '../../dnd/DropZone';
import Ruler from '../../view/Ruler';

@inject('dndStore', 'datasetsDS')
@observer
export default class FormDesigner extends BaseDesigner {
  static defaultProps = {
    prefixCls: `${PREFIX}-form`,
  };

  handleRulerChange = (value) => {
    this.props.record.set('props.labelWidth', value);
  };

  addColumn = () => {
    const { record } = this.props;
    const columns = record.get('props.columns');
    if (columns < 4) {
      record.set('props.columns', columns + 1);
    }
  };

  removeColumn = () => {
    const { record } = this.props;
    const columns = record.get('props.columns');
    if (columns > 1) {
      record.set('props.columns', columns - 1);
    }
  };

  beforeRenderDragItem = () => {
    this.rowCount = 0;
    this.colCount = 0;
    this.rows = [];
  };

  renderDragItem = (dropItemProps, dropIndex, child, index) => {
    const { record, dndStore: { dragItem } } = this.props;
    const columns = record.get('props.columns');
    const children = this.renderComponent(child);
    dropItemProps.component = Col;
    if (this.colCount >= columns) {
      this.colCount = 0;
      this.rowCount += 1;
    }
    const { colCount } = this;
    const dragRowSpan = (dragItem && dragItem.record.get('props.rowSpan')) || 1;
    const dragColSpan = (dragItem && dragItem.record.get('props.colSpan')) || 1;

    const rowSpan = child.get('props.rowSpan') || 1;
    let colSpan = child.get('props.colSpan') || 1;
    for (let i = 0; i < colSpan; i += 1) {
      let find = -1;
      for (let j = 0; j < rowSpan; j += 1) {
        const cols = this.rows[this.rowCount + j] || [];
        this.rows[this.rowCount + j] = cols;
        if (cols[this.colCount + i]) {
          if (find > -1) {
            colSpan = i;
            break;
          }
          this.colCount += 1;
        } else {
          find = i;
        }
        cols[this.colCount + i] = 1;
      }
      this.colCount += 1;

      dropItemProps.span = 24 / columns * colSpan;
      if (children) {
        dropItemProps.children = cloneElement(children, { colCount });
      }
    }
    if (dropIndex !== -1 && index >= dropIndex) {
      if (this.colCount > columns - dragColSpan) {
        dropItemProps.style = { transform: `translate(${(dragColSpan - columns) * 100}%, 100%)` };
      } else {
        dropItemProps.style = { transform: `translateX(${dragColSpan * 100}%)` };
      }
      // else if (index === dropIndex - 1) {
      //   if (dropIndex % columns + colSpan === columns) {
      //     dropItemProps.style = { marginRight: pxToRem(width + 8) };
      //   }
      // }
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

  wrapComponent(component) {
    const { dataSet } = this.props;
    return cloneElement(component, { dataSet: component.props.dataSet || dataSet });
  }

  getButtons() {
    const { record } = this.props;
    const columns = record.get('props.columns');
    const buttons = [];
    if (columns < 4) {
      buttons.push(<Icon key="add" type="add_circle" onClick={this.addColumn} />);
    }
    if (columns > 1) {
      buttons.push(<Icon key="remove" type="remove_circle" onClick={this.removeColumn} />);
    }
    buttons.push(this.renderRemoveButton());
    return buttons;
  }

  render() {
    const { record, dataSet, dndStore, prefixCls } = this.props;
    const { children } = record;
    const labelLayout = record.get('props.labelLayout');
    const columns = record.get('props.columns');
    const labelWidth = normalizeLabelWidth(record.get('props.labelWidth'), columns);
    return (
      <div className={prefixCls}>
        <DragZone
          type="Component"
          record={record}
          buttons={this.getButtons()}
        >
          <div className={`${prefixCls}-header`}>
            {record.get('props.header') || dataSet.get('datasetName')}
          </div>
        </DragZone>
        {
          labelLayout === 'horizontal' && (
            <Ruler columns={columns} values={labelWidth} onChange={this.handleRulerChange} minWidth={50} maxWidth={200} />
          )
        }
        <DropZone
          items={children}
          record={record}
          droppable={dndStore.canDrop(['Field', 'FormItem'])}
          layout="horizontal"
          emptyText="请拖入左侧数据源字段"
          component={Row}
          gutter={10}
          itemRenderer={this.renderDragItem}
          beforeRender={this.beforeRenderDragItem}
        />
      </div>
    );
  }
}
