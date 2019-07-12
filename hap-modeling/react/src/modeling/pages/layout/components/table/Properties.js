import React, { Component } from 'react';
import { Form, NumberField, Output, Select, Switch, TextField } from 'choerodon-ui/pro';
import { isTree } from '../../utils';
import defaultProps from './defaultProps';

export default function (record, ownerDataSet) {
  const { Option } = Select;

  function handleRowHeightChange(value) {
    if (value) {
      record.set('props.rowHeight', 'auto');
    } else {
      record.set('props.rowHeight', defaultProps().rowHeight);
    }
  }

  const autoRowHeight = record.get('props.rowHeight') === 'auto';

  const properties = [
    <Form key="base" header="基本属性">
      <Output key="datasetName" name="datasetName" record={ownerDataSet} label="数据源" />
      <TextField key="props.header" name="props.header" record={record} label="标题" />
      {
        ownerDataSet.get('selection') && (
          <Select key="props.selectionMode" name="props.selectionMode" record={record} label="选择模式" clearButton={false}>
            <Option value="rowbox">选择框选择</Option>
            <Option value="click">单击行选择</Option>
            <Option value="dblclick">双击行选择</Option>
          </Select>
        )
      }
      <Switch key="showQueryBar" name="props.showQueryBar" record={record} label="显示查询条" />
      <Switch key="autoRowHeight" checked={autoRowHeight} onChange={handleRowHeightChange} label="自动行高" />
      {!autoRowHeight && <NumberField key="props.rowHeight" name="props.rowHeight" record={record} label="行高" step={1} min={30} />}
    </Form>,
  ];
  if (isTree(ownerDataSet)) {
    const treePropertirs = [
      <Select key="props.mode" name="props.mode" record={record} label="展示模式" clearButton={false}>
        <Option value="list">普通列表</Option>
        <Option value="tree">树形列表</Option>
      </Select>,
    ];
    if (record.get('props.mode') === 'tree') {
      treePropertirs.push(
        <Switch key="props.defaultRowExpanded" name="props.defaultRowExpanded" record={record} label="默认展开" />,
        <Switch key="props.expandRowByClick" name="props.expandRowByClick" record={record} label="点击行展开" />,
        <NumberField key="props.indentSize" name="props.indentSize" record={record} label="缩进" />,
      );
    }
    properties.push(
      <Form key="tree" header="树形属性">
        {treePropertirs}
      </Form>,
    );
  }
  return properties;
}
