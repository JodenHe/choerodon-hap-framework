import React, { Component, createElement } from 'react';
import { action, computed, get } from 'mobx';
import { inject, observer } from 'mobx-react';
import { Button } from 'choerodon-ui/pro';
import { pxToRem } from 'choerodon-ui/pro/lib/util/UnitConvertor';
import uuid from 'uuid/v4';
import { $l } from 'choerodon-ui/pro/lib/locale-context';
import { PREFIX } from '../../constants';
import DropZone from '../../dnd/DropZone';
import { getDefaultProps } from '../mapping';
import BaseDesigner from '..';
import { getQueryFieldConfigByName } from '../../utils';

@inject('dndStore', 'componentsDS', 'datasetsDS')
@observer
export default class QueryBar extends BaseDesigner {
  static defaultProps = {
    prefixCls: `${PREFIX}-table-query-bar`,
  };

  @computed
  get itemMap() {
    const { record: { children } } = this.props;
    const result = {};
    return children ? children.reduce((map, child) => {
      if (child.get('type') === 'TableQueryField') {
        map[child.get('props.name')] = child;
      }
      return map;
    }, result) : result;
  }

  @computed
  get items() {
    const { dataSet, record: { children = [] } } = this.props;
    const queryFields = dataSet.getCascadeRecords('queryFields');
    return queryFields
      .map(field => children.find(child => child.get('props.name') === field.get('name') && child.get('type') === 'TableQueryField'))
      .filter(Boolean);
  }

  handleDrop = action((dragData, index) => {
    const { dataSet, record: { children = [] } } = this.props;
    const field = getQueryFieldConfigByName(dataSet, dragData.get('props.name'));
    const fieldDataSet = field.dataSet;
    const notCurrent = fieldDataSet.indexOf(field) === -1;
    const currentSnapshot = fieldDataSet.snapshot();
    if (notCurrent) {
      const snapshot = dataSet.dataSetSnapshot.queryFields;
      fieldDataSet.restore(snapshot);
    }
    const fromData = fieldDataSet.get(index);
    if (fromData !== field) {
      if (fromData) {
        const fromIndex = fieldDataSet.indexOf(fromData);
        fieldDataSet.splice(fromIndex, 0, field);
      } else {
        fieldDataSet.push(field);
      }
    }
    if (notCurrent) {
      dataSet.dataSetSnapshot.queryFields = fieldDataSet.snapshot();
      fieldDataSet.restore(currentSnapshot);
    }
    return false;
  });

  renderDragItem = (dropItemProps, dropIndex, child, index, width) => {
    dropItemProps.children = this.renderComponent(child);
    if (dropIndex !== -1 && index >= dropIndex) {
      dropItemProps.style = { transform: `translateX(${pxToRem(width || 100)})` };
    }
  };

  addQueryField = () => {
    const { record } = this.props;
    const queryFieldsLimit = record.get('props.queryFieldsLimit') || 1;
    record.set('props.queryFieldsLimit', queryFieldsLimit + 1);
  };

  removeQueryField = () => {
    const { record } = this.props;
    const queryFieldsLimit = record.get('props.queryFieldsLimit') || 1;
    record.set('props.queryFieldsLimit', queryFieldsLimit - 1);
  };

  getCurrentFields(queryFieldsLimit) {
    const { prefixCls, record, dndStore: { dragItem } } = this.props;
    const data = dragItem && get(dragItem, 'record');
    const type = dragItem && get(dragItem, 'type');
    const { items } = this;
    const droppable = data && type === 'TableQueryField' && items.indexOf(data) !== -1;
    return (
      <DropZone
        className={`${prefixCls}-fields`}
        items={items.slice(0, queryFieldsLimit)}
        record={record}
        droppable={droppable}
        layout="horizontal"
        itemRenderer={this.renderDragItem}
        onDrop={this.handleDrop}
      />
    );
  }

  @action
  syncQueryFields() {
    const { props: { dataSet, componentsDS, record } } = this;
    const queryFields = dataSet.getCascadeRecords('queryFields');
    const items = { ...this.itemMap };
    const { current } = componentsDS;
    queryFields.forEach((field) => {
      const name = field.get('name');
      if (items[name]) {
        delete items[name];
      } else {
        const type = 'TableQueryField';
        const id = uuid();
        const parentId = record.get('id');
        componentsDS.create({
          type, id, parentId, dataSet: dataSet.get('datasetId'), props: { ...getDefaultProps(type, dataSet), name },
        });
      }
    });

    Object.keys(items).forEach(key => componentsDS.remove(items[key]));
    componentsDS.current = current;
  }

  componentDidMount() {
    this.syncQueryFields();
  }

  componentDidUpdate() {
    this.syncQueryFields();
  }

  render() {
    const { prefixCls, record, dataSet } = this.props;
    const showQueryBar = record.get('props.showQueryBar');
    if (showQueryBar) {
      const queryFields = dataSet.getCascadeRecords('queryFields');
      if (queryFields) {
        const { length } = queryFields;
        if (length) {
          const queryFieldsLimit = record.get('props.queryFieldsLimit') || 1;
          const more = length > queryFieldsLimit && (
            <Button className={`${prefixCls}-more`} color="blue" funcType="flat" tabIndex={-1}>{$l('Table', 'advanced_search')}</Button>
          );
          return (
            <div className={prefixCls}>
              <Button icon="add" disabled={queryFieldsLimit === length} onClick={this.addQueryField} />
              <Button icon="remove" disabled={queryFieldsLimit === 1} onClick={this.removeQueryField} />
              {this.getCurrentFields(queryFieldsLimit)}
              <Button className={`${prefixCls}-query`} color="blue" tabIndex={-1}>{$l('Table', 'query_button')}</Button>
              {more}
            </div>
          );
        }
      }
    }
    return null;
  }
}
